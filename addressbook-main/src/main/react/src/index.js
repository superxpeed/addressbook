import React from "react";
import {createRoot} from "react-dom/client";
import {applyMiddleware, combineReducers, compose, createStore,} from "redux";
import {Provider} from "react-redux";
import thunkMiddleware from "redux-thunk";
import {HashRouter, Route, Switch} from "react-router-dom";
import {routerReducer} from "react-router-redux";
import listReducer from "./Pages/ListReducers";
import menuReducer from "./Pages/MenuFormReducers";
import {ListForm} from "./Pages/ListForm";
import {MenuForm} from "./Pages/MenuForm";
import {LoginForm} from "./Pages/LoginForm";
import {AdminPageForm} from "./Pages/AdminPageForm";
import {App} from "./Common/App";

const reducer = combineReducers({listReducer, menuReducer, routing: routerReducer});
const store = createStore(reducer, compose(applyMiddleware(thunkMiddleware)));

export default class Index extends React.Component {

    render() {
        return (
            <Provider store={store}>
                <HashRouter>
                    <App>
                        <Route path="/">
                            <div>
                                <Switch>
                                    <Route path="/adminPage" component={AdminPageForm} exact/>
                                    <Route path="/lastLevel" component={ListForm} exact/>
                                    <Route path="/login" component={LoginForm} exact/>
                                    <Route path="/" component={MenuForm}/>
                                </Switch>
                            </div>
                        </Route>
                    </App>
                </HashRouter>
            </Provider>
        );
    }
}

const root = createRoot(document.getElementById("application"));
root.render(<Index/>);
