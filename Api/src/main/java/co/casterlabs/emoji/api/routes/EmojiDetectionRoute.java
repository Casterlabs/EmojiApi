package co.casterlabs.emoji.api.routes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import co.casterlabs.emoji.data.Emoji;
import co.casterlabs.emoji.data.EmojiAssets;
import co.casterlabs.emoji.data.EmojiIndex;
import co.casterlabs.rakurai.io.http.HttpMethod;
import co.casterlabs.rakurai.io.http.HttpResponse;
import co.casterlabs.rakurai.io.http.StandardHttpStatus;
import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.annotating.JsonClass;
import co.casterlabs.rakurai.json.serialization.JsonParseException;
import co.casterlabs.rakurai.json.validation.JsonValidate;
import co.casterlabs.sora.api.http.HttpProvider;
import co.casterlabs.sora.api.http.SoraHttpSession;
import co.casterlabs.sora.api.http.annotations.HttpEndpoint;
import kotlin.Pair;
import lombok.AllArgsConstructor;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;

@AllArgsConstructor
public class EmojiDetectionRoute implements HttpProvider {
    private EmojiIndex index;

    @HttpEndpoint(uri = "/public/v3/emojis/detect", allowedMethods = {
            HttpMethod.POST
    })
    public HttpResponse onDetectEmojis(SoraHttpSession session) {
        try {
            DetectionRequest body = Rson.DEFAULT.fromJson(session.getRequestBody(), DetectionRequest.class);

            // Too large.
            if (body.text.length() > 10240 /* Bytes */) {
                return HttpResponse.newFixedLengthResponse(StandardHttpStatus.PAYLOAD_TOO_LARGE);
            }

            switch (body.responseFormat) {
                case DETECTED_ONLY: {
                    Set<Pair<Emoji, Emoji.Variation>> detected = this.index.matchAllEmojis(body.text);
                    List<Emoji.Variation> variations = new ArrayList<>(detected.size());

                    for (Pair<Emoji, Emoji.Variation> pair : detected) {
                        variations.add(pair.getSecond());
                    }

                    return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, Rson.DEFAULT.toJson(variations));
                }

                case NODES: {
                    Object[] nodes = this.index.matchAllEmojisAndReturnNodes(body.text);
                    return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, Rson.DEFAULT.toJson(nodes));

                }

                case HTML: {
                    String emojiProvider = session.getQueryParameters().getOrDefault("provider", EmojiAssets.DEFAULT_PROVIDER);
                    boolean escapeText = session.getQueryParameters().getOrDefault("escape", "true").equals("true");

                    String html = this.index.matchAllEmojisAndReturnHtml(body.text, emojiProvider, escapeText);

                    return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, html)
                        .setMimeType("text/html");
                }

            }

            // Can't happen, but we need to shut the compiler up anyway.
            return null;
        } catch (UnsupportedOperationException | JsonParseException e) {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            FastLogger.logException(e);
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.INTERNAL_ERROR);
        }
    }

    @JsonClass(exposeAll = true)
    public static class DetectionRequest {
        private String text;
        private ResponseFormat responseFormat = ResponseFormat.NODES;

        @JsonValidate
        private void $validate() {
            assert this.text != null;
            assert this.responseFormat != null;
        }

        public static enum ResponseFormat {
            DETECTED_ONLY,
            NODES,
            HTML;
        }

    }

}
