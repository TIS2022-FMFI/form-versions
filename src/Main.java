import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Main  extends Application{
    Stage mainStage;
    public static void main(String[] args) throws IOException, SQLException {

        try {
            java.util.Properties prop = new Properties();
            prop.loadFromXML(Files.newInputStream(Paths.get("./conf.xml")));
            Connection connection = DriverManager.getConnection(
                    prop.getProperty("database"),
                    prop.getProperty("user"),
                    prop.getProperty("password"));


            if (connection != null) {
                System.out.println("Success");
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


        User.identifyYourself();
        if (User.isRes()){
        User.setName("bogeman");
            mainStage = stage;

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("xmlka/main.fxml")));

            stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("imgs/logo.png"))));
            Scene s = new Scene(root);
            JMetro jMetro = new JMetro(Style.LIGHT);
            jMetro.setScene(s);




            stage.setTitle("BogeParser (Logged in as "+User.getName()+")");
            stage.setScene(s);
            stage.show();


        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Wrong login information! Please try again.");
            alert.showAndWait();
            start(stage);

        }
    }


    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
}
