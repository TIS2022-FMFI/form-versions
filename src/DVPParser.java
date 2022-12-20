import org.apache.poi.ss.format.CellFormatType;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public class DVPParser {
    private static XSSFSheet sheet;
    private static List<CellRangeAddress> mergedRegions;
    private static List<List<XSSFCell>> all_cell = new ArrayList<>();
    private static int first_row = 8;
    static List<Test> tests = new ArrayList<>();


    public static void add_cell(XSSFCell cell){
        boolean added = false;
        for(CellRangeAddress mergedRegion: mergedRegions) {
            if (mergedRegion.isInRange(cell)) {
                if (cell.getRowIndex() != mergedRegion.getFirstRow() || cell.getColumnIndex() != mergedRegion.getFirstColumn()) {
                    XSSFCell src_cell = all_cell.get(mergedRegion.getFirstRow()).get(mergedRegion.getFirstColumn());
                    cell = copyCell(src_cell, cell);
                    all_cell.get(cell.getRowIndex()).add(cell);
                    added = true;

                }
            }
        }
        if (!added) all_cell.get(cell.getRowIndex()).add(cell);
    }

    public List<Test> getTests() {
        return tests;
    }

    private static XSSFCell copyCell(XSSFCell srcCell, XSSFCell cell) {
        if (srcCell!=null) {
            switch (srcCell.getCellType()) {
                case BOOLEAN:
                    cell.setCellValue(srcCell.getBooleanCellValue());
                    break;
                case NUMERIC:
                    cell.setCellValue(srcCell.getNumericCellValue());
                    if (DateUtil.isCellDateFormatted(srcCell)) cell.setCellValue(srcCell.getDateCellValue());
                    break;
                case STRING:
                    cell.setCellValue(srcCell.getStringCellValue());
                    break;

            }
        }
        return cell;
    }


    public static void readXLSXFile(String path) throws IOException{
        File f = new File(path);
        if(f.exists() && !f.isDirectory()) {
            InputStream ExcelFileToRead = new FileInputStream(path);
            XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);


            if (wb.getSheet("DVP internal") != null) {
                sheet = wb.getSheet("DVP internal");
            } else if (wb.getSheet("DVP") != null) {
                sheet = wb.getSheet("DVP");
            }
            if (sheet != null) {
                XSSFRow row;
                XSSFCell cell;

                Iterator rows = sheet.rowIterator();
                //all_cell = new ArrayList<>();
                mergedRegions = sheet.getMergedRegions();

                while (rows.hasNext()) {
                    row = (XSSFRow) rows.next();

                    Iterator cells = row.cellIterator();
                    List<XSSFCell> cell_list = new ArrayList<>();
                    all_cell.add(cell_list);

                    while (cells.hasNext()) {
                        cell = (XSSFCell) cells.next();
                        add_cell(cell);

                    }

                }
                createTestObjects();
            }
        }


    }

    public static void createTestObjects(){
        for(int row = first_row; row<all_cell.size(); row++){
            tests.add(new Test(all_cell, row));
        }
    }

    public static void print_all_tests(){
        for (Test test: tests){
            System.out.println(test.to_string());
        }
    }

    public static void main(String[] args) throws IOException {
            readXLSXFile("src/excely/DVP_template_empty.xlsx");
            print_all_tests();
    }


}
