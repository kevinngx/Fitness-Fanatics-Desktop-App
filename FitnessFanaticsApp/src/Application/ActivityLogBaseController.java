package Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import sample.DateHelper;
import sample.SessionDataHolder;

import java.io.IOException;

public class ActivityLogBaseController extends ActivityLogController {

    enum Activity_Type {PHYSICAL, AEROBIC, MENTAL, GYM, SLEEP}

    @FXML Label value_resistanceExerciseTime;
    @FXML Label value_aerobicExerciseTime;
    @FXML Label value_mentalExerciseTime;
    @FXML Label value_sleepTime;
    @FXML Label value_gymTime;


    @FXML
    public void initialize() {
        setupHeaders("Activity Log");
        refreshLabels();
    }

    public void refreshLabels() {
        value_aerobicExerciseTime.setText(Double.toString(getExerciseTime(Activity_Type.PHYSICAL)));

    }

    private double getExerciseTime(Activity_Type activityType) {
        double totalTime = 0;
        //TODO: Implement this
        return totalTime;
    }


    @FXML
    public void sleepMinusButtonClick(ActionEvent event) throws IOException {

    }

    @FXML
    public void sleepPlusButtonClick(ActionEvent event) throws IOException {

    }

    @FXML
    public void gymMinusButtonClick(ActionEvent event) throws IOException {

    }

    @FXML
    public void gymPlusButtonClick(ActionEvent event) throws IOException {

    }

    @FXML
    public void physicalExerciseButtonClick(ActionEvent event) throws IOException {

    }

    @FXML
    public void AerobicExerciseButtonClick(ActionEvent event) throws IOException {

    }


}
