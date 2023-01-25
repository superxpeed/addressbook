require("../Common/style.css");

import update from "react-addons-update";
import React from "react";
import {ContactContainer} from "./ContactContainer";
import {ifNoAuthorizedRedirect} from "../Pages/UniversalListActions";
import * as url from "../Common/Url";
import {AuthTokenUtils, Caches, currencies} from "../Common/Utils";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import * as MenuActions from "../Pages/MenuFormActions";
import {FormControl, InputLabel, MenuItem, Select, TextField} from "@mui/material";
import {ContentState, convertFromHTML, convertToRaw} from "draft-js"
import {stateToHTML} from "draft-js-export-html"
import Button from "@mui/material/Button";
import MUIRichTextEditor from "mui-rte";

export class PersonComponentRaw extends React.Component {
    state = {
        locked: true,
    };

    constructor(props) {
        super(props);
        let resume = props.person["resume"] == null ? "<div/>" : props.person["resume"];
        let salaryPerson = props.person["salary"] == null ? "" : props.person["salary"].substring(0, props.person["salary"].length - 4)
        let currencyPerson = props.person["salary"] == null ? "USD" : props.person["salary"].substring(props.person["salary"].length - 3)
        this.state = {
            person: props.person,
            contactList: {
                data: [],
            },
            invalidFields: new Set(),
            salary: salaryPerson,
            currency: currencyPerson,
            resume: this.convertHtmlToMarkUp(resume),
        };
        this.editor = React.createRef();
    }

    handleChange = (e) => {
        this.setState(update(this.state, {person: {[e.target.id]: {$set: e.target.value}}}));
    }

    clearContactList = () => {
        this.setState(update(this.state, {contactList: {data: {$set: undefined}}}));
    };

    convertHtmlToMarkUp = (sourceHtml) => {
        const contentHTML = convertFromHTML(sourceHtml)
        const state = ContentState.createFromBlockArray(contentHTML.contentBlocks, contentHTML.entityMap)
        return JSON.stringify(convertToRaw(state))
    }

    onChangeResume = (_editorState) => {
        this._editorState = _editorState
    };

    getContactList = (id) => {
        let isOk = false;
        let headers = new Headers();
        AuthTokenUtils.addAuthToken(headers);
        headers.append("Accept", "application/json");
        headers.append("Content-Type", "application/json; charset=utf-8");
        fetch(url.GET_CONTACT_LIST + "?personId=" + id, {
            method: "get", headers: headers,
        })
            .then((response) => {
                ifNoAuthorizedRedirect(response);
                isOk = response.ok;
                return response.text();
            })
            .then((text) => {
                if (isOk) {
                    this.setState({contactList: JSON.parse(text).data});
                } else {
                    this.props.showCommonErrorAlert(text);
                }
            });
    };

    saveContacts = (creation, personId, savedPerson) => {
        let isOk = false;
        let headers = new Headers();
        AuthTokenUtils.addAuthToken(headers);
        headers.append("Accept", "application/json");
        headers.append("Content-Type", "application/json; charset=utf-8");
        fetch(url.SAVE_CONTACT_LIST + "?personId=" + personId, {
            method: "post", headers: headers, body: this.container.getJson(),
        })
            .then((response) => {
                ifNoAuthorizedRedirect(response);
                isOk = response.ok;
                return response.text();
            })
            .then((text) => {
                if (isOk) {
                    if (this.props.forUpdate) this.setState(update(this.state, {
                        person: {$set: savedPerson}, contactList: {data: {$set: JSON.parse(text).data}},
                    }));
                    if (creation) {
                        this.props.lockUnlockRecord(Caches.PERSON_CACHE, personId, "unlock", this.props.showNotification);
                        this.props.showCommonAlert("Person created!")
                    } else {
                        this.props.showCommonAlert("Changes saved!")
                    }
                    this.props.onUpdate(savedPerson);
                } else {
                    this.props.showCommonErrorAlert(text);
                }
            });
    };

