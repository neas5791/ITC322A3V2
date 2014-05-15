import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;



public class Driver {
	
	public static Scanner scanner = new Scanner(System.in);
	private static Maze m;
	private static String name;
	
	public static void main (String [] args){
		
		//load a small maze by default
		name = "maze01.mz";
		m = new Maze(name);
		
		boolean quit = false;
		int menu;
		do{
			System.out.println("\nWelcome to the Maze Driver by Sean Matkovich #11187033\nMaze file " + name + " currently loaded\n"
							 + "========================================================");
			
				
			System.out.print("This program will demonstrate the implemented features of the Maze Class.\n"
							+ "\nSelect from the following menu by entering menu number at the prompt."
							+ "\n1. Load Maze a file." // working
							+ "\n2. Display Maze"  // working
							+ "\n3. Find path using Depth-First-Search"
							+ "\n4. Find shortest path using Dijkstra's Algorithm"
							+ "\n5. "
							+ "\n6. "
							+ "\n7. "
							+ "\n8. Quit."
							+ "\nMake your selection: ");
			menu = scanner.nextInt();

			switch (menu){
			case 1:
				loadMazeMenu();
				break;
			case 2:
				System.out.println();
				// return the maze to original state
				m.clearPath();
				if (m != null)
					System.out.println(m.toString());
				
				suspend("Press enter to continue....");
				break;
			case 3:
				if (m != null) {
					m.DepthFirst(0);
					
					System.out.println(m.toString());
					suspend("Press enter to continue....");
				}
				break;
			case 4:
				m.shortestPath(0, m.size() - 1);
				
				System.out.println(m.toString());
				suspend("Press enter to continue....");
				break;
			case 5:

			case 6:

			case 7:

			default:
				quit = !quit;
				System.out.println("See you later :) hope you had fun!!");
				break;
			}
		}while (!quit);
	}
	
	private static void loadMazeMenu(){
		boolean quit = false;
		int menu;
		do{
			System.out.println("\nLOAD MAZE FILE; \n"
								+ "===============\n");
				
			System.out.print("\nSelect from the following menu by entering menu number at the prompt."
							+ "\n1. Load \"maze01.mz\" file."
							+ "\n2. Load \"maze02.mz\" file."
							+ "\n3. Load \"maze03.mz\" file."
							+ "\n4. back to main menu."
							+ "\nMake your selection: ");
			menu = scanner.nextInt();
						
			switch (menu){
			case 1:
				name = "maze01.mz";
				m = new Maze(name);
				quit = !quit;
				break;
			case 2:
				name = "maze02.mz";
				m = new Maze(name);
				quit = !quit;
				break;
			case 3:
				name = "maze03.mz";
				m = new Maze(name);
				quit = !quit;
				break;
			default:
				quit = !quit;
				System.out.println("Back to main menu");
				break;
			}
		}while (!quit);
		
		
	}
	
	/**
	 * Method to halt screen an wait for input before proceeding
	 * @param str is the String contents for display when halted
	 */
	private static void suspend(String str){
		// create a buffer to capture system.in activity
		BufferedReader buf = new BufferedReader (new InputStreamReader (System.in));
		
		String ent = null;
		
		
		do{
			System.out.println(str);
			try {
				ent = buf.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}while(ent.equals("\n"));	
	}
}
