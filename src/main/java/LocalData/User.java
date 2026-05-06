/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LocalData;

/**
 *
 * @author ROG
 */
public class User {
    private int    id;
    private String username;
    private String password;   // as is not encrypted
    private String type;
 
    public User(int id, String username, String password, String type) {
        this.id       = id;
        this.username = username;
        this.password = password;
        this.type     = type;
    }
 
    public int    getId()       { return id;       }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getType()     { return type;     }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setType(String type)         { this.type = type;         }
 
    @Override
    public String toString() {
        return username + " (" + type + ")";
    }

}
