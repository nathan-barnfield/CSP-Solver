public class Node
{
	
	private String[]	variables;
	private int[]		values;
	
	Node(String[] vars)
	{
		
		this.variables	= vars;
		this.values		= new int[variables.length];
		
		for(int i = 0; i < values.length; i++)
			values[i] = -999;	// Uses -999 for null

	}
	
	public void printNode()
	{
		
		for(int i = 0; i < variables.length; i++)
		{
			if(values[i] != -999)
				System.out.format("%s=%d ", variables[i], values[i]);
		}
		
		return;	
		
	}

}
