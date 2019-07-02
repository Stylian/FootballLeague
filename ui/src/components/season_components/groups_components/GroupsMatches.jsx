import React, {Component} from 'react';
import {
    Box,
    Card,
    CardContent,
    CardHeader,
    Grid,
    Paper,
    TableBody,
    TableCell,
    TableHead,
    TableRow
} from "@material-ui/core";

class GroupsMatches extends Component {

    constructor(props) {
        super(props);

        this.state = {
            error: null,
            isLoaded: false,
            days: {},
        };

    }

    componentDidMount() {
        fetch("http://localhost:8080/rest/seasons/" + this.props.year + "/groups/"
            + this.props.round + "/matches")
            .then(res => res.json())
            .then(
                (result) => {
                    this.setState(state => {

                        return {
                            ...state,
                            isLoaded: true,
                            days: result,
                        }
                    });
                },
                (error) => {
                    this.setState(state => {
                        return {
                            ...state,
                            isLoaded: true,
                            error
                        }
                    });
                }
            )
    }

    render() {

        return (
            <Box width={1200}>

                <Grid container spacing={1}>
                    {Object.keys(this.state.days).map((day, index) => {
                        return (
                            <Grid item sm>
                                <Card style={{margin: 20}}>
                                    <CardHeader title={"Day " + day} align={"center"}
                                                style={{backgroundColor: '#f5f5f5'}}
                                                titleTypographyProps={{variant: 'h7'}}
                                    />
                                    <CardContent>
                                        <table className="table" align={"center"}>
                                            <TableHead>
                                                <TableRow>
                                                    <TableCell>Home</TableCell>
                                                    <TableCell>score</TableCell>
                                                    <TableCell>Away</TableCell>
                                                </TableRow>
                                            </TableHead>
                                            <TableBody>
                                                {this.state.days[day].map((game, index) => {
                                                    return (
                                                        <TableRow>
                                                            <TableCell align="left">{game.homeTeam.name}</TableCell>
                                                            <TableCell>{game.result.goalsMadeByHomeTeam + " - "
                                                            + game.result.goalsMadeByAwayTeam}  </TableCell>
                                                            <TableCell align="right">{game.awayTeam.name}</TableCell>
                                                        </TableRow>)
                                                })}
                                            </TableBody>
                                        </table>
                                    </CardContent>
                                </Card>
                            </Grid>
                        )
                    })}
                </Grid>
            </Box>

        );
    }
}


export default GroupsMatches;
