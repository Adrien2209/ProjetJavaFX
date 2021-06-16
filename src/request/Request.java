package request;

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

import java.util.ArrayList;
import java.util.List;

import static sample.API.readJsonFromUrl;
import static sample.API.readJsonFromUrlArray;
import static view.View.geoCoordTo3dCoord;

public class Request {

    public ArrayList<String> GlobalOccurenceScientificName(String scientificname, String geohash, Group parent)
    {
        ArrayList<String> ListRetour = new ArrayList<String>();
        scientificname = scientificname.replaceAll(" ", "%20");
        int total = 0;
        String retour = "";
        if (geohash.equals(""))
        {
            geohash = "3";
        }

        if (geohash.equals("0"))
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
            ListRetour.add(String.valueOf(total));
            return ListRetour;
        }

        JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/" + geohash +"?scientificname=" + scientificname);
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
                String GPS1 = String.valueOf(listecoordinate.getJSONArray(t).getDouble(0));
                String GPS2 = String.valueOf(listecoordinate.getJSONArray(t).getDouble(1));
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
            displaySpecie(parent, scientificname, occurence, PasLegende, GPSZone);

        }
        retour = retour + "\n\nTotal occurrences = "+total+"\nMinimum = "+minimum+"\nMaximum = "+maximum;
        ListRetour.add(retour);
        ListRetour.add(String.valueOf(maximum));
        return ListRetour;

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

    public ArrayList<String> OccurenceWithDate(String scientificname, String geohash, String startdate, String enddate, Group parent) {

        ArrayList<String> ListRetour = new ArrayList<String>();
        scientificname = scientificname.replaceAll(" ", "%20");
        int total = 0;
        String retour = "";
        if (geohash.equals("")) {
            geohash = "3";
        }

        else if (geohash.equals("0")) {
            JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/1?scientificname=" + scientificname + "&startdate=" + startdate + "&enddate=" + enddate);
            JSONArray liste = jsonRoot.getJSONArray("features");
            for (int i = 0; i < liste.length(); i++) {
                int occurence = liste.getJSONObject(i).getJSONObject("properties").getInt("n");
                total = total + occurence;
            }
            String retourscientificname = scientificname.replaceAll("%20", " ");
            retour = retour + "\nTotal number of " + retourscientificname + " recorded on Earth = " + total;
            ListRetour.add(retour);
            ListRetour.add(String.valueOf(total));
            return ListRetour;
        }

        else if (startdate.equals("")) {
            JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/" + geohash +"?scientificname=" + scientificname + "&enddate=" + enddate);
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
                    String GPS1 = String.valueOf(listecoordinate.getJSONArray(t).getDouble(0));
                    String GPS2 = String.valueOf(listecoordinate.getJSONArray(t).getDouble(1));
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
                displaySpecie(parent, scientificname, occurence, PasLegende, GPSZone);

            }
            retour = retour + "\n\nTotal occurrences = " + total + "\nMinimum = " + minimum + "\nMaximum = " + maximum;
            ListRetour.add(retour);
            ListRetour.add(String.valueOf(maximum));
            return ListRetour;

        }
        else if (enddate.equals("")) {
            JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/" + geohash +"?scientificname=" + scientificname + "&startdate=" + startdate);
            if ((jsonRoot.getJSONArray("features").isEmpty())) {
                retour = retour + "\n\nEspece non trouvée ";
                ListRetour.add(retour);
                ListRetour.add(String.valueOf(total));
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
                        String GPS1 = String.valueOf(listecoordinate.getJSONArray(t).getDouble(0));
                        String GPS2 = String.valueOf(listecoordinate.getJSONArray(t).getDouble(1));
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
                    displaySpecie(parent, scientificname, occurence, PasLegende, GPSZone);

                }
                retour = retour + "\n\nTotal occurrences = " + total + "\nMinimum = " + minimum + "\nMaximum = " + maximum;
                ListRetour.add(retour);
                ListRetour.add(String.valueOf(maximum));
                return ListRetour;
            }
        }

        JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/" + geohash + "?scientificname=" + scientificname + "&startdate=" + startdate + "&enddate=" + enddate);
        if (jsonRoot.getJSONArray("features").isEmpty()) {
            retour = retour + "\n\nEspece non trouvée ";
            ListRetour.add(retour);
            ListRetour.add(String.valueOf(total));
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
                    String GPS1 = String.valueOf(listecoordinate.getJSONArray(t).getDouble(0));
                    String GPS2 = String.valueOf(listecoordinate.getJSONArray(t).getDouble(1));
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
                displaySpecie(parent, scientificname, occurence, PasLegende, GPSZone);

            }
            retour = retour + "\n\nTotal occurrences = " + total + "\nMinimum = " + minimum + "\nMaximum = " + maximum;
            ListRetour.add(retour);
            ListRetour.add(String.valueOf(maximum));
            return ListRetour;
        }

    }

    public void displaySpecie(Group parent, String name, int occurence, int PasLegende, ArrayList<Double> GPSZone)
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
        parent.setId(name);
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

        parent.getChildren().addAll(groupbox);
    }

    public static Affine lookAt(Point3D from, Point3D to, Point3D ydir) {
        Point3D zVec = to.subtract(from).normalize();
        Point3D xVec = ydir.normalize().crossProduct(zVec).normalize();
        Point3D yVec = zVec.crossProduct(xVec).normalize();
        return new Affine(xVec.getX(), yVec.getX(), zVec.getX(), from.getX(),
                xVec.getY(), yVec.getY(), zVec.getY(), from.getY(),
                xVec.getZ(), yVec.getZ(), zVec.getZ(), from.getZ());
    }
    
}
