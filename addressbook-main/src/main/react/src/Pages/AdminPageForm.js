import React from "react";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {EventSourcePolyfill} from "event-source-polyfill";
import {styled} from "@mui/material/styles";
import MuiTableCell from "@mui/material/TableCell";
import NavigateNextIcon from "@mui/icons-material/NavigateNext";
import ComputerIcon from "@mui/icons-material/Computer";
import DeveloperBoardIcon from "@mui/icons-material/DeveloperBoard";

import {
    AppBar,
    Breadcrumbs,
    Chip,
    Container,
    Grid,
    IconButton,
    Paper,
    Table,
    TableBody,
    TableContainer,
    TableRow,
    Toolbar,
} from "@mui/material";
import {NavBarComponent} from "../Components/NavBarComponent";
import {getBreadcrumbsList, HashUtils} from "../Common/Utils";
import * as MenuActions from "./MenuFormActions";
import LogoutIcon from "@mui/icons-material/Logout";
import WavesIcon from "@mui/icons-material/Waves";
import StorageIcon from "@mui/icons-material/Storage";
import MemoryIcon from "@mui/icons-material/Memory";

const TableCell = styled(MuiTableCell)({
    width: "50%",
    maxWidth: "50%",
    overflow: "hidden",
    textOverflow: "ellipsis",
});

export class AdminPageFormInner extends React.Component {
    state = {
        jvmState: {},
    };

    onOpen = () => {
        console.log("Connection opened for JVM state");
    };

    onError = () => {
        if (this.state.eventSource.readyState === EventSource.CONNECTING) {
            console.log("Reconnecting to JVM event source");
        } else {
            console.log(`Error: ${this.state.eventSource.readyState}`);
        }
    };

    onMessage = (e) => {
        const result = JSON.parse(e.data);
        this.setState({jvmState: result});
    };

