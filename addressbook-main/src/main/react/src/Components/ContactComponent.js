import React from "react";
import {Button, FormControl, Panel} from "react-bootstrap";
import {ContactTypes} from "../Common/Utils";

export class ContactComponent extends React.Component {
    state = {
        data: this.props.data.data !== undefined ? this.props.data.data : "",
        description: this.props.data.description !== undefined ? this.props.data.description : "",
        type: this.props.data.type !== undefined ? this.props.data.type : "0",
        personId: this.props.data.personId !== undefined ? this.props.data.personId : "",
    };

    constructor(props) {
        super(props);
    }

    handleChange = (e) => {
        this.setState({[e.target.id]: e.target.value});
    };

    toJson() {
        return JSON.stringify({
            data: this.state.data,
            description: this.state.description,
            type: this.state.type,
            id: this.props.id,
            personId: this.state.personId,
        });
    }

    getFormControl(field) {
        return (<FormControl
                style={{marginTop: "5px"}}
                type="text"
                value={this.state[field]}
                placeholder={"Enter " + field}
                id={field}
                onChange={this.handleChange}/>);
    }

    render() {
        return (<Panel style={{marginBottom: "5px"}}>
                <Panel.Heading>
                    <Panel.Title
                        style={{display: "inline-block", width: "calc(100% - 25px)"}}
                        toggle>
                        {this.props.data.type === undefined ? "New contact" : ContactTypes.getEngType(this.props.data.type)}
                    </Panel.Title>
                    <Button
                        style={{
                            width: "32px",
                            height: "32px",
                            marginTop: "-10px",
                            marginBottom: "-5px",
                            marginRight: "-10px",
                            position: "relative",
                            padding: "6px",
                        }}
                        onClick={this.props.deleteContact.bind(null, {id: this.props.id})}>
                        X
                    </Button>
                </Panel.Heading>
                <Panel.Body collapsible>
                    <FormControl
                        id="type"
                        style={{marginTop: "5px"}}
                        componentClass="select"
                        value={this.state.type}
                        placeholder="1"
                        onChange={this.handleChange}>
                        <option value="0">Mobile phone</option>
                        <option value="1">Home phone</option>
                        <option value="2">Address</option>
                        <option value="3">E-mail</option>
                    </FormControl>
                    {this.getFormControl("data")}
                    {this.getFormControl("description")}
                </Panel.Body>
            </Panel>);
    }
}
