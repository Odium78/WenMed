package com.wennard.wenmed;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDB {
    private Connection connection;
    private String url;

    public ConnectDB(String url) {
        this.url = url;
        System.out.println("Initializing Database");
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Database Connected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                System.out.println("Closing Database");
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void makeTable() {
        String[] queries = {
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

        try (Statement stmt = connection.createStatement()) {
            for (String query : queries) {
                stmt.execute(query);
            }
            System.out.println("Tables created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}