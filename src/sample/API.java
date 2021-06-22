package sample;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class API {

    /**
     * Methode qui retourne un JSONObject a partir d'un JSON obtenu à travers un URL
     * @param url : lien internet (API)
     * @return JSONObject
     */
    public static JSONObject readJsonFromUrl(String url){
        String json = "";

        HttpClient client = HttpClient.newBuilder()
                .version(Version.HTTP_1_1)
                .followRedirects(Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type","application/json")
                .GET()
                .build();

        try {
            json = client.sendAsync(request, BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JSONObject(json);
    }

    /**
     * Methode qui retourne un JSONArray a partir d'un JSON obtenu à travers un URL
     * @param url : lien internet (API)
     * @return JSONArray
     */
    public static JSONArray readJsonFromUrlArray(String url){
        String json = "";

        HttpClient client = HttpClient.newBuilder()
                .version(Version.HTTP_1_1)
                .followRedirects(Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type","application/json")
                .GET()
                .build();

        try {
            json = client.sendAsync(request, BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JSONArray(json);
    }

    /**
     * Methode qui permet de lire un fichier JSON local
     * @param rd
     * @return JSON sous format String
     * @throws IOException
     */
    public static String readAll(Reader rd) throws IOException {

        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1){
            sb.append((char) cp);
        }
        return sb.toString();

    }
}
