import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
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
        if (index >= 0 && lines.get(index + 1).equals("Erstellt")) {
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
        if (index >= 0 && lines.get(index + 1).equals("Released")) {
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

//        responsible = header.get(0).released;
//        index = 0;
//        while (lines.get(index).indexOf(responsible) != 0) {
//            index++;
//        }
//        toleranceData.add(lines.get(index - 1));//.substring(lines.get(index - 1).indexOf("ISO ")));
//        toleranceData.add(lines.get(index - 2));//.substring(lines.get(index - 2).indexOf("ISO ")));
//        responsible = lines.get(index++);
//        designation2 = lines.get(index++);
//        scale = lines.get(index++);
//        sheet = lines.get(index++);
//        of = lines.get(index++);
//        externalDocumentNo = lines.get(index++); //asi to je toto ale nwm
//        technInfo = lines.get(index++); //nie je iste
//        calculatedMass = lines.get(index++);
//        material = lines.get(index++);
//        materialNo = lines.get(index++); //nie je iste
//        surfaceProtection = lines.get(index);

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

    //TODO ked bude gui, tak pridat nahravanie obrazku
    public void insertIntoPart() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO part (part_id, type, date, comment, image) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, this.documentNo + this.version);
            s.setString(2, getType());
            s.setDate(3, parseDate(this.header.get(header.size()-1).releaseDate));
            s.setString(4, this.header.get(header.size()-1).changes);
            File img = new File("src/img.png");
            s.setBlob(5, new FileInputStream(img));
            s.executeUpdate();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void insertIntoBom(String bomid) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO bom (part_id, bom_id) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, this.documentNo);
            s.setString(2, bomid);
            s.executeUpdate();
        }
    }

    public Date parseDate(String date) {
        System.out.println(date.split("-")[0]+"-"+date.split("-")[2]+"-"+date.split("-")[1]);
        return Date.valueOf(date.split("-")[0]+"-"+date.split("-")[2]+"-"+date.split("-")[1]);
    }

    public String getType() {
        switch (documentNo.split("\\.")[0]) {
            case "173": return "calculation";
            case "273": return "protoype";
            case "735": return "serial";
        }
        return "null";
    }

    public List<CatiaComment> getHeader() {
        return header;
    }

    public void setHeader(List<CatiaComment> header) {
        this.header = header;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSurfaceProtection() {
        return surfaceProtection;
    }

    public void setSurfaceProtection(String surfaceProtection) {
        this.surfaceProtection = surfaceProtection;
    }

    public String getCalculatedMass() {
        return calculatedMass;
    }

    public void setCalculatedMass(String calculatedMass) {
        this.calculatedMass = calculatedMass;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getVersion2() {
        return version2;
    }

    public void setVersion2(String version2) {
        this.version2 = version2;
    }

    public List<String> getToleranceData() {
        return toleranceData;
    }

    public void setToleranceData(List<String> toleranceData) {
        this.toleranceData = toleranceData;
    }

    public String getExternalDocumentNo() {
        return externalDocumentNo;
    }

    public void setExternalDocumentNo(String externalDocumentNo) {
        this.externalDocumentNo = externalDocumentNo;
    }

    public String getTechnInfo() {
        return technInfo;
    }

    public void setTechnInfo(String technInfo) {
        this.technInfo = technInfo;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getGenerated() {
        return generated;
    }

    public void setGenerated(String generated) {
        this.generated = generated;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getDevelopedFromDocument() {
        return developedFromDocument;
    }

    public void setDevelopedFromDocument(String developedFromDocument) {
        this.developedFromDocument = developedFromDocument;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDesignation2() {
        return designation2;
    }

    public void setDesignation2(String designation2) {
        this.designation2 = designation2;
    }

    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public String getOf() {
        return of;
    }

    public void setOf(String of) {
        this.of = of;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<BOM> getItems() {
        return items;
    }

    public void setItems(List<BOM> items) {
        this.items = items;
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
