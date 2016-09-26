/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renter.management;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import renter.model.User;
import renter.security.Authonicator;

/**
 *
 * @author anwar
 */
public class RenterManagement extends Application {

    public Stage stage;
    private User loggedUser;
    private final double MINIMUM_WINDOW_WIDTH = 800.0;
    private final double MINIMUM_WINDOW_HEIGHT = 600.0;

   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(RenterManagement.class, (java.lang.String[]) null);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            stage = primaryStage;
            stage.setTitle("Somshir Ali Monjil");
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            gotoLogin();
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(RenterManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public boolean userLogging(String userId, String password) {
        Authonicator autho = new Authonicator(userId, password);
        if (Authonicator.validate(userId, password)) {
            loggedUser = User.getInstance();
            loggedUser.setUsername(userId);
            gotoHome();
            return true;
        } else {
            return false;
        }
    }

    private void gotoLogin() {
        try {
            LoginController login = (LoginController) replaceSceneContent("login.fxml");
            login.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(RenterManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void gotoInserBills() {
        try {
            InsertbillController insert = (InsertbillController) replaceSceneContent("insertbill.fxml");
            insert.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(RenterManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void gotoReport() {
        try {
            ReportController report = (ReportController) replaceSceneContent("report.fxml");
            report.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(RenterManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
        
    public void gotoHome() {
        try {
            
            HomeController home = (HomeController) replaceSceneContent("home.fxml");
            home.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(RenterManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public void gotoAddRenter(){
        try {
            Add_renterController addRenter = (Add_renterController) replaceSceneContent("add_renter.fxml");
            addRenter.setApp(this);
        } catch (Exception ex){
            Logger.getLogger(RenterManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public void gotoViewRenter(){
        try {
            ViewprofileController viewRenter = (ViewprofileController) replaceSceneContent("viewprofile.fxml");
            viewRenter.setApp(this);
        } catch (Exception ex){
            Logger.getLogger(RenterManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void gotoAdmin(){
        try {
            AdminController viewRenter = (AdminController) replaceSceneContent("admin.fxml");
            viewRenter.setApp(this);
        } catch (Exception ex){
            Logger.getLogger(RenterManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void gotoDeveloper(){
        try {
            DeveloperController viewRenter = (DeveloperController) replaceSceneContent("developer.fxml");
            viewRenter.setApp(this);
        } catch (Exception ex){
            Logger.getLogger(RenterManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = RenterManagement.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(RenterManagement.class.getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page, 800, 600);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

}
