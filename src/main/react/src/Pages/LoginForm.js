import React from 'react';
import * as url from '../Common/Url';
import {Button, FormControl, Label, Panel} from 'react-bootstrap';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as MenuActions from './MenuFormActions';

@connect(
    null,
    dispatch => ({
        clearAlerts: bindActionCreators(MenuActions.clearAlerts, dispatch),
    })
)
export default class LoginForm extends React.Component {

    state = {
        login: '',
        password: '',
        invalidLoginPassword: false
    };

    constructor(props) {
        super(props);
    }

    keyDownTextField = (e) => {
        let parent = this;
        let keyCode = e.keyCode;
        if (keyCode === 13) {
            parent.login();
        }
    };

    componentDidMount() {
        setTimeout(function () {
            this.props.clearAlerts();
        }.bind(this), 500);
        document.addEventListener('keydown', this.keyDownTextField, false);
    }

    componentWillUnmount() {
        document.removeEventListener('keydown', this.keyDownTextField, false);
    }

    handleChange = (e) => {
        this.setState({[e.target.id]: e.target.value});
    };

    login = () => {
        console.log('Login' + this.state.login);
        console.log('Password' + this.state.password);
        let status;
        let headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        fetch(url.LOGIN, {
            method: 'post',
            headers: headers,
            body: 'username=' + this.state.login + '&password=' + this.state.password
        }).then(response => {
            status = response.status;
            if (status === 401) {
                this.setState({invalidLoginPassword: true});
            }
            if (status === 200) {
                window.location.hash = '#/';
            }
        });
    };

    getWarning = () => {
        if (this.state.invalidLoginPassword) {
            return <Label style={{
                width: '500px',
                display: 'inline-block',
                position: 'absolute',
                left: 'calc(50% - 250px)',
                top: 'calc(50% - 120px)'
            }} bsStyle='danger'>Invalid login or password</Label>
        } else
            return <div/>
    };

    render() {
        return (
            <div>
                {this.getWarning()}
                <Panel style={{
                    width: '500px',
                    height: '200px',
                    position: 'absolute',
                    left: 'calc(50% - 250px)',
                    top: 'calc(50% - 100px)'
                }}>
                    <Panel.Heading>
                        <Panel.Title>Please login</Panel.Title>
                    </Panel.Heading>
                    <Panel.Body>
                        <FormControl
                            style={{marginTop: '5px'}}
                            type='text'
                            value={this.state.login}
                            placeholder={'Enter login'}
                            id='login'
                            onChange={this.handleChange}/>
                        <FormControl
                            style={{marginTop: '5px'}}
                            type='text'
                            value={this.state.password}
                            placeholder={'Enter password'}
                            id='password'
                            onChange={this.handleChange}/>
                        <Button style={{marginTop: '5px', width: '100%'}} onClick={this.login}>Login</Button>
                    </Panel.Body>
                </Panel>
            </div>
        )
    }
}