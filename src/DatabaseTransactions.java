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

    public void insertPart(CatiaSheet mainPdf, List<CatiaSheet> subpartsCatiaSheetList) throws SQLException, IOException {

        DbContext.getConnection().setAutoCommit(false);
        try {
            mainPdf.insertIntoPart(); // inserts the main pdf

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

    public void editPartComment(String partID, String comm) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        DatabaseChange dc = new DatabaseChange(User.getName(), "Edited " + partID + " in the database", new Timestamp(System.currentTimeMillis()));
        dc.insert();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE part SET comment=? WHERE part_id = ?", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, comm);
            s.setString(2,partID);
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

    public void editTestWrapper(TestWrapper tw, Test test, TestResult testResult) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        DatabaseChange dc = new DatabaseChange(User.getName(), "Edited a test for " + test.getDocument_nr() + " in the database", new Timestamp(System.currentTimeMillis()));
        dc.insert();
        try {
            tw.editInDatabase(test, testResult);


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


    public void insertTemplate(Template template) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        try {
            template.insert();
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

    public void deleteTemplate(Template template) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        try {
            template.delete();
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

    public void insertTestList(List<Test> ltw) throws SQLException {

        DbContext.getConnection().setAutoCommit(false);
        try {
            ltw.forEach(test -> {
                try {
                    test.insert();
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
