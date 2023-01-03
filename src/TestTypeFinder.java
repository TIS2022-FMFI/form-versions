import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestTypeFinder {

    private static final TestTypeFinder INSTANCE = new TestTypeFinder();

    public static TestTypeFinder getInstance() {
        return INSTANCE;
    }

    private TestTypeFinder() {
    }

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
}







