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
    public ComboBox<String> dropdownTemplates = new ComboBox<>();

    @FXML
    public TextField showingDVPForPartTextField;

    @FXML
    public Button exportToTemplateButton;

    @FXML
    private ComboBox<String> dateDropdown = new ComboBox<>();

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


    public ObservableList<TestWrapper> observableListItems = FXCollections.observableArrayList(new ArrayList<>());

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
                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])), tw.getTestType()));
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
                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])), tw.getTestType()));
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
                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])), tw.getTestType()));
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
                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])), tw.getTestType()));
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
                        dropdownTemplates.getItems().clear();
                        dateDropdown.getItems().clear();
                        observableListItems.clear();
                        if (!Objects.equals(showingDVPForPartTextField.getText(), ""))
                            getAllTestsSorted(showingDVPForPartTextField.getText());
                        if (listTemplatov == null)
                            listTemplatov = TemplateFinder.getInstance().findAll().stream().map(it -> it.template_name).collect(Collectors.toList());
                        dropdownTemplates.getItems().addAll(listTemplatov);
                        dateDropdown.getItems().addAll(getDatesForAllTests());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    public List<Test> getAllTestsForPart(String partID) throws SQLException {
//        return TestFinder.getInstance().findTestsForPart(partID);
        return TestFinder.getInstance().findTestsForZostava(partID);

    }

    public ObservableList<TestWrapper> getDVPTableFromDB(String partID) throws SQLException {
        ExcelSheet e = new ExcelSheet();
        e.setListOfAllTests(getAllTestsForPart(partID));
        return FXCollections.observableArrayList(e.generateTestWrappersForAllTest());
    }

    // to be called when search textbox is updated
    public void getAllTestsSorted(String partID) throws SQLException {
//        testsForCurrentSearch = getAllTestsForPart(partID).stream().collect(Collectors.groupingBy(Test::getDate));
        testsForCurrentSearch = getAllTestsForPart(partID).stream().collect(Collectors.groupingBy(Test::getDocument_nr)); // nastavi zoznam testov
    }

    // to be called when wanting to update the part with pismenko listview, asi hnedp otom jak zavolas getAllTestsSorted
    public ObservableList<String> getAllAvailablePartsInSearch() {
        return FXCollections.observableArrayList(testsForCurrentSearch.keySet()); // zoznam stringov verzii suciastok
    }

    // to be called when pressing button to update the dates listview
    public List<String> getDatesForSelectedParts(List<String> selection) {
        List<String> dates = new ArrayList<>();
        for (String part : selection) {
            dates.addAll(getDatesForPart(testsForCurrentSearch.get(part).stream().collect(Collectors.groupingBy(Test::getDate))));
        }
        return dates;
    }

    // to be called when pressing search a chces dostat testwrappery pre zobrazenie testov
    public ObservableList<TestWrapper> getTestWrappersForCurrentSelection(List<String> parts, List<String> dates, List<String> tests) {

        List<Test> wantedTests = new ArrayList<>();

        if (dates.isEmpty() || parts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No part/No date selected!");
            alert.showAndWait();
            return FXCollections.observableArrayList(new ArrayList<>());
        }

        for (Test test : new ArrayList<>(
                testsForCurrentSearch.entrySet().stream()
                    .filter(a -> parts.contains(a.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                    .values()).stream()
                        .flatMap(Collection::stream)
                .collect(Collectors.toList())) {

            if (parts.contains(test.getDocument_nr()) &&
                dates.stream()
                        .map(it -> it.split(" ")[1]
                                .split("#")[0])
                        .collect(Collectors.toList())
                        .contains(test.getDate())) {

                if (tests.isEmpty()) {
                    wantedTests.add(test);
                } else {
                    Test t = test;
                    t.setTest_results(test.getTest_results().stream()
                            .filter(it -> tests
                                    .contains(it.getTest_type()))
                            .collect(Collectors.toList()));
                    wantedTests.add(t);
                }
            }
        }

        ExcelSheet e = new ExcelSheet();
        e.setListOfAllTests(wantedTests);
        return FXCollections.observableArrayList(e.generateTestWrappersForAllTest());
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

    public ObservableList<String> getDatesForPart(Map<String, List<Test>> lst) {
        List<String> dates = new ArrayList<>();
        lst.forEach((date, test) -> {
            for (int i = 0; i < test.size(); i++) {
                dates.add(test.get(i).getDocument_nr() + " " + test.get(i).getDate() + "#" + i);
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

        if (observableListItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Nothing to export!");
            alert.showAndWait();
        }

        if (dropdownTemplates.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No template selected!");
            alert.showAndWait();
        } else {

            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel", ".xlsx"));

            File selectedFile = fc.showSaveDialog(null);

            if (selectedFile != null) {
                try {
                    TemplateFinder.getInstance().findByName(dropdownTemplates.getValue()).
                            export(selectedFile.getAbsolutePath(), observableListItems);
                    showingDVPForPartTextField.setText("");
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("File exported succesfully!");
                    alert.showAndWait();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else System.out.println("zle");
        }

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