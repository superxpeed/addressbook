import {FormControl, FormGroup, ControlLabel} from 'react-bootstrap'
import update from 'react-addons-update'
import React from 'react';
import {ContactContainer} from './ContactContainer'
import {bindActionCreators} from 'redux';
import {connect} from "react-redux";
import * as DetailedComponentActions from "./DetailedComponentActions";

@connect(
    state => ({
        contactList: state.detailedComponentReducers.contactList,
    }),
    dispatch => ({
        getContactList: bindActionCreators(DetailedComponentActions.getContactList, dispatch),
    })
)
export class DetailedComponent extends React.Component {

    state = {
        person: {},
    };

    handleChange(e, v) {
        this.setState(update(this.state, {person: {[e]: {$set:v.currentTarget.value}}}));
    }

    constructor(props) {
        super(props);
        this.state = {
            person: props.person,
        }
    }

    componentDidMount() {
        this.props.getContactList(this.state.person['id']);
    }

    getValidationState(field) {
        const length = this.state.person[field].length;
        if (length > 10) return 'success';
        else if (length > 5) return 'warning';
        else if (length > 0) return 'error';
        return null;
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.person !== this.state.person) {
            this.props.getContactList(nextProps.person['id']);
            this.setState({ person: nextProps.person });
        }
    }

    render() {
        return (
            <div>
                <div style={{width: '50%', display: 'inline-block', verticalAlign: 'top'}}>
                <form>
                    <FormGroup
                        controlId='formPersonFirstName'
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
                        controlId='formPersonLastName'
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
                        controlId='formPersonResume'
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
                        controlId='formPersonSalary'
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
                </div>
                <div style={{width: 'calc(50% - 10px)', display: 'inline-block', verticalAlign: 'top',marginTop: '35px', marginLeft: '5px', marginRight: '5px'}}>
                    <ContactContainer ref={(input) => { this.container = input; if(input !== null) input.addFullContact(this.props.contactList.data);}}/>
                </div>
            </div>
        );
    }
}