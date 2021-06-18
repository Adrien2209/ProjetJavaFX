package request;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.String.valueOf;
import static sample.API.readJsonFromUrl;
import static sample.API.readJsonFromUrlArray;
import static view.View.geoCoordTo3dCoord;
import static sample.API.readAll;

public class Request {

    public ArrayList<String> AffichageDepart(Group parent) {
        try (Reader reader = new FileReader("./Delphinidae.json")){
            BufferedReader rd = new BufferedReader(reader);
            String jsonText = readAll(rd);
            JSONObject jsonRoot = new JSONObject(jsonText);

            ArrayList<String> ListRetour = new ArrayList<String>();
            ArrayList<Group> Histogramme = new ArrayList<Group>();
            int total = 0;
            String retour = "";

            JSONArray liste = jsonRoot.getJSONArray("features");
            int minimum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");
            int maximum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");

            //Creation des legendes
            int PasLegende = (maximum/12);

            for (int i=0; i < liste.length(); i++)
            {
                ArrayList<Double> GPSZone = new ArrayList<Double>();
                retour = retour + "\n\nArea " + (i+1) + " coordinates :\n";
                int occurence = liste.getJSONObject(i).getJSONObject("properties").getInt("n");
                JSONArray listecoordinate = liste.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
                for (int t=0; t < listecoordinate.length(); t++)
                {
                    String GPS1 = valueOf(listecoordinate.getJSONArray(t).getDouble(0));
                    String GPS2 = valueOf(listecoordinate.getJSONArray(t).getDouble(1));
                    //Ajout des coordonnés GPS dans une liste pour déterminer le centre de la zone
                    GPSZone.add(listecoordinate.getJSONArray(t).getDouble(0));
                    GPSZone.add(listecoordinate.getJSONArray(t).getDouble(1));
                    retour = retour + "\n[" + GPS1 + "," + GPS2 + "]";
                }
                if (occurence < minimum)
                {
                    minimum = occurence;
                }
                if (occurence > maximum)
                {
                    maximum = occurence;
                }
                total = total + occurence;
                retour = retour + "\n\nNumber of occurrences = " + occurence;

                //Affichage des resultats sur la planete
                Histogramme.add(displaySpecie(occurence, PasLegende, GPSZone));

            }
            retour = retour + "\n\nTotal occurrences = "+total+"\nMinimum = "+minimum+"\nMaximum = "+maximum;
            ListRetour.add(retour);
            ListRetour.add(valueOf(maximum));
            displayHistogramme(parent, Histogramme);
            return ListRetour;

        } catch (IOException e){
            e.printStackTrace();
            ArrayList<String> ListRetour = new ArrayList<String>();
            ListRetour.add("Il y a eu un soucis de chargement du fichier initial");
            ListRetour.add("0");
            return ListRetour;
        }
    }



