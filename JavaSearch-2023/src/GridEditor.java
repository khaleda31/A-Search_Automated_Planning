import java.awt.*;
import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionListener;

public class GridEditor {
    // Colours
    private static final Color EMPTY_COLOR = Color.BLACK;
    private static final Color LEVEL1_COLOR = Color.WHITE;
    private static final Color LEVEL2_COLOR = Color.LIGHT_GRAY;
    private static final Font FONT = new Font("Arial", Font.BOLD, 20);

    // Variables for grid size
    private static int rows = 5;
    private static int cols = 5;
    private static boolean textMode = false;

    private static Grid grid;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Grid Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFrameSize(frame, rows, cols);
        grid = new Grid(rows, cols);

        // Main panel to hold the grid and controls
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel gridPanel = new JPanel(new GridLayout(rows, cols));
        addButtonsToGrid(gridPanel, rows, cols);

        // File panel for saving/opening files
        JPanel filePanel = new JPanel();
        JButton saveButton = new JButton("Save Grid as File");
        JButton openButton = new JButton("Open Grid From File");
        JButton switchMode = new JButton("Toggle Edit Names");
        filePanel.add(saveButton);
        filePanel.add(openButton);
        filePanel.add(switchMode);
        
        // Control panel for rows and columns input
        JPanel controlPanel = new JPanel();
        JTextField rowField = new JTextField(3);
        JTextField colField = new JTextField(3);
        rowField.setText(String.valueOf(rows));
        colField.setText(String.valueOf(cols));
        JButton updateButton = new JButton("Refresh Grid");

        // Add components to control panel
        controlPanel.add(new JLabel("Rows: "));
        controlPanel.add(rowField);
        controlPanel.add(new JLabel("Columns: "));
        controlPanel.add(colField);
        controlPanel.add(updateButton);

