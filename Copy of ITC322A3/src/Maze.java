import java.io.*;
//import java.lang.reflect.Array;
import java.util.Scanner;
import java.util.Stack;

import edu.colorado.graphs.DSP;
import edu.colorado.graphs.Graph;;

public class Maze {
	
	private char[][] maze;
	private int row;
	private int col;
	public Graph g;

	Stack<Integer> depthFirst;// = new Stack<Integer>();
	Stack<Integer> shortPath;// = new Stack<Integer>();
	
	/**
	 *  Constructor for the maze object. This object is developed from the details found in the maze "filename".  
	 *   
	 *  @param filename is the maze file to be converted into a maze object
	 *  @preconditions 
	 *  A maze file starts with two integers representing the number of rows and the number of columns of the 
	 *  maze cell. A maze cell corner is represented by + and walls by “-“ (horizontal walls) and “|” (vertical 
	 *  walls). The below shows an example of maze files which represents a maze with cells in 4 rows and 5 
	 *  columns. We already consider the left-top cell as the starting cell and the bottom-right cell as the 
	 *  ending cell.They are marked by “s” and “e” respectively in the above picture. However both markers are 
	 *  not in a maze file. Please note in this representation, a cell with all four walls is written as
	 *
	 *	+-+
	 *	| |
	 *	+-+
	 *	
	 *  If any of “-“ or “|” is missing, there is no wall on that side. The corner “+” is always there.
 	 *  
	 */
	public Maze(String filename){
		createMazeFromFile(filename);
		createGraph();
	}

