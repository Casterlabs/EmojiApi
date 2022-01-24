package co.casterlabs.emoji;

import co.casterlabs.emoji.data.EmojiAssets;
import co.casterlabs.emoji.data.EmojiIndex;
import co.casterlabs.emoji.generator.EmojiIndexGenerator;

public class Test {

    public static void main(String[] args) throws Exception {
        EmojiIndex index = EmojiIndexGenerator.load();

        // https://www.w3schools.com/html/tryit.asp?filename=tryhtml_basic

        System.out.println(
            "<style>\n"
                + "    body {\n"
                + "        font-size: 2em;\n"
                + "    }\n"
                + "</style>\n"
                + ""
        );

        String[] formats = {
                "Live long and prosper. 🖖 (%s)",
                "I'm a little teapot, short and stout 🫖 (%s)",
                "We come in all colors of the rainbow 👋👋🏻👋🏼👋🏽👋🏾👋🏿 (%s)"
        };

        for (String format : formats) {
            for (EmojiAssets.AssetImageProvider provider : EmojiAssets.emojiProviders) {
                String text = String.format(format, provider.getProviderName());

                System.out.println(index.matchAllEmojisAndReturnHtml(text, provider.getProviderId()));
                System.out.println("<br />");
            }

            System.out.printf(format, "Your Browser");
            System.out.println();
            System.out.println("<br />");
            System.out.println("<br />");
        }
    }

}
