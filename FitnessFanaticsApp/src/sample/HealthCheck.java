package sample;

import java.sql.SQLException;

public class HealthCheck {
    int reportId;
    int userId;
    long date;
    String dateString;
    double cholesterolLevel;
    double bloodPressure;
    double bloodSugar;
    String doctorsComment;

    public HealthCheck(int reportId, int userId, long date, double cholesterolLevel, double bloodPressure, double bloodSugar, String doctorsComment) {
        this.reportId = reportId;
        this.userId = userId;
        this.date = date;
        this.dateString = DateHelper.longDateToString(date);
        this.cholesterolLevel = cholesterolLevel;
        this.bloodPressure = bloodPressure;
        this.bloodSugar = bloodSugar;
        this.doctorsComment = doctorsComment;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public double getCholesterolLevel() {
        return cholesterolLevel;
    }

    public void setCholesterolLevel(double cholesterolLevel) {
        this.cholesterolLevel = cholesterolLevel;
    }

    public double getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(double bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public double getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(double bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public String getDoctorsComment() {
        return doctorsComment;
    }

    public void setDoctorsComment(String doctorsComment) {
        this.doctorsComment = doctorsComment;
    }

    @Override
    public String toString() {
        return "HealthCheck{" +
                "reportId=" + reportId +
                ", userId=" + userId +
                ", date=" + date +
                ", dateString='" + dateString + '\'' +
                ", cholesterolLevel=" + cholesterolLevel +
                ", bloodPressure=" + bloodPressure +
                ", bloodSugar=" + bloodSugar +
                ", doctorsComment='" + doctorsComment + '\'' +
                '}';
    }

    // This method will save the specific instance into the database
    public void createDatabaseRecord() throws SQLException {
        String query = String.format("INSERT INTO \"Health_Check\" (\"userId\",\"date\",\"cholesterolLevel\",\"bloodPressure\",\"bloodSugar\",\"doctorsComment\") " +
                        "VALUES (%s,%s,%s,%s,%s,'%s');",
                userId, date, cholesterolLevel, bloodPressure, bloodSugar, doctorsComment
        );
        Database database = new Database();
        database.insertStatement(query);
    }

}
