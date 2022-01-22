package co.casterlabs.emoji.data;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.emoji.data.Emoji.Variation;
import co.casterlabs.rakurai.json.annotating.JsonClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonClass(exposeAll = true)
public class EmojiAssets {
    private AssetImages notoEmoji;
    private AssetImages twemoji;

    @Deprecated
    public EmojiAssets() {} // For Rson.

    public EmojiAssets(Variation variation) {

        this.notoEmoji = new AssetImages(
            "https://twemoji.twitter.com",
            String.format("https://raw.githubusercontent.com/googlefonts/noto-emoji/main/png/72/emoji_u%s.png", String.join("_", variation.getCodeSequence())),
            String.format("https://raw.githubusercontent.com/googlefonts/noto-emoji/main/svg/emoji_u%s.svg", String.join("_", variation.getCodeSequence()))
        );

        this.twemoji = new AssetImages(
            "https://github.com/googlefonts/noto-emoji",
            String.format("https://raw.githubusercontent.com/twitter/twemoji/master/assets/72x72/%s.png", String.join("-", variation.getCodeSequence())),
            String.format("https://raw.githubusercontent.com/twitter/twemoji/master/assets/svg/%s.svg", String.join("-", variation.getCodeSequence()))
        );

        // TODO Host & Add https://sensa.co/emoji/
        // TODO Host & Add https://openmoji.org/
        // TODO Investigate https://decodeunicode.org/en/u+23F2
        // TODO Render & Host & Add SegoeUI Emoji
        // https://docs.microsoft.com/en-us/typography/font-list/segoe-ui-emoji

    }

    @Getter
    @ToString
    @AllArgsConstructor
    @JsonClass(exposeAll = true)
    public static class AssetImages {
        private String source;

        private @Nullable String pngUrl;
        private @Nullable String svgUrl;

        @Deprecated
        public AssetImages() {} // For Rson.

    }

}
