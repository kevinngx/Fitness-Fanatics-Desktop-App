package Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sample.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class HealthCheckController extends ApplicationBase {

    ArrayList<HealthCheck> dataSet;

    @FXML Label label_warningMessage;

    @FXML Label value_date;
    @FXML DatePicker input_date;

    @FXML Spinner input_cholesterolLevel;
    @FXML Spinner input_bloodPressure;
    @FXML Spinner input_bloodSugar;

    @FXML Label value_cholesterolLevel;
    @FXML Label value_bloodPressure;
    @FXML Label value_bloodSugar;

    @FXML Rectangle rectangle_cholesterol;
    @FXML Rectangle rectangle_bloodPressure;
    @FXML Rectangle rectangle_bloodSugar;

    @FXML TextArea value_doctorsComment;
    @FXML TextArea input_doctorsComment;

    @FXML TableColumn<HealthCheck, String> column_date;
    @FXML TableColumn<HealthCheck, String> column_cholesterolLevel;
    @FXML TableColumn<HealthCheck, String> column_bloodPressure;
    @FXML TableColumn<HealthCheck, String> column_bloodSugar;
    @FXML TableColumn<HealthCheck, String> column_doctorsComment;

    @FXML TableView<HealthCheck> table_summary;

//-----------------------------------------------------------------

    @FXML
    public void initialize() {
        setupHeaders("Health Check");
        setupSpinners();
        refreshData();
    }

    private void refreshData() {
        input_date.setValue(LocalDate.now());
        dataSet = pullData();

        System.out.println("Data pulled: ");
        for (int i = 0; i < dataSet.size(); i++) {
            System.out.println(dataSet.get(i).toString());
        }
        setLatestResults();
        populateTable();

    }

    private void populateTable() {
        column_date.setCellValueFactory(new PropertyValueFactory<>("dateString"));
        column_cholesterolLevel.setCellValueFactory(new PropertyValueFactory<>("cholesterolLevel"));
        column_bloodPressure.setCellValueFactory(new PropertyValueFactory<>("bloodPressure"));
        column_bloodSugar.setCellValueFactory(new PropertyValueFactory<>("bloodSugar"));
        column_doctorsComment.setCellValueFactory(new PropertyValueFactory<>("doctorsComment"));

        ObservableList<HealthCheck> data = FXCollections.observableArrayList();
        data.addAll(dataSet);

        table_summary.setItems(data);
    }

    private void setupSpinners() {
        SpinnerValueFactory<Double> cholesterolCountValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000, 200);
        this.input_cholesterolLevel.setValueFactory(cholesterolCountValueFactory);

        SpinnerValueFactory<Double> bloodPressureValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000, 120);
        this.input_bloodPressure.setValueFactory(bloodPressureValueFactory);

        SpinnerValueFactory<Double> bloodSugarIntakeValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000, 125);
        this.input_bloodSugar.setValueFactory(bloodSugarIntakeValueFactory);
    }

    private void setLatestResults() {
        if (dataSet.size() > 0) {
            value_date.setText(dataSet.get(0).getDateString());
            value_cholesterolLevel.setText(Double.toString(dataSet.get(0).getCholesterolLevel()) + "mg/dL");
            value_bloodPressure.setText(Double.toString(dataSet.get(0).getBloodPressure()) + "mmHg");
            value_bloodSugar.setText(Double.toString(dataSet.get(0).getBloodSugar()) + "mmol/L");
            value_doctorsComment.setText(dataSet.get(0).getDoctorsComment());
            setColorScales();
        } else {
            value_date.setText("No Record");
            value_cholesterolLevel.setText("");
            value_bloodPressure.setText("");
            value_bloodSugar.setText("");
            value_doctorsComment.setText("");
        }
    }

    private void setColorScales() {
        // Cholesterol Level
        if (dataSet.get(0).getCholesterolLevel() < 200) {
            rectangle_cholesterol.setFill(Color.GREEN);
        } else if (dataSet.get(0).getCholesterolLevel() < 240) {
            rectangle_cholesterol.setFill(Color.YELLOW);
        } else {
            rectangle_cholesterol.setFill(Color.RED);
        }

        // Blood Pressure
        if (dataSet.get(0).getBloodPressure() < 120) {
            rectangle_bloodPressure.setFill(Color.GREEN);
        } else if (dataSet.get(0).getBloodPressure() < 129) {
            rectangle_bloodPressure.setFill(Color.YELLOWGREEN);
        } else if (dataSet.get(0).getBloodPressure() < 139) {
            rectangle_bloodPressure.setFill(Color.YELLOW);
        } else if(dataSet.get(0).getBloodPressure() < 180) {
            rectangle_bloodPressure.setFill(Color.ORANGE);
        } else {
            rectangle_bloodPressure.setFill(Color.RED);
        }

        // Blood Sugar
        if (dataSet.get(0).getBloodSugar() < 60 || dataSet.get(0).getBloodPressure() > 200) {
            rectangle_bloodSugar.setFill(Color.RED);
        } else if (dataSet.get(0).getBloodSugar() < 100 || dataSet.get(0).getBloodPressure() > 150) {
            rectangle_bloodSugar.setFill(Color.ORANGE);
        } else {
            rectangle_bloodSugar.setFill(Color.GREEN);
        }

    }

    private ArrayList<HealthCheck> pullData() {
        String query = String.format("select * from Health_Check  " +
                "WHERE userId = %s ORDER BY date desc",
                SessionDataHolder.user.getId());
        ArrayList<HealthCheck> dataSet = new ArrayList<>();

        try {
            ResultSet rs = database.getResultSet(query);
            while (rs.next()) {
                System.out.println("Adding another row");
                dataSet.add(new HealthCheck(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getLong(3),
                        rs.getDouble(4),
                        rs.getDouble(5),
                        rs.getDouble(6),
                        rs.getString(7)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSet;
    }

//-----------------------------------------------------------------

    @FXML
    public void onDeleteSelected(ActionEvent event) {

        System.out.println("Deleting Field");

        try {
            HealthCheck selectedItem = table_summary.getSelectionModel().getSelectedItem();
            System.out.println(selectedItem.toString());

            String query = String.format("DELETE from Health_Check where reportId = %s",
                    selectedItem.getReportId());
            database.insertStatement(query);
            label_warningMessage.setText("Row successfully deleted");
            label_warningMessage.setTextFill(Color.GREEN);
            refreshData();
        } catch (Exception e) {
            System.out.println("ERROR");
            label_warningMessage.setText("Please select a row");
            label_warningMessage.setTextFill(Color.RED);
        }
    }

    @FXML
    public void onAddNewButtonClick(ActionEvent event) throws IOException {

        if (input_date.getValue() == null) {
            label_warningMessage.setTextFill(Color.RED);
            label_warningMessage.setText("Missing a date");
            return;
        }


        HealthCheck newRecord = new HealthCheck(
                0,
                SessionDataHolder.getUser().getId(),
                DateHelper.inputStringToLongDate(input_date.getEditor().getText()),
                (Double) input_cholesterolLevel.getValue(),
                (Double) input_bloodPressure.getValue(),
                (Double) input_bloodSugar.getValue(),
                input_doctorsComment.getText()
        );
        System.out.println(newRecord.toString());
        try {
            newRecord.createDatabaseRecord();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        refreshData();
    }

}
