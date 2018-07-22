import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class models the life cycle of bacteria using a two-dimensional grid of cells.
 * @author yiyedang
 *
 */
public class Game {
	/**
	 * This is the main method of the game of life. It gets a grid from a file, and 
	 * performs manipulations of the simulation by prompting command from the user
	 * @param args (not used)
	 */
	public static void main(String[] args) {
		//print the introductory welcome message
		System.out.println("Welcome to the CS 106B Game of Life,\n" + 
				"a simulation of the lifecycle of a bacteria colony.\n" + 
				"Cells (X) live and die by the following rules:\n" + 
				"- A cell with 1 or fewer neighbors dies.\n" + 
				"- Locations with 2 neighbors remain stable.\n" + 
				"- Locations with 3 neighbors will create life.\n" + 
				"- A cell with 4 or more neighbors dies.");
		Scanner scnr = new Scanner(System.in);
		System.out.println();
		//get a valid file name from the user
		System.out.print("Grid input file name? ");
		String fileName = scnr.nextLine();
		File f = new File("file"+ File.separatorChar + fileName);
		//prompt for user input of a valid file name
		while(!f.exists()) {
			System.out.print("Unable to open that file.  Try again.");
			fileName = scnr.nextLine();
			f = new File("file"+ File.separatorChar + fileName);
		}
		FileReader fr = null; // reference to a FileReader object
		BufferedReader br = null; // reference to a BufferedReader object
		Grid<Character> grid = null;
		try {
			fr = new FileReader("file"+ File.separatorChar + fileName);
			br = new BufferedReader(fr);
			//read the height and width of the grid from the file
			int height = Integer.parseInt(br.readLine());
			int width = Integer.parseInt(br.readLine());
			String line = null;
			int row = 0;
			grid = new Grid<Character>(height, width);//create a grid
		
			// Parse the content of the grid from the file line by line
			while(row < height && (line = br.readLine()) != null){
				for(int col = 0; col < width; col++) {
					Character cell = line.charAt(col);
					grid.set(row, col, cell);
				}
				row++;
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		finally { // close open resources
			if (br != null)
				try {
					br.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
		}
		//prompt the user for whether wrap or not
		System.out.print("Should the simulation wrap around the grid (y/n)? ");
		String command = scnr.nextLine().toLowerCase();
		boolean wrapping = false;
		//prompt a valid input for wrapping command
		while(!command.equals("y") && !command.equals("n")) {
			System.out.print("Invalid choice; please try again. ");
			command = scnr.nextLine();
		}
		if(command.equals("y")) {
			wrapping = true;
		}
		printGrid(grid);
		
		boolean keepPlay = true;
		boolean valid = true;
		//while the user keep playing instead of quitting
		while (keepPlay) {
			//if the input command is valid
			if(valid) {
				System.out.print("a)nimate, t)ick, q)uit? ");
			}
			else {
				System.out.print("Invalid choice; please try again.");
			}
			String input = scnr.nextLine().toLowerCase();//get the command from the user
			//if the command is to tick forward the bacteria simulation by one generation
			if(input.equals("t")) {
				valid = true;
				grid = tick(wrapping, grid);
				printGrid(grid);
			}
			//if the command is to begin an animation loop that ticks forward 
			//the simulation by several generations, once every 50 milliseconds
			else if(input.equals("a")) {
				valid = true;
				System.out.print("How many frames?");
				String frame = scnr.nextLine();
				boolean isInt = false;
				int frameNum = 0;
				//prompt for a valid integer input as the frame number
				try {
					frameNum = Integer.parseInt(frame);
					isInt = true;
				}catch(NumberFormatException e){
					isInt = false;
				}
				while( !isInt) {
					System.out.print("Illegal integer format. Try again.");
					frame = scnr.nextLine();
					try {
						frameNum = Integer.parseInt(frame);
						isInt = true;
					}catch(NumberFormatException e){
						isInt = false;
					}
				}
				//tick forward for a specified number of generations
				for(int i = 0; i < frameNum; i++) {
					grid = tick(wrapping, grid);
					System.out.println("===================="
							+ " (console cleared) ====================");
					printGrid(grid);
				}
			}
			//if the command is to quit the game
			else if(input.equals("q")) {
				System.out.println("Have a nice life!");
				break;
			}
			else {
				valid = false;
			}
		}
		
		scnr.close();
	}

	/**
	 * Ticks forward the simulation for one generation.
	 * @param wrapping
	 * @param grid
	 * @return grid
	 */
	public static Grid<Character> tick(boolean wrapping, Grid<Character> grid) {
		//create a temporary grid having the same content as the original grid
		Grid<Character> temp = new Grid<Character>(grid.height(), grid.width());
		for(int i = 0; i < grid.height(); i++) {
			for(int j = 0; j < grid.width(); j++) {
				temp.set(i, j, grid.get(i, j));
			}
		}
		//loop through all cells in the grid
		for(int row = 0; row < grid.height(); row++) {
			for(int col = 0; col < grid.width(); col++) {
				int count = 0;
				//if the mode is chosen to wrap
				if(wrapping) {
					count = numOfWrapping(row, col, grid);
				}
				else {
					count = numOfNonWrapping(row, col, grid);
				}
				//if the number of neighbors of the cell is 0 or 1
				if(count == 0 || count == 1) {
					temp.set(row, col, '-');
				}
				//if the number of neighbors of the cell is 2
				else if(count == 2) {
					temp.set(row, col, grid.get(row, col));
				}
				//if the number of neighbors of the cell is 3
				else if(count == 3) {
					temp.set(row, col, 'X');
				}
				else {
					temp.set(row, col, '-');
				}
			}
		}
		grid = temp;//assign the changed grid to the original one
		return grid;
	}

	/**
	 * Counts the number of neighbors of a cell in a wrapping mode
	 * @param row
	 * @param col
	 * @param grid
	 * @return
	 */
	public static int numOfWrapping(int row, int col, Grid<Character> grid) {
		int num = 0;
		// if the location is at the up-left corner
		if (row == 0 && col == 0) {
			for (int i = 0; i <= 1; i++) {
				for (int j = 0; j <= 1; j++) {
					if (grid.get(i, j) == 'X') {
						num++;
					}
				}
			}
			if (grid.get(row, col) == 'X') {
				num = num - 1;
			}
			if(grid.get(row, grid.width() - 1) == 'X') {
				num++;
			}
			if(grid.get(row+1, grid.width() - 1) == 'X') {
				num++;
			}
			if(grid.get(grid.height()-1, grid.width() - 1) == 'X'){
				num++;
			}
			if(grid.get(grid.height()-1, col) == 'X'){
				num++;
			}
			if(grid.get(grid.height()-1, col+1) == 'X'){
				num++;
			}
		}
		// if the location is at the bottom-right corner
		else if (row == grid.height() - 1 && col == grid.width() - 1) {
			for (int i = row - 1; i <= row; i++) {
				for (int j = col - 1; j <= col; j++) {
					if (grid.get(i, j) == 'X') {
						num++;
					}
				}
			}
			if (grid.get(row, col) == 'X') {
				num = num - 1;
			}
			if(grid.get(row, 0) == 'X') {
				num++;
			}
			if(grid.get(row-1, 0) == 'X') {
				num++;
			}
			if(grid.get(0, 0) == 'X'){
				num++;
			}
			if(grid.get(0, col) == 'X'){
				num++;
			}
			if(grid.get(0, col-1) == 'X'){
				num++;
			}
		}
		// if the location is at the up-right corner
		else if (row == 0 && col == grid.width()- 1) {
			for (int i = 0; i <= 1; i++) {
				for (int j = col - 1; j <= col; j++) {
					if (grid.get(i, j) == 'X') {
						num++;
					}
				}
			}
			if (grid.get(row, col) == 'X') {
				num = num - 1;
			}
			if(grid.get(row,0) == 'X') {
				num++;
			}
			if(grid.get(row+1, 0) == 'X') {
				num++;
			}
			if(grid.get(grid.height()-1, 0) == 'X'){
				num++;
			}
			if(grid.get(grid.height()-1, col) == 'X'){
				num++;
			}
			if(grid.get(grid.height()-1, col-1) == 'X'){
				num++;
			}
		}
		// if the location is at the bottom-left corner
		else if (row == grid.height() - 1 && col == 0) {
			for (int i = row - 1; i <= row; i++) {
				for (int j = 0; j <= 1; j++) {
					if (grid.get(i, j) == 'X') {
						num++;
					}
				}
			}
			if (grid.get(row, col) == 'X') {
				num = num - 1;
			}
			if(grid.get(0, col) == 'X') {
				num++;
			}
			if(grid.get(0, col+1) == 'X') {
				num++;
			}
			if(grid.get(row - 1, grid.width() - 1) == 'X'){
				num++;
			}
			if(grid.get(row, grid.width() - 1) == 'X'){
				num++;
			}
			if(grid.get(0, grid.width() - 1) == 'X'){
				num++;
			}
		}
		// if the location is at the up edge but not at corner
		else if (row == 0 && col != 0 && col != grid.width() - 1) {
			for (int i = 0; i <= 1; i++) {
				for (int j = col - 1; j <= col + 1; j++) {
					if (grid.get(i, j) == 'X') {
						num++;
					}
				}
			}
			if (grid.get(row, col) == 'X') {
				num = num - 1;
			}
			if(grid.get(grid.height() - 1, col) == 'X') {
				num++;
			}
			if(grid.get(grid.height() - 1, col - 1) == 'X') {
				num++;
			}
			if(grid.get(grid.height() - 1, col + 1) == 'X') {
				num++;
			}
		}
		// if the location is at the bottom edge but not at corner
		else if (row == grid.height() - 1 && col != 0 && col != grid.width() - 1) {
			for (int i = row - 1; i <= row; i++) {
				for (int j = col - 1; j <= col + 1; j++) {
					if (grid.get(i, j) == 'X') {
						num++;
					}
				}
			}
			if (grid.get(row, col) == 'X') {
				num = num - 1;
			}
			if(grid.get(0, col-1) == 'X') {
				num++;
			}
			if(grid.get(0, col) == 'X') {
				num++;
			}
			if(grid.get(0, col+1) == 'X') {
				num++;
			}
		}
		// if the location is at the left edge but not at corner
		else if (col == 0 && row != 0 && row != grid.height() - 1) {
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = 0; j <= 1; j++) {
					if (grid.get(i, j) == 'X') {
						num++;
					}
				}
			}
			if (grid.get(row, col) == 'X') {
				num = num - 1;
			}
			if(grid.get(row, grid.width() - 1) == 'X') {
				num++;
			}
			if(grid.get(row-1, grid.width() - 1) == 'X') {
				num++;
			}
			if(grid.get(row+1, grid.width() - 1) == 'X') {
				num++;
			}
		}
		// if the location is at the right edge but not at corner
		else if (col == grid.width() - 1 && row != 0 && row != grid.height() - 1) {
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = col - 1; j <= col; j++) {
					if (grid.get(i, j) == 'X') {
						num++;
					}
				}
			}
			if (grid.get(row, col) == 'X') {
				num = num - 1;
			}
			if(grid.get(row, 0) == 'X') {
				num++;
			}
			if(grid.get(row-1, 0) == 'X') {
				num++;
			}
			if(grid.get(row+1, 0) == 'X') {
				num++;
			}
		}
		// if the location is not at edge or corner
		else if (row > 0 && row < grid.height() - 1 && col > 0 && col < grid.width() - 1) {
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = col - 1; j <= col + 1; j++) {
					if (grid.get(i, j) == 'X') {
						num++;
					}
				}
			}
			if (grid.get(row, col) == 'X') {
				num = num - 1;
			}

		}
		return num;
	}

	/**
	 * Counts the number of neighbors of a cell in a non-wrapping mode
	 * @param row
	 * @param col
	 * @param grid
	 * @return
	 */
	public static int numOfNonWrapping(int row, int col, Grid<Character> grid) {
		int num = 0;
		// if the location is at the up-left corner
		if (row == 0 && col == 0) {
			for (int i = 0; i <= 1; i++) {
				for (int j = 0; j <= 1; j++) {
					if (grid.get(i, j) == 'X') {
						num++;
					}
				}
			}
			if (grid.get(row, col) == 'X') {
				num = num - 1;
			}
		}
		// if the location is at the bottom-right corner
		if (row == grid.height() - 1 && col == grid.width() - 1) {
			for (int i = row - 1; i <= row; i++) {
				for (int j = col - 1; j <= col; j++) {
					if (grid.get(i, j) == 'X') {
						num++;
					}
				}
			}
			if (grid.get(row, col) == 'X') {
				num = num - 1;
			}
		}
		// if the location is at the up-right corner
		if (row == 0 && col == grid.width()- 1) {
			for (int i = 0; i <= 1; i++) {
				for (int j = col - 1; j <= col; j++) {
					if (grid.get(i, j) == 'X') {
						num++;
					}
				}
			}
			if (grid.get(row, col) == 'X') {
				num = num - 1;
			}
		}
		// if the location is at the bottom-left corner
		if (row == grid.height() - 1 && col == 0) {
			for (int i = row - 1; i <= row; i++) {
				for (int j = 0; j <= 1; j++) {
					if (grid.get(i, j) == 'X') {
						num++;
					}
				}
			}
			if (grid.get(row, col) == 'X') {
				num = num - 1;
			}
		}
		// if the location is at the up edge but not at corner
		if (row == 0 && col != 0 && col != grid.width() - 1) {
			for (int i = 0; i <= 1; i++) {
				for (int j = col - 1; j <= col + 1; j++) {
					if (grid.get(i, j) == 'X') {
						num++;
					}
				}
			}
			if (grid.get(row, col) == 'X') {
				num = num - 1;
			}
		}
		// if the location is at the bottom edge but not at corner
		if (row == grid.height() - 1 && col != 0 && col != grid.width() - 1) {
			for (int i = row - 1; i <= row; i++) {
				for (int j = col - 1; j <= col + 1; j++) {
					if (grid.get(i, j) == 'X') {
						num++;
					}
				}
			}
			if (grid.get(row, col) == 'X') {
				num = num - 1;
			}
		}
		// if the location is at the left edge but not at corner
		if (col == 0 && row != 0 && row != grid.height() - 1) {
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = 0; j <= 1; j++) {
					if (grid.get(i, j) == 'X') {
						num++;
					}
				}
			}
			if (grid.get(row, col) == 'X') {
				num = num - 1;
			}
		}
		// if the location is at the right edge but not at corner
		if (col == grid.width() - 1 && row != 0 && row != grid.height() - 1) {
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = col - 1; j <= col; j++) {
					if (grid.get(i, j) == 'X') {
						num++;
					}
				}
			}
			if (grid.get(row, col) == 'X') {
				num = num - 1;
			}
		}
		// if the location is not at edge or corner
		if (row > 0 && row < grid.height() - 1 && col > 0 && col < grid.width() - 1) {
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = col - 1; j <= col + 1; j++) {
					if (grid.get(i, j) == 'X') {
						num++;
					}
				}
			}
			if (grid.get(row, col) == 'X') {
				num = num - 1;
			}

		}
		return num;
	}

	/**
	 * Prints out the grid line by line
	 * @param grid
	 */
	public static void printGrid(Grid<Character> grid) {
		for(int i = 0; i < grid.height(); i++) {
			for(int j = 0; j < grid.width(); j++) {
				System.out.print(grid.get(i, j));
			}
			System.out.println();
		}
	}
}
