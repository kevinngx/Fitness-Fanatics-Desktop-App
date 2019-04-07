package Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import sample.DailyData;
import sample.SessionDataHolder;

import java.io.IOException;
import java.sql.SQLException;

public class DailyStatsNewDataController extends DailyStatsController {

    private static final String TAG = "DailyStatsNewDataContro: ";

    @FXML private Spinner input_weight;
    @FXML private Spinner input_height;
    @FXML private Spinner input_bodyFatPercentage;
    @FXML private Spinner input_stepCount; // Int
    @FXML private Spinner input_stairCount; // Int
    @FXML private Spinner input_restingHeartRate; // Int

    @FXML
    public void initialize() throws SQLException {
        setupHeaders("Daily Stats - Add New Data");

        // Setup spinners
        SpinnerValueFactory<Double> heightValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 300.0, SessionDataHolder.getUser().getHeight());
        this.input_height.setValueFactory(heightValueFactory);

        SpinnerValueFactory<Double> weightValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 300.0, SessionDataHolder.getUser().getWeight());
        this.input_weight.setValueFactory(weightValueFactory);

        SpinnerValueFactory<Double> bodyFatPercentageValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, SessionDataHolder.getUser().getBodyFatPercentage());
        this.input_bodyFatPercentage.setValueFactory(bodyFatPercentageValueFactory);

        SpinnerValueFactory<Integer> stepCountValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 10000);
        this.input_stepCount.setValueFactory(stepCountValueFactory);

        SpinnerValueFactory<Integer> stairCountValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 100);
        this.input_stairCount.setValueFactory(stairCountValueFactory);

        SpinnerValueFactory<Integer> heartRateFatPercentageValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 80);
        this.input_restingHeartRate.setValueFactory(heartRateFatPercentageValueFactory);
    }
//
//    newUser.setGender(selectedGender.getText());
//    newUser.setHeight((Double) input_height.getValue());
//    newUser.setWeight((Double) input_weight.getValue());

    @FXML
    public void onAddRecordButtonClick(ActionEvent event) throws IOException, SQLException {
        System.out.println(TAG + "Adding record...");
        DailyData newDailyDataEntry = new DailyData(
                SessionDataHolder.getUser().getId(),
                SessionDataHolder.getDateRequested(),
                (Integer) input_stepCount.getValue(),
                (Integer) input_stairCount.getValue(),
                (Integer) input_restingHeartRate.getValue(),
                (Double) input_height.getValue(),
                (Double) input_weight.getValue(),
                (Double) input_bodyFatPercentage.getValue()
        );
        System.out.println(newDailyDataEntry.toString());
        newDailyDataEntry.createDatabaseRecord();
        SessionDataHolder.dailyData = newDailyDataEntry;
        pageSwitcherHelper.switcher(event, "../Application/DailyStatsDisplay.fxml");
    }

}
