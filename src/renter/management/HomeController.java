/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renter.management;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.util.Duration;
import jiconfont.icons.FontAwesome;
import jiconfont.javafx.IconFontFX;

/**
 * FXML Controller class
 *
 * @author anwar
 */
public class HomeController extends AnchorPane implements Initializable {

    private RenterManagement application;
    

    private RotateTransition rotate;
    
    private AudioClip audio;
    private Timeline timeline;
    
    @FXML
    private Pane homePane;
    @FXML
    private ImageView ivReport;
    @FXML
    private ImageView ivAdmin;
    @FXML
    private ImageView ivDeveloper;
    @FXML
    private ImageView ivAddRenter;
    @FXML
    private ImageView ivProfile;
    @FXML
    private ImageView ivInsert;
    @FXML
    private Text txtAddR;
    @FXML
    private Text txtViewR;
    @FXML
    private Text txtBillEntry;
    @FXML
    private Text txtBillReport;
    @FXML
    private Text txtAdmin;
    @FXML
    private Text txtDeveloper;
    

    public void setApp(RenterManagement application) {
        this.application = application;
    }

    private void setLight() {
        ivAddRenter.setEffect(new Lighting());
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

        IconFontFX.register(FontAwesome.getIconFont());

        Stop[] stops = new Stop[]{new Stop(0, Color.RED), new Stop(1, Color.BLUE)};
        LinearGradient fill = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        Color stroke = Color.BLACK;
        Image image = IconFontFX.buildImage(FontAwesome.USER_PLUS, 300, fill, stroke);

        audio = new AudioClip(HomeController.class.getResource("Note5.wav").toString());
        
        //set image 
        ivAddRenter.setImage(image);
        ivProfile.setImage(
                IconFontFX.buildImage(FontAwesome.USERS, 300, fill, stroke));
        ivInsert.setImage(
                IconFontFX.buildImage(FontAwesome.BOOK, 300, fill, stroke));
        ivReport.setImage(
                IconFontFX.buildImage(FontAwesome.CALCULATOR, 300, fill, stroke));
        ivAdmin.setImage(
                IconFontFX.buildImage(FontAwesome.USER_SECRET, 300, fill, stroke));
        ivDeveloper.setImage(
                IconFontFX.buildImage(FontAwesome.COGS, 300, fill, stroke));

        ivAddRenter.setOnMouseEntered((MouseEvent event) -> {
            animationScale(ivAddRenter, txtAddR,event);
        });
        ivAddRenter.setOnMouseExited((MouseEvent event) -> {
            animationReset(ivAddRenter,txtAddR);
        });

        ivInsert.setOnMouseEntered((MouseEvent event) -> {
            animationScale(ivInsert, txtBillEntry,event);
        });
        ivInsert.setOnMouseExited((MouseEvent event) -> {
            animationReset(ivInsert, txtBillEntry);
        });

        ivProfile.setOnMouseEntered((MouseEvent event) -> {
            animationScale(ivProfile, txtViewR,event);
        });
        ivProfile.setOnMouseExited((MouseEvent event) -> {
            animationReset(ivProfile, txtViewR);
        });

        ivReport.setOnMouseEntered((MouseEvent event) -> {
            animationScale(ivReport, txtBillReport,event);
        });
        ivReport.setOnMouseExited((MouseEvent event) -> {
            animationReset(ivReport, txtBillReport);
        });

        ivAdmin.setOnMouseEntered((MouseEvent event) -> {
            animationScale(ivAdmin, txtAdmin, event);
        });
        ivAdmin.setOnMouseExited((MouseEvent event) -> {
            animationReset(ivAdmin, txtAdmin);
        });

        ivDeveloper.setOnMouseEntered((MouseEvent event) -> {
            animationScale(ivDeveloper, txtDeveloper,event);
        });
        ivDeveloper.setOnMouseExited((MouseEvent event) -> {
            animationReset(ivDeveloper, txtDeveloper);
        });
        //application.crateUserTable();
    }

    private void animationScale(ImageView iv, Text txt, MouseEvent event) {
        ((ImageView) event.getSource()).toFront();
        txt.toFront();
        timeline = new Timeline();
        KeyValue keyX = new KeyValue(iv.scaleXProperty(), 2);
        KeyValue keyY = new KeyValue(iv.scaleYProperty(), 2);
        KeyValue txtY = new KeyValue(txt.scaleYProperty(), 2);
        KeyValue txtX = new KeyValue(txt.scaleXProperty(), 2);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(800), keyX, keyY, txtX, txtY);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    private void animationReset(ImageView iv, Text txt) {
        timeline.stop();
        iv.setScaleX(1);
        iv.setScaleY(1);
        txt.setScaleX(1);
        txt.setScaleY(1);
    }

    @FXML
    private void gotoReport(MouseEvent event) {
        if (application != null) {
            audio.play();
            application.gotoReport();
        }
    }

    @FXML
    private void gotoAdmin(MouseEvent event) {
        audio.play();
        application.gotoAdmin();
    }

    @FXML
    private void gotoDeveloper(MouseEvent event) {
        audio.play();
        application.gotoDeveloper();
    }

    @FXML
    private void gotoAddRenter(MouseEvent event) {
        if (application != null) {
            audio.play();
            application.gotoAddRenter();
        }
    }

    @FXML
    private void gotoProfile(MouseEvent event) {
        if (application != null) {
            audio.play();
            application.gotoViewRenter();
        }
    }

    @FXML
    private void gotoInsert(MouseEvent event) {
        if (application != null) {
            audio.play();
            application.gotoInserBills();
        }
    }

}
