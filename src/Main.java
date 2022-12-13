import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/form_versions", "root", "root");
            System.out.println("Success");

            if (connection != null) {
                DbContext.setConnection(connection);

                CatiaSheet cs = PDFParser.parseFile("src/cat2.pdf");


            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
