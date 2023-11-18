package co.casterlabs.emoji.data.impl.assets;

import co.casterlabs.emoji.data.Emoji.Variation;
import co.casterlabs.emoji.data.EmojiAssetImageProvider;
import co.casterlabs.emoji.data.EmojiAssetImageSet;

public class OpenMojiProvider extends EmojiAssetImageProvider {

    public OpenMojiProvider() {
        super(
            "OpenMoji",
            "openmoji",
            "https://openmoji.org",
            14.0 // https://openmoji.org
        );
    }

    @Override
    protected EmojiAssetImageSet produce0(Variation variation) {
        String unicodeformat = String.join("-", variation.getCodeSequence()).toUpperCase();

        return new EmojiAssetImageSet(
            this,
            String.format("https://github.com/hfg-gmuend/openmoji/raw/14.0.0/color/72x72/%s.png", unicodeformat),
            String.format("https://github.com/hfg-gmuend/openmoji/raw/14.0.0/color/svg/%s.svg", unicodeformat)
        );
    }

}
