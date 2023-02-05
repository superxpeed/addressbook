const cp: string = getContextPath();

export const GET_LIST: string = `${cp}rest/getList`;
export const GET_CONTACT_LIST: string = `${cp}rest/getContactList`;
export const SAVE_CONTACT_LIST: string = `${cp}rest/saveOrCreateContacts`;
export const SAVE_PERSON: string = `${cp}rest/saveOrCreatePerson`;
export const SAVE_ORGANIZATION: string = `${cp}rest/saveOrCreateOrganization`;
export const GET_NEXT_LEVEL_MENUS: string = `${cp}rest/getNextLevelMenus`;
export const GET_BREADCRUMBS: string = `${cp}rest/getBreadcrumbs`;
export const LOCK_RECORD: string = `${cp}rest/lockRecord`;
export const UNLOCK_RECORD: string = `${cp}rest/unlockRecord`;
export const GET_USER_INFO: string = `${cp}rest/getUserInfo`;
export const GET_BUILD_INFO: string = `${cp}rest/getBuildInfo`;
export const LOGOUT: string = `${cp}rest/logout`;
export const CHECK_IF_PAGE_EXISTS: string = `${cp}rest/checkIfPageExists`;
export const AUTH: string = `${cp}auth`;

export function getContextPath(): string {
    return (`${window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2))}/`);
}
