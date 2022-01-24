package co.casterlabs.emoji.data.impl.assets;

import co.casterlabs.emoji.WebUtil;
import co.casterlabs.emoji.data.Emoji.Variation;
import co.casterlabs.emoji.data.EmojiAssets;

public class SensaEmojiProvider extends EmojiAssets.AssetImageProvider {

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

        sensaFormat = WebUtil.encodeURI(sensaFormat);

        // TODO parse skin colors
        // Format is as follows: "Vulcan salute skin 1.svg" (1-5)

        return new AssetImageSet(
            this,
            String.format("https://cdn.casterlabs.co/emoji-cdn/sensa/png/2x/%s.png", sensaFormat),
            String.format("https://cdn.casterlabs.co/emoji-cdn/sensa/svg/%s.svg", sensaFormat)
        );
    }

}
