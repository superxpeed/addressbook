import * as types from '../Common/Utils';
import {COMMON_ERROR} from '../Common/Utils';
import {DISMISS_ALERT} from '../Common/Utils';

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
        case COMMON_ERROR: {
            const newAlert = {
                id: (new Date()).getTime(),
                type: 'danger',
                headline: 'Error occurred!',
                message: action.alert
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
        default:
            return state;
    }
}