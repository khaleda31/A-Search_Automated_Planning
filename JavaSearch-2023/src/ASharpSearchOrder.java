import uk.ac.hw.macs.search.ChildWithCost;
import uk.ac.hw.macs.search.SearchOrder;
import uk.ac.hw.macs.search.FringeNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * An A* search order that adds all nodes to the start of the fringe.
 */
public class ASharpSearchOrder implements SearchOrder {

	// Returns the sorted children in descending order by f(n) => g(n) + h(n) 
	private List<ChildWithCost> getSortedChildren(Set<ChildWithCost> children) {
		List<ChildWithCost> sortedChildren = new ArrayList<>(children);
		sortedChildren.sort(Comparator.comparingInt(
            child -> child.cost + child.node.getValue().getHeuristic()
        ));
		return sortedChildren.reversed();
	}

	@Override
	public void addToFringe(List<FringeNode> frontier, FringeNode parent, Set<ChildWithCost> children) {
		// Iterate over each of the sorted SearchNode's children list
		for (ChildWithCost child : getSortedChildren(children)) {
			boolean stateInFrontier = false;
			// Sets stateInFrontier = true if state is in frontier
			for (FringeNode fNode: frontier) {
				if (fNode.node.getValue() == child.node.getValue()) {
					stateInFrontier = true;
					break;
				}
			}
			// Add the state to frontier if is not already in there
			if (!stateInFrontier) {
				frontier.add(0, new FringeNode(child.node, parent, child.cost));
			}
		}
	}
}
