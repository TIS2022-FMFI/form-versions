import org.bouncycastle.util.Times;

import java.sql.*;

public class DatabaseChange {

    private String workerId;
    private String change;
    private Timestamp timestamp;

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

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
