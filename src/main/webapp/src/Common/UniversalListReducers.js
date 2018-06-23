import * as types from './FormConstants';
import * as tableActions from '../Table/TableActions';
import {Caches} from '../Table/Enums';

const initialState = {
    tableDataOrganization: {
        data: []
    },
    fieldDescriptionMapOrganization: {},
    totalDataSizeOrganization: 0,
    tableDataPerson: {
        data: []
    },
    fieldDescriptionMapPerson: {},
    totalDataSizePerson: 0,

    selectedRowsPerson: [],
    selectedRowsOrganization: []
};

export default function universalListReducer(state = initialState, action = {}) {
    switch (action.type) {
        case types.GET_LIST + Caches.ORGANIZATION_CACHE + types.SUCCESS:
            return Object.assign({}, state, {
                tableDataOrganization: action.data,
                fieldDescriptionMapOrganization: action.fieldDescriptionMap,
                totalDataSizeOrganization: action.data.totalDataSize
        });
        case types.GET_LIST + Caches.PERSON_CACHE + types.SUCCESS:
            return Object.assign({}, state, {
                tableDataPerson: action.data,
                fieldDescriptionMapPerson: action.fieldDescriptionMap,
                totalDataSizePerson: action.data.totalDataSize
            });
        case tableActions.ON_SELECT_ROW + Caches.ORGANIZATION_CACHE:
            if (action.isSelected) {
                if (undefined === action.ctrlKey) {
                    return Object.assign({}, state, {
                        selectedRowsOrganization: [...state.selectedRowsOrganization, action.row]
                    });
                } else if (action.ctrlKey) {
                    return Object.assign({}, state, {
                        selectedRowsOrganization: [...state.selectedRowsOrganization, action.row]
                    });
                } else {
                    return Object.assign({}, state, {
                        selectedRowsOrganization: [action.row]
                    });
                }
            } else {
                return Object.assign({}, state, {
                    selectedRowsOrganization: state.selectedRowsOrganization.filter((it => it.id !== action.row.id))
                });
            }
        case tableActions.ON_SELECT_ROW + Caches.PERSON_CACHE:
            if (action.isSelected) {
                if (undefined === action.ctrlKey) {
                    return Object.assign({}, state, {
                        selectedRowsPerson: [...state.selectedRowsPerson, action.row]
                    });
                } else if (action.ctrlKey) {
                    return Object.assign({}, state, {
                        selectedRowsPerson: [...state.selectedRowsPerson, action.row]
                    });
                } else {
                    return Object.assign({}, state, {
                        selectedRowsPerson: [action.row]
                    });
                }
            } else {
                return Object.assign({}, state, {
                    selectedRowsPerson: state.selectedRowsPerson.filter((it => it.id !== action.row.id))
                });
            }
        case tableActions.ON_SELECT_ALL_ROWS_ON_CURRENT_PAGE + Caches.PERSON_CACHE:
            if (action.isSelected) {
                return Object.assign({}, state, {
                    selectedRowsPerson: [...state.selectedRowsPerson, ...action.rows]
                });
            } else {
                return Object.assign({}, state, {
                    selectedRowsPerson: state.selectedRowsPerson.filter(x => action.rows.indexOf(x) < 0)
                });
            }
        case tableActions.ON_SELECT_ALL_ROWS_ON_CURRENT_PAGE + Caches.ORGANIZATION_CACHE:
            if (action.isSelected) {
                return Object.assign({}, state, {
                    selectedRowsOrganization: [...state.selectedRowsOrganization, ...action.rows]
                });
            } else {
                return Object.assign({}, state, {
                    selectedRowsOrganization: state.selectedRowsOrganization.filter(x => action.rows.indexOf(x) < 0)
                });
            }
        default:
            return state;
    }
}