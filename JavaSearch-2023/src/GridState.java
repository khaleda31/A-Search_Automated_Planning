import uk.ac.hw.macs.search.State;

/**
 * A state representing a single node in a grid.
 */
public class GridState implements State {
	
	// Represents the coordinates of the goal state
	private static int goalX, goalY;

	// Represents the coordinates of this state instance
	private int stateX, stateY;
	private boolean goal;
	private char value;
	private int level;
	
	public GridState (char value, int x, int y, int level) {
		this(value, x, y, level, false);
	}
	
	public GridState (char value,  int x, int y, int level, boolean goal) {
		this.stateX = x;
		this.stateY = y;
		this.value = value;
		this.level = level;
		this.goal = goal;
	}

	// Function to set the goal state coordinates
	public static void setGoalCoordinates(int x, int y) {
		goalX = x;
		goalY = y;
	}

	@Override
	// Function to check if this state is the goal state
	public boolean isGoal() {
		return this.goal;
		// Can also be:
		// return (this.stateX == goalX && this.stateY == goalY);
	}
	
	@Override
	// Calculates the Manhattan distance heuristic using absolute difference in x and y coords
	public int getHeuristic() {
		return Math.abs(this.stateX - goalX) + Math.abs(this.stateY - goalY);
	}

	@Override
	public String toString() {
		return "GridState [value=" + value + ", goal=" + goal + "]";
	}

	// Getters & Setters
	// Returns this instance value - used in GridEditor
	public char getValue() {
		return this.value;
	}

	// Sets this instance value - used in GridEditor
	public void setValue(char value) {
		this.value = value;
	}

	// Returns this instance level - used in GridEditor
	public int getLevel() {
		return this.level;
	}

	// Sets this instance level - used in GridEditor
	public void setLevel(int level) {
		this.level = level;
	}
	
}
