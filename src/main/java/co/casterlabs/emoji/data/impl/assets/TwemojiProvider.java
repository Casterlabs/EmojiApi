package co.casterlabs.emoji.data.impl.assets;

import co.casterlabs.emoji.data.Emoji.Variation;
import co.casterlabs.emoji.data.EmojiAssets;

public class TwemojiProvider extends EmojiAssets.AssetImageProvider {

    public TwemojiProvider() {
        super(
            "Twemoji",
            "twemoji",
            "https://github.com/jdecked/twemoji",
            14.0 // https://github.com/jdecked/twemoji#:~:text=Twemoji%20v14.0%20adheres%20to%20the%20Unicode%2014.0%20spec
        );
    }

    @Override
    protected AssetImageSet produce0(Variation variation) {
        String unicodeformat = String.join("-", variation.getCodeSequence()).toLowerCase();

        return new AssetImageSet(
            this,
            String.format("https://raw.githubusercontent.com/jdecked/twemoji/master/assets/72x72/%s.png", unicodeformat),
            String.format("https://raw.githubusercontent.com/jdecked/twemoji/master/assets/svg/%s.svg", unicodeformat)
        );
    }

}
