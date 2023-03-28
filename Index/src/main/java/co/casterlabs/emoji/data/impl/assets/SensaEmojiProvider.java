package co.casterlabs.emoji.data.impl.assets;

import java.util.HashMap;
import java.util.Map;

import co.casterlabs.emoji.data.Emoji.Variation;
import co.casterlabs.emoji.generator.WebUtil;
import co.casterlabs.emoji.data.EmojiAssets;

public class SensaEmojiProvider extends EmojiAssets.AssetImageProvider {
    private static final Map<String, String> skinToneMapping = new HashMap<>();

    static {
        // @formatter:off
        skinToneMapping.put(       "light skin tone", " skin 1");
        skinToneMapping.put("medium-light skin tone", " skin 2");
        skinToneMapping.put(      "medium skin tone", " skin 3");
        skinToneMapping.put( "medium-dark skin tone", " skin 4");
        skinToneMapping.put(        "dark skin tone", " skin 5");
        // @formatter:on
    }

    public SensaEmojiProvider() {
        super(
            "Sensa",
            "sensa-emoji",
            "https://sensa.co/emoji",
            2.0 // This was guessed, the set could still have less than this though.
        );
    }

    @Override
    protected AssetImageSet produce0(Variation variation) {
        String sensaFormat = variation.getIdentifier();
        sensaFormat = sensaFormat.substring(0, 1).toUpperCase() + sensaFormat.substring(1); // Capitialize the first letter.

        // Parse skin colors
        // Format is as follows: "Vulcan salute skin 1.svg" (1-5)
        String skinTone = skinToneMapping.get(variation.getType());
        if (skinTone != null) {
            sensaFormat += skinTone;
        }

        sensaFormat = WebUtil.encodeURI(sensaFormat);

        return new AssetImageSet(
            this,
            String.format("https://cdn.casterlabs.co/emoji-cdn/sensa/png/2x/%s.png", sensaFormat),
            String.format("https://cdn.casterlabs.co/emoji-cdn/sensa/svg/%s.svg", sensaFormat)
        );
    }

}
