/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renter.management;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.html.table.Table;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import renter.db.DBConn;
import renter.model.Bill;
import renter.model.Renter;

/**
 * FXML Controller class
 *
 * @author anwar
 */
public class ViewprofileController extends AnchorPane implements Initializable {

    @FXML
    private Button btnCancel;
    private RenterManagement application;
    @FXML
    private ListView<RenterName> lvRenter;
    private Sql2o sql2o;
    private List<RenterName> myList;
    @FXML
    private GridPane gridPnaeDetails;
    @FXML
    private ImageView ivRenterPic;

    private Text[] text = new Text[10];
    private TextField[] tfFields = new TextField[10];
    
    @FXML
    private TextField tfSearch;
    
    ObservableList<RenterName> renterList;
    @FXML
    private Button btnUpdate;
    
    private boolean isUpdate = false;
    private int selectedID = 0;
    private int dataId = 0;
    
    @FXML
    private TableView<Bill> tableHistory;
    @FXML
    private TableColumn<Bill, Integer> colSL;
    @FXML
    private TableColumn<Bill, Date> colMonth;
    @FXML
    private TableColumn<Bill, Integer> colPaid;
    @FXML
    private TableColumn<Bill, Integer> colDue;
    @FXML
    private TableColumn<Bill, Date> colPaidDate;
    @FXML
    private ComboBox<String> cbxType;
    @FXML
    private Button btnExport;
    
    private List<Bill> tableList = new ArrayList<>();
    private ObservableList<Bill> obTable = null;

    //model 
    public void setApp(RenterManagement application) {
        this.application = application;
    }

    private void prepareRenters() {
        myList = getAllRenterName();
    }
    
