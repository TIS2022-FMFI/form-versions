import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    private static String name;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        User.name = name;
    }

    public static boolean findUserInDatabaseAndCheckPassword(String password) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM users WHERE mail = ?")) {
            s.setString(1, name);
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    return comparePasswords(password, r.getString(3));
                }
            }
        }
        return false;
    }

    public static boolean comparePasswords(String password, String hash) {
        return getPasswordMD5Hash(password).equals(hash);
    }

    public static String getPasswordMD5Hash(String password) {

        String generatedPassword = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generatedPassword;
    }

}
