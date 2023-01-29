import React from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import {Checkbox, Chip, FormControlLabel, FormGroup, IconButton} from "@mui/material";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import * as url from "../Common/Url";
import * as CommonActions from "../Pages/UniversalListActions";
import * as MenuActions from "../Pages/MenuFormActions";
import {AuthTokenUtils} from "../Common/Utils";
import SettingsIcon from "@mui/icons-material/Settings";
import PersonIcon from "@mui/icons-material/Person";

export class NavBarComponentInner extends React.Component {
    state = {
        username: "",
        roles: [],
        showUserInfo: false,
        showSettings: false,
        buildVersion: "",
        buildTime: "",
    };

    handleCloseUserInfo = () => {
        this.setState({showUserInfo: false});
    };

    handleShowUserInfo = () => {
        this.setState({showUserInfo: true});
    };

    handleCloseSettings = () => {
        this.setState({showSettings: false});
    };

    handleShowSettings = () => {
        this.setState({showSettings: true});
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
        let targetBoxColor = this.props.useDarkTheme ? "#90caf9" : "#9c27b0"
        let targetTextColor = this.props.useDarkTheme ? "#000000" : "#FFFFFF"
        this.state.roles.forEach((value, index) => {
            if (index === 0)
                allRoles.push(<Chip key={value} label={value}
                                    sx={{backgroundColor: targetBoxColor, color: targetTextColor}}
                                    color="info" variant="contained"/>);
            else
                allRoles.push(<Chip key={value} label={value}
                                    sx={{ml: 1, backgroundColor: targetBoxColor, color: targetTextColor,}}
                                    color="info" variant="contained"/>);
        });
        return allRoles;
    };

    render() {
        return (<div>
            <div>
                <Button
                    startIcon={<PersonIcon/>}
                    variant="contained"
                    edge="end"
                    color={this.props.useDarkTheme ? "primary" : "topButtonColor"}
                    onClick={this.handleShowUserInfo}
                >
                    {this.state.username}
                </Button>
                <IconButton onClick={this.handleShowSettings}
                            color={this.props.useDarkTheme ? "primary" : "topServiceButtonColor"}>
                    <SettingsIcon/>
                </IconButton>
            </div>
            <Dialog
                onClose={this.handleCloseUserInfo}
                aria-labelledby="roles-dialog-title"
                open={this.state.showUserInfo}
            >
                <DialogTitle id="roles-dialog-title" onClose={this.handleCloseUserInfo}>
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
                        size="small"
                        key="build_version"
                        label={`Build version: ${this.state.buildVersion}`}
                        variant="outlined"
                    />
                    <Chip
                        edge="end"
                        size="small"
                        key="build_time"
                        label={`Build time: ${this.state.buildTime}`}
                        variant="outlined"
                        sx={{ml: 1}}
                    />
                </DialogContent>
            </Dialog>
            <Dialog
                onClose={this.handleCloseSettings}
                aria-labelledby="roles-dialog-title"
                open={this.state.showSettings}
            >
                <DialogTitle id="roles-dialog-title" onClose={this.handleCloseSettings}>
                    Settings
                </DialogTitle>
                <DialogContent dividers>
                    <FormGroup>
                        <FormControlLabel control={<Checkbox checked={this.props.showNotification}
                                                             onChange={(e, v) => {
                                                                 this.props.changeShowNotification(v)
                                                             }}
                                                             inputProps={{"aria-label": "controlled"}}/>}
                                          label="Show lock notifications"/>
                    </FormGroup>
                    <FormGroup>
                        <FormControlLabel control={<Checkbox checked={this.props.useDarkTheme}
                                                             onChange={(e, v) => {
                                                                 this.props.changeUseDarkTheme(v)
                                                             }}
                                                             inputProps={{"aria-label": "controlled"}}/>}
                                          label="Use dark theme"/>
                    </FormGroup>
                </DialogContent>
            </Dialog>
        </div>);
    }
}

export const NavBarComponent = connect((state) => ({
    showNotification: state.universalListReducer.showNotification,
    useDarkTheme: state.universalListReducer.useDarkTheme
}), (dispatch) => ({
    showCommonErrorAlert: bindActionCreators(MenuActions.showCommonErrorAlert, dispatch),
    changeShowNotification: bindActionCreators(CommonActions.changeShowNotification, dispatch),
    changeUseDarkTheme: bindActionCreators(CommonActions.changeUseDarkTheme, dispatch)
}), null, {withRef: true})(NavBarComponentInner);