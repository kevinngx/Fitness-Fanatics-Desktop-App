package SignUp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import sample.*;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpScreenFiveController {
    private static final String TAG = "SignUpScreenFiveController: ";

    private PageSwitcherHelper pageSwitcherHelper = new PageSwitcherHelper();
    Database database = new Database();
    private User newUser;

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
        System.out.println(TAG + "Initialized");
        newUser = SessionDataHolder.getUser();
        System.out.println(newUser.toString());
        confirmation_username.setText(newUser.getUsername());
        confirmation_email.setText(newUser.getEmail());
        confirmation_name.setText(newUser.getFirstName() + " " + newUser.getLastName());
        confirmation_gender.setText(newUser.getGender());
        confirmation_height.setText(Double.toString(newUser.getHeight()) + "cm");
        confirmation_weight.setText(Double.toString(newUser.getWeight()) + "kg");
        confirmation_bodyFatPercentage.setText(Double.toString(newUser.getBodyFatPercentage()) + "%");
        confirmation_dateOfBirth.setText(DateHelper.longDateToString(newUser.getDateOfBirth()));

    }

    @FXML
    private void handleNextButtonAction(ActionEvent event) throws IOException {
        System.out.println(TAG + "Next button pressed");
        String query = String.format("INSERT INTO \"UserData\" (\"email\",\"username\",\"password\"," +
                "\"firstName\",\"lastName\",\"gender\",\"bodyFatPercentage\",\"weight\",\"height\",\"dateOfBirth\") " +
                "VALUES ('%s','%s','%s','%s','%s','%s',%s,%s,%s,%s);",
                newUser.getEmail(), newUser.getUsername(), newUser.getPassword(), newUser.getFirstName(),
                newUser.getLastName(), newUser.getGender(), newUser.getBodyFatPercentage(),
                newUser.getWeight(), newUser.getHeight(), newUser.getDateOfBirth());
        System.out.println(TAG + "generated query: " + query);
        try {
            database.insertStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pageSwitcherHelper.switcher(event, "../SignUp/SignUpSuccess.fxml");
    }


    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        System.out.println(TAG + "Back button pressed");
        pageSwitcherHelper.switcher(event, "../SignUp/SignUpScreenFour.fxml");

    }
}
