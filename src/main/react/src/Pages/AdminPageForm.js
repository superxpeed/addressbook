import React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as MenuActions from './MenuFormActions';
import {Navbar, Nav, Breadcrumb, Button, Table, Label} from 'react-bootstrap'
import {HashUtils} from '../Common/Utils';

@connect(
    state => ({
        breadcrumbs: state.menuReducer.breadcrumbs,
    }),
    dispatch => ({
        getBreadcrumbs: bindActionCreators(MenuActions.getBreadcrumbs, dispatch),
        logout: bindActionCreators(MenuActions.logout, dispatch)
    })
)
export default class AdminPageForm extends React.Component {

    state = {
        jvmstate: {}
    };

    onOpen = () =>  {
        console.log('Connection opened');
    };

    onerror = () => {
        if (this.state.eventSource.readyState === EventSource.CONNECTING) {
            console.log('Reconnecting');
        } else {
            console.log('Error: ' + this.state.eventSource.readyState);
        }
    };

    onMessage = (e) => {
        let result = JSON.parse(e.data);
        this.setState({jvmstate: result});
    };

    constructor(props) {
        super(props);
    }

    componentDidMount() {
        let currentUrl = window.location.hash;
        this.props.getBreadcrumbs(HashUtils.cleanHash(currentUrl));
        let newEventSource = new EventSource('/rest/admin/jvmstate');
        newEventSource.onopen = this.onOpen;
        newEventSource.onmessage = this.onMessage;
        this.setState({eventSource: newEventSource});
    }

    render() {
        let breads = [];
        let breadcrumbsCount = this.props.breadcrumbs.length;
        this.props.breadcrumbs.forEach(function(element, index){
            breads.push(<Breadcrumb.Item style={{fontWeight: index === breadcrumbsCount - 1 ? 'bold' : 'normal'}} key={element.url} href={'#' + element.url}> {element.name} </Breadcrumb.Item>)
        });
        return (
            <div>
                <Navbar>
                    <Navbar.Collapse>
                        <Nav>
                            <Breadcrumb>
                                {breads}
                            </Breadcrumb>
                        </Nav>
                        <Nav pullRight>
                            <Button onClick={() => this.props.logout()}>
                                Logout
                            </Button>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
                <div id="adminContainer">
                    <h4><Label bsStyle="danger">Runtime</Label></h4>
                    <Table striped bordered condensed hover>
                        <tr>
                            <td>Total memory</td>
                            <td>{this.state.jvmstate.runtimeTotalMemory}</td>
                        </tr>
                        <tr>
                            <td>Free memory</td>
                            <td>{this.state.jvmstate.runtimeFreeMemory}</td>
                        </tr>
                        <tr>
                            <td>Max memory</td>
                            <td>{this.state.jvmstate.runtimeMaxMemory}</td>
                        </tr>
                    </Table>
                    <h4><Label bsStyle="danger">System</Label></h4>
                    <Table striped bordered condensed hover>
                        <tr>
                            <td>Available processors</td>
                            <td>{this.state.jvmstate.availableProcessors}</td>
                        </tr>
                        <tr>
                            <td>Load average</td>
                            <td>{this.state.jvmstate.systemLoadAverage}</td>
                        </tr>
                        <tr>
                            <td>Architecture</td>
                            <td>{this.state.jvmstate.arch}</td>
                        </tr>
                        <tr>
                            <td>Operating system</td>
                            <td>{this.state.jvmstate.name}</td>
                        </tr>
                        <tr>
                            <td>Operating system version</td>
                            <td>{this.state.jvmstate.version}</td>
                        </tr>
                        <tr>
                            <td>Total physical memory</td>
                            <td>{this.state.jvmstate.totalPhysicalMemory}</td>
                        </tr>
                        <tr>
                            <td>Total cpu load</td>
                            <td>{this.state.jvmstate.totalCpuLoad}</td>
                        </tr>
                        <tr>
                            <td>Disk size</td>
                            <td>{this.state.jvmstate.diskSize}</td>
                        </tr>
                        <tr>
                            <td>OS user</td>
                            <td>{this.state.jvmstate.user}</td>
                        </tr>
                    </Table>
                    <h4><Label bsStyle="danger">Heap</Label></h4>
                    <Table striped bordered condensed hover>
                        <tr>
                            <td>Used</td>
                            <td>{this.state.jvmstate.heapMemoryUsed}</td>
                        </tr>
                        <tr>
                            <td>Initial</td>
                            <td>{this.state.jvmstate.heapMemoryInit}</td>
                        </tr>
                        <tr>
                            <td>Committed</td>
                            <td>{this.state.jvmstate.heapMemoryCommitted}</td>
                        </tr>
                        <tr>
                            <td>Max</td>
                            <td>{this.state.jvmstate.heapMemoryMax}</td>
                        </tr>
                    </Table>
                    <h4><Label bsStyle="danger">Non-heap</Label></h4>
                    <Table striped bordered condensed hover>
                        <tr>
                            <td>Used</td>
                            <td>{this.state.jvmstate.nonHeapMemoryUsed}</td>
                        </tr>
                        <tr>
                            <td>Initial</td>
                            <td>{this.state.jvmstate.nonHeapMemoryInit}</td>
                        </tr>
                        <tr>
                            <td>Committed</td>
                            <td>{this.state.jvmstate.nonHeapMemoryCommitted}</td>
                        </tr>
                        <tr>
                            <td>Max</td>
                            <td>{this.state.jvmstate.nonHeapMemoryMax}</td>
                        </tr>
                    </Table>
                    <h4><Label bsStyle="danger">Threads</Label></h4>
                    <Table striped bordered condensed hover>
                        <tr>
                            <td>Current thread count</td>
                            <td>{this.state.jvmstate.threadCount}</td>
                        </tr>
                        <tr>
                            <td>Total started thread count</td>
                            <td>{this.state.jvmstate.totalStartedThreadCount}</td>
                        </tr>
                        <tr>
                            <td>Peak thread count</td>
                            <td>{this.state.jvmstate.peakThreadCount}</td>
                        </tr>
                    </Table>
                </div>
            </div>
        )
    }
}