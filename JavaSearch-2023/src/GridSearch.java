import uk.ac.hw.macs.search.SearchProblem;
import uk.ac.hw.macs.search.SearchOrder;
import uk.ac.hw.macs.search.Node;

import java.io.File;
import javax.swing.JFileChooser;


public class GridSearch {
	public static void main(String[] args) {
		JFileChooser filePicker = new JFileChooser();
		File currentDir = new File(System.getProperty("user.dir"));
		filePicker.setCurrentDirectory(currentDir);
		int userSelection = filePicker.showOpenDialog(null);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToOpen = filePicker.getSelectedFile();
			GridParser parser = new GridParser(fileToOpen.getAbsolutePath());
			Grid grid = new Grid(parser.getStates());
			Node root = grid.createConnections();
	
			// Run the search
			SearchOrder order = new ASharpSearchOrder();
			SearchProblem problem = new SearchProblem(order);
			problem.doSearch(root);
		}
	}

}
