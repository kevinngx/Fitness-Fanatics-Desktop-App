package Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import sample.FoodIntake;
import sample.MentalExercise;
import sample.ResistanceExercise;
import sample.SessionDataHolder;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ActivityLogMental extends ActivityLogBaseController {

    @FXML Label label_warningMessage;

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
        date_selection.setValue(LocalDate.now());

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
                        rs.getInt(1),
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
                0,
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

    @FXML
    public void onDeleteSelected(ActionEvent event) {
        System.out.println("Deleting Field");

        try {
            MentalExercise selectedItem = table_summary.getSelectionModel().getSelectedItem();
            System.out.println(selectedItem.toString());

            String query = String.format("DELETE from Mental_Exercise where exerciseId = %s",
                    selectedItem.getExerciseId());
            database.insertStatement(query);
            populateTable();
            label_warningMessage.setTextFill(Color.GREEN);
            label_warningMessage.setText("Row Deleted");
            refreshLabels();
        } catch (Exception e) {
            System.out.println("ERROR");
            label_warningMessage.setText("Please select a row");
            label_warningMessage.setTextFill(Color.RED);
        }
    }
}
