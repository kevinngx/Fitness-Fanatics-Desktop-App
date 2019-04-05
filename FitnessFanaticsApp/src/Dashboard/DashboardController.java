package Dashboard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import sample.PageSwitcherHelper;

import java.io.IOException;

public class DashboardController {

    @FXML
    public void backToLogin(MouseEvent event) throws IOException {
        PageSwitcherHelper pageSwitcherHelper = new PageSwitcherHelper();
        pageSwitcherHelper.switcher(event, "../Login/LoginScreen.fxml");
    }
}
