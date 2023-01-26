import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains all tests of one Excel DVP file, with functions to generate TestWrapper instances
 * @author Jacob Kristof
 * @version 1.0
 */
public class ExcelSheet {

    public List<Test> listOfAllTests = new ArrayList<>();

    public ExcelSheet() {
    }

    /**
     * Generate instances of TestWrapper for each and every test in the listOfAllTests
     *
     * @return the list of new instances of TestWrapper
     */
    public List<TestWrapper> generateTestWrappersForAllTest() {
        List<TestWrapper> testWrapperList = new ArrayList<>();
        listOfAllTests.forEach(test -> {
            test.getTest_results().forEach(testResult -> {
                testWrapperList.add(new TestWrapper(test.getDate(), test.getAA(), test.getDocument_nr(), test.getCustomer_nr(),
                        testResult.getTest_type().replace('\n', ' '), testResult.getTest_result(), testResult.getSoll(), testResult.getSoll_plus(), testResult.getSoll_minus()));
            });
        });
        return testWrapperList;
    }

    /**
     * Uses DVPParser to get all tests from the given Excel file and saves them in listOfAllTests
     *
     * @param path the path to the Excel file
     * @throws IOException the io exception
     */
    public void parseExcelFile(String path) throws IOException {
        DVPParser dvp = new DVPParser();
        dvp.readXLSXFile(path);
        listOfAllTests = dvp.getTests();
    }

    public List<Test> getListOfAllTests() {
        return listOfAllTests;
    }
    public void setListOfAllTests(List<Test> listOfAllTests) {
        this.listOfAllTests = listOfAllTests;
    }
}
