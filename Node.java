public class Node
{
	
	private char[] variables;
	private int[] values;
	
	Node(char[] vars)
	{
		this.variables = vars;
		this.values = new int[variables.length];
	}
	
}
