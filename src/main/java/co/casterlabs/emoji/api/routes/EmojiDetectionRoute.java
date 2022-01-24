package co.casterlabs.emoji.api.routes;

import java.util.Set;

import co.casterlabs.emoji.data.Emoji;
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
import kotlin.Triple;
import lombok.AllArgsConstructor;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;

@AllArgsConstructor
public class EmojiDetectionRoute implements HttpProvider {
    private EmojiIndex index;

    @HttpEndpoint(uri = "/public/v3/emojis/detect", allowedMethods = {
            HttpMethod.GET
    })
    public HttpResponse onDetectEmojis(SoraHttpSession session) {
        try {
            DetectionRequest body = Rson.DEFAULT.fromJson(session.getRequestBody(), DetectionRequest.class);

            Set<Triple<String, Emoji, Emoji.Variation>> detected = this.index.matchAllEmojis(body.text);

            switch (body.responseFormat) {
                case DETECTED_ONLY:
                    return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, Rson.DEFAULT.toJson(detected));

//                case HTML:
//                case NODES:
                default:
                    return null;

            }
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
        private ResponseFormat responseFormat = ResponseFormat.DETECTED_ONLY;

        @JsonValidate
        private void $validate() {
            assert this.text != null;
            assert this.responseFormat != null;
        }

        public static enum ResponseFormat {
            DETECTED_ONLY,
//            NODES,
//            HTML;
        }

    }

}
