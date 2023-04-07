package co.casterlabs.emoji.data.impl.assets;

import co.casterlabs.emoji.data.EmojiAssetImageProvider;
import co.casterlabs.emoji.data.EmojiAssetImageSet;
import co.casterlabs.emoji.data.Emoji.Variation;

public class TossFaceProvider extends EmojiAssetImageProvider {

    public TossFaceProvider() {
        super(
            "Toss Face",
            "toss-face",
            "https://toss.im/tossface",
            14.0 // https://github.com/toss/tossface/releases/tag/v1.3
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
            String.format("https://raw.githubusercontent.com/toss/tossface/main/dist/svg/%s.svg", unicodeformat.substring(1) /* drop leading _ */)
        );
    }

}
