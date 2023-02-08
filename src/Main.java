import backend.State;
import backend.User;
import database.DbContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/**
 * Main class that runs the program, sets up the database connection and starts the front end
 *
 * @author Jacob Kristof
 * @version 1.0
 */
public class Main extends Application {
    Stage mainStage;

   static boolean logged = false;



    public static void main(String[] args) {

        try {
            java.util.Properties prop = new Properties();
            prop.loadFromXML(Files.newInputStream(Paths.get("./configuration.xml")));
            Connection connection = DriverManager.getConnection(
                    prop.getProperty("database"),
                    prop.getProperty("user"),
                    prop.getProperty("password"));

            if (connection != null) {
                System.out.println("Success");
                DbContext.setConnection(connection);

                String loggedName = prop.getProperty("saved_user");
                String loggedPsswrd = prop.getProperty("saved_password");

                if (!Objects.equals(loggedName, "") && !Objects.equals(loggedPsswrd, "")){
                    if (User.findUserInDatabaseAndCheckPassword(loggedName,loggedPsswrd)) {
                        logged = true;
                        User.setName(loggedName);
                        User.setuPsswrd(loggedPsswrd);
                    }
                }
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

    /**
     * Starts the frontend after succesfully logging in
     */
    @Override
    public void start(Stage stage) throws Exception {

        stage.setResizable(false);
        if (logged) drawStage(stage);
        else {
            User.identifyYourself();

            if (User.getRes() == 1) {
                drawStage(stage);
                User.updateUserAppConfig();

            } else if (User.getRes() == 0) {
                ButtonType log = new ButtonType("Login again", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.NONE, "Wrong user login information, please try again !", log);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == log) {
                    start(stage);
                }
            } else {
                ButtonType log = new ButtonType("Login again", ButtonBar.ButtonData.OK_DONE);
                ButtonType exit = new ButtonType("Exit", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.NONE, "Do you really want to exit?", log, exit);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.orElse(exit) == log) {
                    start(stage);
                }
            }

        }
        User.checkIfIsAdmin();
        if (User.getIsAdmin() != 1) State.getAdminTab().setDisable(true);




    }

    public void drawStage(Stage stage) throws IOException {
        mainStage = stage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("xml/main.fxml")));
        Scene s = new Scene(root);
        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(s);
        stage.setTitle("FormVersions (Logged in as " + User.getName() + ")");
        stage.getIcons().add(new Image("img/logo.png"));
        stage.setScene(s);
        stage.show();
    }


    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
}
