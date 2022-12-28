import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BomFinder {

    private static final BomFinder INSTANCE = new BomFinder();

    public static BomFinder getInstance() {
        return INSTANCE;
    }

    private BomFinder() {
    }

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

}







