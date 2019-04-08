package Application;

import com.sun.org.apache.bcel.internal.generic.Select;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.paint.Color;
import sample.AerobicExercise;
import sample.Database;
import sample.DateHelper;
import sample.SessionDataHolder;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountSettingsController extends ApplicationBase {

    Database database = new Database();

    @FXML Label value_userId;
    @FXML Label value_username;
    @FXML Label value_email;
    @FXML Label value_weight;
    @FXML Label value_height;
    @FXML Label value_bodyFatPercentage;
    @FXML Label value_name;
    @FXML Label value_gender;
    @FXML Label value_dob;

    @FXML Spinner input_weight;
    @FXML Spinner input_height;
    @FXML Spinner input_bodyFatPercentage;

//-----------------------------------------------------------------

    @FXML Label value_stepCount;
    @FXML Label value_stairCount;
    @FXML Label value_caloricIntake;
    @FXML Label value_sleepTime;
    @FXML Label value_gymTime;

    @FXML Spinner input_stepCount;
    @FXML Spinner input_stairCount;
    @FXML Spinner input_caloricIntake;
    @FXML Spinner input_sleepTime;
    @FXML Spinner input_gymTime;

    @FXML PasswordField input_currentPassword;
    @FXML PasswordField input_newPasswordOne;
    @FXML PasswordField input_newPasswordTwo;

    @FXML Label label_passwordMessage;


    @FXML
    public void initialize() {
        setupHeaders("Account Settings");
        //TODO: Reset user data

        value_userId.setText(Integer.toString(SessionDataHolder.user.getId()));
        value_username.setText(SessionDataHolder.user.getUsername());
        value_email.setText(SessionDataHolder.user.getEmail());
        value_name.setText(String.format(SessionDataHolder.user.getFirstName() + " " + SessionDataHolder.user.getLastName()));
        value_dob.setText(DateHelper.longDateToString(SessionDataHolder.user.getDateOfBirth()));
        refreshDetailValues();

        //setup spinners
        SpinnerValueFactory<Double> heightValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 300.0, SessionDataHolder.user.getHeight());
        this.input_height.setValueFactory(heightValueFactory);

        SpinnerValueFactory<Double> weightValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 300.0, SessionDataHolder.user.getWeight());
        this.input_weight.setValueFactory(weightValueFactory);

        SpinnerValueFactory<Double> bodyFatPercentageValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, SessionDataHolder.user.getBodyFatPercentage());
        this.input_bodyFatPercentage.setValueFactory(bodyFatPercentageValueFactory);

        refreshGoalValues();

    }

    private void refreshDetailValues() {
        value_weight.setText(Double.toString(SessionDataHolder.user.getWeight()) + "kg");
        value_height.setText(Double.toString(SessionDataHolder.user.getHeight()) + "cm");
        value_bodyFatPercentage.setText(Double.toString(SessionDataHolder.user.getBodyFatPercentage()) + "%");
    }

    private void refreshGoalValues() {
        String query = String.format("SELECT * FROM Goals WHERE userId = %s", SessionDataHolder.user.getId());
        try {
            ResultSet rs = database.getResultSet(query);
            if (rs.next()) {
                setGoalValues(
                rs.getDouble(2),
                rs.getDouble(3),
                rs.getDouble(4),
                rs.getDouble(5),
                rs.getDouble(6)
                );
            } else {
                query = String.format("INSERT INTO Goals (userId) VALUES (%s)", SessionDataHolder.user.getId());
                database.insertStatement(query);
                setGoalValues(0,
                        0,
                        0,
                        0,
                        0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void setGoalValues(double stepCount, double stairCount, double caloricIntake, double sleepTime, double gymTime) {
        value_stepCount.setText(Double.toString(stepCount) + " steps");
        value_stairCount.setText(Double.toString(stairCount) + " stair steps");
        value_caloricIntake.setText(Double.toString(caloricIntake) + " kcals");
        value_sleepTime.setText(Double.toString(sleepTime) + " mins");
        value_gymTime.setText(Double.toString(gymTime) + " mins");

        SpinnerValueFactory<Double> stepCountValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000000000.0, stepCount);
        this.input_stepCount.setValueFactory(stepCountValueFactory);

        SpinnerValueFactory<Double> stairCountValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000000000, stairCount);
        this.input_stairCount.setValueFactory(stairCountValueFactory);

        SpinnerValueFactory<Double> caloricIntakeValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000000000, caloricIntake);
        this.input_caloricIntake.setValueFactory(caloricIntakeValueFactory);

        SpinnerValueFactory<Double> sleepTimeValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000000000, sleepTime);
        this.input_sleepTime.setValueFactory(sleepTimeValueFactory);

        SpinnerValueFactory<Double> gymTimeValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000000000, gymTime);
        this.input_gymTime.setValueFactory(gymTimeValueFactory);

    }

