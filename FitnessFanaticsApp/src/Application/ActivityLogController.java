package Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import sample.DateHelper;
import sample.SessionDataHolder;

import java.io.IOException;
import java.sql.ResultSet;

public class ActivityLogController extends ApplicationBase {

    @FXML private DatePicker date_selection;

    @FXML
    public void initialize() {
        setupHeaders("Activity Log");
    }

    @FXML
    public void onGetDataButtonClick(ActionEvent event) throws IOException {
        if (date_selection.getValue() == null) return;
        SessionDataHolder.dateRequested = DateHelper.inputStringToLongDate(date_selection.getEditor().getText());
        pageSwitcherHelper.switcher(event, "../Application/ActivityLogBase.fxml");
    }

}
