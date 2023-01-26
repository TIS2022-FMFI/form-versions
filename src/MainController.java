import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the whole application
 *
 * @author Peter Vercimak
 * @version 1.0
 */
public class MainController implements Initializable {

    @FXML
    public TabPane tabPane;

    @FXML
    public Tab adminTab;

    /**
     * Switches toa pane with desired index
     */
    public static void switchTab(int index) {
        State.getTabPane().getSelectionModel().select(index);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        State.setTabPane(tabPane);
        State.setAdminTab(adminTab);
    }
}