	private void createMazeFromFile(String filename){
		Scanner scanner;
	
		try {	
			scanner = new Scanner(new FileInputStream(filename));

			// A maze file starts with two integers representing the number of 
			// rows and the number of columns of the maze cell. i.e. the vertices
			row = scanner.nextInt();
			col = scanner.nextInt();
			scanner.nextLine();
			
			
			// Initialize the maze char[], this array is the graphical representation of the maze
			// As each vertices is surrounded by walls char[] representation array is much larger in dimensions
			maze = new char[(row * 2) + 1] [(col * 2) + 1];
			
			// From this point forward the maze file is reading the actually maze into a char[]
			String line;
			int i = 0;
			
			// begins to read line by line the maze file
			// converting each line to character array which is added to the maze
			while (scanner.hasNextLine()){
				line = scanner.nextLine();
				
				// checks for string lines that are not the correct length
				// i.e. these could be openings
				while(line.length() != (col * 2) + 1 ){
					line = line + " ";
				}
				// adds the whole line from the file as string array
				this.maze[i] = (line.toCharArray());
				i++;
			}
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		
		
	}
		
	/**
	 * 	Initializes and creates a Graph object from the maze information
	 *  satisfies parts 3. of Assignment paper
	 */
	private void createGraph(){
		g = new Graph(row * col);
		int vertices = 0;
		
		// Iterating through the maze array from the first vertices
		// we will only test possible vertices
		for (int i = 1; i < (row * 2) + 1; i += 2){
			for(int j = 1; j < (col * 2) + 1; j += 2){
				
				// test for edge to the NORTH
				if ( Character.isWhitespace(maze[i - 1][j]) )
				{
					if ( (i - 1) != 0) {
						g.addEdge(vertices, vertices - col);
						g.addEdge(vertices - col, vertices);
					}
				}
				
				// test for edge to the SOUTH				
				if ( Character.isWhitespace(maze[i + 1][j]) )
				{
					if ( (i + 1) != (row * 2)) {
						g.addEdge(vertices, vertices + col);
						g.addEdge(vertices + col, vertices);
					}					
				}
				
				// test for edge to the WEST				
				if ( Character.isWhitespace(maze[i][j - 1]) )
				{
					if ( (j - 1) != 0 ) {
						g.addEdge(vertices, vertices - 1);
						g.addEdge(vertices - 1, vertices);
					}
						
				}
				
				// test for edge to the EAST
				if ( Character.isWhitespace(maze[i][j + 1]) )
				{
					if ( (j + 1) != (col * 2) ) {
						g.addEdge(vertices, vertices + 1);
						g.addEdge(vertices + 1, vertices);
					}
				}
				
				g.setLabel(vertices, "v" + vertices);
				vertices++;
			}
		}
	}
	
	/**
	 * 	Displays a table representation of the Adjacency Matrix of Graph objects
	 *  Outputs a copy of the Adjacency Matrix in an ASCII file called "adjacencyMatix"  
	*/
	public void printTable(){
	   String s = "";
	   File adjacencyMatrix = new File("adjacencyMatrix");  
	   BufferedWriter writer = null;
	   try {
		   // The below line will output the full path where the file will be written to... 
		   // System.out.println(adjacencyMatrix.getCanonicalPath());
		   
		   writer = new BufferedWriter(new FileWriter(adjacencyMatrix));
		   
		   for ( int i = 0; i < g.size(); i++ ){
			   if (i == 0)
				   s = s + String.format("%8s%-6d","", i);
		   else
			   s = s + String.format("%-6d", i);
		   }
		   
		   System.out.println(s);
           writer.write(s + "\n");
           writer.newLine();
		   for (int i = 0 ; i < g.size(); i ++){
			   s = String.format("%-6d", i);
			   for (int j = 0 ; j < g.size(); j++){
				   if (g.isEdge(i,j))
					   s = s + String.format("%-6s", g.isEdge(i,j));
				   else
					   s = s + String.format("%2s%s%-3s", "","-", "");
			   }
			   System.out.println(s);
			   writer.write(s);
			   writer.newLine();
		   }
		   writer.close();	   
	   } catch (IOException e) {
		   e.printStackTrace();
	   }
   }

	
	/**
	 * Conducts a depth first search of the maze
	 * @param v is the vertex being inspected
	 * @param visited is the array that track if the vertex has previously been inspected
	 * @return true when path exists to destination vertex (Bottom right as per outline of assignment)
	 */
	private boolean DepthFirstSolution(int v, boolean [] visited){
		// Checks if this vertex is the exit point of the maze
		if(v == (g.size() - 1)){
			depthFirst.push(v);
			return true;
		}
	
		
		// Get list of the children of the vertex
		int[ ] connections = g.neighbors(v);
		// nextNeighbour is one of the neighbors in the connections array
		int nextNeighbor;
  
		// Mark this vertex as visited in the array 
		visited[v] = true;
		
		if (depthFirst == null)
			depthFirst = new Stack<Integer>();
		
		// Push this vertex onto the stack
		depthFirst.push(v);

		// System.out.println(g.getLabel(v));

		// Traverse all the neighbors, looking for unmarked vertices:
		for (int i = 0; i < connections.length; i++)
		{
			nextNeighbor = connections[i];
			if (!visited[nextNeighbor]) {
				if (DepthFirstSolution(nextNeighbor, visited)){ return true;}
				depthFirst.pop();
			}
		}
		
		return false;
	
	}

	/**
	 * Output's the order of vertices for which the path takes
	 */
	@SuppressWarnings("unchecked")
	public void printPath(Stack<Integer> path){
		
		Stack<Integer> newStack = new Stack<Integer>();
		Stack<Integer>  stackClone;
		stackClone = (Stack<Integer>) path.clone();
		
		while (!stackClone.empty())
			newStack.push(stackClone.pop());
		
		System.out.println();
		String pathStr = "";
		while (!newStack.empty()){
			if (pathStr.length() == 0)
				pathStr = String.format("%d", newStack.pop());
			else
				pathStr = pathStr + String.format(", %d", newStack.pop());
		}
		System.out.println(pathStr);
		System.out.println();
	}
	
	/**
	 * update the character representation of the maze with the given path
	 * 
	 * @param path is the order of vertices in which travel will be taken
	 */
	@SuppressWarnings("unchecked")
	private void updateMazeWithPath(Stack<Integer> path){
		int i;
		
		Stack<Integer> stackCopy = (Stack<Integer>) path.clone();
		
		clearPath();

		while (!stackCopy.empty()){
			i = stackCopy.pop();
			
			// check if vertex is goal vertex
			if (i == g.size()-1)
				maze[1 + (i/col) * 2][1 + (i%col) * 2] = 'e';
			// check if vertex is start vertex
			else if (i == 0)
				maze[1 + (i/col) * 2][1 + (i%col) * 2] = 's';
			//check if vertex has moved south
			else if (i - stackCopy.peek() == col) 
					maze[1 + (i/col) * 2][1 + (i%col) * 2] = '^';
			//check if vertex has moved north
			else if (i - stackCopy.peek() == -col)
				maze[1 + (i/col) * 2][1 + (i%col) * 2] = 'v';
			//check if vertex has moved west
			else if (i - stackCopy.peek() == 1)
				maze[1 + (i/col) * 2][1 + (i%col) * 2] = '<';
			//check if vertex has moved north
			else if (i - stackCopy.peek() == -1)
				maze[1 + (i/col) * 2][1 + (i%col) * 2] = '>';
		}
	}
	
	/* 
	 * 	This method has been overriden to output a graphical representation of the maze
	 * 
	 */
	public String toString(){
		String screen = "";
		
		int r = (row * 2) +1;
		int c = (col * 2) +1;
		for (int i = 0; i < r; i++){
			for(int j = 0; j < c; j++){
				if (screen.isEmpty())
					screen = ((Character) maze[i][j]).toString();
				else
					screen = screen + ((Character) maze[i][j]).toString();
			}
			screen = screen + "\n";
		}
				
		return screen;
	}
	
	/**
	 * Finds the shortest path between two vertices using Dijkstra's Algorithm
	 * 
	 * @param start the originating vertex
	 * @param destination the destination vertex
	 * @throws IllegalArgumentException 
	 *   Indicates that the start or destination vertex is outside the scope of the graph
	 */
	public void shortestPath(int start, int destination){

		if (start < 0 || start >= size())
			throw new IllegalArgumentException("Start vertex does not exist");
		if (destination < 0 || destination >= size())
			throw new IllegalArgumentException("Destination vertex does not exist");
		
		DSP d = new DSP(g);
		d.buildSpanningTree(start, size()-1);
		
		shortPath = new Stack<Integer>();
		
		for (int x : d.getShortestPath(0, g.size()-1))
			shortPath.push(x);
		
		System.out.println("The vertices indexes of the shortest path are:");
		updateMazeWithPath(shortPath);

		printPath(reverse(shortPath));
	}
	
	/**
	 * Returns the number of vertices in the maze based on the number of rows and columns 
	 * @return integer value of number of vertices
	 */
	public int size(){
		return (row * col);
	}
	
	/**
	 * Displays the path results of a depth first search
	 * @param start is the origin vertex
	 */
	public void DepthFirst(int start){
		// holder array for visit results
		boolean [] visited = new boolean[size()];
		
		// Initialize stack
		depthFirst = new Stack<Integer>();
		
		// conduct a depth first search of the graph
		DepthFirstSolution(start, visited);
	
		System.out.println("The vertices indexes of the depth first approach path are:");
		// update the maze array with the path representation
		updateMazeWithPath(reverse(depthFirst));
		
		// print the resulting maze updates to the screen
		printPath(depthFirst);
	}

	/**
	 * Clears any path information placed into the maze
	 */
	public void clearPath(){
		for (int z = 0 ; z < size(); z++)
			maze[1 + (z/col) * 2][1 + (z%col) * 2] = ' ';
	}

	@SuppressWarnings("unchecked")
	private Stack<Integer> reverse(Stack<Integer> stack){
		Stack<Integer> newStack = new Stack<Integer>();
		Stack<Integer> copyStack = (Stack<Integer>) stack.clone();
		
		while (!copyStack.isEmpty())
			newStack.push(copyStack.pop());
		
		return newStack;
	}

	
}
