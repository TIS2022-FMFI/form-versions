import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class for finding instances of TestWrapper stored in the database
 *
 * @author Jacob Kristof
 * @version 1.0
 */
public class TestWrapperFinder {

    private static final TestWrapperFinder INSTANCE = new TestWrapperFinder();
    public static TestWrapperFinder getInstance() {
        return INSTANCE;
    }

    private TestWrapperFinder() {
    }

    /**
     * Finds all instances of TestWrapper stored in the database
     *
     * @return the list of all instances
     * @throws SQLException the sql exception
     */
    public List<TestWrapper> findAll() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM dvp")) {
            try (ResultSet r = s.executeQuery()) {
                List<TestWrapper> elements = new ArrayList<>();
                while (r.next()) {
                    TestWrapper h = new TestWrapper(
                            r.getString(3),
                            r.getString(4),
                            r.getString(2),
                            r.getString(5),
                            TestTypeFinder.getInstance().returnTestName(r.getInt(10)),
                            r.getString(6),
                            r.getString(7),
                            r.getString(8),
                            r.getString(9)
                    );
                    elements.add(h);
                }
                return elements;
            }
        }
    }

    /**
     * Find all tests in the database for a certain part.
     *
     * @param partId the ID of the part we want tests for
     * @return the map of all tests, where key is the date and list of values are instances of TestWrapper
     * @throws SQLException the sql exception
     */
    public HashMap<String, List<TestWrapper>> findTestsForPart(String partId) throws SQLException {
        HashMap<String, List<TestWrapper>> map = new HashMap<>();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM dvp WHERE part_id = ?")) {
            s.setString(1, partId);
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    String date = r.getString(3);
                    TestWrapper h = new TestWrapper(
                            r.getString(3),
                            r.getString(4),
                            r.getString(2),
                            r.getString(5),
                            TestTypeFinder.getInstance().returnTestName(r.getInt(10)),
                            r.getString(6),
                            r.getString(7),
                            r.getString(8),
                            r.getString(9)
                    );
                    if (!map.containsKey(date)) {
                        map.put(date, new ArrayList<>());
                    }
                    map.get(date).add(h);
                }
                return map;
            }
        }
    }

}