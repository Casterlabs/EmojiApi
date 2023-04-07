package co.casterlabs.emoji.data;

import java.util.concurrent.Future;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.emoji.generator.EmojiIndexGenerator;
import co.casterlabs.emoji.generator.WebUtil;
import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.annotating.JsonClass;
import co.casterlabs.rakurai.json.annotating.JsonDeserializationMethod;
import co.casterlabs.rakurai.json.annotating.JsonExclude;
import co.casterlabs.rakurai.json.annotating.JsonSerializationMethod;
import co.casterlabs.rakurai.json.element.JsonElement;
import co.casterlabs.rakurai.json.serialization.JsonParseException;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonClass(exposeAll = true)
public class EmojiAssetImageSet {
    private @JsonExclude EmojiAssetImageProvider provider;

    private boolean supported;

    private @Nullable String pngUrl;
    private @Nullable String svgUrl;

    private @JsonExclude Future<?> validationFuture;

    @JsonDeserializationMethod("provider")
    private void $deserialize_provider(JsonElement e) throws JsonParseException {
        String providerId = e.getAsObject().getString("providerId");

        for (EmojiAssetImageProvider provider : EmojiAssets.emojiProviders) {
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

    public EmojiAssetImageSet(EmojiAssetImageProvider provider, @Nullable String pngUrl, @Nullable String svgUrl) {
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
    public EmojiAssetImageSet() {} // For Rson.

}