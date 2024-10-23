import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GridParser {
    private List<List<GridState>> gridStates;

    public GridParser(String filepath) {
        gridStates = new ArrayList<>();
        String json = "";

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                json += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove leading/trailing square brackets
        json = json.substring(1, json.length() - 1).trim();

        int rowIndex = 0;
        String[] rows = json.split("],");
        for (String row : rows) {
            List<GridState> gridRow = new ArrayList<>();
            // Remove square brackets from row (leading \\ indicates a literal string)
            row = row.replaceAll("[\\[\\]]", "").trim();
            
            // Split objects in row by comma
            String[] objects = row.split("},");
            
            int colIndex = 0;
            for (String object : objects) {
                // Cell object is a null/wall state (level 0)
                if (!object.contains(":")) {
                    gridRow.add(new GridState(' ', rowIndex, colIndex, 0, false));
                    continue;
                }

                // Remove curly braces and split object into each key:value string pair
                object = object.replaceAll("[\\{\\}]", "").trim();
                String[] fields = object.split(",");

                char name = ' ';
                int level = 0;
                boolean isGoal = false;

                for (String field : fields) {
                    // Split and store key:value
                    String[] keyValue = field.split(":");
                    String key = keyValue[0].trim().replaceAll("\"", "");
                    String value = keyValue[1].trim().replaceAll("\"", "");

                    // Switch case for each GridState field
                    switch (key) {
                        case "value":
                            name = value.charAt(value.length() - 1);
                            break;
                        case "level":
                            level = Integer.parseInt(value);
                            break;
                        case "goal":
                            isGoal = Boolean.parseBoolean(value);
                            break;
                    }
                }
                // rowIndex & colIndex are implicit from Grid[row][col] as formatting stores null states
                gridRow.add(new GridState(name, rowIndex, colIndex, level, isGoal));
                colIndex ++;
            }
            gridStates.add(gridRow);
            rowIndex ++;
        }
    }

    public List<List<GridState>> getStates() {
        return gridStates;
    }
}
