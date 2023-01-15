import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 * Class for holding all information for one test - merged info from Test and TestResult, used for showing data in
 * frontend and for inserting into dataabse
 *
 * @author Jacob Kristof
 * @version 1.0
 */
public class TestWrapper {

    private String date;
    private String AA;
    private String documentNr;
    private String customerNr;
    private String testType;
    private String testResult;
    private String soll;
    private String sollPlus;
    private String sollMinus;


    /**
     * Constructor with all class variables
     *
     * @param date       the date
     * @param AA         the AA
     * @param documentNr the document number/designation number
     * @param customerNr the customer number
     * @param testType   the test type
     * @param testResult the test result
     * @param soll       the soll
     * @param sollPlus   the soll plus
     * @param sollMinus  the soll minus
     */
    public TestWrapper(String date, String AA, String documentNr, String customerNr, String testType, String testResult, String soll, String sollPlus, String sollMinus) {
        this.date = date;
        this.AA = AA;
        this.documentNr = documentNr;
        this.customerNr = customerNr;
        this.testType = testType;
        this.testResult = testResult;
        this.soll = soll;
        this.sollPlus = sollPlus;
        this.sollMinus = sollMinus;
    }

    public void editInDatabase(Test test, TestResult testResult) throws SQLException {
        DatabaseChange dc = new DatabaseChange(User.getName(), "Edited test for " + test.getDocument_nr() + " in the database", new Timestamp(System.currentTimeMillis()));
        dc.insert();
        System.out.println(this.soll + " " + this.sollPlus + " " + this.sollMinus);
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE test_result " +
                                                                                    "SET test_result = ?, test_soll = ?, test_soll_plus = ?, test_soll_minus = ? " +
                                                                                    "WHERE test_id = ? AND id = ?",
                                                                                    Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, this.testResult);
            s.setString(2, this.soll);
            s.setString(3, this.sollPlus);
            s.setString(4, this.sollMinus);
            s.setInt(5, test.databaseId);
            s.setInt(6, testResult.getDbid());
            s.executeUpdate();
        }


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestWrapper that = (TestWrapper) o;
        return date.equals(that.date) && AA.equals(that.AA) && documentNr.equals(that.documentNr) && customerNr.equals(that.customerNr) && testType.equals(that.testType) && testResult.equals(that.testResult) && soll.equals(that.soll) && sollPlus.equals(that.sollPlus) && sollMinus.equals(that.sollMinus);
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setAA(String AA) {
        this.AA = AA;
    }
    public void setDocumentNr(String documentNr) {
        this.documentNr = documentNr;
    }
    public void setCustomerNr(String customerNr) {
        this.customerNr = customerNr;
    }
    public String getTestType() {
        return testType;
    }
    public void setTestType(String testType) {
        this.testType = testType;
    }
    public String getTestResult() {
        return testResult;
    }
    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }
    public void setSoll(String soll) {
        this.soll = soll;
    }
    public void setSollPlus(String sollPlus) {
        this.sollPlus = sollPlus;
    }
    public void setSollMinus(String sollMinus) {
        this.sollMinus = sollMinus;
    }
    public String getAA() {
        return AA;
    }
    public String getDocumentNr() {
        return documentNr;
    }
    public String getCustomerNr() {
        return customerNr;
    }
    public String getSoll() {
        return soll;
    }
    public String getSollPlus() { return sollPlus; }
    public String getSollMinus() {
        return sollMinus;
    }
}
