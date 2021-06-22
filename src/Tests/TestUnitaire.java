package Tests;

import static org.junit.jupiter.api.Assertions.*;

import javafx.scene.Group;
import org.junit.jupiter.api.Test;


import request.Request;

import java.util.ArrayList;


public class TestUnitaire {

    Request request;

    public TestUnitaire()
    {
        request = new Request();
    }
    Group test = new Group();

    @Test
    public void testAffichageDepart(){
        String maximum = "8147";
        ArrayList<String> resultat;
        resultat = request.AffichageDepart(test);
        assertEquals(resultat.get(1), maximum);
    }

    @Test
    public void testGlobalOccurenceScientificName(){
        String maximum = "8147";
        String maximum2 = "23293";
        ArrayList<String> resultat;
        ArrayList<String> resultat2;
        resultat = request.GlobalOccurenceScientificName("Delphinidae", "3","",test);
        resultat2 = request.GlobalOccurenceScientificName("Delphinidae", "2","",test);
        assertEquals(resultat.get(1), maximum);
        assertEquals(resultat2.get(1), maximum2);
    }

    @Test
    public void testAutoCompletion(){
        ArrayList<String> scientificname = request.AutoCompletion("delphi");
        String completedname = "Delphinidae";
        assertEquals(completedname,scientificname.get(0));
    }

    @Test
    public void testOccurenceWithDate(){
        String maximum = "5612";
        ArrayList<String> resultat;
        resultat = request.OccurenceWithDate("Delphinidae", "3","2010-06-02","",test);
        assertEquals(resultat.get(1), maximum);
    }

    @Test
    public void testGetSpecie(){
        String premierespece = "Acanthuridae";
        ArrayList<String> resultat;
        resultat = request.getSpecie("dh");
        assertEquals(resultat.get(0), premierespece);
    }

    @Test
    public void testGetSpecieInformation(){
        String resultat = request.getSpecieInformations("Dentaliidae", "xg");
        String resultatattendu = "\n\n[Scientific Name : Dentalium mediopacificense,\nSpecie : Aucune information,\nSuperclass : Aucune information,\nRecorded By : Aucune information,\nOrder : Dentaliida]";
        assertEquals(resultatattendu,resultat);
    }


}
