package Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.FoodIntake;
import sample.SessionDataHolder;
import java.io.IOException;
import java.sql.SQLException;

//-----------------------------------------------------------------

public class FoodDiaryAddNewController extends FoodDiaryController {

    @FXML ToggleGroup input_foodGroup;
    @FXML ToggleGroup input_mealTime;

    @FXML TextField input_description;

    @FXML Spinner input_carbs;
    @FXML Spinner input_fats;
    @FXML Spinner input_proteins;

//-----------------------------------------------------------------

    @FXML
    public void initialize() {
        setupHeaders("Food Diary - Add New Data");

        // Setup spinners
        SpinnerValueFactory<Double> heightValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100000.0, 0);
        this.input_carbs.setValueFactory(heightValueFactory);

        SpinnerValueFactory<Double> weightValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100000.0, 0);
        this.input_fats.setValueFactory(weightValueFactory);

        SpinnerValueFactory<Double> bodyFatPercentageValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100000.0, 0);
        this.input_proteins.setValueFactory(bodyFatPercentageValueFactory);

    }

    @FXML
    public void onAddNewFoodItemClick(ActionEvent event) throws IOException {
        RadioButton selectedMealTime = (RadioButton) input_mealTime.getSelectedToggle();
        RadioButton selectedFoodGroup= (RadioButton) input_foodGroup.getSelectedToggle();

        FoodIntake newItem = new FoodIntake(
                1,
                SessionDataHolder.user.getId(),
                SessionDataHolder.getDateRequested(),
                selectedMealTime.getText(),
                input_description.getText(),
                (Double) input_carbs.getValue(),
                (Double) input_fats.getValue(),
                (Double) input_proteins.getValue(),
                selectedFoodGroup.getText()
        );
        System.out.println(newItem.toString());
        try {
            newItem.createDatabaseRecord();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pageSwitcherHelper.switcher(event, "../Application/FoodDiaryDisplay.fxml");
    }

    @FXML
    public void onBackToFoodDiaryClick(ActionEvent event) throws IOException {
        pageSwitcherHelper.switcher(event, "../Application/FoodDiaryDisplay.fxml");
    }

}
