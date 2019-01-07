import {FormControl, FormGroup, ControlLabel, Button} from 'react-bootstrap'
import update from 'react-addons-update'
import React from 'react';
import {ContactContainer} from './ContactContainer'
import {ifNoAuthorizedRedirect} from '../Pages/UniversalListActions';
import * as url from '../Common/Url';
import {Caches, TitleConverter} from '../Common/Utils';
import RichTextEditor from 'react-rte';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as MenuActions from '../Pages/MenuFormActions';

@connect(
    null,
    dispatch => ({
        showCommonErrorAlert: bindActionCreators(MenuActions.showCommonErrorAlert, dispatch),
        lockUnlockRecord: bindActionCreators(MenuActions.lockUnlockRecord, dispatch)
    })
)
export class PersonComponent extends React.Component {

    state = {
        person: {},
        contactList: {
            data: []
        },
        resume: RichTextEditor.createEmptyValue(),
        locked: true
    };

    handleChange(e, v) {
        this.setState(update(this.state, {person: {[e]: {$set:v.currentTarget.value}}}));
    }

    constructor(props) {
        super(props);
        let resume = props.person['resume'] === undefined ? '' : props.person['resume'];
        this.state = {
            person: props.person,
            contactList: {
                data: []
            },
            invalidFields: new Set(),
            resume: RichTextEditor.createValueFromString(resume, 'html')
        };
    }

    clearContactList = () => {
        this.setState(update(this.state, {contactList: {data: {$set:undefined}}}));
    };

    onChangeResume = (value) => {
        this.setState(update(this.state, {person: {resume: {$set:value.toString('html')}}, resume: {$set:value}}));
    };

    getContactList = (id) => {
        let isOk = false;
        let headers = new Headers();
        headers.append('Accept', 'application/json');
        headers.append('Content-Type', 'application/json; charset=utf-8');
        fetch(url.GET_CONTACT_LIST + '?personId=' + id, {
            method: 'post',
            credentials: 'include',
            headers: headers
        }).then(response => {
            ifNoAuthorizedRedirect(response);
            isOk = response.ok;
            return response.text()
        }).then(text => {
            if (isOk) {
                this.setState({contactList: JSON.parse(text).data});
            }else {
                this.props.showCommonErrorAlert(text);
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
            credentials: 'include',
            headers: headers,
            body: JSON.stringify(this.state.person)
        }).then(response => {
            ifNoAuthorizedRedirect(response);
            isOk = response.ok;
            return response.text()
        }).then(text => {
            if (isOk) {
                savedPerson = JSON.parse(text).data;
                fetch(url.SAVE_CONTACT_LIST, {
                    method: 'post',
                    credentials: 'include',
                    headers: headers,
                    body: this.container.getJson()
                }).then(response => {
                    ifNoAuthorizedRedirect(response);
                    isOk = response.ok;
                    return response.text()
                }).then(text => {
                    if (isOk) {
                        if(this.props.forUpdate)
                            this.setState(update(this.state, {person: {$set: savedPerson}, contactList: {data: {$set:JSON.parse(text).data}}}));
                        this.props.onUpdate(savedPerson);
                    }else {
                        this.props.showCommonErrorAlert(text);
                    }
                })
            }else {
                this.props.showCommonErrorAlert(text);
            }
        })
    };

    lockCallback = (result) => {
        if(result === 'success'){
            this.setState({locked: true});
        }else if(result === 'warning'){
            this.setState({locked: false});
        }
    };

    componentDidMount() {
        if(this.state.person['id'] !== undefined && this.props.forUpdate) {
            this.getContactList(this.state.person['id']);
            this.props.lockUnlockRecord(Caches.PERSON_CACHE, this.state.person['id'], 'lock', this.lockCallback);
        }else
            this.clearContactList();
    }

    componentWillUnmount() {
        this.props.lockUnlockRecord(Caches.PERSON_CACHE, this.state.person['id'], 'unlock');
    }

    getValidationState(field) {
        if(this.state.person[field] === undefined || this.state.person[field] === null) {
            this.state.invalidFields.add(field);
            return 'error';
        }
        const length = this.state.person[field].length;
        if (length > 10) {
            this.state.invalidFields.delete(field);
            return 'success';
        } else if (length > 5) {
            this.state.invalidFields.delete(field);
            return 'warning';
        } else if (length >= 0) {
            this.state.invalidFields.add(field);
            return 'error';
        }
        return null;
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.person !== this.state.person) {
            this.getContactList(nextProps.person['id']);
            this.setState({ person: nextProps.person, resume: RichTextEditor.createValueFromString(nextProps.person['resume'], 'html') });
        }
    }

    getFieldFormControl(field, fieldType){
        if(fieldType === 'text'){
            return  <FormControl
                type='text'
                value={this.state.person[field]}
                placeholder={'Enter ' + TitleConverter.preparePlaceHolder(TitleConverter.prepareTitle(field))}
                onChange={this.handleChange.bind(this, field)}
            />;
        }
    }

    getFieldForm(field, fieldType){
        return <form>
            <FormGroup
                controlId={field}
                validationState={this.getValidationState(field)}>
                <ControlLabel>{TitleConverter.prepareTitle(field)}</ControlLabel>
                {this.getFieldFormControl(field, fieldType)}
                <FormControl.Feedback />
            </FormGroup>
        </form>;
    }

    render() {
        return (
            <div>
                <div style={{width: '50%', display: 'inline-block', verticalAlign: 'top', paddingLeft: '5px'}}>
                    {this.getFieldForm('firstName', 'text')}
                    {this.getFieldForm('lastName', 'text')}
                    {this.getFieldForm('salary', 'text')}
                    <RichTextEditor
                        placeholder='Resume'
                        editorStyle={{minHeight: 220 }}
                        value={this.state.resume}
                        onChange={this.onChangeResume}
                    />
                    <Button onClick={this.savePerson} disabled={this.state.invalidFields.size !== 0 || !this.state.locked}>
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