    public ArrayList<String> GlobalOccurenceScientificName(String scientificname, String geohashprecision, String geohash, Group parent)
    {
        ArrayList<String> ListRetour = new ArrayList<String>();
        ArrayList<Group> Histogramme = new ArrayList<Group>();
        scientificname = scientificname.replaceAll(" ", "%20");
        int total = 0;
        String retour = "";
        if (geohashprecision.equals(""))
        {
            geohashprecision = "3";
        }

        if (geohashprecision.equals("0"))
        {
            JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/1?scientificname=" + scientificname);
            JSONArray liste = jsonRoot.getJSONArray("features");
            for (int i=0; i < liste.length(); i++)
            {
                int occurence = liste.getJSONObject(i).getJSONObject("properties").getInt("n");
                total = total + occurence;
            }
            String retourscientificname = scientificname.replaceAll("%20", " ");
            retour = retour + "\nTotal number of " + retourscientificname + " recorded on Earth = " + total;
            ListRetour.add(retour);
            ListRetour.add(valueOf(total));
            return ListRetour;
        }

        if(geohash.equals("")){
            JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/" + geohashprecision +"?scientificname=" + scientificname);
            JSONArray liste = jsonRoot.getJSONArray("features");
            int minimum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");
            int maximum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");

            //Creation des legendes
            int PasLegende = (maximum/12);

            for (int i=0; i < liste.length(); i++)
            {
                ArrayList<Double> GPSZone = new ArrayList<Double>();
                retour = retour + "\n\nArea " + (i+1) + " coordinates :\n";
                int occurence = liste.getJSONObject(i).getJSONObject("properties").getInt("n");
                JSONArray listecoordinate = liste.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
                for (int t=0; t < listecoordinate.length(); t++)
                {
                    String GPS1 = valueOf(listecoordinate.getJSONArray(t).getDouble(0));
                    String GPS2 = valueOf(listecoordinate.getJSONArray(t).getDouble(1));
                    //Ajout des coordonnés GPS dans une liste pour déterminer le centre de la zone
                    GPSZone.add(listecoordinate.getJSONArray(t).getDouble(0));
                    GPSZone.add(listecoordinate.getJSONArray(t).getDouble(1));
                    retour = retour + "\n[" + GPS1 + "," + GPS2 + "]";
                }
                if (occurence < minimum)
                {
                    minimum = occurence;
                }
                if (occurence > maximum)
                {
                    maximum = occurence;
                }
                total = total + occurence;
                retour = retour + "\n\nNumber of occurrences = " + occurence;

                //Affichage des resultats sur la planete
                Histogramme.add(displaySpecie(occurence, PasLegende, GPSZone));

            }
            retour = retour + "\n\nTotal occurrences = "+total+"\nMinimum = "+minimum+"\nMaximum = "+maximum;
            ListRetour.add(retour);
            ListRetour.add(valueOf(maximum));
            displayHistogramme(parent, Histogramme);
            return ListRetour;
        }
        else {
            JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/" + geohashprecision +"?scientificname=" + scientificname +"&geometry=" + geohash);
            JSONArray liste = jsonRoot.getJSONArray("features");
            int minimum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");
            int maximum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");

            //Creation des legendes
            int PasLegende = (maximum/12);

            for (int i=0; i < liste.length(); i++)
            {
                ArrayList<Double> GPSZone = new ArrayList<Double>();
                retour = retour + "\n\nArea " + (i+1) + " coordinates :\n";
                int occurence = liste.getJSONObject(i).getJSONObject("properties").getInt("n");
                JSONArray listecoordinate = liste.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
                for (int t=0; t < listecoordinate.length(); t++)
                {
                    String GPS1 = valueOf(listecoordinate.getJSONArray(t).getDouble(0));
                    String GPS2 = valueOf(listecoordinate.getJSONArray(t).getDouble(1));
                    //Ajout des coordonnés GPS dans une liste pour déterminer le centre de la zone
                    GPSZone.add(listecoordinate.getJSONArray(t).getDouble(0));
                    GPSZone.add(listecoordinate.getJSONArray(t).getDouble(1));
                    retour = retour + "\n[" + GPS1 + "," + GPS2 + "]";
                }
                if (occurence < minimum)
                {
                    minimum = occurence;
                }
                if (occurence > maximum)
                {
                    maximum = occurence;
                }
                total = total + occurence;
                retour = retour + "\n\nNumber of occurrences = " + occurence;

                //Affichage des resultats sur la planete
                Histogramme.add(displaySpecie(occurence, PasLegende, GPSZone));

            }
            retour = retour + "\n\nTotal occurrences = "+total+"\nMinimum = "+minimum+"\nMaximum = "+maximum;
            ListRetour.add(retour);
            ListRetour.add(valueOf(maximum));
            displayHistogramme(parent, Histogramme);
            return ListRetour;
        }
    }



    public ArrayList<String> AutoCompletion(String letter){
        JSONArray jsonRoot = readJsonFromUrlArray("https://api.obis.org/v3/taxon/complete/verbose/"+letter);
        ArrayList<String> ListeRetour = new ArrayList<String>();
        for(int i=0; i<jsonRoot.length(); i++){
            String SName = jsonRoot.getJSONObject(i).getString("scientificName");
            ListeRetour.add(SName);

        }
        return ListeRetour;
    }

