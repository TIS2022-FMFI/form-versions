import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UploadDVPController implements Initializable {

    @FXML
    private TableView<TestWrapper> tableViewDVP;

    @FXML
    private TableColumn<TestWrapper, String> docNumDVP;

    @FXML
    private TableColumn<TestWrapper, String> dateDVP;

    @FXML
    private TableColumn<TestWrapper, String> aaDVP;

    @FXML
    private TableColumn<TestWrapper, String> custNumDVP;

    @FXML
    private TableColumn<TestWrapper, String> testTypeDVP;

    @FXML
    private TableColumn<TestWrapper, String> testResDVP;

    @FXML
    private TableColumn<TestWrapper, String> sollDVP;

    @FXML
    private TableColumn<TestWrapper, String> plusDVP;

    @FXML
    private TableColumn<TestWrapper, String> minusDVP;


    ObservableList<TestWrapper> observableListItems;

    ExcelSheet excelSheet;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableViewDVP.setEditable(true);
        observableListItems = FXCollections.observableArrayList();
    }

    public void createTable() {

        docNumDVP.setCellValueFactory(new PropertyValueFactory<>("documentNr"));
        docNumDVP.setCellFactory(TextFieldTableCell.forTableColumn());
        docNumDVP.setOnEditCommit(testWrapperStringCellEditEvent -> {
            TestWrapper tw = testWrapperStringCellEditEvent.getRowValue();
            tw.setDocumentNr(testWrapperStringCellEditEvent.getNewValue());
        });

        dateDVP.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateDVP.setCellFactory(TextFieldTableCell.forTableColumn());
        dateDVP.setOnEditCommit(testWrapperStringCellEditEvent -> {
            TestWrapper tw = testWrapperStringCellEditEvent.getRowValue();
            tw.setDate(testWrapperStringCellEditEvent.getNewValue());
        });

        aaDVP.setCellValueFactory(new PropertyValueFactory<>("AA"));
        aaDVP.setCellFactory(TextFieldTableCell.forTableColumn());
        aaDVP.setOnEditCommit(testWrapperStringCellEditEvent -> {
            TestWrapper tw = testWrapperStringCellEditEvent.getRowValue();
            tw.setAA(testWrapperStringCellEditEvent.getNewValue());
        });

        custNumDVP.setCellValueFactory(new PropertyValueFactory<>("customerNr"));
        custNumDVP.setCellFactory(TextFieldTableCell.forTableColumn());
        custNumDVP.setOnEditCommit(testWrapperStringCellEditEvent -> {
            TestWrapper tw = testWrapperStringCellEditEvent.getRowValue();
            tw.setCustomerNr(testWrapperStringCellEditEvent.getNewValue());
        });

        testTypeDVP.setCellValueFactory(new PropertyValueFactory<>("testType"));
        testTypeDVP.setCellFactory(TextFieldTableCell.forTableColumn());
        testTypeDVP.setOnEditCommit(testWrapperStringCellEditEvent -> {
            TestWrapper tw = testWrapperStringCellEditEvent.getRowValue();
            tw.setTestType(testWrapperStringCellEditEvent.getNewValue());
        });

        testResDVP.setCellValueFactory(new PropertyValueFactory<>("testResult"));
        testResDVP.setCellFactory(TextFieldTableCell.forTableColumn());
        testResDVP.setOnEditCommit(testWrapperStringCellEditEvent -> {
            TestWrapper tw = testWrapperStringCellEditEvent.getRowValue();
            tw.setTestResult(testWrapperStringCellEditEvent.getNewValue());
            editTestResult(tw);
        });

        sollDVP.setCellValueFactory(new PropertyValueFactory<>("sollDVP"));
        sollDVP.setCellFactory(TextFieldTableCell.forTableColumn());
        sollDVP.setOnEditCommit(testWrapperStringCellEditEvent -> {
            TestWrapper tw = testWrapperStringCellEditEvent.getRowValue();
            tw.setSoll(testWrapperStringCellEditEvent.getNewValue());
            editTestResult(tw);
        });

        plusDVP.setCellValueFactory(new PropertyValueFactory<>("sollPlus"));
        plusDVP.setCellFactory(TextFieldTableCell.forTableColumn());
        plusDVP.setOnEditCommit(testWrapperStringCellEditEvent -> {
            TestWrapper tw = testWrapperStringCellEditEvent.getRowValue();
            tw.setSollPlus(testWrapperStringCellEditEvent.getNewValue());
            editTestResult(tw);
        });

        minusDVP.setCellValueFactory(new PropertyValueFactory<>("sollMinus"));
        minusDVP.setCellFactory(TextFieldTableCell.forTableColumn());
        minusDVP.setOnEditCommit(testWrapperStringCellEditEvent -> {
            TestWrapper tw = testWrapperStringCellEditEvent.getRowValue();
            tw.setSollMinus(testWrapperStringCellEditEvent.getNewValue());
            editTestResult(tw);
        });

        tableViewDVP.setItems(observableListItems);
    }


    @FXML
    void clearDVPPage(ActionEvent event) {
        if (observableListItems == null || !observableListItems.isEmpty()) {
            tableViewDVP.getItems().clear();
            excelSheet = new ExcelSheet();
        }
    }

    @FXML
    void insertToDB(ActionEvent event) throws SQLException {
        if (excelSheet.listOfAllTests == null || excelSheet.listOfAllTests.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Empty table to upload!");
            alert.showAndWait();
        } else{
            DatabaseTransactions dbt = new DatabaseTransactions();
            dbt.insertTestList(excelSheet.listOfAllTests);
            clearDVPPage(event);
        }
    }

    @FXML
    void loadDVP(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose the excel file");
        fc.setInitialDirectory(new File((new JFileChooser()).getFileSystemView().getDefaultDirectory().toString()));
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                excelSheet = new ExcelSheet();
                excelSheet.parseExcelFile(selectedFile.getAbsolutePath());
                observableListItems = FXCollections.observableArrayList(excelSheet.generateTestWrappersForAllTest());
                createTable();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else System.out.println("zle");
    }

    void editTestResult(TestWrapper tw) {
        excelSheet.listOfAllTests.forEach(test -> {
            test.getTest_results().forEach(testResult -> {
                if (test.getDocument_nr().equals(tw.getDocumentNr()) &&
                    test.getDate().equals(tw.getDate()) &&
                    testResult.getTest_type().replace('\n', ' ').equals(tw.getTestType().replace('\n', ' '))) {
                    System.out.println(testResult.getTest_type().replace('\n', ' ') + " " + tw.getTestType().replace('\n', ' '));
                    testResult.setTest_result(tw.getTestResult());
                        testResult.setSoll(tw.getSoll());
                        testResult.setSoll_minus(tw.getSollMinus());
                        testResult.setSoll_plus(tw.getSollPlus());
                }
            });
        });
    }
}
