package co.casterlabs.emoji.data.impl.assets;

import co.casterlabs.emoji.data.Emoji.Variation;
import co.casterlabs.emoji.data.EmojiAssets;

public class OpenMojiProvider extends EmojiAssets.AssetImageProvider {

    public OpenMojiProvider() {
        super(
            "OpenMoji",
            "openmoji",
            "https://openmoji.org",
            13.1 // https://openmoji.org
        );
    }

    @Override
    protected AssetImageSet produce0(Variation variation) {
        String unicodeformat = String.join("-", variation.getCodeSequence()).toUpperCase();

        return new AssetImageSet(
            this,
            String.format("https://cdn.casterlabs.co/emoji-cdn/openmoji/png-72x/%s.png", unicodeformat),
            String.format("https://cdn.casterlabs.co/emoji-cdn/openmoji/svg/%s.svg", unicodeformat)
        );
    }

}
