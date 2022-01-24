package co.casterlabs.emoji.api;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.emoji.api.routes.EmojiDetectionRoute;
import co.casterlabs.emoji.api.routes.EmojiQueryRoutes;
import co.casterlabs.emoji.data.EmojiIndex;
import co.casterlabs.emoji.generator.EmojiIndexGenerator;
import co.casterlabs.sora.Sora;
import co.casterlabs.sora.api.PluginImplementation;
import co.casterlabs.sora.api.SoraPlugin;
import lombok.NonNull;
import lombok.SneakyThrows;

@PluginImplementation
public class EmojiApi extends SoraPlugin {

    private EmojiIndex index;

    @SneakyThrows
    @Override
    public void onInit(Sora sora) {
        this.getClassLoader().setDefaultAssertionStatus(true);

        this.index = EmojiIndexGenerator.load();

        sora.addHttpProvider(this, new EmojiQueryRoutes(this.index));
        sora.addHttpProvider(this, new EmojiDetectionRoute(this.index));
    }

    @Override
    public void onClose() {

    }

    @Override
    public @Nullable String getVersion() {
        return "3.0.0";
    }

    @Override
    public @Nullable String getAuthor() {
        return "Casterlabs";
    }

    @Override
    public @NonNull String getName() {
        return "EmojiApi";
    }

    @Override
    public @NonNull String getId() {
        return "co.casterlabs.emoji";
    }

}
