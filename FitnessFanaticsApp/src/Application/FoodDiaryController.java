package Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import sample.DateHelper;
import sample.SessionDataHolder;

import java.io.IOException;
import java.sql.ResultSet;
import java.time.LocalDate;

public class FoodDiaryController extends ApplicationBase {

    @FXML DatePicker date_selection;

    @FXML
    public void initialize() {
        setupHeaders("Food Diary");
        date_selection.setValue(LocalDate.now());
    }

    @FXML
    public void onGetDataButtonClick(ActionEvent event) throws IOException {
        if (date_selection.getValue() == null) return;
        SessionDataHolder.dateRequested = DateHelper.inputStringToLongDate(date_selection.getEditor().getText());
        pageSwitcherHelper.switcher(event, "../Application/FoodDiaryDisplay.fxml");
    }

}
