package view;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.AnimationTimer;
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
import org.json.JSONArray;
import org.json.JSONObject;
import request.Request;
import sample.CameraManager;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.control.textfield.AutoCompletionBinding;


import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static sample.API.readJsonFromUrl;

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
    private Button clearstartdate;

    @FXML
    private Button clearenddate;


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

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //Create a Pane et graph scene root for the 3D content
        Group root3D = new Group();
        Group Histogramme = new Group();

        //Set disable
        informations.setEditable(false);

        //AFFICHAGE DE LA TERRE
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

        //Add an animation timer
        final long startNanoTime = System.nanoTime();
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                double rotationSpeed = 12.0;
                earth.setRotationAxis(new Point3D(0,1,0));
                earth.setRotate(rotationSpeed*t);

            }
        }.start();

        //Auto completion
        ScientificName.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
            ObservableList<String> items = FXCollections.observableArrayList(request.AutoCompletion(ScientificName.getText()));
            combo.setItems(items);
            }
        });

        //Search Button
        search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent){
                informations.setText("Informations");
                String Name = ScientificName.getText();
                String GeoHashPre = GeoHashPrecision.getText();

                // Request with Scientific Name and GeoHash
                if(StartDate.getValue() == null && EndDate.getValue() == null){
                    Histogramme.getChildren().clear();
                    ArrayList<String> resultat = request.GlobalOccurenceScientificName(Name,GeoHashPre, Histogramme);
                    informations.appendText(resultat.get(0));
                    Legend(resultat.get(1));
                }

                // Request with Scientific Name, GeoHash and date
                else if(StartDate.getValue() != null || EndDate.getValue() != null){
                    if(StartDate.getValue() == null){
                        Histogramme.getChildren().clear();
                        String startdate = "";
                        String enddate = EndDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        ArrayList<String> resultat = request.OccurenceWithDate(Name,GeoHashPre,startdate,enddate, Histogramme);
                        informations.appendText(resultat.get(0));
                        Legend(resultat.get(1));
                    }
                    else if(EndDate.getValue() == null){
                        Histogramme.getChildren().clear();
                        String startdate = StartDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        String enddate = "";
                        ArrayList<String> resultat = request.OccurenceWithDate(Name,GeoHashPre,startdate,enddate, Histogramme);
                        informations.appendText(resultat.get(0));
                        Legend(resultat.get(1));
                    }
                    else {
                        Histogramme.getChildren().clear();
                        String startdate = StartDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        String enddate = EndDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        ArrayList<String> resultat = request.OccurenceWithDate(Name,GeoHashPre,startdate,enddate, Histogramme);
                        informations.appendText(resultat.get(0));
                        Legend(resultat.get(1));
                    }
                }
            }
        });

        //Clear Start Date Button
        clearstartdate.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent){
                StartDate.setValue(null);
                }
            });

        //Clear End Date Button
        clearstartdate.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent){
                EndDate.setValue(null);
            }
        });

        combo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ScientificName.setText(String.valueOf(combo.getSelectionModel().getSelectedItem()));
            }
        });
        earth.getChildren().add(Histogramme);
        root3D.getChildren().add(earth);

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


    public static Point3D geoCoordTo3dCoord(double lat, double lon) {
        double lat_cor = lat + TEXTURE_LAT_OFFSET;
        double lon_cor = lon + TEXTURE_LON_OFFSET;
        return new Point3D(
                -java.lang.Math.sin(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)),
                -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor)),
                java.lang.Math.cos(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)));
    }

    public void Legend(String maximum){
        int PasLegende = (Integer.parseInt(maximum)/12);
        legend1.setText("> "+String.valueOf(10*PasLegende));
        legend2.setText("> "+String.valueOf(8*PasLegende));
        legend3.setText("> "+String.valueOf(5*PasLegende));
        legend4.setText("> "+String.valueOf(2*PasLegende));
        legend5.setText("> "+String.valueOf(1*PasLegende));
        legend6.setText("> "+String.valueOf(0));
    }
}
