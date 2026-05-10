/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ui;

/**
 *
 * @author ROG
 */
public class Cart {
    private final String name;
    private final String sku;
    private final int    quantity;
    private final double unitPrice;

    public Cart(String name, String sku, int quantity, double unitPrice) {
        this.name      = name;
        this.sku       = sku;
        this.quantity  = quantity;
        this.unitPrice = unitPrice;
    }

    public String getName()      { return name;      }
    public String getSku()       { return sku;       }
    public int    getQuantity()  { return quantity;  }
    public double getUnitPrice() { return unitPrice; }
    public double getTotal()     { return unitPrice * quantity; }

    @Override
    public String toString() {
        // What shows up in jList1
        return String.format("%s [%s]  x%d  ₱%.2f", name, sku, quantity, getTotal());
    }
}