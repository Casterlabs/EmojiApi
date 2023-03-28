package co.casterlabs.emoji.data.impl.assets;

import co.casterlabs.emoji.data.Emoji.Variation;
import co.casterlabs.emoji.data.EmojiAssets;

public class AppleEmojiProvider extends EmojiAssets.AssetImageProvider {

    public AppleEmojiProvider() {
        super(
            "Apple",
            "apple-emoji",
            "https://apple.com",
            15.0 // https://emojipedia.org/apple
        );
    }

    @Override
    protected AssetImageSet produce0(Variation variation) {
        return new AssetImageSet(
            this,
            null,
            null
        );
    }

}
