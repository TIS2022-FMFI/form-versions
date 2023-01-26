import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for finding instances of BOM stored in the database
 *
 * @author Jacob Kristof
 * @version 1.0
 */
public class BomFinder {

    private static final BomFinder INSTANCE = new BomFinder();
    public static BomFinder getInstance() {
        return INSTANCE;
    }

    private BomFinder() {
    }

    /**
     * Check if a pair of parent-child is already stored in the database
     *
     * @param parentId the parent id
     * @param childId  the child id
     * @return true/false
     */
    public boolean findIfExistsPair(String parentId, String childId) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM bom")) {
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    if (r.getString(3).equals(parentId) && r.getString(2).equals(childId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Find all children of a parent stored in the database
     *
     * @param partID the parent id
     * @return the list of String of partIDs of children
     * @throws SQLException the sql exception
     */
    public List<String> findBomForPart(String partID) throws SQLException {
        List<String> lsc = new ArrayList<>();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT bom.child FROM part JOIN bom ON part.part_id = bom.parent WHERE bom.parent = ?")) {
            s.setString(1, partID);
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    lsc.add(r.getString(1));
                }
                return lsc;
            }
        }
    }

}







