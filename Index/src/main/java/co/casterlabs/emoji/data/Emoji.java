package co.casterlabs.emoji.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import co.casterlabs.emoji.generator.WebUtil;
import co.casterlabs.rakurai.json.annotating.JsonClass;
import co.casterlabs.rakurai.json.annotating.JsonExclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@JsonClass(exposeAll = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Emoji {
    private static final List<String> JOINER_WORDS = Arrays.asList("with", "and", "in");

    @ToString.Exclude
    private String regex;

    private String categoryId;
    private String subcategoryId;

    private String name;
    private String identifier;
    private String shortcode;

    private @ToString.Exclude List<Variation> variations;

    @JsonExclude
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    private Map<String, Variation> variationsMap = new HashMap<>();

    // Gets called by EmojiCategory
    protected void $validate() {
        Collections.reverse(this.variations); // Move the default to the bottom.

        this.shortcode = String.format(
            ":%s:",
            this.name
                .replace(' ', '_')
                .toLowerCase()
        );

        StringBuilder regexBuilder = new StringBuilder();

        regexBuilder.append('(').append(this.shortcode).append(')');

        for (Variation variation : this.variations) {
            this.variationsMap.put(variation.type, variation);
            regexBuilder.append('|').append(variation.regex);
        }

        this.regex = regexBuilder.toString();

        Collections.reverse(this.variations); // Restore the order.

        this.variations = Collections.unmodifiableList(this.variations);
        this.variationsMap = Collections.unmodifiableMap(this.variationsMap);
    }

    @Deprecated
    public Emoji() {} // For Rson.

//    @Override
//    public String toString() {
//        return String.format("Emoji(%s)", this.name);
//    }

    public static Emoji from(
        String categoryId,
        String subcategoryId,
        /* name */
        String identifier,
        /* shortcode */
        List<Variation> variations
    ) {

        StringBuilder name_sb = new StringBuilder();

        for (String word : identifier.split(" ")) {
            name_sb.append(' ');

            if (JOINER_WORDS.contains(word)) {
                name_sb.append(word);
            } else {
                name_sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)); // Capitialize the first letter.
            }
        }

        name_sb.deleteCharAt(0); // Remove additional space

        return new Emoji(
            null,
            categoryId,
            subcategoryId,
            name_sb.toString(),
            identifier,
            null,
            variations,
            new HashMap<>()
        );
    };

    @Getter
    @JsonClass(exposeAll = true)
    public static class Variation {
        private static final Pattern SPECIAL_REGEX_CHARS = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]");

        private String identifier;
        private String name;
        private String type;
        private double since;

        private String[] codeSequence;
        private String sequence;
        private String regex;

        private EmojiAssets assets;

        @Deprecated
        public Variation() {} // For Rson.

        public Variation(String identifier, String name, String type, double since, String[] codeSequence, String sequence) {
            this.identifier = identifier;
            this.name = name;
            this.type = type;
            this.since = since;
            this.sequence = sequence;
            this.codeSequence = codeSequence;
            this.regex = '(' + SPECIAL_REGEX_CHARS.matcher(this.sequence).replaceAll("\\\\$0") + ')';
            this.assets = new EmojiAssets(this);
        }

        @Override
        public String toString() {
            return this.name;
        }

        public String toHTML(@NonNull String provider) {
            if (provider.equals("system")) {
                return String.format(
                    "<span title='%s' data-type='system_emoji'>%s</span>",
                    WebUtil.escapeHtml(this.name),
                    this.sequence
                );
            }

            EmojiAssets.AssetImageProvider.AssetImageSet imageSet = this.assets.getAsset(provider);

            if ((imageSet != null) && imageSet.isSupported()) {
                return String.format(
                    "<img title='%s' alt='%s' src='%s' data-type='emoji' style='height: 1em; width: auto; display: inline-block; vertical-align: middle;' />",
                    WebUtil.escapeHtml(this.name),     // "vulcan salute: light skin tone"
                    WebUtil.escapeHtml(this.sequence), // the actual emoji.
                    imageSet.getSvgUrl()
                );
            } else {
                return String.format(
                    "<span title='%s' data-type='unsupported_emoji'>%s</span>",
                    WebUtil.escapeHtml(this.name),
                    this.sequence
                );
            }
        }

    }

    public static enum Qualification {
        FULLY_QUALIFIED,

        // These are excluded from indexing.
        MINIMALLY_QUALIFIED,
        COMPONENT,
        UNQUALIFIED;

        public static Qualification from(@NonNull String str) {
            return valueOf(
                str
                    .replace('-', '_') // fully-qualified -> fully_qualified
                    .toUpperCase()     // fully_qualified -> FULLY_QUALIFIED
            );
        }

    }

}
