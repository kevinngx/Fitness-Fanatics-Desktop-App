package sample;

import java.sql.SQLException;

public class AerobicExercise {

    int exerciseId;
    int userId;
    long date;
    String exercise;
    int distance;
    int time;

    public AerobicExercise(int exerciseId, int userId, long date, String exercise, int distance, int time) {
        this.exerciseId = exerciseId;
        this.userId = userId;
        this.date = date;
        this.exercise = exercise;
        this.distance = distance;
        this.time = time;
    }

    public AerobicExercise() {
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

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    @Override
    public String toString() {
        return "AerobicExercise{" +
                "userId=" + userId +
                ", date=" + date +
                ", exercise='" + exercise + '\'' +
                ", distance=" + distance +
                ", time=" + time +
                '}';
    }

    public void createDatabaseRecord() throws SQLException {
        String query = String.format("INSERT INTO \"Aerobic_Exercise\" (\"userId\",\"date\",\"exercise\",\"distance\",\"timeTaken\") " +
                "VALUES (%s,%s,'%s',%s,%s);",
                userId, date, exercise, distance, time);
        Database database = new Database();
        database.insertStatement(query);
    }

}
