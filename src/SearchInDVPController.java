import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;

import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SearchInDVPController implements Initializable {
    @FXML
    public ComboBox<String> dropdownTemplates = new ComboBox<>();

    @FXML
    public TextField showingDVPForPartTextField;

    @FXML
    public Button exportToTemplateButton;

    @FXML
    public ListView<String> versionsListView;

    @FXML
    public ListView<String> datesForVersionListView;

    @FXML
    public ListView<String> testTypesListView;

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

    public Map<String, Map<String, List<Test>>> testsForCurrentSearch = new HashMap<>();

    public List<String> listTemplatov = null;



    private List<String> selectedVersionTempList = new ArrayList<>();
    private List<String> selectedDateForPartList = new ArrayList<>();
    private List<String> selectedTestTypesList = new ArrayList<>();


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
//            DatabaseTransactions dbt = new DatabaseTransactions();
//            try {
//                dbt.editTestWrapper(tw, testsForCurrentSearch.get(dateDropdown.getSelectionModel().getSelectedItem().split("#")[0])
//                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])),
//                        findTestResultInTestByName(testsForCurrentSearch.get(dateDropdown.getSelectionModel().getSelectedItem().split("#")[0])
//                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])), tw.getTestType()));
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }


        });


        sollDVPSearch.setCellValueFactory(new PropertyValueFactory<>("sollDVP"));
        sollDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());

        sollDVPSearch.setOnEditCommit(testWrapperStringCellEditEvent -> {
            int index = tableViewDVPSearch.getSelectionModel().getSelectedIndex();
            TestWrapper tw = tableViewDVPSearch.getItems().get(index);
            tw.setSoll(testWrapperStringCellEditEvent.getNewValue());
            System.out.println(tw.getSoll());
//            DatabaseTransactions dbt = new DatabaseTransactions();
//            try {
//                dbt.editTestWrapper(tw, testsForCurrentSearch.get(dateDropdown.getSelectionModel().getSelectedItem().split("#")[0])
//                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])),
//                        findTestResultInTestByName(testsForCurrentSearch.get(dateDropdown.getSelectionModel().getSelectedItem().split("#")[0])
//                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])), tw.getTestType()));
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }

        });


        plusDVPSearch.setCellValueFactory(new PropertyValueFactory<>("sollPlus"));
        plusDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());

        plusDVPSearch.setOnEditCommit(testWrapperStringCellEditEvent -> {
            int index = tableViewDVPSearch.getSelectionModel().getSelectedIndex();
            TestWrapper tw = tableViewDVPSearch.getItems().get(index);
            tw.setSollPlus(testWrapperStringCellEditEvent.getNewValue());
//            DatabaseTransactions dbt = new DatabaseTransactions();
//            try {
//                dbt.editTestWrapper(tw, testsForCurrentSearch.get(dateDropdown.getSelectionModel().getSelectedItem().split("#")[0])
//                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])),
//                        findTestResultInTestByName(testsForCurrentSearch.get(dateDropdown.getSelectionModel().getSelectedItem().split("#")[0])
//                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])), tw.getTestType()));
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }

        });
        minusDVPSearch.setCellValueFactory(new PropertyValueFactory<>("sollMinus"));
        minusDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());
        minusDVPSearch.setOnEditCommit(testWrapperStringCellEditEvent -> {
            int index = tableViewDVPSearch.getSelectionModel().getSelectedIndex();
            TestWrapper tw = tableViewDVPSearch.getItems().get(index);
            tw.setSollMinus(testWrapperStringCellEditEvent.getNewValue());
//            DatabaseTransactions dbt = new DatabaseTransactions();
//            try {
//                dbt.editTestWrapper(tw, testsForCurrentSearch.get(dateDropdown.getSelectionModel().getSelectedItem().split("#")[0])
//                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])),
//                        findTestResultInTestByName(testsForCurrentSearch.get(dateDropdown.getSelectionModel().getSelectedItem().split("#")[0])
//                                .get(Integer.parseInt(dateDropdown.getSelectionModel().getSelectedItem().split("#")[1])), tw.getTestType()));
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }

        });
        tableViewDVPSearch.setItems(observableListItems);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableViewDVPSearch.setEditable(true);

        try {
            fillTestTypeListView();
            setCheckBoxesForDates();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (showingDVPForPartTextField != null) {
            State.setTextField(showingDVPForPartTextField);
            showingDVPForPartTextField.textProperty().addListener(v -> {
                try {
                    dropdownTemplates.getItems().clear();
                    observableListItems.clear();
                    if (!Objects.equals(showingDVPForPartTextField.getText(), "")) {
                        getAllTestsSorted(showingDVPForPartTextField.getText());
                    }
                    fillVersionsOfPartListView();
                    if (listTemplatov == null) {
                        listTemplatov = TemplateFinder.getInstance().findAll().stream().map(it -> it.template_name).collect(Collectors.toList());
                    }
                    dropdownTemplates.getItems().addAll(listTemplatov);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void fillTestTypeListView() throws SQLException {
        List<String> testTypeNames = TestTypeFinder.getInstance().findAll();
        ObservableList<String> r = FXCollections.observableArrayList(testTypeNames);
        testTypesListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            BooleanProperty observable = new SimpleBooleanProperty(selectedTestTypesList.contains(item));
            observable.addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    selectedTestTypesList.add(item);
                } else selectedTestTypesList.remove(item);
                observableListItems = getTestWrappersForCurrentSelection();
                createTable();
            });
            return observable;
        }));
        testTypesListView.setItems(r);
    }

    private void fillVersionsOfPartListView(){
        List<String> partVersions = getAllAvailablePartsInSearch();
        ObservableList<String> r = FXCollections.observableArrayList(partVersions);
        versionsListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            BooleanProperty observable = new SimpleBooleanProperty(selectedVersionTempList.contains(item));
            observable.addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    selectedVersionTempList.add(item);
                } else selectedVersionTempList.remove(item);
                List<String> temp = getDatesForSelectedParts();
                ObservableList<String> tempp = FXCollections.observableArrayList(temp);
                datesForVersionListView.setItems(tempp);
                selectedDateForPartList = new ArrayList<>();
                observableListItems = getTestWrappersForCurrentSelection();
                createTable();
            });
            return observable;
        }));

        versionsListView.setItems(r);
        System.out.println(versionsListView);
    }

    private void setCheckBoxesForDates(){
        datesForVersionListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            BooleanProperty observable = new SimpleBooleanProperty(selectedDateForPartList.contains(item));
            observable.addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    selectedDateForPartList.add(item);

                } else selectedDateForPartList.remove(item);


                observableListItems = getTestWrappersForCurrentSelection();
                createTable();
            });
            return observable;
        }));

    }


    public List<Test> getAllTestsForPart(String partID) throws SQLException {
        return TestFinder.getInstance().findTestsForZostava(partID);
    }

    public void getAllTestsSorted(String partID) throws SQLException {
        for (Test t : getAllTestsForPart(partID)) {
            if (!testsForCurrentSearch.containsKey(t.getDocument_nr())) {
                testsForCurrentSearch.put(t.getDocument_nr(), new HashMap<String, List<Test>>());
                testsForCurrentSearch.get(t.getDocument_nr()).put(t.getDate(), new ArrayList<>());
                testsForCurrentSearch.get(t.getDocument_nr()).get(t.getDate()).add(t);
            } else {
                if (!testsForCurrentSearch.get(t.getDocument_nr()).containsKey(t.getDate())) {
                    testsForCurrentSearch.get(t.getDocument_nr()).put(t.getDate(), new ArrayList<>());
                    testsForCurrentSearch.get(t.getDocument_nr()).get(t.getDate()).add(t);
                } else {
                    testsForCurrentSearch.get(t.getDocument_nr()).get(t.getDate()).add(t);
                }
            }
        }


    }

    public ObservableList<String> getAllAvailablePartsInSearch() {
        return FXCollections.observableArrayList(testsForCurrentSearch.keySet()); // zoznam stringov verzii suciastok
    }

    public List<String> getDatesForSelectedParts() {
        List<String> dates = new ArrayList<>();
        for (String part : selectedVersionTempList) {
            for (String date : testsForCurrentSearch.get(part).keySet()) {
                for (int i = 0; i < testsForCurrentSearch.get(part).get(date).size(); i++) {
                    dates.add(testsForCurrentSearch.get(part).get(date).get(i).getDocument_nr() + " " + date + "#" + i);
                }
            }
        }
        return dates;
    }

    // to be called when pressing search a chces dostat testwrappery pre zobrazenie testov
    public ObservableList<TestWrapper> getTestWrappersForCurrentSelection() {

        List<Test> wantedTests = new ArrayList<>();

        if (!selectedDateForPartList.isEmpty() && !selectedVersionTempList.isEmpty()) {

            for (String part : testsForCurrentSearch.keySet()) {
                for (String date : testsForCurrentSearch.get(part).keySet()) {
                    for (int i = 0; i < testsForCurrentSearch.get(part).get(date).size(); i++) {

                        if (selectedVersionTempList.contains(part) && selectedDateForPartList.contains(part + " " + date + "#" + i)) {

                            if (selectedTestTypesList.isEmpty()) {

                                wantedTests.add(testsForCurrentSearch.get(part).get(date).get(i));

                            } else {

                                Test ref = testsForCurrentSearch.get(part).get(date).get(i);
                                Test t = new Test(ref.getDate(), ref.getAA(), ref.getDocument_nr(), ref.getCustomer_nr(), new ArrayList<>(), 1);
                                List<TestResult> trl = new ArrayList<>();
                                for (TestResult tr : ref.getTest_results()) {
                                    if (selectedTestTypesList.contains(tr.getTest_type())) {
                                        trl.add(tr);
                                    }
                                }
                                t.setTest_results(trl);
                                wantedTests.add(t);
                            }
                        }
                    }
                }
            }
        }

        ExcelSheet e = new ExcelSheet();
        e.setListOfAllTests(wantedTests);
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