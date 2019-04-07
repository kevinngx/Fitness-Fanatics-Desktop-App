package Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.MentalExercise;
import sample.ResistanceExercise;
import sample.SessionDataHolder;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ActivityLogMental extends ActivityLogBaseController {

    @FXML TextField input_description;
    @FXML Spinner input_time;
    @FXML Slider input_emotionalRating;

    @FXML TableColumn<MentalExercise, String> column_exercise;
    @FXML TableColumn<MentalExercise, String> column_emotionalRating;
    @FXML TableColumn<MentalExercise, String> column_time;

    @FXML TableView<MentalExercise> table_summary;

    @FXML
    public void initialize() {
        setupHeaders("Activity - Mental Exercises");

        SpinnerValueFactory<Integer> timeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 0);
        this.input_time.setValueFactory(timeValueFactory);

        try {
            refreshLabels();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        populateTable();

    }

    private void populateTable() {
        ArrayList<MentalExercise> exerciseList = new ArrayList<>();

        String query = String.format("SELECT * FROM Mental_Exercise " +
                        "WHERE (userId = %s AND date = %s)", SessionDataHolder.user.getId(),
                SessionDataHolder.getDateRequested());
        try {
            ResultSet rs = database.getResultSet(query);
            while (rs.next()) {
                exerciseList.add(new MentalExercise(
                        rs.getInt(2), // userId
                        rs.getLong(3), // date
                        rs.getString(4), // exercise
                        rs.getInt(5), // emotionalRating
                        rs.getInt(6) // timeTaken

                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        column_exercise.setCellValueFactory(new PropertyValueFactory<>("exercise"));
        column_emotionalRating.setCellValueFactory(new PropertyValueFactory<>("emotionalRating"));
        column_time.setCellValueFactory(new PropertyValueFactory<>("timeTaken"));

        ObservableList<MentalExercise> data = FXCollections.observableArrayList();
        data.addAll(exerciseList);

        table_summary.setItems(data);

    }


    @FXML
    public void addNewEntryButton(ActionEvent event) throws IOException, SQLException {
        MentalExercise mentalExercise = new MentalExercise(
                SessionDataHolder.user.getId(),
                SessionDataHolder.getDateRequested(),
                input_description.getText(),
                (int) input_emotionalRating.getValue(),
                (int) input_time.getValue()
        );
        System.out.println(mentalExercise.toString());
        mentalExercise.createDatabaseRecord();
        populateTable();
        refreshLabels();
    }
}