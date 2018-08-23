import {FormControl, FormGroup, ControlLabel, Button} from 'react-bootstrap'
import update from 'react-addons-update'
import React from 'react';
import {ContactContainer} from './ContactContainer'
import {ifNoAuthorizedRedirect} from "./CommonActions";
import * as url from "./Url";

export class DetailedComponent extends React.Component {

    state = {
        person: {},
        contactList: {
            data: []
        }
    };

    handleChange(e, v) {
        this.setState(update(this.state, {person: {[e]: {$set:v.currentTarget.value}}}));
    }

    constructor(props) {
        super(props);
        this.state = {
            person: props.person,
            contactList: {
                data: []
            }
        }
    }

    clearContactList = () => {
        this.setState(update(this.state, {contactList: {data: {$set:undefined}}}));
    };

    getContactList = (id) => {
        let isOk = false;
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
                this.setState({contactList: json.data});
            }
        })
    };

    savePerson = () => {
        let savedPerson;
        let isOk = false;
        let headers = new Headers();
        headers.append('Accept', 'application/json');
        headers.append('Content-Type', 'application/json; charset=utf-8');
        fetch(url.SAVE_PERSON, {
            method: 'post',
            headers: headers,
            body: JSON.stringify(this.state.person)
        }).then(response => {
            ifNoAuthorizedRedirect(response);
            isOk = response.ok;
            return response.json()
        }).then(json => {
            if (isOk) {
                savedPerson = json.data;
                fetch(url.SAVE_CONTACT_LIST, {
                    method: 'post',
                    headers: headers,
                    body: this.container.getJson()
                }).then(response => {
                    ifNoAuthorizedRedirect(response);
                    isOk = response.ok;
                    return response.json()
                }).then(json => {
                    if (isOk) {
                        if(this.props.forUpdate)
                            this.setState(update(this.state, {person: {$set: savedPerson}, contactList: {data: {$set:json.data}}}));
                        this.props.onUpdate(savedPerson);
                    }
                })
            }
        })
    };

    componentDidMount() {
        if(this.state.person['id'] !== undefined && this.props.forUpdate)
            this.getContactList(this.state.person['id']);
        else
            this.clearContactList();
    }

    getValidationState(field) {
        if(this.state.person[field] === undefined || this.state.person[field] === null) return 'error';
        const length = this.state.person[field].length;
        if (length > 10) return 'success';
        else if (length > 5) return 'warning';
        else if (length > 0) return 'error';
        return null;
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.person !== this.state.person) {
            this.getContactList(nextProps.person['id']);
            this.setState({ person: nextProps.person });
        }
    }

    render() {
        return (
            <div>
                <div style={{width: '50%', display: 'inline-block', verticalAlign: 'top'}}>
                <form>
                    <FormGroup
                        controlId='firstName'
                        validationState={this.getValidationState('firstName')}>
                        <ControlLabel>Employee first name</ControlLabel>
                        <FormControl
                            type='text'
                            value={this.state.person['firstName']}
                            placeholder='Enter first name'
                            onChange={this.handleChange.bind(this, 'firstName')}
                        />
                        <FormControl.Feedback />
                    </FormGroup>
                </form>
                <form>
                    <FormGroup
                        controlId='lastName'
                        validationState={this.getValidationState('lastName')}>
                        <ControlLabel>Employee last name</ControlLabel>
                        <FormControl
                            type='text'
                            value={this.state.person['lastName']}
                            placeholder="Enter first name"
                            onChange={this.handleChange.bind(this, 'lastName')}
                        />
                        <FormControl.Feedback />
                    </FormGroup>
                </form>
                <form>
                    <FormGroup
                        controlId='resume'
                        validationState={this.getValidationState('resume')}>
                        <ControlLabel>Employee resume</ControlLabel>
                        <FormControl
                            componentClass='textarea'
                            value={this.state.person['resume']}
                            placeholder='Enter resume'
                            onChange={this.handleChange.bind(this, 'resume')}
                        />
                        <FormControl.Feedback />
                    </FormGroup>
                </form>
                <form>
                    <FormGroup
                        controlId='salary'
                        validationState={this.getValidationState('salary')}>
                        <ControlLabel>Employee resume</ControlLabel>
                        <FormControl
                            type='text'
                            value={this.state.person['salary']}
                            placeholder='Enter salary'
                            onChange={this.handleChange.bind(this, 'salary')}
                        />
                        <FormControl.Feedback />
                    </FormGroup>
                </form>
                    <Button onClick={this.savePerson}>
                        Save contacts
                    </Button>
                </div>
                <div style={{width: 'calc(50% - 10px)', display: 'inline-block', verticalAlign: 'top',marginTop: '35px', marginLeft: '5px', marginRight: '5px'}}>
                    <ContactContainer personId={this.state.person['id']} ref={(input) => { this.container = input; if(input !== null) input.addFullContact(this.state.contactList.data);}}/>
                </div>
            </div>
        );
    }
}