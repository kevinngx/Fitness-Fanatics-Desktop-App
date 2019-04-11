package Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import sample.Database;
import sample.DateHelper;
import sample.SessionDataHolder;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ActivityLogBaseController extends ActivityLogController {

    Database database = new Database();

    enum Activity_Type {RESISTANCE, AEROBIC, MENTAL, GYM, SLEEP}

    @FXML Label value_resistanceExerciseTime;
    @FXML Label value_aerobicExerciseTime;
    @FXML Label value_mentalExerciseTime;
    @FXML Label value_sleepTime;
    @FXML Label value_gymTime;

    @FXML private PieChart chart_timeAllocation;


    @FXML
    public void initialize() {
        setupHeaders("Activity Log");
        date_selection.setValue(LocalDate.now());
        try {
            refreshLabels();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshLabels() throws SQLException {
        double aerobicExerciseTime = getActivityTime(Activity_Type.AEROBIC);
        double resistanceExerciseTime = getActivityTime(Activity_Type.RESISTANCE);
        double mentalExerciseTime = getActivityTime(Activity_Type.MENTAL);
        double sleepTime = getActivityTime(Activity_Type.SLEEP);
        double gymTime = getActivityTime(Activity_Type.GYM);

        value_aerobicExerciseTime.setText(Double.toString((int) aerobicExerciseTime));
        value_resistanceExerciseTime.setText(Double.toString((int) resistanceExerciseTime));
        value_mentalExerciseTime.setText(Double.toString((int) mentalExerciseTime));
        value_sleepTime.setText(Integer.toString((int) sleepTime));
        value_gymTime.setText(Integer.toString((int) gymTime));

        chart_timeAllocation.setData(getTimeChartData(aerobicExerciseTime, resistanceExerciseTime, mentalExerciseTime, sleepTime, gymTime));
    }

    private ObservableList<PieChart.Data> getTimeChartData(double aerobicExerciseTime, double resistanceExerciseTime, double mentalExerciseTime, double sleepTime, double gymTime) {
        ObservableList<PieChart.Data> answer = FXCollections.observableArrayList();
        double unallocated = 1440 - (aerobicExerciseTime + resistanceExerciseTime + mentalExerciseTime + sleepTime + gymTime);
        if (unallocated < 0) unallocated = 0;
        answer.addAll(new PieChart.Data("Resistance", aerobicExerciseTime),
                new PieChart.Data("Aerobic", resistanceExerciseTime),
                new PieChart.Data("Mental", mentalExerciseTime),
                new PieChart.Data("Unallocated", unallocated),
                new PieChart.Data("Sleep", sleepTime),
                new PieChart.Data("Gym", gymTime));
        return answer;
    }

    private double getActivityTime(Activity_Type activityType) throws SQLException {
        double totalTime = 0;
        String query = "";

        switch (activityType) {
            case AEROBIC:
                query = String.format("SELECT timeTaken from Aerobic_Exercise " +
                        "WHERE (userId = %s AND date = %s)",
                        SessionDataHolder.user.getId(), SessionDataHolder.dateRequested);
                break;
            case RESISTANCE:
                query = String.format("SELECT timeTaken from Resistance_Exercise " +
                                "WHERE (userId = %s AND date = %s)",
                        SessionDataHolder.user.getId(), SessionDataHolder.dateRequested);
                break;
            case MENTAL:
                query = String.format("SELECT timeTaken from Mental_Exercise " +
                                "WHERE (userId = %s AND date = %s)",
                        SessionDataHolder.user.getId(), SessionDataHolder.dateRequested);
                break;
            case SLEEP:
                query = String.format("SELECT timeTaken from Other_Activities " +
                        "WHERE (userId = %s AND date = %s AND activityType = 'Sleep')",
                        SessionDataHolder.user.getId(), SessionDataHolder.dateRequested);
                break;
            case GYM:
                query = String.format("SELECT timeTaken from Other_Activities " +
                                "WHERE (userId = %s AND date = %s AND activityType = 'Gym')",
                        SessionDataHolder.user.getId(), SessionDataHolder.dateRequested);
                break;

        }

        ResultSet rs = database.getResultSet(query);

        while (rs.next()) {
            totalTime += rs.getDouble(1);
        }

        return totalTime;
    }

    @FXML
    public void sleepMinusButtonClick(ActionEvent event) throws IOException {
        int sleepTime;
        String query = String.format("SELECT timeTaken from Other_Activities " +
                        "WHERE (userId = %s AND date = %s AND activityType = 'Sleep')",
                SessionDataHolder.user.getId(), SessionDataHolder.dateRequested);
        try {
            ResultSet rs = database.getResultSet(query);
            if (rs.next()) {
                // There is a result
                sleepTime = rs.getInt(1);
                if (sleepTime == 0) {
                    return;
                } else {
                    sleepTime -= 10;
                    query = String.format("UPDATE Other_Activities  " +
                            "SET timeTaken = %s " +
                            "WHERE (userId = %s AND date = %s AND activityType = 'Sleep');",
                            sleepTime, SessionDataHolder.user.getId(),SessionDataHolder.getDateRequested());
                    database.insertStatement(query);
                }
            } else {
                // If there is no result, make one!
                query = String.format("INSERT INTO \"Other_Activities\" (\"userId\",\"date\",\"activityType\",\"timeTaken\") " +
                        "VALUES (%s,%s,'Sleep',%s);",
                        SessionDataHolder.user.getId(), SessionDataHolder.getDateRequested(), 0);
                database.insertStatement(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            refreshLabels();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void sleepPlusButtonClick(ActionEvent event) throws IOException {

        int sleepTime;
        String query = String.format("SELECT timeTaken from Other_Activities " +
                        "WHERE (userId = %s AND date = %s AND activityType = 'Sleep')",
                SessionDataHolder.user.getId(), SessionDataHolder.dateRequested);
        try {
            ResultSet rs = database.getResultSet(query);
            if (rs.next()) {
                // There is a result
                sleepTime = rs.getInt(1);
                sleepTime += 10;
                query = String.format("UPDATE Other_Activities  " +
                                "SET timeTaken = %s " +
                                "WHERE (userId = %s AND date = %s AND activityType = 'Sleep');",
                        sleepTime, SessionDataHolder.user.getId(),SessionDataHolder.getDateRequested());
                database.insertStatement(query);
            } else {
                // If there is no result, make one!
                query = String.format("INSERT INTO \"Other_Activities\" (\"userId\",\"date\",\"activityType\",\"timeTaken\") " +
                                "VALUES (%s,%s,'Sleep',%s);",
                        SessionDataHolder.user.getId(), SessionDataHolder.getDateRequested(), 10);
                database.insertStatement(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            refreshLabels();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void gymMinusButtonClick(ActionEvent event) throws IOException {

        int gymTime;
        String query = String.format("SELECT timeTaken from Other_Activities " +
                        "WHERE (userId = %s AND date = %s AND activityType = 'Gym')",
                SessionDataHolder.user.getId(), SessionDataHolder.dateRequested);
        try {
            ResultSet rs = database.getResultSet(query);
            if (rs.next()) {
                // There is a result
                gymTime = rs.getInt(1);
                if (gymTime == 0) {
                    return;
                } else {
                    gymTime -= 10;
                    query = String.format("UPDATE Other_Activities  " +
                                    "SET timeTaken = %s " +
                                    "WHERE (userId = %s AND date = %s AND activityType = 'Gym');",
                            gymTime, SessionDataHolder.user.getId(),SessionDataHolder.getDateRequested());
                    database.insertStatement(query);
                }
            } else {
                // If there is no result, make one!
                query = String.format("INSERT INTO \"Other_Activities\" (\"userId\",\"date\",\"activityType\",\"timeTaken\") " +
                                "VALUES (%s,%s,'Gym',%s);",
                        SessionDataHolder.user.getId(), SessionDataHolder.getDateRequested(), 0);
                database.insertStatement(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            refreshLabels();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void gymPlusButtonClick(ActionEvent event) throws IOException {

        int gymTime;
        String query = String.format("SELECT timeTaken from Other_Activities " +
                        "WHERE (userId = %s AND date = %s AND activityType = 'Gym')",
                SessionDataHolder.user.getId(), SessionDataHolder.dateRequested);
        try {
            ResultSet rs = database.getResultSet(query);
            if (rs.next()) {
                // There is a result
                gymTime = rs.getInt(1);
                gymTime += 10;
                query = String.format("UPDATE Other_Activities  " +
                                "SET timeTaken = %s " +
                                "WHERE (userId = %s AND date = %s AND activityType = 'Gym');",
                        gymTime, SessionDataHolder.user.getId(),SessionDataHolder.getDateRequested());
                database.insertStatement(query);
            } else {
                // If there is no result, make one!
                query = String.format("INSERT INTO \"Other_Activities\" (\"userId\",\"date\",\"activityType\",\"timeTaken\") " +
                                "VALUES (%s,%s,'Gym',%s);",
                        SessionDataHolder.user.getId(), SessionDataHolder.getDateRequested(), 10);
                database.insertStatement(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            refreshLabels();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void resistanceExerciseButtonClick(ActionEvent event) throws IOException {
        pageSwitcherHelper.switcher(event, "../Application/ActivityLogResistance.fxml");
    }

    @FXML
    public void AerobicExerciseButtonClick(ActionEvent event) throws IOException {
        pageSwitcherHelper.switcher(event, "../Application/ActivityLogAerobic.fxml");
    }

    @FXML
    public void MentalExerciseButtonClick(ActionEvent event) throws IOException {
        pageSwitcherHelper.switcher(event, "../Application/ActivityLogMental.fxml");
    }


}
