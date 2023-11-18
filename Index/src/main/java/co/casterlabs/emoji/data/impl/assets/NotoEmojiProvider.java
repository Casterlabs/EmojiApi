package co.casterlabs.emoji.data.impl.assets;

import co.casterlabs.emoji.data.Emoji.Variation;
import co.casterlabs.emoji.data.EmojiAssetImageProvider;
import co.casterlabs.emoji.data.EmojiAssetImageSet;

public class NotoEmojiProvider extends EmojiAssetImageProvider {

    public NotoEmojiProvider() {
        super(
            "Noto Emoji",
            "noto-emoji",
            "https://github.com/googlefonts/noto-emoji",
            15.1 // https://github.com/googlefonts/noto-emoji/releases/tag/v2.040
        );
    }

    @Override
    protected EmojiAssetImageSet produce0(Variation variation) {
        String unicodeformat = String.join("_", variation.getCodeSequence()).toLowerCase();

        return new EmojiAssetImageSet(
            this,
            String.format("https://raw.githubusercontent.com/googlefonts/noto-emoji/v2.040/png/72/emoji_u%s.png", unicodeformat),
            String.format("https://raw.githubusercontent.com/googlefonts/noto-emoji/v2.040/svg/emoji_u%s.svg", unicodeformat)
        );
    }

}
