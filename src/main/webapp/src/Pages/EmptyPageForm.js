import React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as MenuActions from './MenuFormActions';
import {Navbar, Nav, Breadcrumb} from 'react-bootstrap'
import {HashUtils} from '../Common/Utils';

@connect(
    state => ({
        breadcrumbs: state.menuReducer.breadcrumbs,
    }),
    dispatch => ({
        getBreadcrumbs: bindActionCreators(MenuActions.getBreadcrumbs, dispatch),
    })
)
export default class EmptyPageForm extends React.Component {

    constructor(props) {
        super(props);
    }

    componentWillReceiveProps(nextProps, nextContext) {
        let currentUrl = window.location.hash;
        this.props.getBreadcrumbs(HashUtils.cleanHash(currentUrl));
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
                    </Navbar.Collapse>
                </Navbar>
            </div>
        )
    }
}