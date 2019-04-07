package Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import sample.AerobicExercise;
import sample.FoodIntake;
import sample.MentalExercise;
import sample.SessionDataHolder;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ActivityLogAerobic extends ActivityLogBaseController {

    @FXML Label label_warningMessage;

    @FXML TextField input_description;
    @FXML Spinner input_time;
    @FXML Spinner input_distance;

    @FXML TableColumn<AerobicExercise, String> column_exercise;
    @FXML TableColumn<AerobicExercise, String> column_distance;
    @FXML TableColumn<AerobicExercise, String> column_time;

    @FXML
    TableView<AerobicExercise> table_summary;

    @FXML
    public void initialize() {
        setupHeaders("Activity - Aerobic Exercises");

        SpinnerValueFactory<Integer> timeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 0);
        this.input_time.setValueFactory(timeValueFactory);

        SpinnerValueFactory<Integer> distanceValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 0);
        this.input_distance.setValueFactory(distanceValueFactory);

        try {
            refreshLabels();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        populateTable();

    }

    private void populateTable() {
        ArrayList<AerobicExercise> exerciseList = new ArrayList<>();

        String query = String.format("SELECT * FROM Aerobic_Exercise " +
                        "WHERE (userId = %s AND date = %s)", SessionDataHolder.user.getId(),
                SessionDataHolder.getDateRequested());
        try {
            ResultSet rs = database.getResultSet(query);
            while (rs.next()) {
                exerciseList.add(new AerobicExercise(
                        rs.getInt(1),
                        rs.getInt(2), // userId
                        rs.getLong(3), // date
                        rs.getString(4), // exercise
                        rs.getInt(5), // distance
                        rs.getInt(6) // time

                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        column_exercise.setCellValueFactory(new PropertyValueFactory<>("exercise"));
        column_distance.setCellValueFactory(new PropertyValueFactory<>("distance"));
        column_time.setCellValueFactory(new PropertyValueFactory<>("time"));

        ObservableList<AerobicExercise> data = FXCollections.observableArrayList();
        data.addAll(exerciseList);

        table_summary.setItems(data);

    }

    @FXML
    public void addNewEntryButton(ActionEvent event) throws IOException, SQLException {
        AerobicExercise aerobicExercise = new AerobicExercise(
                0,
                SessionDataHolder.user.getId(),
                SessionDataHolder.getDateRequested(),
                input_description.getText(),
                (int) input_distance.getValue(),
                (int) input_time.getValue()
        );
        System.out.println(aerobicExercise.toString());
        aerobicExercise.createDatabaseRecord();
        populateTable();
        refreshLabels();
    }

    @FXML
    public void onDeleteSelected(ActionEvent event) {
        System.out.println("Deleting Field");

        try {
            AerobicExercise selectedItem = table_summary.getSelectionModel().getSelectedItem();
            System.out.println(selectedItem.toString());

            String query = String.format("DELETE from Aerobic_Exercise where exerciseId = %s",
                    selectedItem.getExerciseId());
            database.insertStatement(query);
            populateTable();
            label_warningMessage.setTextFill(Color.GREEN);
            label_warningMessage.setText("Row Deleted");

        } catch (Exception e) {
            System.out.println("ERROR");
            label_warningMessage.setTextFill(Color.RED);
            label_warningMessage.setText("Please select a row");
        }
    }

}
