import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DatabaseTransactions {

    public void insertPart(CatiaSheet mainPdf, String uid, List<CatiaSheet> subpartsCatiaSheetList) throws SQLException, IOException {

        DbContext.getConnection().setAutoCommit(false);
        try {
            mainPdf.insertIntoPart(uid); // inserts the main pdf

            subpartsCatiaSheetList.forEach(cs -> { // inserts the parent-child connections
                if (!cs.parents.isEmpty()) {
                    cs.parents.forEach(parent -> {
                        try {
                            cs.insertIntoBom(parent, uid);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                }
                try {
                    cs.insertIntoBom(mainPdf.documentNo+mainPdf.version, uid);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

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

}
