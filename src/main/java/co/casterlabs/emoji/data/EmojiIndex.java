package co.casterlabs.emoji.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.emoji.WebUtil;
import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.annotating.JsonField;
import co.casterlabs.rakurai.json.validation.JsonValidate;
import kotlin.Pair;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
public class EmojiIndex {

    @ToString.Exclude
    private String json;

    @ToString.Exclude
    private String regex;

    @JsonField
    private String version;

    @JsonField
    private List<EmojiCategory> categories;

    private List<Emoji> allEmojis = new LinkedList<>();
    private List<Emoji.Variation> allEmojiVariations = new LinkedList<>();
    private @ToString.Exclude Map<String, EmojiCategory> categoryMap = new HashMap<>();
    private @ToString.Exclude Map<String, List<Emoji>> subcategoriesMap = new HashMap<>();
    private @ToString.Exclude Map<String, Emoji> emojisMap = new HashMap<>();
    private @ToString.Exclude Map<String, Pair<Emoji, Emoji.Variation>> variationsMap = new HashMap<>();

    @Deprecated
    public EmojiIndex() {} // For Rson.

    public EmojiIndex(String version, List<EmojiCategory> categories) {
        this.version = version;
        this.categories = categories;
        this.$validate();
    }

    @JsonValidate
    private void $validate() {
        StringBuilder regexBuilder = new StringBuilder();

        for (EmojiCategory category : this.categories) {
            regexBuilder.append('|').append(category.getRegex());

            this.categoryMap.put(category.getId(), category);

            this.allEmojis.addAll(category.getEmojis());

            for (Emoji emoji : category.getEmojis()) {
                this.emojisMap.put(emoji.getIdentifier(), emoji);
                this.allEmojiVariations.addAll(emoji.getVariations());

                this.variationsMap.put(emoji.getShortcode(), new Pair<>(emoji, emoji.getVariations().get(0)));

                for (Emoji.Variation variation : emoji.getVariations()) {
                    this.variationsMap.put(variation.getSequence(), new Pair<>(emoji, variation));
                }
            }

            for (String subcategory : category.getSubcategories()) {
                this.subcategoriesMap.put(
                    subcategory,
                    category.getEmojisForSubCategory(subcategory)
                );
            }
        }

        this.regex = regexBuilder
            .deleteCharAt(0) // Remove additional '|'
            .toString();

        this.categories = Collections.unmodifiableList(this.categories);
        this.allEmojis = Collections.unmodifiableList(this.allEmojis);
        this.allEmojiVariations = Collections.unmodifiableList(this.allEmojiVariations);

        this.json = Rson.DEFAULT.toJson(this).toString(true);
    }

    public @Nullable EmojiCategory getCategoryById(@NonNull String id) {
        return this.categoryMap.get(id);
    }

    public @Nullable Emoji getEmoji(@NonNull String identifier) {
        return this.emojisMap.get(identifier);
    }

    public @Nullable Pair<Emoji, Emoji.Variation> getEmojiFromSequence(@NonNull String sequence) {
        return this.variationsMap.get(sequence);
    }

    public @Nullable List<Emoji> getEmojisForSubCategory(@NonNull String subcategory) {
        return this.subcategoriesMap.get(subcategory);
    }

    public Set<Pair<Emoji, Emoji.Variation>> matchAllEmojis(@NonNull String input) {
        Set<Pair<Emoji, Emoji.Variation>> emojis = new HashSet<>();

        Matcher m = Pattern.compile(this.regex, Pattern.UNICODE_CHARACTER_CLASS).matcher(input);
        while (m.find()) {
            String match = m.group();

            Pair<Emoji, Emoji.Variation> pair = this.getEmojiFromSequence(match);

            emojis.add(pair);
        }

        return emojis;
    }

    public Object[] matchAllEmojisAndReturnNodes(@NonNull String input) {
        String[] split;

        // This is our own split algorithm. Why? Because normal regex lookahead and
        // lookbehinds won't work properly.
        {
            List<String> splitList = new LinkedList<>();

            int lastStop = 0;

            Matcher m = Pattern.compile(this.regex, Pattern.UNICODE_CHARACTER_CLASS).matcher(input);
            while (m.find()) {
                int start = m.start();
                int end = m.end();

                if (start - lastStop > 0) { // There's text before this.
                    String text = input.substring(lastStop, start);
                    splitList.add(text);
                }

                lastStop = end;

                String detection = input.substring(start, end);
                splitList.add(detection);
            }

            // Make sure to get the bit we left off.
            if (lastStop < input.length()) {
                String end = input.substring(lastStop);
                splitList.add(end);
            }

            split = splitList.toArray(new String[0]);
        }

        Object[] result = new Object[split.length]; // We're going to be adding another type into here, so that's why it's Object[].

        for (int idx = 0; idx < result.length; idx++) {
            String part = split[idx];
            Pair<Emoji, Emoji.Variation> detection = this.getEmojiFromSequence(part);

            if (detection == null) { // Not an emoji.
                result[idx] = part;
            } else {
                Emoji.Variation variation = detection.getSecond();

                result[idx] = variation;
            }
        }

        return result;
    }

    public String matchAllEmojisAndReturnHtml(@NonNull String input, @NonNull String emojiProvider) {
        Object[] nodes = this.matchAllEmojisAndReturnNodes(input);
        StringBuilder htmlBuilder = new StringBuilder();

        for (Object node : nodes) {
            if (node instanceof String) {
                htmlBuilder.append(String.format("<span data-type='text'>%s</span>", WebUtil.escapeHtml((String) node)));
            } else {
                Emoji.Variation variation = (Emoji.Variation) node;

                EmojiAssets.AssetImageProvider.AssetImageSet imageSet = variation.getAssets().getAsset(emojiProvider);

                if (imageSet.isSupported()) {
                    htmlBuilder.append(
                        String.format(
                            "<img title='%s' alt='%s' src='%s' data-type='emoji' style='height: 1em; width: auto; display: inline-block; vertical-align: middle;' />",
                            WebUtil.escapeHtml(variation.getName()), // "vulcan salute: light skin tone"
                            WebUtil.escapeHtml(variation.getSequence()),   // the actual emoji.
                            imageSet.getSvgUrl()
                        )
                    );
                } else {
                    htmlBuilder.append(
                        String.format(
                            "<span title='%s' alt='%s' data-type='unsupported_emoji'>:%s:</span>",
                            WebUtil.escapeHtml(variation.getName()), // "vulcan salute: dark skin tone"
                            WebUtil.escapeHtml(variation.getSequence()),   // the actual emoji.
                            WebUtil.escapeHtml(variation.getIdentifier())
                        )
                    );
                }
            }
        }

        return htmlBuilder.toString();
    }

    @Override
    public String toString() {
        return String.format("EmojiIndex(%s)", this.categories);
    }

}
