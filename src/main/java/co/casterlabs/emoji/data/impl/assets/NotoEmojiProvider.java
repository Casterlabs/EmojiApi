package co.casterlabs.emoji.data.impl.assets;

import co.casterlabs.emoji.data.Emoji.Variation;
import co.casterlabs.emoji.data.EmojiAssets;

public class NotoEmojiProvider extends EmojiAssets.AssetImageProvider {

    public NotoEmojiProvider() {
        super(
            "Noto Emoji",
            "noto-emoji",
            "https://github.com/googlefonts/noto-emoji",
            14.0 // https://github.com/googlefonts/noto-emoji/releases/tag/v2.034
        );
    }

    @Override
    protected AssetImageSet produce0(Variation variation) {
        String unicodeformat = String.join("_", variation.getCodeSequence()).toLowerCase();

        return new AssetImageSet(
            this,
            String.format("https://raw.githubusercontent.com/googlefonts/noto-emoji/main/png/72/emoji_u%s.png", unicodeformat),
            String.format("https://raw.githubusercontent.com/googlefonts/noto-emoji/main/svg/emoji_u%s.svg", unicodeformat)
        );
    }

}