    public ArrayList<String> OccurenceWithDate(String scientificname, String geohashprecision, String startdate, String enddate, Group parent) {

        ArrayList<String> ListRetour = new ArrayList<String>();
        ArrayList<Group> Histogramme = new ArrayList<Group>();
        scientificname = scientificname.replaceAll(" ", "%20");
        int total = 0;
        String retour = "";
        if (geohashprecision.equals("")) {
            geohashprecision = "3";
        }

        else if (geohashprecision.equals("0")) {
            JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/1?scientificname=" + scientificname + "&startdate=" + startdate + "&enddate=" + enddate);
            JSONArray liste = jsonRoot.getJSONArray("features");
            for (int i = 0; i < liste.length(); i++) {
                int occurence = liste.getJSONObject(i).getJSONObject("properties").getInt("n");
                total = total + occurence;
            }
            String retourscientificname = scientificname.replaceAll("%20", " ");
            retour = retour + "\nTotal number of " + retourscientificname + " recorded on Earth = " + total;
            ListRetour.add(retour);
            ListRetour.add(valueOf(total));
            return ListRetour;
        }

        else if (startdate.equals("")) {
            JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/" + geohashprecision +"?scientificname=" + scientificname + "&enddate=" + enddate);
            JSONArray liste = jsonRoot.getJSONArray("features");
            int minimum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");
            int maximum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");

            //Creation des legendes
            int PasLegende = (maximum/12);

            for (int i = 0; i < liste.length(); i++) {

                ArrayList<Double> GPSZone = new ArrayList<Double>();
                retour = retour + "\n\nArea " + (i + 1) + " coordinates :\n";
                int occurence = liste.getJSONObject(i).getJSONObject("properties").getInt("n");
                JSONArray listecoordinate = liste.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
                for (int t = 0; t < listecoordinate.length(); t++) {
                    String GPS1 = valueOf(listecoordinate.getJSONArray(t).getDouble(0));
                    String GPS2 = valueOf(listecoordinate.getJSONArray(t).getDouble(1));
                    //Ajout des coordonnés GPS dans une liste pour déterminer le centre de la zone
                    GPSZone.add(listecoordinate.getJSONArray(t).getDouble(0));
                    GPSZone.add(listecoordinate.getJSONArray(t).getDouble(1));
                    retour = retour + "\n[" + GPS1 + "," + GPS2 + "]";
                }
                if (occurence < minimum) {
                    minimum = occurence;
                }
                if (occurence > maximum) {
                    maximum = occurence;
                }
                total = total + occurence;
                retour = retour + "\n\nNumber of occurrences = " + occurence;
                //Affichage des resultats sur la planete
                Histogramme.add(displaySpecie(occurence, PasLegende, GPSZone));

            }
            retour = retour + "\n\nTotal occurrences = " + total + "\nMinimum = " + minimum + "\nMaximum = " + maximum;
            ListRetour.add(retour);
            ListRetour.add(valueOf(maximum));
            displayHistogramme(parent, Histogramme);
            return ListRetour;

        }
        else if (enddate.equals("")) {
            JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/" + geohashprecision +"?scientificname=" + scientificname + "&startdate=" + startdate);
            if ((jsonRoot.getJSONArray("features").isEmpty())) {
                retour = retour + "\n\nEspece non trouvée ";
                ListRetour.add(retour);
                ListRetour.add(valueOf(total));
                return ListRetour;
            }
            else {
                JSONArray liste = jsonRoot.getJSONArray("features");
                int minimum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");
                int maximum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");
                //Creation des legendes
                int PasLegende = (maximum/12);
                for (int i = 0; i < liste.length(); i++) {

                    ArrayList<Double> GPSZone = new ArrayList<Double>();
                    retour = retour + "\n\nArea " + (i + 1) + " coordinates :\n";
                    int occurence = liste.getJSONObject(i).getJSONObject("properties").getInt("n");
                    JSONArray listecoordinate = liste.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
                    for (int t = 0; t < listecoordinate.length(); t++) {
                        String GPS1 = valueOf(listecoordinate.getJSONArray(t).getDouble(0));
                        String GPS2 = valueOf(listecoordinate.getJSONArray(t).getDouble(1));
                        //Ajout des coordonnés GPS dans une liste pour déterminer le centre de la zone
                        GPSZone.add(listecoordinate.getJSONArray(t).getDouble(0));
                        GPSZone.add(listecoordinate.getJSONArray(t).getDouble(1));
                        retour = retour + "\n[" + GPS1 + "," + GPS2 + "]";
                    }
                    if (occurence < minimum) {
                        minimum = occurence;
                    }
                    if (occurence > maximum) {
                        maximum = occurence;
                    }
                    total = total + occurence;
                    retour = retour + "\n\nNumber of occurrences = " + occurence;
                    //Affichage des resultats sur la planete
                    Histogramme.add(displaySpecie(occurence, PasLegende, GPSZone));

                }
                retour = retour + "\n\nTotal occurrences = " + total + "\nMinimum = " + minimum + "\nMaximum = " + maximum;
                ListRetour.add(retour);
                ListRetour.add(valueOf(maximum));
                displayHistogramme(parent, Histogramme);
                return ListRetour;
            }
        }

        JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/" + geohashprecision + "?scientificname=" + scientificname + "&startdate=" + startdate + "&enddate=" + enddate);
        if (jsonRoot.getJSONArray("features").isEmpty()) {
            retour = retour + "\n\nEspece non trouvée ";
            ListRetour.add(retour);
            ListRetour.add(valueOf(total));
            return ListRetour;
        }

        else {
            JSONArray liste = jsonRoot.getJSONArray("features");
            int minimum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");
            int maximum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");
            //Creation des legendes
            int PasLegende = (maximum/12);
            for (int i = 0; i < liste.length(); i++) {

                ArrayList<Double> GPSZone = new ArrayList<Double>();
                retour = retour + "\n\nArea " + (i + 1) + " coordinates :\n";
                int occurence = liste.getJSONObject(i).getJSONObject("properties").getInt("n");
                JSONArray listecoordinate = liste.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
                for (int t = 0; t < listecoordinate.length(); t++) {
                    String GPS1 = valueOf(listecoordinate.getJSONArray(t).getDouble(0));
                    String GPS2 = valueOf(listecoordinate.getJSONArray(t).getDouble(1));
                    //Ajout des coordonnés GPS dans une liste pour déterminer le centre de la zone
                    GPSZone.add(listecoordinate.getJSONArray(t).getDouble(0));
                    GPSZone.add(listecoordinate.getJSONArray(t).getDouble(1));
                    retour = retour + "\n[" + GPS1 + "," + GPS2 + "]";
                }
                if (occurence < minimum) {
                    minimum = occurence;
                }
                if (occurence > maximum) {
                    maximum = occurence;
                }
                total = total + occurence;
                retour = retour + "\n\nNumber of occurrences = " + occurence;
                //Affichage des resultats sur la planete
                Histogramme.add(displaySpecie(occurence, PasLegende, GPSZone));

            }
            retour = retour + "\n\nTotal occurrences = " + total + "\nMinimum = " + minimum + "\nMaximum = " + maximum;
            ListRetour.add(retour);
            ListRetour.add(valueOf(maximum));
            displayHistogramme(parent, Histogramme);
            return ListRetour;
        }

    }

