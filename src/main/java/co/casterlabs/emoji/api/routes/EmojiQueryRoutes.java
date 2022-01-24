package co.casterlabs.emoji.api.routes;

import java.util.List;

import co.casterlabs.emoji.data.Emoji;
import co.casterlabs.emoji.data.EmojiCategory;
import co.casterlabs.emoji.data.EmojiIndex;
import co.casterlabs.rakurai.io.http.HttpResponse;
import co.casterlabs.rakurai.io.http.StandardHttpStatus;
import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.sora.api.http.HttpProvider;
import co.casterlabs.sora.api.http.SoraHttpSession;
import co.casterlabs.sora.api.http.annotations.HttpEndpoint;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmojiQueryRoutes implements HttpProvider {
    private EmojiIndex index;

    @HttpEndpoint(uri = "/public/v3/emojis/all")
    public HttpResponse onGetAllEmojis(SoraHttpSession session) {
        return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, this.index.getJson());
    }

    @HttpEndpoint(uri = "/public/v3/emojis/emoji/id/:query")
    public HttpResponse onGetEmojiById(SoraHttpSession session) {
        String query = session.getUriParameters().get("query");
        Emoji result = this.index.getEmoji(query);

        if (result == null) {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.NOT_FOUND);
        } else {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, result.getJson());
        }
    }

    @HttpEndpoint(uri = "/public/v3/emojis/category/:query")
    public HttpResponse onGetEmojiCategory(SoraHttpSession session) {
        String query = session.getUriParameters().get("query");
        EmojiCategory result = this.index.getCategoryById(query);

        if (result == null) {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.NOT_FOUND);
        } else {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, this.index.getJson());
        }
    }

    @HttpEndpoint(uri = "/public/v3/emojis/subcategory/:query")
    public HttpResponse onGetEmojiSubcategory(SoraHttpSession session) {
        String query = session.getUriParameters().get("query");
        List<Emoji> result = this.index.getEmojisForSubCategory(query);

        if (result == null) {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.NOT_FOUND);
        } else {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, Rson.DEFAULT.toJson(result));
        }
    }

}
