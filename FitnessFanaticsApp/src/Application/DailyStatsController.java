package Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import sample.DateHelper;
import sample.PageSwitcherHelper;
import sample.SessionDataHolder;
import sample.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DailyStatsController extends ApplicationBase {

    @FXML private DatePicker date_selection;

    @FXML
    public void initialize() throws SQLException {
        setupHeaders("Daily Stats");
    }

    @FXML
    public void onGetDataButtonClick(ActionEvent event) throws IOException {
        if (date_selection.getValue() == null) return;
        SessionDataHolder.dateRequested = DateHelper.inputStringToLongDate(date_selection.getEditor().getText());
        //TODO: Change '1' and '20190405 to
        String query = String.format("SELECT * FROM DailyData WHERE userId = %s AND date = %s", SessionDataHolder.getUser().getId(), SessionDataHolder.getDateRequested());
        //SessionDataHolder.getUser().getId(), input

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

}