    public void Animation(String scientificname, String startdate, String enddate, Group parent) throws Exception {

        ArrayList<Group> Histogramme = new ArrayList<Group>();
        ArrayList<Group> Histogramme2 = new ArrayList<Group>();

        String datedebut = startdate;
        String datefin = enddate;

        //Pattern de la date
        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");

        //Changement de format des dates
        Date datestart = dateFormat.parse(startdate);
        Date datefinal = dateFormat.parse(enddate);
        Date dateend = dateFormat.parse(enddate);

        //Ajout des 5 ans
        Calendar c = Calendar.getInstance();
        c.setTime(datestart);
        c.add(Calendar.YEAR, 5);
        dateend = c.getTime();
        datefin = dateFormat.format(dateend);

        scientificname = scientificname.replaceAll(" ", "%20");

        while(datestart.compareTo(datefinal) < 0){
            JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/3?scientificname=" + scientificname + "&startdate=" + datedebut + "&enddate=" + datefin);
            JSONArray liste = jsonRoot.getJSONArray("features");
            int maximum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");

            //Creation des legendes
            int PasLegende = (maximum / 12);
            for (int i = 0; i < liste.length(); i++) {

                ArrayList<Double> GPSZone = new ArrayList<Double>();
                int occurence = liste.getJSONObject(i).getJSONObject("properties").getInt("n");
                JSONArray listecoordinate = liste.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
                for (int t = 0; t < listecoordinate.length(); t++) {
                    //Ajout des coordonnés GPS dans une liste pour déterminer le centre de la zone
                    GPSZone.add(listecoordinate.getJSONArray(t).getDouble(0));
                    GPSZone.add(listecoordinate.getJSONArray(t).getDouble(1));
                }
                Histogramme.add(displaySpecie(occurence, PasLegende, GPSZone));
            }
            displayHistogramme(parent, Histogramme);
            c.clear();
            c.setTime(dateend);
            c.add(Calendar.YEAR, 5);
            datestart = c.getTime();
            datedebut = dateFormat.format(datestart);

            Thread.sleep(8000);

            JSONObject jsonRoot2 = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/3?scientificname=" + scientificname + "&startdate=" + datefin + "&enddate=" + datedebut);
            JSONArray liste2 = jsonRoot2.getJSONArray("features");
            int maximum2 = liste2.getJSONObject(0).getJSONObject("properties").getInt("n");
            //Creation des legendes
            int PasLegende2 = (maximum2 / 12);
            for (int i = 0; i < liste.length(); i++) {

                ArrayList<Double> GPSZone = new ArrayList<Double>();
                int occurence = liste.getJSONObject(i).getJSONObject("properties").getInt("n");
                JSONArray listecoordinate = liste.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
                for (int t = 0; t < listecoordinate.length(); t++) {
                    //Ajout des coordonnés GPS dans une liste pour déterminer le centre de la zone
                    GPSZone.add(listecoordinate.getJSONArray(t).getDouble(0));
                    GPSZone.add(listecoordinate.getJSONArray(t).getDouble(1));
                }
                Histogramme2.add(displaySpecie(occurence, PasLegende2, GPSZone));
            }
            undisplayHistogramme(parent);
            displayHistogramme(parent, Histogramme2);
            c.clear();
            c.setTime(datestart);
            c.add(Calendar.YEAR, 5);
            dateend = c.getTime();
            datefin = dateFormat.format(dateend);
        }

    }

