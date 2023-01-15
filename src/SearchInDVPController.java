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


    ObservableList<TestWrapper> observableListItems;

    public Map<String, List<Test>> testsForCurrentSearch;

    public List<String> listTemplatov = null;


    public void createTable() {

        docNumDVPSearch.setCellValueFactory(new PropertyValueFactory<>("documentNr"));
        docNumDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());


        dateDVPSearch.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());


        aaDVPSearch.setCellValueFactory(new PropertyValueFactory<>("AA"));
        aaDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());


        custNumDVPSearch.setCellValueFactory(new PropertyValueFactory<>("customerNr"));
        custNumDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());


        testTypeDVPSearch.setCellValueFactory(new PropertyValueFactory<>("testType"));
        testTypeDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());


        testResDVPSearch.setCellValueFactory(new PropertyValueFactory<>("testResult"));
        testResDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());


        sollDVPSearch.setCellValueFactory(new PropertyValueFactory<>("sollDVP"));
        sollDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());


        plusDVPSearch.setCellValueFactory(new PropertyValueFactory<>("sollPlus"));
        plusDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());


        minusDVPSearch.setCellValueFactory(new PropertyValueFactory<>("sollMinus"));
        minusDVPSearch.setCellFactory(TextFieldTableCell.forTableColumn());


        tableViewDVPSearch.setItems(observableListItems);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

}
