package view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import request.Request;

public class View {

    Request request;

    @FXML
    private TextField ScientificName;

    @FXML
    private AnchorPane OBIS3D;

    public View()
    {
        request = new Request();
    }

    @FXML
    public void initialize()
    {
        ScientificName.setOnKeyPressed(event ->
        {
            if ( event.getCode() == KeyCode.ENTER)
            {
                String Name = ScientificName.getText();
                request.GlobalOccurenceScientificName(Name);
            }
        } );

    }
}