    public Group displaySpecie(int occurence, int PasLegende, ArrayList<Double> GPSZone)
    {

        Color color = Color.RED;
        double longitude = (GPSZone.get(0)+ GPSZone.get(2))/2.0;
        double latitude = (GPSZone.get(1)+ GPSZone.get(5))/2.0;
        double taille = occurence*0.0001;
        if(occurence > 10*PasLegende){
            color = Color.RED;
        }
        else if (occurence > 8*PasLegende){
            color = Color.DARKORANGE;
        }
        else if (occurence > 5*PasLegende){
            color = Color.YELLOW;
        }
        else if (occurence > 2*PasLegende){
            color = Color.GREEN;
        }
        else if (occurence > PasLegende){
            color = Color.CYAN;
        }
        else if (occurence > 0){
            color = Color.BLUE;
        }

        Point3D from = geoCoordTo3dCoord(latitude, longitude);
        Box box = new Box(0.005,0.005,taille);
        box.setId(valueOf(latitude));
        Point3D to = Point3D.ZERO;
        Point3D yDir = new Point3D(0, 1, 0);

        // Town Color
        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(color);
        greenMaterial.setSpecularColor(color);
        box.setMaterial(greenMaterial);

        Group groupbox = new Group();
        Affine affine = new Affine();
        affine.append(lookAt(from,to,yDir));
        groupbox.getTransforms().setAll(affine);
        groupbox.getChildren().addAll(box);
        return groupbox;
        //Add an animation timer
    }

