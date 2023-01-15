import {Button, ControlLabel, FormControl, FormGroup} from "react-bootstrap";
import update from "react-addons-update";
import React from "react";
import {ContactContainer} from "./ContactContainer";
import {ifNoAuthorizedRedirect} from "../Pages/UniversalListActions";
import * as url from "../Common/Url";
import {AuthTokenUtils, Caches, TitleConverter, currencies} from "../Common/Utils";
import RichTextEditor from "react-rte";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import * as MenuActions from "../Pages/MenuFormActions";

@connect(null, (dispatch) => ({
    showCommonErrorAlert: bindActionCreators(MenuActions.showCommonErrorAlert, dispatch),
    showCommonAlert: bindActionCreators(MenuActions.showCommonAlert, dispatch),
    lockUnlockRecord: bindActionCreators(MenuActions.lockUnlockRecord, dispatch),
}))
export class PersonComponent extends React.Component {
    state = {
        person: {}, contactList: {
            data: [],
        }, resume: RichTextEditor.createEmptyValue(), locked: true, salary: null, currency: null
    };

    constructor(props) {
        super(props);
        let resume = props.person["resume"] == null ? "" : props.person["resume"];
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
            resume: RichTextEditor.createValueFromString(resume, "html"),
        };
    }

    handleChange(e, v) {
        this.setState(update(this.state, {person: {[e]: {$set: v.currentTarget.value}}}));
    }

    clearContactList = () => {
        this.setState(update(this.state, {contactList: {data: {$set: undefined}}}));
    };

    onChangeResume = (value) => {
        this.setState(update(this.state, {
            person: {resume: {$set: value.toString("html")}}, resume: {$set: value},
        }));
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
                        this.props.lockUnlockRecord(Caches.PERSON_CACHE, personId, "unlock");
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
        let savedPerson;
        let creation = this.state.person.id == null;
        let headers = new Headers();
        AuthTokenUtils.addAuthToken(headers);
        headers.append("Accept", "application/json");
        headers.append("Content-Type", "application/json; charset=utf-8");
        let isOk = false;
        fetch(url.SAVE_PERSON, {
            method: "post", headers: headers, body: JSON.stringify(this.state.person),
        })
            .then((response) => {
                ifNoAuthorizedRedirect(response);
                isOk = response.ok;
                return response.text();
            })
            .then((text) => {
                if (isOk) {
                    savedPerson = JSON.parse(text).data;
                    let personId = creation ? savedPerson.id : this.state.person.id;
                    if (creation) {
                        this.props.lockUnlockRecord(Caches.PERSON_CACHE, personId, "lock", (result) => {
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
            this.props.lockUnlockRecord(Caches.PERSON_CACHE, this.state.person["id"], "lock", this.lockCallback);
        } else {
            this.clearContactList();
            this.setState({locked: true});
        }
    }

    componentWillUnmount() {
        if (this.props.forUpdate) this.props.lockUnlockRecord(Caches.PERSON_CACHE, this.state.person["id"], "unlock");
    }

    getValidationState(field) {
        if (field === "salary") {
            if (this.state.person["salary"] == null || this.state.person["salary"].length === 3 || this.state.person["salary"].length === 4) {
                this.state.invalidFields.add(field);
                return "error";
            }
        } else if (this.state.person[field] == null || this.state.person[field].length === 0) {
            this.state.invalidFields.add(field);
            return "error";
        }
        this.state.invalidFields.delete(field);
        return "success";
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.person !== this.state.person) {
            if (this.state.person != null) {
                this.props.lockUnlockRecord(Caches.PERSON_CACHE, this.state.person["id"], "unlock");
            }
            let newResume = nextProps.person["resume"] == null ? "" : nextProps.person["resume"];
            let salaryPerson = nextProps.person["salary"] == null ? "" : nextProps.person["salary"].substring(0, nextProps.person["salary"].length - 4)
            let currencyPerson = nextProps.person["salary"] == null ? "USD" : nextProps.person["salary"].substring(nextProps.person["salary"].length - 3)
            this.getContactList(nextProps.person["id"]);
            this.setState({
                person: nextProps.person,
                resume: RichTextEditor.createValueFromString(newResume, "html"),
                salary: salaryPerson,
                currency: currencyPerson
            });
            this.props.lockUnlockRecord(Caches.PERSON_CACHE, nextProps.person["id"], "lock", this.lockCallback);
        }
    }

    getFieldFormControl(field, fieldType) {
        if (fieldType === "text") {
            return (<FormControl
                type="text"
                value={this.state.person[field]}
                placeholder={"Enter " + TitleConverter.preparePlaceHolder(TitleConverter.prepareTitle(field))}
                onChange={this.handleChange.bind(this, field)}
            />);
        }
    }

    getFieldForm(field, fieldType) {
        return (<form>
            <FormGroup
                controlId={field}
                validationState={this.getValidationState(field)}
            >
                <ControlLabel>{TitleConverter.prepareTitle(field)}</ControlLabel>
                {this.getFieldFormControl(field, fieldType)}
                <FormControl.Feedback/>
            </FormGroup>
        </form>);
    }

    getCurrencyOptions() {
        let opts = []
        currencies.forEach(currency => opts.push(<option value={currency}>{currency}</option>))
        return opts
    }

    getSalaryForm() {
        return (<div>
            <form style={{width: "80%", display: "inline-block"}}>
                <FormGroup
                    controlId="salary"
                    validationState={this.getValidationState("salary")}>
                    <ControlLabel>Annual salary</ControlLabel>
                    <FormControl
                        type="text"
                        value={this.state.salary}
                        placeholder="Enter annual salary"
                        onChange={e => {
                            const re = /^[0-9\b]+$/;
                            if (e.currentTarget.value === '' || re.test(e.currentTarget.value)) {
                                let currentPerson = Object.assign({}, this.state.person)
                                currentPerson["salary"] = e.currentTarget.value + " " + this.state.currency
                                this.setState({salary: e.currentTarget.value, person: currentPerson})
                            }
                        }}/>
                    <FormControl.Feedback/>
                </FormGroup>
            </form>
            <form style={{width: "20%", display: "inline-block", paddingLeft: "5px"}}>
                <FormGroup
                    controlId="currency">
                    <ControlLabel>Currency</ControlLabel>
                    <FormControl componentClass="select"
                                 value={this.state.currency}
                                 placeholder="Enter salary currency"
                                 onChange={e => {
                                     let currentPerson = Object.assign({}, this.state.person)
                                     currentPerson["salary"] = this.state.salary + " " + e.currentTarget.value
                                     this.setState({currency: e.currentTarget.value, person: currentPerson})
                                 }}>
                        {this.getCurrencyOptions()}
                    </FormControl>
                    <FormControl.Feedback/>
                </FormGroup>
            </form>
        </div>);
    }

    render() {
        return (<div>
            <div
                style={{
                    width: "50%", display: "inline-block", verticalAlign: "top", paddingLeft: "5px",
                }}
            >
                {this.getFieldForm("firstName", "text")}
                {this.getFieldForm("lastName", "text")}
                {this.getSalaryForm()}
                <RichTextEditor
                    placeholder="Resume"
                    editorStyle={{minHeight: 220}}
                    value={this.state.resume}
                    onChange={this.onChangeResume}
                />
                <Button
                    onClick={this.savePerson}
                    disabled={this.state.invalidFields.size !== 0 || !this.state.locked}
                >
                    Save contacts
                </Button>
            </div>
            <div
                style={{
                    width: "calc(50% - 10px)",
                    display: "inline-block",
                    verticalAlign: "top",
                    marginTop: "35px",
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
