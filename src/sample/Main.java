package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import static sample.API.readJsonFromUrl;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();

        try (Reader reader = new FileReader("data.json"))
        {
            /*BufferedReader rd = new BufferedReader(reader);
            String jsonText = readAll(rd);
            JSONObject jsonRoot = new JSONObject(jsonText);
            JSONArray resultatRecherche = jsonRoot.getJSONObject("query").getJSONArray("search");
            JSONObject article = resultatRecherche.getJSONObject(0);
            System.out.println(article.getString("title"));
            System.out.println(article.getString("snippet")); */
            JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/3?scientificname=Delphinidae");
            JSONObject geometry = jsonRoot.getJSONArray("features").getJSONObject(0).getJSONObject("geometry");
            JSONObject occurence = jsonRoot.getJSONArray("features").getJSONObject(0).getJSONObject("properties");
            System.out.println(geometry.getJSONArray("coordinates"));
            System.out.println(occurence.getInt("n"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        launch(args);
    }

    private static String readAll(Reader rd) throws IOException {

        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1){
            sb.append((char) cp);
        }
        return sb.toString();

    }


}
