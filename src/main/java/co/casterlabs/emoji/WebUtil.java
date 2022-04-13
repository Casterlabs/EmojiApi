package co.casterlabs.emoji;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import lombok.NonNull;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebUtil {
    private static final OkHttpClient client = new OkHttpClient();

    public static String sendHttpRequest(@NonNull Request.Builder builder) throws IOException {
        try (Response response = client.newCall(builder.build()).execute()) {
            return response.body().string();
        }
    }

    public static boolean doesContentExist(@Nullable String url) {
        if (url == null) {
            return false;
        }

        Request request = new Request.Builder()
            .url(url)
            .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            if (e.getMessage().equals("timeout")) {
                return doesContentExist(url); // Try again.
            }
            System.err.println(url);
            e.printStackTrace();
            return false;
        }
    }

    public static String escapeHtml(@NonNull String str) {
        return str
            .codePoints()
            .mapToObj(c -> c > 127 || "\"'<>&".indexOf(c) != -1 ? "&#" + c + ";" : new String(Character.toChars(c)))
            .collect(Collectors.joining());
    }

    @SneakyThrows
    public static String encodeURI(@NonNull String s) {
        return URLEncoder.encode(s, "UTF-8")
            .replaceAll("\\+", "%20");
    }

    @SneakyThrows
    public static String decodeURI(@NonNull String s) {
        return URLDecoder.decode(s, "UTF-8");
    }

    @SneakyThrows
    public static String decodeURIComponent(@NonNull String s) {
        return URLDecoder.decode(s, "UTF-8");
    }

    @SneakyThrows
    public static String encodeURIComponent(@NonNull String s) {
        return URLEncoder.encode(s, "UTF-8")
            .replaceAll("\\+", "%20")
            .replaceAll("\\%21", "!")
            .replaceAll("\\%27", "'")
            .replaceAll("\\%28", "(")
            .replaceAll("\\%29", ")")
            .replaceAll("\\%7E", "~");
    }

}
