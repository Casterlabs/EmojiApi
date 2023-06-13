package co.casterlabs.emoji.api.routes;

import java.util.ArrayList;
import java.util.List;

import co.casterlabs.emoji.data.EmojiCategory;
import co.casterlabs.emoji.data.EmojiIndex;
import co.casterlabs.rakurai.io.http.StandardHttpStatus;
import co.casterlabs.rakurai.io.http.server.HttpResponse;
import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.sora.api.http.HttpProvider;
import co.casterlabs.sora.api.http.SoraHttpSession;
import co.casterlabs.sora.api.http.annotations.HttpEndpoint;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmojiCategoryRoutes implements HttpProvider {
    private EmojiIndex index;

    @HttpEndpoint(uri = "/public/v3/emojis/categories")
    public HttpResponse onGetAllEmojiCategories(SoraHttpSession session) {
        List<EmojiCategory> categories = this.index.getCategories();
        List<String> categoryNames = new ArrayList<>(categories.size());

        for (EmojiCategory category : categories) {
            categoryNames.add(category.getId());
        }

        return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, Rson.DEFAULT.toJson(categoryNames))
            .setMimeType("application/json");
    }

    @HttpEndpoint(uri = "/public/v3/emojis/category/:query")
    public HttpResponse onGetEmojiCategory(SoraHttpSession session) {
        String query = session.getUriParameters().get("query");
        EmojiCategory result = this.index.getCategoryById(query);

        if (result == null) {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.NOT_FOUND);
        } else {
            return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, Rson.DEFAULT.toJson(result))
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
