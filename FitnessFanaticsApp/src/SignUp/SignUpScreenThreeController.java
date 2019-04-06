package SignUp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import sample.PageSwitcherHelper;
import sample.SessionDataHolder;
import sample.User;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SignUpScreenThreeController {
    private static final String TAG = "SignUpScreenThreeController: ";

    private PageSwitcherHelper pageSwitcherHelper = new PageSwitcherHelper();
    private User newUser;

    @FXML ToggleGroup input_gender;
    @FXML Spinner input_height;
    @FXML Spinner input_weight;
    @FXML Spinner input_bodyFatPercentage;

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
        //setup spinners
        SpinnerValueFactory<Double> heightValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 300.0, 150.0);
        this.input_height.setValueFactory(heightValueFactory);

        SpinnerValueFactory<Double> weightValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 300.0, 75.0);
        this.input_weight.setValueFactory(weightValueFactory);

        SpinnerValueFactory<Double> bodyFatPercentageValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, 20.0);
        this.input_bodyFatPercentage.setValueFactory(bodyFatPercentageValueFactory);

        newUser = SessionDataHolder.getUser();
        confirmation_username.setText(newUser.getUsername());
        confirmation_email.setText(newUser.getEmail());
        confirmation_firstName.setText(newUser.getFirstName());
        confirmation_lastName.setText(newUser.getLastName());
    }

    @FXML
    Label signupStatusLabel;

    @FXML
    private void handleNextButtonAction(ActionEvent event) throws IOException {
        System.out.println(TAG + "Next button pressed");
        RadioButton selectedGender = (RadioButton) input_gender.getSelectedToggle();
        newUser.setGender(selectedGender.getText());
        newUser.setHeight((Double) input_height.getValue());
        newUser.setWeight((Double) input_weight.getValue());
        newUser.setBodyFatPercentage((Double) input_bodyFatPercentage.getValue());
        pageSwitcherHelper.switcher(event, "../SignUp/SignUpScreenFour.fxml");
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        System.out.println(TAG + "Back button pressed");
        pageSwitcherHelper.switcher(event, "../SignUp/SignUpScreenTwo.fxml");

    }

    @FXML
    private void handleHelpButtonClick(MouseEvent event) throws IOException {
        try {
            Desktop.getDesktop().browse(new URI("https://www.calculator.net/body-fat-calculator.html"));
        } catch (IOException e1) {
                e1.printStackTrace();
        } catch (URISyntaxException e1) {
                e1.printStackTrace();
        }
    }
}
