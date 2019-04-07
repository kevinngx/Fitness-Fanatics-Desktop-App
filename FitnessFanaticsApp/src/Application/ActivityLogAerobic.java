package Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.AerobicExercise;
import sample.MentalExercise;
import sample.SessionDataHolder;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ActivityLogAerobic extends ActivityLogBaseController {

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

}
