package co.casterlabs.emoji.data.impl.assets;

import co.casterlabs.emoji.data.EmojiAssetImageProvider;
import co.casterlabs.emoji.data.EmojiAssetImageSet;
import co.casterlabs.emoji.data.Emoji.Variation;

public class NotoEmojiProvider extends EmojiAssetImageProvider {

    public NotoEmojiProvider() {
        super(
            "Noto Emoji",
            "noto-emoji",
            "https://github.com/googlefonts/noto-emoji",
            15.0 // https://github.com/googlefonts/noto-emoji/releases/tag/v2.038
        );
    }

    @Override
    protected EmojiAssetImageSet produce0(Variation variation) {
        String unicodeformat = String.join("_", variation.getCodeSequence()).toLowerCase();

        return new EmojiAssetImageSet(
            this,
            String.format("https://raw.githubusercontent.com/googlefonts/noto-emoji/main/png/72/emoji_u%s.png", unicodeformat),
            String.format("https://raw.githubusercontent.com/googlefonts/noto-emoji/main/svg/emoji_u%s.svg", unicodeformat)
        );
    }

}
