/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package Main;

import Database.ConnectDB;
import Database.Database;
import Ui.Window;
import javax.swing.SwingUtilities;

/**
 *
 * @author ROG
 */
public class Wenmed {
    public static void main(String[] args) { 
        System.out.println("Hello World!");
        
        ConnectDB connection = new ConnectDB();
        Database db = new Database(connection);
        
        // run window on separate thread
        SwingUtilities.invokeLater(() -> new Window(db));
        
        db.makeTable();
    }
}
