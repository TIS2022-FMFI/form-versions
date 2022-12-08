import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CatiaSheet {
    public List<CatiaComment> header = new ArrayList<>();
    public String material = "";
    public String surfaceProtection = "";
    public String calculatedMass = "";
    public String materialNo = "";
    public String version2 = "";
    public List<String> toleranceData = new ArrayList<>();
    public String externalDocumentNo = "";
    public String technInfo = "";
    public String scale = "";
    public String generated = "";
    public String responsible = "";
    public String developedFromDocument = "";
    public String size = "";
    public String designation = "";
    public String designation2 = "";
    public String sheet = "";
    public String of = "";
    public String documentNo = "";
    public String version = "";
    public List<BOM> items = new ArrayList<>();

    public CatiaSheet(List<String> lines) {
        int index = lines.indexOf("Toleranzenangaben / Tolerances data");
        if (index > 0 && lines.get(index + 1).equals("Erstellt")) {
            index -= 1;
            toleranceData.add(lines.get(index).substring(lines.get(index).indexOf("ISO ")));
            index += 16;
            toleranceData.add(lines.get(index).substring(lines.get(index).indexOf("ISO ")));
            index += 15;
            designation = lines.get(index++);
            generated = lines.get(index++);
            version = String.valueOf(lines.get(index).charAt(0));
            documentNo = lines.get(index++).substring(1);
            size = lines.get(index++);
            developedFromDocument = lines.get(index); //neviem co tu ma byt asi  Developed from document
        }

        index = lines.indexOf("Freigegeben");
        if (lines.get(index + 1).equals("Released")) {
            index += 2;
            List<String> line = new ArrayList<>(Arrays.asList(lines.get(index).split("\\s+")));
            char headVersion = 'A';
            while (line.get(0).toCharArray().length == 1 && line.get(0).toCharArray()[0] == headVersion) {
                while (!line.get(line.size() - 2).matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
                    index++;
                    line.addAll(Arrays.asList(lines.get(index).split("\\s+")));
                }
                header.add(new CatiaComment(line));
                index++;
                headVersion++;
                line = new ArrayList<>(Arrays.asList(lines.get(index).split("\\s+")));
            }
        }

        responsible = header.get(0).released;
        index = 0;
        while (lines.get(index).indexOf(responsible) != 0) {
            index++;
        }
        toleranceData.add(lines.get(index - 1));//.substring(lines.get(index - 1).indexOf("ISO ")));
        toleranceData.add(lines.get(index - 2));//.substring(lines.get(index - 2).indexOf("ISO ")));
        responsible = lines.get(index++);
        designation2 = lines.get(index++);
        scale = lines.get(index++);
        sheet = lines.get(index++);
        of = lines.get(index++);
        externalDocumentNo = lines.get(index++); //asi to je toto ale nwm
        technInfo = lines.get(index++); //nie je iste
        calculatedMass = lines.get(index++);
        material = lines.get(index++);
        materialNo = lines.get(index++); //nie je iste
        surfaceProtection = lines.get(index);

        index = 0;
        while (index < lines.size() - 1 && !lines.get(index).equals("Item") && !lines.get(index + 1).equals("Benennung")) {
            index++;
        }
        if (index < lines.size() - 1) {
            index -= 2;
            List<String> line = new ArrayList<>(Arrays.asList(lines.get(index).split("\\s+")));
            while (line.get(line.size() - 2).matches("[0-9]+\\.[0-9]+") && line.get(line.size() - 1).equals("g") || line.get(line.size() - 1).matches("[0-9]+\\.[0-9]+g")) {
                while (!line.get(0).matches("[0-9]+")) {
                    index--;
                    List<String> newLine = new ArrayList<>();
                    newLine.addAll(Arrays.asList(lines.get(index).split("\\s+")));
                    newLine.addAll(line);
                    line = new ArrayList<>(newLine);
                }
                items.add(new BOM(line));
                index--;
                line = new ArrayList<>(Arrays.asList(lines.get(index).split("\\s+")));
            }
        }
    }

    public void print() {
        System.out.print("Header: ");
        header.forEach(CatiaComment::print);
        System.out.print("Version: ");
        System.out.println(version);
        System.out.print("Document-no.: ");
        System.out.println(documentNo);
        System.out.print("Designation: ");
        System.out.println(designation);
        System.out.println(designation2);
        System.out.print("Tolerances data: ");
        System.out.println(toleranceData);
        System.out.print("Generated: ");
        System.out.println(generated);
        System.out.print("Responsible: ");
        System.out.println(responsible);
        System.out.print("Size: ");
        System.out.println(size);
        System.out.print("Developed from document: ");
        System.out.println(developedFromDocument);
        System.out.print("Scale: ");
        System.out.println(scale);
        System.out.print("Sheet: ");
        System.out.println(sheet);
        System.out.print("Of: ");
        System.out.println(of);
        System.out.print("External document-no.: ");
        System.out.println(externalDocumentNo);
        System.out.print("Techn. Info: ");
        System.out.println(technInfo);
        System.out.print("Calculated mass: ");
        System.out.println(calculatedMass);
        System.out.print("Material: ");
        System.out.println(material);
        System.out.print("Material-no.: ");
        System.out.println(materialNo);
        System.out.print("Surface protection: ");
        System.out.println(surfaceProtection);
        if (items.size() > 0) {
            System.out.println();
            System.out.println("Items:");
            items.forEach(BOM::print);
        }
    }
}
