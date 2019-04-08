package Application;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sample.SessionDataHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DailyStatsDisplayController extends DailyStatsController {

    private static final int IDEAL_RESTING_HEART_RATE = 80;

    @FXML Label value_height;
    @FXML Label value_weight;
    @FXML Label value_bmi;
    @FXML Label value_fatMass;
    @FXML Label value_leanMass;
    @FXML Label value_bodyFat;
    @FXML Label value_stepCount;
    @FXML Label value_stairCount;
    @FXML Label value_heartRate;

    @FXML Label goal_stepCount;
    @FXML Label goal_stairCount;
    @FXML Label goal_restingHeartRate;

    @FXML Label achievment_stepCount;
    @FXML Label achievment_stairCount;
    @FXML Label achievment_restingHeartRate;

    @FXML Rectangle rectangle_stepCount;
    @FXML Rectangle rectangle_stairCount;
    @FXML Rectangle rectangle_restingHeartRate;

    @FXML private PieChart bmi_chart;

    @FXML
    public void initialize() throws SQLException {
        date_selection.setValue(LocalDate.now());
        setupHeaders("Daily Stats - Summary");
        value_height.setText(Double.toString(SessionDataHolder.dailyData.getHeight()));
        value_weight.setText(Double.toString(SessionDataHolder.dailyData.getWeight()));
        String bmi = String.format("%.2f", calculateBodyMassIndex());
        value_bmi.setText(bmi);
        String fatMass = String.format("%.2f", calculateLeanMass());
        value_fatMass.setText(fatMass);
        String leanMass = String.format("%.2f", calculateFatMass());
        value_leanMass.setText(leanMass);
        value_bodyFat.setText(Double.toString(SessionDataHolder.dailyData.getBodyFatPercentage()));
        value_stepCount.setText(Integer.toString(SessionDataHolder.dailyData.getStepCount()));
        value_stairCount.setText(Integer.toString(SessionDataHolder.dailyData.getStairCount()));
        value_heartRate.setText(Integer.toString(SessionDataHolder.dailyData.getRestingHeartRate()));

        bmi_chart.setData(getBmiChartData());
        setColorScales();

        //TODO: Set color scales for step counts
    }

    private double calculateBodyMassIndex() {
        return (SessionDataHolder.dailyData.getWeight() / Math.pow(SessionDataHolder.dailyData.getHeight()/100, 2));
    }

    private double calculateFatMass() {
        return SessionDataHolder.dailyData.getWeight() * (SessionDataHolder.dailyData.getBodyFatPercentage() / 100);
    }

    private double calculateLeanMass() {
        return SessionDataHolder.dailyData.getWeight() * (1 - (SessionDataHolder.dailyData.getBodyFatPercentage() / 100));
    }

    private ObservableList<PieChart.Data> getBmiChartData() {
        ObservableList<PieChart.Data> answer = FXCollections.observableArrayList();
        answer.addAll(new PieChart.Data("Fat Mass", calculateFatMass()),
                new PieChart.Data("Lean Mass", calculateLeanMass()));
        return answer;
    }

    private void setColorScales() {
        int stepGoal = 0;
        int stairGoal = 0;
        double stepAchievment = 0;
        double stairAchievment = 0;

        goal_restingHeartRate.setText(Integer.toString(IDEAL_RESTING_HEART_RATE));
        double restingHeartRateAchievment = (100 * SessionDataHolder.dailyData.getRestingHeartRate())/ (double) IDEAL_RESTING_HEART_RATE;
        achievment_restingHeartRate.setText(String.format("%.2f", restingHeartRateAchievment) + "%");

        String query = String.format("SELECT * FROM Goals " +
                        "WHERE (userId = %s);",
                SessionDataHolder.user.getId());
        try {
            ResultSet rs = database.getResultSet(query);
            if (rs.next()) {
                stepGoal = rs.getInt(2);
                stairGoal = rs.getInt(3);
                goal_stepCount.setText(Integer.toString(stepGoal));
                goal_stairCount.setText(Integer.toString(stairGoal));
                stepAchievment = (100 * SessionDataHolder.dailyData.getStepCount())/ (double) stepGoal;
                stairAchievment =  (100 * SessionDataHolder.dailyData.getStairCount()) / (double) stairGoal;
                achievment_stepCount.setText(String.format("%.2f", stepAchievment) + "%");
                achievment_stairCount.setText(String.format("%.2f", stairAchievment) + "%");

            } else {
                goal_stairCount.setText("Goal not set");
                goal_stepCount.setText("Goal not set");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // stepGoal
        if (stepAchievment < 25) {
            rectangle_stepCount.setFill(Color.RED);
        } else if (stepAchievment < 50) {
            rectangle_stepCount.setFill(Color.ORANGE);
        } else if (stepAchievment < 75) {
            rectangle_stepCount.setFill(Color.YELLOW);
        } else if (stepAchievment < 100) {
            rectangle_stepCount.setFill(Color.YELLOWGREEN);
        } else {
            rectangle_stepCount.setFill(Color.GREEN);
        }

        // stairGoal
        if (stairAchievment < 25) {
            rectangle_stairCount.setFill(Color.RED);
        } else if (stairAchievment < 50) {
            rectangle_stairCount.setFill(Color.ORANGE);
        } else if (stairAchievment < 75) {
            rectangle_stairCount.setFill(Color.YELLOW);
        } else if (stairAchievment < 100) {
            rectangle_stairCount.setFill(Color.YELLOWGREEN);
        } else {
            rectangle_stairCount.setFill(Color.GREEN);
        }


        // restingHeartRate
        if (restingHeartRateAchievment < 25) {
            rectangle_restingHeartRate.setFill(Color.RED);
        } else if (restingHeartRateAchievment < 50) {
            rectangle_restingHeartRate.setFill(Color.ORANGE);
        } else if (restingHeartRateAchievment < 75) {
            rectangle_restingHeartRate.setFill(Color.YELLOW);
        } else if (restingHeartRateAchievment < 100) {
            rectangle_restingHeartRate.setFill(Color.YELLOWGREEN);
        } else {
            rectangle_restingHeartRate.setFill(Color.GREEN);
        }


    }


}
