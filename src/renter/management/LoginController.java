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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author anwar
 */
public class LoginController extends AnchorPane implements Initializable {

    @FXML
    private TextField userId;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    @FXML
    private Label errorMessage;

    private RenterManagement application;

    public void setApp(RenterManagement application) {
        this.application = application;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        errorMessage.setText("");
        userId.setPromptText("Username");
        password.setPromptText("Password");
        userId.setText("al-amin");
        password.setText("al-amin");
    }

    @FXML
    private void processLogin(ActionEvent event) {
        if (application == null) {
            // We are running in isolated FXML, possibly in Scene Builder.
            // NO-OP.
           // errorMessage.setText("Hello " + userId.getText());
        } else if (!application.userLogging(userId.getText(), password.getText())) {
            errorMessage.setText("Username/Password is incorrect");
        }
    }

}
