package Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import sample.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DashboardController extends ApplicationBase {

    @FXML
    NumberAxis y_axis;
    @FXML
    CategoryAxis x_axis;
    @FXML
    LineChart<?, ?> chart_sleepTracker;

    HealthCheck latestHealthCheck;
    long daysSinceLastHealthCheck = 0;

    @FXML
    Label value_recentCheckup;
    @FXML
    Label value_cholesterolLevel;
    @FXML
    Label value_bloodPressure;
    @FXML
    Label value_bloodSugar;

    @FXML
    Rectangle rectangle_daysSinceLastCheck;
    @FXML
    Rectangle rectangle_cholesterol;
    @FXML
    Rectangle rectangle_bloodPressure;
    @FXML
    Rectangle rectangle_bloodSugar;

    // Calorie summary things

    enum Macronutrient {CARBS, FATS, PROTEINS};
    enum Activity_Type {RESISTANCE, AEROBIC, MENTAL, GYM, SLEEP};

    double caloriesConsumed;
    double caloriesBurned;
    @FXML
    private PieChart chart_macros;
    @FXML
    private PieChart chart_timeAllocation;
    @FXML
    Label value_caloriesConsumed;
    @FXML
    Label value_caloriesBurned;
    @FXML
    Label value_netIntake;
    @FXML
    Label value_goalIntake;
    @FXML
    Label value_difference;
    @FXML
    Rectangle rectangle_calories;


    ArrayList<FoodIntake> foodItems = new ArrayList<>();

    @FXML
    public void initialize() throws SQLException {
        setupHeaders("Dashboard");
        getRecentHealthCheck();
        getFoodConsumption();
        getActivityLog();
        getCalorieSummary();
        getSleepChart();
    }

    private void getSleepChart() {
        XYChart.Series series = new XYChart.Series();
        ArrayList<OtherActivity> sleepTimes = getSleepTimes();

        int i = 0;
        if (sleepTimes.size() > 5) {
            i = (sleepTimes.size() - 5);
        }
        for (; i < sleepTimes.size(); i++) {
            System.out.println(String.format("Adding: %s, %s ", DateHelper.longDateToStringWithoutYear(sleepTimes.get(i).getDate()), sleepTimes.get(i).getTimeTaken()));
            series.getData().add(new XYChart.Data(DateHelper.longDateToStringWithoutYear(sleepTimes.get(i).getDate()), sleepTimes.get(i).getTimeTaken()));
        }

        chart_sleepTracker.getData().addAll(series);
    }

    private ArrayList<OtherActivity> getSleepTimes() {
        ArrayList<OtherActivity> sleepTimes = new ArrayList<>();
        String query = String.format("SELECT * FROM Other_Activities  " +
                "WHERE userId = %s AND activityType = 'Sleep'", SessionDataHolder.user.getId());
        try {
            ResultSet rs = database.getResultSet(query);
            while (rs.next()) {
                sleepTimes.add(new OtherActivity(
                        rs.getInt(1),
                        rs.getLong(2),
                        rs.getString(3),
                        rs.getInt(4)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sleepTimes;
    }

    private void getCalorieSummary() {
        double netIntake = caloriesConsumed - caloriesBurned;
        value_netIntake.setText(String.format("%.2f", netIntake) + "kcals");
        double goalIntake = getGoalIntake();
        if (goalIntake != 0) value_goalIntake.setText(String.format("%.2f", goalIntake) + "kcals");
        double difference = goalIntake - netIntake;
        if (goalIntake != 0) value_difference.setText(String.format("%.2f", difference) + "kcals");

        //  Get color scale
        if (difference < -400 || difference > 400) {
            rectangle_calories.setFill(Color.RED);
        } else if (difference < -300 || difference > 300) {
            rectangle_calories.setFill(Color.ORANGE);
        } else if (difference < -200 || difference > 200) {
            rectangle_calories.setFill(Color.YELLOW);
        } else if (difference < -100 || difference > 100) {
            rectangle_calories.setFill(Color.YELLOWGREEN);
        } else {
            rectangle_calories.setFill(Color.GREEN);
        }

    }

    private double getGoalIntake() {
        String query = String.format("SELECT caloricIntake FROM Goals WHERE userId = %s", SessionDataHolder.user.getId());
        try {
            ResultSet rs = database.getResultSet(query);
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void getActivityLog() throws SQLException {
        double aerobicExerciseTime = getActivityTime(Activity_Type.AEROBIC);
        double resistanceExerciseTime = getActivityTime(Activity_Type.RESISTANCE);
        double mentalExerciseTime = getActivityTime(Activity_Type.MENTAL);
        double sleepTime = getActivityTime(Activity_Type.SLEEP);
        double gymTime = getActivityTime(Activity_Type.GYM);

        // Calculate and set calories burned;

        double unallocatedTime = 1440 - (aerobicExerciseTime + resistanceExerciseTime + mentalExerciseTime + sleepTime + gymTime);
        if (unallocatedTime < 0) unallocatedTime = 0;
        caloriesBurned = calculateCaloriesBurned(aerobicExerciseTime, resistanceExerciseTime, mentalExerciseTime, sleepTime, gymTime, unallocatedTime);
        value_caloriesBurned.setText(String.format("%.2f", caloriesBurned) + "kcals");
        chart_timeAllocation.setData(getTimeChartData(aerobicExerciseTime, resistanceExerciseTime, mentalExerciseTime, sleepTime, gymTime, unallocatedTime));
    }

    private double calculateCaloriesBurned(double aerobicExerciseTime, double resistanceExerciseTime, double mentalExerciseTime, double sleepTime, double gymTime, double unallocatedTime) {
        double caloriesBurned = 0;

        caloriesBurned += (unallocatedTime / (60.0 * 24)) * SessionDataHolder.user.calculateBasalMetabolicRate();
        caloriesBurned += (sleepTime / 60.0) * SessionDataHolder.user.getWeight();
        caloriesBurned += (gymTime / 60.0) * SessionDataHolder.user.getWeight() * 6.0;
        caloriesBurned += (resistanceExerciseTime / 60.0) * SessionDataHolder.user.getWeight() * 8.0;
        caloriesBurned += (aerobicExerciseTime / 60.0) * SessionDataHolder.user.getWeight() * 4.0;

        return caloriesBurned;
    }

    private double getActivityTime(Activity_Type activityType) throws SQLException {
        double totalTime = 0;
        String query = "";

        switch (activityType) {
            case AEROBIC:
                query = String.format("SELECT timeTaken from Aerobic_Exercise " +
                                "WHERE (userId = %s AND date = %s)",
                        SessionDataHolder.user.getId(), DateHelper.getCurrentDate());
                break;
            case RESISTANCE:
                query = String.format("SELECT timeTaken from Resistance_Exercise " +
                                "WHERE (userId = %s AND date = %s)",
                        SessionDataHolder.user.getId(), DateHelper.getCurrentDate());
                break;
            case MENTAL:
                query = String.format("SELECT timeTaken from Mental_Exercise " +
                                "WHERE (userId = %s AND date = %s)",
                        SessionDataHolder.user.getId(), DateHelper.getCurrentDate());
                break;
            case SLEEP:
                query = String.format("SELECT timeTaken from Other_Activities " +
                                "WHERE (userId = %s AND date = %s AND activityType = 'Sleep')",
                        SessionDataHolder.user.getId(), DateHelper.getCurrentDate());
                break;
            case GYM:
                query = String.format("SELECT timeTaken from Other_Activities " +
                                "WHERE (userId = %s AND date = %s AND activityType = 'Gym')",
                        SessionDataHolder.user.getId(), DateHelper.getCurrentDate());
                break;

        }

        ResultSet rs = database.getResultSet(query);

        while (rs.next()) {
            totalTime += rs.getDouble(1);
        }

        return totalTime;
    }

    private ObservableList<PieChart.Data> getTimeChartData(double aerobicExerciseTime, double resistanceExerciseTime, double mentalExerciseTime, double sleepTime, double gymTime, double unallocatedTime) {
        ObservableList<PieChart.Data> answer = FXCollections.observableArrayList();
        answer.addAll(new PieChart.Data("Resistance", aerobicExerciseTime),
                new PieChart.Data("Aerobic", resistanceExerciseTime),
                new PieChart.Data("Mental", mentalExerciseTime),
                new PieChart.Data("Unallocated", unallocatedTime),
                new PieChart.Data("Sleep", sleepTime),
                new PieChart.Data("Gym", gymTime));
        return answer;
    }

    private void getFoodConsumption() {
        getFoodData();
        double carbCalories = getCalories(Macronutrient.CARBS);
        double fatCalories = getCalories(Macronutrient.FATS);
        double proteinCalories = getCalories(Macronutrient.PROTEINS);
        caloriesConsumed = carbCalories + fatCalories + proteinCalories;
        value_caloriesConsumed.setText(Double.toString(caloriesConsumed) + "kcals");
        chart_macros.setData(getMacroChartData(carbCalories, fatCalories, proteinCalories));
    }

    private void getFoodData() {

        String query;
        query = String.format("SELECT * FROM FoodIntake " +
                        "WHERE (date = %s AND userId = %s)",
                DateHelper.getCurrentDate(), SessionDataHolder.user.getId());

        ArrayList<FoodIntake> foodItems = new ArrayList<>();

        try {
            ResultSet rs = database.getResultSet(query);
            while (rs.next()) {
                foodItems.add(new FoodIntake(
                        Integer.parseInt(rs.getString(1)), // mealId
                        Integer.parseInt(rs.getString(2)), // userId
                        Long.parseLong(rs.getString(3)), // date
                        rs.getString(4), // mealTime
                        rs.getString(5), // description
                        Double.parseDouble(rs.getString(6)), // carbContent
                        Double.parseDouble(rs.getString(7)), // fatContent
                        Double.parseDouble(rs.getString(8)), // proteinContent
                        rs.getString(9)
                ));
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.foodItems = foodItems;

    }

    private double getCalories(Macronutrient macronutrient) {
        double calories = 0;
        switch (macronutrient) {
            case CARBS:
                calories += getAmount(Macronutrient.CARBS) * 4;
                break;
            case FATS:
                calories += getAmount(Macronutrient.FATS) * 9;
                break;
            case PROTEINS:
                calories += getAmount(Macronutrient.PROTEINS) * 4;
                break;
        }
        return calories;
    }

    private double getAmount(Macronutrient macronutrient) {
        double amount = 0;
        switch (macronutrient) {
            case CARBS:
                for (int i = 0; i < foodItems.size(); i++) {
                    amount += foodItems.get(i).getCarbContent();
                }
                break;

            case FATS:
                for (int i = 0; i < foodItems.size(); i++) {
                    amount += foodItems.get(i).getFatContent();
                }
                break;

            case PROTEINS:
                for (int i = 0; i < foodItems.size(); i++) {
                    amount += foodItems.get(i).getProteinContent();
                }
                break;
        }
        return amount;
    }

    private ObservableList<PieChart.Data> getMacroChartData(double carbCalories, double fatCalories, double proteinCalories) {
        ObservableList<PieChart.Data> answer = FXCollections.observableArrayList();
        answer.addAll(new PieChart.Data("Carbs", carbCalories),
                new PieChart.Data("Fats", fatCalories),
                new PieChart.Data("Proteins", proteinCalories));
        return answer;
    }

    private void getRecentHealthCheck() {
        String query = String.format("select * from Health_Check  " +
                        "WHERE userId = %s ORDER BY date desc",
                SessionDataHolder.user.getId());
        try {
            ResultSet rs = database.getResultSet(query);
            if (rs.next()) {
                System.out.println("Adding another row");
                latestHealthCheck = new HealthCheck(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getLong(3),
                        rs.getDouble(4),
                        rs.getDouble(5),
                        rs.getDouble(6),
                        rs.getString(7)
                );
                daysSinceLastHealthCheck = DateHelper.getDaysInBetween(DateHelper.getCurrentDate(), latestHealthCheck.getDate());
                System.out.println("Days since last check: " + Long.toString(daysSinceLastHealthCheck));
                value_recentCheckup.setText(Long.toString(daysSinceLastHealthCheck) + " days");
                value_cholesterolLevel.setText(Double.toString(latestHealthCheck.getCholesterolLevel()) + "mg/dL");
                value_bloodPressure.setText(Double.toString(latestHealthCheck.getBloodPressure()) + "mmHg");
                value_bloodSugar.setText(Double.toString(latestHealthCheck.getBloodSugar()) + "mmol/L");
                setColorScales();
            } else {
                value_recentCheckup.setText("No Records");
                value_cholesterolLevel.setText("N/A");
                value_bloodSugar.setText("N/A");
                value_bloodPressure.setText("N/A");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void setColorScales() {
        // Cholesterol Level
        if (latestHealthCheck.getCholesterolLevel() < 200) {
            rectangle_cholesterol.setFill(Color.GREEN);
        } else if (latestHealthCheck.getCholesterolLevel() < 240) {
            rectangle_cholesterol.setFill(Color.YELLOW);
        } else {
            rectangle_cholesterol.setFill(Color.RED);
        }

        // Blood Pressure
        if (latestHealthCheck.getBloodPressure() < 120) {
            rectangle_bloodPressure.setFill(Color.GREEN);
        } else if (latestHealthCheck.getBloodPressure() < 129) {
            rectangle_bloodPressure.setFill(Color.YELLOWGREEN);
        } else if (latestHealthCheck.getBloodPressure() < 139) {
            rectangle_bloodPressure.setFill(Color.YELLOW);
        } else if (latestHealthCheck.getBloodPressure() < 180) {
            rectangle_bloodPressure.setFill(Color.ORANGE);
        } else {
            rectangle_bloodPressure.setFill(Color.RED);
        }

        // Blood Sugar
        if (latestHealthCheck.getBloodSugar() < 60 || latestHealthCheck.getBloodSugar() > 200) {
            rectangle_bloodSugar.setFill(Color.RED);
        } else if (latestHealthCheck.getBloodSugar() < 100 || latestHealthCheck.getBloodSugar() > 150) {
            rectangle_bloodSugar.setFill(Color.YELLOW);
        } else {
            rectangle_bloodSugar.setFill(Color.GREEN);
        }

        // Days since
        if (daysSinceLastHealthCheck > 365 * 2) {
            rectangle_daysSinceLastCheck.setFill(Color.RED);
        } else if (daysSinceLastHealthCheck > 365 * 1) {
            rectangle_daysSinceLastCheck.setFill(Color.YELLOW);
        } else {
            rectangle_daysSinceLastCheck.setFill(Color.GREEN);
        }
    }

    @FXML
    public void onHelpCholesterolButtonClick(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../Application/HelpCholesterol.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 259, 255);
            Stage stage = new Stage();
            stage.setTitle("Cholesterol Ratings");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onHelpBloodPressureButtonClick(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../Application/HelpBloodPressure.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 259, 345);
            Stage stage = new Stage();
            stage.setTitle("Blood Pressure");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onHelpBloodSugarButtonClick(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../Application/HelpBloodSugar.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 335, 255);
            Stage stage = new Stage();
            stage.setTitle("Blood Sugar Guide");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onHelpCalorieButtonClick(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../Application/HelpCalorie.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 259, 302);
            Stage stage = new Stage();
            stage.setTitle("Calorie Guide");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onHelpHealthCheckButtonClick(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../Application/HelpHealthCheck.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 259, 255);
            Stage stage = new Stage();
            stage.setTitle("Cholesterol Ratings");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
