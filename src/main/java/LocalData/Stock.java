/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LocalData;

/**
 *
 * @author ROG
 */

public class Stock {

    private int     id;
    private int     drugId;
    private String  sku;
    private String  dosage;
    private String  form;
    private String  packaging;
    private double  origPrice;
    private double  price;
    private double  discount;
    private int     quantity;
    private int     minStock;
    private String  unit;
    private boolean available;
    private String  imageId;
    private String  supName;
    private String  lastRestock;
    private String  expDate;
    private String  createdAt;
    private String  updatedAt;

    /** Full constructor — used when loading from the database. */
    public Stock(int id, int drugId, String sku, String dosage, String form,
                 String packaging, double origPrice, double price, double discount,
                 int quantity, int minStock, String unit, boolean available,
                 String imageId, String supName, String lastRestock, String expDate,
                 String createdAt, String updatedAt) {
        this.id          = id;
        this.drugId      = drugId;
        this.sku         = sku;
        this.dosage      = dosage;
        this.form        = form;
        this.packaging   = packaging;
        this.origPrice   = origPrice;
        this.price       = price;
        this.discount    = discount;
        this.quantity    = quantity;
        this.minStock    = minStock;
        this.unit        = unit;
        this.available   = available;
        this.imageId     = imageId;
        this.supName     = supName;
        this.lastRestock = lastRestock;
        this.expDate     = expDate;
        this.createdAt   = createdAt;
        this.updatedAt   = updatedAt;
    }

    /**
     * Convenience constructor for new stock entries that dont have a DB id
     * yet (id = -1 until flushed).
     */
    public Stock(int drugId, String sku, String dosage, String form,
                 String packaging, double origPrice, double price, double discount,
                 int quantity, int minStock, String unit, boolean available,
                 String imageId, String supName, String lastRestock, String expDate) {
        this(-1, drugId, sku, dosage, form, packaging, origPrice, price, discount,
             quantity, minStock, unit, available, imageId, supName, lastRestock,
             expDate, "", "");
    }

    public int     getId()          { return id;          }
    public int     getDrugId()      { return drugId;      }
    public String  getSku()         { return sku;         }
    public String  getDosage()      { return dosage;      }
    public String  getForm()        { return form;        }
    public String  getPackaging()   { return packaging;   }
    public double  getOrigPrice()   { return origPrice;   }
    public double  getPrice()       { return price;       }
    public double  getDiscount()    { return discount;    }
    public int     getQuantity()    { return quantity;    }
    public int     getMinStock()    { return minStock;    }
    public String  getUnit()        { return unit;        }
    public boolean isAvailable()    { return available;   }
    public String  getImageId()     { return imageId;     }
    public String  getSupName()     { return supName;     }
    public String  getLastRestock() { return lastRestock; }
    public String  getExpDate()     { return expDate;     }
    public String  getCreatedAt()   { return createdAt;   }
    public String  getUpdatedAt()   { return updatedAt;   }

    public void setId(int id)                   { this.id = id;                   }
    public void setQuantity(int quantity)        { this.quantity = quantity;        }
    public void setPrice(double price)           { this.price = price;             }
    public void setDiscount(double discount)     { this.discount = discount;       }
    public void setAvailable(boolean available)  { this.available = available;     }
    public void setLastRestock(String lastRestock){ this.lastRestock = lastRestock; }
    public void setUpdatedAt(String updatedAt)   { this.updatedAt = updatedAt;     }

    @Override
    public String toString() {
        return sku + " | qty:" + quantity + " | $" + price;
    }
}