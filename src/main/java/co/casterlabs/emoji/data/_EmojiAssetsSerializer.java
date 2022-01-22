package co.casterlabs.emoji.data;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.annotating.JsonSerializer;
import co.casterlabs.rakurai.json.element.JsonElement;
import co.casterlabs.rakurai.json.serialization.JsonParseException;
import lombok.NonNull;

public class _EmojiAssetsSerializer implements JsonSerializer<EmojiAssets> {

    @Override
    public @Nullable EmojiAssets deserialize(@NonNull JsonElement value, @NonNull Class<?> type, @NonNull Rson rson) throws JsonParseException {
        // Attemps to deserialize should always fail.
        return null;
    }

    @Override
    public JsonElement serialize(@NonNull Object _value, @NonNull Rson rson) {
        EmojiAssets value = (EmojiAssets) _value;

        return rson.toJson(value.getAssets());
    }

}