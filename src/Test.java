import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
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

    public int databaseId;

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

    public Test(String date, String AA, String document_nr, String customer_nr, List<TestResult> test_results, int dbid) {
        this.date = date;
        this.AA = AA;
        Document_nr = document_nr;
        Customer_nr = customer_nr;
        this.test_results = test_results;
        this.databaseId = dbid;
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

    public void insert(String uid) throws SQLException {

            DatabaseChange dc = new DatabaseChange(uid, "Uploaded a test for " + this.date + " to the database", new Timestamp(System.currentTimeMillis()));
            dc.insert();

            int databaseId;

            try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO test (date, customer_id, aa, part_id) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                s.setString(1, this.date);
                s.setString(2, this.getCustomer_nr());
                s.setString(3, getAA());
                s.setString(4, getDocument_nr());
                s.executeUpdate();

                try (ResultSet generatedKeys = s.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        databaseId = Math.toIntExact(generatedKeys.getLong(1));
                    }
                    else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }

            }


        for (TestResult testResult : test_results) {
            try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO test_result (test_type, test_result, test_soll, test_soll_plus, test_soll_minus, test_id) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                s.setString(2, testResult.getTest_result());
                s.setString(3, testResult.getSoll());
                s.setString(4, testResult.getSoll_plus());
                s.setString(5, testResult.getSoll_minus());
                if (TestTypeFinder.getInstance().returnIdInTable(testResult.getTest_type().replace('\n', ' ')) == -1) {
                    throw new SQLException("Wrong test name (" + testResult.getTest_type().replace('\n', ' ') + "), nothing inserted");
                }
                s.setInt(1, TestTypeFinder.getInstance().returnIdInTable(testResult.getTest_type().replace('\n', ' ')));
                s.setInt(6, databaseId);
                System.out.println(testResult.getSoll());
                s.executeUpdate();
            }
        }

    }

    public void addTestToListOfTests(TestResult test) {
        test_results.add(test);
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

