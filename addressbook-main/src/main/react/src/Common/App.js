import React from "react";
import {createTheme, ThemeProvider} from "@mui/material/styles";
import CssBaseline from "@mui/material/CssBaseline";
import {connect} from "react-redux";
import {purple} from "@mui/material/colors";

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
        return (<ThemeProvider theme={this.props.useDarkTheme ? darkTheme : lightTheme}>
            <CssBaseline/>
            {this.props.children}
        </ThemeProvider>);
    }
}

export const App = connect((state) => ({
    useDarkTheme: state.universalListReducer.useDarkTheme
}), null, null, {withRef: true})(AppInner);