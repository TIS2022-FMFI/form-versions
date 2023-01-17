import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Template {
    String template_name = "";
    List<String> result_names = new ArrayList<>();
    List<Integer> row_ids = new ArrayList<>();
    List<Integer> col_ids = new ArrayList<>();
    List<Integer> sheet_ids = new ArrayList<>();
    String path_to_excel = "";
    int databaseId;
    XSSFWorkbook emptyTable;


    public Template() {
    }

    public Template(String path_to_excel, String template_name, List<String> result_names, List<Integer> row_ids, List<Integer> col_ids, List<Integer> sheet_ids) {
        this.path_to_excel = path_to_excel;
        this.template_name = template_name;
        this.result_names = result_names;
        this.row_ids = row_ids;
        this.col_ids = col_ids;
        this.sheet_ids = sheet_ids;
    }

    public Template(String name, int dbId, XSSFWorkbook table) {
        this.template_name = name;
        this.databaseId = dbId;
        this.emptyTable = table;
    }

    public Template(String path_to_excel, TextField templateMenu, List<ChoiceBox> results, List<TextField> rows, List<TextField> cols, List<TextField> sheets) {
        this.path_to_excel = path_to_excel;
        this.template_name = templateMenu.getText().toString().trim();

        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getValue() != null) {
                String result_name = results.get(i).getValue().toString();
                String row_id = rows.get(i).getText().replace(" ", "");
                String col_id = cols.get(i).getText().replace(" ", "");
                String sheet_id = sheets.get(i).getText().replace(" ", "");
                if (validInput(row_id, col_id, sheet_id)) {
                    result_names.add(result_name);
                    row_ids.add(Integer.parseInt(row_id));
                    if (isNumeric(col_id)) {
                        col_ids.add(Integer.parseInt(col_id));
                    } else {
                        col_ids.add(letterToNumber(col_id));
                    }
                    sheet_ids.add(Integer.parseInt(sheet_id));
                }
            }
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer num = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public Boolean validInput(String row_id, String col_id, String sheet_id) {
        boolean is_valid = true;

        if (row_id.replace(" ", "").equals("")) { return false;}

        try { Integer.parseInt(row_id);
        } catch (NumberFormatException nfe) { return false; }

        if (col_id.replace(" ", "").equals("")) { return false; }

        try {Integer.parseInt(col_id);
        } catch (NumberFormatException nfe) {
            Integer length = col_id.length();
            for (int i = 0; i < length; i++) {
                if (col_id.charAt(i) < 65 || (col_id.charAt(i) > 90 && col_id.charAt(i) < 97) || col_id.charAt(i) > 122) {
                    return false;
                }
            }
        }

        if (sheet_id.replace(" ", "").equals("")) { return false; }

        try { Integer.parseInt(sheet_id);
        } catch (NumberFormatException nfe) { return false; }

        return is_valid;
    }

    public Integer letterToNumber(String letter) {

        double column = 0;
        Integer length = letter.length();
        for (int i = 0; i < length; i++) {
            column += (letter.toUpperCase().charAt(i) - 64) * Math.pow(26, length - i - 1);
        }
        return (int) column;
    }

    private String findResultFromWrapperList(List<TestWrapper> testWrapperList, String name) {
        for (TestWrapper tw : testWrapperList) {
            if (tw.getTestType().equalsIgnoreCase(name)) {
                return tw.getTestResult();
            }
        }
        return "";
    }

    public boolean checkIfListHasAllTests(List<TestWrapper> testWrapperList) {
        if (result_names.size() > testWrapperList.size()) return false;
        for (String rn : result_names) {
            if (testWrapperList.stream()
                    .map(TestWrapper::getTestType)
                    .filter(testType -> testType
                            .equals(rn))
                    .count() != 1) {
                return false;
            }
        }
        return true;
    }

    public void export(String path, List<TestWrapper> testWrapperList) {

        try {
            FileOutputStream out = new FileOutputStream(path);
            emptyTable.write(out);
            File xlsxFile = new File(path);
            FileInputStream inputStream = new FileInputStream(xlsxFile);
            for (int i = 0; i < result_names.size(); i++) {
                Sheet sheet = emptyTable.getSheetAt(sheet_ids.get(i) - 1);
                Row r = sheet.getRow(row_ids.get(i) - 1);
                try {
                    Cell c = r.getCell(col_ids.get(i) - 1);
                    c.setCellValue(findResultFromWrapperList(testWrapperList, result_names.get(i)));
                } catch (NullPointerException e) {
                    try {
                        Cell c = r.createCell(col_ids.get(i) - 1);
                        c.setCellValue(findResultFromWrapperList(testWrapperList, result_names.get(i)));
                    } catch (NullPointerException e2) {
                        Row r2 = sheet.createRow(row_ids.get(i) - 1);
                        Cell c = r2.createCell(col_ids.get(i) - 1);
                        c.setCellValue(findResultFromWrapperList(testWrapperList, result_names.get(i)));
                    }
                }
            }

            inputStream.close();
            FileOutputStream os = new FileOutputStream(xlsxFile);
            emptyTable.write(os);
            emptyTable.close();
            os.close();
            System.out.println("Excel file has been updated successfully.");

        } catch (EncryptedDocumentException | IOException e) {
            System.err.println("Exception while updating an existing excel file.");
        }
    }

    public void insert() throws SQLException {

        if (!TemplateFinder.getInstance().exists(template_name)) {

            DatabaseChange dc = new DatabaseChange(User.getName(), "Uploaded a template named " + template_name, new Timestamp(System.currentTimeMillis()));
            dc.insert();

            int databaseId;

            try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO template (name, excel) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
                s.setString(1, this.template_name);

                XSSFWorkbook workbook = new XSSFWorkbook(Files.newInputStream(Paths.get(path_to_excel)));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                workbook.write(baos);
                s.setBytes(2, baos.toByteArray());

                s.executeUpdate();

                try (ResultSet generatedKeys = s.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        databaseId = Math.toIntExact(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (int i = 0; i < result_names.size(); i++) {
                try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO coordinates (table_id, row, col, sheet, test_type) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                    s.setInt(1, databaseId);
                    s.setInt(2, row_ids.get(i));
                    s.setInt(3, col_ids.get(i));
                    s.setInt(4, sheet_ids.get(i));
                    if (TestTypeFinder.getInstance().returnIdInTable(result_names.get(i)) == -1) {
                        throw new SQLException("Wrong test name, nothing inserted");
                    }
                    s.setInt(5, TestTypeFinder.getInstance().returnIdInTable(result_names.get(i)));
                    s.executeUpdate();
                }
            }
        }


    }

    public void delete() throws SQLException {
        DatabaseChange dc = new DatabaseChange(User.getName(), "Deleted a template named " + template_name, new Timestamp(System.currentTimeMillis()));
        dc.insert();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM template WHERE name = ? ")) {
            s.setString(1, template_name);
            s.executeUpdate();
        }
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

}
