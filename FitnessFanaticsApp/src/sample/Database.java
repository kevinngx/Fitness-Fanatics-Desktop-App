package sample;

import java.sql.*;

/***************************************************************************************
 *    This code was adapted from the tutorial 3B of INFS2605, as provided on the Edsetem website
 *    It has been modified to meet the needs of our application
 *    It is a helper class that is used to access our SQLite Database
 ***************************************************************************************/

public class Database {

    public static Connection conn;

    public static void openConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection("jdbc:sqlite:fitnessFanaticsDB.db");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public ResultSet getResultSet(String selectQuery) throws SQLException {
        openConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(selectQuery);
        return resultSet;
    }

    public void insertStatement(String insertQuery) throws SQLException {
        Statement statement = null;
        openConnection();
        conn.setAutoCommit(false);
        try {
            System.out.println("Database opened successfully");
            statement = conn.createStatement();
            System.out.println("The query was: " + insertQuery);
            statement.executeUpdate(insertQuery);
            System.out.println("Insertion successful!");
            statement.close();
            conn.commit();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        statement.close();
    }

}
