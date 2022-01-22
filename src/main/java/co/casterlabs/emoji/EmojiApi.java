package co.casterlabs.emoji;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.sora.Sora;
import co.casterlabs.sora.api.PluginImplementation;
import co.casterlabs.sora.api.SoraPlugin;
import lombok.NonNull;

@PluginImplementation
public class EmojiApi extends SoraPlugin {

	@Override
	public void onInit(Sora sora) {

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
