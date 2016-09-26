/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renter.management;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import renter.db.DBConn;
import renter.model.Renter;

/**
 * FXML Controller class
 *
 * @author anwar
 */
public class Add_renterController extends AnchorPane implements Initializable {

    private RenterManagement application;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfFather;
    @FXML
    private TextField tfVillage;
    @FXML
    private TextField tfPost;
    @FXML
    private TextField tfThana;
    @FXML
    private TextField tfZilla;
    @FXML
    private TextField tfMobile;
    @FXML
    private TextField tfNID;
    @FXML
    private TextField tfPassport;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfRef;
    @FXML
    private TextField tfHouse;
    @FXML
    private TextField tfAmount;
    private Renter renter;
    private Sql2o sql2o;
    @FXML
    private ImageView ivRenterProfile;
    private Image image;
    private byte[] imagebyte;

    public void setApp(RenterManagement application) {
        this.application = application;
    }

    private boolean isRepeate = true;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        sql2o = new Sql2o(DBConn.DBURL, DBConn.USERNAME, DBConn.PASSWORD);
    }

    @FXML
    private void addRenter(ActionEvent event) throws SQLException, IOException {
        
        //:id:name:fatherName:address:phoneNo:email:nidNo:passportNo:refBy:houseNo:houseRent:photo
        // r_id,r_name,r_father,r_address,r_phoneno,r_email,r_houseno,r_houserent,r_nid,r_passport,r_ref
        
        
        try(Connection conn = sql2o.open()){
            
            String sql = "insert into app.renter(r_id,"
                    + "r_name,"
                    + "r_father,"
                    + "r_address,"
                    + "r_phoneno,"
                    + "r_email,"
                    + "r_houseno,"
                    + "r_houserent,"
                    + "r_nid,"
                    + "r_passport,"
                    + "r_ref,"
                    + "is_active, "
                    + "r_photo) values (:r_id,:r_name,:r_father,:r_address,"
                    + ":r_phoneno, :r_email, :r_houseno, :r_houserent, :r_nid,"
                    + ":r_passport, :r_ref, :is_active, :r_photo)";

            renter = new Renter(
                    new Random().nextInt(1000 - 100) + 1,
                    tfName.getText(),
                    tfFather.getText(),
                    tfVillage.getText() + ", " + tfPost.getText() + ", "  + tfThana.getText() + ", "  + tfZilla.getText(),
                    tfMobile.getText(),
                    tfEmail.getText(),
                    tfNID.getText(),
                    tfPassport.getText(),
                    tfRef.getText(),
                    tfHouse.getText(),
                    Integer.parseInt(tfAmount.getText()),
                    imagebyte,
                    true
            );

            conn.createQuery(sql).bind(renter).executeUpdate();
            Alert alart = new Alert(Alert.AlertType.INFORMATION, "Successfylly Added.", ButtonType.OK);
            alart.showAndWait();
            
            //clear all
            tfName.setText("");
            tfFather.setText("");
            tfVillage.setText("");
            tfMobile.setText("");
            tfEmail.setText("");
            tfNID.setText("");
            tfPassport.setText("");
            tfRef.setText("");
            tfHouse.setText("");
            tfAmount.setText("");
            tfPost.setText("");
            tfThana.setText("");
            tfZilla.setText("");
           // ivRenterProfile.setImage();
        }
        
    }
    
    //file choser configration
    private void configureFileChooser(final FileChooser fileChooser){
        fileChooser.setTitle("Select Renter Profile Picture");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.jpg","*.png", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
                //new FileChooser.ExtensionFilter("All Files", "*.*")
        );
    }

    @FXML
    private void gotoHome(ActionEvent event) {
        isRepeate = true;
        application.gotoHome();
    }

    @FXML
    private void browseImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(application.stage);
        if (file != null) {
            try {
                imagebyte =  Files.readAllBytes(file.toPath());
                image = new Image(file.toURI().toString());
                ivRenterProfile.setImage(image);
            } catch (IOException ex) {
                Logger.getLogger(Add_renterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
