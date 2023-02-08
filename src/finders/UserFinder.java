package finders;

import database.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for finding instances of backend.User stored in the database
 *
 * @author Jacob Kristof
 * @version 1.0
 */
public class UserFinder {

    private static final UserFinder INSTANCE = new UserFinder();
    public static UserFinder getInstance() {
        return INSTANCE;
    }

    private UserFinder() {
    }

    /**
     * Find all instances of backend.User stored in the database
     *
     * @return the list of the instances
     * @throws SQLException the sql exception
     */
    public static List<String> getAllUsers() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM users")) {
            try (ResultSet r = s.executeQuery()) {
                List<String> elements = new ArrayList<>();
                String mail;
                int adm;
                String finalString;
                while (r.next()) {
                    mail = r.getString("mail");
                    adm = r.getInt("admin");
                    finalString =  mail + " " + adm;
                    elements.add(finalString);
                }
                return elements;
            }
        }
    }

    /**
     * Find all user info from a given mail
     *
     * @param userMail the mail of the user we want the ID for
     * @return String in a "MAIL ADMIN" format
     * @throws SQLException the sql exception
     */
    public static String getSpecificUser(String userMail) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM users where mail = ?")) {
            s.setString(1, userMail);
            try (ResultSet r = s.executeQuery()) {
                String mail;
                int adm;
                String finalString;
                while (r.next()) {
                    mail = r.getString("mail");
                    adm = r.getInt("admin");
                    finalString = mail + " " + adm;
                    return finalString;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * Checks if a user with given adress exists in the database
     *
     * @param userName the username we want to check
     * @return the boolean
     * @throws SQLException
     */
    public static boolean checkIfCanAddNewUser(String userName) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("select * from users where mail = ?")) {
            s.setString(1, userName);
            try (ResultSet r = s.executeQuery()) {
                return !r.next();
            }
        }
    }
}







