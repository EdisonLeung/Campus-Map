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

import React, {Component} from 'react';
import {Building, Path} from "./types";

interface EdgeListProps {
    onChange(edges: Path[]): void;
}

interface EditEdeListState {
    startSelection: string;
    endSelection: string;
    errorText: string;
    buildings: Building[];
}
/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
const initStartSelection: string = "select start building";
const initEndSelection: string = "select end building";
class EdgeList extends Component<EdgeListProps, EditEdeListState> {
    constructor(props: EdgeListProps) {
        super(props);
        this.state = {
            startSelection: initStartSelection,
            endSelection: initEndSelection,
            errorText:"",
            buildings: []
        };
        this.initializeBuildingList();
    }

    // Gets the building list from CampusMaps and transfers that list to a type script list
    async initializeBuildingList() {
        const buildingMap: Building[] = [];
        try {
            let responsePromise = fetch("http://localhost:4567/buildings");
            let response = await responsePromise;

            let building = (await response.json());
            for (let shortName in building) {
                buildingMap.push({shortName:shortName, longName: building[shortName]})
            }
        } catch (e) {
            alert("Unable to retrieve buildings");
            console.log(e);
        }
        this.setState({
            buildings:buildingMap
        })
    }

    // handles event for when the start building dropdown box changes
    handleStartChange = (event:any) => {
        this.setState({
            startSelection:event.target.value
        })
    }

    // handles event for when the destination building dropdown box changes
    handleEndChange = (event:any) => {
        this.setState({
            endSelection:event.target.value
        })
    }

    // set and displays the given error message
    setErrorMessage (msg: string) {
        this.setState({
            errorText:msg
        })
    }

    // clears building selection and map paths when the clear button is pressed
    onClearButtonClick = () => {
        this.setState({
            startSelection: initStartSelection,
            endSelection: initStartSelection,
        });
        this.props.onChange([]);
    }

    // function that gets the shortest path from each building when the draw button is pressed
    makeRequestRoute = async () => {
        const paths: Path[] = [];
        this.setErrorMessage("");
        if (this.state.startSelection === initStartSelection ||
            this.state.endSelection === initEndSelection) {
            this.setErrorMessage("No building selected");
        } else {
            try {
                let responsePromise = fetch("http://localhost:4567/route?start=" +
                    this.state.startSelection + "&end=" + this.state.endSelection);
                let response = await responsePromise;

                if (!response.ok) {
                    alert("invalid input");
                    return;
                }

                let object = (await response.json());
                for (let i = 0; i < object.path.length; i++) {
                    let path = object.path[i];
                    paths.push({
                        x1: path.start.x,
                        y1: path.start.y,
                        x2: path.end.x,
                        y2: path.end.y,
                        key: path.cost,
                        color: "red"
                    })
                }
                this.props.onChange(paths);
            } catch (e) {
                alert("unable to render path");
                console.log(e);
            }
        }
    }

    // visualizes the building options into a dropdown box
    loadBuildingSelection = (initialSelection: string) => {
        let buildingList = [];
        buildingList.push(<option value={initialSelection}> {initialSelection} </option>)

        for (let i = 0; i < this.state.buildings.length; i++) {
            buildingList.push(<option value={this.state.buildings[i].shortName}>{
                this.state.buildings[i].shortName} - {this.state.buildings[i].longName}</option>)
        }
        return buildingList;
    }
    render() {
        return (
            <div id="edge-list">
                <div style={{color:"red"}}>{this.state.errorText}</div>
                Building Paths <br/>
                <select value={this.state.startSelection} onChange={this.handleStartChange}>
                    {[this.loadBuildingSelection(initStartSelection)]}
                </select>
                <select value={this.state.endSelection} onChange={this.handleEndChange}>
                    {[this.loadBuildingSelection(initEndSelection)]}
                </select>
                <br/>
                <button onClick= {this.makeRequestRoute}>Draw</button>
                <button onClick={this.onClearButtonClick}>Clear</button>
            </div>
        );
    }
}

export default EdgeList;
