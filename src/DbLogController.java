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

public class DbLogController implements Initializable {
    public ListView<String> dbLogListView;

    private ObservableList<String> items = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillListView();
    }


    public void refresh(ActionEvent actionEvent) {
        dbLogListView.getItems().clear();
        fillListView();
    }

    public void fillListView(){
        try {
            List<DatabaseChange> temp = new ArrayList<>();
            StringBuilder tempString = new StringBuilder();
            temp = DatabaseChangeFinder.getInstance().findAll();
            temp.forEach(i ->{
                tempString.append("User ").append(i.getWorkerId()).append(" ").append(i.getChange()).append(" at this timestamp : ").append(i.getTimestamp());
                items.add(tempString.toString());
                tempString.setLength(0);
            });
            dbLogListView.setItems(items);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
