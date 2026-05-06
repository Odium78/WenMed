/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LocalData;

/**
 *
 * @author ROG
 */

import Database.Database;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Runtime in-memory cache for the entire database.
 *
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │  How it works                                                           │
 * │  • loadAll(db)  — called once at startup; fills users, drugs, stocks   │
 * │  • All reads happen entirely in-memory (no DB round-trip)              │
 * │  • User writes (add/remove) hit the DB immediately + sync local list   │
 * │  • Drug / Stock additions go into a pending queue; call                │
 * │    flushPendingToDatabase(db) to persist them when ready               │
 * └─────────────────────────────────────────────────────────────────────────┘
 */
public class LocalDataStore {

    // ── Live lists ────────────────────────────────────────────────────────────
    private final ArrayList<User>  users  = new ArrayList<>();
    private final ArrayList<Drug>  drugs  = new ArrayList<>();
    private final ArrayList<Stock> stocks = new ArrayList<>();

    // ── Pending-write queues (not yet in DB) ──────────────────────────────────
    private final ArrayList<Drug>  pendingDrugs  = new ArrayList<>();
    private final ArrayList<Stock> pendingStocks = new ArrayList<>();

    // ═════════════════════════════════════════════════════════════════════════
    // LOAD
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Loads all tables from the database into memory.
     * Call this exactly once after the DB connection is ready.
     */
    public void loadAll(Database db) {
        loadUsers(db);
        loadDrugs(db);
        loadStocks(db);
        System.out.println("[LocalDataStore] Loaded "
            + users.size()  + " users, "
            + drugs.size()  + " drugs, "
            + stocks.size() + " stock entries.");
    }

    private void loadUsers(Database db) {
        users.clear();
        users.addAll(db.getFullUsers());
    }

    private void loadDrugs(Database db) {
        drugs.clear();
        drugs.addAll(db.getDrugs());
    }

    private void loadStocks(Database db) {
        stocks.clear();
        stocks.addAll(db.getStocks());
    }

    // ═════════════════════════════════════════════════════════════════════════
    // USER operations  (immediate DB write + local sync)
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Adds a user to the local cache.
     * The caller is responsible for the DB write (database.addUser) first.
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Removes a user from the local cache by username.
     * The caller is responsible for the DB delete (database.deleteUser) first.
     * @return true if the user was found and removed
     */
    public boolean removeUser(String username) {
        return users.removeIf(u -> u.getUsername().equalsIgnoreCase(username));
    }

    /**
     * Updates a user's password in the local cache.
     */
    public boolean updateUserPassword(String username, String newPassword) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                u.setPassword(newPassword);
                return true;
            }
        }
        return false;
    }

    // ── Auth helpers (replaces DB round-trips) ────────────────────────────────

    /**
     * Returns the user's type if credentials match, or null if not found.
     * This is the in-memory replacement for database.getUserType().
     */
    public String getUserType(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u.getType();
            }
        }
        return null;
    }

    /**
     * Returns true if credentials match any user.
     * This is the in-memory replacement for database.authUser().
     */
    public boolean authUser(String username, String password) {
        return getUserType(username, password) != null;
    }

    /** Returns an unmodifiable view of the user list. */
    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // DRUG operations  (local + pending queue)
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Adds a new drug to the local cache and marks it as pending for DB write.
     */
    public void addDrug(Drug drug) {
        drugs.add(drug);
        pendingDrugs.add(drug);
    }

    /**
     * Removes a drug from the local cache by id.
     */
    public boolean removeDrug(int drugId) {
        return drugs.removeIf(d -> d.getId() == drugId);
    }

    /** Returns an unmodifiable view of the drug list. */
    public List<Drug> getDrugs() {
        return Collections.unmodifiableList(drugs);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // STOCK operations  (local + pending queue)
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Adds a new stock entry to the local cache and marks it as pending.
     */
    public void addStock(Stock stock) {
        stocks.add(stock);
        pendingStocks.add(stock);
    }

    /**
     * Adjusts the quantity of a stock entry in memory.
     * Does NOT write to DB — call flushPendingToDatabase() when ready.
     */
    public boolean adjustQuantity(String sku, int delta) {
        for (Stock s : stocks) {
            if (s.getSku().equalsIgnoreCase(sku)) {
                s.setQuantity(s.getQuantity() + delta);
                if (!pendingStocks.contains(s)) pendingStocks.add(s);
                return true;
            }
        }
        return false;
    }

    /** Returns an unmodifiable view of the full stock list. */
    public List<Stock> getStocks() {
        return Collections.unmodifiableList(stocks);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // FLUSH  — write pending changes to the database
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Persists all pending drugs and stock entries to the database.
     * Call this on a background thread or when the employee explicitly saves.
     */
    public void flushPendingToDatabase(Database db) {
        // Flush pending drugs
        for (Drug d : new ArrayList<>(pendingDrugs)) {
            boolean ok = db.addDrug(d.getName(), d.getType());
            if (ok) pendingDrugs.remove(d);
        }

        // Flush pending stock entries
        for (Stock s : new ArrayList<>(pendingStocks)) {
            boolean ok = db.addStock(s);
            if (ok) {
                pendingStocks.remove(s);
            }
        }

        System.out.println("[LocalDataStore] Flush complete. "
            + "Remaining pending — drugs: " + pendingDrugs.size()
            + ", stocks: " + pendingStocks.size());
    }

    /** How many records are waiting to be written to the database. */
    public int pendingCount() {
        return pendingDrugs.size() + pendingStocks.size();
    }
    
    /**
 * Removes a stock entry from the local cache by id.
 * The caller is responsible for the DB delete first.
 */
    public boolean removeStock(int stockId) {
        return stocks.removeIf(s -> s.getId() == stockId);
    }
    
    public boolean updateUsername(String oldUsername, String newUsername) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(oldUsername)) {
                u.setUsername(newUsername);
                return true;
            }
        }
        return false;
    }

    public boolean updateUserType(String username, String newType) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                u.setType(newType);
                return true;
            }
        }
        return false;
    }
}
