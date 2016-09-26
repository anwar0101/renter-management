/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renter.management;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author anwar
 */
public class DeveloperController implements Initializable {

    @FXML
    private ImageView lvAdmin;
    @FXML
    private Button btnCancel;
    @FXML
    private ImageView lvAdmin1;
    private RenterManagement application;

    
    public void setApp(RenterManagement application) {
        this.application = application;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void gotoHome(ActionEvent event) {
        application.gotoHome();
    }
    
}
