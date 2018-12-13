import React from 'react';
import ReactDOM from 'react-dom';
import {createStore, combineReducers, applyMiddleware, compose} from 'redux';
import {Provider} from 'react-redux';
import thunkMiddleware from 'redux-thunk';
import { Route, Switch} from 'react-router';
import { HashRouter } from 'react-router-dom';
import {routerReducer} from 'react-router-redux';
import universalListReducer from './Pages/UniversalListReducers';
import menuReducer from './Pages/MenuFormReducers';
import UniversalListForm from './Pages/UniversalListForm';
import MenuForm from './Pages/MenuForm'

const reducer = combineReducers({universalListReducer,  menuReducer,  routing: routerReducer});
const store = createStore(reducer, compose(applyMiddleware(thunkMiddleware), window.devToolsExtension ? window.devToolsExtension() : f => f));

export default class Index extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Provider store={store}>
                <HashRouter>

                    <Route path='/'>
                        <div>
                            <Switch>
                                <Route path='/lastLevel'   component={UniversalListForm}       exact   />
                                <Route path='/'        component={MenuForm}           />
                            </Switch>
                        </div>
                    </Route>
                </HashRouter>
            </Provider>
        )
    }
}

ReactDOM.render(<Index/>, document.getElementById('application'));