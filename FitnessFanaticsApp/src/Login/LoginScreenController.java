package Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.Database;
import sample.PageSwitcherHelper;
import sample.SessionDataHolder;
import sample.User;

import java.io.IOException;
import java.sql.ResultSet;

public class LoginScreenController {
    private static final String TAG = "LoginScreenController: ";

    enum Login_Status {
        EMPTY_USERNAME, EMPTY_PASSWORD, USER_NOT_FOUND, INCORRECT_PASSWORD, LOGIN_SUCCESSFUL
    }

    PageSwitcherHelper pageSwitcherHelper = new PageSwitcherHelper();
    Database database = new Database();
    User currentUser;

    @FXML TextField input_username;
    @FXML PasswordField input_password;
    @FXML Label loginStatusLabel;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws IOException {

        // Cancels action if any required fields are empty
        if (!checkInputs()) return;

        if (findUser(input_username.getText())) {
            if (authenticateUser(input_password.getText())) {
                updateLabel(Login_Status.LOGIN_SUCCESSFUL);
                pageSwitcherHelper.switcher(event, "../Dashboard/DashboardLandingPage.fxml");
            } else {
                updateLabel(Login_Status.INCORRECT_PASSWORD);
            }
        } else {
            updateLabel(Login_Status.USER_NOT_FOUND);
        }

    }

    private boolean findUser(String inputUsername) {
        String query = "SELECT * FROM UserData \n" +
                "WHERE username = \"" + inputUsername + "\"";

        System.out.println(TAG + "Finding user...");
        try {
            ResultSet rs = database.getResultSet(query);
            if (rs.next()) {
                System.out.println(TAG + "User found!");
                currentUser = new User(
                        Integer.parseInt(rs.getString(1)), // userId
                        rs.getString(2), // email
                        rs.getString(3), // username
                        rs.getString(4), // password
                        rs.getString(5), // firstName
                        rs.getString(6), // lastName
                        rs.getString(7), // gender
                        Double.parseDouble(rs.getString(8)), // bodyFatPercentage
                        Double.parseDouble(rs.getString(9)), // weight
                        Double.parseDouble(rs.getString(10)), // weight
                        Long.parseLong(rs.getString(11)) // dateOfBirth

                );
                System.out.println(currentUser.toString());
                return true;
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(TAG + "No user found");
        return false;
    }

    private boolean authenticateUser(String inputPassword) {
        if (inputPassword.equals(currentUser.getPassword())) {
            System.out.println(TAG + "User authenticated, signing in...");
            SessionDataHolder.user = currentUser;
            return true;
        }
        System.out.println(TAG + "Incorrect Password");
        return false;
    }

    @FXML
    private void handleSignUpButtonAction(ActionEvent event) throws IOException {
        pageSwitcherHelper.switcher(event, "../SignUp/SignUpScreenOne.fxml");
    }

    // returns true if input is valid
    private boolean checkInputs() {
        if (input_username.getText().isEmpty()) {
            updateLabel(Login_Status.EMPTY_USERNAME);
            return false;
        } else if (input_password.getText().isEmpty()) {
            updateLabel(Login_Status.EMPTY_PASSWORD);
            return false;
        }
        return true;
    }

    private void updateLabel(Login_Status loginStatus) {
        switch (loginStatus) {

            case EMPTY_USERNAME:
                loginStatusLabel.setTextFill(Color.BLACK);
                loginStatusLabel.setText("Please enter a username");
                break;

            case EMPTY_PASSWORD:
                loginStatusLabel.setTextFill(Color.BLACK);
                loginStatusLabel.setText("Please enter a password");
                break;

            case USER_NOT_FOUND:
                loginStatusLabel.setTextFill(Color.RED);
                loginStatusLabel.setText("User not found");
                break;

            case INCORRECT_PASSWORD:
                loginStatusLabel.setTextFill(Color.RED);
                loginStatusLabel.setText("Incorrect password");
                break;

            case LOGIN_SUCCESSFUL:
                loginStatusLabel.setTextFill(Color.GREEN);
                loginStatusLabel.setText("Login Successful, signing in");
                break;
        }
    }

}