    public ArrayList<String> getSpecie(String geohash){
        JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence?geometry="+ geohash +"&fields=family&size=150");
        int nbbug = 0;
        JSONArray liste = jsonRoot.getJSONArray("results");
        ArrayList<String> ListeRetour = new ArrayList<String>();
        for(int i=0; i<liste.length(); i++){
            if(liste.getJSONObject(i).isEmpty()){
                nbbug = nbbug+1;
            }
            else{
                String SName = liste.getJSONObject(i).getString("family");
                if(!ListeRetour.contains(SName)){
                    ListeRetour.add(SName);
                }
            }
        }
        Collections.sort(ListeRetour);
        return ListeRetour;
    }

    public String getSpecieInformations(String scientificname, String geohash){
        JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence?scientificname=" + scientificname+ "&geometry="+ geohash +"&fields=scientificName%2Corder%2Csuperclass%2CrecordedBy%2Cspecies&size=150");
        if(scientificname.equals("")){
            jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence?geometry="+ geohash +"&fields=scientificName%2Corder%2Csuperclass%2CrecordedBy%2Cspecies&size=150");
        }
        int nbbug = 0;
        JSONArray liste = jsonRoot.getJSONArray("results");
        String Retour = "";
        for(int i=0; i<liste.length(); i++){
            if(liste.getJSONObject(i).isEmpty()){
                nbbug = nbbug+1;
            }
            else{
                String SName;
                String species;
                String superclass;
                String order;
                String recordedby;
                if(liste.getJSONObject(i).has("scientificName")){
                    SName = liste.getJSONObject(i).getString("scientificName");
                } else {
                    SName = "Aucune information";
                }
                if(liste.getJSONObject(i).has("specie")){
                    species = liste.getJSONObject(i).getString("specie");
                } else {
                    species = "Aucune information";
                }
                if(liste.getJSONObject(i).has("superclass")){
                    superclass = liste.getJSONObject(i).getString("superclass");
                } else {
                    superclass = "Aucune information";
                }
                if(liste.getJSONObject(i).has("order")){
                    order = liste.getJSONObject(i).getString("order");
                } else {
                    order = "Aucune information";
                }
                if(liste.getJSONObject(i).has("recordedBy")){
                    recordedby = liste.getJSONObject(i).getString("recordedBy");
                } else {
                    recordedby = "Aucune information";
                }
                Retour = Retour + "\n\n[Scientific Name : " + SName +",\nSpecie : "+ species +",\nSuperclass : "+ superclass +",\nRecorded By : " + recordedby +",\nOrder : "+ order +"]";
            }
        }
        return Retour;
    }





    public void displayHistogramme(Group parent, ArrayList<Group> Histogramme){
        for(Group g : Histogramme){
            parent.getChildren().addAll(g);
        }
    }

    public void undisplayHistogramme(Group parent){
        parent.getChildren().clear();
    }

    public static Affine lookAt(Point3D from, Point3D to, Point3D ydir) {
        Point3D zVec = to.subtract(from).normalize();
        Point3D xVec = ydir.normalize().crossProduct(zVec).normalize();
        Point3D yVec = zVec.crossProduct(xVec).normalize();
        return new Affine(xVec.getX(), yVec.getX(), zVec.getX(), from.getX(),
                xVec.getY(), yVec.getY(), zVec.getY(), from.getY(),
                xVec.getZ(), yVec.getZ(), zVec.getZ(), from.getZ());
    }

