package co.casterlabs.emoji.api.routes;

import co.casterlabs.emoji.data.EmojiCategory;
import co.casterlabs.emoji.data.EmojiIndex;
import co.casterlabs.rakurai.io.http.HttpResponse;
import co.casterlabs.rakurai.io.http.StandardHttpStatus;
import co.casterlabs.sora.api.http.HttpProvider;
import co.casterlabs.sora.api.http.SoraHttpSession;
import co.casterlabs.sora.api.http.annotations.HttpEndpoint;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmojiCategoryRoutes implements HttpProvider {
    private EmojiIndex index;

    @HttpEndpoint(uri = "/public/v3/emojis/category/:query")
    public HttpResponse onGetEmojiCategory(SoraHttpSession session) {
        String query = session.getUriParameters().get("query");
        EmojiCategory result = this.index.getCategoryById(query);

        if (result == null) {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.NOT_FOUND);
        } else {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, result.getJson())
                .setMimeType("application/json");
        }
    }

    @HttpEndpoint(uri = "/public/v3/emojis/category/:query/regex")
    public HttpResponse onGetEmojiCategoryRegex(SoraHttpSession session) {
        String query = session.getUriParameters().get("query");
        EmojiCategory result = this.index.getCategoryById(query);

        if (result == null) {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.NOT_FOUND);
        } else {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, result.getRegex())
                .setMimeType("text/plain; charset=utf-8");
        }
    }

}
