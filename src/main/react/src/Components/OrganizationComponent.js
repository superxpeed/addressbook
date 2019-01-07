import {FormControl, FormGroup, ControlLabel, Button} from 'react-bootstrap'
import update from 'react-addons-update'
import React from 'react';
import * as url from '../Common/Url';
import {ifNoAuthorizedRedirect} from '../Pages/UniversalListActions';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as TableActions from '../Table/TableActions';
import {Caches, Generator, OrgTypes} from '../Common/Utils';
import * as MenuActions from '../Pages/MenuFormActions';

@connect(
    null,
    dispatch => ({
        updateRow: bindActionCreators(TableActions.updateRow, dispatch),
        addRow: bindActionCreators(TableActions.addRow, dispatch),
        showCommonErrorAlert: bindActionCreators(MenuActions.showCommonErrorAlert, dispatch),
        lockUnlockRecord: bindActionCreators(MenuActions.lockUnlockRecord, dispatch)
    })
)
export class OrganizationComponent extends React.Component {

    state = {
        organization: {},
        create: false,
        locked: true
    };

    handleChange(e, v) {
        this.setState(update(this.state, {organization: {[e]: {$set:v.currentTarget.value}}}));
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

    lockCallback = (result) => {
        if(result === 'success'){
            this.setState({locked: true});
        }else if(result === 'warning'){
            this.setState({locked: false});
        }
    };

    componentWillReceiveProps(nextProps) {
        if (nextProps.organization !== this.state.organization) {
            if(this.state.organization['id'] !== undefined && this.state.create === false){
                this.props.lockUnlockRecord(Caches.ORGANIZATION_CACHE, this.state.organization['id'], 'unlock');
            }
            if(nextProps.organization['id'] === undefined){
                this.setState({ organization: {name: '', street: '', id: Generator.uuidv4(), zip: '', type: '2'}, create: true, locked: true });
            }else{
                this.props.lockUnlockRecord(Caches.ORGANIZATION_CACHE, nextProps.organization['id'], 'lock', this.lockCallback);
                let org = Object.assign({}, nextProps.organization);
                org['type'] =  OrgTypes.getEngType(nextProps.organization['type']);
                this.setState({ organization: org, create: false });
            }
        }
    }

    saveOrganization = () => {
        let isOk = false;
        let headers = new Headers();
        headers.append('Accept', 'application/json');
        headers.append('Content-Type', 'application/json; charset=utf-8');
        fetch(url.SAVE_ORGANIZATION, {
            method: 'post',
            headers: headers,
            credentials: 'include',
            body: JSON.stringify(this.state.organization)
        }).then(response => {
            ifNoAuthorizedRedirect(response);
            isOk = response.ok;
            return response.text()
        }).then(text => {
            if (isOk) {
                this.props.updateRow(this.state.organization, Caches.ORGANIZATION_CACHE);
            }else {
                this.props.showCommonErrorAlert(text);
            }
        });
    };

    handleTypeChange = (e) =>{
        this.setState(update(this.state, {organization: {[e.target.id]: {$set:e.target.value}}}));
    };

    getButton(){
        if(this.state.create){
            return <Button style={{width: '100%'}} onClick={this.saveOrganization}>
                Create organization
            </Button>;
        }else{
            return <Button style={{width: '100%'}} onClick={this.saveOrganization} disabled={!this.state.locked}>
                Save organization
            </Button>;
        }
    }

    render() {
        return (
            <div style={{padding: '10px'}}>
                <div style={{width: '50%', display: 'inline-block', verticalAlign: 'top', paddingRight: '5px'}}>
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
                </div>
                <div style={{width: '50%', display: 'inline-block', verticalAlign: 'top', paddingLeft: '5px'}}>
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
                        <FormGroup>
                            <ControlLabel>Type</ControlLabel>
                            <FormControl id='type' componentClass='select' value={this.state.organization['type']} onChange={this.handleTypeChange}>
                                <option value='0'>Non profit</option>
                                <option value='1'>Private</option>
                                <option value='2'>Government</option>
                                <option value='3'>Public</option>
                            </FormControl>
                        </FormGroup>
                    </form>
                </div>
                {this.getButton()}
            </div>
        );
    }
}