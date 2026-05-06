/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LocalData;

/**
 *
 * @author ROG
 */
public class Drug {
    private int    id;
    private String name;
    private String type;
 
    public Drug(int id, String name, String type) {
        this.id   = id;
        this.name = name;
        this.type = type;
    }
 
    public int    getId()   { return id;   }
    public String getName() { return name; }
    public String getType() { return type; }
 
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
 
    @Override
    public String toString() {
        return name + " [" + type + "]";
    }
}

