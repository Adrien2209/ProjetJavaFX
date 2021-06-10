package sample;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    @FXML
    private Pane pane3D;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //Create a Pane et graph scene root for the 3D content
        Group root3D = new Group();

        //Create cube shape
        Box blueCube = new Box(1, 1, 1);
        Box redCube = new Box(1, 1, 1);
        Box greenCube = new Box(1, 1, 1);

        //Create Blue Material
        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.BLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        //Create Red Material
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.RED);
        redMaterial.setSpecularColor(Color.RED);

        //Create Green Material
        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.GREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        //Set it to the cube
        blueCube.setMaterial(blueMaterial);
        redCube.setMaterial(redMaterial);
        greenCube.setMaterial(greenMaterial);

        //Add the cube to this node
        root3D.getChildren().add(blueCube);
        root3D.getChildren().add(redCube);
        root3D.getChildren().add(greenCube);

        //Translate the Red and Green Cube
        redCube.setTranslateX(1.5);
        redCube.setTranslateZ(0.5);
        greenCube.setTranslateY(-1.5);

        // Camera
        PerspectiveCamera camera = new PerspectiveCamera(true);

        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-180);
        light.setTranslateY(-180);
        light.setTranslateZ(-180);
        light.getScope().addAll(root3D);
        root3D.getChildren().add(light);

        // Create the subscene
        SubScene subscene = new SubScene(root3D, 600, 600, true, SceneAntialiasing.BALANCED);
        subscene.setCamera(camera);
        subscene.setFill(Color.GREY);
        pane3D.getChildren().addAll(subscene);

        //Add an animation timer
        final long startNanoTime = System.nanoTime();
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                double t = (currentNanoTime-startNanoTime)/1000000000.0;
                double rotationSpeed = 12.0;
                //Add code for Animation
                greenCube.setRotationAxis(new Point3D(0,1,0));
                greenCube.setRotate(rotationSpeed * t);

            }
        }.start();

        // Build camera manager
        new CameraManager(camera, pane3D, root3D);

    }
}
