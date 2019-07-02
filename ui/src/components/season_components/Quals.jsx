import React, {Component} from 'react';
import {AppBar, Tab, Tabs} from "@material-ui/core";
import QualsSeeding from "./quals_components/QualsSeeding";
import QualsMatches from "./quals_components/QualsMatches";

class Quals extends Component {

    constructor(props) {
        super(props);

        this.state = {
            tabActive: 1,
        };

    }

    handleChange = (event, newValue) => {
        this.setState(state => {
            return {
                ...state,
                tabActive: newValue,
            }
        });
    }

    render() {
        return (
            <div>
                <AppBar position="static">
                    <Tabs value={this.state.tabActive} onChange={this.handleChange}>
                        <Tab label="Seeding"/>
                        <Tab label="Matches"/>
                        <Tab label="Match Replays"/>
                    </Tabs>
                </AppBar>
                {this.state.tabActive === 0 && <QualsSeeding year={this.props.year} round={this.props.round}/>}
                {this.state.tabActive === 1 && <QualsMatches year={this.props.year} round={this.props.round} day={1}/>}
                {this.state.tabActive === 2 && <QualsMatches year={this.props.year} round={this.props.round} day={-1}/>}
            </div>
        );
    }
}


export default Quals;