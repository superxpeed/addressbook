import React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as MenuActions from './MenuFormActions';
import {Navbar, Nav, Breadcrumb, Button} from 'react-bootstrap'
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

    constructor(props) {
        super(props);
    }

    componentDidMount() {
        let currentUrl = window.location.hash;
        this.props.getBreadcrumbs(HashUtils.cleanHash(currentUrl));
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
                <div> ADMIN PAGE FORM</div>
            </div>
        )
    }
}