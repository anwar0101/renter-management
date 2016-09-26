/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renter.management;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
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
public class InsertbillController extends AnchorPane implements Initializable {

    @FXML
    private ListView<Bill> lvInsertBill;
    @FXML
    private Button btnSaveBill;
    
    private boolean isClear = false;

    List<Bill> myList;
    private RenterManagement application;
    private Sql2o sql2o;

    //Create dummy list of MyObject
    private void prepareMyList() {
        myList = new ArrayList<>();
        //foreach all renters
        getAllRenterName().stream().forEach((rn) -> {
            Date date = Date.valueOf(LocalDate.now().withDayOfMonth(1));
            if (isHave(rn.r_id, date) == 1) {
                int paidAmount = paidAmount(rn.r_id, date);
                int dueAmount = dueAmount(rn.r_id, date);

                myList.add(new Bill(date,
                        rn.getR_id(), rn.getR_name(), rn.getR_houserent(), paidAmount, dueAmount, new Date(2016, 04, 05)));
            } else {
                Bill bill = new Bill(Date.valueOf(LocalDate.now().withDayOfMonth(1)),
                        rn.getR_id(), rn.getR_name(), rn.getR_houserent(), 0, rn.getR_houserent(), Date.valueOf(LocalDate.now()));
                myList.add(bill);
                //create a new one
                //insert into database
                String sql = "insert into app.bill(b_month,"
                        + "r_id,"
                        + "r_name,"
                        + "r_houserent,"
                        + "b_paid,"
                        + "b_due,"
                        + "b_update ) values (:b_month, :r_id, :r_name, :r_houserent,"
                        + ":b_paid, :b_due, :b_update )";
                try (Connection conn = sql2o.open()) {
                    conn.createQuery(sql).bind(bill).executeUpdate();
                    System.out.println("Successfully inserted.");
                }
            }
        });

    }
    
    

