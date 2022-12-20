import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Test {
    private String date = "";
    private String AA = "";
    private String Document_nr = "";
    private String Customer_nr = "";
    private List<TestResult> test_results = new ArrayList<>();
    private int date_col_idx = 0;
    private int AA_col_idx = 1;
    private int documentnr_col_idx = 2;
    private int customernr_col_idx = 3;

    private List<List<XSSFCell>> all_cell;

    public String getDate() {
        return date;
    }

    public String getAA() {
        return AA;
    }

    public String getDocument_nr() {
        return Document_nr;
    }

    public String getCustomer_nr() {
        return Customer_nr;
    }

    public List<TestResult> getTest_results() {
        return test_results;
    }

    public Test(List<List<XSSFCell>> all_cell0, int row) {
        all_cell = all_cell0;

        date = get_string_value_from_cell(get_cell(row, date_col_idx));
        AA = get_string_value_from_cell(get_cell(row, AA_col_idx));
        Document_nr = get_string_value_from_cell(get_cell(row, documentnr_col_idx));
        Customer_nr = get_string_value_from_cell(get_cell(row, customernr_col_idx));


        for(XSSFCell cell: all_cell.get(row)){
            if(cell.getCellType() != CellType.BLANK && cell.getColumnIndex()>3){
                TestResult testResult = new TestResult(all_cell, cell);
                if(!testResult.getTest_result().equals("")) {
                    test_results.add(testResult);
                }
            }
        }
    }

    private String get_string_value_from_cell(XSSFCell cell) {
        String string_value = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case BOOLEAN:
                    string_value = String.valueOf(cell.getBooleanCellValue());
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date javaDate = DateUtil.getJavaDate(cell.getNumericCellValue());
                        string_value = new SimpleDateFormat("MM.dd.yyyy").format(javaDate);
                    } else string_value = String.valueOf(cell.getNumericCellValue());
                    break;
                case STRING:
                    string_value = cell.getStringCellValue();
                    break;
            }
        }
        return string_value;
    }

    XSSFCell get_cell(int row_idx, int col_idx){
        for (List <XSSFCell> cells_row: all_cell){
            for (XSSFCell cell: cells_row){
                if(cell.getRowIndex() == row_idx && cell.getColumnIndex() == col_idx){
                    return cell;
                }
            }
        }
        return null;
    }

    public String to_string() {
        String return_str =  "\n\n  Test\n\n" +
                "date = " + date + "\n" +
                "AA = " + AA + "\n" +
                "Document_nr = " + Document_nr + "\n" +
                "Customer_nr = " + Customer_nr + "\n\n";
        for (TestResult res : test_results){
            return_str+=res.to_string();
        }
        return return_str;
    }
}

