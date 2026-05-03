/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author ROG
 */
public class ConnectDB{
    private Connection connection;
    private String url = "jdbc:sqlite:data.db";
    
    public ConnectDB(){
        System.out.println("Initializing Database");
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Database Connected");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(), "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void closeConnection() {
        try {
            if (connection != null) {
                System.out.println("Closing Database");
                connection.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(), "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Connection getConnection() {
        return connection;
    }
}
