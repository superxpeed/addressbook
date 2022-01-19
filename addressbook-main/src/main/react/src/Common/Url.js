const cp = getContextPath();

export const GET_LIST_4_UNIVERSAL_LIST_FORM =
    cp + "rest/getList4UniversalListForm";
export const GET_CONTACT_LIST = cp + "rest/getContactList";
export const SAVE_CONTACT_LIST = cp + "rest/saveOrCreateContacts";
export const SAVE_PERSON = cp + "rest/saveOrCreatePerson";
export const SAVE_ORGANIZATION = cp + "rest/saveOrCreateOrganization";
export const GET_NEXT_LEVEL_MENUS = cp + "rest/getNextLevelMenus";
export const GET_BREADCRUMBS = cp + "rest/getBreadcrumbs";
export const LOCK_RECORD = cp + "rest/lockRecord";
export const UNLOCK_RECORD = cp + "rest/unlockRecord";
export const GET_USER_INFO = cp + "rest/getUserInfo";
export const LOGOUT = cp + "rest/logout";
export const CHECK_IF_PAGE_EXISTS = cp + "rest/checkIfPageExists";
export const AUTH = cp + "auth";

export function getContextPath() {
    return (
        window.location.pathname.substring(
            0,
            window.location.pathname.indexOf("/", 2)
        ) + "/"
    );
}
