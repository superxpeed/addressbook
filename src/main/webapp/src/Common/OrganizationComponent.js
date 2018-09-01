import {FormControl, FormGroup, ControlLabel, Button} from 'react-bootstrap'
import update from 'react-addons-update'
import React from 'react';
import * as url from './Url';

export class OrganizationComponent extends React.Component {

    state = {
        organization: {}
    };

    handleChange(e) {
        this.setState(update(this.state, {organization: {[e.target.id]: {$set:e.target.value}}}));
    }

    constructor(props) {
        super(props);
        this.state = {
            organization: props.organization
        }
    }

    getValidationState(field) {
        if(this.state.organization[field] === undefined || this.state.organization[field] === null) return 'error';
        const length = this.state.organization[field].length;
        if (length > 10) return 'success';
        else if (length > 5) return 'warning';
        else if (length > 0) return 'error';
        return null;
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.organization !== this.state.organization) {
            this.getContactList(nextProps.organization['id']);
            this.setState({ organization: nextProps.organization });
        }
    }

    render() {
        return (
            <div>
                <div style={{width: '50%', display: 'inline-block', verticalAlign: 'top'}}>
                    <form>
                        <FormGroup
                            controlId='name'
                            validationState={this.getValidationState('name')}>
                            <ControlLabel>Organization name</ControlLabel>
                            <FormControl
                                type='text'
                                value={this.state.organization['name']}
                                placeholder='Enter name'
                                onChange={this.handleChange.bind(this, 'name')}
                            />
                            <FormControl.Feedback />
                        </FormGroup>
                    </form>
                    <form>
                        <FormGroup
                            controlId='street'
                            validationState={this.getValidationState('street')}>
                            <ControlLabel>Address street</ControlLabel>
                            <FormControl
                                type='text'
                                value={this.state.organization['street']}
                                placeholder='Enter address steet'
                                onChange={this.handleChange.bind(this, 'street')}
                            />
                            <FormControl.Feedback />
                        </FormGroup>
                    </form>
                    <form>
                        <FormGroup
                            controlId='zip'
                            validationState={this.getValidationState('zip')}>
                            <ControlLabel>Address zip</ControlLabel>
                            <FormControl
                                type='text'
                                value={this.state.organization['zip']}
                                placeholder='Enter address zip'
                                onChange={this.handleChange.bind(this, 'zip')}
                            />
                            <FormControl.Feedback />
                        </FormGroup>
                    </form>
                    <form>
                        <FormControl id='type' style={{marginTop: '5px'}} componentClass='select' value={this.state.organization['type']} placeholder='PRIVATE' onChange={this.handleChange}>
                            <option value='PRIVATE'>PRIVATE</option>
                            <option value='PUBLIC'>PUBLIC</option>
                        </FormControl>
                    </form>
                    <Button onClick={this.savePerson}>
                        Save organization
                    </Button>
                </div>
            </div>
        );
    }
}