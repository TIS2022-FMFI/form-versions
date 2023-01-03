import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseChangeFinder {

    private static final DatabaseChangeFinder INSTANCE = new DatabaseChangeFinder();

    public static DatabaseChangeFinder getInstance() {
        return INSTANCE;
    }

    private DatabaseChangeFinder() {
    }

    public List<DatabaseChange> findAll() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM db_log ORDER BY time DESC")) {
            try (ResultSet r = s.executeQuery()) {
                List<DatabaseChange> elements = new ArrayList<>();
                while (r.next()) {
                    DatabaseChange h = new DatabaseChange(
                            r.getString(2),
                            r.getString(4),
                            r.getTimestamp(3)
                    );
                    elements.add(h);
                }
                return elements;
            }
        }
    }

}







