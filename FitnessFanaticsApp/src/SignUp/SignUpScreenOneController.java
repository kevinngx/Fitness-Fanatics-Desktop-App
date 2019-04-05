package SignUp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import sample.SessionDataHolder;
import sample.PageSwitcherHelper;
import sample.User;

import java.io.IOException;

public class SignUpScreenOneController {
    private static final String TAG = "SignUpScreenOneControll: ";

    // Instantiate the PageSwitchHelper class
    PageSwitcherHelper pageSwitcherHelper = new PageSwitcherHelper();
    User newUser = new User();

    @FXML TextField input_username;
    @FXML PasswordField input_passwordOne;
    @FXML PasswordField input_passwordTwo;

    @FXML
    Label signupStatusLabel;

    @FXML
    private void handleNextButtonAction(ActionEvent event) throws IOException {
        System.out.println(TAG + "Next button pressed");
        if (!checkInputs()) return;
        newUser.setUsername(input_username.getText());
        newUser.setPassword(input_passwordOne.getText());
        SessionDataHolder.setUser(newUser);
        pageSwitcherHelper.switcher(event, "../SignUp/SignUpScreenTwo.fxml");

    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        System.out.println(TAG + "Back button pressed");

    }

    // returns true if input is valid
    private boolean checkInputs() {
        // Check if fields are empty
        if (input_username.getText().isEmpty() || input_passwordOne.getText().isEmpty() || input_passwordTwo.getText().isEmpty()) {
            signupStatusLabel.setTextFill(Color.RED);
            signupStatusLabel.setText("Please fill out all fields");
            return false;
        }

        // Check if username is unique
        return true;
    }
}
