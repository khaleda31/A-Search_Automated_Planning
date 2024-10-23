import java.util.ArrayList;
import java.util.List;

import uk.ac.hw.macs.search.Node;

public class Grid {
    
    private static List<List<GridState>> gridStates = new ArrayList<>();
    List<List<Node>> gridNodes;
    public int rows;
    public int cols;

    public Grid (int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        for (int row = 0; row < rows; row++) {
            List<GridState> rowStates = new ArrayList<>();
            for (int col = 0; col < cols; col++) {
                rowStates.add(new GridState(' ', row, col, 0));
            }
            gridStates.add(rowStates);
        }
	}

    public Grid (List<List<GridState>> gridStates) {
        Grid.gridStates = gridStates;
        this.rows = gridStates.size();
        this.cols = gridStates.get(0).size();
    }

    public void initialseGrid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        gridStates.clear();
        for (int row = 0; row < rows; row++) {
            List<GridState> rowStates = new ArrayList<>();
            for (int col = 0; col < cols; col++) {
                rowStates.add(new GridState(' ', row, col, 0));
            }
            gridStates.add(rowStates);
        }
    }

    public GridState getState(int row, int col) {
        return gridStates.get(row).get(col);
    }

    // Returns the Json formatted 2D grid object
    public String getGridJson() {
        String gridJson = "[\n";
        for (int row = 0; row < this.rows; row++) {
            gridJson += "\t[\n";
            for (int col = 0; col < cols; col++) {
                // Append state object data
                GridState state = this.getState(row, col);
                if (state.getLevel() != 0) {
                    gridJson += "\t\t{\n";
                    gridJson += "\t\t\t\"value\": \"" + state.getValue() + "\",\n";
                    gridJson += "\t\t\t\"level\": " + state.getLevel() + ",\n";
                    gridJson += "\t\t\t\"goal\": " + (state.getValue() == 'G') + "\n";
                    gridJson += "\t\t}";
                } else gridJson += "\t\t{}";
                // Trailing comma formatting for object
                if (col < cols - 1) {
                    gridJson += ",\n";
                } else gridJson += "\n";
            }
            // Trailing comma formatting for row
            if (row < rows - 1) {
                gridJson += "\t],\n";
            } else gridJson += "\t]\n";
        }
        gridJson += "]";
        return gridJson;
    }

    // Set the grid data to the data of an existing grid
    public void updateGrid(List<List<GridState>> newGridStates) {
        gridStates.clear();
        gridStates = newGridStates;
        rows = gridStates.size();
        cols = gridStates.get(0).size();
    }

    private List<int[]> getValidPairIndices(int row, int col) {
        List<Integer> rowValues = new ArrayList<>();
        rowValues.add(row);
        if (row < this.rows - 1) rowValues.add(row+1);
        if (row > 0) rowValues.add(row-1);
        
        List<Integer> colValues = new ArrayList<>();
        colValues.add(col);
        if (col < this.cols - 1) colValues.add(col+1);
        if (col > 0) colValues.add(col-1);
        
        List<int[]> pairs = new ArrayList<>();
        for (Integer x: rowValues) {
            for (Integer y: colValues) {
                if ((Math.abs(row - x) + Math.abs(col-y)) == 1) {
                    pairs.add(new int[]{x,y});
                }
            }
        }
        return pairs;
    }

    // Populate gridNodes
    private void setGridNodes() {
        gridNodes = new ArrayList<>();
        for (int row = 0; row < this.rows; row++) {
            List<Node> rowNodes = new ArrayList<>();
            for (int col = 0; col < this.cols; col++) {
                GridState state = this.getState(row,col);
                rowNodes.add(new Node(state));
            }
            gridNodes.add(rowNodes);
        }
    }
    
    public Node createConnections() {
        Node root = null;
        this.setGridNodes();
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                GridState state = this.getState(row,col);
                Node node = gridNodes.get(row).get(col);
                if (state.getLevel() == 0) continue;
                else if (state.isGoal()) {
                    GridState.setGoalCoordinates(row, col);
                } 
                else if (state.getValue() == 'S') root = node;

                List<int[]> pairs = getValidPairIndices(row, col);
                for (int[] pair: pairs) {
                    GridState connectedState = this.getState(pair[0],pair[1]);
                    int cost = connectedState.getLevel();
                    if (cost == 0) {
                        continue;
                    }

                    Node connectedNode = gridNodes.get(pair[0]).get(pair[1]);
                    node.addChild(connectedNode, cost);
                }
            }
        }
        return root;
    }

}
