import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

/**
 * This class contains methods and implementations of a boggle board.
 * @author yiyedang
 *
 */
public class Boggle {
	private TreeSet<String> dictionary;             // store all the words in the dictionary	
	private Grid<Cube> board;                       // store the current board
	private TreeSet<String> humanWords;             // store the words found by human
	private TreeSet<String> compWords;   		// store the words found by computer
	// letters on all 6 sides of every cube
	private static final String[] CUBES = {
		    "AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS",
		    "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
		    "DISTTY", "EEGHNW", "EEINSU", "EHRTVW",
		    "EIOSST", "ELRTTY", "HIMNQU", "HLNNRZ"
		};

	/**
	 * An inner class to represent the Cube.
	 * @author hanjiedeng
	 */
	private class Cube{
		private char letter;
		private boolean visited;
		public Cube(char letter) {
			this.letter = letter;
			this.visited = false;
		}
	}
	
	/**
	 * Constructs an empty boggle 
	 * @param dictionary
	 */
	public Boggle(TreeSet<String> dictionary) {
		this.dictionary = dictionary;
		board = new Grid<Cube>(4,4);
		humanWords = new TreeSet<String>();
		compWords = new TreeSet<String>();
	}
	
	/**
	 * Sets up the game board.
	 * @param boardText
	 */
	public void setBoard(String boardText) {
		//if the user chooses to generate a random board
		if(boardText.equals("")) {
			//shake the board 
			shuffle(CUBES);
			//randomly set the face-up side of each cube
			for(int row = 0; row < board.numRows(); row++) {
				for(int col = 0; col < board.numCols(); col++) {
					Random rand = new Random();
					int num = rand.nextInt(6);
					char letter = CUBES[row * board.numRows() + col].charAt(num);
					board.set(row, col, new Cube(letter));
				}
			}
		} else {
			//set up the manual board
			boardText = boardText.toUpperCase().trim();
			for(int row = 0; row < board.numRows(); row++) {
				for(int col = 0; col < board.numCols(); col++) {
					char letter = boardText.charAt(row * board.numRows() + col);
					board.set(row, col, new Cube(letter));
				}
			}
		}
	}
	
	/**
	 * Shuffle the Strings in the cubes in a random way.
	 * @param cubes an array of Strings
	 */
	private void shuffle(String[] cubes) {
		for(int i = 0; i < cubes.length; i++) {
			int j = (int)(Math.random() * cubes.length);
			if(i != j) {
				String temp = cubes[i];
				cubes[i] = cubes[j];
				cubes[j] = temp;
			}
		}
	}
	
	/**
	 * Get the letter at the index (row, col)
	 * @param row the row number
	 * @param col the column number
	 * @return the letter at the index (row, col)
	 */
	public char getLetter(int row, int col) {
		return board.get(row, col).letter;
	}
	
	public void setVisited(int row, int col, boolean visited) {
		board.get(row, col).visited = visited;
	}
	
	public boolean getVisited(int row, int col) {
		return board.get(row, col).visited;
	}
	/**
	 * Gets the set of words found by human
	 * @return human word set
	 */
	public TreeSet<String> getHumanWords(){
		return this.humanWords;
	}
	
