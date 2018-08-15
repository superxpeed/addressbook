import {SUCCESS, FAILED} from './FormConstants';
import * as url from './Url';
import {asyncCommonCatch, ifNoAuthorizedRedirect} from "./CommonActions";

export const GET_CONTACT_LIST = 'GET_CONTACT_LIST';

export function getContactList(id) {
    let isOk = false;
    return function (dispatch, getState) {
        let headers = new Headers();
        headers.append('Accept', 'application/json');
        headers.append('Content-Type', 'application/json; charset=utf-8');
        fetch(url.GET_CONTACT_LIST + "?personId=" + id, {
            method: 'post',
            headers: headers
        }).then(response => {
            ifNoAuthorizedRedirect(response);
            isOk = response.ok;
            return response.json()
        }).then(json => {
            if (isOk) {
                dispatch({
                    type: GET_CONTACT_LIST + SUCCESS,
                    data: json.data
                });
            } else {
                dispatch({type: GET_CONTACT_LIST + FAILED});
            }
        }).catch(error => {
            asyncCommonCatch(GET_CONTACT_LIST, error, dispatch)
        });
    }
}


