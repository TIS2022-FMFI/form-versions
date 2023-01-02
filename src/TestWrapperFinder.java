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

public class TestWrapperFinder {

    private static final TestWrapperFinder INSTANCE = new TestWrapperFinder();

    public static TestWrapperFinder getInstance() {
        return INSTANCE;
    }

    private TestWrapperFinder() {
    }

    public List<TestWrapper> findAll() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM dvp")) {
            try (ResultSet r = s.executeQuery()) {
                List<TestWrapper> elements = new ArrayList<>();
                while (r.next()) {
                    TestWrapper h = new TestWrapper(
                            r.getString(3),
                            r.getString(4),
                            r.getString(2),
                            r.getString(5),
                            r.getString(6),
                            r.getString(7),
                            r.getString(8),
                            r.getString(9),
                            r.getString(10)
                    );
                    elements.add(h);
                }
                return elements;
            }
        }
    }
}