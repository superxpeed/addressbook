const cp = getContextPath();


export const GET_LIST_4_UNIVERSAL_LIST_FORM = cp + 'rest/getList4UniversalListForm';
export const GET_CONTACT_LIST = cp + 'rest/getContactList';
export const SAVE_CONTACT_LIST = cp + 'rest/saveOrCreateContacts';
export const SAVE_PERSON = cp + 'rest/saveOrCreatePerson';

export function getContextPath() {
    return window.location.pathname.substring(0, window.location.pathname.indexOf('/',2)) + '/';
}



