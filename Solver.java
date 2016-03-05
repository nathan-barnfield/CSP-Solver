/*
 * Name:		Daniel Pacheco & Nathan Barnfield
 * Course:		CS 4365.001
 * Homework:	#2
 * Due Date:	March 10th, 2016
 * Class:		Solver.java
 * Purpose:		[Insert Here]
*/

import java.util.*;
import java.io.*;

public class Solver
{
	
	// Operation Codes
	public static final int EQUALITY     = 0;
	public static final int INEQUALITY	 = 1;
	public static final int LESS_THAN    = 2;
	public static final int GREATER_THAN = 3;

	// Map that holds the possible values of the varibales
	//	map<string,ArrayList>
	private static Map<String, ArrayList<Integer>> valLists = new HashMap<String, ArrayList<Integer>>();
	
	public static void main (String args[]) throws IOException
	{
		//commandLineCheck(args);
		// Variable Declaration
		//
		// File input of the .var file
		//
		Scanner varInput = new Scanner(/*new File(args[0])*/new File("ex1.var1"));
		
		while(varInput.hasNextLine())
		{
			String temp = varInput.nextLine();							//Grab the line from the .var file
			ArrayList<Integer> tempArray = new ArrayList<>();			//ArrayList of that will be placed in the Map
			
			temp = temp.replaceAll(":", "");
			
			Scanner tokenizer = new Scanner(temp);						//Instantitate a new scanner that will parse through the given string
			tokenizer.useDelimiter(" ");								//tokenize the input by using spaces as a the delimiter
			
			String var = tokenizer.next();
			while(tokenizer.hasNext())
			{
				tempArray.add(Integer.parseInt(tokenizer.next()));
			}
			
			valLists.put(var, tempArray);
		}
		varInput.close();												//close the input for the .var file
		
		int temp1 = valLists.size();
		
		System.out.println(temp1);
		
		String cmdLine;
		Scanner input = new Scanner(System.in);
		String fileVariables = "ex1.var";
		String fileConstrants = "ex1.con";
		ArrayList<String> constraints = new ArrayList<String>();

/*		
		// Prompt the user for the required file names
		System.out.println(">> ENTER COMMAND: <variables.var> <constraints.con> <none|fc>");
		System.out.println(">> NOTE: cAsE sEnSiTiVe; Don't include brackets; Files requires file type");
		cmdLine = input.nextLine();
		String[] words = cmdLine.split(" "); // Used to read the input
*/			
		
		constraints = getConstraints(fileConstrants);
		
		String varArray[] = {"A", "B", "C", "D", "E"};	// Test array for creating the node
		Node testNode = new Node(varArray);				// Creating a test node
		testNode.printNode();							// Printing the test node
		
	}
	
	public static ArrayList<String> getConstraints(String fileName) throws IOException
	{
		
		ArrayList<String> c = new ArrayList<>();		// Array list of constraints
		BufferedReader br	= new BufferedReader(new FileReader(fileName));
		String line			= null;

		while((line = br.readLine()) != null)
		{
			String temp = line.replaceAll("\\s","");	// Removes blank spaces from current line
			c.add(temp);								// Add the constraint to an array list
		}
		
		br.close();
		
		return c;
		
	}

}


//testing 
//commit