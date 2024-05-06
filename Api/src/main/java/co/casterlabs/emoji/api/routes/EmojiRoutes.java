package co.casterlabs.emoji.api.routes;

import java.net.URLDecoder;

import co.casterlabs.emoji.data.Emoji;
import co.casterlabs.emoji.data.EmojiIndex;
import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rhs.protocol.StandardHttpStatus;
import co.casterlabs.rhs.server.HttpResponse;
import co.casterlabs.sora.api.http.HttpProvider;
import co.casterlabs.sora.api.http.SoraHttpSession;
import co.casterlabs.sora.api.http.annotations.HttpEndpoint;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;

@AllArgsConstructor
public class EmojiRoutes implements HttpProvider {
    private EmojiIndex index;

    @HttpEndpoint(uri = "/public/v3/emojis/emoji/:query")
    public HttpResponse onGetEmojiById(SoraHttpSession session) {
        String query = decodeURI(session.getUriParameters().get("query"));
        Emoji result = this.index.getEmoji(query);

        if (result == null) {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.NOT_FOUND);
        } else {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, Rson.DEFAULT.toJson(result))
                .setMimeType("application/json");
        }
    }

    @HttpEndpoint(uri = "/public/v3/emojis/emoji/:query/regex")
    public HttpResponse onGetEmojiRegexById(SoraHttpSession session) {
        String query = decodeURI(session.getUriParameters().get("query"));
        Emoji result = this.index.getEmoji(query);

        if (result == null) {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.NOT_FOUND);
        } else {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, result.getRegex())
                .setMimeType("text/plain; charset=utf-8");
        }
    }

    @SneakyThrows
    public static String decodeURI(@NonNull String s) {
        return URLDecoder.decode(s, "UTF-8");
    }

}
