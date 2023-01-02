//import javax.jnlp.ClipboardService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelSheet {

    public static List<Test> listOfAllTests = new ArrayList<>();

    public ExcelSheet() {
    }

    public List<TestWrapper> generateTestWrappersForAllTest() {
        List<TestWrapper> testWrapperList = new ArrayList<>();
        listOfAllTests.forEach(test -> {
            test.getTest_results().forEach(testResult -> {
                testWrapperList.add(new TestWrapper(test.getDate(), test.getAA(), test.getDocument_nr(), test.getCustomer_nr(),
                        testResult.getTest_type(), testResult.getTest_result(), testResult.getSoll(), testResult.getSoll_plus(), testResult.getSoll_minus()));
            });
        });
        return testWrapperList;
    }

    public List<Test> getListOfAllTests() {
        return listOfAllTests;
    }

    public void setListOfAllTests(List<Test> listOfAllTests) {
        ExcelSheet.listOfAllTests = listOfAllTests;
    }

    public void parseExcelFile(String path) throws IOException {
        DVPParser dvp = new DVPParser();
        dvp.readXLSXFile(path);
        listOfAllTests = dvp.getTests();
    }





}
