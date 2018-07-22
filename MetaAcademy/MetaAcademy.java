import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MetaAcademy{
	
	private static Scanner stdIn = new Scanner(System.in);
	
	public static void main(String[] args) {
		while(true) {
	        System.out.println("Welcome to Meta Academy. Coming online soon...");
	        System.out.println("1. Greatest Common Divisor");
	        System.out.println("2. Personal curriculum");
	        System.out.println("3. Generate question");
	        System.out.println("4. Exit");
	        System.out.print("What do you want? ");
	        int choice = stdIn.nextInt();
	        if(choice == 4) break;
	        switch(choice) {
		        case 1: greatestCommonDivisor(); break;
		        case 2: makePersonalCurriculum(); break;
		        case 3: generateProblem(); break;
		        default: invalidChoice(); break;
	        }
	        System.out.println();
	    }
	    System.out.print("Have a nice day!");
	}
	
	
	/**
	 * Function: Demo Definition
	 * -------------------------
	 * Teaches students about recursion by definition then shows an interactive
	 * demo of Euclid's GCD algorithm.
	 */
	private static void greatestCommonDivisor() {
		System.out.println();
		System.out.println("Some operations are much easier to define recursively. ");
		System.out.println("One amazing example of this is Euclid's Algorithm to ");
		System.out.println("calculate the greatest common divisor (gcd). In the algorithm ");
		System.out.println("Euclid famously shows that the gcd(a, b) is equal to ");
		System.out.println("gcd(b, r) where r is the remainder when you divide a by b. ");
		System.out.println("In the case where b is equal to 0, gcd(a, 0) is simply a. ");
		System.out.println("Since gcd is defined recursively, it is much easier to program ");
		System.out.println("using recursion. Let's calculate gcd.");
		System.out.println();
		
		System.out.print("Enter a: ");
	    int a = stdIn.nextInt();
	    System.out.print("Enter b: ");
	    int b = stdIn.nextInt();

	    int result = Recursions.gcd(a, b);

	    System.out.println("The greatest common divisor of " + a + " and " + b + " is " + result + ".");
	    System.out.println();
	}
	
	
	/**
	 * Function: Make Personal Curriculum
	 * -----------------------------------
	 * Asks the learner what course they are working on and their goal concept.
	 * Outputs a list of concepts they should learn (in order) to get to learn
	 * their goal concept.
	 * @throws IOException 
	 */
	private static void makePersonalCurriculum(){
		System.out.println();
	    String fileName = getPreReqFileName();
	    HashMap<String, ArrayList<String>> prereqMap = loadPrereqMap(fileName);
	    String goal = "";
	    while(!prereqMap.containsKey(goal)){
	        String prompt = "Enter the concept the student would like to learn ";
	        prompt = prompt + "(or ? to list concepts): ";
	        System.out.print(prompt);
	        goal = stdIn.nextLine();
	        if(goal.equals("?")) {
	            for(String key : prereqMap.keySet()) {
	                System.out.println(key);
	            }
	        } else if(!prereqMap.containsKey(goal)) {
	            System.out.println("Invalid concept.");
	        }
	    }
	    
	    System.out.println("The order you should learn concepts:");
	    Recursions.personalCurriculum(prereqMap, goal);
	    System.out.println();
	}
	
	
	/**
	 * Function: Get Prereq File Name
	 * -------------------------
	 * A helper function that asks the user for a course until it is
	 * able to find the corresponding prereq file.
	 */
	private static String getPreReqFileName() {
	    while(true) {
	    		System.out.print("What course? ");
	    		String course = stdIn.nextLine(); // avoid the "\n" trash from the previous input
	        course = stdIn.nextLine();
	        String prereqFile = "file/" + course + "-prereq.txt";
	        File file = new File(prereqFile);
	        if(file.exists()) {
	            return prereqFile;
	        }
	        System.out.println("could not find prereq file for course.");
	    }
	}
	
	/**
	 * Function: Load Prereq Map
	 * -------------------------
	 * Loads a prerequisite map from file.
	 * @throws IOException 
	 */
	private static HashMap<String, ArrayList<String>> loadPrereqMap(String fileName) {
	    HashMap<String, ArrayList<String>> prereqMap = new HashMap<String, ArrayList<String>>();
	    try {
	    		BufferedReader fileStream = new BufferedReader(new FileReader(fileName));
		    String line;
		    while(true) {
		    		line = fileStream.readLine();
		    		if(line == null) break;
		    		
		        ArrayList<String> parts = stringSplit(line, ":");
		        if(parts.size() != 2) continue;
		        
		        String key = parts.get(0);
		        String values = parts.get(1);
		        ArrayList<String> prereqs = stringSplit(values, ",");
		        prereqMap.put(key, prereqs);
		    }
		    
		    fileStream.close();
	    }catch(IOException e) {
	    		e.printStackTrace();
	    }
	    
	    return prereqMap;
	}

	
	/**
	 * Function: Split a line by delimiter
	 * -------------------------
	 * Does what it says.
	 */
	private static ArrayList<String> stringSplit(String line, String delim){
		String[] tokens = line.split(delim);
		ArrayList<String> parts = new ArrayList<String>();
		for(String token : tokens) {
			parts.add(token);
		}
		return parts;
	}
	
	
	/**
	 * Function: Generate Problem
	 * -------------------------
	 * A helper function that takes in a file name and loads in
	 * the corresponding grammar, returning it in 
	 * HashMap<String, ArrayList<String> > form. This grammar loader is built
	 * to be able to handle production rules that are multiple lines long.
	 * @throws IOException 
	 */
	private static void generateProblem(){
		System.out.print("What grammar? ");
		stdIn.nextLine(); // avoid trash input
	    String fileName = "file/" + stdIn.nextLine();
	    while(true) {
	    		System.out.print("What symbol (empty string to exit): ");
	        String symbol = stdIn.nextLine();
	        if(symbol.equals("")) break;
	        HashMap<String, ArrayList<String>> grammar = loadGrammar(fileName);
	        System.out.println("grammar size " + grammar.size());
	        String str = Recursions.generate(grammar, symbol);
	        System.out.println(str + "\n");
	    }
	}
	
	
	/**
	 * Function: Load Grammar
	 * -------------------------
	 * A helper function that takes in a file name and loads in
	 * the corresponding grammar, returning it in 
	 * HashMap<String, ArrayList<String>> form. This grammar loader is build 
	 * to be able to handle production rules that are multiple lines long.
	 * @throws IOException 
	 */
	public static HashMap<String, ArrayList<String>> loadGrammar(String fileName){
	    HashMap<String, ArrayList<String>> grammar = new HashMap<>();
	    
	    try {
	    		BufferedReader fileStream = new BufferedReader(new FileReader(fileName));
		    String key = "", line = "", currExpansion = "";
		    
		    while(true) {
		    		line = fileStream.readLine();
		    		if(line == null) break;
		    		
		        if(line.equals("-----")) {
		        		if(grammar.containsKey(key)) {
		        			grammar.get(key).add(currExpansion);
		        		}else {
		        			ArrayList<String> value = new ArrayList<String>();
		        			value.add(currExpansion);
		        			grammar.put(key, value);
		        		}
		            key = "";
		            currExpansion = "";
		        }
		        
		        ArrayList<String> parts = stringSplit(line, ":");
		        if(parts.size() == 2) {
		            key = parts.get(0);
		            String values = parts.get(1);
		            ArrayList<String> expansions = stringSplit(values, "\\|");
		            grammar.put(key, expansions);
		            currExpansion = grammar.get(key).remove(expansions.size() - 1);
		        } else {
		            currExpansion += "\n" + line;
		        }
		    }
		    
		    fileStream.close();
		    
	    }catch(IOException e) {
	    		System.out.println(e.getMessage());
	    }
	    
	    return grammar;
	}
	
	/**
	 * Function: Invalid Choice
	 * -------------------------
	 * Does what it says.
	 */
	private static void invalidChoice() {
	    System.out.println("Invalid choice");
	}
}
