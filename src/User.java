import javafx.scene.control.TextInputDialog;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class User {

    private String name;

    public User(){
        try {
            name = nameOfUser();
            if (name.equals("")) {
                String userId = identifyYourself();
                registerNewUser(userId);
                name = userId;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String nameOfUser() throws IOException {
        FileInputStream fis = new FileInputStream("src\\userid\\userlogin");
        return IOUtils.toString(fis, StandardCharsets.UTF_8);
    }

    public String identifyYourself(){
        TextInputDialog TID = new TextInputDialog();
        TID.setHeaderText("Please identify yourself");
        TID.showAndWait();

        return TID.getEditor().getText();
    }

    public void registerNewUser(String name) throws IOException {
        Path path = Paths.get("src\\userid\\userlogin");
        byte[] strToBytes = name.getBytes();

        Files.write(path, strToBytes);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
