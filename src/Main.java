import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {
    public static void main(String[] args) {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/moja", "root", "root");
            System.out.println("Success");

            if (connection != null) {
                DbContext.setConnection(connection);

                CatiaSheet cs = PDFParser.parseFile("src/cat3.pdf");


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
        Parent root = FXMLLoader.load(getClass().getResource("versionOne.fxml"));
        stage.setTitle("BogeParser");
        stage.setScene(new Scene(root));
        stage.show();
    }

}
