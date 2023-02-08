package finders;

import database.DatabaseChange;
import database.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for finding instances of database.DatabaseChange stored in the database
 *
 * @author Jacob Kristof
 * @version 1.0
 */
public class DatabaseChangeFinder {

    private static final DatabaseChangeFinder INSTANCE = new DatabaseChangeFinder();
    public static DatabaseChangeFinder getInstance() {
        return INSTANCE;
    }

    private DatabaseChangeFinder() {
    }

    /**
     * Find all changes stored in the database and return them as a list of database.DatabaseChange instances
     *
     * @return the list of all changes as instances of database.DatabaseChange
     * @throws SQLException the sql exception
     */
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

    /**
     * Find all changes made by a user stored in the database and return them as a list of database.DatabaseChange instances
     *
     * @param name the user we want to search for
     * @return the list of all changes as instances of database.DatabaseChange
     * @throws SQLException the sql exception
     */
    public static List<DatabaseChange> findWhereName(String name) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM db_log WHERE user_id = ? ORDER BY time DESC")) {
            s.setString(1, name);
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







