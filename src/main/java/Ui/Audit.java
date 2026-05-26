package Ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * @author ROG
 */
public class Audit {

    // ── Singleton instance ────────────────────────────────────────────────────
    private static Audit instance;

    // ── Table reference and model ─────────────────────────────────────────────
    private JTable table;
    private DefaultTableModel model;

    // ── Timestamp format ──────────────────────────────────────────────────────
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");

    // ── Column indices ────────────────────────────────────────────────────────
    private static final int COL_DATE    = 0;
    private static final int COL_TYPE    = 1;
    private static final int COL_MESSAGE = 2;

    private Audit(JTable table) {
        this.table = table;
        setupModel();
    }
    
    public static void init(JTable auditTable) {
        instance = new Audit(auditTable);
    }

    public static void log(String type, String message) {
        if (instance == null) return;   // guard — not yet initialised
        instance.addEntry(type, message);
    }

    private void setupModel() {
        model = new DefaultTableModel(
            new String[]{ "Date & Time", "Event", "Message" }, 0
        );

        table.setModel(model);

        if (table.getColumnModel().getColumnCount() == 3) {
            table.getColumnModel().getColumn(COL_DATE).setPreferredWidth(160);
            table.getColumnModel().getColumn(COL_TYPE).setPreferredWidth(90);
            table.getColumnModel().getColumn(COL_MESSAGE).setPreferredWidth(500);
        }

        table.setAutoCreateRowSorter(true);
    }

    // run on edt!!
    private void addEntry(String type, String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        Runnable r = () -> model.insertRow(0, new Object[]{ timestamp, type, message });
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }
}