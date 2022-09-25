package labrynth.CS146;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Labyrinth {
	// currentLabyrinth - an arraylist implementation of a graph
	private Vertex[] currentLabyrinth;
	// textLabyrinth - an array of Strings which depict the currentLabyrinth
	private String[] textLabyrinth;
	// dfsIterationNum - holds the current iteration number for a DFS traversal
	private int dfsIterationNum;
	//number of cells visited per search method
	private int visitedCells;

	// getVisitedCells - returns number of cells visited
	public int getVisitedCells() {
		return visitedCells;
	}

	// createLabyrinth - create an empty graph
	public void createLabyrinth(int size) {
		currentLabyrinth = new Vertex[size];
		for (int i = 0; i < size; i++) {
			currentLabyrinth[i] = new Vertex(i);
		}
	}

	public double getLabyrinthLength() {
		return Math.sqrt((double) (currentLabyrinth.length));
	}

	// printLabyrinthGraph - prints the arrayList depicition of currentLabyrinth
	public void printLabyrinthGraph() {
		for (Vertex vertex : currentLabyrinth) {
			System.out.print("\n" + vertex.getIndex() + " -> ");
			Iterator<Vertex> iter = vertex.getEdgeIterator();
			while (iter.hasNext()) {
				System.out.print(iter.next().getIndex() + ", ");
			}
		}
		System.out.println();
	}

	// generateLabyrinthString - generate a visual representation (with strings) of the Labyrinth
	// this function assumes the Labyrinth is square
	public void generateLabyrinthString(boolean viewNumber) {
		// length - the length of one side of currentLabyrinth
		int length = (int) (Math.sqrt((double) (currentLabyrinth.length)));
		// visitedCells - the number of cells visited
		int visitedCells = 0;
		textLabyrinth = new String[2 * length + 1];
		textLabyrinth[0] = "+ +";
		for (int i = 0; i < length - 1; i++) {
			textLabyrinth[0] += "-+";
		}
		for (int j = 1; j < textLabyrinth.length - 1; j += 2) {
			textLabyrinth[j] = "|";
			textLabyrinth[j + 1] = "+";
			for (int i = visitedCells; i < visitedCells + length; i++) {
				Iterator<Vertex> iter = currentLabyrinth[i].getEdgeIterator();
				boolean next = false;
				boolean below = false;
				while (iter.hasNext()) {
					Vertex v = iter.next();
					if (v.getIndex() == i + 1) {
						next = true;
					}
					if (v.getIndex() == i + length) {
						below = true;
					}
				}
				if (currentLabyrinth[i].getViewNumber() != -99) {
					if (viewNumber == true) {
						if (next == true) {
							if (currentLabyrinth[i].getViewNumber() == Integer.MIN_VALUE)
								textLabyrinth[j] += " " + " ";
							else
								textLabyrinth[j] += currentLabyrinth[i].getViewNumber() + " ";
						} else {
							if (currentLabyrinth[i].getViewNumber() == Integer.MIN_VALUE)
								textLabyrinth[j] += " " + "|";
							else
								textLabyrinth[j] += currentLabyrinth[i].getViewNumber() + "|";
						}
					} else {
						if (next == true)
							textLabyrinth[j] += currentLabyrinth[i].getValue() + " ";
						else
							textLabyrinth[j] += currentLabyrinth[i].getValue() + "|";
					}

					if (i == length * length - 1)
						below = true;

					if (below == true)
						textLabyrinth[j + 1] += " " + "+";
					else
						textLabyrinth[j + 1] += "-" + "+";
				} else {
					if (i == length * length - 1)
						below = true;
					if (next == true)
						textLabyrinth[j] += currentLabyrinth[i].getValue() + " ";
					else
						textLabyrinth[j] += currentLabyrinth[i].getValue() + "|";
					if (below == true)
						textLabyrinth[j + 1] += " " + "+";
					else
						textLabyrinth[j + 1] += "-" + "+";
				}

			}
			visitedCells += length;
		}
	}

	// printLabyrinth - prints a visual representation of the current graph
	public void printLabyrinthText(boolean viewNumber) {
		generateLabyrinthString(viewNumber);
		for (String s : textLabyrinth) {
			System.out.println(s);
		}
		System.out.println("");
	}

	/**
	 * readLabyrinth reads and save to memory the text files containning square labyrinths
	 * the labyrinth text file is converted to a arrayList implementation of a graph
	 *
	 * @param String - the name of the file which holds a labyrinth we would like to load
	 */
	public void readLabyrinth(String fileName) {
		try {
			// br - short for BufferReader, used bring lines of the text file into the program to be processed
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			// currentLine - the line in the Labyrinth which is currently being interpreted
			int currentLine = 2;
			// currentVertex - the Vertex in the labyrinth which is currently being populated
			int currentVertex = 0;
			// lineSeperator - used to evaluate whether the line contains horizontal or verticle edges
			int lineSeperator = 1;
			// edgeDivider - edge divider is used to evaluate whether there is an edge between two vertices
			int edgeDivider;
			String[] widthHeight = br.readLine().split(" ");
			int width = Integer.parseInt(widthHeight[0]);
			int height = Integer.parseInt(widthHeight[1]);
			createLabyrinth(width * height);
			br.readLine();
			String[] uncleanLine = {};
			while (currentLine++ < height * 2 + 1) {
				uncleanLine = br.readLine().split("");
				if (lineSeperator++ % 2 == 1) {
					// we are reading a line containing Horizontal edges
					edgeDivider = 2;
					while (edgeDivider < uncleanLine.length) {
						if (uncleanLine[edgeDivider].equals(" ")) {
							currentLabyrinth[currentVertex].addEdge(currentLabyrinth[currentVertex + 1]);
							currentLabyrinth[currentVertex + 1].addEdge(currentLabyrinth[currentVertex]);
						}
						edgeDivider = edgeDivider + 2;
						currentVertex++;
					}
				} else {
					// we are reading a line containning Vertical edges
					edgeDivider = 1;
					currentVertex = currentVertex - width;
					while (edgeDivider < uncleanLine.length) {
						if (uncleanLine[edgeDivider].equals(" ")) {
							currentLabyrinth[currentVertex].addEdge(currentLabyrinth[currentVertex + width]);
							currentLabyrinth[currentVertex + width].addEdge(currentLabyrinth[currentVertex]);
						}
						edgeDivider = edgeDivider + 2;
						currentVertex++;
					}
				}
			}
			br.close();
		} catch (IOException e) {
			System.out.println("The filename given was not found!!!");
		}
	}

	// resetAllVertices - sets the default values of all instances of Vertex in currentLabyrinth
	public void resetAllVertices() {
		for (int i = 0; i < currentLabyrinth.length; i++) {
			currentLabyrinth[i].reset();
		}
	}

	public void dfs() {
		resetAllVertices(); //resets all of the vertices to their original state

		visitedCells = 0;
		dfsIterationNum = 0; //sets the current iteration number to zero
		currentLabyrinth[0].setDistance(0); //since all labyrinths start at zero we can set d to zero
		dfsVisit(currentLabyrinth[0]); //we only need to start form the 0th vertex
	}

	public void dfsVisit(Vertex v) {
		v.setColor(Color.GRAY); //set color of current vertex to grey
		visitedCells++;
		v.setViewNumber(dfsIterationNum); //sets the view number of the vertex

		if (dfsIterationNum == 9) //if iterationNum = 9 change it to zero
			dfsIterationNum = 0;
		else
			dfsIterationNum++; //else add 1 to it

		//Initialization of the vertex iterator
		Iterator<Vertex> iter = v.getEdgeIterator();

		if (v.getIndex() != currentLabyrinth.length - 1) {
			while (iter.hasNext()) {
				Vertex temp = iter.next(); //gets the next vertex in the LinkedList
				if (temp.getColor().equals(Color.WHITE)) { //if the color of the vertex is white
					temp.setParent(v); //sets the parent of the temporary vertex
					temp.setDistance(v.getDistance() + 1); //sets the distance of the temporary vertex
					dfsVisit(temp); //Recursive call of the of dfsVisit using the temp vertex
				}
			}
			v.setColor(Color.BLACK); //sets the color of the current vertex to black
		}
	}

	public ArrayList<Integer> traceDFSBestPath() {
		dfs();
		// this will hold an ArrayList of integers that represent the path to take to
		// complete the labyrinth
		ArrayList<Integer> path = new ArrayList<>();
		// Instance of a vertex that keeps track of which vertex we are on
		Vertex current = currentLabyrinth[currentLabyrinth.length - 1];

		// this will loop through all of the parents of the last vertex(the exit) until
		// we hit the entrance
		while (current.getParent() != null) { // exit if the vertex has no parent
			current.setValue("#"); // set the value of the current vertex to # for printing later
			path.add(0, current.getIndex()); // adds the current index to the start of the ArrayList
			current = current.getParent(); // sets current to the parent of current
		}

		current.setValue("#");
		path.add(0, 0); // adds the entrance to the ArrayList

		return path; // returns the shortest path to the exit
	}

	public void bfs() {
		resetAllVertices();
		boolean atLast = false;
		visitedCells = 0;
		Queue<Vertex> queue = new LinkedList<>(); // initializes the queue
		queue.add(currentLabyrinth[0]); // adds the first vertex to the queue
		int counter = 0;// what step we are on

		while (queue.size() != 0) {

			Vertex u = queue.remove(); // removes the vertex but stores it in u
			visitedCells++;
			u.setViewNumber(counter);

			if (counter == 9)
				counter = 0;
			else
				counter++;

			if (u.getIndex() == currentLabyrinth.length - 1)
				break;

			Iterator<Vertex> iter = u.getEdgeIterator();
			while (iter.hasNext()) {
				Vertex temp = iter.next();
				if (temp.getColor().equals(Color.WHITE)) { // if the color of the vertex is white
					temp.setColor(Color.GRAY); // change the color of the vertex to grey because it has been discovered
					temp.setDistance(u.getDistance() + 1); // set the distance of the current verex to its parents+1
					temp.setParent(u); // sets the parent of the vertex
					queue.add(temp);
				}

			}
			u.setColor(Color.BLACK); // set the color of u to black as all of its edges have been discovered
		}
	}

	public ArrayList<Integer> traceBFSBestPath() {

		bfs(); // this calls BFS alg which will find the distance between 0 and every other
		// connected vertex

		// this will hold an ArrayList of integers that represent the path to take to
		// complete the labyrinth
		ArrayList<Integer> path = new ArrayList<>();
		// Instance of a vertex that keeps track of which vertex we are on
		Vertex current = currentLabyrinth[currentLabyrinth.length - 1];

		// this will loop through all of the parents of the last vertex(the exit) until
		// we hit the entrance
		while (current.getParent() != null) { // exit if the vertex has no parent
			current.setValue("#"); // set the value of the current vertex to # for printing later
			path.add(0, current.getIndex()); // adds the current index to the start of the ArrayList
			current = current.getParent(); // sets current to the parent of current
		}

		current.setValue("#");
		path.add(0, 0); // adds the entrance to the ArrayList

		return path; // returns the shortest path to the exit
	}


	/**
	 * GenerateLabyrinth - this function is used to randomly generate square labyrinth with an ArrayList implementation
	 * of the a labyrinth this function relies on two helper function getPotentialNeighbors and potentially add
	 *
	 * @param length - the length of one side of the square labyrinth
	 */
	public void GenerateLabyrinth(int length) {
		createLabyrinth(length * length);

		// potentialNeighbors - this array holds all vertices ajacent to the currentCell which are not connected to the graph
		ArrayList<Vertex> potentialNeighbors;
		// stack - an arraylist reappropriated to be used a stack holding cells are connected to root
		ArrayList<Vertex> stack = new ArrayList<>();
		// currentCell - the vertex which may be connected to an vertex not currently attached to the graph
		Vertex currentCell = currentLabyrinth[0];
		// newNeighbor - the vertex which has been selected to be connected to current cell
		Vertex newNeighbor;
		// rand - an instance of the Random class used to generate an integer for selecting vertex to be the neighbor of currentCell
		Random rand = new Random(1);
		// visitedCells - the number of cells which have been assigned to currentCell and thus connected to root
		int visitedCells = 1;

		while (visitedCells < length * length) {
			potentialNeighbors = getPotentialNeighbors(length, currentCell.getIndex());
			if (potentialNeighbors.size() > 0) {
				newNeighbor = potentialNeighbors.get(rand.nextInt(potentialNeighbors.size()));
				currentCell.addEdge(newNeighbor);
				newNeighbor.addEdge(currentCell);
				stack.add(currentCell);
				currentCell = newNeighbor;
				visitedCells++;
			} else {
				if (stack.size() > 0) {
					currentCell = stack.get(stack.size() - 1);
					stack.remove(stack.size() - 1);
				}
			}
		}
	}

	public ArrayList<Vertex> getPotentialNeighbors(int length, int index) {
		ArrayList<Vertex> potentialNeighbors = new ArrayList<>();
		//top left
		if (index == 0) {
			potentiallyAdd(currentLabyrinth[index + 1], potentialNeighbors);
			potentiallyAdd(currentLabyrinth[index + length], potentialNeighbors);
		}
		//top right
		else if (index == length - 1) {
			potentiallyAdd(currentLabyrinth[index - 1], potentialNeighbors);
			potentiallyAdd(currentLabyrinth[index + length], potentialNeighbors);
		}
		//top edge
		else if (index < length) {
			potentiallyAdd(currentLabyrinth[index + 1], potentialNeighbors);
			potentiallyAdd(currentLabyrinth[index - 1], potentialNeighbors);
			potentiallyAdd(currentLabyrinth[index + length], potentialNeighbors);
		}
		//left edge
		else if (index % length == 0 && index < length * (length - 1)) {
			potentiallyAdd(currentLabyrinth[index + 1], potentialNeighbors);
			potentiallyAdd(currentLabyrinth[index - length], potentialNeighbors);
			potentiallyAdd(currentLabyrinth[index + length], potentialNeighbors);
		}
		//right edge
		else if (index % length == length - 1 && index < length * (length - 1)) {
			potentiallyAdd(currentLabyrinth[index - 1], potentialNeighbors);
			potentiallyAdd(currentLabyrinth[index - length], potentialNeighbors);
			potentiallyAdd(currentLabyrinth[index + length], potentialNeighbors);
		}
		//bottom left
		else if (index == length * (length - 1)) {
			potentiallyAdd(currentLabyrinth[index + 1], potentialNeighbors);
			potentiallyAdd(currentLabyrinth[index - length], potentialNeighbors);
		}
		//bottom right
		else if (index == length * length - 1) {
			potentiallyAdd(currentLabyrinth[index - 1], potentialNeighbors);
			potentiallyAdd(currentLabyrinth[index - length], potentialNeighbors);
		}
		//bottom edge
		else if (index > length * (length - 1)) {
			potentiallyAdd(currentLabyrinth[index - 1], potentialNeighbors);
			potentiallyAdd(currentLabyrinth[index + 1], potentialNeighbors);
			potentiallyAdd(currentLabyrinth[index - length], potentialNeighbors);
		} else {
			potentiallyAdd(currentLabyrinth[index - 1], potentialNeighbors);
			potentiallyAdd(currentLabyrinth[index + 1], potentialNeighbors);
			potentiallyAdd(currentLabyrinth[index - length], potentialNeighbors);
			potentiallyAdd(currentLabyrinth[index + length], potentialNeighbors);
		}
		return potentialNeighbors;
	}

	public void potentiallyAdd(Vertex cur, ArrayList<Vertex> potentialNeighbors) {
		// iter -  an iterator which has holds the neighbors a cell we would like to potentially connect to currentCell
		Iterator<Vertex> iter = cur.getEdgeIterator();
		if (!(iter.hasNext())) {
			potentialNeighbors.add(cur);
		}
	}

	public void createLabyrinthResultFile(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		//writeFile - where the text should be outputted
		PrintWriter writeFile = new PrintWriter(fileName, "UTF-8");


		printLabyrinthText(true);
		for (String s : textLabyrinth) {
			writeFile.println(s);
		}
		writeFile.println();


		writeFile.println("Test File (" + getLabyrinthLength() + " " + getLabyrinthLength() + ")");
		writeFile.println("Depth First Search");

		ArrayList<Integer> path = traceDFSBestPath();

		writeFile.println("The order by which all of the vertices were found(BFS):");
		printLabyrinthText(true);
		for (String s : textLabyrinth) {
			writeFile.println(s);
		}
		writeFile.println();


		writeFile.println("The best path to finish the maze(DFS):");
		printLabyrinthText(false);
		for (String s : textLabyrinth) {
			writeFile.println(s);
		}
		writeFile.println();


		System.out.print("Path(DFS):");
		for (int i = 0; i < path.size(); i++) {
			System.out.print(path.get(i) + ",");
		}
		System.out.println("\n\n");

		writeFile.println();
		writeFile.println("Length of Path: " + path.size());
		writeFile.println("Visited Cells: " + getVisitedCells());
		writeFile.println();


		writeFile.println("Breadth First Search");
		path = traceBFSBestPath();

		writeFile.println("The order by which all of the vertices were found(BFS):");
		printLabyrinthText(true);
		for (String s : textLabyrinth) {
			writeFile.println(s);
		}
		writeFile.println();

		writeFile.println("The best path to finish the maze(BFS):");
		printLabyrinthText(false);
		for (String s : textLabyrinth) {
			writeFile.println(s);
		}
		writeFile.println();

		System.out.print("Path(BFS): ");
		for (int i = 0; i < path.size(); i++) {
			System.out.print(path.get(i) + ",");
		}
		System.out.println("\n\n");

		writeFile.println();
		writeFile.println("Length of Path: " + path.size());
		writeFile.println("Visited Cells: " + getVisitedCells());
		writeFile.println();
		writeFile.close();
	}
}