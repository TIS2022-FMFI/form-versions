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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    @FXML
    public ImageView currImageForPart;








    //odtialto dolu je druha Scene




    ////////////////////////////////////////////////


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (partIDInput != null) {

            partIDInput.textProperty().addListener(v -> {
                //sem treba nakodit select z databazy na konkretny search
                System.out.println(partIDInput.getText());   // <- v partIDInput je pri zmene nacitany konkretny string s ktorym mozes pracovat kubko aby si hladal v DB
                try {

                    partHistoryListView.getItems().clear();
                    BOMListView.getItems().clear();
                    currImageForPart.setImage(null);
                    partComment.setText("");
                    partHistoryListView.getItems().addAll(getDBInfoPartListView(partIDInput.getText()));
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            });



            partHistoryListView.setOnMouseClicked(mouseEvent ->
            {
                try {
                    fillPartHistoryListview();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            BOMListView.setOnMouseClicked(mouseEvent -> {
                try {
                    fillPartBOMListview();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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
        if (CatiaSheetFinder.getInstance().findWithId(selectedPart).size() > 0) {
            return CatiaSheetFinder.getInstance().findWithId(selectedPart).get(0).lastHeaderChange;
        }
        return "";
    }

    public Image getSelectedPartImage(String partId) throws SQLException {
        if (CatiaSheetFinder.getInstance().findWithId(partId).size() > 0) {
            return CatiaSheetFinder.getInstance().findWithId(partId).get(0).image;
        }
        return null;
    }



//    public void showDVPScene(ActionEvent actionEvent) throws IOException {
//
//        System.out.println("im here");
//
//        Parent root =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("xmlka/showDVPForPartXML.fxml")));
//        Scene scene = new Scene(root);
//        Stage thisStage = (Stage) showDVPForPartButton.getScene().getWindow();
//        thisStage.setScene(scene);
//
//
//    }


    public void fillPartHistoryListview() throws SQLException {
        String selected = partHistoryListView.getSelectionModel().getSelectedItem();
        BOMListView.getItems().clear();
        BOMListView.getItems().addAll(getDBInfoBOMListView(selected));
        partComment.setText(getDBInfoPartComment(selected));
        currImageForPart.setImage(getSelectedPartImage(selected));
    }

    public void fillPartBOMListview() throws SQLException {
        String selected = BOMListView.getSelectionModel().getSelectedItem();
        partComment.setText(getDBInfoPartComment(selected));
        currImageForPart.setImage(getSelectedPartImage(selected));
    }










    //odtialto dolu funkcionalita druhej sceny kde sa riesia dvpcka



//    public void returnToSearchPage(ActionEvent actionEvent) throws IOException {
//        Parent root =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("xmlka/main.fxml")));
//        Scene scene = new Scene(root);
//        Stage thisStage = (Stage) returnToSearchButton.getScene().getWindow();
//        thisStage.setScene(scene);
//
//    }




    public void showDVP(ActionEvent actionEvent) {
        State.getTextField().setText(partIDInput.getText());
        MainController.switchTab(1);
    }
}
