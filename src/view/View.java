package view;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import request.Request;
import sample.CameraManager;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.control.textfield.AutoCompletionBinding;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class View implements Initializable {

    private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;

    Request request;

    @FXML
    private TextField ScientificName;

    @FXML
    private AnchorPane OBIS3D;

    @FXML
    private Pane pane3D;

    @FXML
    private TextField GeoHashPrecision;

    @FXML
    private DatePicker StartDate;

    @FXML
    private DatePicker EndDate;

    @FXML
    private TextField TimeSpan;

    @FXML
    private TextField NumberOfIntervals;

    @FXML
    private TextField GPSCoordinates;

    @FXML
    private TextField GeoHash;

    @FXML
    private RadioButton Move;

    @FXML
    private ToggleGroup MoveSelection;

    @FXML
    private RadioButton Selection;

    @FXML
    private ListView<?> SpecieList;

    @FXML
    private Button search;

    @FXML
    private Text legend1;

    @FXML
    private Text legend2;

    @FXML
    private Text legend3;

    @FXML
    private Text legend4;

    @FXML
    private Text legend5;

    @FXML
    private Text legend6;

    @FXML
    private ComboBox combo;

    @FXML
    private TextArea informations;


    public View()
    {
        request = new Request();
    }

    ArrayList<String> possibleWords = new ArrayList<String>();


    public void handleButtonSearch(ActionEvent actionEvent) throws IOException {
        informations.setText("Informations");
        String Name = ScientificName.getText();
        String GeoHashPre = GeoHashPrecision.getText();
        request.GlobalOccurenceScientificName(Name,GeoHashPre);
        informations.appendText(request.GlobalOccurenceScientificName(Name,GeoHashPre));
    }


    /*
    public void ScientificNameActualization(KeyEvent keyEvent) throws IOException{
        possibleWords.clear();
        String lettre = ScientificName.getText();
        possibleWords = request.AutoCompletion(lettre);
        TextFields.bindAutoCompletion(ScientificName,possibleWords);
    }

     */

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //Set disable
        informations.setEditable(false);
        //AFFICHAGE DE LA TERRE

        //Create a Pane et graph scene root for the 3D content
        Group root3D = new Group();

        // Load geometry
        ObjModelImporter objImporter = new ObjModelImporter();
        try {
            URL modelUrl = this.getClass().getResource("../sample/Earth/earth.obj");
            objImporter.read(modelUrl);
        } catch (ImportException e) {
            // handle exception
            System.out.println(e.getMessage());
        }
        MeshView[] meshViews = objImporter.getImport();
        Group earth = new Group(meshViews);
        root3D.getChildren().add(earth);

        // Add a camera group
        PerspectiveCamera camera = new PerspectiveCamera(true);
        new CameraManager(camera, pane3D, root3D);

        // Add point light
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-180);
        light.setTranslateY(-90);
        light.setTranslateZ(-120);
        light.getScope().addAll(root3D);
        root3D.getChildren().add(light);

        // Add ambient light
        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
        ambientLight.getScope().addAll(root3D);
        root3D.getChildren().add(ambientLight);

        // Create the subscene
        SubScene subscene = new SubScene(root3D, 566, 700, true, SceneAntialiasing.BALANCED);
        subscene.setCamera(camera);
        subscene.setFill(Color.gray(0.2));
        pane3D.getChildren().addAll(subscene);

        //Affichage des villes
        displayTown2(root3D,"Brest", 48.447911f,-4.418539f);
        displayTown(root3D,"Paris", 48.86667f,2.33333f);

        //FIN AFFICHAGE DE LA TERRE

        //Auto completion
        ScientificName.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
            ObservableList<String> items = FXCollections.observableArrayList(request.AutoCompletion(ScientificName.getText()));
            combo.setItems(items);
            }
        });

        combo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ScientificName.setText(String.valueOf(combo.getSelectionModel().getSelectedItem()));
            }
        });

    }

    // From Rahel Lüthy : https://netzwerg.ch/blog/2015/03/22/javafx-3d-line/
    public Cylinder createLine(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(0.01f, height);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }


    public static Point3D geoCoordTo3dCoord(float lat, float lon) {
        float lat_cor = lat + TEXTURE_LAT_OFFSET;
        float lon_cor = lon + TEXTURE_LON_OFFSET;
        return new Point3D(
                -java.lang.Math.sin(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)),
                -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor)),
                java.lang.Math.cos(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)));
    }

    public void displayTown(Group parent, String name, float latitude, float longitude)
    {
        Point3D emplacement = geoCoordTo3dCoord(latitude, longitude);
        Box town = new Box(0.005,0.01,0.005);
        parent.setId(name);

        // Town Color
        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.GREEN);
        greenMaterial.setSpecularColor(Color.GREEN);
        town.setMaterial(greenMaterial);

        //Town placement

        town.setTranslateX(emplacement.getX());
        town.setTranslateY(emplacement.getY());
        town.setTranslateZ(emplacement.getZ());

        parent.getChildren().add(town);

    }

    public void displayTown2(Group parent, String name, float latitude, float longitude)
    {
        Point3D emplacement = geoCoordTo3dCoord(latitude, longitude);
        Box town = new Box(0.005,0.05,0.005);
        parent.setId(name);

        // Town Color
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.RED);
        redMaterial.setSpecularColor(Color.RED);
        town.setMaterial(redMaterial);

        //Town placement

        town.setTranslateX(emplacement.getX());
        town.setTranslateY(emplacement.getY());
        town.setTranslateZ(emplacement.getZ());

        parent.getChildren().add(town);

    }

}
