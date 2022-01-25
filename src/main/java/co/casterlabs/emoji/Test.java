package co.casterlabs.emoji;

import co.casterlabs.emoji.api.EmojiApi;
import co.casterlabs.sora.SoraFramework;
import co.casterlabs.sora.SoraLauncher;

public class Test {

    public static void main(String[] args) throws Exception {
        SoraFramework framework = new SoraLauncher().buildWithoutPluginLoader();

        framework
            .getSora()
            .register(new EmojiApi());

        framework.startHttpServer();
    }

}
