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
