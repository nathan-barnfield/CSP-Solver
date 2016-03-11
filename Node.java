import java.util.*;
import java.util.Map.Entry;

public class Node
{
	private static ArrayList<String> variables;
	private int[] values;
	
	Node(Map<String, ArrayList<Integer>> v)
	{
		variables	= new ArrayList<>();
		Iterator it	= v.entrySet().iterator();
		
		while(it.hasNext())	
		{
			Map.Entry entry = (Entry) it.next();
			String temp = (String) entry.getKey();
			variables.add(temp);	// Populates the variable ArrayList from the HashMap
			it.remove();			// Avoids ConcurrentModificationException
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
	
	public void setValue(String var, int vals)
	{
		
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