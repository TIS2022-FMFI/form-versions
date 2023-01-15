import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import org.bouncycastle.asn1.tsp.TSTInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

public class DatabaseTransactions {

    public void insertPart(CatiaSheet mainPdf, String uid, List<CatiaSheet> subpartsCatiaSheetList) throws SQLException, IOException {

        DbContext.getConnection().setAutoCommit(false);
        try {
            mainPdf.insertIntoPart(uid); // inserts the main pdf

            subpartsCatiaSheetList.forEach(cs -> { // inserts the parent-child connections
                cs.parents.forEach(parent -> {
                    try {
                        cs.insertIntoBom(parent, uid);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });


                try {
                    cs.insertIntoPart(uid); // inserts the subpart itself

                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Upload not succesful!");
            alert.showAndWait();
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Upload succesful!");
            alert.showAndWait();
        }
    }

    public void editPartComment(String partID, String comm, String uid) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        DatabaseChange dc = new DatabaseChange(uid, "Edited " + partID + " in the database", new Timestamp(System.currentTimeMillis()));
        dc.insert();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE part SET comment=? WHERE part_id = ?", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, comm);
            s.executeUpdate();
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Failed editing comment");
            alert.showAndWait();
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Comment edited succesfuly!");
            alert.showAndWait();
        }
    }

    public void editTestWrapper(TestWrapper tw, Test test, TestResult testResult, String uid) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        DatabaseChange dc = new DatabaseChange(uid, "Edited a test for " + test.getDocument_nr() + " in the database", new Timestamp(System.currentTimeMillis()));
        dc.insert();
        try {
            tw.editInDatabase(uid, test, testResult);


        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Failed editing test result");
            alert.showAndWait();
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Test result edited succesfuly!");
            alert.showAndWait();
        }
    }


    public void insertTemplate(String uid, Template template) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        try {
            template.insert(uid);
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Template upload not succesful!");
            alert.showAndWait();
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Template uploaded succesfuly!");
            alert.showAndWait();
        }
    }

    public void deleteTemplate(String uid, Template template) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        try {
            template.delete(uid);
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Delete not succesful!");
            alert.showAndWait();
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Template deleted succesfuly!");
            alert.showAndWait();
        }
    }

    public void insertTestWrapper(String uid, TestWrapper testWrapper) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        try {
            testWrapper.insert(uid);
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }

    public void insertTestWrapperList(String uid, List<TestWrapper> ltw) throws SQLException {

        DbContext.getConnection().setAutoCommit(false);
        try {
            ltw.forEach(testWrapper -> {
                try {
                    testWrapper.insert(uid);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }

    public void insertTestList(String uid, List<Test> ltw) throws SQLException {

        DbContext.getConnection().setAutoCommit(false);
        try {
            ltw.forEach(test -> {
                try {
                    test.insert(uid);
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Upload not succesful!");
                    alert.showAndWait();
                    throw new RuntimeException(e);

                }
            });
        } finally {
            DbContext.getConnection().setAutoCommit(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Upload succesful!");
            alert.showAndWait();
        }
    }

    public void deleteCatiaSheet(String partID, String uid) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        DatabaseChange dc = new DatabaseChange(uid, "Deleted CatiaSheed with id " + partID + " in the database", new Timestamp(System.currentTimeMillis()));
        dc.insert();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM part WHERE part_id = ?", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, partID);
            s.executeUpdate();
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Failed to delete CatiaSheet");
            alert.showAndWait();
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("CatiaSheet deleted succesfuly!");
            alert.showAndWait();
        }
    }


}
