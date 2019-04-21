package Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sample.Database;
import sample.DateHelper;
import sample.PageSwitcherHelper;
import sample.SessionDataHolder;

import java.io.IOException;
import java.sql.ResultSet;
import java.time.LocalDate;

public class ApplicationBase {

    PageSwitcherHelper pageSwitcherHelper = new PageSwitcherHelper();
    Database database = new Database();

    @FXML private Label page_title;
    @FXML Label current_user;

//-----------------------------------------------------------------

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

        String query = String.format("SELECT * FROM DailyData WHERE userId = %s AND date = %s", SessionDataHolder.getUser().getId(), SessionDataHolder.getDateRequested());

        try {
            ResultSet rs = database.getResultSet(query);
            System.out.println("Result obtained!");
            if (rs.next()) {
                System.out.println("Navigating to daily data display page");
                SessionDataHolder.dailyData.setUserId(rs.getInt(1));
                SessionDataHolder.dailyData.setDate(rs.getLong(2));
                SessionDataHolder.dailyData.setStepCount(rs.getInt(3));
                SessionDataHolder.dailyData.setStairCount(rs.getInt(4));
                SessionDataHolder.dailyData.setRestingHeartRate(rs.getInt(5));
                SessionDataHolder.dailyData.setWeight(rs.getInt(6));
                SessionDataHolder.dailyData.setBodyFatPercentage(rs.getInt(7));
                SessionDataHolder.dailyData.setHeight(rs.getInt(8));
                System.out.println("Record pulled: " + SessionDataHolder.dailyData.toString());

                pageSwitcherHelper.switcher(event, "../Application/DailyStatsDisplay.fxml");
            } else {
                System.out.println("No data found for date, navigating to new daily data page");
                pageSwitcherHelper.switcher(event, "../Application/DailyStatsNewData.fxml");
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    @FXML
    public void onFoodDiaryButtonClick(MouseEvent event) throws IOException {
        pageSwitcherHelper.switcher(event, "../Application/FoodDiaryDisplay.fxml");
    }

    @FXML
    public void onActivityLogButtonClick(MouseEvent event) throws IOException {
        pageSwitcherHelper.switcher(event, "../Application/ActivityLogBase.fxml");
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
