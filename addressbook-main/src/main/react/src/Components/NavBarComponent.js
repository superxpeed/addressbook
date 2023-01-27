import React from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import {Checkbox, Chip, FormControlLabel, FormGroup} from "@mui/material";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import * as url from "../Common/Url";
import * as CommonActions from "../Pages/UniversalListActions";
import * as MenuActions from "../Pages/MenuFormActions";
import {AuthTokenUtils} from "../Common/Utils";

export class NavBarComponentInner extends React.Component {
    state = {
        username: "", roles: [], show: false, buildVersion: "", buildTime: "",
    };

    handleClose = () => {
        this.setState({show: false});
    };

    handleShow = () => {
        this.setState({show: true});
    };

    updatePersonInfo = () => {
        let isOk = false;
        const headers = new Headers();
        AuthTokenUtils.addAuthToken(headers);
        headers.append("Accept", "application/json");
        headers.append("Content-Type", "application/json; charset=utf-8");
        fetch(url.GET_USER_INFO, {
            method: "get", headers,
        })
            .then((response) => {
                CommonActions.ifNoAuthorizedRedirect(response);
                isOk = response.ok;
                return response.text();
            })
            .then((text) => {
                if (isOk) {
                    const currentUser = JSON.parse(text);
                    this.setState({
                        username: currentUser.login, roles: currentUser.roles,
                    });
                } else {
                    this.props.showCommonErrorAlert(text);
                }
            });
    };

    updateBuildInfo = () => {
        let isOk = false;
        const headers = new Headers();
        AuthTokenUtils.addAuthToken(headers);
        headers.append("Accept", "application/json");
        headers.append("Content-Type", "application/json; charset=utf-8");
        fetch(url.GET_BUILD_INFO, {
            method: "get", headers,
        })
            .then((response) => {
                CommonActions.ifNoAuthorizedRedirect(response);
                isOk = response.ok;
                return response.text();
            })
            .then((text) => {
                if (isOk) {
                    const buildInfo = JSON.parse(text);
                    this.setState({
                        buildVersion: buildInfo.version, buildTime: buildInfo.time,
                    });
                } else {
                    this.props.showCommonErrorAlert(text);
                }
            });
    };

    componentDidMount() {
        this.updatePersonInfo();
        this.updateBuildInfo();
    }

    getRoles = () => {
        const allRoles = [];
        this.state.roles.forEach((value, index) => {
            if (index === 0)
                allRoles.push(<Chip key={value} label={value} variant="outlined"/>);
            else
                allRoles.push(<Chip key={value} label={value} variant="outlined" sx={{ml: 1}}/>);
        });
        return allRoles;
    };

    render() {
        return (
            <div>
                <div>
                    <Button
                        variant="contained"
                        edge="end"
                        onClick={this.handleShow}
                    >
                        {this.state.username}
                    </Button>
                </div>
                <Dialog
                    onClose={this.handleClose}
                    aria-labelledby="roles-dialog-title"
                    open={this.state.show}
                >
                    <DialogTitle id="roles-dialog-title" onClose={this.handleClose}>
                        Roles for
                        {" "}
                        {this.state.username}
                    </DialogTitle>
                    <DialogContent dividers>
                        {this.getRoles()}
                    </DialogContent>
                    <DialogContent dividers>
                        <Chip
                            edge="end"
                            color="info"
                            size="small"
                            key="build_version"
                            label={`Build version: ${this.state.buildVersion}`}
                            variant="outlined"
                        />
                        <Chip
                            edge="end"
                            color="info"
                            size="small"
                            key="build_time"
                            label={`Build time: ${this.state.buildTime}`}
                            variant="outlined"
                            sx={{ml: 1}}
                        />
                    </DialogContent>
                    <DialogContent dividers>
                        <FormGroup>
                            <FormControlLabel control={<Checkbox checked={this.props.showNotification}
                                                                 onChange={(e, v) => {
                                                                     this.props.changeShowNotification(v)
                                                                 }}
                                                                 inputProps={{"aria-label": "controlled"}}/>}
                                              label="Show lock notifications"/>
                        </FormGroup>
                    </DialogContent>
                </Dialog>
            </div>
        );
    }
}

export const NavBarComponent = connect((state) => ({
    showNotification: state.universalListReducer.showNotification,
}), (dispatch) => ({
    showCommonErrorAlert: bindActionCreators(MenuActions.showCommonErrorAlert, dispatch),
    changeShowNotification: bindActionCreators(CommonActions.changeShowNotification, dispatch),
}), null, {withRef: true})(NavBarComponentInner);