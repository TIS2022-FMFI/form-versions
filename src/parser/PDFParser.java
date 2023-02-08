package parser;

import catia.CatiaSheet;
import javafx.scene.control.Alert;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class for parsing a PDF file
 *
 * @author Lukas Kostrian
 * @version 1.0
 */
public class PDFParser {

    public static CatiaSheet parseFile(String path) throws Exception {
        try {
            PDDocument document = PDDocument.load(new File(path));
            PDFTextStripper stripper = new PDFTextStripper();
            String str = stripper.getText(document);
            Scanner scnLine = new Scanner(str);
            String line;
            List<String> allLines = new ArrayList<>();

            while (scnLine.hasNextLine()) {
                line = scnLine.nextLine();
                allLines.add(line);
            }

            document.close();
            return new CatiaSheet(allLines);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error while parsing PDF file");
            alert.showAndWait();
            throw new Exception("FileError");
        }
    }

}
