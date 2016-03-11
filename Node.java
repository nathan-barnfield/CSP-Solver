import java.util.*;
import java.util.Map.Entry;

public class Node
{
	private static ArrayList<String> variables;
	private int[] values;
	private static Map<String, ArrayList<Integer>> variableMap;
	
	Node(Map<String, ArrayList<Integer>> v)
	{
		if (variableMap == null)
			this.variableMap = v;
		
		variables	= new ArrayList<>();
		Iterator it	= v.entrySet().iterator();
		
		while(it.hasNext())	
		{
			Map.Entry entry = (Entry) it.next();
			String temp		= (String) entry.getKey();
			variables.add(temp);	// Populates the variable ArrayList from the HashMap
		}
		
		this.values		= new int[variables.size()];
		
		for(int i = 0; i < values.length; i++)
			values[i] = -999;		// Uses -999 for null
	}
	
	public ArrayList<String> getVariables()
	{
		return variables;
	}
	
	public int[] getValues()
	{
		return this.values;
	}
	
	public ArrayList<Integer> getLegalValues(String var)
	{
		return variableMap.get(var);
	}
	
	public void setValue(String var, int val)
	{
		this.values[variables.indexOf(var)] = val;
	}
	
	public void printNode(int c)
	{
		String counter	= (++c) + ". ";
		String output	= "";
		
		for(int i = 0; i < variables.size(); i++)
		{
			if(values[i] != -999)
				output = output + variables.get(i) + "=" + values[i] + " ";
		}
		
		System.out.format("%-4s", counter);
		System.out.format("%-30s", output);
		
		return;	
	}
	
	public String getMostConstrainedVar(ArrayList<String> csp)
	{
		ArrayList<String> validVariables = new ArrayList<String>();
		ArrayList<Integer> constraintVals = new ArrayList<Integer>();
		
		for(int i = 0; i < variables.size(); i++)
		{
			if(values[i] == -999)
				validVariables.add(variables.get(i));
		}
		
		for(int i = 0; i < csp.size(); i++)
		{
			
		}
		
		
		return mostConstrainedVar;
	}
	
}