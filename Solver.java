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
	public static int		counter		 = 0;
	
	public static void main (String args[]) throws IOException
	{
		ArrayList<String> constraints 				= new ArrayList<String>();
		Map<String, ArrayList<Integer>> variables	= new HashMap<String, ArrayList<Integer>>();

		commandLineCheck(args);						// Format checking 
		variables		= getVariables(args);		// Gets the variables
		constraints		= getConstraints(args);		// Gets the constraints
		Node startState = new Node(variables);		// Creates the start state for the algorithm
		Node solution	= backtrackingSearch(startState, constraints);
		
		if(solution != null)						//If the search was successful
		{
			solution.printNode(counter);
				if(counter < 30)
					System.out.println(" solution");
		}
		else
		{
			System.out.println("No Valid Solution found.");
		}
	}
	
	public static void commandLineCheck(String args[])	//Check to see if the correct amount of command line arguments were entered
	{
		if(args.length != 3)
		{
			System.err.println("ERROR: Unexpected number of Command Line arguments. Program will now exit with an error.");
			System.exit(-1);
		}
	}
	
	public static Map<String, ArrayList<Integer>> getVariables(String args[]) throws IOException
	{
		String variablesFile 				= args[0];
		Map<String, ArrayList<Integer>> v	= new HashMap<String, ArrayList<Integer>>();
		Scanner varInput					= new Scanner(new File(variablesFile));
		
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
	
	public static ArrayList<String> getConstraints(String args[]) throws IOException
	{
		String constraintsFile	= args[1];
		ArrayList<String> c 	= new ArrayList<>();	// Array list of constraints
		BufferedReader br		= new BufferedReader(new FileReader(constraintsFile));
		String line				= null;

		while((line = br.readLine()) != null)
		{
			String temp = line.replaceAll("\\s","");	// Removes blank spaces from current line
			c.add(temp);								// Add the constraint to an array list
		}
		
		br.close();
		
		return c;
	}
	
	public static boolean constraintChecking(Node assignment, ArrayList<String> constraints)
	{
		ArrayList<String> vars	= assignment.getVariables();
		int[] vals				= assignment.getValues();
		
		for (int i = 0; i < constraints.size(); i++)
		{
			String constraint	= constraints.get(i);
			int opCode			= getOpcode(constraint.charAt(1));
			int operand1		= vals[vars.indexOf(String.valueOf(constraint.charAt(0)))];
			int operand2		= vals[vars.indexOf(String.valueOf(constraint.charAt(2)))];
			
			if(operand1 != -999 && operand2 != -999)
			{
				switch(opCode)
				{
				case EQUALITY:
					if(!(operand1 == operand2))
					{
						assignment.printNode(counter);
						System.out.println(" failure");
						counter++;
						return false;
					}
					break;
				case INEQUALITY:
					if(!(operand1 != operand2))
					{
						assignment.printNode(counter);
						System.out.println(" failure");
						counter++;
						return false;
					}
					break;
				case LESS_THAN:
					if(!(operand1 < operand2))
					{
						assignment.printNode(counter);
						System.out.println(" failure");
						counter++;
						return false;
					}
					break;
				case GREATER_THAN:
					if(!(operand1 > operand2))
					{
						assignment.printNode(counter);
						System.out.println(" failure");
						counter++;
						return false;
					}
					break;
				default:
					System.err.println("ERROR: Unrecognized opCode in Constraint Checking: " + opCode + ". Program will now exit.");
					System.exit(-1);
					break;
				}
			}
				
		}
		
		return true; // Returns true if all the constraints are true	
	}
	
	public static Node backtrackingSearch(Node solution, ArrayList<String> csp)
	{
		return recursiveBacktracking(solution, csp);
	}
	
	public static Node recursiveBacktracking(Node assignment, ArrayList<String> csp)
	{
		if(baseCaseCheck(assignment, csp))
			return assignment;											// Returns if given a solution
		
		String var				= selectUnassignedVar(assignment);
		ArrayList<Integer> vals	= assignment.getLegalValues(var);
		
		for (int i = 0; i < vals.size() && counter < 30; i++)
		{
			assignment.setValue(var, vals.get(i));						// Add { var = value } to assignment
			
			if(constraintChecking(assignment,csp))
			{
				Node result = recursiveBacktracking(assignment, csp);	// Result <-- recrusiveBacktracking
				
				if(constraintChecking(result,csp) && checkForNull(result))
					return result;										// If result != failure, then return result
			}
			
			assignment.setValue(var, -999);								// Remove { var = value } from assignment
		}
		
		return null;													//return failure
	}
	
	public static boolean baseCaseCheck(Node assignment, ArrayList<String> csp)
	{
		// Checks the node to see if all constraints are satisfied and all variables are assigned
		if(!constraintChecking(assignment,csp) || !checkForNull(assignment))
			return false; // Returns false if there are conflicts
		
		return true;
	}
	
	private static boolean checkForNull(Node assignment)	// Checks to see if all variables have been instantiated (not null)
	{
		int[] vals = assignment.getValues();				// Place values into a temp int[] array
		
		for (int i = 0; i < vals.length; i++)				// Loop through all the values
		{
			if (vals[i] == -999)
				return false;								// If the value is null(-999), return false
		}
		
		return true;										// All the values are not null, return true
	}
	
	private static int getOpcode(char operator)				// Gets the opcode of the character passed to it (=, !, <, >)
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
	
	private static String selectUnassignedVar(Node assignment)	// Selects the next unassigned variable
	{
		ArrayList<String> vars	= assignment.getVariables();
		int[] vals				= assignment.getValues();
		
		for (int i = 0; i < vals.length; i++)
		{
			if (vals[i] == -999)
				return vars.get(i);
		}
		
		return null;
	}

	public static int getLeastConsVal(Node assignment,ArrayList<String> csp, String key)
	{
		Map<String, ArrayList<Integer>> legalValsMap = assignment.getLegalValMap();
		ArrayList<Integer> possibleVals = legalValsMap.get(key);							//get the possible values to choose from
		
		int leasttConstVal;
		int lowestNumVarsElim = -999;
		
		ArrayList<String> releventConstraints = new ArrayList<String>();
		ArrayList<String> keys = new ArrayList<String>();
		
		for(int i = 0; i < csp.size(); i++)													//go through the constraints and pick out the ones that the key is apart of
		{
			if(csp.get(i).contains(key))
				releventConstraints.add(csp.get(i));
		}
		
		for(int i = 0; i < releventConstraints.size(); i++)									//go through the reduced constraints list and pick out the other variables that are affected bythe key
		{
			if(key.compareTo(String.valueOf(releventConstraints.get(i).charAt(0))) == 0)
				if(!keys.contains(String.valueOf(releventConstraints.get(i).charAt(2))))
					keys.add(String.valueOf(releventConstraints.get(i).charAt(2)));
			else
				keys.add(String.valueOf(releventConstraints.get(i).charAt(0)));
		}
		
		
		
		
		for (int i = 0; i < possibleVals.size(); i++ )
		{
			int currentVal = possibleVals.get(i);
			
			for(int k = 0; k < keys.size(); k++)
			{
				ArrayList<Integer> temp = legalValsMap.get(keys.get(k));
				
				for(int j = 0; j < temp.size(); j++)
				{
					
				}
			}
			
		}
	}
	
}