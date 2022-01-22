package co.casterlabs.emoji.generator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.casterlabs.emoji.WebUtil;
import co.casterlabs.emoji.data.Emoji;
import co.casterlabs.emoji.data.EmojiCategory;
import co.casterlabs.emoji.data.EmojiIndex;
import co.casterlabs.rakurai.io.IOUtil;
import co.casterlabs.rakurai.json.Rson;
import okhttp3.Request;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;

public class EmojiIndexGenerator {
    private static final String EMOJI_VERSION = "E14.0";
    private static final String EMOJI_TEST_URL = "https://www.unicode.org/Public/emoji/" + EMOJI_VERSION.substring(1) + "/emoji-test.txt";

    private static final FastLogger logger = new FastLogger();

    /**
     * @deprecated This is to be called in an dev environment to generate the
     *             {@code emoji_index.json} file. Do not use it outside of that
     *             case.
     */
    @Deprecated
    public static void main(String[] args) throws IOException {
        generate();
    }

    /**
     * Loads the index from the cached version in the jar's resources.
     */
    public static EmojiIndex load() throws IOException {
        try (InputStream in = EmojiIndexGenerator.class.getClassLoader().getResourceAsStream("emoji_index.json")) {
            String contents = IOUtil.readInputStreamString(in, StandardCharsets.UTF_8);

            return Rson.DEFAULT.fromJson(contents, EmojiIndex.class);
        }
    }

    private static EmojiIndex generate() throws IOException {
        String[] declaration = getEmojiDeclaration();

        List<EmojiCategory> categories = new LinkedList<>();

        String currentGroup = null;
        String currentSubgroup = null;

        String currentGroupId = null;
        List<Emoji> currentGroupEmojis = new LinkedList<>();
        Map<String, List<Emoji.Variation>> currentGroupEmojiVariations = new HashMap<>();

        for (String line : declaration) {
            if (!line.isEmpty()) {
                logger.debug("(currentGroup = '%s', currentSubgroup = '%s')", currentGroup, currentSubgroup);
                logger.debug("Processing line: %s", line);

                if (line.startsWith("#") && line.contains("subtotal:")) {
                    logger.debug("Line is a subtotal declaration, ignoring.");
                } else if (line.startsWith("# group:")) {
                    logger.debug("Line is a group declaration.");

                    if (currentGroup != null) {
                        if (!currentGroupEmojis.isEmpty()) {
                            categories.add(
                                EmojiCategory.from(
                                    currentGroup,
                                    currentGroupEmojis
                                )
                            );

                            currentGroupEmojis = new LinkedList<>();
                        }

                        currentGroupEmojiVariations.clear();
                    }

                    currentGroup = line.substring(line.indexOf(":") + 1).trim();
                    currentGroupId = EmojiCategory.nameToId(currentGroup);

//                    if (currentGroupId.equals("flags")) {
//                        logger.setCurrentLevel(LogLevel.DEBUG);
//                    } else {
//                        logger.setCurrentLevel(LogLevel.INFO);
//                    }
                } else if (line.startsWith("# subgroup:")) {
                    logger.debug("Line is a subgroup declaration.");
                    currentSubgroup = line.substring(line.indexOf(":") + 1).trim();
                } else {
                    logger.debug("Line is an emoji declaration.");

                    String[] $split_pound = line.split("#", 2);
                    String[] split_semicolon = $split_pound[0].split(";");
                    String[] split_space = $split_pound[1].trim().split(" ", 3);
                    String[] split_identifier = split_space[2].split(":");

                    Emoji.Qualification qualification = Emoji.Qualification.from(split_semicolon[1].trim());

                    if (qualification == Emoji.Qualification.FULLY_QUALIFIED) {
                        String sequence = split_space[0];
                        String since = split_space[1];
                        String identifier = split_identifier[0].trim();
                        String variationId = (split_identifier.length > 1) ? split_identifier[1].trim() : "default";
                        String variationName = split_space[2].trim();
                        String[] codeSequence = split_semicolon[0].trim().toLowerCase().split(" ");

                        Emoji.Variation variation = new Emoji.Variation(identifier, variationName, variationId, qualification, since, codeSequence, sequence);

                        List<Emoji.Variation> variations = currentGroupEmojiVariations.get(identifier);

                        // Create the emoji.
                        if (variations == null) {
                            variations = new LinkedList<>();
                            currentGroupEmojiVariations.put(identifier, variations);

                            currentGroupEmojis.add(
                                Emoji.from(
                                    currentGroupId,
                                    currentSubgroup,
                                    identifier,
                                    variations
                                )
                            );
                        }

                        variations.add(variation);
                    } else {
                        logger.debug("Emoji wasn't qualified enough.");
                    }
                }

                logger.debug("");
            }
        }

        // Add the final group
        categories.add(
            EmojiCategory.from(
                currentGroup,
                currentGroupEmojis
            )
        );

        EmojiIndex index = new EmojiIndex(EMOJI_VERSION, categories);

        Files.write(new File("src/main/resources/emoji_index.json").toPath(), index.getJson().getBytes(StandardCharsets.UTF_8));

        return index;
    }

    private static String[] getEmojiDeclaration() throws IOException {
        String response = WebUtil.sendHttpRequest(new Request.Builder().url(EMOJI_TEST_URL));

        int startIndex = response.indexOf("# group: ");
        int endIndex = response.indexOf("# Status Counts");

        response = response.substring(startIndex, endIndex); // Excludes the headers & meta.

        return response.split("[\\r\\n]+");
    }

}
