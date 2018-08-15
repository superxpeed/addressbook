import {SUCCESS, FAILED} from './FormConstants';
import * as url from './Url';
import * as tableActions from '../Table/TableActions';
import {Caches} from '../Table/Enums';

export const GET_LIST = 'GET_LIST';

export function asyncCommonCatch(action, error, dispatch) {
    console.error(error);
    dispatch({type: action + FAILED});
    const message = {title: 'Произошла ошибка в javascript: ' + error, content: error.stack};
}

export function clearPersonSelection(rows) {
    return dispatch => {
        dispatch({
            type: tableActions.ON_SELECT_ROW + Caches.PERSON_CACHE,
            row: rows,
            isSelected: false
        })
    }
}

export function getList(url, filterDto = null, cacheName) {
    let isOk = false;
    return function (dispatch, getState) {
        let headers = new Headers();
        headers.append('Accept', 'application/json');
        headers.append('Content-Type', 'application/json; charset=utf-8');

        fetch(url, {
            method: 'post',
            headers: headers,
            body: JSON.stringify(filterDto),
        }).then(response => {
            ifNoAuthorizedRedirect(response);
            isOk = response.ok;
            return response.json()
        }).then(json => {
            if (isOk) {
                dispatch({
                    type: GET_LIST + cacheName + SUCCESS,
                    data: json.data,
                    fieldDescriptionMap: json.fieldDescriptionMap,
                });
            } else {
                dispatch({type: GET_LIST + FAILED});
            }
        }).catch(error => {
            asyncCommonCatch(GET_LIST, error, dispatch)
        });
    }
}

export function ifNoAuthorizedRedirect(response) {
    let loginPage = response.headers.get('loginPage');
    if (response.status == 401 && loginPage) {
        window.location.href = url.getContextPath() + loginPage;
    }
}
