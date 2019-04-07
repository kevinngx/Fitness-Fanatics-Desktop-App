package Application;

import com.sun.org.apache.bcel.internal.generic.Select;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.DateHelper;
import sample.FoodIntake;
import sample.SessionDataHolder;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

public class FoodDiaryDisplayController extends FoodDiaryController {

    private static final String TAG = "FoodDiaryDisplayControl: ";

    enum Macronutrient {CARBS, FATS, PROTEINS}
    enum Query_Type {ALL, MEAL_TIME, FOOD_GROUP}
    ArrayList<FoodIntake> foodItems;

    @FXML Label value_carbAmount;
    @FXML Label value_carbCalories;
    @FXML Label value_fatAmount;
    @FXML Label value_fatCalories;
    @FXML Label value_proteinAmount;
    @FXML Label value_proteinCalories;
    @FXML Label value_totalCalories;

    @FXML TableColumn<FoodIntake, String> column_mealTime;
    @FXML TableColumn<FoodIntake, String> column_foodGroup;
    @FXML TableColumn<FoodIntake, String> column_description;
    @FXML TableColumn<FoodIntake, String> column_carbs;
    @FXML TableColumn<FoodIntake, String> column_fats;
    @FXML TableColumn<FoodIntake, String> column_proteins;
    @FXML TableColumn<FoodIntake, String> column_calories;

    @FXML TableView<FoodIntake> table_summary;

    @FXML private PieChart macronutrient_chart;

    @FXML
    public void initialize() {
        setupHeaders("Food Diary - Display");

        // Get Data
        refreshData(Query_Type.ALL,"ALL");

        System.out.println(TAG + "Food items found: ");
        for (int i = 0; i < foodItems.size(); i++) {
            System.out.println(foodItems.get(i).toString());
        }

    }

    private void setFields() {
        double carbAmount = getAmount(Macronutrient.CARBS);
        double carbCalories = getCalories(Macronutrient.CARBS);
        double fatAmount = getAmount(Macronutrient.FATS);
        double fatCalories = getCalories(Macronutrient.FATS);
        double proteinAmount = getAmount(Macronutrient.PROTEINS);
        double proteinCalories = getCalories(Macronutrient.PROTEINS);
        double totalCalories = carbCalories + fatCalories + proteinCalories;

        // Set Data
        value_carbAmount.setText(String.format("%.2f", (carbAmount)) + "g");
        value_carbCalories.setText(String.format("%.2f", (carbCalories)) + "kcal");
        value_fatAmount.setText(String.format("%.2f", (fatAmount)) + "g");
        value_fatCalories.setText(String.format("%.2f", (fatCalories))+ "kcal");
        value_proteinAmount.setText(String.format("%.2f", (proteinAmount)) + "g");
        value_proteinCalories.setText(String.format("%.2f", (proteinCalories))+ "kcal");
        value_totalCalories.setText(String.format("%.0f", totalCalories));

        macronutrient_chart.setData(getMacroChartData(carbCalories, fatCalories, proteinCalories));
    }

    private ObservableList<PieChart.Data> getMacroChartData(double carbCalories, double fatCalories, double proteinCalories) {
        ObservableList<PieChart.Data> answer = FXCollections.observableArrayList();
        answer.addAll(new PieChart.Data("Carbs", carbCalories),
                new PieChart.Data("Fats", fatCalories),
                new PieChart.Data("Proteins", proteinCalories));
        return answer;
    }

    private double getCalories(Macronutrient macronutrient) {
        double calories = 0;
        switch (macronutrient) {
            case CARBS:
                calories += getAmount(Macronutrient.CARBS) * 4;
                break;
            case FATS:
                calories += getAmount(Macronutrient.FATS) * 4;
                break;
            case PROTEINS:
                calories += getAmount(Macronutrient.PROTEINS) * 4;
                break;
        }
        return calories;
    }

    private double getAmount(Macronutrient macronutrient) {
        double amount = 0;
        switch (macronutrient) {
            case CARBS:
                for (int i = 0; i < foodItems.size(); i++) {
                    amount += foodItems.get(i).getCarbContent();
                }
                break;

            case FATS:
                for (int i = 0; i < foodItems.size(); i++) {
                    amount += foodItems.get(i).getFatContent();
                }
                break;

            case PROTEINS:
                for (int i = 0; i < foodItems.size(); i++) {
                    amount += foodItems.get(i).getProteinContent();
                }
                break;
        }
        return amount;
    }

