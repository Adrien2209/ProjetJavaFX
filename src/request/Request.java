package request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static sample.API.readJsonFromUrl;
import static sample.API.readJsonFromUrlArray;

public class Request {


    public String GlobalOccurenceScientificName(String scientificname, String geohash)
    {
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
            return retour;
        }

        JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/" + geohash +"?scientificname=" + scientificname);
        JSONArray liste = jsonRoot.getJSONArray("features");
        int minimum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");
        int maximum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");
        for (int i=0; i < liste.length(); i++)
        {
            retour = retour + "\n\nArea " + (i+1) + " coordinates :\n";
            int occurence = liste.getJSONObject(i).getJSONObject("properties").getInt("n");
            JSONArray listecoordinate = liste.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
            for (int t=0; t < listecoordinate.length(); t++)
            {
                String GPS1 = String.valueOf(listecoordinate.getJSONArray(t).getDouble(0));
                String GPS2 = String.valueOf(listecoordinate.getJSONArray(t).getDouble(1));
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

        }
        retour = retour + "\n\nTotal occurrences = "+total+"\nMinimum = "+minimum+"\nMaximum = "+maximum;
        return retour;

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

    public String OccurenceWithDate(String scientificname, String geohash, String startdate, String enddate) {
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
            return retour;
        }

        else if (startdate.equals("")) {
            JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/1?scientificname=" + scientificname + "&enddate=" + enddate);
            JSONArray liste = jsonRoot.getJSONArray("features");
            int minimum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");
            int maximum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");
            for (int i = 0; i < liste.length(); i++) {
                retour = retour + "\n\nArea " + (i + 1) + " coordinates :\n";
                int occurence = liste.getJSONObject(i).getJSONObject("properties").getInt("n");
                JSONArray listecoordinate = liste.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
                for (int t = 0; t < listecoordinate.length(); t++) {
                    String GPS1 = String.valueOf(listecoordinate.getJSONArray(t).getDouble(0));
                    String GPS2 = String.valueOf(listecoordinate.getJSONArray(t).getDouble(1));
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

            }
            retour = retour + "\n\nTotal occurrences = " + total + "\nMinimum = " + minimum + "\nMaximum = " + maximum;
            return retour;

        }
        else if (enddate.equals("")) {
            JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/1?scientificname=" + scientificname + "&startdate=" + startdate);
            if (!jsonRoot.isEmpty()) {
                retour = retour + "\n\nEspece non trouvée ";
                return retour;
            }
            else {
                JSONArray liste = jsonRoot.getJSONArray("features");
                int minimum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");
                int maximum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");
                for (int i = 0; i < liste.length(); i++) {
                    retour = retour + "\n\nArea " + (i + 1) + " coordinates :\n";
                    int occurence = liste.getJSONObject(i).getJSONObject("properties").getInt("n");
                    JSONArray listecoordinate = liste.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
                    for (int t = 0; t < listecoordinate.length(); t++) {
                        String GPS1 = String.valueOf(listecoordinate.getJSONArray(t).getDouble(0));
                        String GPS2 = String.valueOf(listecoordinate.getJSONArray(t).getDouble(1));
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

                }
                retour = retour + "\n\nTotal occurrences = " + total + "\nMinimum = " + minimum + "\nMaximum = " + maximum;
                return retour;
            }
        }

        JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/" + geohash + "?scientificname=" + scientificname + "&startdate=" + startdate + "&enddate=" + enddate);
        if (!jsonRoot.isEmpty()) {
            retour = retour + "\n\nEspece non trouvée ";
            return retour;
        }

        else {
            JSONArray liste = jsonRoot.getJSONArray("features");
            int minimum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");
            int maximum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");
            for (int i = 0; i < liste.length(); i++) {
                retour = retour + "\n\nArea " + (i + 1) + " coordinates :\n";
                int occurence = liste.getJSONObject(i).getJSONObject("properties").getInt("n");
                JSONArray listecoordinate = liste.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
                for (int t = 0; t < listecoordinate.length(); t++) {
                    String GPS1 = String.valueOf(listecoordinate.getJSONArray(t).getDouble(0));
                    String GPS2 = String.valueOf(listecoordinate.getJSONArray(t).getDouble(1));
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

            }
            retour = retour + "\n\nTotal occurrences = " + total + "\nMinimum = " + minimum + "\nMaximum = " + maximum;
            return retour;
        }

    }

}
