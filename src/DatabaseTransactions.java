import org.bouncycastle.asn1.tsp.TSTInfo;

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
                    throw new RuntimeException(e);
                }
            });
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }

}
