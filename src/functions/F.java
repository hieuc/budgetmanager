package functions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main function class for the program.
 * 
 * @author Victor
 * @version December 2019
 */
public final class F {
    /** Root path to data files. */
    private static final String ROOT_PATH = System.getProperty("user.dir") + "/data";
    
    /** Path to deposit file. */
    private static final String DEPOS_PATH = ROOT_PATH + "/deposit.txt";
    
    /** Path to deduction file. */
    private static final String DEDUCT_PATH = ROOT_PATH + "/deduction.txt";
    
    /** Line separator character. */
    private static final String LINE_SEPARATOR = "\\|";
    
    /** Reader handles inputs from files. */
    private static BufferedReader reader;
    
    /** Writer handles writing to files. */
    private static BufferedWriter writer;
    
    /**
     * Private constructor prevents outside inits.
     */
    private F() {
        
    }
    
    /**
     * Read the deposit data.
     * 
     * @return List of strings of deposit data.
     */
    public static List<String> readDepos() {
        //final StringBuilder data = new StringBuilder();
        //readFile(DEPOS_PATH).forEach(s -> data.append(s + "\n"));;
        return readFile(DEPOS_PATH);
    }
    
    /**
     * Read the deduction data.
     * 
     * @return List of strings of deduction data.
     */
    public static List<String> readDeduct() {
        //final StringBuilder data = new StringBuilder();
        //readFile(DEDUCT_PATH).forEach(s -> data.append(s + "\n"));;
        return readFile(DEDUCT_PATH);
    }
    
    /**
     * Take a data line and separate it to 3 parts: comment-amount-date. 
     * 
     * @param line being parsed
     * @return a String array of 3 parts
     */
    public static String[] parseLine(final String line) {
        return line.split(LINE_SEPARATOR);
    }
    
    /**
     * Calculate total price from list of lines.
     * 
     * @param lines of either deduction or deposit
     * @return calculated sum
     */
    public static double calcTotal(final List<String> lines) {
        double sum = 0;
        for (final String s : lines) {
            sum += Double.valueOf(parseLine(s)[1]);
        }
        return sum;
    }
    
    public static List<String> readDeductMonth(final int month, final int year){
        return readMonth(readDeduct(), month, year);
    }
    
    public static List<String> readDeposMonth(final int month, final int year){
        return readMonth(readDepos(), month, year);
    }
    
    /*
     * NOTE: readmonth should be able to handle year input.
     * flow: binary search for year, find start and end index, binary search for month, find start and end index.
     */
    private static List<String> readMonth(final List<String> lines, final int month, final int year) {
        // search for year index
        int index = binSearch(lines, 0, lines.size() - 1, year, false);
        if (index == -1) {
            return Arrays.asList("Data not found!");
        }
        
        // search index range
        int lower = index;
        int upper = index;
        while (getMY(lines.get(lower - 1), false) == year) {
            lower--;
        }
        while (upper < lines.size() - 1 && getMY(lines.get(upper + 1), false) == year) {
            upper++;
        }
        
        // search for month index
        index = binSearch(lines, lower, upper, month, true);
        if (index == -1) {
            return Arrays.asList("Data not found!");
        }
        // search for index range 
        lower = index;
        upper = index;
        while (getMY(lines.get(lower - 1), true) == month) {
            lower--;
        }
        while (upper < lines.size() - 1 && getMY(lines.get(upper + 1), true) == month) {
            upper++;
        }
        
        return new ArrayList<String>(lines.subList(lower, upper + 1));
    }
    
    /**
     * Binary search to final target month/year.
     * 
     * @param lines list of lines in the pool to search
     * @param l left bound
     * @param r right bound
     * @param target target value
     * @param m true if searching for month, false if searching for year
     * @return index of target
     */
    private static int binSearch(final List<String> lines, final int l, final int r,
            final int target, final boolean m) {
        if (r >= l) {
            final int mid = (l + r) / 2;
            // Target is at the middle
            if (mid >= l && mid <= r) {
                final int value = getMY(lines.get(mid), m);
                
                if (value == target) {
                    return mid;
                }
                
                // Target is smaller than mid
                if (target < value) {
                    return binSearch(lines, l, mid - 1, target, m);
                }
                
                // Target is larger than mid
                return binSearch(lines, mid + 1, r, target, m); 
            }
        }
        
        // Target not found
        return -1;
    }
    
    /**
     * Get month/year value from a line.
     * 
     * @param s line as a String.
     * @param m true if returning month, year otherwise
     * @return int value
     */
    private static int getMY(final String s, final boolean m) {
        if (m) { // month
            return Integer.valueOf(parseLine(s)[2].split("-")[1]);
        } // year
        return Integer.valueOf(parseLine(s)[2].split("-")[0].trim());
    }
    
    /**
     * Get data from a file.
     * 
     * @param path to file
     * @return list of lines in file.
     */
    private static List<String> readFile(final String path) {
        reader = initReader(path);
        final List<String> data = reader.lines().collect(Collectors.toList());
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    
    /**
     * Return a file Object and create the file if directory not found.
     * 
     * @param path 
     * @return the File object.
     */
    private static BufferedReader initReader(final String path) {
        BufferedReader result = null;
        try {
            final File file = new File(path);
            if (!file.exists()) {
                // make directory
                Files.createDirectories(Paths.get(ROOT_PATH));
                // make text file
                file.createNewFile();
            }
            result = Files.newBufferedReader(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
