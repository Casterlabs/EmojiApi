package co.casterlabs.emoji.data;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.annotating.JsonSerializer;
import co.casterlabs.rakurai.json.element.JsonElement;
import co.casterlabs.rakurai.json.element.JsonObject;
import co.casterlabs.rakurai.json.serialization.JsonParseException;
import lombok.NonNull;

public class _EmojiAssetsSerializer implements JsonSerializer<EmojiAssets> {

    @Override
    public @Nullable EmojiAssets deserialize(@NonNull JsonElement value, @NonNull Class<?> type, @NonNull Rson rson) throws JsonParseException {
        JsonObject mapping = value.getAsObject();
        Map<String, EmojiAssetImageSet> assets = new HashMap<>();

        for (Map.Entry<String, JsonElement> entry : mapping.entrySet()) {
            EmojiAssetImageSet set = Rson.DEFAULT.fromJson(entry.getValue(), EmojiAssetImageSet.class);

            assets.put(entry.getKey(), set);
        }

        return new EmojiAssets(assets);
    }

    @Override
    public JsonElement serialize(@NonNull Object _value, @NonNull Rson rson) {
        EmojiAssets value = (EmojiAssets) _value;

        return rson.toJson(value.getAssets());
    }

}