    /*
    public void Animation(String scientificname, String startdate, String enddate, Group parent) throws Exception {

        ArrayList<Group> Histogramme = new ArrayList<Group>();
        ArrayList<Group> Histogramme2 = new ArrayList<Group>();

        String datedebut = startdate;
        String datefin = enddate;

        //Pattern de la date
        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");

        //Changement de format des dates
        Date datestart = dateFormat.parse(startdate);
        Date datefinal = dateFormat.parse(enddate);
        Date dateend = dateFormat.parse(enddate);

        //Ajout des 5 ans
        Calendar c = Calendar.getInstance();
        c.setTime(datestart);
        c.add(Calendar.YEAR, 5);
        dateend = c.getTime();
        datefin = dateFormat.format(dateend);

        scientificname = scientificname.replaceAll(" ", "%20");

        while(datestart.compareTo(datefinal) < 0){
            JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/3?scientificname=" + scientificname + "&startdate=" + datedebut + "&enddate=" + datefin);
            JSONArray liste = jsonRoot.getJSONArray("features");
            int maximum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");

            //Creation des legendes
            int PasLegende = (maximum / 12);
            for (int i = 0; i < liste.length(); i++) {

                ArrayList<Double> GPSZone = new ArrayList<Double>();
                int occurence = liste.getJSONObject(i).getJSONObject("properties").getInt("n");
                JSONArray listecoordinate = liste.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
                for (int t = 0; t < listecoordinate.length(); t++) {
                    //Ajout des coordonnés GPS dans une liste pour déterminer le centre de la zone
                    GPSZone.add(listecoordinate.getJSONArray(t).getDouble(0));
                    GPSZone.add(listecoordinate.getJSONArray(t).getDouble(1));
                }
                Histogramme.add(displaySpecie(occurence, PasLegende, GPSZone));
                c.clear();
                c.setTime(dateend);
                c.add(Calendar.YEAR, 5);
                datestart = c.getTime();
                datedebut = dateFormat.format(datestart);
            }

            JSONObject jsonRoot2 = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/3?scientificname=" + scientificname + "&startdate=" + datefin + "&enddate=" + datedebut);
            JSONArray liste2 = jsonRoot2.getJSONArray("features");
            int maximum2 = liste2.getJSONObject(0).getJSONObject("properties").getInt("n");
            //Creation des legendes
            int PasLegende2 = (maximum2 / 12);
            for (int i = 0; i < liste.length(); i++) {

                ArrayList<Double> GPSZone = new ArrayList<Double>();
                int occurence = liste.getJSONObject(i).getJSONObject("properties").getInt("n");
                JSONArray listecoordinate = liste.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
                for (int t = 0; t < listecoordinate.length(); t++) {
                    //Ajout des coordonnés GPS dans une liste pour déterminer le centre de la zone
                    GPSZone.add(listecoordinate.getJSONArray(t).getDouble(0));
                    GPSZone.add(listecoordinate.getJSONArray(t).getDouble(1));
                }
                Histogramme2.add(displaySpecie(occurence, PasLegende2, GPSZone));
                for(Group e : Histogramme){
                    String id = e.getChildren().get(0).getId();
                    for(Group f : Histogramme2){
                        String id2 = f.getChildren().get(0).getId();

                        if(id2.equals(id)){
                            Box histo = ((Box) e.getChildren().get(0));
                            Box histo2 = ((Box) f.getChildren().get(0));
                            double taille1 = histo.getWidth();
                            double taille2 = histo2.getWidth();
                            double pas = (taille2 - taille1)/5.0;
                            //Add an animation timer
                            final long startNanoTime = System.nanoTime();
                            new AnimationTimer() {
                                public void handle(long currentNanoTime) {
                                    double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                                    histo.setWidth(taille1+t*pas);
                                    if(t % 0.5 == 0){
                                        displayHistogramme(parent, Histogramme);
                                    }


                                    //histo.setWidth(taille2);
                                }
                            }.start();
                        }
                    }
                }
                //displayHistogramme(parent, Histogramme2);
            }
            c.clear();
            c.setTime(datestart);
            c.add(Calendar.YEAR, 5);
            dateend = c.getTime();
            datefin = dateFormat.format(dateend);
        }

    }*/


}
