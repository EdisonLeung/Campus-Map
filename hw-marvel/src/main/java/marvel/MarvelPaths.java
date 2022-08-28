package marvel;

import graph.Graph;

import java.io.File;
import java.util.*;

public class MarvelPaths {
    // Main method to run run Marvel Paths
    public static void main(String[] args) {
        Graph<String, String> graph;
        System.out.println("*====================================*\n" +
                           "| WELCOME TO MARVEL BFS PATH FINDING |\n" +
                           "*====================================*\n");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Files to create Directed Labeled Graphs from:\n" +
                           "---------------------------------------------");
        List<String> fileNames = new ArrayList<>();
        File[] folder = new File("C:\\Users\\ediso\\IdeaProjects\\cse331-22wi-edleung\\hw-marvel\\" +
                "src\\main\\resources\\data").listFiles();
        for (File file : folder) {
            fileNames.add(file.getName());
            System.out.println(file.getName());
        }

        while (true) {
            System.out.print("What file would you like to import into your graph?: ");
            String fileName = scanner.nextLine();
            if (fileNames.contains(fileName)) {
                graph = createGraph(fileName);
                System.out.println("Created graph from " + fileName + "\n");
                System.out.println("Nodes in graph: " + graph.getNodes());
                break;
            } else {
                System.out.println("That is not a valid input. Try again. ");
            }
        }

        boolean keepGoing = true;
        while(keepGoing) {
            System.out.print("What would you like to do next (ListNodes, Pathfinding, Exit): ");
            String input = scanner.nextLine();
            switch (input){
                case "ListNodes":
                    for (String node : graph.getNodes()) {
                        System.out.println(node);
                    }
                    break;
                case "Pathfinding":

                    String startNode = getNode(scanner, graph, "start");
                    String endNode = getNode(scanner, graph, "destination");

                    System.out.println();
                    List<Graph.Edge<String, String>> path = findPath(graph, startNode, endNode);
                    if (path == null) {
                        System.out.println("No Path Found");
                    } else {
                        System.out.println("The shortest path from " + startNode + " to "
                                + endNode + " is:");
                        String start = startNode;
                        for (Graph.Edge<String, String> edge : path) {
                            System.out.println(start + " to " + edge.getDestination() + " via "
                                    + edge.getLabel());
                            start = edge.getDestination();
                        }
                    }
                    break;
                case "Exit":
                    keepGoing = false;
                    break;
                default:
                    System.out.println("That was not a valid input.");
            }
        }
        System.out.println("THANK YOU FOR PARTICIPATING");
    }

    // Helper method to get user input for a start/destination node
    private static String getNode(Scanner scanner, Graph<String, String> graph, String type) {
        String node;
        while (true) {
            System.out.print("What is the " + type + " node?: ");
            node = scanner.nextLine();
            if (graph.containsNode(node)) {
                return node;
            } else {
                System.out.println("Graph does not contain given node.");
            }
        }
    }

    /**
     * Creates a directed label graph with the information given
     *
     * @param fileName the csv file to import into the graph
     * @return a directed labeled graph with the give data in fileName
     * @spec.requires fileName != null, fileName must have .cvs at the end,
     * the cvs file with the given fileName must be in the proper cvs format
     */
    public static Graph<String, String> createGraph(String fileName) {
        Map<String, Set<String>> graphData = MarvelParser.parseData(fileName);

        Graph<String, String> graph = new Graph<>();
        for (String edgeLabel : graphData.keySet()) {
            for (String character : graphData.get(edgeLabel)) {
                if (!graph.containsNode(character)) { graph.addNode(character); }
                if (graphData.get(edgeLabel).size() > 1) {
                    for (String des : graphData.get(edgeLabel)) {
                        if (!des.equals(character)) {
                            if (!graph.containsNode(des)) {
                                graph.addNode(des);
                            }
                            graph.addEdge(character, edgeLabel, des);
                        }
                    }
                }
            }
        }
        return graph;
    }

    /**
     * Gets the shortest path from one node to another
     *
     * @param graph the graph to perform the pathfinding search on
     * @param startNode the start node
     * @param destNode the destination node
     * @return a List with the nodes and edges that have the shortest path from
     * startNode to destNode, returns null if no path found
     * @spec.requires graph != null, graph must contain given startNode and destNode
     */
    public static List<Graph.Edge<String, String>> findPath(Graph<String, String> graph, String startNode, String destNode) {
        Queue<String> nodesToVisit = new LinkedList<>();
        Map<String, List<Graph.Edge<String, String>>> visitedNodes = new HashMap<>();

        nodesToVisit.add(startNode);
        visitedNodes.put(startNode, new ArrayList<>());
        while (!nodesToVisit.isEmpty()) {
            String node = nodesToVisit.remove();
            if (node.equals(destNode)) {
                return visitedNodes.get(node);
            }

            // sort edges so it will find the path which is lexicographically least
            List<Graph.Edge<String, String>> sortedEdges = new ArrayList<>(graph.getChildren(node));
            Collections.sort(sortedEdges, (e1, e2) -> {
                if (e1.getDestination().equals(e2.getDestination())) {
                    return e1.getLabel().compareTo(e2.getLabel());
                }
                return e1.getDestination().compareTo(e2.getDestination());
            });

            for (Graph.Edge<String, String> edge : sortedEdges) {
                if (!visitedNodes.containsKey(edge.getDestination())) {
                    // Creates a path with the edge associated to it
                    List<Graph.Edge<String, String>> pathPost = new ArrayList<>(visitedNodes.get(node));
                    pathPost.add(edge);

                    // adds the created path to the visitedNodes in order to track the paths
                    visitedNodes.put(edge.getDestination(), pathPost);
                    nodesToVisit.add(edge.getDestination());
                }
            }
        }

        return null;
    }

}
