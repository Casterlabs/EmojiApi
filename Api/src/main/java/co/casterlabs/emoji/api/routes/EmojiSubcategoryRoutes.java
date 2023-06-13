package co.casterlabs.emoji.api.routes;

import java.util.List;

import co.casterlabs.emoji.data.Emoji;
import co.casterlabs.emoji.data.EmojiIndex;
import co.casterlabs.rakurai.io.http.StandardHttpStatus;
import co.casterlabs.rakurai.io.http.server.HttpResponse;
import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.sora.api.http.HttpProvider;
import co.casterlabs.sora.api.http.SoraHttpSession;
import co.casterlabs.sora.api.http.annotations.HttpEndpoint;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmojiSubcategoryRoutes implements HttpProvider {
    private EmojiIndex index;

    @HttpEndpoint(uri = "/public/v3/emojis/subcategory/:query")
    public HttpResponse onGetEmojiSubcategory(SoraHttpSession session) {
        String query = session.getUriParameters().get("query");
        List<Emoji> result = this.index.getEmojisForSubCategory(query);

        if (result == null) {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.NOT_FOUND);
        } else {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, Rson.DEFAULT.toJson(result))
                .setMimeType("application/json");
        }
    }

    @HttpEndpoint(uri = "/public/v3/emojis/subcategory/:query/regex")
    public HttpResponse onGetEmojiSubcategoryRegex(SoraHttpSession session) {
        // TODO sort emojis into subcategories. Until then, this route will not work.
        return HttpResponse.newFixedLengthResponse(StandardHttpStatus.NOT_IMPLEMENTED, "Not here.");
    }

}
