import org.apache.poi.ss.usermodel.DateUtil;
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
    private XSSFSheet sheet;
    private List<CellRangeAddress> mergedRegions;
    private List<List<XSSFCell>> all_cell = new ArrayList<>();
    private int first_row = 8;
    List<Test> tests = new ArrayList<>();

    public DVPParser() {
    }


    public void add_cell(XSSFCell cell){
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

    private XSSFCell copyCell(XSSFCell srcCell, XSSFCell cell) {
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


    public void readXLSXFile(String path) throws IOException{
        File f = new File(path);
        if(f.exists() && !f.isDirectory()) {
            InputStream ExcelFileToRead = new FileInputStream(path);
            XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);


            if (wb.getSheet("DVP internal") != null) {
                sheet = wb.getSheet("DVP internal");
            } else if (wb.getSheet("DVP") != null) {
                sheet = wb.getSheet("DVP");
            }
            else{
                System.out.println("Nena??iel sa sheet s n??zvom DVP alebo DVP Internal");
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

    public void createTestObjects(){
        for(int row = first_row; row<all_cell.size(); row++){
            Test test = new Test(all_cell, row);
            if(test.getTest_results().size() > 0) {
                tests.add(test);
            }
        }
    }

    public void print_all_tests(){
        System.out.println(tests);
        for (Test test: tests){
            System.out.println(test.to_string());
        }
    }


}
