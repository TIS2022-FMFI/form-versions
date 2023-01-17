import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Database Log window
 *
 * @author Peter Vercimak
 * @version 1.0
 */
public class DbLogController implements Initializable {

    public ListView<String> dbLogListView;
    private ObservableList<String> items = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            fillListView();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Refreshes the list with most up-to-date information from the database
     */
    public void refresh(ActionEvent actionEvent) throws SQLException {
        dbLogListView.getItems().clear();
        fillListView();
    }

    /**
     * Fills the list with information from the database
     */
    public void fillListView() throws SQLException {
        List<DatabaseChange> temp = new ArrayList<>();
        StringBuilder tempString = new StringBuilder();
        temp = DatabaseChangeFinder.getInstance().findAll();
        temp.forEach(i ->{
            tempString.append("User ").append(i.getWorkerId()).append(" ").append(i.getChange()).append(" at this timestamp : ").append(i.getTimestamp());
            items.add(tempString.toString());
            tempString.setLength(0);
        });
        dbLogListView.setItems(items);
    }
}
