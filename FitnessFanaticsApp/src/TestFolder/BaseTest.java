package TestFolder;

import Login.LoginScreenController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.PageSwitcherHelper;

import java.io.IOException;

public class BaseTest {

    PageSwitcherHelper pageSwitcherHelper = new PageSwitcherHelper();

    @FXML
    Label page_name;

    @FXML
    private void goToPageOneButton(ActionEvent event) throws IOException {
        pageSwitcherHelper.switcher(event, "../TestFolder/PageOneTest.fxml");
    }

    @FXML
    private void goToPageTwoButton(ActionEvent event) throws IOException {
        pageSwitcherHelper.switcher(event, "../TestFolder/PageTwoTest.fxml");
    }

    @FXML
    private void goToPageThreeButton(ActionEvent event) throws IOException {
        pageSwitcherHelper.switcher(event, "../TestFolder/PageThreeTest.fxml");
    }

}

