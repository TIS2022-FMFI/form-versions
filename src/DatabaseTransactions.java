import java.io.IOException;
import java.sql.SQLException;
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
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }

    public void insertTemplate(String uid, Template template) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        try {
            template.insert(uid);
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }

    public void deleteTemplate(String uid, Template template) throws SQLException {
        DbContext.getConnection().setAutoCommit(false);
        try {
            template.delete(uid);
        } catch (SQLException e) {
            DbContext.getConnection().rollback();
            throw new RuntimeException(e);
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }

}
