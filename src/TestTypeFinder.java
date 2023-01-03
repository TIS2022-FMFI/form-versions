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

}







