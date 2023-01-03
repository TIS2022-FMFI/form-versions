import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TemplateFinder {

    private static final TemplateFinder INSTANCE = new TemplateFinder();

    public static TemplateFinder getInstance() {
        return INSTANCE;
    }

    private TemplateFinder() {
    }

    public boolean exists(String name) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM template WHERE name = ?")) {
            s.setString(1, name);
            try (ResultSet r = s.executeQuery()) {
                List<String> elements = new ArrayList<>();
                while (r.next()) {
                    elements.add(r.getString(2));
                }
                return elements.size() > 0;
            }
        }
    }

}







