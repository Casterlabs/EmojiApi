package co.casterlabs.emoji.data.impl.assets;

import co.casterlabs.emoji.data.Emoji.Variation;
import co.casterlabs.emoji.data.EmojiAssetImageProvider;
import co.casterlabs.emoji.data.EmojiAssetImageSet;

public class TossFaceProvider extends EmojiAssetImageProvider {

    public TossFaceProvider() {
        super(
            "Toss Face",
            "toss-face",
            "https://toss.im/tossface",
            14.0 // https://github.com/toss/tossface/releases/tag/v1.6
        );
    }

    @Override
    protected EmojiAssetImageSet produce0(Variation variation) {
        StringBuilder unicodeformat = new StringBuilder();

        for (String code : variation.getCodeSequence()) {
            unicodeformat.append("_u").append(code.toUpperCase());
        }

        return new EmojiAssetImageSet(
            this,
            null,
            String.format("https://raw.githubusercontent.com/toss/tossface/v1.6/dist/svg/%s.svg", unicodeformat.substring(1) /* drop leading _ */)
        );
    }

}