//-----------------------------------------------------------------

    @FXML
    public void updatePasswordButtonClick(ActionEvent event) throws IOException, SQLException {
        System.out.println(SessionDataHolder.user.toString());
        if (!checkValidPassword()) return;
        String newPassword = input_newPasswordOne.getText();
        String query = String.format("UPDATE UserData " +
                        "SET password = '%s' " +
                        "WHERE (userId = %s);",
                newPassword,
                SessionDataHolder.user.getId());
        database.insertStatement(query);
        label_passwordMessage.setTextFill(Color.GREEN);
        label_passwordMessage.setText("Password Changed");
        SessionDataHolder.user.setPassword(newPassword);
        System.out.println(SessionDataHolder.user.toString());
    }

    private boolean checkValidPassword() {

        if (input_currentPassword.getText().isEmpty() || input_newPasswordOne.getText().isEmpty() || input_newPasswordTwo.getText().isEmpty()) {
            label_passwordMessage.setTextFill(Color.RED);
            label_passwordMessage.setText("Please fill all password fields");
            return false;
        } else if (!input_currentPassword.getText().equals(SessionDataHolder.user.getPassword())) {
            label_passwordMessage.setTextFill(Color.RED);
            label_passwordMessage.setText("Incorrect current password");
            return false;
        } else if (input_newPasswordOne.getText().equals(input_newPasswordTwo.getText())) {
            label_passwordMessage.setTextFill(Color.RED);
            label_passwordMessage.setText("New passwords do not match");
        }

        return true;
    }

    @FXML
    public void updateWeightButtonClick(ActionEvent event) throws IOException, SQLException {
        String newValue = input_weight.getValue().toString();
        String query = String.format("UPDATE UserData " +
                        "SET weight = %s " +
                        "WHERE (userId = %s);",
                newValue,
                SessionDataHolder.user.getId());
        database.insertStatement(query);
        SessionDataHolder.user.setWeight(Double.parseDouble(newValue));
        value_weight.setText(newValue + "kg");
    }

    @FXML
    public void updateHeightButtonClick(ActionEvent event) throws IOException, SQLException {
        String newValue = input_height.getValue().toString();
        String query = String.format("UPDATE UserData " +
                        "SET height = %s " +
                        "WHERE (userId = %s);",
                newValue,
                SessionDataHolder.user.getId());
        database.insertStatement(query);
        SessionDataHolder.user.setHeight(Double.parseDouble(newValue));
        value_height.setText(newValue + "cm");
    }

    @FXML
    public void updateBodyFatPercentageButtonClick(ActionEvent event) throws IOException, SQLException {
        String newValue = input_bodyFatPercentage.getValue().toString();
        String query = String.format("UPDATE UserData " +
                        "SET bodyFatPercentage = %s " +
                        "WHERE (userId = %s);",
                newValue,
                SessionDataHolder.user.getId());
        database.insertStatement(query);
        SessionDataHolder.user.setBodyFatPercentage(Double.parseDouble(newValue));
        value_bodyFatPercentage.setText(newValue + "%");
    }

    @FXML
    public void updateStepCountButtonClick(ActionEvent event) throws IOException, SQLException {
        String newValue = input_stepCount.getValue().toString();
        String query = String.format("UPDATE Goals " +
                                "SET stepCount = %s " +
                                "WHERE (userId = %s);",
                newValue,
                SessionDataHolder.user.getId());
        database.insertStatement(query);
        value_stepCount.setText(newValue + " steps");
    }

    @FXML
    public void updateStairCountButtonClick(ActionEvent event) throws IOException, SQLException {
        String newValue = input_stairCount.getValue().toString();
        System.out.println("Button Pressed");
        String query = String.format("UPDATE Goals " +
                        "SET stairCount = %s " +
                        "WHERE (userId = %s);",
                newValue,
                SessionDataHolder.user.getId());
        database.insertStatement(query);
        value_stairCount.setText(newValue + " stair steps");
    }

    @FXML
    public void updateCaloricIntakeButtonClick(ActionEvent event) throws IOException, SQLException {
        String newValue = input_caloricIntake.getValue().toString();
        String query = String.format("UPDATE Goals " +
                        "SET caloricIntake = %s " +
                        "WHERE (userId = %s);",
                newValue,
                SessionDataHolder.user.getId());
        database.insertStatement(query);
        value_caloricIntake.setText(newValue + "kcals");
    }

    @FXML
    public void updateSleepTimeButtonClick(ActionEvent event) throws IOException, SQLException {
        String newValue = input_sleepTime.getValue().toString();
        String query = String.format("UPDATE Goals " +
                        "SET sleepTime = %s " +
                        "WHERE (userId = %s);",
                newValue,
                SessionDataHolder.user.getId());
        database.insertStatement(query);
        value_sleepTime.setText(newValue + " mins");
    }

    @FXML
    public void updateGymTimeButtonClick(ActionEvent event) throws IOException, SQLException {
        String newValue = input_gymTime.getValue().toString();
        String query = String.format("UPDATE Goals " +
                        "SET gymTime = %s " +
                        "WHERE (userId = %s);",
                newValue,
                SessionDataHolder.user.getId());
        database.insertStatement(query);
        value_gymTime.setText(newValue + " mins");
    }

}
