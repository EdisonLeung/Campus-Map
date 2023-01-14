import pathfinder.CampusMap;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;

import com.google.gson.Gson;

import campuspaths.utils.CORSFilter;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.TreeMap;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        // port(getHerokuAssignedPort());
//        CampusMap campusMap = new CampusMap();
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();

        get("/buildings", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                Gson gson = new Gson();
                CampusMap campusMap = new CampusMap();
                return gson.toJson(new TreeMap<>(campusMap.buildingNames()));
            }
        });

        get("/route", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                CampusMap campusMap = new CampusMap();

                String start = request.queryParams("start");
                String end = request.queryParams("end");

                if (start == null || end == null) {
                    halt(400, "start or end node is null");
                }

                if (campusMap.shortNameExists(start) && campusMap.shortNameExists(end)) {
                    Path<Point> path = campusMap.findShortestPath(start, end);
                    Gson gson = new Gson();
                    return gson.toJson(path);
                } else {
                    halt(400, "must be a valid building name");
                }
                return null;
            }
        });
        get("/building_location", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                CampusMap campusMap = new CampusMap();

                String name = request.queryParams("name");

                if (name == null) {
                    halt("name is null");
                }

                if (campusMap.shortNameExists(name)) {
                    Point point = campusMap.getBuildingLocation(name);
                    Gson gson = new Gson();
                    return gson.toJson(point);
                } else {
                    halt("Must be valid building name");
                }
                return null;
            }
        });
    }

    // static int getHerokuAssignedPort() {
    //     ProcessBuilder processBuilder = new ProcessBuilder();
    //     if (processBuilder.environment().get("PORT") != null) {
    //         return Integer.parseInt(processBuilder.environment().get("PORT"));
    //     }
    //     return 4567;
    // }

}
