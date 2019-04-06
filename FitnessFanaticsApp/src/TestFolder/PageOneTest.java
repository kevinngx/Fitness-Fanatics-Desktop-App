package TestFolder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import sample.SessionDataHolder;

public class PageOneTest extends BaseTest {

    @FXML
    private PieChart test_chart;


    @FXML
    public void initialize() {
        page_name.setText("Page One");

        test_chart.setData(getChartData());
    }

    private ObservableList<PieChart.Data> getChartData() {
        ObservableList<PieChart.Data> answer = FXCollections.observableArrayList();
        answer.addAll(new PieChart.Data("ElementOne", 20),
                new PieChart.Data("ElementTwo", 20));
        return answer;
    }
}
