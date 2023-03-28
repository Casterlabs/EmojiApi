package co.casterlabs.emoji.data.impl.assets;

import co.casterlabs.emoji.data.Emoji.Variation;
import co.casterlabs.emoji.data.EmojiAssets;

public class TossFaceProvider extends EmojiAssets.AssetImageProvider {

    public TossFaceProvider() {
        super(
            "Toss Face",
            "toss-face",
            "https://toss.im/tossface",
            14.0 // https://github.com/toss/tossface/releases/tag/v1.3
        );
    }

    @Override
    protected AssetImageSet produce0(Variation variation) {
        StringBuilder unicodeformat = new StringBuilder();

        for (String code : variation.getCodeSequence()) {
            unicodeformat.append("_u").append(code.toUpperCase());
        }

        return new AssetImageSet(
            this,
            null,
            String.format("https://raw.githubusercontent.com/toss/tossface/main/dist/svg/%s.svg", unicodeformat.substring(1) /* drop leading _ */)
        );
    }

}
