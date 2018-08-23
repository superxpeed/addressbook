export const ON_SELECT_ROW = 'ON_SELECT_ROW';
export const ON_SELECT_ALL_ROWS_ON_CURRENT_PAGE = 'ON_SELECT_ALL_ROWS_ON_CURRENT_PAGE';
export const UPDATE_ROW_IN_TABLE = 'UPDATE_ROW_IN_TABLE';
export const ADD_ROW_TO_TABLE = 'ADD_ROW_TO_TABLE';

export function onSelectRow(row, isSelected, e = {}, cacheName) {
    return dispatch => {
        dispatch({
            type: ON_SELECT_ROW + cacheName,
            row: row,
            isSelected: isSelected,
            ctrlKey: e.ctrlKey
        })
    }
}

export function updateRow(row, cacheName) {
    return dispatch => {
        dispatch({
            type: UPDATE_ROW_IN_TABLE + cacheName,
            row: row
        })
    }
}

export function addRow(row, cacheName) {
    return dispatch => {
        dispatch({
            type: ADD_ROW_TO_TABLE + cacheName,
            row: row
        })
    }
}

export function onSelectAllRowsOnCurrentPage(isSelected, rows, cacheName) {
    return dispatch => {
        dispatch({
            type: ON_SELECT_ALL_ROWS_ON_CURRENT_PAGE + cacheName,
            isSelected: isSelected,
            rows: rows
        })
    }
}