import React from 'react';
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import * as MenuActions from "./MenuFormActions";
import {Navbar, Nav, Button, Breadcrumb} from 'react-bootstrap'

@connect(
    state => ({
        breadcrumbs: state.menuReducer.breadcrumbs,
        menus: state.menuReducer.menus,
    }),
    dispatch => ({
        getBreadcrumbs: bindActionCreators(MenuActions.getBreadcrumbs, dispatch),
        getNextLevelMenus: bindActionCreators(MenuActions.getNextLevelMenus, dispatch),
    })
)
export default class MenuForm extends React.Component {

    state = {
        currentUrl: undefined
    };

    constructor(props) {
        super(props);
    }

    cleanHash = (hash) => {
        if(hash === '/root') return hash;
        if(hash.startsWith('#')) return hash.substring(1);
    };

    componentWillReceiveProps(nextProps, nextContext) {
        console.log('newProps');
        let currentUrl = window.location.hash;
        console.log(currentUrl);
        if(currentUrl === '#/') currentUrl = '/root';
        if(this.state.currentUrl !== this.cleanHash(currentUrl)){
            this.setState({currentUrl: this.cleanHash(currentUrl)});
            this.props.getBreadcrumbs(this.cleanHash(currentUrl));
            this.props.getNextLevelMenus(this.cleanHash(currentUrl));
        }
    }


    componentDidMount() {
        console.log('newProps');
        let currentUrl = window.location.hash;
        console.log(currentUrl);
        if(currentUrl === '#/') currentUrl = '/root';
        if(this.state.currentUrl !== this.cleanHash(currentUrl)){
            this.setState({currentUrl: this.cleanHash(currentUrl)});
            this.props.getBreadcrumbs(this.cleanHash(currentUrl));
            this.props.getNextLevelMenus(this.cleanHash(currentUrl));
        }
    }

    render() {
        let allMenus = [];
        this.props.menus.forEach(function(element){
            allMenus.push(<Button style={{height: '200px', width: '200px', margin: '10px', lineHeight: '200px', fontSize: 'x-large'}} href={'#' + element.url}> {element.name} </Button>)
        });
        let breads = [];
        this.props.breadcrumbs.forEach(function(element){
            breads.push(<Breadcrumb.Item href={'#' + element.url}> {element.name} </Breadcrumb.Item>)
        });
        if(this.props.breadcrumbs.length === 0) breads.push(<Breadcrumb.Item href={'#/'}>Home</Breadcrumb.Item>);
        console.log(this.props.breadcrumbs);
        console.log(this.props.menus);
        return (
            <div>
                <Navbar>
                    <Navbar.Collapse>
                        <Nav>
                            <Breadcrumb>
                                {breads}
                            </Breadcrumb>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
                {allMenus}
            </div>
        )
    }
}