package co.casterlabs.emoji.data.impl.assets;

import co.casterlabs.emoji.data.Emoji.Variation;
import co.casterlabs.emoji.data.EmojiAssets;

public class OpenMojiProvider extends EmojiAssets.AssetImageProvider {

    public OpenMojiProvider() {
        super(
            "OpenMoji",
            "openmoji",
            "https://openmoji.org",
            14.0 // https://openmoji.org
        );
    }

    @Override
    protected AssetImageSet produce0(Variation variation) {
        String unicodeformat = String.join("-", variation.getCodeSequence()).toUpperCase();

        return new AssetImageSet(
            this,
            String.format("https://github.com/hfg-gmuend/openmoji/raw/master/color/72x72/%s.png", unicodeformat),
            String.format("https://github.com/hfg-gmuend/openmoji/raw/master/color/svg/%s.svg", unicodeformat)
        );
    }

}
