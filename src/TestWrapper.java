import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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

    @Override
    public String toString() {
        return "TestWrapper{" +
                "date='" + date + '\'' +
                ", AA='" + AA + '\'' +
                ", documentNr='" + documentNr + '\'' +
                ", customerNr='" + customerNr + '\'' +
                ", testType='" + testType + '\'' +
                ", testResult='" + testResult + '\'' +
                ", soll='" + soll + '\'' +
                ", sollPlus='" + sollPlus + '\'' +
                ", sollMinus='" + sollMinus + '\'' +
                '}';
    }

    public void insert(String uid) throws SQLException {
        if (isNotInDatabase(this)) {
            DatabaseChange dc = new DatabaseChange(uid, "Uploaded a test for " + documentNr + " to the database at ", new Timestamp(System.currentTimeMillis()));
            dc.insert();
            try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO dvp (part_id, date, aa, consumer_id, test_name, test_result, test_soll, test_soll_plus, test_soll_minus) VALUES (?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                s.setString(1, this.documentNr);
                s.setString(2, this.date);
                s.setString(3, this.AA);
                s.setString(4, this.customerNr);
                s.setString(5, this.testType);
                s.setString(6, this.testResult);
                s.setString(7, this.soll);
                s.setString(8, this.sollPlus);
                s.setString(9, this.sollMinus);
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
        return Objects.equals(date, that.date) && Objects.equals(AA, that.AA) && Objects.equals(documentNr, that.documentNr) && Objects.equals(customerNr, that.customerNr) && Objects.equals(testType, that.testType) && Objects.equals(testResult, that.testResult) && Objects.equals(soll, that.soll) && Objects.equals(sollPlus, that.sollPlus) && Objects.equals(sollMinus, that.sollMinus);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAA() {
        return AA;
    }

    public void setAA(String AA) {
        this.AA = AA;
    }

    public String getDocumentNr() {
        return documentNr;
    }

    public void setDocumentNr(String documentNr) {
        this.documentNr = documentNr;
    }

    public String getCustomerNr() {
        return customerNr;
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

    public String getSoll() {
        return soll;
    }

    public void setSoll(String soll) {
        this.soll = soll;
    }

    public String getSollPlus() {
        return sollPlus;
    }

    public void setSollPlus(String sollPlus) {
        this.sollPlus = sollPlus;
    }

    public String getSollMinus() {
        return sollMinus;
    }

    public void setSollMinus(String sollMinus) {
        this.sollMinus = sollMinus;
    }

}
