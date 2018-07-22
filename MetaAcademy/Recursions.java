import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.StringTokenizer;

public class Recursions {
	
	/**
	 * Calculates the greatest common divisor (GCD) of two integers.
	 * @param a
	 * @param b
	 * @return 
	 */
	public static int gcd(int a, int b) {
		//base case when b is 0, gcd is a
		if(b == 0) {
			System.out.println("gcd(" + a + ", 0) = " + a);
			return a;
		} else {
			System.out.println("gcd(" + a + ", " + b + ") = " + "gcd(" + b + ", " + a % b + ")");
			return gcd(b, a % b);
		}
	}

	/**
	 * Calculates a personalized curriculum of required concepts according to a particular course and goal.
	 * @param prereqMap
	 * @param goal
	 */
	public static void personalCurriculum(HashMap<String, ArrayList<String>> prereqMap, String goal) {
		HashSet<String> used = new HashSet<String>();
		allPreqsOfConcept(used, prereqMap, goal);
	}
	
	/**
	 * Private helper method for calculating personal curriculum.
	 * @param concept
	 * @param prereqMap
	 */
	private static void allPreqsOfConcept(HashSet<String> used, HashMap<String, ArrayList<String>> prereqMap, String goal) {
		ArrayList<String> preqs = prereqMap.get(goal);
		//base case when a course have no more prerequisites
		if(preqs == null) {
			if(!used.contains(goal)) {
				System.out.println(goal);
				used.add(goal);
			}
		} else {
			for(String concept : preqs) {
				allPreqsOfConcept(used, prereqMap, concept);
			}
			if(!used.contains(goal)) {
				System.out.println(goal);
				used.add(goal);
			}
		}
	}

	/**
	 * Generates a random question from a grammar
	 * @param grammar
	 * @param symbol
	 * @return str
	 */
	public static String generate(HashMap<String, ArrayList<String>> grammar, String symbol) {
		ArrayList<String> rules = grammar.get(symbol);
		String str = "";
		String rule = rules.get((int)(Math.random()*(rules.size() - 1)));
//		Random rand = new Random();
//		int randInt = rand.nextInt(rules.size() - 1);
//		String rule = rules.get(randInt);
		StringTokenizer scanner = new StringTokenizer(rule, "[ ;\n.<>]", true);
	    while(scanner.hasMoreTokens()){
	    		String token = scanner.nextToken();
	    		//base case when the token is a terminal, add the token directly
	    		if(isTerminal(grammar, token)) {
	    			str = str + token;
	    		} else {
	    			str = str + generate(grammar, token);
	    		}
	    }
		return str;  
	}
	
	/**
	 * Private helper method to determine whether a string is terminal
	 * @param grammar
	 * @param rule
	 * @return
	 */
	private static boolean isTerminal(HashMap<String, ArrayList<String>> grammar, String rule){
		StringTokenizer scanner = new StringTokenizer(rule, "[ ;\n.<>]", true);
	    while(scanner.hasMoreTokens()){
	        String token = scanner.nextToken();
	        if(grammar.containsKey(token))
	            return false;
	    }
	    return true;
	}
}
