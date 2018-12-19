import * as types from '../Common/Utils';

const initialState = {
    breadcrumbs: [],
    menus: []
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
        default:
            return state;
    }
}