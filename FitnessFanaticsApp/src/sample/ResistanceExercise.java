package sample;

import java.sql.SQLException;

public class ResistanceExercise {

    int exerciseId;
    int userId;
    long date;
    String exercise;
    int mass;
    int sets;
    int timeTaken;

    public ResistanceExercise(int exerciseId, int userId, long date, String exercise, int mass, int sets, int timeTaken) {
        this.exerciseId = exerciseId;
        this.userId = userId;
        this.date = date;
        this.exercise = exercise;
        this.mass = mass;
        this.sets = sets;
        this.timeTaken = timeTaken;
    }

    public ResistanceExercise() {
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

    public int getMass() {
        return mass;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    @Override
    public String toString() {
        return "ResistanceExercise{" +
                "userId=" + userId +
                ", date=" + date +
                ", exercise='" + exercise + '\'' +
                ", mass=" + mass +
                ", sets=" + sets +
                ", timeTaken=" + timeTaken +
                '}';
    }

    public void createDatabaseRecord() throws SQLException {
        String query = String.format("INSERT INTO \"Resistance_Exercise\" (\"userId\",\"date\",\"exercise\",\"mass\",\"sets\",\"timeTaken\") " +
                "VALUES (%s,%s,'%s',%s,%s,%s);",
                userId, date, exercise, mass, sets, timeTaken
        );
        Database database = new Database();
        database.insertStatement(query);
    }
}
