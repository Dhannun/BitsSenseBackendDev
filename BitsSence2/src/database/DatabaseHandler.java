/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import alertmaker.AlertMaker;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wecom
 */
public class DatabaseHandler {
    
    private static String email;
    private static DatabaseHandler handler = null;
    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement preparedStatement;
    private static final String DB_URL = "jdbc:derby:database;create=true";
    
    private DatabaseHandler() {
        createConnection();
        createTableUsers();
    }
    
    public static DatabaseHandler getIstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }
    
    private void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            connection = DriverManager.getConnection(DB_URL);
//            JOptionPane.showMessageDialog(null, "Connected");
            AlertMaker.showInformation("Connection", "Database Connected");
        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(null, "Can't connect to database!");
            AlertMaker.showError("Connection", "Can't connect to database!");
            System.exit(0);
        }
    }
    
    private void createTableUsers() {
        final String TABLE_NAME = "USERS";
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, TABLE_NAME.toUpperCase(), null);

            if (resultSet.next()) {
                AlertMaker.showInformation("Connection", "Table " + TABLE_NAME + " already exist");
//                JOptionPane.showMessageDialog(null, "Table " + TABLE_NAME + " already exist");
//                System.out.println("Table " + TABLE_NAME + " already exist");
            } else {
                statement = connection.createStatement();
                String query = "CREATE TABLE " + TABLE_NAME + "(\n"
                        + "email VARCHAR(100) NOT NULL PRIMARY KEY,\n"
                        + "surname VARCHAR(20) NOT NULL,\n"
                        + "first_name VARCHAR(20) NOT NULL,\n"
                        + "phone VARCHAR(20) NOT NULL,\n"
                        + "dob VARCHAR(10) NOT NULL,\n"
                        + "bvn VARCHAR(100) NOT NULL,\n"
                        + "password VARCHAR(120) NOT NULL,\n"
                        + "status VARCHAR(10) NOT NULL"
                        + ")";
                
                try {
                    statement.execute(query);
                    AlertMaker.showInformation("Connection", "Table " + TABLE_NAME + " created successful! :-)");
//                    JOptionPane.showMessageDialog(null, "Table " + TABLE_NAME + " created successful! :-)");                    
                } catch (Exception e) {
                    AlertMaker.showInformation("Connection", "The table query is not executed. x :-(\n"+e.getMessage());
//                    JOptionPane.showMessageDialog(null, "The table query is not executed. x :-(");
//                    System.err.println(e.getMessage());
                }
                
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    public static Boolean execAction(String query, String[] data) {
        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < data.length; i++) {
                preparedStatement.setString(i+1, data[i]);
            }
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }

    public static ResultSet execQuery(String query) {
        ResultSet resultSet;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
        return resultSet;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        DatabaseHandler.email = email;
    }
    
    
    public static void main(String[] args) {
            System.out.println("Start");
            new DatabaseHandler();
        try {
            String qu = "SELECT * FROM USERS";
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(qu);
            System.out.println("/////////////////////////////////////\n"
                    + "////////// Database Records /////////\n"
                    + "/////////////////////////////////////\n");
            while (rs.next()) {
                String email = rs.getString("email");
                String surname = rs.getString("surname");
                String fname = rs.getString("first_name");
                String phone = rs.getString("phone");
                String dob = rs.getString("dob");
                String bvn = rs.getString("bvn");
                String password = rs.getString("password");
                String otp = rs.getString("status");
                
                System.out.println("Email: " +email+"\nSurname: " +surname+"\nFirst Name: "+fname+"\nPhone: "+phone+"\nDOB: "+dob+"\nbvn: "+bvn+"\nPassword: "+password+"\nOTP: "+otp);
            }
//        DatabaseHandler.getIstance();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Done");
    }
}