    public void refreshData(Query_Type queryType, String queryParameter) {
        String query;
        if (queryType == Query_Type.ALL) {
            System.out.println(TAG + "Selecting ALL Data");
            query = String.format("SELECT * FROM FoodIntake " +
                            "WHERE (date = %s AND userId = %s)",
                    SessionDataHolder.dateRequested, SessionDataHolder.user.getId());
        } else if (queryType == Query_Type.MEAL_TIME){
            System.out.println(TAG + "Selecting Meal Time Data");
            query = String.format("SELECT * FROM FoodIntake " +
                            "WHERE (date = %s AND userId = %s AND mealTime = '%s')",
                    SessionDataHolder.dateRequested, SessionDataHolder.user.getId(), queryParameter);
        } else {
            System.out.println(TAG + "Selecting Food Group Data");
            query = String.format("SELECT * FROM FoodIntake " +
                            "WHERE (date = %s AND userId = %s AND foodGroup = '%s')",
                    SessionDataHolder.dateRequested, SessionDataHolder.user.getId(), queryParameter);
        }

        ArrayList<FoodIntake> foodItems = new ArrayList<>();

        try {
            ResultSet rs = database.getResultSet(query);
            while (rs.next()) {
                foodItems.add(new FoodIntake(
                        Integer.parseInt(rs.getString(1)), // mealId
                        Integer.parseInt(rs.getString(2)), // userId
                        Long.parseLong(rs.getString(3)), // date
                        rs.getString(4), // mealTime
                        rs.getString(5), // description
                        Double.parseDouble(rs.getString(6)), // carbContent
                        Double.parseDouble(rs.getString(7)), // fatContent
                        Double.parseDouble(rs.getString(8)), // proteinContent
                        rs.getString(9)
                ));
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.foodItems = foodItems;

        setFields();
        populateTable();
    }

    private void populateTable() {
        column_mealTime.setCellValueFactory(new PropertyValueFactory<>("mealTime"));
        column_foodGroup.setCellValueFactory(new PropertyValueFactory<>("foodGroup"));
        column_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        column_carbs.setCellValueFactory(new PropertyValueFactory<>("carbContent"));
        column_fats.setCellValueFactory(new PropertyValueFactory<>("fatContent"));
        column_proteins.setCellValueFactory(new PropertyValueFactory<>("proteinContent")); // ERROR IS HERE
        column_calories.setCellValueFactory(new PropertyValueFactory<>("calories"));

        ObservableList<FoodIntake> data = FXCollections.observableArrayList();
        data.addAll(foodItems);

        table_summary.setItems(data);
    }

    @FXML
    public void onGetAllData(ActionEvent event) throws IOException {
        System.out.println(TAG + "Getting all data for this date");
        refreshData(Query_Type.ALL,"ALL");

    }

    @FXML
    public void onGetBreakfastButtonClick(ActionEvent event) throws IOException {
        System.out.println(TAG + "Getting breakfast data for this date");
        refreshData(Query_Type.MEAL_TIME, "Breakfast");
    }

    @FXML
    public void onGetLunchButtonClick(ActionEvent event) throws IOException {
        System.out.println(TAG + "Getting lunch data for this date");
        refreshData(Query_Type.MEAL_TIME,"Lunch");
    }

    @FXML
    public void onGetDinnerButtonClick(ActionEvent event) throws IOException {
        System.out.println(TAG + "Getting dinner data for this date");
        refreshData(Query_Type.MEAL_TIME,"Dinner");
    }

    @FXML
    public void onGetSnackButtonClick(ActionEvent event) throws IOException {
        System.out.println(TAG + "Getting snack for this date");
        refreshData(Query_Type.MEAL_TIME,"Snack");
    }

    @FXML
    public void onGetAddNewMealButtonClick(ActionEvent event) throws IOException {
        System.out.println(TAG + "Adding new meal item");
        pageSwitcherHelper.switcher(event, "../Application/FoodDiaryAddNew.fxml");
    }


    @FXML
    public void onGetVegesButtonClick(ActionEvent event) throws IOException {
        System.out.println(TAG + "Getting veges for this date");
        refreshData(Query_Type.FOOD_GROUP,"Fruits and Vegetables");
    }

    @FXML
    public void onGetMeatsButtonClick(ActionEvent event) throws IOException {
        System.out.println(TAG + "Getting meats for this date");
        refreshData(Query_Type.FOOD_GROUP,"Meat and Fish");
    }

    @FXML
    public void onGetFatsButtonClick(ActionEvent event) throws IOException {
        System.out.println(TAG + "Getting fats for this date");
        refreshData(Query_Type.FOOD_GROUP,"Fats and Sugars");
    }

    @FXML
    public void onGetDairyButtonClick(ActionEvent event) throws IOException {
        System.out.println(TAG + "Getting dairy for this date");
        refreshData(Query_Type.FOOD_GROUP,"Milk and Dairy");
    }

    @FXML
    public void onGetGrainsButtonClick(ActionEvent event) throws IOException {
        System.out.println(TAG + "Getting grains for this date");
        refreshData(Query_Type.FOOD_GROUP,"Breads and Grains");
    }



}