    private List<RenterName> getAllRenterName() {
        String sql
                = "SELECT r_id, r_name, r_houserent from APP.RENTER";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(RenterName.class);
        }
    }

    private Integer isHave(int r_id, Date date) {
        String sql = "select count(b_month) from app.bill where b_month=:b_month and r_id=:r_id";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("b_month", date)
                    .addParameter("r_id", r_id)
                    .executeScalar(Integer.class);
        }
    }

    private Integer paidAmount(int r_id, Date date) {
        String sql = "select b_paid from app.bill where b_month=:b_month and r_id=:r_id";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("b_month", date)
                    .addParameter("r_id", r_id)
                    .executeScalar(Integer.class);
        }
    }

    private Integer dueAmount(int r_id, Date date) {
        String sql = "select b_due from app.bill where b_month=:b_month and r_id=:r_id";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("b_month", date)
                    .addParameter("r_id", r_id)
                    .executeScalar(Integer.class);
        }
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

        sql2o = new Sql2o(DBConn.DBURL, DBConn.USERNAME, DBConn.PASSWORD);
        prepareMyList();

        ObservableList<Bill> renterList = FXCollections.observableList(myList);
        lvInsertBill.setItems(renterList);
        lvInsertBill.setEditable(true);

        
        
        lvInsertBill.setCellFactory((ListView<Bill> param) -> {
            
            ListCell<Bill> cell = null;
            
            
            cell = new ListCell<Bill>() {

                private TextField[] textFields = new TextField[2];
                private Text[] texts = new Text[3];
                private DatePicker datePicker = new DatePicker();
                private DatePicker datePicker2 = new DatePicker();
                private boolean isChanged = false;
                private int txIndex = 0;
                private String temp = "";
                private boolean isHave = false;

                {

                    for (Text txt : texts) {
                        txt = new Text();
                        texts[txIndex++] = txt;
                    }

                    txIndex = 0;

                    for (TextField tf : textFields) {

                        tf = new TextField();
                        textFields[txIndex] = tf;

                        textFields[txIndex].setOnAction((ActionEvent event) -> {
                            //enter action

                        });

                        textFields[txIndex].focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                            //focuse enter
                            if (oldValue && !newValue) {
                                //focuse lost
                                System.out.println("Lost");
                                if (isChanged) {
                                    commitEdit(getItem());
                                }
                            } else {
                                //focuse enter
                                System.out.println("Enter:");
                            }
                        });

                        textFields[txIndex].textProperty().addListener((observable, oldValue, newValue) -> {
                            if (oldValue == null) {
                                //value not change 
                                System.out.println("not change.");
                                isChanged = false;
                            } else {
                                //value changed
                                System.out.println("changed.");
                                isChanged = true;
                            }
                        });
                        txIndex++;
                    }

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

                    StringConverter converter2 = new StringConverter<LocalDate>() {
                        DateTimeFormatter dateFormatter
                                = DateTimeFormatter.ofPattern("dd-MM");

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
                    datePicker.setValue(LocalDate.now().withDayOfMonth(1));
                    datePicker.setPromptText("MM-yyyy".toLowerCase());
                    datePicker.valueProperty().addListener((ObservableValue<? extends LocalDate> observable,
                            LocalDate oldValue, LocalDate newValue) -> {
                        if (!oldValue.withDayOfMonth(1).equals(newValue.withDayOfMonth(1))) {
                            System.out.println(oldValue + "->" + newValue);
                            
                            Date date = Date.valueOf(newValue.withDayOfMonth(1));
                            int r_id = getItem().getR_id();
                            
                            if(isHave(r_id, date) == 1){
                                //throw new value for commit
                                for(Bill b: getMonthBill(date, r_id)){
                                    isHave = true;
                                    commitEdit(b);
                                }
                            }
                        } else {
                            System.out.println("Same month.");
                        }
                    });

                    datePicker2.setConverter(converter2);
                    datePicker2.setValue(LocalDate.now());
                    datePicker2.setPromptText("dd-MM".toLowerCase());
                    
                    datePicker2.valueProperty().addListener((ObservableValue<? extends LocalDate> observable,
                            LocalDate oldValue, LocalDate newValue) -> {
                        if (!oldValue.equals(newValue)) {
                            System.out.println(oldValue + "->" + newValue);
                            
                            //commit edit
                            commitEdit(getItem());
                        } else {
                            System.out.println("Same date.");
                        }
                    });

                }

                //Override method
                @Override
                public void cancelEdit() {
                    super.cancelEdit(); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void commitEdit(Bill newValue) {
                    super.commitEdit(newValue); //To change body of generated methods, choose Tools | Templates.

                    if(isHave){
                        datePicker.setValue(newValue.getB_month().toLocalDate());
                        datePicker2.setValue(newValue.getB_update().toLocalDate());
                        
                        //txtfiled
                        textFields[0].setText(""+ newValue.getB_paid());
                        textFields[1].setText(""+ newValue.getB_due());
                        isHave = false;
                    } else {
                        
                        newValue.setB_due(Integer.parseInt(textFields[0].getText()));
                        
                        if (!textFields[0].getText().equals("")
                                && datePicker.getValue() != null && datePicker2.getValue() != null) {

                            newValue.setB_paid(Integer.parseInt(textFields[0].getText()));
                            int due = Integer.parseInt(texts[2].getText())
                                    - newValue.getB_paid();
                            newValue.setB_due(due);
                            textFields[1].setText("" + due);
                            LocalDate d = datePicker.getValue();
                            d = d.withDayOfMonth(1);
                            newValue.setB_month(Date.valueOf(d));
                            LocalDate date = datePicker2.getValue();
                            newValue.setB_update(Date.valueOf(date));

                            System.out.println("Date :" + date.getYear() + ":" + date.getMonthValue());

                            //insert into database
                            String sql = "insert into app.bill(b_month,"
                                    + "r_id,"
                                    + "r_name,"
                                    + "r_houserent,"
                                    + "b_paid,"
                                    + "b_due,"
                                    + "b_update ) values (:b_month, :r_id, :r_name, :r_houserent,"
                                    + ":b_paid, :b_due, :b_update )";

                            if (isHave(newValue.getR_id(), Date.valueOf(d)) == 1) {
                                sql = "update app.bill set "
                                        + "b_paid = :b_paid,"
                                        + "b_due = :b_due,"
                                        + "b_update = :b_update where b_month=:b_month and r_id=:r_id";

                                try (Connection conn = sql2o.open()) {
                                    conn.createQuery(sql).bind(newValue).executeUpdate();
                                    System.out.println("Successfully updated.");
                                    //set color
                                    textFields[0].setStyle("-fx-background-color: green;-fx-text-fill: white;");
                                    textFields[1].setStyle("-fx-background-color: green;-fx-text-fill: white;");
                                }

                            } else {
                                try (Connection conn = sql2o.open()) {
                                    conn.createQuery(sql).bind(newValue).executeUpdate();
                                    System.out.println("Successfully inserted.");
                                    //set color
                                    textFields[0].setStyle("-fx-background-color: green;-fx-text-fill: white;");
                                    textFields[1].setStyle("-fx-background-color: green;-fx-text-fill: white;");
                                }
                            }

//                            //set color
//                            textFields[0].setStyle("-fx-background-color: orange;-fx-text-fill: white;");
//                            textFields[1].setStyle("-fx-background-color: orange;-fx-text-fill: white;");
                        }
                    }
                }

                @Override
                public void startEdit() {
                    super.startEdit(); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                protected void updateItem(Bill t, boolean bln) {
                    super.updateItem(t, bln);
                    if (t != null) {

                        GridPane grid = new GridPane();
                        grid.setHgap(2);

                        ColumnConstraints[] columns = new ColumnConstraints[8];

                        int index = 0;

                        for (ColumnConstraints cc : columns) {

                            if (index == 0) {
                                cc = new ColumnConstraints(50);
                            } else {
                                cc = new ColumnConstraints(120);
                            }

                            columns[index] = cc;
                            grid.getColumnConstraints().add(columns[index]);

                            switch (index) {
                                case 0:
                                    texts[0].setText("" + (getIndex() + 1));
                                    grid.add(texts[0], index, 0);
                                    break;
                                case 1:
                                    texts[1].setText("" + t.getR_name());
                                    grid.add(texts[1], index, 0);
                                    break;
                                case 2:
                                    texts[2].setText("" + t.getR_houserent());
                                    grid.add(texts[2], index, 0);
                                    break;
                                case 3:

                                    grid.add(datePicker, index, 0);
                                    break;
                                case 4:
                                    textFields[0].setText("" + t.getB_paid());
                                    grid.add(textFields[0], index, 0);
                                    break;
                                case 5:
                                    isClear = t.getB_due()== 0;
                                    textFields[1].setText("" + t.getB_due());
                                    grid.add(textFields[1], index, 0);
                                    break;
                                case 6:
                                    grid.add(datePicker2, index, 0);
                                    break;
                            }
                            index++;
                        }
//                        if (isClear) {
//                            grid.setStyle("-fx-background-color: #18823D;-fx-text-fill: white;");
//                        }
                        setGraphic(grid);
                    }
                }

            };
//            if (isClear) {
//                cell.setStyle("-fx-background-color: green;-fx-text-fill: white;");
//            } else {
//                cell.setStyle("-fx-background-color: red;-fx-text-fill: white;");
//            }
            return cell;
        });

        lvInsertBill.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Bill>() {

            @Override
            public void changed(ObservableValue<? extends Bill> observable, Bill oldValue, Bill newValue) {
                System.out.println("onChange");
            }

        });

    }

    @FXML
    private void SaveBill(ActionEvent event) {

    }

    @FXML
    private void gotoHome(ActionEvent event) {
        application.gotoHome();
    }

    private class RenterName {

        private int r_id;
        private String r_name;
        private int r_houserent;

        public RenterName(int r_id, String r_name, int r_houserent) {
            this.r_id = r_id;
            this.r_name = r_name;
            this.r_houserent = r_houserent;
        }

        public int getR_id() {
            return r_id;
        }

        public void setR_id(int r_id) {
            this.r_id = r_id;
        }

        public String getR_name() {
            return r_name;
        }

        public void setR_name(String r_name) {
            this.r_name = r_name;
        }

        public int getR_houserent() {
            return r_houserent;
        }

        public void setR_houserent(int r_houserent) {
            this.r_houserent = r_houserent;
        }

    }

}
