import React from "react";
import {createTheme, ThemeProvider} from "@mui/material/styles";
import CssBaseline from "@mui/material/CssBaseline";
import {connect} from "react-redux";
import {purple} from "@mui/material/colors";
import {Alert, AlertTitle, Collapse, Drawer} from "@mui/material";
import * as CommonActions from "../Pages/ListActions";
import DeleteForeverOutlinedIcon from "@mui/icons-material/DeleteForeverOutlined";
import * as MenuActions from "../Pages/MenuFormActions";
import {bindActionCreators} from "redux";
import Button from "@mui/material/Button";
import {TransitionGroup} from "react-transition-group";

const darkTheme = createTheme({
    palette: {
        mode: "dark",
        topButtonColor: {
            main: "#90caf9"
        },
        topServiceButtonColor: {
            main: "#90caf9"
        }
    },
});

const lightTheme = createTheme({
    palette: {
        primary: {
            main: purple[500]
        },
        topButtonColor: {
            main: purple[700]
        },
        topServiceButtonColor: {
            main: "#FFFFFF"
        }
    },
});

export class AppInner extends React.Component {
    render() {
        let currentAlerts = []
        this.props.alerts.forEach(alert => {
            let targetMessage = alert.message;
            if (targetMessage == null || targetMessage.trim().length === 0) {
                targetMessage = null;
            } else {
                if (targetMessage.length > 300) targetMessage = targetMessage.substring(0, 300)
            }
            currentAlerts.push(
                <Collapse key={alert.id}>
                    <Alert variant="filled"
                           severity={alert.type}
                           sx={{width: "600px", marginLeft: "10px", marginRight: "10px", marginTop: "5px"}}
                           onClose={() => this.props.dismissAlert(alert)}>
                        <AlertTitle>{alert.headline}</AlertTitle>
                        {targetMessage}
                    </Alert>
                </Collapse>
            )
        })
        return (<ThemeProvider theme={this.props.useDarkTheme ? darkTheme : lightTheme}>
            <CssBaseline>
                {this.props.children}
                <React.Fragment key="drawer_right">
                    <Drawer
                        anchor="right"
                        open={this.props.drawerOpened}
                        onClose={() => this.props.openCloseDrawer(false)}
                    >
                        <Button sx={{
                            width: "600px",
                            marginLeft: "10px",
                            marginRight: "10px",
                            marginTop: "10px",
                            height: "56px",
                            minHeight: "56px"
                        }}
                                onClick={() => this.props.clearAlerts()}
                                startIcon={<DeleteForeverOutlinedIcon/>}
                                variant="contained">
                            Clear all notifications
                        </Button>
                        <TransitionGroup>
                            {currentAlerts}
                        </TransitionGroup>
                    </Drawer>
                </React.Fragment>
            </CssBaseline>
        </ThemeProvider>);
    }
}

export const App = connect((state) => ({
    useDarkTheme: state.listReducer.useDarkTheme,
    drawerOpened: state.listReducer.drawerOpened,
    alerts: state.menuReducer.alerts
}), (dispatch) => ({
    openCloseDrawer: bindActionCreators(CommonActions.openCloseDrawer, dispatch),
    dismissAlert: bindActionCreators(MenuActions.dismissAlert, dispatch),
    clearAlerts: bindActionCreators(MenuActions.clearAlerts, dispatch)
}), null, {withRef: true})(AppInner);