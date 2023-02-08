package parser;

import dvp.Test;
import javafx.scene.control.Alert;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Parser for Excel files containing tests
 *
 * @author Barbora Vicianova
 * @version 1.0
 */
public class DVPParser {
    List<Test> tests = new ArrayList<>();
    private XSSFSheet sheet;
    private List<CellRangeAddress> mergedRegions;
    private final List<List<XSSFCell>> allCell = new ArrayList<>();
    private final int firstRow = 8;

    public DVPParser() {
    }

    public void addCell(XSSFCell cell) {
        boolean added = false;
        for (CellRangeAddress mergedRegion : mergedRegions) {
            if (mergedRegion.isInRange(cell)) {
                if (cell.getRowIndex() != mergedRegion.getFirstRow() || cell.getColumnIndex() != mergedRegion.getFirstColumn()) {
                    XSSFCell src_cell = allCell.get(mergedRegion.getFirstRow()).get(mergedRegion.getFirstColumn());
                    cell = copyCell(src_cell, cell);
                    allCell.get(cell.getRowIndex()).add(cell);
                    added = true;

                }
            }
        }
        if (!added) allCell.get(cell.getRowIndex()).add(cell);
    }

    public List<Test> getTests() {
        return tests;
    }

    private XSSFCell copyCell(XSSFCell srcCell, XSSFCell cell) {
        if (srcCell != null) {
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


    public void readXLSXFile(String path) throws IOException {
        File f = new File(path);
        if (f.exists() && !f.isDirectory()) {
            InputStream ExcelFileToRead = new FileInputStream(path);
            XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);


            if (wb.getSheet("DVP internal") != null) {
                sheet = wb.getSheet("DVP internal");
            } else if (wb.getSheet("DVP") != null) {
                sheet = wb.getSheet("DVP");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Invalid excel file!");
                alert.showAndWait();
                System.out.println("Nenašiel sa sheet s názvom DVP alebo DVP Internal");
            }
            if (sheet != null) {
                XSSFRow row;
                XSSFCell cell;

                Iterator rows = sheet.rowIterator();
                mergedRegions = sheet.getMergedRegions();

                while (rows.hasNext()) {
                    row = (XSSFRow) rows.next();

                    Iterator cells = row.cellIterator();
                    List<XSSFCell> cell_list = new ArrayList<>();
                    allCell.add(cell_list);

                    while (cells.hasNext()) {
                        cell = (XSSFCell) cells.next();
                        addCell(cell);

                    }
                }
                createTestObjects();
            }
        }
    }

    public void createTestObjects() {
        for (int row = firstRow; row < allCell.size(); row++) {
            Test test = new Test(allCell, row);
            if (test.getTest_results().size() > 0) {
                tests.add(test);
            }
        }
    }


}