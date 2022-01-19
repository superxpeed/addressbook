import React from "react";
import {Button, Label, Modal} from "react-bootstrap";
import * as url from "../Common/Url";
import {ifNoAuthorizedRedirect} from "../Pages/UniversalListActions";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import * as MenuActions from "../Pages/MenuFormActions";

@connect(null, (dispatch) => ({
    showCommonErrorAlert: bindActionCreators(
        MenuActions.showCommonErrorAlert,
        dispatch
    ),
}))
export class UserComponent extends React.Component {
    state = {
        username: "",
        roles: [],
        show: false,
    };

    constructor(props) {
        super(props);
    }

    handleClose = () => {
        this.setState({show: false});
    };

    handleShow = () => {
        this.setState({show: true});
    };

    updatePersonInfo = () => {
        let isOk = false;
        let headers = new Headers();
        let token = window.sessionStorage.getItem("auth-token");
        headers.append("Accept", "application/json");
        headers.append("Content-Type", "application/json; charset=utf-8");
        headers.append("Authorization", "Bearer " + token);
        fetch(url.GET_USER_INFO, {
            method: "get",
            headers: headers,
        })
            .then((response) => {
                ifNoAuthorizedRedirect(response);
                isOk = response.ok;
                return response.text();
            })
            .then((text) => {
                if (isOk) {
                    let currentUser = JSON.parse(text);
                    this.setState({
                        username: currentUser.login,
                        roles: currentUser.roles,
                    });
                } else {
                    this.props.showCommonErrorAlert(text);
                }
            });
    };

    componentDidMount() {
        this.updatePersonInfo();
    }

    getRoles = () => {
        let allRoles = [];
        this.state.roles.forEach((value) => {
            allRoles.push(
                <Label bsStyle="primary" style={{marginRight: "5px"}} key={value}>
                    {value}
                </Label>
            );
        });
        return allRoles;
    };

    render() {
        return (
            <div style={{display: "inline-block"}}>
                <Button
                    style={{maxWidth: "100px", overflowX: "hidden", marginRight: "5px"}}
                    onClick={this.handleShow}
                >
                    {this.state.username}
                </Button>
                <Modal show={this.state.show} onHide={this.handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Roles for {this.state.username}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>{this.getRoles()}</Modal.Body>
                </Modal>
            </div>
        );
    }
}