    componentDidMount() {
        const EventSource = EventSourcePolyfill;
        const currentUrl = window.location.hash;
        this.props.getBreadcrumbs(HashUtils.cleanHash(currentUrl));
        const token = window.sessionStorage.getItem("auth-token");
        const newEventSource = new EventSource("/rest/admin/jvmState", {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        newEventSource.onopen = this.onOpen;
        newEventSource.onmessage = this.onMessage;
        newEventSource.onerror = this.onError;

        this.setState({
            eventSource: newEventSource,
        });
    }

    componentWillUnmount() {
        this.state.eventSource.close();
    }

    render() {
        const breads = getBreadcrumbsList(this.props.breadcrumbs, this.props.useDarkTheme)
        let separator;
        if (this.props.useDarkTheme) {
            separator = <NavigateNextIcon fontSize="small"/>
        } else {
            separator = <NavigateNextIcon fontSize="small" style={{color: "white"}}/>
        }
        return (
            <div>
                <AppBar position="static">
                    <Container maxWidth="xl">
                        <Toolbar disableGutters>
                            <Breadcrumbs separator={separator} style={{flex: 1}}
                                         aria-label="breadcrumb">{breads}</Breadcrumbs>
                            <NavBarComponent/>
                            <IconButton color={this.props.useDarkTheme ? "primary" : "topServiceButtonColor"}
                                        onClick={() => this.props.logout()}>
                                <LogoutIcon/>
                            </IconButton>
                        </Toolbar>
                    </Container>
                </AppBar>
                <Container maxWidth="sm">
                    <Grid container sx={{justifyContent: "center"}}>
                        <Chip icon={<DeveloperBoardIcon/>} color="primary" label="Runtime" sx={{m: 2, width: "100%"}}/>
                    </Grid>
                    <TableContainer component={Paper}>
                        <Table aria-label="dense table">
                            <TableBody>
                                <TableRow component="th">
                                    <TableCell>Total memory</TableCell>
                                    <TableCell>{this.state.jvmState.runtimeTotalMemory}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>Free memory</TableCell>
                                    <TableCell>{this.state.jvmState.runtimeFreeMemory}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>Max memory</TableCell>
                                    <TableCell>{this.state.jvmState.runtimeMaxMemory}</TableCell>
                                </TableRow>
                            </TableBody>
                        </Table>
                    </TableContainer>
                    <Grid container sx={{justifyContent: "center"}}>
                        <Chip icon={<ComputerIcon/>} color="primary" label="System" sx={{m: 2, width: "100%"}}/>
                    </Grid>
                    <TableContainer component={Paper}>
                        <Table aria-label="dense table">
                            <TableBody>
                                <TableRow component="th">
                                    <TableCell>Available processors</TableCell>
                                    <TableCell>{this.state.jvmState.availableProcessors}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>Load average</TableCell>
                                    <TableCell>{this.state.jvmState.systemLoadAverage}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>Architecture</TableCell>
                                    <TableCell>{this.state.jvmState.arch}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>Operating system</TableCell>
                                    <TableCell>{this.state.jvmState.name}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>Operating system version</TableCell>
                                    <TableCell>{this.state.jvmState.version}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>Total physical memory</TableCell>
                                    <TableCell>{this.state.jvmState.totalPhysicalMemory}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>Total cpu load</TableCell>
                                    <TableCell>{this.state.jvmState.totalCpuLoad}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>Disk size</TableCell>
                                    <TableCell>{this.state.jvmState.diskSize}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>OS user</TableCell>
                                    <TableCell>{this.state.jvmState.user}</TableCell>
                                </TableRow>
                            </TableBody>
                        </Table>
                    </TableContainer>
                    <Grid container sx={{justifyContent: "center"}}>
                        <Chip icon={<StorageIcon/>} color="primary" label="Heap" sx={{m: 2, width: "100%"}}/>
                    </Grid>
                    <TableContainer component={Paper}>
                        <Table aria-label="dense table">
                            <TableRow component="th">
                                <TableCell>Used</TableCell>
                                <TableCell>{this.state.jvmState.heapMemoryUsed}</TableCell>
                            </TableRow>
                            <TableRow>
                                <TableCell>Initial</TableCell>
                                <TableCell>{this.state.jvmState.heapMemoryInit}</TableCell>
                            </TableRow>
                            <TableRow>
                                <TableCell>Committed</TableCell>
                                <TableCell>{this.state.jvmState.heapMemoryCommitted}</TableCell>
                            </TableRow>
                            <TableRow>
                                <TableCell>Max</TableCell>
                                <TableCell>{this.state.jvmState.heapMemoryMax}</TableCell>
                            </TableRow>
                        </Table>
                    </TableContainer>
                    <Grid container sx={{justifyContent: "center"}}>
                        <Chip icon={<MemoryIcon/>} color="primary" label="Non-heap" sx={{m: 2, width: "100%"}}/>
                    </Grid>
                    <TableContainer component={Paper}>
                        <Table aria-label="dense table">
                            <TableRow component="th">
                                <TableCell>Used</TableCell>
                                <TableCell>{this.state.jvmState.nonHeapMemoryUsed}</TableCell>
                            </TableRow>
                            <TableRow>
                                <TableCell>Initial</TableCell>
                                <TableCell>{this.state.jvmState.nonHeapMemoryInit}</TableCell>
                            </TableRow>
                            <TableRow>
                                <TableCell>Committed</TableCell>
                                <TableCell>{this.state.jvmState.nonHeapMemoryCommitted}</TableCell>
                            </TableRow>
                            <TableRow>
                                <TableCell>Max</TableCell>
                                <TableCell>{this.state.jvmState.nonHeapMemoryMax}</TableCell>
                            </TableRow>
                        </Table>
                    </TableContainer>
                    <Grid container sx={{justifyContent: "center"}}>
                        <Chip icon={<WavesIcon/>} color="primary" label="Threads" sx={{m: 2, width: "100%"}}/>
                    </Grid>
                    <TableContainer component={Paper}>
                        <Table>
                            <TableRow component="th">
                                <TableCell>Current thread count</TableCell>
                                <TableCell>{this.state.jvmState.threadCount}</TableCell>
                            </TableRow>
                            <TableRow>
                                <TableCell>Total started thread count</TableCell>
                                <TableCell>{this.state.jvmState.totalStartedThreadCount}</TableCell>
                            </TableRow>
                            <TableRow>
                                <TableCell>Peak thread count</TableCell>
                                <TableCell>{this.state.jvmState.peakThreadCount}</TableCell>
                            </TableRow>
                        </Table>
                    </TableContainer>
                </Container>
            </div>
        );
    }
}

export const AdminPageForm = connect((state) => ({
    breadcrumbs: state.menuReducer.breadcrumbs,
    useDarkTheme: state.universalListReducer.useDarkTheme
}), (dispatch) => ({
    getBreadcrumbs: bindActionCreators(MenuActions.getBreadcrumbs, dispatch),
    logout: bindActionCreators(MenuActions.logout, dispatch),
}), null, {withRef: true})(AdminPageFormInner);