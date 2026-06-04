package Database;

import LocalData.Drug;
import LocalData.DrugDetail;
import LocalData.Stock;
import LocalData.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Database {

    private ConnectDB connectDB;
    public Connection database;

    public Database(ConnectDB connectionDB) {
        this.connectDB = connectionDB;
        this.database  = connectionDB.getConnection();
    }

    public boolean authUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement exec = database.prepareStatement(query)) {
            exec.setString(1, username);
            exec.setString(2, password);
            ResultSet set = exec.executeQuery();
            return set.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public String getUserType(String username, String password) {
        String query = "SELECT type FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement exec = database.prepareStatement(query)) {
            exec.setString(1, username);
            exec.setString(2, password);
            ResultSet set = exec.executeQuery();
            if (set.next()) return set.getString("type");
            return null;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public boolean addUser(String username, String password, String type) {
        String query = "INSERT INTO users(username, password, type) VALUES(?,?,?)";
        try (PreparedStatement exec = database.prepareStatement(query)) {
            exec.setString(1, username);
            exec.setString(2, password);
            exec.setString(3, type);
            exec.executeUpdate();
            JOptionPane.showMessageDialog(null, username + " Created Successfully",
                    "User Success", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean deleteUser(String username) {
        String query = "DELETE FROM users WHERE username = ?";
        try (PreparedStatement exec = database.prepareStatement(query)) {
            exec.setString(1, username);
            int rowsAffected = exec.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, username + " Deleted Successfully",
                        "User Success", JOptionPane.OK_OPTION);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, username + " Not Found",
                        "User ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Returns a simple String[] list (username, type) — kept for legacy use.
     */
    public List<String[]> getUsers() {
        List<String[]> users = new ArrayList<>();
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

    /**
     * Returns full User objects (including password) for loading into
     * LocalDataStore. Only called once at startup.
     */
    public List<User> getFullUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, username, password, type FROM users ORDER BY username";
        try (Statement stmt = database.createStatement();
             ResultSet set = stmt.executeQuery(query)) {
            while (set.next()) {
                users.add(new User(
                    set.getInt("id"),
                    set.getString("username"),
                    set.getString("password"),
                    set.getString("type")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
        }
        return users;
    }
    
    // memory saver?
    public List<Drug> getDrugs() {
        List<Drug> drugs = new ArrayList<>();
        String query = "SELECT id, name, type FROM drugs ORDER BY name";
        try (Statement stmt = database.createStatement();
             ResultSet set = stmt.executeQuery(query)) {
            while (set.next()) {
                drugs.add(new Drug(
                    set.getInt("id"),
                    set.getString("name"),
                    set.getString("type")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
        }
        return drugs;
    }

    public boolean addDrug(String name, String type) {
        String query = "INSERT INTO drugs(name, type) VALUES(?,?)";
        try (PreparedStatement exec = database.prepareStatement(query)) {
            exec.setString(1, name);
            exec.setString(2, type);
            exec.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public List<Stock> getStocks() {
        List<Stock> stocks = new ArrayList<>();
        String query = "SELECT id, drug_id, sku, dosage, form, packaging, "
                     + "orig_price, price, discount, quantity, min_stock, unit, "
                     + "available, image_id, sup_name, last_restock, exp_date, "
                     + "create_at, update_at FROM kiosk_inv ORDER BY id";
        try (Statement stmt = database.createStatement();
             ResultSet set = stmt.executeQuery(query)) {
            while (set.next()) {
                stocks.add(new Stock(
                    set.getInt("id"),
                    set.getInt("drug_id"),
                    set.getString("sku"),
                    set.getString("dosage"),
                    set.getString("form"),
                    set.getString("packaging"),
                    set.getDouble("orig_price"),
                    set.getDouble("price"),
                    set.getDouble("discount"),
                    set.getInt("quantity"),
                    set.getInt("min_stock"),
                    set.getString("unit"),
                    set.getInt("available") == 1,
                    set.getString("image_id"),
                    set.getString("sup_name"),
                    set.getString("last_restock"),
                    set.getString("exp_date"),
                    set.getString("create_at"),
                    set.getString("update_at")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
        }
        return stocks;
    }
    
    public List<DrugDetail> getDrugDetails(String drugName) {
        List<DrugDetail> results = new ArrayList<>();
        String query =
            "SELECT d.name, d.type, k.dosage, k.form, k.sup_name, k.price, k.sku " +
            "FROM drugs d " +
            "JOIN kiosk_inv k ON k.drug_id = d.id " +
            "WHERE d.name = ? COLLATE NOCASE " +
            "ORDER BY k.dosage, k.form";

        try (PreparedStatement exec = database.prepareStatement(query)) {
            exec.setString(1, drugName);
            ResultSet set = exec.executeQuery();
            while (set.next()) {
                results.add(new DrugDetail(
                    set.getString("name"),
                    set.getString("type"),
                    set.getString("dosage"),
                    set.getString("form"),
                    set.getString("sup_name"),
                    set.getDouble("price"),
                    set.getString("sku")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
        }
        return results;
    }

    public DrugDetail getDrugDetailBySku(String sku) {
        String query =
            "SELECT d.name, d.type, k.dosage, k.form, k.sup_name, k.price, k.sku " +
            "FROM drugs d " +
            "JOIN kiosk_inv k ON k.drug_id = d.id " +
            "WHERE k.sku = ? COLLATE NOCASE";

        try (PreparedStatement exec = database.prepareStatement(query)) {
            exec.setString(1, sku);
            ResultSet set = exec.executeQuery();
            if (set.next()) {
                return new DrugDetail(
                    set.getString("name"),
                    set.getString("type"),
                    set.getString("dosage"),
                    set.getString("form"),
                    set.getString("sup_name"),
                    set.getDouble("price"),
                    set.getString("sku")
                );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public boolean addStock(Stock s) {
        String query =
            "INSERT INTO kiosk_inv(drug_id, sku, dosage, form, packaging, "
            + "orig_price, price, discount, quantity, min_stock, unit, "
            + "available, image_id, sup_name, last_restock, exp_date) "
            + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement exec = database.prepareStatement(query)) {
            exec.setInt(1,    s.getDrugId());
            exec.setString(2, s.getSku());
            exec.setString(3, s.getDosage());
            exec.setString(4, s.getForm());
            exec.setString(5, s.getPackaging());
            exec.setDouble(6, s.getOrigPrice());
            exec.setDouble(7, s.getPrice());
            exec.setDouble(8, s.getDiscount());
            exec.setInt(9,    s.getQuantity());
            exec.setInt(10,   s.getMinStock());
            exec.setString(11, s.getUnit());
            exec.setInt(12,   s.isAvailable() ? 1 : 0);
            exec.setString(13,   s.getImageId());
            exec.setString(14, s.getSupName());
            exec.setString(15, s.getLastRestock());
            exec.setString(16, s.getExpDate());
            exec.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean deleteStock(int id) {
        String query = "DELETE FROM kiosk_inv WHERE id = ?";
        try (PreparedStatement exec = database.prepareStatement(query)) {
            exec.setInt(1, id);
            int rows = exec.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Stock entry deleted.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "No entry found with that ID.",
                        "Not Found", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean deleteDrug(int id) {
        String query = "DELETE FROM drugs WHERE id = ?";
        try (PreparedStatement exec = database.prepareStatement(query)) {
            exec.setInt(1, id);
            return exec.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean updateUsername(String oldUsername, String newUsername) {
        String query = "UPDATE users SET username = ? WHERE username = ?";
        try (PreparedStatement exec = database.prepareStatement(query)) {
            exec.setString(1, newUsername);
            exec.setString(2, oldUsername);
            return exec.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updatePassword(String username, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE username = ?";
        try (PreparedStatement exec = database.prepareStatement(query)) {
            exec.setString(1, newPassword);
            exec.setString(2, username);
            return exec.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateUserType(String username, String newType) {
        String query = "UPDATE users SET type = ? WHERE username = ?";
        try (PreparedStatement exec = database.prepareStatement(query)) {
            exec.setString(1, newType);
            exec.setString(2, username);
            return exec.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean updateStockQuantity(String sku, int newQuantity) {
        String query = "UPDATE kiosk_inv SET quantity = ? WHERE sku = ? COLLATE NOCASE";
        try (PreparedStatement exec = database.prepareStatement(query)) {
            exec.setInt(1, newQuantity);
            exec.setString(2, sku);
            return exec.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean updateStock(int id, String sku, String dosage, String form,
                           String packaging, double origPrice, double price,
                           double discount, int quantity, int minStock,
                           String unit, boolean available, String imageId,
                           String supName, String lastRestock, String expDate) {
        String query =
            "UPDATE kiosk_inv SET sku=?, dosage=?, form=?, packaging=?, " +
            "orig_price=?, price=?, discount=?, quantity=?, min_stock=?, " +
            "unit=?, available=?, image_id=?, sup_name=?, last_restock=?, exp_date=? " +
            "WHERE id=?";
        try (PreparedStatement exec = database.prepareStatement(query)) {
            exec.setString(1,  sku);
            exec.setString(2,  dosage);
            exec.setString(3,  form);
            exec.setString(4,  packaging);
            exec.setDouble(5,  origPrice);
            exec.setDouble(6,  price);
            exec.setDouble(7,  discount);
            exec.setInt(8,     quantity);
            exec.setInt(9,     minStock);
            exec.setString(10, unit);
            exec.setInt(11,    available ? 1 : 0);
            exec.setString(12, imageId);
            exec.setString(13, supName);
            exec.setString(14, lastRestock);
            exec.setString(15, expDate);
            exec.setInt(16,    id);
            return exec.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "SQL ERROR " + e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

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
                image_id     TEXT NOT NULL,
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