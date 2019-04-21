package Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class HelpCholesterol {

    @FXML
    Button close_button;

    @FXML
    public void onBackSelected(ActionEvent event) throws IOException {

        // get a handle to the stage
        Stage stage = (Stage) close_button.getScene().getWindow();
        // do what you have to do
        stage.close();

    }
}
