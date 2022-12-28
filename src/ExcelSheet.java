import javax.jnlp.ClipboardService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelSheet {

    public static List<Test> listOfAllTests = new ArrayList<>();

    public ExcelSheet(List<Test> tests) {
        listOfAllTests = tests;
    }

    public static List<TestWrapper> generateTestWrappersForAllTest() {
        List<TestWrapper> testWrapperList = new ArrayList<>();
        listOfAllTests.forEach(test -> {
            test.getTest_results().forEach(testResult -> {
                testWrapperList.add(new TestWrapper(test.getDate(), test.getAA(), test.getDocument_nr(), test.getCustomer_nr(),
                        testResult.getTest_type(), testResult.getTest_result(), testResult.getSoll(), testResult.getSoll_plus(), testResult.getSoll_minus()));
            });
        });
        return testWrapperList;
    }

    public static List<Test> getListOfAllTests() {
        return listOfAllTests;
    }

    public static void setListOfAllTests(List<Test> listOfAllTests) {
        ExcelSheet.listOfAllTests = listOfAllTests;
    }

    public static void parseExcelFile(String path) throws IOException {
        DVPParser dvp = new DVPParser();
        dvp.readXLSXFile(path);
        listOfAllTests = dvp.getTests();
    }



    public static void main(String[] args) throws IOException {
        parseExcelFile("src\\excely\\DVP_template_empty.xlsx");
        generateTestWrappersForAllTest().forEach(testWrapper -> {
            System.out.println(testWrapper.toString().replace('\n', ' '));;
        });

    }
}
