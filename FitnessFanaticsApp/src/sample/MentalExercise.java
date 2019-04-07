package sample;

import java.sql.SQLException;

public class MentalExercise {

    int userId;
    long date;
    String exercise;
    int emotionalRating;
    int timeTaken;

    public MentalExercise(int userId, long date, String exercise, int emotionalRating, int timeTaken) {
        this.userId = userId;
        this.date = date;
        this.exercise = exercise;
        this.emotionalRating = emotionalRating;
        this.timeTaken = timeTaken;
    }

    public MentalExercise() {
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

    public int getEmotionalRating() {
        return emotionalRating;
    }

    public void setEmotionalRating(int emotionalRating) {
        this.emotionalRating = emotionalRating;
    }

    public int getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }

    @Override
    public String toString() {
        return "MentalExercise{" +
                "userId=" + userId +
                ", date=" + date +
                ", exercise='" + exercise + '\'' +
                ", emotionalRating=" + emotionalRating +
                ", timeTaken=" + timeTaken +
                '}';
    }

    public void createDatabaseRecord() throws SQLException {
        String query = String.format("INSERT INTO \"Mental_Exercise\" (\"userId\",\"date\",\"exercise\",\"reflectionRating\",\"timeTaken\") " +
                "VALUES (%s,%s,'%s',%s,%s);",
                userId, date, exercise, emotionalRating, timeTaken
        );
        Database database = new Database();
        database.insertStatement(query);
    }
}
