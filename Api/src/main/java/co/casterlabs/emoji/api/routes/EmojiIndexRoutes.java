package co.casterlabs.emoji.api.routes;

import java.util.ArrayList;
import java.util.List;

import co.casterlabs.emoji.data.Emoji;
import co.casterlabs.emoji.data.EmojiIndex;
import co.casterlabs.rakurai.io.http.HttpResponse;
import co.casterlabs.rakurai.io.http.StandardHttpStatus;
import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.sora.api.http.HttpProvider;
import co.casterlabs.sora.api.http.SoraHttpSession;
import co.casterlabs.sora.api.http.annotations.HttpEndpoint;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmojiIndexRoutes implements HttpProvider {
    private EmojiIndex index;

    @HttpEndpoint(uri = "/public/v3/emojis/currentversion")
    public HttpResponse onGetEmojiVersion(SoraHttpSession session) {
        return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, this.index.getVersion());
    }

    @HttpEndpoint(uri = "/public/v3/emojis/all")
    public HttpResponse onGetAllEmojis(SoraHttpSession session) {
        List<Emoji> emojis = this.index.getAllEmojis();
        List<String> emojiNames = new ArrayList<>(emojis.size());

        for (Emoji emoji : emojis) {
            emojiNames.add(emoji.getIdentifier());
        }

        return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, Rson.DEFAULT.toJson(emojiNames))
            .setMimeType("application/json");
    }

    @HttpEndpoint(uri = "/public/v3/emojis/all/regex")
    public HttpResponse onGetAllEmojisRegex(SoraHttpSession session) {
        return HttpResponse.newFixedLengthResponse(StandardHttpStatus.OK, this.index.getRegex())
            .setMimeType("text/plain; charset=utf-8");
    }

}
