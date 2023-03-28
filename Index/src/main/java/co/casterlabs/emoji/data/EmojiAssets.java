package co.casterlabs.emoji.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.emoji.data.Emoji.Variation;
import co.casterlabs.emoji.data.EmojiAssets.AssetImageProvider.AssetImageSet;
import co.casterlabs.emoji.data.impl.assets.NotoEmojiProvider;
import co.casterlabs.emoji.data.impl.assets.OpenMojiProvider;
import co.casterlabs.emoji.data.impl.assets.SensaEmojiProvider;
import co.casterlabs.emoji.data.impl.assets.TwemojiProvider;
import co.casterlabs.emoji.generator.EmojiIndexGenerator;
import co.casterlabs.emoji.generator.WebUtil;
import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.annotating.JsonClass;
import co.casterlabs.rakurai.json.annotating.JsonDeserializationMethod;
import co.casterlabs.rakurai.json.annotating.JsonExclude;
import co.casterlabs.rakurai.json.annotating.JsonSerializationMethod;
import co.casterlabs.rakurai.json.element.JsonElement;
import co.casterlabs.rakurai.json.serialization.JsonParseException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

// This class is manually created in Emoji.Variation, Rson never deserializes it.

@JsonClass(serializer = _EmojiAssetsSerializer.class)
public class EmojiAssets {
    public static final String DEFAULT_PROVIDER = "noto-emoji"; // They support the most.

    /* 
     * TODO Investigate https://decodeunicode.org/en/u+23F2
     * TODO Render & Host & Add SegoeUI Emoji https://docs.microsoft.com/en-us/typography/font-list/segoe-ui-emoji
     */
    public static final List<AssetImageProvider> emojiProviders = Arrays.asList(
        new NotoEmojiProvider(),
        new TwemojiProvider(),
        new OpenMojiProvider(),
        new SensaEmojiProvider()
    );

    // --

    @Getter(AccessLevel.PROTECTED)
    private Map<String, AssetImageSet> assets = new HashMap<>();

    public EmojiAssets(Variation variation) {
        for (AssetImageProvider provider : emojiProviders) {
            this.assets.put(provider.getProviderId(), provider.produce(variation));
        }
    }

    public EmojiAssets(Map<String, AssetImageSet> assets) {
        this.assets = assets; // Skips generation.
    }

    public @Nullable AssetImageSet getAsset(@NonNull String provider) {
        return this.assets.get(provider);
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
            }

            return new AssetImageSet(this, null, null);
        }

        protected abstract AssetImageSet produce0(Emoji.Variation variation);

        public boolean supports(double version) {
            return this.emojiCompliance >= version;
        }

        @Getter
        @ToString
        @JsonClass(exposeAll = true)
        public static class AssetImageSet {
            private @JsonExclude AssetImageProvider provider;

            private boolean supported;

            private @Nullable String pngUrl;
            private @Nullable String svgUrl;

            private @JsonExclude Future<?> validationFuture;

            @JsonDeserializationMethod("provider")
            private void $deserialize_provider(JsonElement e) throws JsonParseException {
                String providerId = e.getAsObject().getString("providerId");

                for (AssetImageProvider provider : emojiProviders) {
                    if (provider.providerId.equals(providerId)) {
                        this.provider = provider;
                        return;
                    }
                }

                throw new JsonParseException("Cannot find a provider with an id of " + providerId);
            }

            @JsonSerializationMethod("provider")
            private JsonElement $serialize_provider() {
                return Rson.DEFAULT.toJson(this.provider);
            }

            public AssetImageSet(AssetImageProvider provider, @Nullable String pngUrl, @Nullable String svgUrl) {
                this.provider = provider;
                this.pngUrl = pngUrl;
                this.svgUrl = svgUrl;
                this.supported = (this.pngUrl != null) || (this.svgUrl != null);

                // Note that this is not called during load() since it'll get deserialized
                // instead.
                this.validationFuture = EmojiIndexGenerator.getValidationThreadPool().submit(() -> {
                    boolean valid = WebUtil.doesContentExist(this.pngUrl) ||
                        WebUtil.doesContentExist(this.svgUrl);

                    if (!valid) {
                        this.pngUrl = null;
                        this.svgUrl = null;
                        this.supported = false;
                    }
                });
            }

            @Deprecated
            public AssetImageSet() {} // For Rson.

        }

    }

}
