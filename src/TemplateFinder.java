import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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

    public List<Template> findAll() throws SQLException {
        List<Template> elements = new ArrayList<>();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM template")) {
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    Template t = new Template(r.getString(2),
                            r.getInt(1),
                            new XSSFWorkbook(new ByteArrayInputStream(r.getBytes(3))));
                    setCoordinatesForTemplate(t);
                    elements.add(t);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return elements;
    }

    public void setCoordinatesForTemplate(Template template) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM coordinates WHERE table_id = ?")) {
            s.setInt(1, template.databaseId);
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    template.row_ids.add(r.getInt(3));
                    template.col_ids.add(r.getInt(4));
                    template.sheet_ids.add(r.getInt(5));
                    template.result_names.add(TestTypeFinder.getInstance().returnTestName(r.getInt(6)));
                }
            }
        }
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

    public Template findByName(String name) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM template WHERE name = ?")) {
            s.setString(1, name);
            try (ResultSet r = s.executeQuery()) {
                List<Template> elements = new ArrayList<>();
                while (r.next()) {
                    Template t = new Template(r.getString(2),
                                                r.getInt(1),
                                                new XSSFWorkbook(new ByteArrayInputStream(r.getBytes(3))));
                    elements.add(t);
                }
                if (elements.size() == 1) return elements.get(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new Template();
    }

}







