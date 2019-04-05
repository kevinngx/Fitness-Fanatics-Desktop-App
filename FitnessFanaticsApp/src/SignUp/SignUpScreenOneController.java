package SignUp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import sample.Database;
import sample.SessionDataHolder;
import sample.PageSwitcherHelper;
import sample.User;

import java.io.IOException;
import java.sql.ResultSet;

public class SignUpScreenOneController {
    private static final String TAG = "SignUpScreenOneController: ";

    PageSwitcherHelper pageSwitcherHelper = new PageSwitcherHelper();
    Database database = new Database();
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

        // Check if passwords match.
        if (!input_passwordOne.getText().equals(input_passwordTwo.getText())) {
            signupStatusLabel.setTextFill(Color.RED);
            signupStatusLabel.setText("Passwords do not match");
            return false;
        }

        // Check if username is unique
        String query = "SELECT * FROM UserData \n" +
                "WHERE username = \"" + input_username.getText() + "\"";

        try {
            ResultSet rs = database.getResultSet(query);
            if (rs.next()) {
                System.out.println(TAG + "User found, with email: " + rs.getString(2));
                signupStatusLabel.setTextFill(Color.RED);
                signupStatusLabel.setText("Username is already taken!");
                return false;
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;
    }
}
