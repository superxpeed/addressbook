import React from "react";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {AlertList} from "react-bs-notifier";
import {AppBar, Breadcrumbs, Container, IconButton, Link, Toolbar,} from "@mui/material";
import Button from "@mui/material/Button";
import * as MenuActions from "./MenuFormActions";
import * as Utils from "../Common/Utils";
import {AuthTokenUtils, HashUtils} from "../Common/Utils";
import {NavBarComponent} from "../Components/NavBarComponent";
import * as url from "../Common/Url";
import {ifNoAuthorizedRedirect} from "./UniversalListActions";
import NavigateNextIcon from "@mui/icons-material/NavigateNext";
import LogoutIcon from "@mui/icons-material/Logout";

export class MenuFormInner extends React.Component {
    state = {
        currentUrl: undefined,
    };

    updateAll = () => {
        let currentUrl = window.location.hash;
        if (currentUrl === "#/") currentUrl = "/root";
        currentUrl = HashUtils.cleanHash(currentUrl);
        if (this.state.currentUrl !== currentUrl) {
            const headers = new Headers();
            AuthTokenUtils.addAuthToken(headers);
            fetch(`${url.CHECK_IF_PAGE_EXISTS}?page=${currentUrl}`, {
                method: "get", headers,
            }).then((response) => {
                ifNoAuthorizedRedirect(response);
                return response.text();
            }).then((text) => {
                if (text === "true") {
                    this.setState({currentUrl});
                    this.props.getBreadcrumbs(currentUrl);
                    this.props.getNextLevelMenus(currentUrl);
                } else if (text === "false") {
                    window.history.pushState("", "/", "404.html");
                    window.location.pathname = "404.html";
                }
            });
        }
    };

    onAlertDismissed(alert) {
        this.props.dismissAlert(alert);
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        this.updateAll();
    }

    componentDidMount() {
        this.updateAll();
    }

    render() {
        const allMenus = [];
        this.props.menus.forEach((element) => {
            allMenus.push(<Button
                variant="contained"
                key={`btn_${element.url}`}
                style={{
                    height: "200px",
                    width: "200px",
                    margin: "10px",
                    textAlign: "center",
                    fontSize: "x-large",
                }}
                href={`#${element.url}`}
            >
                {" "}
                {element.name}
                {" "}
            </Button>);
        });
        const breads = Utils.getBreadcrumbsList(this.props.breadcrumbs, this.props.useDarkTheme)
        if (this.props.breadcrumbs.length === 0) {
            breads.push(<Link underline="hover" color={this.props.useDarkTheme ? "inherit" : "white"} key="/root"
                              href="#/">
                Home
            </Link>);
        }
        let separator;
        if (this.props.useDarkTheme) {
            separator = <NavigateNextIcon fontSize="small"/>
        } else {
            separator = <NavigateNextIcon fontSize="small" style={{color: "white"}}/>
        }
        const allAlerts = this.props.alerts;
        return (
            <div>
                <AlertList
                    showIcon={false}
                    position="top-right"
                    alerts={allAlerts}
                    timeout={1500}
                    dismissTitle="Close"
                    onDismiss={this.onAlertDismissed.bind(this)}
                />
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
                {allMenus}
            </div>
        );
    }
}

export const MenuForm = connect((state) => ({
    breadcrumbs: state.menuReducer.breadcrumbs,
    menus: state.menuReducer.menus,
    alerts: state.menuReducer.alerts,
    useDarkTheme: state.universalListReducer.useDarkTheme
}), (dispatch) => ({
    getBreadcrumbs: bindActionCreators(MenuActions.getBreadcrumbs, dispatch),
    getNextLevelMenus: bindActionCreators(MenuActions.getNextLevelMenus, dispatch),
    logout: bindActionCreators(MenuActions.logout, dispatch),
    dismissAlert: bindActionCreators(MenuActions.dismissAlert, dispatch),
}), null, {withRef: true})(MenuFormInner);