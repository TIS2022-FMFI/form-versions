import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;

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

    public void insert(String uid) throws SQLException {
        if (isNotInDatabase(this)) {
            DatabaseChange dc = new DatabaseChange(uid, "Uploaded a test for " + documentNr + " to the database at ", new Timestamp(System.currentTimeMillis()));
            dc.insert();
            try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO dvp (part_id, date, aa, consumer_id, test_result, test_soll, test_soll_plus, test_soll_minus, test_type_id) VALUES (?,?,?,?,?,?,?,?,?)")) {
                s.setString(1, this.documentNr);
                s.setString(2, this.date);
                s.setString(3, this.AA);
                s.setString(4, this.customerNr);
                s.setString(5, this.testResult);
                s.setString(6, this.soll);
                s.setString(7, this.sollPlus);
                s.setString(8, this.sollMinus);
                s.setInt(9, TestTypeFinder.getInstance().returnIdInTable(this.testType));
                s.executeUpdate();
            }
        }
    }

    public boolean isNotInDatabase(TestWrapper test) throws SQLException {
        return (!TestWrapperFinder.getInstance().findAll().contains(test));
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

    public String getSollPlus() {
        return sollPlus;
    }

    public String getSollMinus() {
        return sollMinus;
    }
}
