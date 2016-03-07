import java.util.*;
public class Node
{
	private static ArrayList<String> variables;
	private int[] values;
	
	Node(ArrayList<String> vars)
	{
		if(variables == null)
			variables	= vars;
		this.values		= new int[variables.size()];
		
		for(int i = 0; i < values.length; i++)
			values[i] = -999;	// Uses -999 for null

	}
	
	public ArrayList<String> getVariables()
	{
		return variables;
	}
	
	public int[] getValues()
	{
		return this.values;
	}
	
	public void setValues(int[] val)
	{
		this.values = val;
	}
	
	
	public void printNode()
	{
		
		for(int i = 0; i < variables.size(); i++)
		{
			if(values[i] != -999)
				System.out.format("%s=%d ", variables.get(i), values[i]);
		}
		
		return;	
		
	}
	
}