import {SUCCESS} from './FormConstants';
import * as da from './DetailedComponentActions';

const initialState = {
    contactList: {
        data: []
    },
};

export default function detailedComponentReducers(state = initialState, action = {}) {
    switch (action.type) {
        case da.GET_CONTACT_LIST + SUCCESS:
            return Object.assign({}, state, {
                contactList: action.data
            });
        default:
            return state;
    }
}