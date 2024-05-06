package co.casterlabs.emoji.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.commons.functional.tuples.Pair;
import co.casterlabs.rakurai.json.annotating.JsonClass;
import co.casterlabs.rakurai.json.annotating.JsonExclude;
import co.casterlabs.rakurai.json.validation.JsonValidate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@JsonClass(exposeAll = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmojiCategory {

    @JsonExclude
    @ToString.Exclude
    private String regex;

    private String id;
    private String name;
    private List<Emoji> emojis;
    private List<String> subcategories;

    @JsonExclude
    @ToString.Exclude
    private List<Emoji.Variation> allEmojiVariations = new LinkedList<>();

    @JsonExclude
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    private Map<String, Emoji> emojisMap = new HashMap<>();

    @JsonExclude
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    private Map<String, Pair<Emoji, Emoji.Variation>> variationsMap = new HashMap<>();

    @JsonExclude
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    private Map<String, List<Emoji>> subcategoriesMap = new HashMap<>();

    @JsonValidate
    private void $validate() {
        StringBuilder regexBuilder = new StringBuilder();

        for (Emoji emoji : this.emojis) {
            emoji.$validate();

            regexBuilder.append('|').append(emoji.getRegex());

            this.emojisMap.put(emoji.getIdentifier(), emoji);

            this.allEmojiVariations.addAll(emoji.getVariations());

            for (Emoji.Variation variation : emoji.getVariations()) {
                this.variationsMap.put(variation.getSequence(), new Pair<>(emoji, variation));
            }

            // Add it to it's subcategory.
            List<Emoji> subcategoryEmojis = this.subcategoriesMap.get(emoji.getSubcategoryId());

            // Create the subcategory if it doesn't exist.
            if (subcategoryEmojis == null) {
                subcategoryEmojis = new LinkedList<>();

                this.subcategoriesMap.put(emoji.getSubcategoryId(), subcategoryEmojis);
            }

            subcategoryEmojis.add(emoji);
        }

        // Make the subcategories unmodifiable.
        for (Map.Entry<String, List<Emoji>> entry : this.subcategoriesMap.entrySet()) {
            entry.setValue(Collections.unmodifiableList(entry.getValue()));
        }

        this.regex = regexBuilder
            .deleteCharAt(0) // Remove additional '|'
            .toString();

        this.allEmojiVariations = Collections.unmodifiableList(this.allEmojiVariations);
        this.subcategories = Collections.unmodifiableList(new ArrayList<>(this.subcategoriesMap.keySet()));
        this.variationsMap = Collections.unmodifiableMap(this.variationsMap);
        this.emojis = Collections.unmodifiableList(this.emojis);
    }

    @Deprecated
    public EmojiCategory() {} // For Rson.

    public @Nullable Emoji getEmoji(@NonNull String identifier) {
        return this.emojisMap.get(identifier);
    }

    public @Nullable List<Emoji> getEmojisForSubCategory(@NonNull String subcategory) {
        return this.subcategoriesMap.get(subcategory);
    }

    public int size() {
        return this.emojis.size();
    }

    public static EmojiCategory from(
        /* id */
        String name,
        List<Emoji> emojis
    ) {

        String id = nameToId(name);

        EmojiCategory ec = new EmojiCategory();

        ec.id = id;
        ec.name = name;
        ec.emojis = emojis;
        ec.$validate();

        return ec;
    }

    public static String nameToId(@NonNull String name) {
        return name
            .replace(" ", "-")   // "Smileys & Emotion" -> "Smileys-&-Emotion"
            .replace("&", "and") // "Smileys-&-Emotion" -> "Smileys-and-Emotion"
            .toLowerCase();    // "Smileys-and-Emotion" -> "smileys-and-emotion"
    }

}
