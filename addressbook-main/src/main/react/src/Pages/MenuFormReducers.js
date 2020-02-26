import * as types from '../Common/Utils';
import {ADD_ALERT, CLEAR_ALERTS, DISMISS_ALERT} from '../Common/Utils';

const initialState = {
    breadcrumbs: [],
    menus: [],
    alerts: []
};

export default function menuReducer(state = initialState, action = {}) {
    switch (action.type) {
        case types.GET_MENU + types.SUCCESS:
            return Object.assign({}, state, {
                menus: action.data
            });
        case types.GET_BREADCRUMBS + types.SUCCESS:
            return Object.assign({}, state, {
                breadcrumbs: action.data
            });
        case ADD_ALERT: {
            const newAlert = {
                id: (new Date()).getTime(),
                type: action.alert.type,
                headline: action.alert.headline,
                message: action.alert.message
            };
            return Object.assign({}, state, {
                alerts: [...state.alerts, newAlert]
            });
        }
        case DISMISS_ALERT: {
            return Object.assign({}, state, {
                alerts: state.alerts.filter((it => it.id !== action.alert.id))
            });
        }
        case CLEAR_ALERTS: {
            return Object.assign({}, state, {
                alerts: []
            });
        }
        default:
            return state;
    }
}