import {ADD_ALERT, CLEAR_ALERTS, DISMISS_ALERT, GET_BREADCRUMBS, GET_MENU, SUCCESS} from "../Common/Utils";
import {asyncCommonCatch, ifNoAuthorizedRedirect,} from "./UniversalListActions";
import * as url from "../Common/Url";

export function getNextLevelMenus(currentUrl) {
    let isOk = false;
    return function (dispatch) {
        let headers = new Headers();
        let token = window.sessionStorage.getItem("auth-token");
        headers.append("Accept", "application/json");
        headers.append("Content-Type", "application/json; charset=utf-8");
        headers.append("Authorization", "Bearer " + token);
        fetch(url.GET_NEXT_LEVEL_MENUS + "?currentUrl=" + currentUrl, {
            method: "get",
            headers: headers,
        })
            .then((response) => {
                ifNoAuthorizedRedirect(response);
                isOk = response.ok;
                return response.text();
            })
            .then((text) => {
                if (isOk) {
                    dispatch({
                        type: GET_MENU + SUCCESS,
                        data: JSON.parse(text).data,
                    });
                } else {
                    dispatch({
                        type: ADD_ALERT,
                        alert: JSON.parse(text),
                    });
                }
            })
            .catch((error) => {
                asyncCommonCatch(GET_MENU, error, dispatch);
            });
    };
}

export function showCommonErrorAlert(text) {
    return (dispatch) => {
        dispatch({
            type: ADD_ALERT,
            alert: JSON.parse(text),
        });
    };
}

export function getBreadcrumbs(currentUrl) {
    let isOk = false;
    return function (dispatch) {
        let headers = new Headers();
        let token = window.sessionStorage.getItem("auth-token");
        headers.append("Accept", "application/json");
        headers.append("Content-Type", "application/json; charset=utf-8");
        headers.append("Authorization", "Bearer " + token);
        fetch(url.GET_BREADCRUMBS + "?currentUrl=" + currentUrl, {
            method: "get",
            headers: headers,
        })
            .then((response) => {
                ifNoAuthorizedRedirect(response);
                isOk = response.ok;
                return response.text();
            })
            .then((text) => {
                if (isOk) {
                    dispatch({
                        type: GET_BREADCRUMBS + SUCCESS,
                        data: JSON.parse(text).data,
                    });
                } else {
                    dispatch({
                        type: ADD_ALERT,
                        alert: JSON.parse(text),
                    });
                }
            })
            .catch((error) => {
                asyncCommonCatch(GET_BREADCRUMBS, error, dispatch);
            });
    };
}

export function lockUnlockRecord(type, id, action, callback) {
    let targetUrl = "";
    let isOk = false;
    if (action === "lock") {
        targetUrl = url.LOCK_RECORD;
    } else if (action === "unlock") {
        targetUrl = url.UNLOCK_RECORD;
    }
    return function (dispatch) {
        let headers = new Headers();
        let token = window.sessionStorage.getItem("auth-token");
        headers.append("Authorization", "Bearer " + token);
        fetch(targetUrl + "?type=" + type + "&id=" + id, {
            method: "get",
            headers: headers,
        })
            .then((response) => {
                ifNoAuthorizedRedirect(response);
                isOk = response.ok;
                return response.text();
            })
            .then((text) => {
                if (isOk) {
                    dispatch({
                        type: ADD_ALERT,
                        alert: JSON.parse(text).data,
                    });
                    if (callback) callback(JSON.parse(text).data.type);
                } else {
                    dispatch({
                        type: ADD_ALERT,
                        alert: JSON.parse(text),
                    });
                }
            })
            .catch((error) => {
                asyncCommonCatch(GET_BREADCRUMBS, error, dispatch);
            });
    };
}

export function logout() {
    let isOk = false;
    return function () {
        let headers = new Headers();
        let token = window.sessionStorage.getItem("auth-token");
        headers.append("Authorization", "Bearer " + token);
        fetch(url.LOGOUT, {
            method: "get",
            headers: headers,
        }).then((response) => {
            isOk = response.ok;
            if (isOk) {
                window.sessionStorage.clear();
                window.location.hash = "#/login";
            }
        });
    };
}

export function dismissAlert(alert) {
    return (dispatch) => {
        dispatch({
            type: DISMISS_ALERT,
            alert: alert,
        });
    };
}

export function clearAlerts() {
    return (dispatch) => {
        dispatch({
            type: CLEAR_ALERTS,
        });
    };
}
