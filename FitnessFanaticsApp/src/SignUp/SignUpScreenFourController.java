package SignUp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import sample.DateHelper;
import sample.PageSwitcherHelper;
import sample.SessionDataHolder;
import sample.User;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class SignUpScreenFourController {
    private static final String TAG = "SignUpScreenFourController: ";

    private PageSwitcherHelper pageSwitcherHelper = new PageSwitcherHelper();
    private User newUser;

    @FXML DatePicker input_dateOfBirth;

    @FXML Label confirmation_username;
    @FXML Label confirmation_email;
    @FXML Label confirmation_firstName;
    @FXML Label confirmation_lastName;
    @FXML Label confirmation_gender;
    @FXML Label confirmation_height;
    @FXML Label confirmation_weight;
    @FXML Label confirmation_bodyFatPercentage;


    @FXML
    public void initialize() {
        newUser = SessionDataHolder.getUser();
        confirmation_username.setText(newUser.getUsername());
        confirmation_email.setText(newUser.getEmail());
        confirmation_firstName.setText(newUser.getFirstName());
        confirmation_lastName.setText(newUser.getLastName());
        confirmation_gender.setText(newUser.getGender());
        confirmation_height.setText(Double.toString(newUser.getHeight()) + "cm");
        confirmation_weight.setText(Double.toString(newUser.getWeight()) + "kg");
        confirmation_bodyFatPercentage.setText(Double.toString(newUser.getBodyFatPercentage()) + "%");

    }

    @FXML
    Label signupStatusLabel;

    @FXML
    private void handleNextButtonAction(ActionEvent event) throws IOException {
        System.out.println(TAG + "Next button pressed");
        if (input_dateOfBirth.getValue() == null) {
            signupStatusLabel.setTextFill(Color.RED);
            signupStatusLabel.setText("Please enter a DOB before proceeding");
            return;
        }
        newUser.setDateOfBirth(DateHelper.inputStringToLongDate(input_dateOfBirth.getEditor().getText()));
        SessionDataHolder.user = newUser;
        pageSwitcherHelper.switcher(event, "../SignUp/SignUpScreenFive.fxml");
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        System.out.println(TAG + "Back button pressed");
        pageSwitcherHelper.switcher(event, "../SignUp/SignUpScreenThree.fxml");

    }
}
