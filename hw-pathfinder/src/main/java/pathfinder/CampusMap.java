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

package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampusMap implements ModelAPI {
    /**
     * Directed Labeled Graph that represents all the campus routes from each building
     */
    private final Graph<Point, Double> campusPaths;
    /**
     * Map for mapping the campus buildings short names to the actual building object
     */
    private final Map<String, CampusBuilding> buildings;

    private static final boolean DEBUG = false;

    // Abstract Function:
    //      AF(this) = CampusMap campusPaths and buildings where:
    //          campusPath.getNodes() = points from the start and end points from "campus_path.cvs"
    //          campusPath.getEdge(n) = the distance from Point n to all the other Point it connects to
    //              where n is a Point in campusPath.keySet()
    //          buildings.keySet() = the shortNames associated with each building from "campus_buildings.csv"
    //          buildings.get(n) = the building associated with n
    //              where n is a CampusBuilding in buildings.keySet()
    // Representation Invariant:
    //      Every key in campusMap != null &&
    //      Every value associated with campusMap != null &&
    //      Every key in buildings != null &&
    //      Every value associated with buildings != null
    /**
     * Creates a UW CampusMap with the building locations and routes to and from each location
     *
     * @spec.effects constructs a virtual map representation of the UW campus
     */
    public CampusMap() {
        checkRep();

        List<CampusPath> paths = CampusPathsParser.parseCampusPaths("campus_paths.csv");
        List<CampusBuilding> campusBuildings = CampusPathsParser.parseCampusBuildings("campus_buildings.csv");

        buildings = new HashMap<>();
        campusPaths = initializeGraph(paths);

        for (CampusBuilding building : campusBuildings) {
            buildings.put(building.getShortName(), building);
        }
        checkRep();
    }

    // private helper methods that constructs graph from given list of paths
    private Graph<Point, Double> initializeGraph(List<CampusPath> paths) {
        Graph<Point, Double> graph = new Graph<>();
        for (CampusPath path : paths) {
            Point start = new Point(path.getX1(), path.getY1());
            Point end = new Point(path.getX2(), path.getY2());
            if (!graph.containsNode(start)) {
                graph.addNode(start);
            }
            if (!graph.containsNode(end)) {
                graph.addNode(end);
            }
            graph.addEdge(start, path.getDistance(), end);
        }
        return graph;
    }
    // private method to check if rep invariant holds
    private void checkRep() {
        if (DEBUG) {
            for (Point point : campusPaths.getNodes()) {
                assert (point != null);
                for (Graph.Edge<Double, Point> edge : campusPaths.getChildren(point)) {
                    assert (edge != null);
                }
            }

            for (String building : buildings.keySet()) {
                assert (building != null);
                assert (buildings.get(building) != null);
            }
        }
    }

    @Override
    public boolean shortNameExists(String shortName) {
        return buildings.containsKey(shortName);
    }

    @Override
    public String longNameForShort(String shortName) {
        if (!shortNameExists(shortName)) {
            throw new IllegalArgumentException();
        }
        return buildings.get(shortName).getLongName();
    }

    @Override
    public Map<String, String> buildingNames() {
        checkRep();
        Map<String, String> names = new HashMap<>();
        for (String building : buildings.keySet()) {
            names.put(building, buildings.get(building).getLongName());
        }
        checkRep();
        return names;
    }

    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        checkRep();
        if (startShortName == null || endShortName == null) {
            throw new IllegalArgumentException("given param(s) are null");
        } else if (!shortNameExists(startShortName) || !shortNameExists(endShortName)) {
            throw new IllegalArgumentException("graph does not contain given node(s)");
        }

        Point startPoint = new Point(buildings.get(startShortName).getX(), buildings.get(startShortName).getY());
        Point endPoint = new Point(buildings.get(endShortName).getX(), buildings.get(endShortName).getY());
        Path<Point> shortestPath = DijkastraAlgorithm.findMinCostPath(campusPaths, startPoint, endPoint);
        checkRep();
        return shortestPath;
    }

}
