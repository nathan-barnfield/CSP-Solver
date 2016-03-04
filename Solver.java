/*
 * Name:		Daniel Pacheco & Nathan Barnfield
 * Course:		CS 4365.001
 * Homework:	#2
 * Due Date:	March 10th, 2016
 * Class:		Solver.java
 * Purpose:		[Insert Here]
*/

import java.util.Scanner;
import java.io.*;

public class Solver
{
	
	// Operation Codes
	public static final int EQUALITY     = 0;
	public static final int INEQUALITY	 = 1;
	public static final int LESS_THAN    = 2;
	public static final int GREATER_THAN = 3;

	public static void main (String args[])
	{
		
		// Variable Declaration
		String cmdLine;
		Scanner input = new Scanner(System.in);
		String fileVariables = "ex1.var";
		String fileConstrants = "ex1.con";
		
/*		
		// Prompt the user for the required file names
		System.out.println(">> ENTER COMMAND: <variables.var> <oconstraints.con> <none|fc>");
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