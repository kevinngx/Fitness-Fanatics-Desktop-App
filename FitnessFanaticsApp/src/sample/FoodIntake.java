package sample;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class FoodIntake {

    int mealId;
    int userId;
    long date;
    String mealTime;
    String description;
    double carbContent;
    double fatContent;
    double proteinContent;
    double calories;
    String foodGroup;

    public FoodIntake() {
    }

    public FoodIntake(int mealId, int userId, long date, String mealTime, String description, double carbContent, double fatContent, double proteinContent, String foodGroup) {
        this.mealId = mealId;
        this.userId = userId;
        this.date = date;
        this.mealTime = mealTime;
        this.description = description;
        this.carbContent = carbContent;
        this.fatContent = fatContent;
        this.proteinContent = proteinContent;
        this. calories = ( (carbContent * 4) + (proteinContent * 4) + (fatContent * 9));
        this.foodGroup = foodGroup;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
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

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCarbContent() {
        return carbContent;
    }

    public void setCarbContent(double carbContent) {
        this.carbContent = carbContent;
    }

    public double getFatContent() {
        return fatContent;
    }

    public void setFatContent(double fatContent) {
        this.fatContent = fatContent;
    }

    public double getProteinContent() {
        return proteinContent;
    }

    public void setProteinContent(double proteinContent) {
        this.proteinContent = proteinContent;
    }

    @Override
    public String toString() {
        return "FoodIntake{" +
                "mealId=" + mealId +
                ", userId=" + userId +
                ", date=" + date +
                ", mealTime='" + mealTime + '\'' +
                ", description='" + description + '\'' +
                ", carbContent=" + carbContent +
                ", fatContent=" + fatContent +
                ", proteinContent=" + proteinContent +
                '}';
    }

    public String getFoodGroup() {
        return foodGroup;
    }

    public void setFoodGroup(String foodGroup) {
        this.foodGroup = foodGroup;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void createDatabaseRecord() throws SQLException {
        String query = String.format("INSERT INTO \"FoodIntake\" (\"userId\",\"date\",\"mealTime\",\"description\",\"carbContent\",\"fatContent\",\"proteinContent\",\"foodGroup\") " +
                "VALUES (%s, %s,'%s','%s', %s, %s, %s,'%s');", userId, date, mealTime, description, carbContent, fatContent, proteinContent, foodGroup);
        Database database = new Database();
        database.insertStatement(query);
    }
}

