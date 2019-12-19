package main;

import java.awt.EventQueue;
import view.ViewGUI;

/** 
 * Main program to run the money management progaram.
 * 
 * @author Victor 
 */
public class MoneyMain { 
    
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
       
       /*
      Random rand = new Random();
      Scanner console = new Scanner(System.in);
      Expenses test = new Expenses();
      double amount;
      String note;
      int choice = 1;
      
      while (choice != 0) 
      {
         System.out.println("----------------------------------------Menu----------------------------------------");
         System.out.println("1. Add expenses.");
         System.out.println("2. Add income.");
         System.out.println("3. See FULL summary.");
         System.out.println("4. Clear all files.");
         System.out.println("0. Exit.");
         System.out.print(">> Input: ");
         choice = console.nextInt();
         while (choice < 0 || choice > 4) 
         {
            System.out.print(">> Input (0-3) : ");
            choice = console.nextInt();           
         }
         switch (choice)
         {
            case 1: test.addDeduct(console);
                     break;
            case 2: test.addDepos(console);
                     break; 
            case 3: test.summary();
                     break;
            case 4: test.clearFiles(console);
                     break;
            default: System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tok");
            break;
         }
         
         
      }
         */   

   }
   
   
}
