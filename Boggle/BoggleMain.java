import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeSet;
/**
 * This class contains implementations and methods of the boggle game.
 * @author yiyedang
 *
 */
public class BoggleMain {
	private static final String DICTIONARY_FILE = "dictionary.txt";
	private static Scanner scnr = new Scanner(System.in);
	/**
	 * This is the main method of the game
	 * @param args (not used)
	 */
	public static void main(String[] args) {
		System.out.print("Welcome to CS 106B Boggle!\n" + 
				"This game is a search for words on a 2-D board of letter cubes.\n" + 
				"The good news is that you might improve your vocabulary a bit.\n" + 
				"The bad news is that you're probably going to lose miserably to\n" + 
				"this little dictionary-toting hunk of silicon.\n" + 
				"If only YOU had a gig of RAM!\n" + 
				"\n" + "Press Enter to begin the game ... ");
		TreeSet<String> dictionary = new TreeSet<>();
	    readDictionaryFromFile(dictionary, DICTIONARY_FILE);
	   scnr.nextLine();
	    // play the game repeatedly until the user decides to quit
	    while (true) {
	        playOneGame(dictionary);   // your function
	        if (!getYesOrNo("Play again (Y/N)? ")) {
	            break;
	        }
	    }
	    System.out.println("Have a nice day.");
	}
	
	/**
	 * Plays one run of the game.
	 * @param dictionary
	 */
	private static void playOneGame(TreeSet<String> dictionary) {
		Boggle boggle = new Boggle(dictionary);
		String prompt = "Do you want to generate a random board? ";
		String boardText = "";
		if(getYesOrNo(prompt)) {
			boggle.setBoard(boardText);
		}
		else {
    			while(true) {
    				System.out.print("Type the 16 letters to appear on the board: ");
    				boardText = scnr.nextLine().toUpperCase();
    				if(isValid(boardText)) {
    					break;
    				}
    				else {
    					System.out.println("That is not a valid 16-letter board string. Try again.");
    				}
    			}
    			boggle.setBoard(boardText);
		}
		
		//human's turn
		System.out.println("==================== (console cleared) ====================");
		System.out.println("It's your turn! ");
		System.out.println(boggle);
		System.out.println("Your words (" + boggle.getHumanWords().size() + "): {" + boggle.getHumanWords() + "} ");
		System.out.println("Your score: " + boggle.getScoreHuman());
		System.out.print("Type a word (or Enter to stop): ");
		String word = scnr.nextLine();
		//always prompt for a word until the user stops
		while(true) {
			//if the word is suitable for search
			if(boggle.checkWord(word)) {
				System.out.println("==================== (console cleared) ====================");
				//if the word can be formed
				if(boggle.humanWordSearch(word)) {
					System.out.print("You found a new word! ");
	                System.out.println("\"" + word.toUpperCase()  + "\"");
				} else {
					System.out.println("That word can't be formed on this board.");
				}
			} else if (word.equals("")){
				break;
			}
			else {
				System.out.println("==================== (console cleared) ====================");
				System.out.println("You must enter an unfound 4+ letter word from the dictionary.");
			}
			System.out.print(boggle);
			System.out.println("Your words (" + boggle.getHumanWords().size() + "): {" + boggle.printWords(boggle.getHumanWords()) + "} ");
			System.out.println("Your score: " + boggle.getScoreHuman());
			System.out.print("Type a word (or Enter to stop): ");
			word = scnr.nextLine();
			//if the user enter, then quit human's turn
			if(word.equals("")) {
				break;
			}
			boggle.reset();
		}
		
		//computer's turn
		boggle.reset();
		System.out.println();
		System.out.println("It's my turn! ");
		System.out.println("My words (" + boggle.computerWordSearch().size() + "): {" + boggle.printWords(boggle.computerWordSearch()) + "} ");
		System.out.println("My score: " + boggle.getScoreComputer());
		//if the computer gets greater score than the human
		if(boggle.getScoreComputer() > boggle.getScoreHuman()) {
			System.out.println("Ha ha ha, I destroyed you. Better luck next time, puny human!\n");
		}
		else {
			System.out.println("WOW, you defeated me! Congratulations!");
		}
	}
	
	/**
	 * Checks the validity of the input board by the user.
	 * @param typeBoard
	 * @return whether the typed board is valid
	 */
	public static boolean isValid(String typeBoard) {
		//if the typed board is not 16 in length
		if(typeBoard.length() != 16) {
			return false;
		}
		//check whether each character of the typed board is a upper-case letter
		for(int i = 0; i < typeBoard.length(); i++) {
			if(typeBoard.charAt(i) < 'A' || typeBoard.charAt(i) > 'Z') {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Read all the words from filename and add them all to the dictionary.
	 * @param dict a HashSet of Strings to store words
	 * @param filename the relative path of the file storing words
	 */
	private static void readDictionaryFromFile(TreeSet<String> dictionary, String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String word = "";
			while(true) {
				word = br.readLine();
				if(word == null) break;
				dictionary.add(word.toUpperCase().trim());
			}
			br.close();
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
	
	}
	
	/**
	 * Prompt user to enter yes or no.
	 * @param prompt the message to be shown in the console
	 * @return true if the user enters 'Y' or 'y'; false if the user enters 'N' or 'n'.
	 */
	private static boolean getYesOrNo(String prompt) {
		System.out.print(prompt);
		String answer = scnr.nextLine();
		while(!(answer.equals("Y") || answer.equals("y") || answer.equals("N") || answer.equals("n"))) {
			System.out.println("Invalid command. Please enter 'Y' or 'y' to indicate yes; enter 'N' or 'n' to indicate no.");
			System.out.print(prompt);
			answer = scnr.nextLine();
		}
		
		if(answer.equals("Y") || answer.equals("y")) {
			return true;
		}
		else {
			return false;
		}
	}
}

