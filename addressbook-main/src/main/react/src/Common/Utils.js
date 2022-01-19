export const REQUEST = "_REQUEST";
export const SUCCESS = "_SUCCESS";
export const GET_LIST = "GET_LIST";
export const GET_MENU = "GET_MENU";
export const GET_BREADCRUMBS = "GET_BREADCRUMBS";
export const ADD_ALERT = "ADD_ALERT";
export const DISMISS_ALERT = "DISMISS_ALERT";
export const CLEAR_ALERTS = "CLEAR_ALERTS";

export var Caches = {
    ORGANIZATION_CACHE: "com.addressbook.model.Organization",
    PERSON_CACHE: "com.addressbook.model.Person",
};

export class OrgTypes {
    static getEngType = (type) => {
        if (type === "Non profit") return "0";
        if (type === "Private") return "1";
        if (type === "Government") return "2";
        if (type === "Public") return "3";
    };
    static getNumType = (type) => {
        if (type === "0") return "Non profit";
        if (type === "1") return "Private";
        if (type === "2") return "Government";
        if (type === "3") return "Public";
    };
}

export class ContactTypes {
    static getEngType = (type) => {
        if (type === "0") return "Mobile phone";
        if (type === "1") return "Home phone";
        if (type === "2") return "Address";
        if (type === "3") return "E-mail";
    };
}

export class HashUtils {
    static cleanHash = (hash) => {
        if (hash === "/root") return hash;
        if (hash.startsWith("#")) return hash.substring(1);
    };
}

export class AuthTokenUtils {
    static addAuthToken = (headers) => {
        headers.append("Authorization", "Bearer " + window.sessionStorage.getItem("auth-token"));
    };
}

export class TitleConverter {
    static prepareTitle = (field) => {
        let title = "";
        let result = field.split(/(?=[A-Z])/);
        result.forEach(function (value, index) {
            if (index === 0) title += value[0].toUpperCase() + value.substr(1) + " ";
            else title += value.toLowerCase() + " ";
        });
        return title;
    };

    static preparePlaceHolder = (title) => {
        return title[0].toLowerCase() + title.substr(1);
    };
}

export class Generator {
    static uuidv4() {
        return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (
            c
        ) {
            let r = (Math.random() * 16) | 0,
                v = c === "x" ? r : (r & 0x3) | 0x8;
            return v.toString(16);
        });
    }
}
