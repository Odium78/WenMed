package Database;

import java.sql.*;
import java.util.List;
import javax.swing.JOptionPane;

public class Database{
    private ConnectDB connectDB;
    public Connection database;
    public Database(ConnectDB connectionDB){
        this.connectDB = connectionDB;
        this.database = connectionDB.getConnection();
    }
    
    public boolean authUser(String username, String password){
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try(PreparedStatement exec = database.prepareStatement(query)){
            exec.setString(1, username);
            exec.setString(2, password);
            
            ResultSet set = exec.executeQuery();
            if (set.next()){
                return true;
            } else {
                return false;
            }
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getStackTrace(), "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean addUser(String username, String password, String type){
        String query = "INSERT INTO users(username, password, type) VALUES(?,?,?)";
        try(PreparedStatement exec = database.prepareStatement(query)){
            exec.setString(1, username);
            exec.setString(2, password);
            exec.setString(3, type);
            
            exec.executeUpdate();
            JOptionPane.showMessageDialog(null, username + " Created Successfully", "User Success", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getStackTrace(), "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean deleteUser(String username){
        String query = "DELETE FROM users WHERE username = ?";
        try(PreparedStatement exec = database.prepareStatement(query)){
            exec.setString(1, username);
            
            int rowsAffected = exec.executeUpdate();
            
            if (rowsAffected > 0){
                JOptionPane.showMessageDialog(null, username + " Deleted Successfully", "User Success", JOptionPane.OK_OPTION);
                return true;
            } else{
                JOptionPane.showMessageDialog(null, username + " Not Found", "User ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch(SQLException e){
                JOptionPane.showMessageDialog(null, e.getStackTrace(), "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
                return false;
        }
    }
    
    public List<String[]> getUsers() {
        java.util.List<String[]> users = new java.util.ArrayList<>();
        String query = "SELECT username, type FROM users ORDER BY username";
        try (Statement stmt = database.createStatement();
             ResultSet set = stmt.executeQuery(query)) {
            while (set.next()) {
                users.add(new String[]{
                    set.getString("username"),
                    set.getString("type")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
        }
        return users;
    }
    
    public String getUserType(String username, String password) {
        String query = "SELECT type FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement exec = database.prepareStatement(query)) {
            exec.setString(1, username);
            exec.setString(2, password);
            ResultSet set = exec.executeQuery();
            if (set.next()) {
                return set.getString("type");
            }
            return null;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }


    
    // table creation (not used in release)
    public void makeTable() {
        String[] queries = {
            """
            CREATE TABLE IF NOT EXISTS users (
                id          INTEGER PRIMARY KEY AUTOINCREMENT,
                username    TEXT UNIQUE NOT NULL,
                password    TEXT NOT NULL,
                type        TEXT NOT NULL
            )
            """,
            
            """
            CREATE TABLE IF NOT EXISTS drugs (
                id      INTEGER PRIMARY KEY AUTOINCREMENT,
                name    TEXT UNIQUE NOT NULL,
                type    TEXT NOT NULL
            )
            """,

            """
            CREATE TABLE IF NOT EXISTS kiosk_inv (
                id           INTEGER PRIMARY KEY AUTOINCREMENT,
                drug_id      INTEGER NOT NULL,
                sku          TEXT UNIQUE NOT NULL,
                dosage       TEXT NOT NULL,
                form         TEXT NOT NULL,
                packaging    TEXT NOT NULL,
                orig_price   REAL NOT NULL DEFAULT 0.00,
                price        REAL NOT NULL DEFAULT 0.00,
                discount     REAL NOT NULL DEFAULT 0.00,
                quantity     INTEGER NOT NULL DEFAULT 0,
                min_stock    INTEGER NOT NULL DEFAULT 10,
                unit         TEXT NOT NULL DEFAULT 'pcs',
                available    INTEGER NOT NULL DEFAULT 1,
                image_id     INTEGER DEFAULT 0,
                sup_name     TEXT NOT NULL,
                last_restock TEXT NOT NULL,
                exp_date     TEXT NOT NULL,
                create_at    TEXT NOT NULL DEFAULT (datetime('now')),
                update_at    TEXT NOT NULL DEFAULT (datetime('now')),

                FOREIGN KEY (drug_id) REFERENCES drugs(id) ON DELETE RESTRICT
            )
            """,
            """
            CREATE TRIGGER IF NOT EXISTS trg_kiosk_upd
            AFTER UPDATE ON kiosk_inv
            FOR EACH ROW
            BEGIN
                UPDATE kiosk_inv SET update_at = datetime('now') WHERE id = OLD.id;
            END
            """,

            "CREATE INDEX IF NOT EXISTS idx_inv_drug_id    ON kiosk_inv (drug_id)",
            "CREATE INDEX IF NOT EXISTS idx_inv_sku        ON kiosk_inv (sku)",
            "CREATE INDEX IF NOT EXISTS idx_inv_available  ON kiosk_inv (available)"
        };

        try (Statement stmt = database.createStatement()) {
            for (String query : queries) {
                stmt.execute(query);
            }
            System.out.println("Tables created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}