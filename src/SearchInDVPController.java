import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;

import javax.xml.crypto.Data;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class SearchInDVPController implements Initializable {

    @FXML
    public ComboBox<String> dropdownTemplates;

    @FXML
    public TextField showingDVPForPartTextField;

    @FXML
    private ComboBox<String> dateDropdown;

    @FXML
    private TableView<TestWrapper> tableViewDVPSearch;

    @FXML
    private TableColumn<TestWrapper, String> docNumDVPSearch;

    @FXML
    private TableColumn<TestWrapper, String> dateDVPSearch;

    @FXML
    private TableColumn<TestWrapper, String> aaDVPSearch;

    @FXML
    private TableColumn<TestWrapper, String> custNumDVPSearch;

    @FXML
    private TableColumn<TestWrapper, String> testTypeDVPSearch;

    @FXML
    private TableColumn<TestWrapper, String> testResDVPSearch;

    @FXML
    private TableColumn<TestWrapper, String> sollDVPSearch;

    @FXML
    private TableColumn<TestWrapper, String> plusDVPSearch;

    @FXML
    private TableColumn<TestWrapper, String> minusDVPSearch;


    public ObservableList<TestWrapper> observableListItems;

    public Map<String, List<Test>> testsForCurrentSearch;

    public List<String> listTemplatov = null;


    public void createTable() {

        docNumDVPSearch.setCellValueFactory(new PropertyValueFactory<>("documentNr"));
        docNumDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());
        docNumDVPSearch.setEditable(false);



        dateDVPSearch.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());
        dateDVPSearch.setEditable(false);


        aaDVPSearch.setCellValueFactory(new PropertyValueFactory<>("AA"));
        aaDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());
        aaDVPSearch.setEditable(false);


        custNumDVPSearch.setCellValueFactory(new PropertyValueFactory<>("customerNr"));
        custNumDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());
        custNumDVPSearch.setEditable(false);


        testTypeDVPSearch.setCellValueFactory(new PropertyValueFactory<>("testType"));
        testTypeDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());
        testTypeDVPSearch.setEditable(false);


        testResDVPSearch.setCellValueFactory(new PropertyValueFactory<>("testResult"));
        testResDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());



        testResDVPSearch.setOnEditCommit(testWrapperStringCellEditEvent -> {
            int index = tableViewDVPSearch.getSelectionModel().getSelectedIndex();
            TestWrapper tw = tableViewDVPSearch.getItems().get(index);
            tw.setTestResult(testWrapperStringCellEditEvent.getNewValue());
            DatabaseTransactions dbt = new DatabaseTransactions();
            try {
                dbt.editTestWrapper(tw, testsForCurrentSearch.get(dateDropdown.getSelectionModel().getSelectedItem().split("#")[0])
                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])),
                        findTestResultInTestByName(testsForCurrentSearch.get(dateDropdown.getSelectionModel().getSelectedItem().split("#")[0])
                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])), tw.getTestType()), "dummy");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        });


        sollDVPSearch.setCellValueFactory(new PropertyValueFactory<>("sollDVP"));
        sollDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());

        sollDVPSearch.setOnEditCommit(testWrapperStringCellEditEvent -> {
            int index = tableViewDVPSearch.getSelectionModel().getSelectedIndex();
            TestWrapper tw = tableViewDVPSearch.getItems().get(index);
            tw.setSoll(testWrapperStringCellEditEvent.getNewValue());
            System.out.println(tw.getSoll());
            DatabaseTransactions dbt = new DatabaseTransactions();
            try {
                dbt.editTestWrapper(tw, testsForCurrentSearch.get(dateDropdown.getSelectionModel().getSelectedItem().split("#")[0])
                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])),
                        findTestResultInTestByName(testsForCurrentSearch.get(dateDropdown.getSelectionModel().getSelectedItem().split("#")[0])
                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])), tw.getTestType()), "dummy");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });


        plusDVPSearch.setCellValueFactory(new PropertyValueFactory<>("sollPlus"));
        plusDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());

        plusDVPSearch.setOnEditCommit(testWrapperStringCellEditEvent -> {
            int index = tableViewDVPSearch.getSelectionModel().getSelectedIndex();
            TestWrapper tw = tableViewDVPSearch.getItems().get(index);
            tw.setSollPlus(testWrapperStringCellEditEvent.getNewValue());
            DatabaseTransactions dbt = new DatabaseTransactions();
            try {
                dbt.editTestWrapper(tw, testsForCurrentSearch.get(dateDropdown.getSelectionModel().getSelectedItem().split("#")[0])
                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])),
                        findTestResultInTestByName(testsForCurrentSearch.get(dateDropdown.getSelectionModel().getSelectedItem().split("#")[0])
                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])), tw.getTestType()), "dummy");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });


        minusDVPSearch.setCellValueFactory(new PropertyValueFactory<>("sollMinus"));
        minusDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());

        minusDVPSearch.setOnEditCommit(testWrapperStringCellEditEvent -> {
            int index = tableViewDVPSearch.getSelectionModel().getSelectedIndex();
            TestWrapper tw = tableViewDVPSearch.getItems().get(index);
            tw.setSollMinus(testWrapperStringCellEditEvent.getNewValue());
            DatabaseTransactions dbt = new DatabaseTransactions();
            try {
                dbt.editTestWrapper(tw, testsForCurrentSearch.get(dateDropdown.getSelectionModel().getSelectedItem().split("#")[0])
                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])),
                        findTestResultInTestByName(testsForCurrentSearch.get(dateDropdown.getSelectionModel().getSelectedItem().split("#")[0])
                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])), tw.getTestType()), "dummy");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });


        tableViewDVPSearch.setItems(observableListItems);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableViewDVPSearch.setEditable(true);
        if (showingDVPForPartTextField != null) {
            State.setTextField(showingDVPForPartTextField);

            dateDropdown.setOnAction(event -> {
                if (!dateDropdown.getItems().isEmpty()) {
                    if (!tableViewDVPSearch.getItems().isEmpty())tableViewDVPSearch.getItems().clear();
                    observableListItems = getTestFromSelected(dateDropdown.getSelectionModel().getSelectedItem());
                    createTable();
                }


            });

            showingDVPForPartTextField.textProperty().addListener(v -> {
                try {

                    if(!dropdownTemplates.getItems().isEmpty())dropdownTemplates.getItems().clear();
                    if (!dateDropdown.getItems().isEmpty()) dateDropdown.getItems().clear();
                    else {
                        if (!Objects.equals(showingDVPForPartTextField.getText(), ""))
                        getAllTestsSorted(showingDVPForPartTextField.getText());
                        if (listTemplatov == null)
                            listTemplatov = TemplateFinder.getInstance().findAll().stream().map(it -> it.template_name).collect(Collectors.toList());
                        dropdownTemplates.getItems().addAll(listTemplatov);
                        dateDropdown.getItems().addAll(getDatesForAllTests());
                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    public List<Test> getAllTestsForPart(String partID) throws SQLException {
        return TestFinder.getInstance().findTestsForPart(partID);
    }

    public ObservableList<TestWrapper> getDVPTableFromDB(String partID) throws SQLException {
        ExcelSheet e = new ExcelSheet();
        e.setListOfAllTests(getAllTestsForPart(partID));
        return FXCollections.observableArrayList(e.generateTestWrappersForAllTest());
    }

    public void getAllTestsSorted(String partID) throws SQLException {
        testsForCurrentSearch = getAllTestsForPart(partID).stream().collect(Collectors.groupingBy(Test::getDate));
    }

    public ObservableList<String> getDatesForAllTests() {
        List<String> dates = new ArrayList<>();
        testsForCurrentSearch.forEach((date, test) -> {
            for (int i = 0; i < test.size(); i++) {
                dates.add(test.get(i).getDate() + "#" + i);
            }
        });
        return FXCollections.observableArrayList(dates);
    }

    public ObservableList<TestWrapper> getTestFromSelected(String selectedTest) {
        ExcelSheet e = new ExcelSheet();
        List<Test> tst = new ArrayList<>();
        tst.add(testsForCurrentSearch.get(selectedTest.split("#")[0]).get(Integer.parseInt(selectedTest.split("#")[1])));
        e.setListOfAllTests(tst);
        return FXCollections.observableArrayList(e.generateTestWrappersForAllTest());
    }


    public void exportDVPOfPartToTemplate(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel", ".xlsx"));

        File selectedFile = fc.showSaveDialog(null);

        if (selectedFile != null) {
            try {
                TemplateFinder.getInstance().findByName(dropdownTemplates.getValue()).
                        export(selectedFile.getAbsolutePath(), observableListItems);
                showingDVPForPartTextField.setText("");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else System.out.println("zle");
    }

    public TestResult findTestResultInTestByName(Test test, String name) {
        for (int i = 0; i < test.getTest_results().size(); i++) {
            if (Objects.equals(test.getTest_results().get(i).getTest_type(), name)) {
                return test.getTest_results().get(i);
            }
        }
        return new TestResult();
    }
}