    savePerson = () => {
        this.editor.current.save()
        let targetPerson = this.state.person
        targetPerson.resume = this.htmlResume
        let savedPerson;
        let creation = targetPerson.id == null;
        let headers = new Headers();
        AuthTokenUtils.addAuthToken(headers);
        headers.append("Accept", "application/json");
        headers.append("Content-Type", "application/json; charset=utf-8");
        let isOk = false;
        fetch(url.SAVE_PERSON, {
            method: "post", headers: headers, body: JSON.stringify(targetPerson),
        })
            .then((response) => {
                ifNoAuthorizedRedirect(response);
                isOk = response.ok;
                return response.text();
            })
            .then((text) => {
                if (isOk) {
                    savedPerson = JSON.parse(text).data;
                    let personId = creation ? savedPerson.id : targetPerson.id;
                    if (creation) {
                        this.props.lockUnlockRecord(Caches.PERSON_CACHE, personId, "lock", this.props.showNotification, (result) => {
                            if (result === "success") {
                                this.setState({locked: true});
                                this.saveContacts(creation, personId, savedPerson);
                            } else if (result === "warning") {
                                this.setState({locked: false});
                            }
                        });
                    } else {
                        this.saveContacts(creation, personId, savedPerson);
                    }
                } else {
                    this.props.showCommonErrorAlert(text);
                }
            });
    };

    lockCallback = (result) => {
        if (result === "success") {
            this.setState({locked: true});
        } else if (result === "warning") {
            this.setState({locked: false});
        }
    };

    componentDidMount() {
        if (this.state.person["id"] != null && this.props.forUpdate) {
            this.getContactList(this.state.person["id"]);
            this.props.lockUnlockRecord(Caches.PERSON_CACHE, this.state.person["id"], "lock", this.props.showNotification, this.lockCallback);
        } else {
            this.clearContactList();
            this.setState({locked: true});
        }
    }

    componentWillUnmount() {
        if (this.props.forUpdate) this.props.lockUnlockRecord(Caches.PERSON_CACHE, this.state.person["id"], "unlock", this.props.showNotification);
    }

