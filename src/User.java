import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class User {

    public static int res = -1;
    private static String name;

    public static String getName() {
        return name;
    }
    public static void setName(String name) {
        User.name = name;
    }
    public static int getRes() {
        return res;
    }
    public static void setRes(int res) {
        User.res = res;
    }

    public static void identifyYourself() {
        setRes(-1);
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Please fill in your login information");

        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancel);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(username::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(u -> {
            setName(u.getKey());
            try {
                boolean tempRes = findUserInDatabaseAndCheckPassword(u.getValue());
                if (tempRes) res = 1;
                else res = 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    public static boolean findUserInDatabaseAndCheckPassword(String password) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM users WHERE mail = ?")) {
            s.setString(1, getName());
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    return comparePasswords(password, r.getString(3));
                }
            }
        }
        return false;
    }

    public static boolean comparePasswords(String password, String hash) {
        return getPasswordMD5Hash(password).equals(hash);
    }

    public static String getPasswordMD5Hash(String password) {

        String generatedPassword = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generatedPassword;
    }

    public static void insert(String password) {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO users (mail, psswrd) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, name);
            s.setString(2, getPasswordMD5Hash(password));
            s.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void delete() {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM users WHERE mail = ?", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, name);
            s.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static List<String> getAll() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM users")) {
            try (ResultSet r = s.executeQuery()) {
                List<String> elements = new ArrayList<>();
                while (r.next()) {
                    elements.add(r.getString(2));
                }
                return elements;
            }
        }
    }

}
