/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renter.management;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import renter.db.DBConn;
import renter.model.Bill;

/**
 * FXML Controller class
 *
 * @author anwar
 */
public class ReportController extends AnchorPane implements Initializable {

    private RenterManagement application;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button btnSaveBill;
    @FXML
    private TableView<Bill> tableReport;

    @FXML
    private TableColumn<Bill, Number> colSl;
    @FXML
    private TableColumn<Bill, String> colName;
    @FXML
    private TableColumn<Bill, Integer> colHouseRent;
    @FXML
    private TableColumn<Bill, Date> colBillMonth;
    @FXML
    private TableColumn<Bill, Integer> colBillPaid;
    @FXML
    private TableColumn<Bill, Integer> colDueBill;
    @FXML
    private TableColumn<Bill, Date> colPaidDate;

    private Sql2o sql2o;
    private List<Bill> myList;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblTPaid;
    @FXML
    private Label lblTDue;

    public void setApp(RenterManagement application) {
        this.application = application;
    }

    private void prepareList() {
        Date date = Date.valueOf(LocalDate.now().withDayOfMonth(1));
        datePicker.setValue(LocalDate.now().withDayOfMonth(1));
        myList = getMonthBill(date);
        lblTPaid.setText("Paid = " + getTotalPaid(date) + " /=");
        lblTDue.setText("Due = " + getTotalDue(date) + " /=");
        lblTotal.setText("Amount = " + getTobePaid() + " /=");
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

        sql2o = new Sql2o(DBConn.DBURL, DBConn.USERNAME, DBConn.PASSWORD);

        //bind cell value
        colName.setCellValueFactory(new PropertyValueFactory<>("r_name"));
        colHouseRent.setCellValueFactory(new PropertyValueFactory<>("r_houserent"));
        colBillMonth.setCellValueFactory(new PropertyValueFactory<>("b_month"));
        colBillPaid.setCellValueFactory(new PropertyValueFactory<>("b_paid"));
        colDueBill.setCellValueFactory(new PropertyValueFactory<>("b_due"));
        colPaidDate.setCellValueFactory(new PropertyValueFactory<>("b_update"));

        //sl
        colSl.setCellValueFactory((CellDataFeatures<Bill, Number> column)
                -> {
            return new ReadOnlyObjectWrapper<>(tableReport.getItems().indexOf(column.getValue()) + 1);
        });

        //
        prepareList();
        ObservableList<Bill> bills = FXCollections.observableList(myList);

        //set to table view
        tableReport.setItems(bills);

        StringConverter converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter
                    = DateTimeFormatter.ofPattern("MM-yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };

        datePicker.setConverter(converter);
        datePicker.setPromptText("mm-yy");

    }

    private List<Bill> getMonthBill() {
        String sql
                = "SELECT * from APP.BILL";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Bill.class);
        }
    }

    private List<Bill> getMonthBill(Date date) {
        String sql
                = "SELECT * from APP.BILL where b_month=:b_month";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("b_month", date)
                    .executeAndFetch(Bill.class);
        }
    }

    @FXML
    private void printTable(ActionEvent event) throws DocumentException, FileNotFoundException, IOException {
        //pdf writer code 

        if (datePicker.getValue() != null) {
            Date date = Date.valueOf(datePicker.getValue().withDayOfMonth(1));
            String DEST = "exports/simple_table.pdf";
            
            FileChooser fileChooser = new FileChooser();
            configureFileChooser(fileChooser, date.toLocalDate().toString() + ".pdf");
            File file = fileChooser.showSaveDialog(application.stage);
            if (file != null) {
                DEST = file.getAbsolutePath();
                //call to make pdf
                createPdf(DEST, getMonthBill(date), getTotalDue(date), getTotalPaid(date));
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "PDF Saved.", ButtonType.OK);
                alert.showAndWait();
            }
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

    public void createPdf(String dest, List<Bill> bills, int due, int paid) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        
        float[] columnWidths = {1, 5, 4, 4, 4, 4, 4};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        
        Font f1 = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, GrayColor.GRAYWHITE);
        PdfPCell cell1 = new PdfPCell(new Phrase("Somshir Ali Monjil", f1));
        cell1.setBackgroundColor(GrayColor.GRAYBLACK);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setColspan(7);
        table.addCell(cell1);
        
        Font f = new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, GrayColor.GRAYWHITE);
        PdfPCell cell = new PdfPCell(new Phrase("Report of "
                +datePicker.getValue().getMonth().name() 
                + ", " + datePicker.getValue().getYear(), f));
        cell.setBackgroundColor(GrayColor.GRAYBLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(7);
        table.addCell(cell);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        //column name
        table.addCell("#");
        table.addCell("Renter Name");
        table.addCell("House Rent");
        table.addCell("Month");
        table.addCell("Paid");
        table.addCell("Due");
        table.addCell("Paid Date");

        table.setHeaderRows(1);

        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        int index = 1;
        for (Bill b: bills) {
            table.addCell(String.valueOf(index++));
            table.addCell(b.getR_name());
            table.addCell(String.valueOf(b.getR_houserent()));
            table.addCell(String.valueOf(b.getB_month().toLocalDate().toString()));
            table.addCell(String.valueOf(b.getB_paid()));
            table.addCell(String.valueOf(b.getB_due()));
            table.addCell(String.valueOf(b.getB_update().toLocalDate().toString()));
        }
        
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("Total = " + paid + " /=");
        table.addCell("Total = " + due + " /=");
        table.addCell("");
        
        document.add(table);
        document.close();
    }

    @FXML
    private void gotoHome(ActionEvent event) {
        application.gotoHome();
    }

    @FXML
    private void viewReport(ActionEvent event) {

        if (datePicker.getValue() != null) {
            Date date = Date.valueOf(datePicker.getValue().withDayOfMonth(1));

            myList = getMonthBill(date);
            ObservableList<Bill> bills = FXCollections.observableList(myList);
            tableReport.setItems(bills);
            lblTPaid.setText("Paid = " + getTotalPaid(date) + " /=");
            lblTDue.setText("Due = " + getTotalDue(date) + " /=");
            lblTotal.setText("Amount = " + getTobePaid() + " /=");
        }
    }

    private Integer getTotalPaid(Date date) {
        String sql = "SELECT sum(b_paid) FROM app.bill where b_month=:b_month";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("b_month", date)
                    .executeScalar(Integer.class);
        }
    }

    private Integer getTotalDue(Date date) {
        String sql = "SELECT sum(b_due) FROM app.bill where b_month=:b_month";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("b_month", date)
                    .executeScalar(Integer.class);
        }
    }

    private Integer getTobePaid() {
        String sql = "SELECT sum(r_houserent) FROM app.renter where is_active = true";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeScalar(Integer.class);
        }
    }

}
