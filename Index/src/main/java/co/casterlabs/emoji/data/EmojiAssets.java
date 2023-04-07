package co.casterlabs.emoji.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.emoji.data.Emoji.Variation;
import co.casterlabs.emoji.data.impl.assets.NotoEmojiProvider;
import co.casterlabs.emoji.data.impl.assets.OpenMojiProvider;
import co.casterlabs.emoji.data.impl.assets.SensaEmojiProvider;
import co.casterlabs.emoji.data.impl.assets.TossFaceProvider;
import co.casterlabs.emoji.data.impl.assets.TwemojiProvider;
import co.casterlabs.rakurai.json.annotating.JsonClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

// This class is manually created in Emoji.Variation, Rson never deserializes it.

@JsonClass(serializer = _EmojiAssetsSerializer.class)
public class EmojiAssets {
    public static final String DEFAULT_PROVIDER = "noto-emoji"; // They support the most.

    /* 
     * TODO Investigate https://decodeunicode.org/en/u+23F2
     * TODO Render & Host & Add SegoeUI Emoji https://docs.microsoft.com/en-us/typography/font-list/segoe-ui-emoji
     */
    public static final List<EmojiAssetImageProvider> emojiProviders = Arrays.asList(
        new NotoEmojiProvider(),
        new TwemojiProvider(),
        new OpenMojiProvider(),
        new SensaEmojiProvider(),
        new TossFaceProvider()
    );

    // --

    @Getter(AccessLevel.PROTECTED)
    private Map<String, EmojiAssetImageSet> assets = new HashMap<>();

    public EmojiAssets(Variation variation) {
        for (EmojiAssetImageProvider provider : emojiProviders) {
            this.assets.put(provider.getProviderId(), provider.produce(variation));
        }
    }

    public EmojiAssets(Map<String, EmojiAssetImageSet> assets) {
        this.assets = assets; // Skips generation.
    }

    public @Nullable EmojiAssetImageSet getAsset(@NonNull String provider) {
        return this.assets.get(provider);
    }

    @Override
    public String toString() {
        return this.assets.toString();
    }

}