	/**
	 * Check to see if the parameter word is suitable to search for on the board.
	 * @param word
	 * @return whether the word is suitable
	 */
	public boolean checkWord(String word) {
		word = word.toUpperCase().trim();
		if(word.length() >= 4 && dictionary.contains(word) && !humanWords.contains(word)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Recursively search the word in the boggle board.
	 * @param word the String to be searched
	 * @return true if the word can be formed in the boggle; false if not;
	 */
	public boolean humanWordSearch(String word) {
		word = word.toUpperCase().trim();
		for(int row = 0; row < board.numRows(); row++) {
			for(int col = 0; col < board.numCols(); col++) {
				if(word.charAt(0) == getLetter(row, col)) {
					if(humanHelper(word, row, col)) {
						humanWords.add(word);
						return true;
					}
				}
			}
		}
	    return false;
	}
	
	/**
	 * Private helper method to explore and look for the word on the board.
	 * @param word
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean humanHelper(String word, int row, int col) {
		// base case 1: if the word is found
		if(word.equals("")) {
			return true;
		}
		//base case 2: if the position is out of bounds or is visited
		else if(!board.inBounds(row, col) || getVisited(row, col)) {
			return false;
		}
		else {
			if(word.length() > 1) {
				word = word.substring(1, word.length() - 1);
			}
			else {
				word = "";
			}
			setVisited(row, col, true);
			//if the first char of the word could be found 
			if(humanHelper(word, row - 1, col - 1) || humanHelper(word, row - 1, col) ||
					humanHelper(word, row - 1, col + 1) || humanHelper(word, row, col - 1)
					|| humanHelper(word, row, col + 1) || humanHelper(word, row + 1, col - 1)
					||humanHelper(word, row + 1, col) ||humanHelper(word, row + 1, col + 1)) {
				return true;
			}
			setVisited(row, col, false);
			return false;
		}
	}

	/**
	 * Calculate the score human get.
	 * @return the score human get.
	 */
	public int getScoreHuman() {
		int score = 0;
		Iterator<String> mark = humanWords.iterator();
		while(mark.hasNext()) {
			score += mark.next().length() - 3;
		}
		return score;
	}
	
	/**
	 * Find all the words that can be formed in the boggle board,
	 * except those already found by human player.
	 * @return a TreeSet of words found by computer.
	 */
	public TreeSet<String> computerWordSearch(){
		//find all remained words on the board
		for(int row = 0; row < board.numRows(); row++) {
			for(int col = 0; col < board.numCols(); col++) {
				compHelper("", row, col);
			}
		}
		return compWords;
	}
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @param string
	 */
	private void compHelper(String word, int row, int col) {
		// base case 1: if the word is found
		if(!board.inBounds(row, col) || getVisited(row, col)) {
			return;
		} else {
			word += getLetter(row, col);
			setVisited(row, col, true);
			//if the word is valid, add it to compWords
			if(checkWord(word)) {
				compWords.add(word);
			}
			//if the prefix exists in the dictionary, explore eight neighbors
			if(containsPrefix(dictionary, word)) {
				compHelper(word, row - 1, col - 1);
				compHelper(word, row - 1, col);
				compHelper(word, row - 1, col + 1);
				compHelper(word, row, col - 1);
				compHelper(word, row, col + 1);
				compHelper(word, row + 1, col - 1);
				compHelper(word, row + 1, col);
				compHelper(word, row + 1, col + 1);
			}
			setVisited(row, col, false);
		}
	}
	
	/**
	 * See if the prefix exists in the dictionary.
	 * @param dictionary a tree set of words
 	 * @param prefix a prefix
	 * @return true if the prefix exists in the dictionary; false if not.
	 */
	private boolean containsPrefix(TreeSet<String> dictionary, String prefix) {
		if(dictionary.subSet(prefix, prefix + Character.MAX_VALUE).isEmpty()) {
			return false;
		}else{
			return true;
		}
	}

	/**
	 * Calculate the score the computer gets.
	 * @return the score the computer gets.
	 */
	public int getScoreComputer() {
		int score = 0;
		Iterator<String> mark = compWords.iterator();
		while(mark.hasNext()) {
			score += mark.next().length() - 3;
		}
		return score;
	}
	
	/**
	 * Returns a 2D-String representation of the boggle board.
	 * @return str
	 */
	public String toString() {
		String str = "";
		for(int row = 0; row < board.numRows(); row++) {
			for(int col = 0; col < board.numCols(); col++) {
				str += getLetter(row, col);
			}
			str+= "\n";
		}
		return str;
	}
	
	/**
	 * Set all the cubes on the board to be unvisited.
	 */
	public void reset() {
		for(int i = 0; i < board.numRows(); i++) {
			for(int j = 0; j < board.numCols(); j++) {
				board.get(i, j).visited = false;
			}
		}
	}
	
	/**
	 * Prints out the word set in a specified way
	 * @param words
	 * @return a string of the line to be printed
	 */
	public String printWords(TreeSet<String> words) {
		String result = "";
		ArrayList<String> list = new ArrayList<String>();
		for(String word : words) {
			list.add(word);
		}
		for(int i = 0; i < list.size() - 1; i++){
			result += "\"" + list.get(i) + "\"" + ", ";
		}
		result += "\"" + list.get(list.size() - 1) + "\"";
		return result;
	}
}
