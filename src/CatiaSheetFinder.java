import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CatiaSheetFinder {

    private static final CatiaSheetFinder INSTANCE = new CatiaSheetFinder();

    public static CatiaSheetFinder getInstance() {
        return INSTANCE;
    }

    private CatiaSheetFinder() {
    }

    public List<CatiaSheet> findAll() throws SQLException {
        List<CatiaComment> lsc = new ArrayList<>();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM part")) {
            try (ResultSet r = s.executeQuery()) {
                List<CatiaSheet> elements = new ArrayList<>();
                while (r.next()) {
                    CatiaSheet h = new CatiaSheet(
                            r.getString(2).substring(0, r.getString(2).length() - 1),
                            r.getString(2).substring(r.getString(2).length() - 1),
                            r.getDate(4).toString(),
                            lsc,
                            SwingFXUtils.toFXImage(ImageIO.read(r.getBlob(6).getBinaryStream()), null)
                    );
                    elements.add(h);
                }
                return elements;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<CatiaSheet>();
    }

    public List<CatiaSheet> findWithId(String id) throws SQLException {
        List<CatiaComment> lsc = new ArrayList<>();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM part WHERE part_id = ?")) {
            s.setString(1, id);
            try (ResultSet r = s.executeQuery()) {
                List<CatiaSheet> elements = new ArrayList<>();
                while (r.next()) {
                    CatiaSheet h = new CatiaSheet(
                            r.getString(2).substring(0, r.getString(2).length() - 1),
                            r.getString(2).substring(r.getString(2).length() - 1),
                            r.getDate(4).toString(),
                            lsc,
                            SwingFXUtils.toFXImage(ImageIO.read(r.getBlob(6).getBinaryStream()), null)
                    );
                    elements.add(h);
                }
                return elements;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<CatiaSheet>();
    }
}







