import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class Main  extends Application{
    Stage mainStage;
    public static void main(String[] args) throws IOException, SQLException {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/moja", "root", "root");
            System.out.println("Success");

            if (connection != null) {
                DbContext.setConnection(connection);
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        launch(args);



    }

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("xmlka/main.fxml")));
        stage.setTitle("BogeParser (Logged in as dummyString)");
        stage.setScene(new Scene(root));
        stage.show();
    }


    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
}
