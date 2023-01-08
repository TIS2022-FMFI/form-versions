import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class for finding instances of Test stored in the database
 *
 * @author Jacob Kristof
 * @version 1.0
 */
public class TestFinder {

    private static final TestFinder INSTANCE = new TestFinder();
    public static TestFinder getInstance() {
        return INSTANCE;
    }

    private TestFinder() {
    }

    /**
     * Find all tests in the database for a certain part.
     *
     * @param partId the ID of the part we want tests for
     * @return the list of all tests
     * @throws SQLException the sql exception
     */
    public List<Test> findTestsForPart(String partId) throws SQLException {
        List<Test> lst = new ArrayList<>();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM test WHERE part_id = ?")) {
            s.setString(1, partId);
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    Test h = new Test(
                            r.getString(2),
                            r.getString(4),
                            r.getString(5),
                            r.getString(3),
                            new ArrayList<>(),
                            r.getInt(1)
                    );
                    addResultsToTest(h);
                    lst.add(h);
                }
                return lst;
            }
        }
    }

    public void addResultsToTest(Test test) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM test_result WHERE test_id = ?")) {
            s.setInt(1, test.databaseId);
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    test.getTest_results().add(new TestResult(
                            TestTypeFinder.getInstance().returnTestName(r.getInt(1)),
                            r.getString(2),
                            r.getString(3),
                            r.getString(4),
                            r.getString(5)
                    ));
                }
            }
        }
    }

}