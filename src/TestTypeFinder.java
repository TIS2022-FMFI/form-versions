import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for finding instances of TestType stored in the database
 *
 * @author Jacob Kristof
 * @version 1.0
 */
public class TestTypeFinder {

    private static final TestTypeFinder INSTANCE = new TestTypeFinder();
    public static TestTypeFinder getInstance() {
        return INSTANCE;
    }

    private TestTypeFinder() {
    }

    /**
     * Find all instances of TestType stored in the database
     *
     * @return the list of the instances
     * @throws SQLException the sql exception
     */
    public List<String> findAll() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM test_types ORDER BY id")) {
            try (ResultSet r = s.executeQuery()) {
                List<String> elements = new ArrayList<>();
                while (r.next()) {
                    elements.add(r.getString(2));
                }
                return elements;
            }
        }
    }

    /**
     * Finds the ID of the test based on its name. Returns -1 if the name does not exist
     *
     * @param testName the name of the test we want the ID for
     * @return the ID
     * @throws SQLException the sql exception
     */
    public Integer returnIdInTable(String testName) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM test_types WHERE name = ?")) {
            s.setString(1, testName);
            try (ResultSet r = s.executeQuery()) {
                List<Integer> elements = new ArrayList<>();
                while (r.next()) {
                    elements.add(r.getInt(1));
                }
                if (elements.size() == 1) {
                    return elements.get(0);
                } else {
                    return -1;
                }
            }
        }
    }

    /**
     * Finds the ID of the test based on its name. Returns "null" if the ID does not exist
     *
     * @param id the ID of the test we want the name for
     * @return the name
     * @throws SQLException the sql exception
     */
    public String returnTestName(int id) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM test_types WHERE id = ?")) {
            s.setInt(1, id);
            try (ResultSet r = s.executeQuery()) {
                List<String> elements = new ArrayList<>();
                while (r.next()) {
                    elements.add(r.getString(2));
                }
                if (elements.size() == 1) {
                    return elements.get(0);
                } else {
                    return "null";
                }
            }
        }
    }
}







