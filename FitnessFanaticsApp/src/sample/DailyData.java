package sample;

import java.sql.SQLException;

public class DailyData {

    private int userId;
    private long date;
    private int stepCount;
    private int stairCount;
    private int restingHeartRate;
    private double height;
    private double weight;
    private double bodyFatPercentage;

    public DailyData() {
    }

    public DailyData(int userId, long date, int stepCount, int stairCount, int restingHeartRate, double height, double weight, double bodyFatPercentage) {
        this.userId = userId;
        this.date = date;
        this.stepCount = stepCount;
        this.stairCount = stairCount;
        this.restingHeartRate = restingHeartRate;
        this.height = height;
        this.weight = weight;
        this.bodyFatPercentage = bodyFatPercentage;
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

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public int getStairCount() {
        return stairCount;
    }

    public void setStairCount(int stairCount) {
        this.stairCount = stairCount;
    }

    public int getRestingHeartRate() {
        return restingHeartRate;
    }

    public void setRestingHeartRate(int restingHeartRate) {
        this.restingHeartRate = restingHeartRate;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(double bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    @Override
    public String toString() {
        return "DailyData{" +
                "userId=" + userId +
                ", date=" + date +
                ", stepCount=" + stepCount +
                ", stairCount=" + stairCount +
                ", restingHeartRate=" + restingHeartRate +
                ", height=" + height +
                ", weight=" + weight +
                ", bodyFatPercentage=" + bodyFatPercentage +
                '}';
    }

    public void createDatabaseRecord() throws SQLException {
        String query = String.format("INSERT INTO \"DailyData\" (\"userID\",\"date\",\"stepCount\",\"stairCount\"," +
                        "\"restingHeartRate\",\"weight\",\"bodyFatPercentage\",\"height\") " +
                        "VALUES (%s,%s,%s,%s,%s,%s,%s,%s)",
                        userId, date, stepCount, stairCount, restingHeartRate, weight, bodyFatPercentage,  height);
        Database database = new Database();
        database.insertStatement(query);
    }
}
