/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renter.management;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import renter.db.DBConn;
import renter.model.User;

/**
 * FXML Controller class
 *
 * @author anwar
 */
public class AdminController implements Initializable {

    @FXML
    private TextField tfName;
    @FXML
    private ImageView lvAdmin;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField tfName1;
    @FXML
    private TextField tfName11;
    private RenterManagement application;
    private Sql2o sql2o;
    private Image image;
    private byte[] imagebyte;
    
    public void setApp(RenterManagement application) {
        this.application = application;
    }
    
    

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        User u = User.getInstance();
        
        tfName.setText(u.getUsername());
        if (u.getImage() != null) {
            // convert byte array back to BufferedImage
//            System.out.println("inside user.");
            try {
                InputStream in = new ByteArrayInputStream(u.getImage());
                BufferedImage buffer = ImageIO.read(in);
                Image img = SwingFXUtils.toFXImage(buffer, null);
                Platform.runLater(() -> {
                    lvAdmin.setImage(img);
                });
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Image im = new Image(RenterManagement.class.getResourceAsStream("usericon.png"));
            lvAdmin.setImage(im);
        }
        
        sql2o = new Sql2o(DBConn.DBURL, DBConn.USERNAME, DBConn.PASSWORD);
    }    

    @FXML
    private void gotoHome(ActionEvent event) {
        application.gotoHome();
    }

    @FXML
    private void browseImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try {
                imagebyte =  Files.readAllBytes(file.toPath());
                image = new Image(file.toURI().toString());
                lvAdmin.setImage(image);
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
    private void backupData(ActionEvent event) {
    }

    @FXML
    private void importData(ActionEvent event) {
    }

    @FXML
    private void updatePassword(ActionEvent event) {
        if(tfName.getText().length() != 0 && tfName1.getText().length() != 0 && tfName11.getText().length() != 0){
            
            User u = User.getInstance();
            u.setUsername(tfName.getText());
            u.setPassword(tfName11.getText());
            u.setId(1);
            u.setImage(imagebyte);
            
            String sql = "update app.admin set username=:username,password=:password where id=:id";
            if(imagebyte != null){
                sql = "update app.admin set username=:username,password=:password , image=:image where id=:id";
            }
            
            try(Connection conn = sql2o.open()){
                conn.createQuery(sql).bind(u).executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Password Changed.", ButtonType.OK);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "TextFiled is empty.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    
}
