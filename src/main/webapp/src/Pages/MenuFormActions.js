import {FAILED, SUCCESS, GET_BREADCRUMBS, GET_MENU} from '../Common/Utils';
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
            headers: headers
        }).then(response => {
            ifNoAuthorizedRedirect(response);
            isOk = response.ok;
            return response.json()
        }).then(json => {
            if (isOk) {
                dispatch({
                    type: GET_MENU + SUCCESS,
                    data: json.data
                });
            } else {
                dispatch({type: GET_MENU + FAILED});
            }
        }).catch(error => {
            asyncCommonCatch(GET_MENU, error, dispatch)
        });
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
            headers: headers
        }).then(response => {
            ifNoAuthorizedRedirect(response);
            isOk = response.ok;
            return response.json()
        }).then(json => {
            if (isOk) {
                dispatch({
                    type: GET_BREADCRUMBS + SUCCESS,
                    data: json.data
                });
            } else {
                dispatch({type: GET_BREADCRUMBS + FAILED});
            }
        }).catch(error => {
            asyncCommonCatch(GET_BREADCRUMBS, error, dispatch)
        });
    }
}