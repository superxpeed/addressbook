export const ON_SELECT_ROW = 'ON_SELECT_ROW';
export const ON_SELECT_ALL_ROWS_ON_CURRENT_PAGE = 'ON_SELECT_ALL_ROWS_ON_CURRENT_PAGE';

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
export function onSelectAllRowsOnCurrentPage(isSelected, rows, cacheName) {
    return dispatch => {
        dispatch({
            type: ON_SELECT_ALL_ROWS_ON_CURRENT_PAGE + cacheName,
            isSelected: isSelected,
            rows: rows
        })
    }
}