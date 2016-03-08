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

	// Map that holds the possible values of the variables
	//	map<string,ArrayList>
	//private static Map<String, ArrayList<Integer>> valLists = new HashMap<String, ArrayList<Integer>>();
	
	public static void main (String args[]) throws IOException
	{
		commandLineCheck(args);									//format checking 
		// Variable Declaration
		String cmdLine;
		Scanner input = new Scanner(System.in);
		String variablesFile = "ex1.var1";
		String constraintsFile = "ex1.con";
		ArrayList<String> constraints = new ArrayList<String>();
		Map<String, ArrayList<Integer>> variables = new HashMap<String, ArrayList<Integer>>();

		
		variables = getVariables(variablesFile);
		
		int temp2 = variables.size();
		System.out.println(variables.get("E").toString());		// Test to print the size of the map
		
		constraints = getConstraints(constraintsFile);
	
		String varArray[] = {"A", "B", "C", "D", "E"};	// Test array for creating the node
		Node testNode = new Node(new ArrayList<String>(Arrays.asList(varArray)));				// Creating a test node
		testNode.printNode();							// Printing the test node
		
	}
	
	public static Map<String, ArrayList<Integer>> getVariables(String fileName) throws IOException
	{
		Map<String, ArrayList<Integer>> v = new HashMap<String, ArrayList<Integer>>();
		Scanner varInput = new Scanner(new File(fileName));
		
		while(varInput.hasNextLine())
		{
			String temp = varInput.nextLine();					// Grab the line from the .var1 file
			ArrayList<Integer> tempArray = new ArrayList<>();	// ArrayList of that will be placed in the Map
			
			temp = temp.replaceAll(":", "");
			
			Scanner tokenizer = new Scanner(temp);				// Instantiate a new scanner that will parse through the given string
			tokenizer.useDelimiter(" ");						// Tokenize the input by using spaces as a the delimiter
			
			String var = tokenizer.next();
			while(tokenizer.hasNext())
			{
				tempArray.add(Integer.parseInt(tokenizer.next()));
			}
			
			v.put(var, tempArray);								// Add the arraylist to the map
			tokenizer.close();
		}
		varInput.close();
		
		return v;
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

	public static void commandLineCheck(String args[])	//Check to see if the correct amount of command line arguments were entered
	{
		if(args.length != 3)
		{
			System.err.println("ERROR: Unexpected number of Command Line arguments. Program will now exit with an error.");
			System.exit(-1);
		}
		
	}
	
	public static boolean constraintChecking(Node assignment, ArrayList<String> constraints)
	//public static boolean constraintChecking(ArrayList<String> vars, int[] vals, ArrayList<String> constraints)
	{
		ArrayList<String> vars = assignment.getVariables();
		int[] vals = assignment.getValues();
		
		
		for (int i = 0; i < constraints.size(); i++)		
		{
			String constraint = constraints.get(i);
			int opCode = getOpcode(constraint.charAt(1));
			int operand1 = vals[vars.indexOf(constraint.charAt(0))];
			int operand2 = vals[vars.indexOf(constraint.charAt(2))];
			
			if(operand1 != -999 && operand2 != -999)
			{
				switch(opCode)
				{
				case EQUALITY: if(!(operand1 == operand2))
									return false;
								break;
				case INEQUALITY: if(!(operand1 != operand2))
									return false;
								break;
				case LESS_THAN: if(!(operand1 < operand2))
									return false;
								break;
				case GREATER_THAN: if(!(operand1 > operand2))
									return false;
								break;
				default:		System.err.println("ERROR: Unrecognized opCode in Constraint Checking: " + opCode + ". Program will now exit.");
								System.exit(-1);
								break;
				}
			}
			
							
		}
		
		
		return true;															//return true if all the constraints are true.	
	}
	
	public static Node backtrackingSearch(ArrayList<String> csp)				//First function call for the backtracking algo
	{
		Node solution = new Node(csp);
		
		return recursiveBacktracking(solution,csp);
	}
	
	public static Node recursiveBacktracking(Node assignment, ArrayList<String> csp)
	{
		if(baseCaseCheck(assignment, csp))										//base case. If all variables have a value and are consistant, return the assignment.
			return assignment;
		
		
		
		return assignment;
	}
	
	public static boolean baseCaseCheck(Node assignment, ArrayList<String> csp)	//Check the node to see if it is consistent and all the variables are instantiated
	{
		if(!constraintChecking(assignment,csp) || !checkForNull(assignment))	//if there are conflicts, return false
			return false;
		
		return true;		//return true if all the values and variables have been instantiated and the assignment is consistent
	}
	
	private static boolean checkForNull(Node assignment)			//checks to see if all the values have been instantiated(not null)
	{
		int[] vals = assignment.getValues();						//place values into a temp int[] array
		
		for (int i = 0; i < vals.length; i++)						//loop through all the values
		{
			if (vals[i] == -999)
				return false;										//if the value is null(-999), return false
		}
		
		return true;												//All the values are not null, return true
	}
	
	private static int getOpcode(char operator)						//gets the opcode of the character passed to it (=,!,<,>)
	{
		if(operator == '=')
			return EQUALITY;
		else if(operator == '!')
			return INEQUALITY;
		else if(operator == '<')
			return LESS_THAN;
		else if(operator == '>')
			return GREATER_THAN;
		
		System.err.println("ERROR: Unexpected operator in constraints: " + operator + ". Program will now exit.");
		System.exit(-1);
		return 0;
	}
	
	
}