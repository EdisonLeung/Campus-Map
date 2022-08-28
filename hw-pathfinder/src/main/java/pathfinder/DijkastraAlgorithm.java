package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;

import java.util.*;
public class DijkastraAlgorithm {

    /**
     *
     * finds the minimum cost path in a graph
     *
     * @param graph the graph to perform the search on
     * @param startNode the starting node for the search
     * @param endNode the destination node for the search
     * @param <E> the node Data Type
     * @return the minimum cost path from start node to end node, returns null if no path found
     * @spec.requires graph != null, startNode != null, endNode != null, graph must contain
     * startNode and endNode
     */
    public static <E> Path<E> findMinCostPath(Graph<E, Double> graph, E startNode, E endNode) {
        E start = startNode;
        E end = endNode;
        Queue<Path<E>> active = new PriorityQueue<>((o1, o2) -> (int)Math.signum(o1.getCost() - o2.getCost()));
        Set<E> finished = new HashSet<>();
        active.add(new Path<>(start));

        while (!active.isEmpty()) {
            // gets the current min path
            Path<E> minPath = active.poll();
            E minDest = minPath.getEnd();

            if (minDest.equals(end)){
                return minPath;
            }
            if (finished.contains(minDest)) {
                continue;
            }

            for (Graph.Edge<Double, E> child : graph.getChildren(minDest)) {
                // If we don't know the minimum-cost path from start to child,
                // examine the path we've just found
                if (!finished.contains(child)) {
                    Path<E> newPath = minPath.extend(child.getDestination(), child.getLabel());
                    active.add(newPath);
                }
            }
            finished.add(minDest);
        }
        // If the loop terminates, then no path exists from start to end.
        return null;
    }
}
