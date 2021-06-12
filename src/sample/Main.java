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


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        try
        {
            //On prépare le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));

            //On charge le fichier FXML, il appellera la méthode *initialize()* de la vue
            Parent root = loader.load();

            //On crée la scène
            Scene scene = new Scene(root);

            primaryStage.setTitle("Obis 3D");

            //On définit cette scène comme étant la scène de notre première fenêtre
            primaryStage.setScene(scene);

            //On rend cette fenêtre visible
            primaryStage.show();
        }
        catch(Exception e)
        {
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
