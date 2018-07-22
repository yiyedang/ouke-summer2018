
/** Type: HuffmanNode
 * 	A node inside a Huffman encoding tree.	 Each node stores four
 * 	values - the character stored here (or NOT_A_CHAR if the value
 * 	is not a character), references to the 0 and 1 subtrees, and the
 * 	character count (weight) of the tree.
 */
public class HuffmanNode implements Comparable<HuffmanNode>{

	/**
	 * Construct a HuffmanNode with a char and its count
	 */
	public HuffmanNode(char character, int count) {
		this.setCharacter(character);
		this.setCount(count);
		left = null;
		right = null;
	}
	
	/**
	 * Return the character of this HuffmanNode
	 */
	public char getCharacter() {
		return character;
	}
	
	/**
	 * Set the character of this HuffmanNode
	 */
	public void setCharacter(char character) {
		this.character = character;
	}

	/**
	 * Return the number of appearances of the character in the HuffmanNode
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Set the number of appearances of the character in the HuffmanNode
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * Get the left or zero subtree of this HuffmanNode
	 */
	public HuffmanNode getLeft() {
		return left;
	}

	/**
	 * Set the left or zero subtree of this HuffmanNode
	 */
	public void setLeft(HuffmanNode left) {
		this.left = left;
	}

	/**
	 * Get the right or one subtree of this HuffmanNode
	 */
	public HuffmanNode getRight() {
		return right;
	}

	/**
	 * Set the right or one subtree of this HuffmanNode
	 */
	public void setRight(HuffmanNode right) {
		this.right = right;
	}
	
	/**
	 * See if this HuffmanNode is a leaf, meaning there is no left or right subtree.
	 */
	public boolean isLeaf() {
		return left == null && right == null;
	}
	
	/**
	 * See if this HuffmanNode has a left subtree.
	 */
	public boolean hasLeft() {
		return left != null;
	}
	
	/**
	 * See if this HuffmanNode has a right subtree.
	 */
	public boolean hasRight() {
		return right != null;
	}
	
	/**
	 * Compare HuffmanNode by their count
	 */
	public int compareTo(HuffmanNode other) {
		return this.count - other.count;
	}
	
	/**
	 * A string representation of a Huffman Node
	 */
	public String toString() {
		return "[" + toPrintableChar(character) + ": count(" + count + ") ]";
	}

	/*
	 * Returns a printable string for the given character.
	 */
	public static String toPrintableChar(int ch) {
	    if (ch == '\n') {
	        return "'\\n'";
	    } else if (ch == '\t') {
	        return "'\\t'";
	    } else if (ch == '\r') {
	        return "'\\r'";
	    } else if (ch == '\f') {
	        return "'\\f'";
	    } else if (ch == '\b') {
	        return "'\\b'";
	    } else if (ch == '\0') {
	        return "'\\0'";
	    } else if (ch == ' ') {
	        return "' '";
	    } else if (ch == (char)256) {
	    		return "EOF";
	    } else if (ch == (char)257) {
	    		return "NOT";
	    } else {
	        return "'" + (char) ch + "'";
	    }
	}
	
	/* instance variables */
	private char character;
	private int count;
	private HuffmanNode left;
	private HuffmanNode right;
}

