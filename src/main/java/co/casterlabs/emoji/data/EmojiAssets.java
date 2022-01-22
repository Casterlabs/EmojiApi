package co.casterlabs.emoji.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.emoji.data.Emoji.Variation;
import co.casterlabs.emoji.data.EmojiAssets.AssetImageProvider.AssetImageSet;
import co.casterlabs.emoji.data.impl.assets.NotoEmojiProvider;
import co.casterlabs.emoji.data.impl.assets.OpenMojiProvider;
import co.casterlabs.emoji.data.impl.assets.TwemojiProvider;
import co.casterlabs.rakurai.json.annotating.JsonClass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

// This class is manually created in Emoji.Variation, Rson never deserializes it.

@Getter
@JsonClass(serializer = _EmojiAssetsSerializer.class)
public class EmojiAssets {

    /* 
     * TODO Host & Add https://sensa.co/emoji/
     * TODO Host & Add https://openmoji.org/
     * TODO Investigate https://decodeunicode.org/en/u+23F2
     * TODO Render & Host & Add SegoeUI Emoji https://docs.microsoft.com/en-us/typography/font-list/segoe-ui-emoji
     */
    public static final List<AssetImageProvider> emojiProviders = Arrays.asList(
        new NotoEmojiProvider(),
        new TwemojiProvider(),
        new OpenMojiProvider()
    );

    private Map<String, AssetImageSet> assets = new HashMap<>();

    public EmojiAssets(Variation variation) {
        for (AssetImageProvider provider : emojiProviders) {
            this.assets.put(provider.getProviderId(), provider.produce(variation));
        }
    }

    @Override
    public String toString() {
        return this.assets.toString();
    }

    @Getter
    @ToString
    @JsonClass(exposeAll = true)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static abstract class AssetImageProvider {
        private String providerName;
        private String providerId;
        private String providerHomePage;
        private double emojiCompliance;

        public AssetImageSet produce(Emoji.Variation variation) {
            if (this.supports(variation.getSince())) {
                return this.produce0(variation);
            } else {
                return new AssetImageSet(this, null, null);
            }
        }

        protected abstract AssetImageSet produce0(Emoji.Variation variation);

        public boolean supports(double version) {
            return this.emojiCompliance >= version;
        }

        @Getter
        @ToString
        @JsonClass(exposeAll = true)
        public static class AssetImageSet {
            private AssetImageProvider provider;

            private boolean supported;

            private @Nullable String pngUrl;
            private @Nullable String svgUrl;

            public AssetImageSet(AssetImageProvider provider, @Nullable String pngUrl, @Nullable String svgUrl) {
                this.provider = provider;
                this.pngUrl = pngUrl;
                this.svgUrl = svgUrl;
                this.supported = (this.pngUrl != null) || (this.svgUrl != null);
            }
        }

    }

}
