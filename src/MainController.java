import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    public  TabPane tabPane;




    public static void switchTab(int index){
        State.getTabPane().getSelectionModel().select(index);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        State.setTabPane(tabPane);
    }
}
