package finders;

import database.DbContext;
import dvp.Template;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for finding instances of dvp.Template stored in the database
 *
 * @author Jacob Kristof
 * @version 1.0
 */
public class TemplateFinder {

    private static final TemplateFinder INSTANCE = new TemplateFinder();
    public static TemplateFinder getInstance() {
        return INSTANCE;
    }

    private TemplateFinder() {
    }

    /**
     * Find all instances of dvp.Template stored in the database
     *
     * @return the list of the instances
     * @throws SQLException the sql exception
     */
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

    /**
     * Finds all coordinates and values stored in the database for the given template and adds them to the instance
     *
     * @param template the template we want to add coordinate info to
     * @throws SQLException the sql exception
     */
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


    /**
     * Checks whether a dvp.Template with given name already exists in the database
     *
     * @param name the name we want to check
     * @return true/false regarding the status
     * @throws SQLException the sql exception
     */
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

    /**
     * Finds the dvp.Template in the database by the name and returns its instance. If it doesn't exist, return empty dvp.Template
     *
     * @param name the name of the template we want to look up
     * @return the instance of the template
     * @throws SQLException the sql exception
     */
    public Template findByName(String name) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM template WHERE name = ?")) {
            s.setString(1, name);
            try (ResultSet r = s.executeQuery()) {
                List<Template> elements = new ArrayList<>();
                while (r.next()) {
                    Template t = new Template(r.getString(2),
                                                r.getInt(1),
                                                new XSSFWorkbook(new ByteArrayInputStream(r.getBytes(3))));
                    setCoordinatesForTemplate(t);
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







