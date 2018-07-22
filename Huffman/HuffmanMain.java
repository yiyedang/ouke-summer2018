import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import edu.princeton.cs.algs4.BinaryIn;
import edu.princeton.cs.algs4.BinaryOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class HuffmanMain {

	private static Scanner stdIn = new Scanner(System.in);
	private static final String DEFAULT_COMPRESSED_FILE_EXTENSION = ".huf";
	private static final String DEFAULT_DECOMPRESSED_FILE_EXTENSION = ".txt";
	
	 // these variables maintain state between steps 1-4
    private static String filename = "";
    private static HuffmanNode encodingTree = null;
    private static HashMap<Character, Integer> freqTable = null;
    private static HashMap<Character, String> encodingMap = null;
	
	public static void main(String[] args) throws IOException {
	    intro();

	    // prompt user for options repeatedly
	    while (true) {
	        String choice = menu();
	        if (choice.equals("Q")) {
	            break;
	        } else if (choice.equals("1")) {
	            test_buildFrequencyTable();
	        } else if (choice.equals("2")) {
	            test_buildEncodingTree();
	        } else if (choice.equals("3")) {
	        		test_buildEncodingMap();
	        } else if (choice.equals("4")) {
	            test_encodeData();
	        } else if (choice.equals("5")) {
	            test_decodeData();
	        } else if (choice.equals("C")) {
	            test_compress();
	        } else if (choice.equals("D")) {
	            test_decompress();
	        } else if (choice.equals("B")) {
	            test_binaryFileViewer();
	        } else if (choice.equals("T")) {
	            test_textFileViewer();
	        }
	    }

	    System.out.println("Exiting.");
	}
	
	/**
	 * Sets up the output console and explains the program to the user.
	 */
	private static void intro() {
	    System.out.println("Welcome to CS 106B Shrink-It!");
	    System.out.println("This program uses the Huffman coding algorithm for compression.");
	    System.out.println("Any file can be compressed by this method, often with substantial");
	    System.out.println("savings. Decompression will faithfully reproduce the original.");
	}
	
	/**
	 * Prints a menu of choices for the user and reads/returns the user's response.
	 */
	private static String menu() {
	    System.out.println();
	    System.out.println("1) build character frequency table");
	    System.out.println("2) build encoding tree");
	    System.out.println("3) build encoding map");
	    System.out.println("4) encode data");
	    System.out.println("5) decode data");
	    System.out.println();
	    System.out.println("C) compress file");
	    System.out.println("D) decompress file");
	    System.out.println();
	    System.out.println("B) binary file viewer");
	    System.out.println("T) text file viewer");
	    System.out.println("Q) quit");

	    System.out.print("Your choice? ");
	    String choice = stdIn.nextLine().toUpperCase().trim();
	    return choice;
	}

	/**
	 * Tests the buildFrequencyTable function.
	 * Prompts the user for a string of data or input file to read,
	 * then builds a frequency map of its characters and prints that map's contents.
	 * Stores the built map in the given output parameter freqTable.
	 * Also stores output parameters for the text input read, and whether the input
	 * came from a string of text or a file.  This is reused later by main.
	 * @throws IOException 
	 *
	 */
	private static void test_buildFrequencyTable() throws IOException {
		filename = readFileNameFromConsole("File name to process: ");
		RandomAccessFile input = new RandomAccessFile(filename, "r");
		
	    System.out.println("Building frequency table ...");
	    freqTable = Huffman.buildFrequencyTable(input);
	    Set<Character> keys = freqTable.keySet();
	    for (char ch : keys) {
	        System.out.print("    " + (int)ch);
	        System.out.print(": " + HuffmanNode.toPrintableChar(ch) + "  => ");
	        System.out.println(freqTable.get(ch));
	    }
	    System.out.println(freqTable.size() + " character frequencies found.");
	}
	
	/**
	 * Tests the buildEncodingTree function.
	 * Accepts a frequency table map as a parameter, presumably the one generated
	 * previously in step 1 by buildFrequencyTable.
	 * Then prints the encoding tree in an indented sideways format.
	 * Stores the built tree in the given output parameter encodingTree.
	 */
	private static void test_buildEncodingTree() {
	    if (freqTable == null) {
	        System.out.println("Can't build tree; character frequency table is empty or uninitialized.");
	    } else {
	        System.out.println("Building encoding tree ...");
	        encodingTree = Huffman.buildEncodingTree(freqTable);
	        Huffman.printSideways(encodingTree);
	    }
	}
	
	/**
	 * Tests the buildEncodingMap function.
	 * Accepts an encoding tree as a parameter, presumably the one generated
	 * previously in step 2 by buildEncodingTree.
	 * Then prints the encoding map of characters to binary encodings.
	 * Stores the built map in the given output parameter encodingMap.
	 */
	private static void test_buildEncodingMap() {
	    if (encodingTree == null) {
	        System.out.println("Can't build map; encoding tree is NULL.");
	    } else {
	        System.out.println("Building encoding map ...");
	        encodingMap = Huffman.buildEncodingMap(encodingTree);
	        for (char ch : encodingMap.keySet()) {
	            System.out.print("    " +  (int)ch);
	            System.out.print(": " + HuffmanNode.toPrintableChar(ch) + "  => ");
	            System.out.println(encodingMap.get(ch));
	        }
	        System.out.println(encodingMap.size() + " character encodings found.");
	    }
	}
	
	/**
	 * Tests the encodeData function.
	 * Accepts as a parameter a map of encodings, presumably the one generated
	 * previously in step 3 by buildEncodingMap.
	 * Allows the user to encode the same data from the original file/string,
	 * or new data that is typed / data from a file.
	 * Once encoding is done, prints the bits of the encoded data.
	 * @throws IOException 
	 */
	private static void test_encodeData() throws IOException {
	    if (encodingMap == null) {
	        System.out.println("Can't encode data; encoding map is empty or uninitialized.");
	    } else {
	        System.out.println("Encoding data ...");
	        String compressedFileName = filename.substring(0, filename.indexOf("."))  + DEFAULT_COMPRESSED_FILE_EXTENSION;
			RandomAccessFile input = new RandomAccessFile(filename, "r");
	        BinaryOut output = new BinaryOut(compressedFileName);
	        
			Huffman.encodeData(encodingMap, input, output);
	        output.flush();   // remember to flush
	        output.close();   // remember to close
	        
	        File file = new File(compressedFileName);
	        if(file.exists()) {
		        System.out.println("Here is the binary encoded data (" + file.length() + " bytes):");
		        printBitsToConsole(compressedFileName);
		        System.out.println();
	        }else {
	        		System.out.println("Compressed file not exists.");
	        }
	    }
	}
	
	/**
	 * Print the bits of the content of the file to console
	 */
	private static void printBitsToConsole(String filename) {
		BinaryIn in = new BinaryIn(filename);
		int i = 0;
		while(!in.isEmpty()) {
			i++;
			if(in.readBoolean()) {
				System.out.print(1);
			}else {
				System.out.print(0);
			}
			
			if (i > 0 && i % 8 == 0) {
	            System.out.print(" ");
	        }
			
	        if (i > 0 && i % 64 == 0) {
	            System.out.println();
	        }
		}
		System.out.println();
	}
	
	/**
	 * Tests the decodeData function.
	 * Uses the given encoding tree, presumably one encoded previously in step 2
	 * by buildEncodingTree.
	 * Prompts for a file and decodes it into a
	 * string output stream, then prints the text on the console.
	 * @throws IOException 
	 */
	private static void test_decodeData() throws IOException {
	    if (encodingTree == null) {
	        System.out.println("Can't decode; encoding tree is NULL.");
	    } else {
    			String compressedFileName = readFileNameFromConsole("File name to process (originalFileName.huf): ");
    			String decompressedFileName = compressedFileName.substring(0, compressedFileName.indexOf(".huf")) 
    					+ DEFAULT_DECOMPRESSED_FILE_EXTENSION;

    			System.out.println("Decoding data ...");
    			BinaryIn input = new BinaryIn(compressedFileName);
    			RandomAccessFile output = new RandomAccessFile(decompressedFileName, "rw");
    			
	        Huffman.decodeData(encodingTree, input, output);

	        File decoded = new File(decompressedFileName);
	        System.out.println("Here is the decoded data ("
	             + decoded.length() + " bytes):");
	        
	        input = new BinaryIn(decompressedFileName);
	        while(!input.isEmpty()) {
	        		System.out.print(input.readChar());
	        }
	        System.out.println();
	    }
	}
	
	/**
	 * Tests the compress function.
	 * Prompts for input/output file names and opens streams on those files.
	 * Then calls your compress function and displays information about how many
	 * bytes were written, if any.
	 * @throws IOException 
	 */
	private static void test_compress() throws IOException {
	    String inputFileName = readFileNameFromConsole("Input file name: ");
	    String defaultOutputFileName = inputFileName.substring(inputFileName.indexOf("/")+1, inputFileName.indexOf(".")) + DEFAULT_COMPRESSED_FILE_EXTENSION;
	    System.out.print("Output file name (Enter for " + defaultOutputFileName + "): ");
	    String outputFileName = "res/" + StdIn.readLine();
	    
	    if(outputFileName.equals("res/")) {
	    		outputFileName += defaultOutputFileName;
	    }
	    
	    if (inputFileName.equals(outputFileName)) {
	        System.out.println("You cannot specify the same file as both the input file");
	        System.out.println("and the output file.  Aborting.");
	        return;
	    }
	    
	    if (!confirmOverwrite(outputFileName)) {
	        return;
	    }
	    
	    RandomAccessFile input = new RandomAccessFile(inputFileName, "r");
	    BinaryOut output = new BinaryOut(outputFileName);

	    int inputFileSize = fileSize(inputFileName);
	    System.out.println("Reading " + inputFileSize + " uncompressed bytes.");
	    System.out.println("Compressing ...");
	    Huffman.compress(input, output);
	    output.flush();
	    output.close();

	    if (fileExists(outputFileName)) {
	        System.out.println("Wrote " + fileSize(outputFileName) + " compressed bytes.");
	    } else {
	    		System.out.println("Compressed output file was not found; perhaps there was an error.");
	    }
	}
	
	
	/*
	 * Tests the decompress function.
	 * Prompts for input/output file names and opens streams on those files.
	 * Then calls your decompress function and displays information about how many
	 * bytes were written, if any.
	 */
	private static void test_decompress() throws IOException {
	    String inputFileName = readFileNameFromConsole("Input file name: ");
	    String defaultOutputFileName = inputFileName.substring(inputFileName.indexOf("/")+1, inputFileName.indexOf(".")) + "-out" + DEFAULT_DECOMPRESSED_FILE_EXTENSION;
	    System.out.print("Output file name (Enter for " + defaultOutputFileName + "): ");
	    String outputFileName = "res/" + StdIn.readLine();
	    
	    if (outputFileName.equals("res/")) {
	        outputFileName += defaultOutputFileName;
	    }
	    
	    if (inputFileName.equals(outputFileName)) {
	        System.out.println("You cannot specify the same file as both the input file");
	        System.out.println("and the output file.  Aborting.");
	        return;
	    }
	    
	    if (!confirmOverwrite(outputFileName)) {
	        return;
	    }

	    int inputFileSize = fileSize(inputFileName);
	    System.out.println("Reading " + inputFileSize + " compressed bytes.");
	    BinaryIn input = new BinaryIn(inputFileName);
	    RandomAccessFile output = new RandomAccessFile(outputFileName, "rw");
	    System.out.println("Decompressing ...");
	    Huffman.decompress(input, output);

	    if (fileExists(outputFileName)) {
	        System.out.println("Wrote " + fileSize(outputFileName) + " decompressed bytes.");
	    } else {
	        System.out.println("Decompressed output file was not found; perhaps there was an error.");
	    }
	}
	
	
	/**
	 * Binary file viewer function.
	 * Prompts the user for a file name and then prints all bits/bytes of that file.
	 */
	private static void test_binaryFileViewer() {
	    String filename = readFileNameFromConsole("File name to display: ");
	    File file = new File(filename);
	    System.out.println("Here is the binary encoded data (" + file.length() + " bytes):");
	    printBitsToConsole(filename);
	}
	
	/**
	 * Text file viewer function.
	 * Prompts the user for a file name and prints all text in that file.
	 */
	private static void test_textFileViewer() {
	    String filename = readFileNameFromConsole("File name to display: ");
	    String data = readDataFromFile(filename);
	    File file = new File(filename);
	    System.out.println("Here is the text data (" + file.length() + " bytes):");
	    System.out.println(data);
	}
	
	/**
	 * Confirm to overwrite an existing file.
	 */
	private static boolean confirmOverwrite(String filename) {
		File file = new File(filename);
		if(!file.exists()) {
			return true;
		}else {
			return getYesOrNo(filename + " already exists.  Overwrite? (y/n) ");
		}
	}
	
	/**
	 * Get Yes or No from User; Yes returns true; No returns false;
	 * User can input "y" or "Y" to indicate yes, "N" or "n" to indicate no.
	 */
	private static boolean getYesOrNo(String prompt) {
		while(true) {
			StdOut.print(prompt);
			String answer = StdIn.readLine().toLowerCase().trim();
			if(answer.equals("y")) {
				return true;
			}else if(answer.equals("n")) {
				return false;
			}
		}
	}
	
	/**
	 * Get the size of a file. If file does not exist, return -1.
	 */
	private static int fileSize(String filename) {
		File file = new File(filename);
		if(file.exists()) {
			return (int) file.length();
		}else {
			System.err.println(filename + " does not exist.");
			return -1;
		}
	}
	
	/**
	 * See if a file exists.
	 */
	private static boolean fileExists(String filename) {
		File file = new File(filename);
		if(file.exists()) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Read data from a file indicated by filename.
	 */
	public static String readDataFromFile(String filename) {
		BinaryIn in = new BinaryIn(filename);
		StringBuilder data = new StringBuilder();
		while(!in.isEmpty()) {
			char ch = in.readChar();
			data.append(ch);
		}
		return data.toString();
	}
	
	/**
	 * Read a valid file name from console.
	 */
	public static String readFileNameFromConsole(String prompt) {
		String filename = "res/";
		while(true) {
			System.out.print(prompt);
			filename += StdIn.readLine();
			File file = new File(filename);
			if(file.exists()) break;
			System.out.println("File not exists. Please try again.");
		}
		return filename;
	}
}

