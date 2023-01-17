import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    public String currClickedOn = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (partIDInput != null) {

            partIDInput.textProperty().addListener(v -> {
                System.out.println(partIDInput.getText());
                try {
                    partHistoryListView.getItems().clear();
                    BOMListView.getItems().clear();
                    currImageForPart.setImage(null);
                    partComment.setText("");
                    partHistoryListView.getItems().addAll(getDBInfoPartListView(partIDInput.getText()));
                } catch (SQLException e) {
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

    public List<String> getDBInfoPartListView(String part) throws SQLException {
        return CatiaSheetFinder.getInstance().findHistoryForPart(part).stream().map(it -> it.documentNo + it.version).collect(Collectors.toList());
    }

    public List<String> getDBInfoBOMListView(String selectedPart) throws SQLException {
        return BomFinder.getInstance().findBomForPart(selectedPart);
    }

    public String getDBInfoPartComment(String selectedPart) throws SQLException {
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

    public void fillPartHistoryListview() throws SQLException {
        currClickedOn = partHistoryListView.getSelectionModel().getSelectedItem();
        BOMListView.getItems().clear();
        BOMListView.getItems().addAll(getDBInfoBOMListView(currClickedOn));
        partComment.setText(getDBInfoPartComment(currClickedOn));
        currImageForPart.setImage(getSelectedPartImage(currClickedOn));
    }

    public void fillPartBOMListview() throws SQLException {
        currClickedOn = BOMListView.getSelectionModel().getSelectedItem();
        partComment.setText(getDBInfoPartComment(currClickedOn));
        currImageForPart.setImage(getSelectedPartImage(currClickedOn));
    }

    public void showDVP(ActionEvent actionEvent) {
        if (partIDInput != null) {
            State.getTextField().setText(partIDInput.getText());
            MainController.switchTab(1);
        }
    }

    public void removeSelectedPartFromDB(ActionEvent actionEvent) throws SQLException {
        if (!Objects.equals(currClickedOn, "")) {
            DatabaseTransactions dT = new DatabaseTransactions();
            dT.deleteCatiaSheet(currClickedOn);
            clearPage();
            partHistoryListView.getItems().addAll(getDBInfoPartListView(partIDInput.getText()));
        }
    }

    private void clearPage() {
        partHistoryListView.getItems().clear();
        BOMListView.getItems().clear();
        if (currImageForPart != null) currImageForPart.setImage(null);
        partComment.setText("");
        currClickedOn = "";
    }

    public void updateCommentOfPart(ActionEvent actionEvent) throws SQLException {
        DatabaseTransactions dT = new DatabaseTransactions();
        if (!Objects.equals(currClickedOn, "") && partComment != null) {
            dT.editPartComment(currClickedOn, partComment.getText());
        }

    }
}
