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

import {LatLngExpression} from "leaflet";
import React, { Component } from "react";
import {MapContainer, TileLayer} from "react-leaflet";
import "leaflet/dist/leaflet.css";
import { UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER } from "./Constants";
import {Path} from "./types";
import MapLine from "./MapLine";
import LineMarker from "./LineMarker";

// This defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];

interface MapProps {
    edgeList: Path[]
}

interface MapState {
}

class Map extends Component<MapProps, MapState> {
    renderLines() {
        let lines = [];
        for (let i = 0; i < this.props.edgeList.length; i++) {
            const edge: Path = this.props.edgeList[i];
            lines[i] = <MapLine key={edge.key} color={edge.color} x1={edge.x1} y1={edge.y1} x2={edge.x2} y2={edge.y2}/>
        }
        return lines;
    }
    renderMarkers() {
        let paths: Path[] = this.props.edgeList;
        let rval = [];
        if (paths.length > 0) {
            rval.push(<LineMarker x={paths[0].x1} y={paths[0].y1} popupMsg={`start`}/>);
            rval.push(<LineMarker x={paths[paths.length-1].x2} y={paths[paths.length-1].y2} popupMsg={`destination`}/>);
        }
        return rval;
    }
    render() {
        return (
            <div id="map">
                <MapContainer
                    center={position}
                    zoom={15}
                    scrollWheelZoom={true}
                >
                    <TileLayer
                        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />
                    {
                        [this.renderLines(),
                            this.renderMarkers()]
                    }
                </MapContainer>
            </div>
        );
    }
}

export default Map;
