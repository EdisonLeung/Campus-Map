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

package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.CampusMap;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.ArrayList;
import java.util.TreeMap;

public class SparkServer {

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();

        CampusMap campusMap = new CampusMap();

        Spark.get("/buildings", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                Gson gson = new Gson();
                return gson.toJson(new TreeMap<>(campusMap.buildingNames()));
            }
        });

        Spark.get("/route", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String start = request.queryParams("start");
                String end = request.queryParams("end");

                if (start == null || end == null) {
                    Spark.halt(400, "start or end node is null");
                }

                if (campusMap.shortNameExists(start) && campusMap.shortNameExists(end)) {
                    Path<Point> path = campusMap.findShortestPath(start, end);
                    Gson gson = new Gson();
                    return gson.toJson(path);
                } else {
                    Spark.halt(400, "must be a valid building name");
                }
                return null;
            }
        });
    }

}
