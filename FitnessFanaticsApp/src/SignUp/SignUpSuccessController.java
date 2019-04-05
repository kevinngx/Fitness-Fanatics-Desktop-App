package SignUp;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import sample.Main;
import sample.PageSwitcherHelper;

import java.io.IOException;

public class SignUpSuccessController {

    @FXML ProgressBar inserting_data;

    @FXML Label label_progress;

    @FXML Button back_to_login_button;

    public void initialize() throws InterruptedException {
        inserting_data.setProgress(10);

        back_to_login_button.setVisible(false);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(inserting_data.progressProperty(), 0)),
                new KeyFrame(Duration.minutes(0.05), e-> {
                    label_progress.setTextFill(Color.GREEN);
                    label_progress.setText("Account Successfully Created!");
                    back_to_login_button.setVisible(true);
                }, new KeyValue(inserting_data.progressProperty(), 1))
        );
        timeline.play();
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) throws IOException {
        PageSwitcherHelper pageSwitcherHelper = new PageSwitcherHelper();
        pageSwitcherHelper.switcher(event, "../Login/LoginScreen.fxml");

    }



}
