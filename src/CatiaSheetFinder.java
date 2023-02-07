import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for finding instances of CatiaSheet stored in the database
 *
 * @author Jacob Kristof
 * @version 1.0
 */
public class CatiaSheetFinder {

    private static final CatiaSheetFinder INSTANCE = new CatiaSheetFinder();

    private CatiaSheetFinder() {
    }

    public static CatiaSheetFinder getInstance() {
        return INSTANCE;
    }

    /**
     * Find a CatiaSheet instance with given ID stored in the database and return it as a list of CatiaSheet instances
     *
     * @param id the id of a part we want to search for
     * @return the list of all CatiaSheet as instances of CatiaSheet
     * @throws SQLException the sql exception
     */
    public List<CatiaSheet> findWithId(String id) throws SQLException {
        List<CatiaComment> lsc = new ArrayList<>();
        Image img = null;
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM part WHERE part_id = ?")) {
            s.setString(1, id);
            try (ResultSet r = s.executeQuery()) {
                List<CatiaSheet> elements = new ArrayList<>();
                while (r.next()) {
                    if (r.getBlob(6) != null) {
                        BufferedImage imag = ImageIO.read(r.getBlob(6).getBinaryStream());
                        img = SwingFXUtils.toFXImage(imag, null);
                    }
                    lsc.add(new CatiaComment(r.getString(5)));
                    CatiaSheet h = new CatiaSheet(
                            r.getString(2).substring(0, r.getString(2).length() - 1),
                            r.getString(2).substring(r.getString(2).length() - 1),
                            r.getString(4),
                            r.getString(5),
                            r.getString(4),
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

    /**
     * Find all CatiaSheet instances with given base ID stored in the database and return it as a list of CatiaSheet instances
     *
     * @param partID the base id of parts we want to search for
     * @return the list of all CatiaSheet as instances of CatiaSheet
     * @throws SQLException the sql exception
     */
    public List<CatiaSheet> findHistoryForPart(String partID) throws SQLException {
        Image img = null;
        if (partID.matches("[0-9]{3}[.][0-9]{3}[.][0-9]{3}[.][0-9]{3}") || partID.matches("[0-9]{3}[.][0-9]{3}")) {
            try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM part WHERE part_id REGEXP ? OR developed_from REGEXP ? order by part_id asc")) {
                String rgx = "";
                if (partID.matches("[0-9]{3}[.][0-9]{3}[.][0-9]{3}[.][0-9]{3}")) {
                    rgx = "[0-9]{3}[.][0-9]" + partID.split("\\.")[1].substring(1) + "[.]" + partID.split("\\.")[2] + "[.]" + partID.split("\\.")[3].substring(0, 3);
                }
                if (partID.matches("[0-9]{3}[.][0-9]{3}")) {
                    rgx = "[0-9]{3}[.][0-9]{3}[.]" + partID.split("\\.")[0] + "[.]" + partID.split("\\.")[1].substring(0, 3);
                }
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
                                r.getString(5),
                                r.getString(4),
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
        }
        return new ArrayList<CatiaSheet>();
    }

}