    getValidationState(field) {
        if (field === "salary") {
            if (this.state.person["salary"] == null || this.state.person["salary"].length === 3 || this.state.person["salary"].length === 4) {
                this.state.invalidFields.add(field);
                return false;
            }
        } else if (this.state.person[field] == null || this.state.person[field].trim().length === 0) {
            this.state.invalidFields.add(field);
            return false;
        }
        this.state.invalidFields.delete(field);
        return true;
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.person !== prevProps.person) {
            if (this.state.person != null) {
                this.props.lockUnlockRecord(Caches.PERSON_CACHE, this.state.person["id"], "unlock", this.props.showNotification);
            }
            let newResume = this.props.person["resume"] == null ? "<div/>" : this.props.person["resume"];
            let salaryPerson = this.props.person["salary"] == null ? "" : this.props.person["salary"].substring(0, this.props.person["salary"].length - 4)
            let currencyPerson = this.props.person["salary"] == null ? "USD" : this.props.person["salary"].substring(this.props.person["salary"].length - 3)
            this.getContactList(this.props.person["id"]);
            this.setState({
                person: this.props.person,
                resume: this.convertHtmlToMarkUp(newResume),
                salary: salaryPerson,
                currency: currencyPerson
            });
            this.props.lockUnlockRecord(Caches.PERSON_CACHE, this.props.person["id"], "lock", this.props.showNotification, this.lockCallback);
        }
    }

    getCurrencyOptions() {
        let opts = []
        currencies.forEach(currency => opts.push(<MenuItem key={currency} value={currency}>{currency}</MenuItem>))
        return opts
    }

    getSalaryForm() {
        return (<div style={{height: "95px"}}>
            <div style={{width: "80%", display: "inline-block", height: "100%"}}>
                <TextField
                    error={!this.getValidationState("salary")}
                    id="salary"
                    type="text"
                    label="Enter annual salary"
                    variant="outlined"
                    autoComplete="off"
                    sx={{mt: 1, display: "flex", height: "80px"}}
                    value={this.state.salary}
                    helperText={!this.getValidationState("salary") ? "Required field!" : ""}
                    onChange={e => {
                        const re = /^[0-9\b]+$/;
                        if (e.currentTarget.value === "" || re.test(e.currentTarget.value)) {
                            let currentPerson = Object.assign({}, this.state.person)
                            currentPerson["salary"] = e.currentTarget.value + " " + this.state.currency
                            this.setState({salary: e.currentTarget.value, person: currentPerson})
                        }
                    }}
                />
            </div>
            <div style={{width: "20%", display: "inline-block", paddingLeft: "5px", height: "100%"}}>
                <FormControl fullWidth={true} sx={{mt: 1, display: "flex", height: "100%"}}>
                    <InputLabel id="currency-label">Currency</InputLabel>
                    <Select
                        labelId="currency-label"
                        id="currency"
                        name="currency"
                        value={this.state.currency}
                        label="Currency"
                        onChange={e => {
                            let currentPerson = Object.assign({}, this.state.person)
                            currentPerson["salary"] = this.state.salary + " " + e.target.value
                            this.setState({currency: e.target.value, person: currentPerson})
                        }}
                    >
                        {this.getCurrencyOptions()}
                    </Select>
                </FormControl>
            </div>
        </div>);
    }

    render() {
        return (<div>
            <div
                style={{
                    width: "50%", display: "inline-block", verticalAlign: "top", paddingLeft: "5px",
                }}
            >
                <TextField
                    error={!this.getValidationState("firstName")}
                    id="firstName"
                    type="text"
                    label="Enter first name"
                    value={this.state.person["firstName"]}
                    variant="outlined"
                    autoComplete="off"
                    sx={{mt: 1, display: "flex", height: "80px"}}
                    helperText={!this.getValidationState("firstName") ? "Required field!" : ""}
                    onChange={this.handleChange}
                />
                <TextField
                    error={!this.getValidationState("lastName")}
                    id="lastName"
                    type="text"
                    label="Enter last name"
                    value={this.state.person["lastName"]}
                    variant="outlined"
                    autoComplete="off"
                    sx={{mt: 1, display: "flex", height: "80px"}}
                    helperText={!this.getValidationState("lastName") ? "Required field!" : ""}
                    onChange={this.handleChange}
                />
                {this.getSalaryForm()}
                <MUIRichTextEditor
                    label="Resume"
                    ref={this.editor}
                    controls={["title", "bold", "italic", "underline", "strikethrough", "highlight", "undo",
                        "redo", "link", "media", "numberList", "bulletList", "quote", "code", "clear"]}
                    onChange={this.onChangeResume}
                    value={this.state.resume}
                    onSave={() => {
                        this.htmlResume = stateToHTML(this._editorState.getCurrentContent())
                    }}
                />
                <Button
                    variant="contained" sx={{mt: 4, width: "100%", height: "56px"}}
                    onClick={this.savePerson}
                    disabled={this.state.invalidFields.size !== 0 || !this.state.locked}
                >
                    Save data
                </Button>
            </div>
            <div
                style={{
                    width: "calc(50% - 10px)",
                    display: "inline-block",
                    verticalAlign: "top",
                    marginLeft: "5px",
                    marginRight: "5px",
                }}
            >
                <ContactContainer
                    personId={this.state.person["id"]}
                    ref={(input) => {
                        this.container = input;
                        if (input !== null) input.addFullContact(this.state.contactList.data);
                    }}
                />
            </div>
        </div>);
    }
}


export const PersonComponent = connect((state) => ({
    showNotification: state.universalListReducer.showNotification,
}), (dispatch) => ({
    showCommonErrorAlert: bindActionCreators(MenuActions.showCommonErrorAlert, dispatch),
    showCommonAlert: bindActionCreators(MenuActions.showCommonAlert, dispatch),
    lockUnlockRecord: bindActionCreators(MenuActions.lockUnlockRecord, dispatch),
}), null, {withRef: true})(PersonComponentRaw);