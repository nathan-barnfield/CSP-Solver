import java.util.*;
import java.util.Map.Entry;

public class Node
{
	private static ArrayList<String> variables;
	private int[] values;
	private static Map<String, ArrayList<Integer>> variableMap;
	private static Map<String, ArrayList<Integer>> legalVals;
	
	Node(Map<String, ArrayList<Integer>> v)
	{
		if (variableMap == null)
		{
			this.legalVals = v;
			this.variableMap = v;
		}
		
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
	
	public  Map<String,ArrayList<Integer>> getLegalValMap()
	{
		return legalVals;
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
	
	public void setLegalVar(ArrayList<Integer> legalVar, String key)
	{
		legalVals.remove(key);
		legalVals.put(key, legalVar);
	}
	
	
	public String getMostConstrainedVar(ArrayList<String> csp)				//Function that returns the most constrained variable
	{
		ArrayList<String> validVariables = new ArrayList<String>();			//ArrayList to hold the 
		ArrayList<Integer> constraintVals = new ArrayList<Integer>();
		
		for(int i = 0; i < variables.size(); i++)
		{
			if(values[i] == -999)
				validVariables.add(variables.get(i));
		}
		
		String mostConstrainedVar = new String();
		int highestVal = 0;
		
		for(int i = 0; i < validVariables.size(); i++)
		{
			constraintVals = legalVals.get(validVariables.get(i));
			
			if(constraintVals.size() > highestVal)
			{
				highestVal = constraintVals.size();
				mostConstrainedVar = validVariables.get(i);
			}
			else if(constraintVals.size() == highestVal)
				mostConstrainedVar = tiebreak(validVariables.get(i),mostConstrainedVar, csp);
		}
		return mostConstrainedVar;
	}
	
	private String tiebreak(String var1, String var2, ArrayList<String> csp)	//function to find the most constraining variable
	{
		int var1Occ = 0;
		int var2Occ = 0;
		
		for(int i = 0; i < csp.size(); i++)
		{
			if(csp.get(i).contains(var1))
				var1Occ++;
			if(csp.get(i).contains(var2))
				var2Occ++;
		}
		
		if(var1Occ > var2Occ)
			return var1;
		else
			return var2;
	}
	
	
}