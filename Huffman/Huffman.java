import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Set;

import edu.princeton.cs.algs4.BinaryIn;
import edu.princeton.cs.algs4.BinaryOut;

public class Huffman {
	private static final char PSEUDO_EOF = (char)256;
	private static final char NOT_A_CHAR = (char)257;
	
	/**
	 * Read input from a given input stream (which could be a file on disk, a string buffer, etc.). 
	 * Count and return a mapping from each character (represented as int here) to the number of
	 * times that character appears in the file. 
	 * @param input
	 * @return
	 * @throws IOException 
	 */
	public static HashMap<Character, Integer> buildFrequencyTable(RandomAccessFile input) throws IOException {
		HashMap<Character, Integer> table = new HashMap<Character, Integer>();
		//read the input by byte and put each character on the table with its frequency
		for(int i = 0; i < input.length(); i++) {
			char key = (char)input.readByte();
			//if the table already contains the char value
			if(table.containsKey(key)) {
				//increase its frequency by 1
				table.put(key, table.get(key) + 1);
			} else {
				//assign its frequency as 1
				table.put(key, 1);
			}
		}
		table.put(PSEUDO_EOF, 1);
		return table;
	}
	
	/**
	 * Use the frequency table to create a Huffman encoding tree based on those frequencies.
	 * @param freqTable
	 * @return a pointer to the root of the tree.
	 */
	public static HuffmanNode buildEncodingTree(HashMap<Character, Integer> freqTable) {
		PriorityQueue<HuffmanNode> pqueue = new PriorityQueue<HuffmanNode>();
		Set<Character> keys = freqTable.keySet();
		//convert the characters into nodes and out them into a priority queue
		for(Character key : keys) {
			HuffmanNode node = new HuffmanNode(key, freqTable.get(key));
			pqueue.add(node);
		}
	
		//while the size of the priority queue is not 1
		while(pqueue.size() > 1) {
			//remove two nodes from the front of the queue
			HuffmanNode left = pqueue.poll();
			HuffmanNode right = pqueue.poll();
			//join them into a new node whose frequency is their sum
			HuffmanNode newNode = new HuffmanNode(NOT_A_CHAR, left.getCount() + right.getCount());
			newNode.setLeft(left);
			newNode.setRight(right);
			pqueue.add(newNode);
		}
		//return the root (i.e. the only node left in the priority queue)
		return pqueue.poll();
		
	}
	
	/**
	 * Create and return a Huffman encoding map based on the tree's structure. 
	 * Each key in the map is a character, and each value is the binary encoding for that 
	 * character represented as a string.  
	 * @param encodingTree
	 * @return map  Huffman encoding map
	 */
	public static HashMap<Character, String> buildEncodingMap(HuffmanNode encodingTree) {
		HashMap<Character, String> map = new HashMap<Character, String>();
		buildMapHelper(map, encodingTree, "");
		return map;
	}

	/**
	 * Private helper method to recurse down branches and help build up the encoding map
	 * @param map
	 * @param node
	 * @param str
	 */
	private static void buildMapHelper(HashMap<Character, String> map, HuffmanNode node, String str) {
		//base case: if the node is a leaf
		if(node.isLeaf()) {
			//put the node on the map
			char key = node.getCharacter();
			map.put(key, str);
		} else {
			//recurse down the left branch
			if(node.hasLeft()) {
				buildMapHelper(map, node.getLeft(), str +"0");
			}
			//recurse down the right branch
			if(node.hasRight()) {
				buildMapHelper(map, node.getRight(), str +"1");
			}
		}
	}

	/**
	 * Read one character at a time from a given input file, and use the provided encoding map 
	 * to encode each character to binary, then write the character's encoded binary bits to 
	 * the given bit output bit stream.
	 * @param encodingMap
	 * @param input
	 * @param output
	 * @throws IOException 
	 */
	public static void encodeData(HashMap<Character, String> encodingMap, RandomAccessFile input, BinaryOut output) throws IOException {
		input.seek(0);
		//for every char in the input
		for(int i = 0; i < input.length(); i++) {
			char key = (char)input.readByte();
			String str = encodingMap.get(key); //get the code of the char
			//convert the code from a string to bits
			for(int j = 0; j < str.length(); j++) {
				if(str.charAt(j) == '0') {
					output.write(false);
				} else {
					output.write(true);
				}
			}
		}
		//convert the string code of EOF to bits
		String str = encodingMap.get(PSEUDO_EOF);
		for(int i = 0; i < str.length(); i++) {
			if(str.charAt(i) == '0') {
				output.write(false);
			} else {
				output.write(true);
			}
		}
		output.close();
	}

	/**
	 * Read bits from the given input file one at a time, and recursively walk through 
	 * the specified decoding tree to write the original uncompressed contents of that file 
	 * to the given output stream.
	 * @param encodingTree
	 * @param input
	 * @param output
	 * @throws IOException 
	 */
	public static void decodeData(HuffmanNode encodingTree, BinaryIn input, RandomAccessFile output) throws IOException {
		HuffmanNode root = encodingTree;
		//while the input is not empty
		while(!input.isEmpty()) {
			//get the next bit
			boolean bit = input.readBoolean();
			//if the node is a leaf
			if(encodingTree.isLeaf()) {
				//if it is an EOF, quit
				if(encodingTree.getCharacter() == PSEUDO_EOF) {
					break;
				} else {
					//else write it to output
					output.write(encodingTree.getCharacter());
				}
				encodingTree = root;
			}
			//if the bit is 1
			if(bit) {
				//recurse down the right branch
				encodingTree = encodingTree.getRight();
			} else {
				//else recurse down the left branch
				encodingTree = encodingTree.getLeft();
			}
		}
		output.close();
	}
	
	/**
	 * Compress the given input file into the given output file.
	 * @param input
	 * @param output
	 * @throws IOException 
	 */
	public static void compress(RandomAccessFile input, BinaryOut output) throws IOException {
		input.seek(0);
		//build frequency table
		HashMap<Character, Integer> table = buildFrequencyTable(input);
		Set<Character> keys = table.keySet();
		int size = table.size();
		output.write(size);
		// write the frequency table into the file as a header
		for(Character key : keys) {
			output.write((int)key); // write the key
			output.write(table.get(key));// write the frequency
		}
		//build encoding tree
		HuffmanNode encodingTree = buildEncodingTree(table);
		//build encoding map
		HashMap<Character, String> encodingMap = buildEncodingMap(encodingTree);
		//encode data
		encodeData(encodingMap, input, output);
		output.close();
	}

	/**
	 * Read the bits from the given input file one at a time, including your header packed inside 
	 * the start of the file, to write the original contents of that file to the file specified by
	 * the output parameter
	 * @param input
	 * @param output
	 * @throws IOException 
	 */
	public static void decompress(BinaryIn input, RandomAccessFile output) throws IOException {
		HashMap<Character, Integer> table = new HashMap<Character, Integer>();
		//read frequency table
		int size = input.readInt();
		for(int i = 0; i < size; i++) {
			char key = (char) input.readInt(); // read the key
			table.put(key, input.readInt()); //read the frequency
		}
		//build encoding tree
		HuffmanNode encodingTree = buildEncodingTree(table);
		//decode data
		decodeData(encodingTree, input, output);
	}

	public static void printSideways(HuffmanNode node) {
		printSidewaysHelper(node, "");
	}
	
	public static void printSidewaysHelper(HuffmanNode node, String indent) {
		if(node != null) {
			printSidewaysHelper(node.getRight(), indent + "    ");
			System.out.println(indent + node);
			printSidewaysHelper(node.getLeft(), indent + "    ");
		}
	}
}
