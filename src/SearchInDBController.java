import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SearchInDBController implements Initializable {

    @FXML
    public TextField partIDInput;

    @FXML
    public ListView<String> partHistoryListView;

    @FXML
    public ListView<String> BOMListView;

    @FXML
    public TextArea partComment;

    @FXML
    public Button showDVPForPartButton;








    //odtialto dolu je druha Scene
    @FXML
    public Button returnToSearchButton;

    @FXML
    public ComboBox<String> dropdownTemplates;

    @FXML
    private TextField showingDVPForPartTextField;

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



    ////////////////////////////////////////////////


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (partIDInput != null) {
            partIDInput.textProperty().addListener(v -> {
                //sem treba nakodit select z databazy na konkretny search
                System.out.println(partIDInput.getText());   // <- v partIDInput je pri zmene nacitany konkretny string s ktorym mozes pracovat kubko aby si hladal v DB
            });
        }
    }

    public List<String> getDBInfoPartListView(String part) throws SQLException {    //vrati z db List vsetkych partov z historie
        return CatiaSheetFinder.getInstance().findHistoryForPart(part).stream().map(it -> it.documentNo + it.version).collect(Collectors.toList());
    }

    public List<String> getDBInfoBOMListView(String selectedPart) throws SQLException { //vráti z db List vsetkych BOM partov
        return CatiaSheetFinder.getInstance().findBomFOrPart(selectedPart);
    }

    public String getDBInfoPartComment(String selectedPart) throws SQLException {   //vrati komentar selected z databazy
        return CatiaSheetFinder.getInstance().findWithId(selectedPart).get(0).lastHeaderChange;
    }

    public void showDVPScene(ActionEvent actionEvent) throws IOException {
        Parent root =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("xmlka/showDVPForPartXML.fxml")));
        Scene scene = new Scene(root);
        Stage thisStage = (Stage) showDVPForPartButton.getScene().getWindow();
        thisStage.setScene(scene);
    }











    //odtialto dolu funkcionalita druhej sceny kde sa riesia dvpcka
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


//        tableViewDVPSearch.setItems(observableListItems);
    }


    public void returnToSearchPage(ActionEvent actionEvent) throws IOException {
        Parent root =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("xmlka/main.fxml")));
        Scene scene = new Scene(root);
        Stage thisStage = (Stage) returnToSearchButton.getScene().getWindow();
        thisStage.setScene(scene);

    }

    public List<Test> getAllTestsForPart(String partID) throws SQLException {
        return TestFinder.getInstance().findTestsForPart(partID);
    }

    public ObservableList<TestWrapper> getDVPTableFromDB(String partID) throws SQLException { // toto ta krasne poprosinkám urobiť kubko cmuq
        ExcelSheet e = new ExcelSheet();
        e.setListOfAllTests(getAllTestsForPart(partID));
        return FXCollections.observableArrayList(e.generateTestWrappersForAllTest());
    }


    public void exportDVPOfPartToTemplate(ActionEvent actionEvent) {
    }


//    public void setPartHistoryListView(){
//
//    }
//
//    public void setBOMListView(){
//
//    }
//
//    public void setPartComment(){
//
//    }
}
