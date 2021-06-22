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
            Parent content = FXMLLoader.load(getClass().getResource("sample.fxml"));

            primaryStage.setTitle("Obis 3D");

            primaryStage.setScene(new Scene(content));

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


}
