public class Grid<T> {
	
	/**
	 * This is a default constructor to set the elements as null pointer
	 * and to set the number of rows and columns to be zero.
	 */
	public Grid() {
		elements = null;
		nRows = 0;
		nCols = 0;
	}
	
	/** Initializes a new grid of the given size. */
	@SuppressWarnings("unchecked")
	public Grid(int row, int col) {
		elements = (T[][])new Object[row][col];
		nRows = row;
		nCols = col;
	}
	
	/** Initializes a new grid of the given size, with every cell set to the given value. */
	@SuppressWarnings("unchecked")
	public Grid(int row, int col, T value) {
		elements = (T[][])new Object[row][col];
		nRows = row;
		nCols = col;
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {
				elements[i][j] = value;
			}
		}
	}
	
	/** Returns the element at the specified row/col position in this grid. */
	public T get(int row, int col) {
		checkIndexes(row, col, nRows-1, nCols-1, "get");
		return elements[row][col];
	}
	
	/** Sets every grid element to the given value. */
	public void fill(T value) {
		for(int i = 0; i < nRows; i++) {
			for(int j = 0; j < nCols; j++) {
				elements[i][j] = value;
			}
		}
	}
	
	/** Reinitializes the grid to have the specified number of rows and columns. */
	@SuppressWarnings("unchecked")
	public void resize(int numRows, int numCols, boolean retain) {
		
		// invalid parameters
		if(nRows < 0 || nCols < 0) {
			String output = "";
			output += "Grid::resize: Attempt to resize grid to invalid size ("+ numRows + ", " + numCols + ")";
			System.err.print(output);
		}
		
		// optimization: don't do the resize if we are already that size
		if(nRows == numRows && nCols == numCols) {
			return;
		}
		
		// save backup of old array/size
		T[][] oldElements = elements;
		int oldnRows = nRows;
		int oldnCols = nCols;
		
		// create new empty array and set new size
		nRows = numRows;
		nCols = numCols;
		elements = (T[][])(new Object[nRows][nCols]);
		
		// possibly retain old contents
	    if (retain) {
	        int minRows = oldnRows < numRows ? oldnRows : numRows;
	        int minCols = oldnCols < numCols ? oldnCols : numCols;
	        for (int row = 0; row < minRows; row++) {
	            for (int col = 0; col < minCols; col++) {
	                elements[row][col] = oldElements[row][col];
	            }
	        }
	    }
	}
	
	/** Replaces the element at the specified row/col location in this grid with a new value. */
	public void set(int row, int col, T value) {
		checkIndexes(row, col, nRows - 1, nCols - 1, "set");
		elements[row][col] = value;
	}
	
	/** Returns the total number of elements in the grid. */
	public int size() {
		return nRows * nCols;
	}
	
	/** Returns the grid's height, that is, the number of rows in the grid. */
	public int height() {
		return nRows;
	}
	
	/** Returns the grid's width, that is, the number of columns in the grid. */
	public int width() {
		return nCols;
	}
	
	/** Returns true if the specified row and column position is inside the bounds of the grid. */
	public boolean inbounds(int row, int col) {
		return row >= 0 && col >= 0 && row < nRows && col < nCols;
	}
	
	/** Returns the number of columns in the grid. */
	public int numCols() {
		return nCols;
	}
	
	/** Returns the number of rows in the grid. */
	public int numRows() {
		return nRows;
	}

	/** Returns true if the grid has 0 rows and/or 0 columns. */
	public boolean isEmpty() {
		return nRows == 0 || nCols == 0;
	}
	
	
	/**
    * Throws an ErrorException if the given row/col are not within the range of
    * (0,0) through (rowMax-1,colMax-1) inclusive.
    * This is a consolidated error handler for all various Grid members that
    * accept index parameters.
    * The prefix parameter represents a text string to place at the start of
    * the error message, generally to help indicate which member threw the error.
    */
	private void checkIndexes(int row, int col, int rowMax, int colMax, String prefix) {
		int rowMin = 0;
		int colMin = 0;
		
		if (row < rowMin || row > rowMax || col < colMin || col > colMax) {
			String output = "";
			output += "Grid::" + prefix + ": (" + row + ", " + col + ")" + " is outside of valid range [";
			if (rowMin < rowMax && colMin < colMax) {
				output += "(" + rowMin + ", " + colMin + ")..(" + rowMax + ", " + colMax + ")";
			} else if (rowMin == rowMax && colMin == colMax) {
				output += "(" + rowMin + ", " + colMin + ")";
			} // else min > max, no range, empty grid
			output += "]\n";
			System.err.print(output);
		}
	}
	
	 /**
     * Method: toString
     * Usage: String str = grid.toString();
     * --------------------------------------
     * Converts the grid to a printable string representation.
     * The string returned is a 2-dimensional representation such as:
     * "{[1, 2, 3],\n
     *   [4, 5, 6],\n
     *   [7, 8, 9]}"
     */
	public String toString() {
		String result = "{";
		for(int i = 0; i < nRows; i++) {
			for(int j = 0; j < nCols; j++) {
				if(j == 0) {
					result += "[" + elements[i][j] + ", ";
				}
				else if(j == nCols - 1) {
					if(i != nRows - 1)
						result += elements[i][j] + "],";
					else
						result += elements[i][j] + "]";
				}
				else {
					result += elements[i][j] + ", ";
				}
			}
			
			if(i != nRows - 1)
				result += "\n";
			else
				result += "}";
		}
		return result;
	}
	
	/** Returns true if the two grids contain the same elements. */
	public boolean equals(Grid<T> other) {
		if(this == other) {
			return true;
		}
	
		if(this.nRows == other.nRows && this.nCols == other.nCols) {
			for(int i = 0; i < nRows; i++) {
				for(int j = 0; j < nCols; j++) {
					if(!this.elements[i][j].equals(other.elements[i][j])) {
						return false;
					}
				}
			}
		}else {
			return false;
		}
		
		return true;
	}
	
	/* instance variables */
	private T[][] elements = null;
	private int nRows = 0;
	private int nCols = 0;

}