        // ActionListener for clicking save file button
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser filePicker = new JFileChooser();
                File currentDir = new File(System.getProperty("user.dir"));
		        filePicker.setCurrentDirectory(currentDir);
                int userSelection = filePicker.showSaveDialog(frame);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = filePicker.getSelectedFile();
                    saveGridToFile(fileToSave.getAbsolutePath() + ".json");
                }
            }
        });
        
        // ActionListener for clicking open file button
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser filePicker = new JFileChooser();
                File currentDir = new File(System.getProperty("user.dir"));
		        filePicker.setCurrentDirectory(currentDir);
                int userSelection = filePicker.showOpenDialog(frame);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToOpen = filePicker.getSelectedFile();
                    GridParser parser = new GridParser(fileToOpen.getAbsolutePath());
                    grid.updateGrid(parser.getStates());
                    rows = grid.rows;
                    cols = grid.cols;
                    rowField.setText(String.valueOf(rows));
                    colField.setText(String.valueOf(cols));
                    refreshGrid(frame, gridPanel, rows, cols);
                }
            }
        });

        // ActionListener for clicking switch refresh grid button
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateGrid(frame, gridPanel, rowField, colField);
            }
        });
        
        // ActionListener for clicking toggle edit mode button
        switchMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textMode = !textMode;
                if (textMode) switchMode.setText("Toggle Edit Levels");
                else switchMode.setText("Toggle Edit Names");
                updateGrid(frame, gridPanel, rowField, colField);
            }
        });

        // Add the file, grid and control panels to the main panel
        mainPanel.add(filePanel, BorderLayout.NORTH);
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // Update grid data
    private static void updateGrid(JFrame frame, JPanel gridPanel, JTextField rowField, JTextField colField) {
        try {
            int newRows = Integer.parseInt(rowField.getText());
            int newCols = Integer.parseInt(colField.getText());
            
            if (rows != newRows || cols != newCols) {
                grid.initialseGrid(newRows, newCols);
                rows = newRows;
                cols = newCols;
            }

            refreshGrid(frame, gridPanel, rows, cols);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter integers.");
        }
    }

    // Refresh the grid panel
    private static void refreshGrid(JFrame frame, JPanel gridPanel, int rows, int cols) {
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(rows, cols));
        
        if (textMode) {
            addTextBoxToGrid(gridPanel, rows, cols);
        } else {
            addButtonsToGrid(gridPanel, rows, cols);
        }

        setFrameSize(frame, rows, cols);

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private static void setCellColour(JComponent item, int row, int col, int cellLevel) {
        switch (cellLevel) {
            case 0:
                // Set colour to BLACK and clear cell text + value
                item.setBackground(EMPTY_COLOR);
                if (item instanceof JButton) {
                    ((JButton) item).setText("");
                } else {
                    ((JTextField) item).setText("");
                }
                grid.getState(row, col).setValue(' ');
                break;
            case 1:
                // Set colour to WHITE and set cell value
                item.setBackground(LEVEL1_COLOR);
                setCellText(item, row, col);
                break;
            case 2:
                // Set colour to GREY and set cell value
                item.setBackground(LEVEL2_COLOR);
                setCellText(item, row, col);
                break;
        }
    }

    // Set text value of the component to the corresponding grid cell value
    private static void setCellText(JComponent item, int row, int col) {
        String cellValue = String.valueOf(grid.getState(row, col).getValue());
        if (item instanceof JButton) {
            ((JButton) item).setText(cellValue);
            if (cellValue == "S" || cellValue == "G") {
                item.setForeground(Color.RED);
            }
        } else {
            ((JTextField) item).setText(cellValue);
        }
    }

    // Method to add toggleable buttons to the grid panel
    private static void addButtonsToGrid(JPanel gridPanel, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JButton button = new JButton();
                button.setFont(FONT);
                
                final int rowIndex = i;
                final int colIndex = j;
                final int cellLevel = grid.getState(rowIndex, colIndex).getLevel();
                setCellColour(button, rowIndex, colIndex, cellLevel);
                
                // Action listener to toggle between three states
                button.addActionListener(new ActionListener() {
                    int row = rowIndex;
                    int col = colIndex;
                    int level = cellLevel;
                    public void actionPerformed(ActionEvent e) {
                        // Cycle between 0, 1, 2
                        level = (level + 1) % 3;
                        grid.getState(row, col).setLevel(level);
                        setCellColour(button, row, col, level);
                    }
                });
                
                gridPanel.add(button);
            }
        }
    }
    
    // Method to add text fields into the grid panel
    private static void addTextBoxToGrid(JPanel gridPanel, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int cellLevel = grid.getState(i, j).getLevel();
                
                JTextField cell = new JTextField();
                cell.setFont(FONT);
                
                setCellColour(cell, i, j, cellLevel);
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setForeground(Color.BLACK);
                
                // Disable cell if cell is empty
                if (cellLevel == 0) {
                    cell.setText("");
                    cell.setEnabled(false);
                }

                final int row = i;
                final int col = j;
                cell.addFocusListener(new FocusListener() {
                    // When to cell loses focus
                    public void focusLost(FocusEvent e) {
                        // Set cell value to last character index
                        String cellValue = cell.getText().toUpperCase();
                        int lastIndex = cellValue.length() - 1;
                        char newValue = cellValue.charAt(lastIndex);
                        cellValue = String.valueOf(newValue);
                        cell.setText(cellValue.toUpperCase());
                        grid.getState(row, col).setValue(newValue);

                        // Indicate if cell is root or goal state
                        if (newValue == 'S' || newValue == 'G') {
                            cell.setForeground(Color.RED);
                        } 
                    }

                    // Required for a FocusListener
                    public void focusGained(FocusEvent e) {}
                });

                gridPanel.add(cell);
            }
        }
    }

    // Change the size of the JFrame according to grid dimensions
    private static void setFrameSize(JFrame frame, int rows, int cols) {
        int width = cols * 110; 
        int height = (rows * 110) + 20; 
        frame.setSize(width, height);
    }

    // Save <filename>.json to device file system
    private static void saveGridToFile(String filepath) {
        String gridJson = grid.getGridJson();
        try (FileWriter writer = new FileWriter(filepath)) {
            writer.write(gridJson);
            System.out.println("File saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
