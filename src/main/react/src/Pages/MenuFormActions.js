import {SUCCESS, GET_BREADCRUMBS, GET_MENU, DISMISS_ALERT, COMMON_ERROR} from '../Common/Utils';
import {asyncCommonCatch, ifNoAuthorizedRedirect} from './UniversalListActions';
import * as url from '../Common/Url';

export function getNextLevelMenus(currentUrl) {
    let isOk = false;
    return function (dispatch) {
        let headers = new Headers();
        headers.append('Accept', 'application/json');
        headers.append('Content-Type', 'application/json; charset=utf-8');
        fetch(url.GET_NEXT_LEVEL_MENUS + '?currentUrl=' + currentUrl, {
            method: 'get',
            credentials: 'include',
            headers: headers
        }).then(response => {
            ifNoAuthorizedRedirect(response);
            isOk = response.ok;
            return response.text()
        }).then(text => {
            if (isOk) {
                dispatch({
                    type: GET_MENU + SUCCESS,
                    data: JSON.parse(text).data
                });
            }else {
                dispatch({
                    type: COMMON_ERROR,
                    alert: text
                });
            }
        }).catch(error => {
            asyncCommonCatch(GET_MENU, error, dispatch)
        });
    }
}

export function showCommonErrorAlert(text){
    return dispatch => {
        dispatch({
            type: COMMON_ERROR,
            alert: text
        })
    }
}

export function getBreadcrumbs(currentUrl) {
    let isOk = false;
    return function (dispatch) {
        let headers = new Headers();
        headers.append('Accept', 'application/json');
        headers.append('Content-Type', 'application/json; charset=utf-8');
        fetch(url.GET_BREADCRUMBS + '?currentUrl=' + currentUrl, {
            method: 'get',
            credentials: 'include',
            headers: headers
        }).then(response => {
            ifNoAuthorizedRedirect(response);
            isOk = response.ok;
            return response.text()
        }).then(text => {
            if (isOk) {
                dispatch({
                    type: GET_BREADCRUMBS + SUCCESS,
                    data: JSON.parse(text).data
                });
            }else {
                dispatch({
                    type: COMMON_ERROR,
                    alert: text
                });
            }
        }).catch(error => {
            asyncCommonCatch(GET_BREADCRUMBS, error, dispatch)
        });
    }
}

export function logout() {
    let isOk = false;
    return function () {
        let headers = new Headers();
        fetch(url.LOGOUT, {
            method: 'get',
            credentials: 'include',
            headers: headers
        }).then(response => {
            isOk = response.ok;
            if (isOk) {
                window.location.hash = '#/login';
            }
        });
    }
}

export function dismissAlert(alert) {
    return dispatch => {
        dispatch({
            type: DISMISS_ALERT,
            alert: alert
        })
    }
}



