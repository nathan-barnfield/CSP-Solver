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
	private static Map<String, ArrayList<Integer>> valLists;
	
	public static void main (String args[]) throws FileNotFoundException
	{
		//commandLineCheck(args);
		// Variable Declaration
		//
		// File input of the .var file
		//
		Scanner varInput = new Scanner(/*new File(args[0])*/"ex1.var1");
		
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
		
		valLists.get("A").toString();
		
		String cmdLine;
		Scanner input = new Scanner(System.in);
		String fileVariables = "ex1.var";
		String fileConstrants = "ex1.con";
		
/*		
		// Prompt the user for the required file names
		System.out.println(">> ENTER COMMAND: <variables.var> <constraints.con> <none|fc>");
		System.out.println(">> NOTE: cAsE sEnSiTiVe; Don't include brackets; Files requires file type");
		cmdLine = input.nextLine();
		String[] words = cmdLine.split(" "); // Used to read the input
*/			
		
		
	
	}
	
	public static void countVariables(String fileVariables) throws FileNotFoundException
	{
		BufferedReader br = new BufferedReader(new FileReader(fileVariables));
	}

}