package Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import sample.ResistanceExercise;
import sample.SessionDataHolder;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

//-----------------------------------------------------------------

public class ActivityLogResistance extends ActivityLogBaseController {

    @FXML Label label_warningMessage;
    @FXML TextField input_description;
    @FXML private Spinner input_mass;
    @FXML private Spinner input_sets;
    @FXML private Spinner input_time;

    @FXML TableColumn<ResistanceExercise, String> column_exercise;
    @FXML TableColumn<ResistanceExercise, String> column_mass;
    @FXML TableColumn<ResistanceExercise, String> column_sets;
    @FXML TableColumn<ResistanceExercise, String> column_time;

    @FXML TableView<ResistanceExercise> table_summary;

//-----------------------------------------------------------------

    @FXML
    public void initialize() {
        setupHeaders("Activity - Resistance Exercises");
        date_selection.setValue(LocalDate.now());

        SpinnerValueFactory<Integer> massValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 0);
        this.input_mass.setValueFactory(massValueFactory);

        SpinnerValueFactory<Integer> setsValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 0);
        this.input_sets.setValueFactory(setsValueFactory);

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
        ArrayList<ResistanceExercise> exerciseList = new ArrayList<>();
        String query = String.format("SELECT * FROM Resistance_Exercise " +
                "WHERE (userId = %s AND date = %s)", SessionDataHolder.user.getId(),
                SessionDataHolder.getDateRequested());

        try {
            ResultSet rs = database.getResultSet(query);
            while (rs.next()) {
                exerciseList.add(new ResistanceExercise(
                        rs.getInt(1),
                        rs.getInt(2), // userId
                        rs.getLong(3), // date
                        rs.getString(4), // exercise
                        rs.getInt(5), // mass
                        rs.getInt(6), // sets
                        rs.getInt(7) // time
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        column_exercise.setCellValueFactory(new PropertyValueFactory<>("exercise"));
        column_mass.setCellValueFactory(new PropertyValueFactory<>("mass"));
        column_sets.setCellValueFactory(new PropertyValueFactory<>("sets"));
        column_time.setCellValueFactory(new PropertyValueFactory<>("timeTaken"));

        ObservableList<ResistanceExercise> data = FXCollections.observableArrayList();
        data.addAll(exerciseList);

        table_summary.setItems(data);
    }

    @FXML
    public void addNewEntryButton(ActionEvent event) throws IOException, SQLException {
        ResistanceExercise resistanceExercise = new ResistanceExercise(
                0,
                SessionDataHolder.user.getId(),
                SessionDataHolder.getDateRequested(),
                input_description.getText(),
                (Integer) input_mass.getValue(),
                (Integer) input_sets.getValue(),
                (Integer) input_time.getValue()
        );
        System.out.println(resistanceExercise.toString());
        resistanceExercise.createDatabaseRecord();
        populateTable();
        refreshLabels();
    }

    @FXML
    public void onDeleteSelected(ActionEvent event) {
        System.out.println("Deleting Field");

        try {
            ResistanceExercise selectedItem = table_summary.getSelectionModel().getSelectedItem();
            System.out.println(selectedItem.toString());
            String query = String.format("DELETE from Resistance_Exercise where exerciseId = %s",
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
