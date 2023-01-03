import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class Template {
    String template_name = "";
    List<String> result_names = new ArrayList<>();
    List<Integer> row_ids = new ArrayList<>();
    List<Integer> col_ids = new ArrayList<>();
    List<Integer> sheet_ids = new ArrayList<>();
    String path_to_excel = "";

    public Template(String path_to_excel, String template_name, List<String> result_names, List<Integer> row_ids, List<Integer> col_ids, List<Integer> sheet_ids) {
        this.path_to_excel = path_to_excel;
        this.template_name = template_name;
        this.result_names = result_names;
        this.row_ids = row_ids;
        this.col_ids = col_ids;
        this.sheet_ids = sheet_ids;
    }

    public Template(String path_to_excel, TextField templateMenu, List<ChoiceBox> results, List<TextField> rows, List<TextField> cols, List<TextField> sheets) {
        this.path_to_excel = path_to_excel;
        this.template_name = templateMenu.getText().toString().trim();

        for(int i = 0; i<results.size(); i++) {
            if (results.get(i).getValue() != null) {
                String result_name = results.get(i).getValue().toString().replace(" ", "");
                String row_id = rows.get(i).getText().replace(" ", "");
                String col_id = cols.get(i).getText().replace(" ", "");
                String sheet_id = sheets.get(i).getText().replace(" ", "");
                if (valid_input(row_id,col_id,sheet_id)) {
                    result_names.add(result_name);
                    row_ids.add(Integer.parseInt(row_id));
                    if(isNumeric(col_id)) {
                        col_ids.add(Integer.parseInt(col_id));
                    }
                    else {
                        col_ids.add(letter_to_number(col_id));
                    }
                    sheet_ids.add(Integer.parseInt(sheet_id));
                }
            }
        }
    }

    public Boolean valid_input(String row_id, String col_id, String sheet_id){
        boolean is_valid = true;

        if(row_id.replace(" ", "").equals("")){
            return false;
        }
        try{
            Integer.parseInt(row_id);
        } catch (NumberFormatException nfe) {
            return false;
        }
        if(col_id.replace(" ", "").equals("")){
            return false;
        }
        try{
            Integer.parseInt(col_id);
        } catch (NumberFormatException nfe) {
            Integer length = col_id.length();
            for (int i = 0; i < length; i++){
                if(col_id.charAt(i)<65 || (col_id.charAt(i)>90 && col_id.charAt(i)<97) || col_id.charAt(i)>122){
                    return false;
                }
            }
        }

        if(sheet_id.replace(" ", "").equals("")){
            return false;
        }

        try{
            Integer.parseInt(sheet_id);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return is_valid;
    }

    public Integer letter_to_number(String letter){

        double column = 0;
        Integer length = letter.length();
        for (int i = 0; i < length; i++){
            column += (letter.toUpperCase().charAt(i) - 64) * Math.pow(26, length - i - 1);
        }
        return (int)column;
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

    @Override
    public String toString() {

        String str = "Template: \n";
        str+=template_name;
        for(int i = 0; i<result_names.size(); i++) {
            str += " " + result_names.get(i) + " " + row_ids.get(i) + " " + col_ids.get(i) + " " + sheet_ids.get(i) + "\n";

        }
        return str;
    }

    private String findResultFromWrapperList(List<TestWrapper> testWrapperList, String name) {
        for (TestWrapper tw : testWrapperList) {
            if (tw.getTestType().equalsIgnoreCase(name)) {
                return tw.getTestResult();
            }
        }
        return "";
    }

    public void export(String path, String partID) throws Exception {
        try {
            File xlsxFile = new File(path);
            FileInputStream inputStream = new FileInputStream(xlsxFile);
            Workbook workbook = WorkbookFactory.create(inputStream);

            List<TestWrapper> testWrapperList = new ArrayList<>();      //TODO metoda, ktora mu aj da prvky a pouziva partID
            TestWrapper tw1 = new TestWrapper("","","","","one","jeden","","","");
            TestWrapper tw2 = new TestWrapper("","","","","two","dva","","","");
            testWrapperList.add(tw1);
            testWrapperList.add(tw2);

            for (int i = 0; i < result_names.size(); i++) {
                Sheet sheet = workbook.getSheetAt(sheet_ids.get(i));
                Row r = sheet.getRow(row_ids.get(i));
                try {
                    Cell c = r.getCell(col_ids.get(i));
                    c.setCellValue(findResultFromWrapperList(testWrapperList,result_names.get(i)));
                } catch (NullPointerException e) {
                    try {
                        Cell c = r.createCell(col_ids.get(i));
                        c.setCellValue(findResultFromWrapperList(testWrapperList,result_names.get(i)));
                    } catch (NullPointerException e2) {
                        Row r2 = sheet.createRow(row_ids.get(i));
                        Cell c = r2.createCell(col_ids.get(i));
                        c.setCellValue(findResultFromWrapperList(testWrapperList,result_names.get(i)));
                    }
                }
            }

            inputStream.close();
            FileOutputStream os = new FileOutputStream(xlsxFile);
            workbook.write(os);
            workbook.close();
            os.close();

            System.out.println("Excel file has been updated successfully.");

        } catch (EncryptedDocumentException | IOException e) {
            System.err.println("Exception while updating an existing excel file.");
            //e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        String path_to_excel = "src/excely/dummy.xlsx";
        String template_name = "dummy";
        List<String> result_names = new ArrayList<>();
        result_names.add("one");
        result_names.add("two");
        List<Integer> row_ids = new ArrayList<>();
        row_ids.add(0);
        row_ids.add(1);
        List<Integer> col_ids = new ArrayList<>();
        col_ids.add(0);
        col_ids.add(1);
        List<Integer> sheet_ids = new ArrayList<>();
        sheet_ids.add(0);
        sheet_ids.add(0);
        Template t = new Template(path_to_excel, template_name,result_names,row_ids,col_ids,sheet_ids);
        t.export("src/excely/dummy.xlsx", "123.456.789A");
    }

}
