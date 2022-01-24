package co.casterlabs.emoji;

import co.casterlabs.emoji.data.EmojiIndex;
import co.casterlabs.emoji.generator.EmojiIndexGenerator;
import co.casterlabs.rakurai.json.Rson;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;

public class Test {

    public static void main(String[] args) throws Exception {
        EmojiIndex index = EmojiIndexGenerator.load();

        FastLogger.logStatic(
            Rson.DEFAULT.toJson(
                index.matchAllEmojisAndReturnNodes("start :vulcan_salute: end")
            ).toString(true)
        );

////        Thread.sleep(100000);
    }

}
