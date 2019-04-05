package SignUp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import sample.DateHelper;
import sample.PageSwitcherHelper;
import sample.SessionDataHolder;
import sample.User;

import java.io.IOException;

public class SignUpScreenFiveController {
    private static final String TAG = "SignUpScreenFiveController: ";

    private PageSwitcherHelper pageSwitcherHelper = new PageSwitcherHelper();
    private User newUser;

    @FXML DatePicker input_dateOfBirth;

    @FXML Label confirmation_username;
    @FXML Label confirmation_email;
    @FXML Label confirmation_name;
    @FXML Label confirmation_gender;
    @FXML Label confirmation_height;
    @FXML Label confirmation_weight;
    @FXML Label confirmation_bodyFatPercentage;
    @FXML Label confirmation_dateOfBirth;


    @FXML
    public void initialize() {
        newUser = SessionDataHolder.getUser();
        System.out.println(newUser.toString());
        confirmation_username.setText(newUser.getUsername());
        confirmation_email.setText(newUser.getEmail());
        confirmation_name.setText(newUser.getFirstName() + " " + newUser.getLastName());
        confirmation_gender.setText(newUser.getGender());
        confirmation_height.setText(Double.toString(newUser.getHeight()) + "cm");
        // Fix Weight
        confirmation_weight.setText(Double.toString(newUser.getWeight()) + "kg");
        confirmation_bodyFatPercentage.setText(Double.toString(newUser.getBodyFatPercentage()) + "%");
        confirmation_dateOfBirth.setText(DateHelper.longDateToString(newUser.getDateOfBirth()));

    }

    @FXML
    Label signupStatusLabel;

    @FXML
    private void handleNextButtonAction(ActionEvent event) throws IOException {
        System.out.println(TAG + "Next button pressed");
        //Todo: SQL Query to create new record and insert into database

    }


    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        System.out.println(TAG + "Back button pressed");
        pageSwitcherHelper.switcher(event, "../SignUp/SignUpScreenFour.fxml");

    }
}
