import React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as MenuActions from './MenuFormActions';
import {Navbar, Nav, Button, Breadcrumb} from 'react-bootstrap'
import {AlertList} from 'react-bs-notifier';
import {HashUtils} from '../Common/Utils';

@connect(
    state => ({
        breadcrumbs: state.menuReducer.breadcrumbs,
        menus: state.menuReducer.menus,
        alerts: state.menuReducer.alerts
    }),
    dispatch => ({
        getBreadcrumbs: bindActionCreators(MenuActions.getBreadcrumbs, dispatch),
        getNextLevelMenus: bindActionCreators(MenuActions.getNextLevelMenus, dispatch),
        logout: bindActionCreators(MenuActions.logout, dispatch),
        dismissAlert: bindActionCreators(MenuActions.dismissAlert, dispatch)
    })
)
export default class MenuForm extends React.Component {

    state = {
        currentUrl: undefined
    };

    constructor(props) {
        super(props);
    }

    updateAll = () => {
        let currentUrl = window.location.hash;
        if(currentUrl === '#/') currentUrl = '/root';
        currentUrl = HashUtils.cleanHash(currentUrl);
        if(this.state.currentUrl !== currentUrl){
            this.setState({currentUrl: currentUrl});
            this.props.getBreadcrumbs(currentUrl);
            this.props.getNextLevelMenus(currentUrl);
        }
    };

    onAlertDismissed(alert) {
        this.props.dismissAlert(alert);
    }

    componentWillReceiveProps(nextProps, nextContext) {
        this.updateAll();
    }

    componentDidMount() {
        this.updateAll();
    }

    render() {
        let allMenus = [];
        this.props.menus.forEach(function(element){
            allMenus.push(<Button key={'btn_' + element.url} style={{height: '200px', width: '200px', margin: '10px', lineHeight: '200px', fontSize: 'x-large'}} href={'#' + element.url}> {element.name} </Button>)
        });
        let breads = [];
        let breadcrumbsCount = this.props.breadcrumbs.length;
        this.props.breadcrumbs.forEach(function(element, index){
            breads.push(<Breadcrumb.Item style={{fontWeight: index === breadcrumbsCount - 1 ? 'bold' : 'normal'}} key={element.url} href={'#' + element.url}> {element.name} </Breadcrumb.Item>)
        });
        if(this.props.breadcrumbs.length === 0) breads.push(<Breadcrumb.Item key={'/root'} href={'#/'}>Home</Breadcrumb.Item>);
        let allAlerts = this.props.alerts;
        return (
            <div>
                <AlertList
                    position={'top-right'}
                    alerts={allAlerts}
                    timeout={10000}
                    dismissTitle='Begone!'
                    onDismiss={this.onAlertDismissed.bind(this)}
                />
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
                {allMenus}
            </div>
        )
    }
}