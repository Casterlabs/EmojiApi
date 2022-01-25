package co.casterlabs.emoji.api;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.emoji.api.routes.EmojiCategoryRoutes;
import co.casterlabs.emoji.api.routes.EmojiDetectionRoute;
import co.casterlabs.emoji.api.routes.EmojiRoutes;
import co.casterlabs.emoji.api.routes.EmojiIndexRoutes;
import co.casterlabs.emoji.api.routes.EmojiSubcategoryRoutes;
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
        if (this.getClassLoader() != null) {
            this.getClassLoader().setDefaultAssertionStatus(true);
        }

        this.index = EmojiIndexGenerator.load();

        sora.addHttpProvider(this, new EmojiIndexRoutes(this.index));
        sora.addHttpProvider(this, new EmojiCategoryRoutes(this.index));
        sora.addHttpProvider(this, new EmojiSubcategoryRoutes(this.index));
        sora.addHttpProvider(this, new EmojiRoutes(this.index));
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
