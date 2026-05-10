/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LocalData;

/**
 *
 * @author ROG
 */
public class DrugDetail {
    private final String name;       // drugs.name
    private final String drugType;   // drugs.type  (e.g. "OTC", "Rx")
    private final String dosage;     // kiosk_inv.dosage
    private final String form;       // kiosk_inv.form   (tablet / syrup / …)
    private final String supName;    // kiosk_inv.sup_name
    private final double price;      // kiosk_inv.price
    private final String sku;        // kiosk_inv.sku  (handy for FK back to Stock)

    public DrugDetail(String name, String drugType, String dosage,
                      String form, String supName, double price, String sku) {
        this.name     = name;
        this.drugType = drugType;
        this.dosage   = dosage;
        this.form     = form;
        this.supName  = supName;
        this.price    = price;
        this.sku      = sku;
    }

    public String getName()     { return name;     }
    public String getDrugType() { return drugType; }
    public String getDosage()   { return dosage;   }
    public String getForm()     { return form;     }
    public String getSupName()  { return supName;  }
    public double getPrice()    { return price;    }
    public String getSku()      { return sku;      }

    @Override
    public String toString() {
        return name + " " + dosage + " " + form
             + " | " + drugType
             + " | $" + price
             + " | sup: " + supName;
    }
}

/**
 * A read-only snapshot combining one drugs row + one kiosk_inv row.
 * A single drug name may produce multiple DrugDetail entries
 * (e.g. Paracetamol 500 mg tablet vs 250 mg syrup).
 */
