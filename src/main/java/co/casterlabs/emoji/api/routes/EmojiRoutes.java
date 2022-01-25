package co.casterlabs.emoji.api.routes;

import co.casterlabs.emoji.WebUtil;
import co.casterlabs.emoji.data.Emoji;
import co.casterlabs.emoji.data.EmojiIndex;
import co.casterlabs.rakurai.io.http.HttpResponse;
import co.casterlabs.rakurai.io.http.StandardHttpStatus;
import co.casterlabs.sora.api.http.HttpProvider;
import co.casterlabs.sora.api.http.SoraHttpSession;
import co.casterlabs.sora.api.http.annotations.HttpEndpoint;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmojiRoutes implements HttpProvider {
    private EmojiIndex index;

    @HttpEndpoint(uri = "/public/v3/emojis/emoji/id/:query")
    public HttpResponse onGetEmojiById(SoraHttpSession session) {
        String query = WebUtil.decodeURI(session.getUriParameters().get("query"));
        Emoji result = this.index.getEmoji(query);

        if (result == null) {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.NOT_FOUND);
        } else {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, result.getJson())
                .setMimeType("application/json");
        }
    }

    @HttpEndpoint(uri = "/public/v3/emojis/emoji/id/:query/regex")
    public HttpResponse onGetEmojiRegexById(SoraHttpSession session) {
        String query = session.getUriParameters().get("query");
        Emoji result = this.index.getEmoji(query);

        if (result == null) {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.NOT_FOUND);
        } else {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, result.getRegex())
                .setMimeType("text/plain; charset=utf-8");
        }
    }

}
