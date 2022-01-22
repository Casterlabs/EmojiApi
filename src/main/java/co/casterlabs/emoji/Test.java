package co.casterlabs.emoji;

import co.casterlabs.emoji.data.EmojiCategory;
import co.casterlabs.emoji.data.EmojiIndex;
import co.casterlabs.emoji.generator.EmojiIndexGenerator;
import co.casterlabs.rakurai.json.Rson;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;

public class Test {

    public static void main(String[] args) throws Exception {
        EmojiIndex index = EmojiIndexGenerator.load();

        EmojiCategory category = index.getCategoryById("smileys-and-emotion");

        FastLogger.logStatic("Category '%s' has %d emojis.", category.getName(), category.getAllEmojiVariations().size());
        FastLogger.logStatic("There are %d emojis.", index.getAllEmojiVariations().size());

        FastLogger.logStatic(
            Rson.DEFAULT.toJson(
                index
                    .getEmoji("vulcan salute")
                    .getVariations()
                    .get(3)
                    .getAssets()
            ).toString(true)
        );

////        Thread.sleep(100000);
    }

}
