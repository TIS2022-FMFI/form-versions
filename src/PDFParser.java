import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class PDFParser {
    public static CatiaSheet parseFile(String path) throws Exception {
        try {
            PDDocument document = PDDocument.load(new File(path));
            PDFTextStripper stripper = new PDFTextStripper();
            String str = stripper.getText(document);
            Scanner scnLine = new Scanner(str);
            String line;
            //int r = 0;
            List<String> allLines = new ArrayList<>();
            while (scnLine.hasNextLine()) {
                line = scnLine.nextLine();
                allLines.add(line);
                //r++;
            }
            //System.out.println(allLines);
            //System.out.println();
            //System.out.println("riadkov bolo "+r);
            document.close();
            return new CatiaSheet(allLines);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new Exception("FileError");
    }

    public static void main(String[] args) throws Exception {
        String path = "src/cat2.pdf";
        CatiaSheet cs = parseFile(path);

        cs.print();

    }
}
