import React, {Component} from "react";
import {xToLon, yToLat} from "./MapLine";
import {icon} from "leaflet";
import {Marker, Popup} from "react-leaflet";

interface LineMarkerProps {
    x: number;
    y: number;
    popupMsg: string;
}

class LineMarker extends Component<LineMarkerProps, any> {
    constructor(props: LineMarkerProps) {
        super(props);
    }

    render() {
        return (
            <div>
                <Marker position={[yToLat(this.props.y), xToLon(this.props.x)] }
                        icon={icon(
                            {iconUrl: "https://www.pngall.com/wp-content/uploads/2017/05/Map-Marker-PNG-Pic.png",
                                    iconSize: [25, 40],
                                    iconAnchor: [12, 40]})}>
                    <Popup>{this.props.popupMsg}</Popup>
                </Marker>
            </div>
        );
    }
}
export default LineMarker;