import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

/**
 * Singleton class that manages the switching between search in DB and search in DVP
 *
 * @author Peter Vercimak
 * @version 1.0
 */
public class State {

    public static TabPane tabPane;
    public static TextField textField;
    public static Tab adminTab;

    public static TabPane getTabPane() {
        return tabPane;
    }
    public static void setTabPane(TabPane tabPane) {
        State.tabPane = tabPane;
    }
    public static TextField getTextField() {
        return textField;
    }
    public static void setTextField(TextField textField) {
        State.textField = textField;
    }
    public static Tab getAdminTab() {
        return adminTab;
    }
    public static void setAdminTab(Tab adminTab) {
        State.adminTab = adminTab;
    }
}
