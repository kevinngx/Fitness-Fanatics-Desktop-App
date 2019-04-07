package sample;

import java.sql.ResultSet;

public class SessionDataHolder {

    public static User user = getDefaultUser();
    public static DailyData dailyData = new DailyData();
    public static FoodIntake foodIntake = new FoodIntake();
    public static String currentPage;
    public static String previousPage;
    public static long dateRequested;

    public static User getUser() {
        return user;
    }

    public static void setUser(User inputUser) {
        user = inputUser;
    }

    public static long getDateRequested() {
        return dateRequested;
    }

    public static void setDateRequested(long dateRequested) {
        SessionDataHolder.dateRequested = dateRequested;
    }

    private static User getDefaultUser() {
        // This will set up a default user for testing purposes
        User defaultUser = new User();
        Database database = new Database();
        String query = "SELECT * FROM UserData \n" +
                "WHERE username = \"" + "kevinngx" + "\"";
        try {
            ResultSet rs = database.getResultSet(query);
            if (rs.next()) {
                defaultUser = new User(
                        Integer.parseInt(rs.getString(1)), // userId
                        rs.getString(2), // email
                        rs.getString(3), // username
                        rs.getString(4), // password
                        rs.getString(5), // firstName
                        rs.getString(6), // lastName
                        rs.getString(7), // gender
                        Double.parseDouble(rs.getString(8)), // bodyFatPercentage
                        Double.parseDouble(rs.getString(9)), // weight
                        Double.parseDouble(rs.getString(10)), // weight
                        Long.parseLong(rs.getString(11)) // dateOfBirth
                );

            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  defaultUser;
    }

}
