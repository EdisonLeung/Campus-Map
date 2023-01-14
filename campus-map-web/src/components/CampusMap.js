import {
  GoogleMap,
  useLoadScript,
  Polyline,
  Marker,
} from "@react-google-maps/api";
import React, { Component, useEffect, useState } from "react";
import { UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER } from "./Constants";

import Select from "react-select";
import {
  getBuildingLocation,
  initializeBuildingList,
  makeRequestRoute,
} from "./ServerRequests";
import Footer from "./Footer";

export default class CampusMap extends Component {
  render() {
    let resumeData = this.props.resumeData;

    return (
      <div className="App">
        <Header />
        <section id="project-read">
          <div style={{ height: "10vh" }} />
          <div className="row">
            <h1>UW Campus Map (Project Overview)</h1>
            <h6>
              Designed a web program for users to be able to find the shortest
              path between two buildings at the University of Washington. By
              selecting from the dropdown menu or using the search bar at the
              top right of the map, users can select a start location and
              desitnation location. The program will then automatically generate
              the shortest path between the two locations. A demonstration of
              the Campus Map can be seen below in the video. Feel free to
              checkout the project at the github repository{" "}
              <a
                href="https://github.com/EdisonLeung/Campus-Map"
                target="_blank"
                rel="noopener noreferrer"
              >
                here
              </a>
            </h6>
            <h1>Tools Used</h1>
            <ul className="skills">
              <li>React</li>
              <li>Java</li>
              <li>Typescript</li>
              <li>Node.js</li>
              <li>Spark Framework</li>
              <li>JUnit Testing</li>
              <li>Google API</li>
            </ul>
            <img
              src="images/UW_Logo.png"
              alt=""
              className="center header-offset"
            />
          </div>
        </section>
        <Body />
        <section id="project-read">
          <div className="row">
            <h1>Project Development</h1>
            <h6>
              Built the project using React.js for simplicty and component based
              development. The backend uses a Spark Framework Java server to
              communicate between the Java program that handles the logic and
              the frontend UI.
            </h6>
            <br />
            <h6>
              For the Java Program, I created a Generic GraphADT where the nodes
              represented various points on campus and the edges represented the
              distance between each point. This allowed me to represent the
              roads and buildings on campus. Using Dijkstra's Algorithm, I was
              able to find the shortest path from two given points
            </h6>
            <br />
            <h6>
              For the Map itself, I used the Google Maps API to get an accurate
              map of the University.
            </h6>
            <br />
            <h6>
              {" "}
              To ensure code stability, I implemented JUnit testing to ensure
              the functionality of the GraphADT and Typescript for
              type-checking.
            </h6>
            <br />
          </div>
        </section>
        <div style={{ backgroundColor: "black" }}>
          <Footer resumeData={resumeData} />
        </div>
      </div>
    );
  }
}

const Body = () => {
  const { isLoaded } = useLoadScript({
    googleMapsApiKey: process.env.REACT_APP_PUBLIC_GOOGLE_MAPS_API_KEY,
  });

  if (!isLoaded) return <div>Loading...</div>;
  return <Map />;
};

function Map() {
  const initial_center = { lat: UW_LATITUDE_CENTER, lng: UW_LONGITUDE_CENTER };

  const [center, setCenter] = useState(initial_center);
  const [zoom, setZoom] = useState(16);
  const [lines, setLines] = useState([]);

  const [buildings, setBuildings] = useState([]);
  const [start, setStart] = useState("");
  const [end, setEnd] = useState("");

  const [startPoint, setStartPoint] = useState({});
  const [endPoint, setEndPoint] = useState({});

  useEffect(() => {
    window.scrollTo(0, 250);
    initializeBuildingList(setBuildings);
  }, []);

  return (
    <div id="map" className="row center-box">
      <GoogleMap
        zoom={zoom}
        center={center}
        mapContainerClassName="column"
        mapContainerStyle={{ width: "100%" }}
      >
        {lines.map((line, index) => (
          <Polyline
            key={index}
            path={[line.startPoint, line.endPoint]}
            options={{
              strokeColor: "#ff2527",
              strokeOpacity: 0.75,
              strokeWeight: 2,
            }}
          />
        ))}
        {start !== "" && (
          <Marker
            position={{
              lat: parseFloat(startPoint.lat),
              lng: parseFloat(startPoint.lng),
            }}
            icon={{
              url: "http://maps.google.com/mapfiles/ms/icons/red-dot.png",
            }}
          ></Marker>
        )}
        {end !== "" && (
          <Marker
            position={{
              lat: parseFloat(endPoint.lat),
              lng: parseFloat(endPoint.lng),
            }}
            icon={{
              url: "http://maps.google.com/mapfiles/ms/icons/green-dot.png",
            }}
          ></Marker>
        )}
      </GoogleMap>
      <div className="overlay-box">
        <h4>Find Path Between Buildings</h4>

        <form role="search">
          <Select
            id="search"
            placeholder="Search Start Building..."
            options={buildings.map((opt) => ({
              label: opt.longName + " - " + opt.shortName,
              value: opt.shortName,
            }))}
            onChange={(opt) => {
              setStart(opt.value);
              getBuildingLocation(opt.value, setStartPoint);
              if (end !== "")
                makeRequestRoute(opt.value, end, setLines, setZoom, setCenter);
            }}
            required
          />
        </form>

        <form role="search">
          <Select
            id="search"
            placeholder="Search Destination Building..."
            options={buildings.map((opt) => ({
              label: opt.longName + " - " + opt.shortName,
              value: opt.shortName,
            }))}
            onChange={(opt) => {
              setEnd(opt.value);
              getBuildingLocation(opt.value, setEndPoint);
              if (start !== "")
                makeRequestRoute(
                  start,
                  opt.value,
                  setLines,
                  setZoom,
                  setCenter
                );
            }}
            required
          />
        </form>
      </div>
    </div>
  );
}

const Header = () => {
  return (
    <React.Fragment>
      <div id="home">
        <nav id="nav-wrap">
          <a className="mobile-btn" href="#nav-wrap" title="Show navigation">
            Show navigation
          </a>
          <a className="mobile-btn" href="#" title="Hide navigation">
            Hide navigation
          </a>
          <ul id="nav" className="nav">
            <li>
              <a href="https://main.d3uqk9wtm98zyo.amplifyapp.com/#home">Home</a>
            </li>
            <li>
              <a href="https://main.d3uqk9wtm98zyo.amplifyapp.com/#about">About</a>
            </li>
            <li>
              <a href="https://main.d3uqk9wtm98zyo.amplifyapp.com/#resume">Resume</a>
            </li>
            <li>
              <a href="https://main.d3uqk9wtm98zyo.amplifyapp.com/#portfolio">Projects</a>
            </li>
            {/* <li>
              <a href="/#contact">
                Contact
              </a>
            </li> */}
          </ul>
        </nav>
      </div>
    </React.Fragment>
  );
};
