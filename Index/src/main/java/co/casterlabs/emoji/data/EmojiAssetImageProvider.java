package co.casterlabs.emoji.data;

import co.casterlabs.rakurai.json.annotating.JsonClass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonClass(exposeAll = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class EmojiAssetImageProvider {
    private String providerName;
    String providerId;
    private String providerHomePage;
    private double emojiCompliance;

    public EmojiAssetImageSet produce(Emoji.Variation variation) {
        if (this.supports(variation.getSince())) {
            return this.produce0(variation);
        }

        return new EmojiAssetImageSet(this, null, null);
    }

    protected abstract EmojiAssetImageSet produce0(Emoji.Variation variation);

    public boolean supports(double version) {
        return this.emojiCompliance >= version;
    }

}