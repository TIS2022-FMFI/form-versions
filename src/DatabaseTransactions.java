import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for storing all database transactions that can be triggered throughout the app
 *
 * @author Jacob Kristof
 * @version 1.0
 */
public class DatabaseTransactions {

    /**
     * Insert a component pdf along with uploaded subparts. Also inserts parent-child connections to BOM table
     *
     * @param mainPdf                instance of the main pdf
     * @param subpartsCatiaSheetList lsit of all uplaoded subaprts
     */
    public void insertPart(CatiaSheet mainPdf, List<CatiaSheet> subpartsCatiaSheetList) throws SQLException, IOException {

        DbContext.getConnection().setAutoCommit(false);
        try {
            mainPdf.insertIntoPart();
            subpartsCatiaSheetList.forEach(cs -> { // inserts the parent-child connections
                cs.parents.forEach(parent -> {
                    try {
                        cs.insertIntoBom(parent);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                try {
                    cs.insertIntoPart(); // inserts the subpart itself
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Upload not succesful!");
            alert.showAndWait();
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Upload succesful!");
            alert.showAndWait();
        }
    }

    /**
     * Replaces the comment of a part in the database with a new one
     *
     * @param partID     id of the part we want to edit
     * @param newComment the new comment
     */
    public void editPartComment(String partID, String newComment) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE part SET comment=? WHERE part_id = ?", Statement.RETURN_GENERATED_KEYS)) {
            DatabaseChange dc = new DatabaseChange(User.getName(), "Edited a comment for " + partID, new Timestamp(System.currentTimeMillis()));
            dc.insert();
            s.setString(1, newComment);
            s.setString(2, partID);
            s.executeUpdate();
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Failed editing comment");
            alert.showAndWait();
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Comment edited succesfuly!");
            alert.showAndWait();
        }
    }

    /**
     * Edits a test in the databse, called when editing the tables in front end. Due to them having TestWrappers, we
     * need additional info
     *
     * @param editedTestWrapper the TestWrapper instance that has updated information
     * @param parentTest        the Test the TestWrapper belongs to
     * @param testResult        the TestResult that was updated in the TestWrapper
     */
    public void editTestWrapper(TestWrapper editedTestWrapper, Test parentTest, TestResult testResult) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        try {
            DatabaseChange dc = new DatabaseChange(User.getName(), "Edited a test for " + parentTest.getDocument_nr(), new Timestamp(System.currentTimeMillis()));
            dc.insert();
            editedTestWrapper.editInDatabase(parentTest, testResult);
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Failed editing test result");
            alert.showAndWait();
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Test result edited succesfuly!");
            alert.showAndWait();
        }
    }

    /**
     * Inserts a template into the database
     *
     * @param template the template we want to insert
     */
    public void insertTemplate(Template template) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        try {
            template.insert();
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Template upload not succesful!");
            alert.showAndWait();
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Template uploaded succesfuly!");
            alert.showAndWait();
        }
    }

    /**
     * Deletes a template from the database
     *
     * @param template the template we want to delete
     */
    public void deleteTemplate(Template template) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        try {
            template.delete();
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Delete not succesful!");
            alert.showAndWait();
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Template deleted succesfuly!");
            alert.showAndWait();
        }
    }

    /**
     * Inserts all Tests from a list
     *
     * @param listOfTests the list we want to upload
     */
    public void insertTestList(List<Test> listOfTests) throws SQLException {

        boolean check = true;
        DbContext.getConnection().setAutoCommit(false);
        try {
            for (Test t :
                    listOfTests) {
                t.insert();
            }
        } catch (SQLException e) {
                DbContext.getConnection().rollback();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Upload not succesful!");
                alert.showAndWait();
                check = false;
                throw new RuntimeException(e);
        }

        if (check) {
            DbContext.getConnection().setAutoCommit(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Upload succesful!");
            alert.showAndWait();
        }
    }

    /**
     * Deletes a CatiaSheet from the database
     *
     * @param partID the id of the part we want to delete
     */
    public void deleteCatiaSheet(String partID) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        DatabaseChange dc = new DatabaseChange(User.getName(), "Deleted CatiaSheed with id " + partID, new Timestamp(System.currentTimeMillis()));
        dc.insert();
        try {
            PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM part WHERE part_id = ?", Statement.RETURN_GENERATED_KEYS);
            s.setString(1, partID);
            s.executeUpdate();
            s = DbContext.getConnection().prepareStatement("DELETE FROM bom WHERE parent = ? OR child = ?", Statement.RETURN_GENERATED_KEYS);
            s.setString(1, partID);
            s.setString(2, partID);
            s.executeUpdate();
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Failed to delete CatiaSheet");
            alert.showAndWait();
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("CatiaSheet deleted succesfuly!");
            alert.showAndWait();
        }
    }

    /**
     * Deletes all tests for a part with given id
     *
     * @param partID the id of the part we want to delete the tests for
     */
    public void deleteTestsForPartId(String partID) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        DatabaseChange dc = new DatabaseChange(User.getName(), "Deleted all tests for " + partID, new Timestamp(System.currentTimeMillis()));
        dc.insert();
        try {
            PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM test WHERE part_id = ?", Statement.RETURN_GENERATED_KEYS);
            s.setString(1, partID);
            s.executeUpdate();
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Failed to delete tests");
            alert.showAndWait();
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Tests deleted succesfuly!");
            alert.showAndWait();
        }
    }


    public static boolean checkIfCanAddNewUser(String userName) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("select * from users where mail = ?")) {
            s.setString(1, userName);
            try (ResultSet r = s.executeQuery()) {
                return !r.next();
            }
        }
    }

    /**
     * Adds a new user to the database
     *
     * @param mail user's mail
     * @param password user's password in MD5
     * @param admin boolean whether new user is admin or not
     * @throws SQLException
     */
    public static void insertUser(String mail, String password, int admin) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        DatabaseChange dc = new DatabaseChange(User.getName(), "Added user " + mail, new Timestamp(System.currentTimeMillis()));
        dc.insert();
        try {
            PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO users (mail, psswrd, admin) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            s.setString(1, mail);
            s.setString(2, User.getPasswordMD5Hash(password));
            s.setInt(3, admin);
            s.executeUpdate();
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Failed to insert user");
            alert.showAndWait();
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("User added succesfully!");
            alert.showAndWait();
        }

    }

    /**
     * Delete all users in the given list from the database
     *
     * @param selectedUsers list of usernames
     * @throws SQLException
     */
    public static void deleteSelectedUsers(List<String> selectedUsers) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        DatabaseChange dc = new DatabaseChange(User.getName(), "Deleted " + selectedUsers.size() + "user/s", new Timestamp(System.currentTimeMillis()));
        dc.insert();
        try {
            for (String u : selectedUsers) {
                PreparedStatement s = DbContext.getConnection().prepareStatement("delete from users where mail = ?");
                    s.setString(1, u);
                    s.executeQuery();
                }
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Failed to delete user/s");
            alert.showAndWait();
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(selectedUsers.size() + " user/s deleted succesfuly!");
            alert.showAndWait();
        }
    }
}
