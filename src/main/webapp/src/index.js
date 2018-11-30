import React from 'react';
import ReactDOM from 'react-dom';
import {createStore, combineReducers, applyMiddleware, compose} from 'redux';
import {Provider} from 'react-redux';
import thunkMiddleware from 'redux-thunk';
import {Router, Route, hashHistory} from 'react-router';
import {syncHistoryWithStore, routerReducer, routerMiddleware} from 'react-router-redux';
import universalListReducer from './Pages/UniversalListReducers';
import UniversalListForm from './Pages/UniversalListForm';

const routerMW = routerMiddleware(hashHistory);
const reducer = combineReducers({universalListReducer,  routing: routerReducer});
const store = createStore(reducer, compose(applyMiddleware(thunkMiddleware,  routerMW), window.devToolsExtension ? window.devToolsExtension() : f => f));
const history = syncHistoryWithStore(hashHistory, store);

export default class Index extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Provider store={store}>
                <Router history={history}>
                    <Route path='/' component={UniversalListForm}/>
                </Router>
            </Provider>
        )
    }
}

ReactDOM.render(<Index/>, document.getElementById('application'));