import React from "react";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import * as MenuActions from "./MenuFormActions";
import {Breadcrumb, Button, Label, Nav, Navbar, Table} from "react-bootstrap";
import {HashUtils} from "../Common/Utils";
import {UserComponent} from "../Components/UserComponent";
import {EventSourcePolyfill} from "event-source-polyfill";

@connect(
    (state) => ({
        breadcrumbs: state.menuReducer.breadcrumbs,
    }),
    (dispatch) => ({
        getBreadcrumbs: bindActionCreators(MenuActions.getBreadcrumbs, dispatch),
        logout: bindActionCreators(MenuActions.logout, dispatch),
    })
)
export default class AdminPageForm extends React.Component {
    state = {
        jvmState: {}
    };

    constructor(props) {
        super(props);
    }

    onOpen = () => {
        console.log("Connection opened for JVM state");
    };

    onError = () => {
        if (this.state.eventSource.readyState === EventSource.CONNECTING) {
            console.log("Reconnecting to JVM event source");
        } else {
            console.log("Error: " + this.state.eventSource.readyState);
        }
    };

    onMessage = (e) => {
        let result = JSON.parse(e.data);
        this.setState({jvmState: result});
    };

    componentDidMount() {
        let EventSource = EventSourcePolyfill;
        let currentUrl = window.location.hash;
        this.props.getBreadcrumbs(HashUtils.cleanHash(currentUrl));
        let token = window.sessionStorage.getItem("auth-token");
        let newEventSource = new EventSource("/rest/admin/jvmState", {
            headers: {
                "Authorization": "Bearer " + token
            }
        });
        newEventSource.onopen = this.onOpen;
        newEventSource.onmessage = this.onMessage;
        newEventSource.onerror = this.onError;

        this.setState({
            eventSource: newEventSource
        });
    }

    componentWillUnmount() {
        this.state.eventSource.close();
    }

    render() {
        let breads = [];
        let breadcrumbsCount = this.props.breadcrumbs.length;
        this.props.breadcrumbs.forEach(function (element, index) {
            breads.push(
                <Breadcrumb.Item
                    style={{
                        fontWeight: index === breadcrumbsCount - 1 ? "bold" : "normal",
                    }}
                    key={element.url}
                    href={"#" + element.url}
                >
                    {" "}
                    {element.name}{" "}
                </Breadcrumb.Item>
            );
        });
        return (
            <div>
                <Navbar>
                    <Navbar.Collapse>
                        <Nav>
                            <Breadcrumb>{breads}</Breadcrumb>
                        </Nav>
                        <Nav pullRight>
                            <UserComponent/>
                            <Button onClick={() => this.props.logout()}>Logout</Button>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
                <div id="adminContainer">
                    <h4>
                        <Label bsStyle="danger">Runtime</Label>
                    </h4>
                    <Table striped bordered condensed hover>
                        <tr>
                            <td>Total memory</td>
                            <td>{this.state.jvmState.runtimeTotalMemory}</td>
                        </tr>
                        <tr>
                            <td>Free memory</td>
                            <td>{this.state.jvmState.runtimeFreeMemory}</td>
                        </tr>
                        <tr>
                            <td>Max memory</td>
                            <td>{this.state.jvmState.runtimeMaxMemory}</td>
                        </tr>
                    </Table>
                    <h4>
                        <Label bsStyle="danger">System</Label>
                    </h4>
                    <Table striped bordered condensed hover>
                        <tr>
                            <td>Available processors</td>
                            <td>{this.state.jvmState.availableProcessors}</td>
                        </tr>
                        <tr>
                            <td>Load average</td>
                            <td>{this.state.jvmState.systemLoadAverage}</td>
                        </tr>
                        <tr>
                            <td>Architecture</td>
                            <td>{this.state.jvmState.arch}</td>
                        </tr>
                        <tr>
                            <td>Operating system</td>
                            <td>{this.state.jvmState.name}</td>
                        </tr>
                        <tr>
                            <td>Operating system version</td>
                            <td>{this.state.jvmState.version}</td>
                        </tr>
                        <tr>
                            <td>Total physical memory</td>
                            <td>{this.state.jvmState.totalPhysicalMemory}</td>
                        </tr>
                        <tr>
                            <td>Total cpu load</td>
                            <td>{this.state.jvmState.totalCpuLoad}</td>
                        </tr>
                        <tr>
                            <td>Disk size</td>
                            <td>{this.state.jvmState.diskSize}</td>
                        </tr>
                        <tr>
                            <td>OS user</td>
                            <td>{this.state.jvmState.user}</td>
                        </tr>
                    </Table>
                    <h4>
                        <Label bsStyle="danger">Heap</Label>
                    </h4>
                    <Table striped bordered condensed hover>
                        <tr>
                            <td>Used</td>
                            <td>{this.state.jvmState.heapMemoryUsed}</td>
                        </tr>
                        <tr>
                            <td>Initial</td>
                            <td>{this.state.jvmState.heapMemoryInit}</td>
                        </tr>
                        <tr>
                            <td>Committed</td>
                            <td>{this.state.jvmState.heapMemoryCommitted}</td>
                        </tr>
                        <tr>
                            <td>Max</td>
                            <td>{this.state.jvmState.heapMemoryMax}</td>
                        </tr>
                    </Table>
                    <h4>
                        <Label bsStyle="danger">Non-heap</Label>
                    </h4>
                    <Table striped bordered condensed hover>
                        <tr>
                            <td>Used</td>
                            <td>{this.state.jvmState.nonHeapMemoryUsed}</td>
                        </tr>
                        <tr>
                            <td>Initial</td>
                            <td>{this.state.jvmState.nonHeapMemoryInit}</td>
                        </tr>
                        <tr>
                            <td>Committed</td>
                            <td>{this.state.jvmState.nonHeapMemoryCommitted}</td>
                        </tr>
                        <tr>
                            <td>Max</td>
                            <td>{this.state.jvmState.nonHeapMemoryMax}</td>
                        </tr>
                    </Table>
                    <h4>
                        <Label bsStyle="danger">Threads</Label>
                    </h4>
                    <Table striped bordered condensed hover>
                        <tr>
                            <td>Current thread count</td>
                            <td>{this.state.jvmState.threadCount}</td>
                        </tr>
                        <tr>
                            <td>Total started thread count</td>
                            <td>{this.state.jvmState.totalStartedThreadCount}</td>
                        </tr>
                        <tr>
                            <td>Peak thread count</td>
                            <td>{this.state.jvmState.peakThreadCount}</td>
                        </tr>
                    </Table>
                </div>
            </div>
        );
    }
}
