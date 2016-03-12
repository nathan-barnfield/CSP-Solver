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
	public static boolean 	FCFlag;
	
	public static void main (String args[]) throws IOException
	{
		ArrayList<String> constraints 				= new ArrayList<String>();
		Map<String, ArrayList<Integer>> variables	= new HashMap<String, ArrayList<Integer>>();

		commandLineCheck(args);						// Format checking 
		
		if(args[2].compareTo("fc") == 0)
			FCFlag = true;
		else if(args[2].compareTo("none") == 0)
			FCFlag = false;
		else
		{
			System.out.println("Unexpected input for consistency enforcing procedure input:" + args[3] + ". Program will now exit.");
			System.exit(-1);
		}
			
		
		variables		= getVariables(args);		// Gets the variables
		constraints		= getConstraints(args);		// Gets the constraints
		Node startState = new Node(variables);		// Creates the start state for the algorithm
		Node solution	= backtrackingSearch(startState, constraints, FCFlag);
		
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
	
	public static Node backtrackingSearch(Node solution, ArrayList<String> csp, boolean FCFlag)
	{
		if(FCFlag)
			return recursiveBacktrackingWithFC(solution, csp);
		else
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
	
	
	public static Node recursiveBacktrackingWithFC(Node assignment, ArrayList<String> csp)
	{
		if(baseCaseCheck(assignment, csp))
			return assignment;											// Returns if given a solution
		
		
		String var				= assignment.getMostConstrainedVar(csp);
		ArrayList<Integer> vals	= assignment.getLegalValues(var);
		
		ArrayList<Integer> valsTried = new ArrayList<Integer>();
		
		for (int i = 0; i < vals.size() && counter < 30; i++)
		{
			int chosenVal = getLeastConstVal(assignment,csp,var, valsTried);
			assignment.setValue(var,chosenVal);						// Add { var = value } to assignment
			
			counter++;
			
			Node temp = forwardChecking(assignment, csp, chosenVal, var);
			if(temp != null && counter < 30)
			{
				Node result = recursiveBacktrackingWithFC(temp, csp);	// Result <-- recrusiveBacktracking
				
				if(constraintChecking(result,csp) && checkForNull(result))
					return result;										// If result != failure, then return result
			}
			
			assignment.setValue(var, -999);								// Remove { var = value } from assignment
			valsTried.add(chosenVal);
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

	public static int getLeastConstVal(Node assignment,ArrayList<String> csp, String key, ArrayList<Integer> valsTried)				//key being the variable that was chosen
	{
		Map<String, ArrayList<Integer>> legalValsMap = assignment.getLegalValMap();
		ArrayList<Integer> possibleVals = legalValsMap.get(key);							//get the possible values to choose from
		
		for(int i = 0; i < valsTried.size(); i++)
			if(possibleVals.contains(valsTried.get(i)))
				possibleVals.remove(valsTried.get(i));
		
		int leastConstVal = possibleVals.get(0);
		int lowestNumVarsElim = Integer.MAX_VALUE;
		
		ArrayList<String> releventConstraints = new ArrayList<String>();
		ArrayList<String> keys = new ArrayList<String>();
		
		for(int i = 0; i < csp.size(); i++)													//go through the constraints and pick out the ones that the key is apart of
		{
			if(csp.get(i).contains(key))
				releventConstraints.add(csp.get(i));
		}
		
		for(int i = 0; i < releventConstraints.size(); i++)									//go through the reduced constraints list and pick out the other variables that are affected by the key
		{
			if(key.compareTo(String.valueOf(releventConstraints.get(i).charAt(0))) == 0)
				if(!keys.contains(String.valueOf(releventConstraints.get(i).charAt(2))))
					keys.add(String.valueOf(releventConstraints.get(i).charAt(2)));
			else
				if(!keys.contains(String.valueOf(releventConstraints.get(i).charAt(0))))
					keys.add(String.valueOf(releventConstraints.get(i).charAt(0)));
		}
		
		for (int i = 0; i < possibleVals.size(); i++ )
		{
			int currentVal = possibleVals.get(i);
			int currentCount = 0;
			for(int k = 0; k < keys.size(); k++)
			{
				ArrayList<Integer> temp = legalValsMap.get(keys.get(k));
				
				for(int j = 0; j < temp.size(); j++)
				{
					for(int l = 0; l < releventConstraints.size();l++)
					{
						if(releventConstraints.get(l).contains(keys.get(k)))
						{
							//put switch statement here
							String constraint	= releventConstraints.get(l);
							int opCode			= getOpcode(constraint.charAt(1));
							int operand1, operand2;
							if(keys.get(k).compareTo(String.valueOf(constraint.charAt(0))) == 0)
							{
								 operand1		= temp.get(j);	//if the variable being constrained is the first op, place that value in operand 1
								 operand2		= currentVal;
							}
							else
							{
								 operand1		= currentVal;	//if the variable being constrained is the second op, place that value in operand 2
								 operand2		= temp.get(j);
							}
							
							switch(opCode)
							{
							case EQUALITY:
								if(!(operand1 == operand2))
									currentCount++;
								break;
							case INEQUALITY:
								if(!(operand1 != operand2))
									currentCount++;
								break;
							case LESS_THAN:
								if(!(operand1 < operand2))
									currentCount++;
								break;
							case GREATER_THAN:
								if(!(operand1 > operand2))
									currentCount++;
								break;
							default:
								System.err.println("ERROR: Unrecognized opCode in get least constraining value method: " + opCode + ". Program will now exit.");
								System.exit(-1);
								break;
							}
							
						}
					}
				}
			}
			
			if(currentCount < lowestNumVarsElim)
				leastConstVal = currentVal;
		}
		return leastConstVal;
	}
	
	public static Node forwardChecking(Node assignment, ArrayList<String> csp, int valAssigned, String varAssigned)
	{
		Map<String, ArrayList<Integer>> legalValsMap = assignment.getLegalValMap();		//get the map that contains all the current legal values
		ArrayList<String> releventConstraints = new ArrayList<String>();				//all the constraints that the variable is involved in
		ArrayList<String> keys = new ArrayList<String>();								//ArrayList that holds all the variables constrained by the chosen variable
		
		for(int i = 0; i < csp.size(); i++)												//go through the constraints and pick out the ones that the chosen variable is a part of
		{
			if(csp.get(i).contains(varAssigned))
				releventConstraints.add(csp.get(i));
		}
		
		for(int i = 0; i < releventConstraints.size(); i++)									//go through the reduced constraints list and pick out the other variables that are affected by the chosen variable
		{
			if(varAssigned.compareTo(String.valueOf(releventConstraints.get(i).charAt(0))) == 0)
				if(!keys.contains(String.valueOf(releventConstraints.get(i).charAt(2))))
					keys.add(String.valueOf(releventConstraints.get(i).charAt(2)));
			else
				if(!keys.contains(String.valueOf(releventConstraints.get(i).charAt(0))))
					keys.add(String.valueOf(releventConstraints.get(i).charAt(0)));
		}
		
		for(int i = 0; i < keys.size(); i++)
		{
			ArrayList<Integer> temp = legalValsMap.get(keys.get(i));
			ArrayList<Integer> valsToRemove = new ArrayList<Integer>();
			
			for(int k = 0;k < temp.size(); k++)
			{
				for(int j = 0; j < releventConstraints.size();j++)
				{
					if(releventConstraints.get(j).contains(keys.get(i)))
					{
						//put switch statement here
						String constraint	= releventConstraints.get(j);
						int opCode			= getOpcode(constraint.charAt(1));
						int operand1, operand2;
						if(keys.get(i).compareTo(String.valueOf(constraint.charAt(0))) == 0)
						{
							 operand1		= temp.get(k);	//if the variable being constrained is the first op, place that value in operand 1
							 operand2		= valAssigned;
						}
						else
						{
							 operand1		= valAssigned;	//if the variable being constrained is the second op, place that value in operand 2
							 operand2		= temp.get(k);
						}
						
						switch(opCode)
						{
						case EQUALITY:
							if(!(operand1 == operand2))
								if(!valsToRemove.contains(temp.get(k)))
									valsToRemove.add(temp.get(k));
							break;
						case INEQUALITY:
							if(!(operand1 != operand2))
								if(!valsToRemove.contains(temp.get(k)))
									valsToRemove.add(temp.get(k));
							break;
						case LESS_THAN:
							if(!(operand1 < operand2))
								if(!valsToRemove.contains(temp.get(k)))
									valsToRemove.add(temp.get(k));
							break;
						case GREATER_THAN:
							if(!(operand1 > operand2))
								if(!valsToRemove.contains(temp.get(k)))
									valsToRemove.add(temp.get(k));
							break;
						default:
							System.err.println("ERROR: Unrecognized opCode in get least constraining value method: " + opCode + ". Program will now exit.");
							System.exit(-1);
							break;
						}
						
					}
				
				
				
			}
			
			
			
			}
			
			for(int l = 0; l < valsToRemove.size(); l++)
			{
				if(temp.contains(valsToRemove.get(l)))
					temp.remove(valsToRemove.get(l));
			}
			if(temp.size() == 0)
				return null;
			assignment.setLegalVar(temp, keys.get(i));
			
		}
		return assignment;
	}
}
