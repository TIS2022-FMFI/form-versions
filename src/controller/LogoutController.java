package controller;

import backend.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogoutController implements Initializable {

    public void logOut(ActionEvent actionEvent) throws IOException {
        User.setuPsswrd("");
        User.setName("");
        User.updateUserAppConfig();
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
