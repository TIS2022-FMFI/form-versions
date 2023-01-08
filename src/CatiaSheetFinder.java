import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
                            SwingFXUtils.toFXImage(ImageIO.read(r.getBlob(6).getBinaryStream()), null),
                            r.getString(8),
                            r.getString(7)
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
        Image img = null;
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM part WHERE part_id = ?")) {
            s.setString(1, id);
            try (ResultSet r = s.executeQuery()) {
                List<CatiaSheet> elements = new ArrayList<>();
                while (r.next()) {
                    if (r.getBlob(6) != null) {
                        img = SwingFXUtils.toFXImage(ImageIO.read(r.getBlob(6).getBinaryStream()), null);
                    }
                    CatiaSheet h = new CatiaSheet(
                            r.getString(2).substring(0, r.getString(2).length() - 1),
                            r.getString(2).substring(r.getString(2).length() - 1),
                            r.getString(4),
                            lsc,
                            img,
                            r.getString(8),
                            r.getString(7)
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

    public List<CatiaSheet> findHistoryForPart(String partID) throws SQLException {
        List<CatiaComment> lsc = new ArrayList<>();
        Image img = null;
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM part WHERE part_id REGEXP ? OR developed_from REGEXP ?")) {
            String rgx = "'[0-9]{3}[.][0-9]" + partID.split("\\.")[1].substring(1) + "[.]" + partID.split("\\.")[2] + "[.]" + partID.split("\\.")[3].substring(0,3) + "[A-Z]'";
            s.setString(1, rgx);
            s.setString(2, rgx);
            try (ResultSet r = s.executeQuery()) {
                List<CatiaSheet> elements = new ArrayList<>();
                while (r.next()) {
                    if (r.getBlob(6) != null) {
                        img = SwingFXUtils.toFXImage(ImageIO.read(r.getBlob(6).getBinaryStream()), null);
                    }
                    CatiaSheet h = new CatiaSheet(
                            r.getString(2).substring(0, r.getString(2).length() - 1),
                            r.getString(2).substring(r.getString(2).length() - 1),
                            r.getString(4),
                            lsc,
                            img,
                            r.getString(8),
                            r.getString(7)
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

    public List<String> findBomFOrPart(String partID) throws SQLException {
        List<String> lsc = new ArrayList<>();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT bom.child FROM part\n JOIN bom ON part.part_id = bom.parent\n WHERE bom.parent = ?")) {
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







