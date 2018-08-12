import {FormControl, FormGroup, ControlLabel, HelpBlock, Button} from 'react-bootstrap'
import update from 'react-addons-update'
import React from 'react';
import {ContactContainer} from './ContactContainer'

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

    getValidationState(field) {
        const length = this.state.person[field].length;
        if (length > 10) return 'success';
        else if (length > 5) return 'warning';
        else if (length > 0) return 'error';
        return null;
    }

    render() {

        let contact1 ='[{"data":"8-915-287-9000","description":"mobile1","type":"1","id":816}, {"data":"","description":"","type":"2","id":3256}, {"data":"","description":"","type":"3","id":2403}]';

        let parsed = JSON.parse(contact1);

        return (
            <div>
                <form>
                    <FormGroup
                        controlId='formPersonId'
                        validationState={this.getValidationState('id')}>
                        <ControlLabel>Employee ID</ControlLabel>
                        <FormControl
                            type='text'
                            value={this.state.person['id']}
                            placeholder='Enter id'
                            onChange={this.handleChange.bind(this, 'id')}
                        />
                        <FormControl.Feedback />
                    </FormGroup>
                </form>
                <form>
                    <FormGroup
                        controlId='formPersonOrgId'
                        validationState={this.getValidationState('orgId')}>
                        <ControlLabel>Employee organization id</ControlLabel>
                        <FormControl
                            type='text'
                            value={this.state.person['orgId']}
                            placeholder='Enter organization id'
                            onChange={this.handleChange.bind(this, 'orgId')}
                        />
                        <FormControl.Feedback />
                    </FormGroup>
                </form>
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
                <ContactContainer ref={(input) => { this.container = input; if(input !== null) input.addFullContact(parsed);}}/>
            </div>
        );
    }
}