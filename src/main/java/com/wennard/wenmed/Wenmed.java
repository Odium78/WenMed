/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.wennard.wenmed;

import javax.swing.SwingUtilities;

/**
 *
 * @author ROG
 */
public class Wenmed {
    public static void main(String[] args) { 
        System.out.println("Hello World!");
        
        ConnectDB db = new ConnectDB("jdbc:sqlite:data.db");
        
        // run window on separate thread
        SwingUtilities.invokeLater(Window::new);
    }
}
