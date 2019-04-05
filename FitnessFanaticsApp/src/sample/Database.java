package sample;

import java.sql.*;

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

    public ResultSet getResultSet(String sqlstatement) throws SQLException {
        openConnection();
        Statement statement = conn.createStatement();
        ResultSet RS = statement.executeQuery(sqlstatement);
        return RS;
    }

    public void insertStatement(String insert_query) throws SQLException {
        Statement statement = null;
        openConnection();
        conn.setAutoCommit(false);
        try {
            System.out.println("Database opened successfully");
            statement = conn.createStatement();
            System.out.println("The query was: " + insert_query);
            statement.executeUpdate(insert_query);
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
