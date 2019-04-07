package Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import sample.SessionDataHolder;

import java.sql.SQLException;

public class DailyStatsDisplayController extends DailyStatsController {

    @FXML Label value_height;
    @FXML Label value_weight;
    @FXML Label value_bmi;
    @FXML Label value_fatMass;
    @FXML Label value_leanMass;
    @FXML Label value_bodyFat;
    @FXML Label value_stepCount;
    @FXML Label value_stairCount;
    @FXML Label value_heartRate;

    @FXML private PieChart bmi_chart;

    @FXML
    public void initialize() throws SQLException {
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

}
