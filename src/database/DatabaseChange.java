package database;

import java.sql.*;

/**
 * Class for a Database Change value. Holds information about the ID of the user who
 * did the change, the change itself and a timestamp.
 *
 * @author Jacob Kristof
 * @version 1.0
 */
public class DatabaseChange {

    private String workerId;
    private String change;
    private Timestamp timestamp;

    /**
     * Constructor containing all three class variables
     *
     * @param w ID of the user
     * @param c information about the change
     * @param t the timestamp
     */
    public DatabaseChange(String w, String c, Timestamp t) {
        workerId = w;
        change = c;
        timestamp = t;
    }

    public void insert() {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO db_log (user_id, time, value) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, this.workerId);
            s.setTimestamp(2, this.timestamp);
            s.setString(3, this.change);
            s.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String getWorkerId() {
        return workerId;
    }
    public String getChange() {
        return change;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
}
