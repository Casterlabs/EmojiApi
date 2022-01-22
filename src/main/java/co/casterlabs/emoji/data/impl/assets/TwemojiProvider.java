package co.casterlabs.emoji.data.impl.assets;

import co.casterlabs.emoji.data.Emoji.Variation;
import co.casterlabs.emoji.data.EmojiAssets;

public class TwemojiProvider extends EmojiAssets.AssetImageProvider {

    public TwemojiProvider() {
        super(
            "Twemoji",
            "twemoji",
            "https://twemoji.twitter.com",
            13.1 // https://github.com/twitter/twemoji#:~:text=across%20all%20platforms.-,Twemoji%20v13.1,-adheres%20to%20the
        );
    }

    @Override
    protected AssetImageSet produce0(Variation variation) {
        String unicodeformat = String.join("-", variation.getCodeSequence());

        return new AssetImageSet(
            this,
            String.format("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/%s.png", unicodeformat),
            String.format("https://raw.githubusercontent.com/twitter/twemoji/master/assets/svg/%s.svg", unicodeformat)
        );
    }

}
