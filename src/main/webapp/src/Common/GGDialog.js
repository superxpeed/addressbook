import {Modal, Button, ControlLabel,FormControl, Form, Col, FormGroup} from 'react-bootstrap'
import React from 'react';

export class GGDialog extends React.Component {

    constructor(props, context) {
        super(props, context);

        this.state = {
            snapshotPath: ''
        };
    }

    checkPathReachability = () =>{
        console.log('Check here');
    };

    takeSnapshot = () => {
      console.log('Take snapshot here');
    };

    handleChange(e) {
        this.setState({ snapshotPath: e.target.value });
    }
    render() {
        return (
            <div>
                <Modal show={this.props.show} onHide={this.props.handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>GridGain control</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <h4>Take snapshot</h4>
                        <ControlLabel>Snapshot directory</ControlLabel>
                        <Form horizontal>
                            <FormGroup controlId='snapPathForm'>
                                <Col sm={8}>
                                    <FormControl
                                        type='text'
                                        value={this.state.value}
                                        placeholder='Enter or choose directory for LFS snapshot'
                                        onChange={this.handleChange}
                                    />
                                </Col>
                                <Col sm={2} style={{padding: '0px', width: 'auto', margin: '0px'}}>
                                    <Button onClick={this.checkPathReachability}>Check</Button>
                                </Col>
                                <Col sm={2}>
                                    <Button onClick={this.takeSnapshot}>Snapshot</Button>
                                </Col>
                            </FormGroup>
                        </Form>

                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={this.props.handleClose}>Close</Button>
                    </Modal.Footer>
                </Modal>
            </div>
        );

    }
}