package request;

import org.json.JSONArray;
import org.json.JSONObject;

import static sample.API.readJsonFromUrl;

public class Request {


    public void GlobalOccurenceScientificName(String scientificname)
    {
        int total = 0;

        JSONObject jsonRoot = readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/3?scientificname=" + scientificname);
        JSONArray liste = jsonRoot.getJSONArray("features");
        int minimum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");
        int maximum = liste.getJSONObject(0).getJSONObject("properties").getInt("n");
        for (int i=0; i < liste.length(); i++)
        {
            int occurence = liste.getJSONObject(i).getJSONObject("properties").getInt("n");
            if (occurence < minimum)
            {
                minimum = occurence;
            }
            if (occurence > maximum)
            {
                maximum = occurence;
            }
            total = total + occurence;
        }
        System.out.println(total);
        System.out.println(minimum);
        System.out.println(maximum);
        /*
        JSONObject geometry = jsonRoot.getJSONArray("features").getJSONObject(0).getJSONObject("geometry");
        JSONObject occurences = jsonRoot.getJSONArray("features").getJSONObject(0).getJSONObject("properties");
        System.out.println(geometry.getJSONArray("coordinates"));
        System.out.println(occurences.getInt("n"));
        */
    }

    
}
