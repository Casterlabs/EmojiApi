package co.casterlabs.emoji;

import co.casterlabs.emoji.data.EmojiCategory;
import co.casterlabs.emoji.data.EmojiIndex;
import co.casterlabs.emoji.generator.EmojiIndexGenerator;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;

public class Test {

    public static void main(String[] args) throws Exception {
        EmojiIndex index = EmojiIndexGenerator.load();

        EmojiCategory category = index.getCategoryById("smileys-and-emotion");

        FastLogger.logStatic("Category '%s' has %d emojis.", category.getName(), category.getAllEmojiVariations().size());
        FastLogger.logStatic("There are %d emojis.", index.getAllEmojiVariations().size());

        FastLogger.logStatic(index.getEmoji("vulcan salute").getVariations().get(3).getAssets());

////        Thread.sleep(100000);
    }

}