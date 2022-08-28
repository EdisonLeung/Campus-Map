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
import {Path} from "./types";

interface EdgeListProps {
    onChange(edges: Path[]): void;
}

interface EditEdeListState {
    textValue: string;
    errorText: string;
}
/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
const initialMessage: string = "Enter edge data (x1 y1 x2 y2 color)";
class EdgeList extends Component<EdgeListProps, EditEdeListState> {
    constructor(props: EdgeListProps) {
        super(props);
        this.state = {
            textValue: initialMessage,
            errorText:""
        };
    }

    textChange(event: any) {
        this.setState({
            textValue: event.target.value
        });
    }
    onDrawButtonClick = () => {
        let data: string[] = this.state.textValue.split(  /[ \n]/);
        const paths: Path[] = [];

        const correctlyFormatted = () => {
            this.setErrorMessage("");
            if (data.length % 5 !== 0) {
               this.setErrorMessage("Wrong number of arguments (requires: x1 y1 x2 y2 color)");
                return false;
            }
            for (let i = 0; i < data.length; i++) {
                if ((i+1) % 5 === 0 && !isNaN(parseFloat(data[i]))) {
                    this.setErrorMessage("Given color is a number")
                    return false;
                } else if ((i+1) % 5 !== 0) {
                    if (isNaN(parseFloat(data[i]))) {
                        this.setErrorMessage("Given point number is not a number")
                        return false;
                    } else if ((parseFloat(data[i]) < 0 || parseFloat(data[i]) > 4000)) {
                        this.setErrorMessage("Point is out of bounds (0, 0) - (4000, 4000)")
                        return false;
                    }
                }
            }
            return true;
        }

        if (correctlyFormatted()) {
            for (let i = 0; i < data.length; i+=5) {
                paths.push({x1: parseFloat(data[i]),
                            y1: parseFloat(data[i+1]),
                            x2: parseFloat(data[i+2]),
                            y2: parseFloat(data[i+3]),
                            color: data[i+4],
                            key: "key" + i})
            }
            this.props.onChange(paths)
        }
    }

    setErrorMessage (msg: string) {
        this.setState({
            errorText:msg
        })
    }
    onClearButtonClick = () => {
        this.setState({
            textValue: initialMessage,
        });
        this.props.onChange([]);
    }
    render() {
        return (
            <div id="edge-list">
                <div style={{color:"red"}}>{this.state.errorText}</div>
                Edges <br/>
                <textarea
                    rows={5}
                    cols={30}
                    onChange={(event) => this.textChange(event)}
                    value={this.state.textValue}
                /> <br/>
                <button onClick= {this.onDrawButtonClick}>Draw</button>
                <button onClick={this.onClearButtonClick}>Clear</button>
            </div>
    );
    }
}

export default EdgeList;
