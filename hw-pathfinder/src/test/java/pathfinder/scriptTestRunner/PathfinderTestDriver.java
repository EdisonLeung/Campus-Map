/*
 * Copyright (C) 2022 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Winter Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder.scriptTestRunner;

import graph.Graph;
import marvel.MarvelPaths;
import pathfinder.DijkastraAlgorithm;
import pathfinder.datastructures.Path;

import java.io.*;
import java.util.*;

/**
 * This class implements a testing driver which reads test scripts
 * from files for testing MarvelPaths.
 **/
public class PathfinderTestDriver {

    // ***************************
    // ***  JUnit Test Driver  ***
    // ***************************

    /**
     * String -> Graph: maps the names of graphs to the actual graph
     **/
    private final Map<String, Graph<String, Double>> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;

    /**
     * @spec.requires r != null && w != null
     * @spec.effects Creates a new MarvelTestDriver which reads command from
     * {@code r} and writes results to {@code w}
     **/
    // Leave this constructor public
    public PathfinderTestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    /**
     * @throws IOException if the input or output sources encounter an IOException
     * @spec.effects Executes the commands read from the input and writes results to the output
     **/
    // Leave this method public
    public void runTests() throws IOException {
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if(st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while(st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch(command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }

    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        Graph<String, Double> graph = new Graph<>();

        graphs.put(graphName, graph);
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        graphs.get(graphName).addNode(nodeName);
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        double edgeLabel = Double.parseDouble(arguments.get(3));

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         double edgeLabel) {
        graphs.get(graphName).addEdge(parentName, edgeLabel, childName);
        output.println(String.format("added edge %.3f", edgeLabel)+ " from " + parentName + " to " + childName + " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        Set<String> nodes = graphs.get(graphName).getNodes();
        List<String> rval = new ArrayList<>(nodes);
        Collections.sort(rval);
        output.print(graphName + " contains:");
        for (String node : rval) {
            output.print(" " + node);
        }
        output.println();
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        Set<Graph.Edge<Double, String>> edges = graphs.get(graphName).getChildren(parentName);
        List<Graph.Edge<Double, String>> rval = new ArrayList<>(edges);
        Collections.sort(rval, (e1, e2) -> {
            if (e1.getDestination().equals(e2.getDestination())) {
                return (int)Math.signum(e1.getLabel() - e2.getLabel());
            }
            return e1.getDestination().compareTo(e2.getDestination());
        });

        output.print("the children of " + parentName + " in " + graphName + " are:");
        for (Graph.Edge<Double, String> edge: rval) {
            output.print(" " + edge.getDestination() + String.format("(%.3f)", edge.getLabel()));
        }
        output.println();
    }

    private void findPath(List<String> arguments) {
        if (arguments.size() != 3) {
            throw new CommandException("Bad arguments to FindPath: " + arguments);
        }
        String graphName = arguments.get(0);
        String startNode = arguments.get(1);
        String destNode = arguments.get(2);
        findPath(graphName, startNode, destNode);
    }

    private void findPath(String graphName, String startNode, String destNode) {
        if (!graphs.get(graphName).containsNode(startNode)) {
            output.println("unknown: " + startNode);
        }
        if (!graphs.get(graphName).containsNode(destNode)) {
            output.println("unknown: " + destNode);
        }

        if (graphs.get(graphName).containsNode(startNode) && graphs.get(graphName).containsNode(destNode)) {
            output.println("path from " + startNode + " to " + destNode + ":");
            Path<String> shortestPath = DijkastraAlgorithm.findMinCostPath(
                    graphs.get(graphName), startNode, destNode
            );
            if (shortestPath == null) {
                output.println("no path found");
            } else {
                Iterator<Path<String>.Segment> itr = shortestPath.iterator();
                while(itr.hasNext()) {
                    Path<String>.Segment segment = itr.next();
                    output.println(segment.getStart() + " to " + segment.getEnd() + " with weight " + String.format("%.3f", segment.getCost()));
                }
                output.println("total cost: " + String.format("%.3f", shortestPath.getCost()));
            }
        }
    }
    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}

