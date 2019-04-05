package SignUp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.PageSwitcherHelper;
import sample.SessionDataHolder;
import sample.User;

import java.io.IOException;

public class SignUpScreenTwoController {
    private static final String TAG = "SignUpScreenTwoController: ";

    private PageSwitcherHelper pageSwitcherHelper = new PageSwitcherHelper();
    private User newUser;

    @FXML TextField input_email;
    @FXML TextField input_firstName;
    @FXML TextField input_lastName;

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
    }

    @FXML
    Label signupStatusLabel;

    @FXML
    private void handleNextButtonAction(ActionEvent event) throws IOException {
        System.out.println(TAG + "Next button pressed");
        if (!checkInputs()) return;
        newUser.setEmail(input_email.getText());
        newUser.setFirstName(input_firstName.getText());
        newUser.setLastName(input_lastName.getText());
        SessionDataHolder.setUser(newUser);
        pageSwitcherHelper.switcher(event, "../SignUp/SignUpScreenThree.fxml");

    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        System.out.println(TAG + "Back button pressed");
        pageSwitcherHelper.switcher(event, "../SignUp/SignUpScreenOne.fxml");

    }

    // returns true if input is valid
    private boolean checkInputs() {
        // Check if fields are empty
        if (input_email.getText().isEmpty() || input_firstName.getText().isEmpty() || input_lastName.getText().isEmpty()) {
            signupStatusLabel.setTextFill(Color.RED);
            signupStatusLabel.setText("Please fill out all fields");
            return false;
        }

        // Check if email is unique
        return true;
    }


}
