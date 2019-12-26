package main;

import java.awt.EventQueue;
import view.ViewGUI;

/** 
 * Main program to run the money management progaram.
 * 
 * @author Victor 
 */
public final class MoneyMain { 
    
    /**
     * Private constructor. Do nothing.
     */
    private MoneyMain() {
        
    }
    
    /**
     * Main method. Runs the program.
     * 
     * @param args
     */
   public static void main(final String... args) {  
       EventQueue.invokeLater(new Runnable() {
           @Override
           public void run() {
               new ViewGUI();     
           }
       });
   }
}
