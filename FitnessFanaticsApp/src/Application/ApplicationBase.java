package Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sample.Database;
import sample.PageSwitcherHelper;
import sample.SessionDataHolder;

import java.io.IOException;

public class ApplicationBase {

    PageSwitcherHelper pageSwitcherHelper = new PageSwitcherHelper();

    Database database = new Database();

    @FXML
    private Label page_title;

    @FXML Label current_user;


    @FXML
    public void onLogoutButtonClick(ActionEvent event) throws IOException {
        pageSwitcherHelper.switcher(event, "../Login/LoginScreen.fxml");
    }

    @FXML
    public void onHomeButtonClick(ActionEvent event) throws IOException {
        pageSwitcherHelper.switcher(event, "../Application/Dashboard.fxml");
    }

    @FXML
    public void onDashboardButtonClick(MouseEvent event) throws IOException {
        pageSwitcherHelper.switcher(event, "../Application/Dashboard.fxml");
    }

    @FXML
    public void onDailyStatsButtonClick(MouseEvent event) throws IOException {
        pageSwitcherHelper.switcher(event, "../Application/DailyStats.fxml");
    }

    @FXML
    public void onFoodDiaryButtonClick(MouseEvent event) throws IOException {
        pageSwitcherHelper.switcher(event, "../Application/FoodDiary.fxml");
    }

    @FXML
    public void onActivityLogButtonClick(MouseEvent event) throws IOException {
        pageSwitcherHelper.switcher(event, "../Application/ActivityLog.fxml");
    }

    @FXML
    public void onHealthCheckClick(MouseEvent event) throws IOException {
        pageSwitcherHelper.switcher(event, "../Application/HealthCheck.fxml");
    }

    @FXML
    public void onAccountSettingsClick(MouseEvent event) throws IOException {
        pageSwitcherHelper.switcher(event, "../Application/AccountSettings.fxml");
    }

    public void setupHeaders(String pageName) {
        current_user.setText(SessionDataHolder.getUser().getFirstName() + " " +SessionDataHolder.getUser().getLastName());
        page_title.setText(pageName);
    }

}
