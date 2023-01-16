import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TestResult {

    private String test_type = "";
    private String test_result = "";
    private String soll = "";
    private String soll_plus = "";
    private String soll_minus = "";
    private int dbid;
    private int parentTestId;
    private List<List<XSSFCell>> all_cell;
    private int soll_row_idx = 4;
    private int test_name_row_idx = 1;
    private int first_col_of_soll = 10;
    private int col;


    public TestResult(List<List<XSSFCell>> all_cell0, XSSFCell cell0) {
        all_cell = all_cell0;
        col = cell0.getColumnIndex();
        test_type = create_test_type_name().replace('\n', '\0');
        test_result = get_string_value_from_cell(cell0);
        soll = get_string_value_from_cell(get_cell(soll_row_idx, col));
        soll_plus = get_string_value_from_cell(get_cell(soll_row_idx+1, col));
        soll_minus = get_string_value_from_cell(get_cell(soll_row_idx+2, col));
        if( col<first_col_of_soll){
            soll = "";
            soll_plus = "";
            soll_minus = "";
        }
    }

    public TestResult() {}

    public String getTest_type() {
        return test_type;
    }

    public String getTest_result() {
        return test_result;
    }

    public String getSoll() {
        return soll;
    }

    public String getSoll_plus() {
        return soll_plus;
    }

    public String getSoll_minus() {
        return soll_minus;
    }

    public List<List<XSSFCell>> getAll_cell() {
        return all_cell;
    }

    private String create_test_type_name(){
        XSSFCell test_name_cell1 = get_cell(test_name_row_idx, col);
        XSSFCell test_name_cell2 = get_cell(test_name_row_idx+1, col);
        XSSFCell test_name_cell3 = get_cell(test_name_row_idx+2, col);

        String test_type_name1 = get_string_value_from_cell(test_name_cell1);
        String test_type_name2 = get_string_value_from_cell(test_name_cell2);
        String test_type_name3 = get_string_value_from_cell(test_name_cell3);

        String test_type_name = "";
        test_type_name = test_type_name1;
        if (!test_type_name1.equals(test_type_name2) && !test_type_name2.equals("")){
            if(!test_type_name.equals("")){
                test_type_name += " : ";
            }
            test_type_name += test_type_name2;
        }
        if (!test_type_name2.equals(test_type_name3) && !test_type_name3.equals("")){
            if(!test_type_name.equals("")){
                test_type_name += " : ";
            }
            test_type_name += test_type_name3;
        }

        if(col>=4 && col <= 8){
            XSSFCell test_name_cell4 = get_cell(test_name_row_idx+3, col);
            String test_type_name4 = get_string_value_from_cell(test_name_cell4);
            if(!test_type_name4.equals(test_type_name1) && !test_type_name4.equals(test_type_name2) && !test_type_name4.equals(test_type_name3)
                    && !test_name_cell4.equals("")){

                if(!test_type_name.equals("")){
                    test_type_name += " : ";
                }
                test_type_name += test_type_name4;
            }
        }
        return test_type_name;
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
        String res_str ="   Test results \n" +
                "test_type = " + test_type + "\n" +
                "test_result = " + test_result + "\n" +
                "soll = " + soll + "\n" +
                "soll_plus = " + soll_plus + "\n" +
                "soll_minus = " + soll_minus + "\n" +
                "\n";
        return res_str;
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

    public TestResult(String test_type, String test_result, String soll, String soll_plus, String soll_minus, int dbd, int prntid) {
        this.test_type = test_type;
        this.test_result = test_result;
        this.soll = soll;
        this.soll_plus = soll_plus;
        this.soll_minus = soll_minus;
        this.dbid = dbd;
        this.parentTestId = prntid;
    }

    public int getDbid() {
        return dbid;
    }

    public void setDbid(int dbid) {
        this.dbid = dbid;
    }

    public void setTest_result(String test_result) {
        this.test_result = test_result;
    }

    public void setSoll(String soll) {
        this.soll = soll;
    }

    public void setSoll_plus(String soll_plus) {
        this.soll_plus = soll_plus;
    }

    public void setSoll_minus(String soll_minus) {
        this.soll_minus = soll_minus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResult that = (TestResult) o;
        return test_type.equals(that.test_type) &&
                test_result.equals(that.test_result) &&
                soll.equals(that.soll) &&
                soll_plus.equals(that.soll_plus) &&
                soll_minus.equals(that.soll_minus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(test_type, test_result, soll, soll_plus, soll_minus);
    }
}
