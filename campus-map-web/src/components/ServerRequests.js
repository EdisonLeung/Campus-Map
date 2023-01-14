import {
  MAP_SCALE_FACTOR,
  UW_LATITUDE,
  UW_LATITUDE_OFFSET,
  UW_LATITUDE_SCALE,
  UW_LONGITUDE,
  UW_LONGITUDE_OFFSET,
  UW_LONGITUDE_SCALE,
} from "./Constants";

function xToLon(x) {
  return UW_LONGITUDE + (x - UW_LONGITUDE_OFFSET) * UW_LONGITUDE_SCALE;
}

/**
 * Converts y coordinate to latitude
 */
function yToLat(y) {
  return UW_LATITUDE + (y - UW_LATITUDE_OFFSET) * UW_LATITUDE_SCALE;
}

/*
Finds shortest path from one building to another
*/
export const makeRequestRoute = async (start, end, setLines, setZoom, setCenter) => {
  const paths = [];
  if (start === "" || end === "initEndSelection") {
    console.log("No building selected");
  } else {
    try {
      // let responsePromise = fetch(
      //   "https://spark-heroku-example.herokuapp.com/route?start=" + start + "&end=" + end
      // );
      let responsePromise = fetch("http://localhost:4567/route?start=" + start + "&end=" + end);

      let response = await responsePromise;

      if (!response.ok) {
        alert("invalid input");
        return;
      }

      let object = await response.json();

      let sumLon = 0;
      let sumLat = 0;
      for (let i = 0; i < object.path.length; i++) {
        let path = object.path[i];
        paths.push({
          startPoint: {
            lat: yToLat(path.start.y),
            lng: xToLon(path.start.x),
          },
          endPoint: { lat: yToLat(path.end.y), lng: xToLon(path.end.x) },
        });
        sumLon += xToLon(path.start.x) + xToLon(path.end.x) 
        sumLat += yToLat(path.start.y) + yToLat(path.end.y) 

      }
      const distance = Math.hypot(paths[paths.length-1].endPoint.lat-paths[0].startPoint.lat, paths[paths.length-1].endPoint.lng-paths[0].startPoint.lng)
      setLines(paths);
      setZoom(17);
      // console.log(Math.sqrt(distance * 84463.67103358038))
      // console.log(Math.hypot(paths[paths.length-1].endPoint.lat-paths[0].startPoint.lat, paths[paths.length-1].endPoint.lng-paths[0].startPoint.lng))
      setCenter({lat: sumLat / (paths.length * 2), lng: sumLon / (paths.length * 2)})
    } catch (e) {
      alert("unable to render path");
      console.log(e);
    }
  }
};

export async function initializeBuildingList(setBuildings) {
  const buildingMap = [];
  try {
    let responsePromise = fetch("http://localhost:4567/buildings");
    // let responsePromise = fetch("https://spark-heroku-example.herokuapp.com/buildings");

    let response = await responsePromise;

    let building = await response.json();
    for (let shortName in building) {
      buildingMap.push({
        shortName: shortName,
        longName: building[shortName],
      });
    }
  } catch (e) {
    alert("Campus Map Project Unavaliable");
    console.log(e);
  }
  setBuildings(buildingMap);
}

export async function getBuildingLocation(name, setPoint) {
  try {
    let responsePromise = fetch("http://localhost:4567/building_location?name=" + name);
    // let responsePromise = fetch("https://spark-heroku-example.herokuapp.com/building_location?name=" + name)
    let response = await responsePromise;

    let buildingLocation = await response.json();
    
    setPoint({ lat: yToLat(buildingLocation.y), lng: xToLon(buildingLocation.x) })
  } catch(e) {
    alert("Unable to retrieve building location")
  }
}