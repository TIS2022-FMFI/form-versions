import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class AdminController implements Initializable {

    @FXML
    public TextField newMailTextField;

    @FXML
    public CheckBox isAdminCheckBox;

    @FXML
    public PasswordField newPasswordField;

    @FXML
    public TextField searchForThisUserTextField;

    @FXML
    public ListView<String> searchResultListView;


    List<String> allUsers;
    ObservableList<String> o;
    List<String> selectedUsers;

    public void addNewUser(ActionEvent actionEvent) throws SQLException {
        if (newMailTextField == null || Objects.equals(newMailTextField.getText(), "")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Please fill in Email of new User !");
            alert.showAndWait();
        }

        else if (newPasswordField == null || Objects.equals(newPasswordField.getText(), "")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Please fill in password of new User ! ");
            alert.showAndWait();
        }
        else {
            int newUserIsAdmin = 0;
            if (isAdminCheckBox.isSelected()) newUserIsAdmin = 1;

            try {
                if (DatabaseTransactions.checkIfCanAddNewUser(newMailTextField.getText())) {
                    DatabaseTransactions.insert(newMailTextField.getText(), newPasswordField.getText(), newUserIsAdmin);
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("User with the same email adress is already in the System ! Please try a different one.");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            isAdminCheckBox.setSelected(false);
            newPasswordField.setText("");
            newMailTextField.setText("");
            loadAllUsersToListView();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            allUsers = new ArrayList<>();
            o = FXCollections.observableArrayList(allUsers);
            loadAllUsersToListView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        selectedUsers = new ArrayList<>();

        searchResultListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            BooleanProperty observable = new SimpleBooleanProperty(selectedUsers.contains(item));
            observable.addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    selectedUsers.add(item);
                } else selectedUsers.remove(item);
                System.out.println(selectedUsers);
            });
            return observable;
        }));
    }

    public void loadAllUsersToListView() throws SQLException {
        searchForThisUserTextField.setText("");
        allUsers = DatabaseTransactions.getAllUsers();
        o = FXCollections.observableArrayList(allUsers);
        searchResultListView.setItems(o);
    }

    public void searchForAndShowSpecificUser(ActionEvent actionEvent) throws SQLException {
        if (!Objects.equals(searchForThisUserTextField.getText(), "")){
            allUsers = Collections.singletonList(DatabaseTransactions.getSpecificUser(searchForThisUserTextField.getText()));
            o = FXCollections.observableArrayList(allUsers);
            searchResultListView.setItems(o);
        }
    }


    public void removeSelectedUsers(ActionEvent actionEvent) throws SQLException {
        List<String> selecteduserMails = new ArrayList<>();
        for (String s : selectedUsers){
            selecteduserMails.add(s.split(" ")[0]);
        }
        DatabaseTransactions.deleteSelectedUsers(selecteduserMails);
        loadAllUsersToListView();
        if (!selectedUsers.isEmpty())
        selectedUsers.clear();
    }

}