    private List<RenterName> getAllRenterName() {
        String sql
                = "SELECT r_id, r_name from APP.RENTER";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(RenterName.class);
        }
    }
    
    private void prepareBills(int r_id, boolean notAll){
        Date date = Date.valueOf(LocalDate.now().withDayOfMonth(1));
       if(notAll){
           tableList = getMonthBill(date, r_id);
       } else {
           tableList = getAllBill(r_id);
       }
        obTable = FXCollections.observableArrayList(tableList);
        tableHistory.setItems(obTable);
    }

    private void getAllRenterDetails(int id, boolean isEdit) throws IOException {
        String sql
                = "SELECT r_id,r_name,r_father,r_address,r_phoneno,r_email,"
                + "r_houseno,r_houserent,r_nid,r_passport,r_ref,r_photo from APP.RENTER where r_id = :renterId";

        List<Renter> renter;

        try (Connection con = sql2o.open()) {
            renter = con.createQuery(sql)
                    .addParameter("renterId", id)
                    .executeAndFetch(Renter.class);
        }

        for (Renter r : renter) {

            String[] str = {
                r.getR_name(),
                r.getR_father(),
                r.getR_address(),
                r.getR_phoneno(),
                r.getR_email(),
                r.getR_nid(),
                r.getR_passport(),
                r.getR_ref(),
                r.getR_houseno(),
                r.getR_houserent() + ""
            };
            
            if(r.getR_photo() != null){
                // convert byte array back to BufferedImage
                InputStream in = new ByteArrayInputStream(r.getR_photo());
                BufferedImage buffer = ImageIO.read(in);
                Image image = SwingFXUtils.toFXImage(buffer, null);
                ivRenterPic.setImage(image);
            } else {
                Image im = new Image(RenterManagement.class.getResourceAsStream("usericon.png"));
                ivRenterPic.setImage(im);
            }

            if(isEdit){
                //edit request
                int index = 0;
                for (Text te : text) {
                    gridPnaeDetails.getChildren().remove(te);
                }
                
                index = 0;
                for (TextField te : tfFields) {
                    gridPnaeDetails.getChildren().remove(te);
                    gridPnaeDetails.add(te, 1, index);
                    te.setText(str[index++]);
                }
                btnUpdate.setVisible(true);
                btnExport.setVisible(false);
                cbxType.setVisible(false);
                tableHistory.setVisible(false);
                
            } else {
                //view request
                int index = 0;
                for (TextField te : tfFields) {
                    gridPnaeDetails.getChildren().remove(te);
                }
                
                index = 0;
                for (Text te : text) {
                    gridPnaeDetails.getChildren().remove(te);
                    gridPnaeDetails.add(te, 1, index);
                    te.setText(str[index++]);
                }
                btnUpdate.setVisible(false);
                selectedID = 0;
                isUpdate = false;
                btnExport.setVisible(true);
                cbxType.setVisible(true);
                tableHistory.setVisible(true);
            }
            
            //set image if not null
            if(r.getR_photo() != null){
//                BufferedImage img = ImageIO.
//                ivRenterPic.setImage(value);
            }
        }

    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //vboxContain.setSpacing(5);
        //conn = DBConn.getInstance();
        
        if(!isUpdate){
            cbxType.setVisible(false);
            tableHistory.setVisible(false);
        }

        sql2o = new Sql2o(DBConn.DBURL, DBConn.USERNAME, DBConn.PASSWORD);
        prepareRenters();
        renterList = FXCollections.observableList(myList);
        lvRenter.setItems(renterList);
        btnUpdate.setVisible(false);
        
        ObservableList<String> lst = FXCollections.observableArrayList();
        lst.add("This Month");
        lst.add("All");
        cbxType.setItems(lst);
        cbxType.getSelectionModel().selectFirst();
        
        cbxType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue != null && dataId != 0){
                    if(newValue.equalsIgnoreCase("All")){
                        prepareBills(dataId, false);
                    } else if(newValue.equalsIgnoreCase("This Month")){
                        prepareBills(dataId, true);
                    }
                }
            }
        });
        
        colMonth.setCellValueFactory(new PropertyValueFactory<>("b_month"));
        colPaid.setCellValueFactory(new PropertyValueFactory<>("b_paid"));
        colDue.setCellValueFactory(new PropertyValueFactory<>("b_due"));
        colPaidDate.setCellValueFactory(new PropertyValueFactory<>("b_update"));
        
        //sl
        colSL.setCellValueFactory((CellDataFeatures<Bill, Integer> column)
                -> {
            return new ReadOnlyObjectWrapper<>(tableHistory.getItems().indexOf(column.getValue()) + 1);
        });

        lvRenter.setCellFactory((ListView<RenterName> param) -> new CustomCell());

        lvRenter.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends RenterName> observable, RenterName oldValue, RenterName newValue) -> {
            if (newValue != null) {
                try {
                    getAllRenterDetails(newValue.getR_Id(), false);
                    dataId = newValue.getR_Id();
                    switch(cbxType.getSelectionModel().getSelectedIndex()){
                        case 0:
                            prepareBills(newValue.getR_Id(), true);
                            break;
                        case 1:
                            prepareBills(newValue.getR_Id(), false);
                            break;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ViewprofileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        tfSearch.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            handleSearchByKey2(oldValue, newValue);
        });
        
        int index = 0;
        for (Text te : text) {
            te = new Text();
            te.setFont(new Font(20));
            //gridPnaeDetails.add(te, 1, index);
            text[index++] = te;
        }
        
        index = 0;
        for (TextField te : tfFields) {
            te = new TextField();
            //gridPnaeDetails.add(te, 1, index);
            tfFields[index++] = te;
        }
        
    }

    @FXML
    private void renterUpdate(ActionEvent event) {
        String name= tfFields[0].getText();
        String fatherName= tfFields[1].getText();
        String address= tfFields[2].getText();
        String phone= tfFields[3].getText();
        String email= tfFields[4].getText();
        String nid= tfFields[5].getText();
        String passport= tfFields[6].getText();
        String ref= tfFields[7].getText();
        String houseNo= tfFields[8].getText();
        String houseRent= tfFields[9].getText();
        
        if(name.length() != 0 && houseRent.length() != 0 && selectedID != 0){
            
            Renter renter = new Renter(
                    selectedID,
                    name,
                    fatherName,
                    address,
                    phone,
                    email,
                    nid,
                    passport,
                    ref,
                    houseNo,
                    Integer.parseInt(houseRent),
                    null,
                    true
            );
            
            String sql = "update app.renter set "
                    + "r_name = :r_name,"
                    + "r_father = :r_father,"
                    + "r_address = :r_address,"
                    + "r_phoneno = :r_phoneno,"
                    + "r_email = :r_email,"
                    + "r_houseno = :r_houseno,"
                    + "r_houserent = :r_houserent,"
                    + "r_nid = :r_nid,"
                    + "r_passport = :r_passport,"
                    + "r_ref = :r_ref where r_id = :r_id";
            try(Connection conn = sql2o.open()){
                conn.createQuery(sql).bind(renter).executeUpdate();
                Alert alert = new Alert(AlertType.INFORMATION, "Successfully Updated.", ButtonType.OK);
                alert.showAndWait();
//                isUpdate = false;
//                selectedID = 0;
            }
        }
    }

    @FXML
    private void exportPdf(ActionEvent event) throws IOException, DocumentException {
        String DEST = "exports/exports.pdf";
        FileChooser fileChooser = new FileChooser();
            configureFileChooser(fileChooser, LocalDate.now().toString() + ".pdf");
            File file = fileChooser.showSaveDialog(application.stage);
            if (file != null) {
                DEST = file.getAbsolutePath();
                //call to make pdf
                createPdf(DEST, tableList);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "PDF Exported.", ButtonType.OK);
                alert.showAndWait();
            }
    }

    class CustomCell extends ListCell<RenterName> {

        @Override
        protected void updateItem(RenterName item, boolean empty) {
            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
            if (item != null) {
                setText(item.getR_name());
                ContextMenu contextMenu = new ContextMenu();
                MenuItem item1 = new MenuItem("Edit");
                item1.setOnAction((ActionEvent e) -> {
                    try {
                        //edit renter info
                        getAllRenterDetails(item.getR_Id(), true);
                        //prepareBills(item.getR_Id());
                    } catch (IOException ex) {
                        Logger.getLogger(ViewprofileController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    isUpdate = true;
                    selectedID = item.getR_Id();
                    btnUpdate.setVisible(true);
                });
                MenuItem item2 = new MenuItem("Disable");
                boolean isActive = isActive(item.getR_Id());
                if(isActive){
                    item2.setText("Disable");
                } else {
                    item2.setText("Enable");
                    setStyle("-fx-background-color: orange;");
                }
                item2.setOnAction((ActionEvent e) -> {
                    //disable renter / enable renter
                    
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Renter Name: " + item.getR_name());
                    alert.setContentText("Are you sure to Disable/Enable  ?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        // ... user chose OK
                         disableRente(item.getR_Id(), isActive);
                         refreshList();
                    } else {
                        // ... user chose CANCEL or closed the dialog
                    }
                });
                
                MenuItem item3 = new MenuItem("Delete");
                item3.setOnAction((ActionEvent e) -> {
                    //delete entry
                    TextInputDialog dialog = new TextInputDialog("BlackHole Tech");
                    dialog.setTitle("Renter Delete Confirmation");
                    dialog.setHeaderText("This is only for developers. You cannot delete Renter becasue error will be occured.");
                    dialog.setContentText("Enter Developer Key:");

// Traditional way to get the response value.
                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        if(result.get().equals("34722645")){
                            deleteRenter(item.getR_Id());
                            refreshList();
                        }
                    }
                });
                
                //menu item for view total accounts
                contextMenu.getItems().addAll(item1, item2, item3);
                contextMenu.setStyle("-fx-background-color: #03a9f4; -fx-text-fill: black");
                setContextMenu(contextMenu);
            }
        }

    }
    
    private void refreshList(){
        prepareRenters();
        renterList = FXCollections.observableList(myList);
        lvRenter.setItems(renterList);
        lvRenter.refresh();
    }
    
    private void disableRente(int r_id, boolean isDisable){
        String sql = ""; 
        if(isDisable){
            sql = "update app.renter set is_active = false where r_id=:r_id";
        } else {
            sql = "update app.renter set is_active = true where r_id=:r_id";
        }
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                    .addParameter("r_id", r_id)
                    .executeUpdate();
            System.out.println("Successfully updated.");
        }
    }
    
    private void deleteRenter(int r_id){
        String sql = "delete from app.renter where r_id=:r_id"; 
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                    .addParameter("r_id", r_id)
                    .executeUpdate();
            System.out.println("Successfully Deleted");
        }
    }

    private Boolean isActive(int r_id){
        String sql = "select is_active from app.renter where r_id=:r_id";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("r_id", r_id)
                    .executeScalar(Boolean.class);
        }
    }
    
    private class RenterName {

        private int r_id;
        private String r_name;

        public RenterName(int r_id, String r_name) {
            this.r_id = r_id;
            this.r_name = r_name;
        }

        public int getR_Id() {
            return r_id;
        }

        public String getR_name() {
            return r_name;
        }
    }

    @FXML
    private void gotoHome(ActionEvent event) {
        application.gotoHome();
    }
    
    public void handleSearchByKey2(String oldVal, String newVal) {
        // If the number of characters in the text box is less than last time
        // it must be because the user pressed delete
        if ( oldVal != null && (newVal.length() < oldVal.length()) ) {
            // Restore the lists original set of entries 
            // and start from the beginning
            lvRenter.setItems(renterList);
        }
         
        // Break out all of the parts of the search text 
        // by splitting on white space
        String[] parts = newVal.toUpperCase().split(" ");
 
        // Filter out the entries that don't contain the entered text
        ObservableList<RenterName> subentries = FXCollections.observableArrayList();
        for (RenterName entry: lvRenter.getItems() ) {
            boolean match = true;
            String entryText = entry.getR_name();
            for ( String part: parts ) {
                // The entry needs to contain all portions of the
                // search string *but* in any order
                if ( ! entryText.toUpperCase().contains(part) ) {
                    match = false;
                    break;
                }
            }
 
            if ( match ) {
                subentries.add(entry);
            }
        }
        lvRenter.setItems(subentries);
    }
    
    
    private List<Bill> getMonthBill(Date date, int r_id) {
        String sql
                = "SELECT * from APP.BILL where b_month=:b_month and r_id=:r_id";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("b_month", date)
                    .addParameter("r_id", r_id)
                    .executeAndFetch(Bill.class);
        }
    }
    private List<Bill> getAllBill(int r_id) {
        String sql
                = "SELECT * from APP.BILL where r_id=:r_id";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("r_id", r_id)
                    .executeAndFetch(Bill.class);
        }
    }
    
    
    //file choser configration
    private void configureFileChooser(final FileChooser fileChooser, String name){
        fileChooser.setTitle("Export Report.");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        
        fileChooser.setInitialFileName(name);
        
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Pdf", "*.pdf")
                //new FileChooser.ExtensionFilter("All Files", "*.*")
        );
    }

    public void createPdf(String dest, List<Bill> bills) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        
        //add user info table 
        PdfPTable table1 = new PdfPTable(3);
        table1.setWidthPercentage(100);
        table1.addCell(getCell("Name\nHouse No\nHouse Rent\n\n", PdfPCell.ALIGN_LEFT));
        table1.addCell(getCell(text[0].getText() 
                +"\n#" + text[8].getText()
                +"\n" + text[9].getText()+"/=\n\n", PdfPCell.ALIGN_LEFT));
        table1.addCell(getCell("", PdfPCell.ALIGN_LEFT));
        
        float[] columnWidths = {1, 5, 4, 4, 4};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        
        com.itextpdf.text.Font f1 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD, GrayColor.GRAYWHITE);
        PdfPCell cell1 = new PdfPCell(new Phrase("Somshir Ali Monjil", f1));
        cell1.setBackgroundColor(GrayColor.GRAYBLACK);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setColspan(5);
        table.addCell(cell1);
        
        com.itextpdf.text.Font f = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 13, com.itextpdf.text.Font.NORMAL, GrayColor.GRAYWHITE);
        PdfPCell cell = new PdfPCell(new Phrase("Report of "
                +LocalDate.now().getMonth().name()
                + ", " + LocalDate.now().getYear(), f));
        cell.setBackgroundColor(GrayColor.GRAYBLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(5);
        table.addCell(cell);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        //column name
        table.addCell("#");
        table.addCell("Month");
        table.addCell("Paid");
        table.addCell("Due");
        table.addCell("Paid Date");

        table.setHeaderRows(1);

        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        int index = 1;
        int paid = 0;
        int due = 0;
        
        for (Bill b: bills) {
            table.addCell(String.valueOf(index++));
            table.addCell(String.valueOf(b.getB_month().toLocalDate().toString()));
            table.addCell(String.valueOf(b.getB_paid()));
            table.addCell(String.valueOf(b.getB_due()));
            table.addCell(String.valueOf(b.getB_update().toLocalDate().toString()));
            
            paid+= b.getB_paid();
            due+= b.getB_due();
        }
        table.addCell("");
        table.addCell("");
        table.addCell("Total = " + paid + " /=");
        table.addCell("Total = " + due + " /=");
        table.addCell("");
        
        document.add(table1);
        
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        
        document.add(table);
        //
        //add another table
        
        document.close();
    }

    public PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
    

}
