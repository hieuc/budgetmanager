package main;

import java.util.*;
import java.io.*;
import java.time.*;

public class Expenses {
   private static final String DEDUCTION_FILE_PATH = "deduction.txt";
   private static final String DEPOSITE_FILE_PATH = "deposite.txt";
   PrintWriter wdd; // write
   PrintWriter wdp;
   Scanner rdd; //read
   Scanner rdp;
     
     
   public void initialize() { // initialize file scanner for reading
      try 
      {
         rdd = new Scanner(new FileReader(DEDUCTION_FILE_PATH));
         rdp = new Scanner(new FileReader(DEPOSITE_FILE_PATH));
      }
      catch (IOException e) 
      {
         System.out.println("IOException: " + e.getMessage());
      }
   }
   
   public Expenses(){ // constructor
      initialize();
   } 
   
   
   public void addDeduct(Scanner console) // deducting
   {
      double amount = 0;
      String note = "";
      String input = "n";
      
      while (input.charAt(0) =='n' || input.contains("no")) // if no in input any other char will be accepted as yes
      { // validate
         System.out.print("Enter amount: ");
         amount = console.nextDouble();
         System.out.print("Note: ");
         console.nextLine();
         note = console.nextLine();
         System.out.print("Is this correct? Amount: " + amount + " " + note);
         input = console.nextLine();
      }
      
      try 
      {
         wdd = new PrintWriter(new FileWriter(DEDUCTION_FILE_PATH, true));
      }
      
      catch (IOException e) 
      {
         System.out.println("IOException: " + e.getMessage());
      }
      
      wdd.println("'" + note + " '  " + amount + "  " + LocalDateTime.now().toString().substring(0,10)); // add to file
      wdd.close();
   }
   
   
   public void addDepos(Scanner console) // depositing
   {
      double amount = 0;
      String note = "";
      String input = "n";
      
      while (input.charAt(0) == 'n' || input.contains("no")) // if no in input
      { // validate
         System.out.print("Enter amount: ");
         amount = console.nextDouble();
         System.out.print("Note: ");
         console.nextLine();
         note = console.nextLine();
         System.out.print("Is this correct? Amount: " + amount + " " + note);
         input = console.nextLine();
      }

      try 
      {
         wdp = new PrintWriter(new FileWriter(DEPOSITE_FILE_PATH, true));
      }
      catch (IOException e) 
      {
         System.out.println("IOException: " + e.getMessage());
      }
      
      wdp.println("'" + note + " '  " + amount + "  " + LocalDateTime.now().toString().substring(0,10)); // add to file
      wdp.close();
   }
   
   
   public void summary() 
   {  
      double money; // to hold money in general
      double sumE = 0; // hold sum Expenses
      double sumI = 0; // hold sum income
      int stack = 0; // count the "'" char
      String note = "";
      String date = "";
      
      ArrayList<Double> expenses = new ArrayList<>(); // expense
      ArrayList<Double> income = new ArrayList<>(); // income
      
      System.out.println("----------------------------------------Summary----------------------------------------");
      System.out.println("Deduction: ");
      while (rdd.hasNext())  // read fro file
      {  
         
         if (rdd.hasNextDouble() && stack > 1) // check for doubles
         {  
            money = rdd.nextDouble();
            expenses.add(money); // add to array
            if (rdd.hasNext()) // date 
            {
               date = rdd.next();
            }
            System.out.printf("- %-40s $%10.2f %20s \n", note.substring(1, note.length() - 2), money, date);
            // reset
            note = "";
            date = "";
            stack = 0;
         }
         else 
         {
            String s = rdd.next();
            note += s + " "; // add note
            if (s.contains("'")) stack++; // check for ' char
         }
      }
      
      for (double a : expenses) sumE += a; 
      System.out.printf("%-42s $%10.2f \n", "Total expenses:", sumE);     
      
      System.out.println("\nDeposit: ");
      while (rdp.hasNext()) // read from file
      {
         if (rdp.hasNextDouble() && stack > 1) // check for doubles
         {  
            money = rdp.nextDouble(); // add to array
            income.add(money);
            if (rdp.hasNext()) // date 
            {
               date = rdp.next();
            }
            System.out.printf("- %-40s $%10.2f %20s \n", note.substring(1, note.length() - 2), money, date);
            note = "";
            date = "";
            stack = 0;
         }
         else 
         {
            String s = rdp.next();
            note += s + " "; // add note
            if (s.contains("'")) stack++; // check for ' char
         }
      }
      
      for (double a : income) sumI += a;
      System.out.printf("%-42s $%10.2f \n\n", "Total income:", sumI); 
      
      System.out.printf("%-42s $%10.2f \n", "Net:", sumI - sumE);  // net of income - deposite
      
      rdd.reset(); // clear buffer for next session
      rdp.reset();
      initialize(); // recreate scanner  
   }
   
   
   public void clearFiles(Scanner console) 
   {
      System.out.println("Do you really want to clear all files?");
      String input = console.nextLine();
      if (input.equals("")) input = console.nextLine(); 
      if (input.charAt(0) == 'y' || input.contains("ye"))
      {
         try
         {
            wdd = new PrintWriter(new FileWriter(DEDUCTION_FILE_PATH));
            wdp = new PrintWriter(new FileWriter(DEPOSITE_FILE_PATH));
         }
         catch (IOException e) 
         {
            System.out.println("IOException: " + e.getMessage());
         }
                  
         wdd.close();
         wdp.close();
         
         System.out.println("All files have been cleared.");
      }
   }
   
   
   public void saveFile() 
   {
   
   }
}