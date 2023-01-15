import React from "react";
import {ContactComponent} from "./ContactComponent";
import {Button, Modal} from "react-bootstrap";

export class ContactContainer extends React.Component {
    state = {
        contacts: new Map(), conRefs: new Map(),
        show: false
    };

    constructor(props) {
        super(props);
    }

    static getId() {
        return Math.floor(Math.random() * 10000);
    }

    addFullContact = (contacts) => {
        if (contacts != null && contacts.length !== 0) {
            let contactsMap = new Map();
            let delFunc = this.showConfirmationDialog;
            let conRefsLocal = this.state.conRefs;
            contacts.forEach(function (element) {
                let newCon = (<ContactComponent
                    key={element.id}
                    data={element}
                    ref={(input) => {
                        conRefsLocal.set(element.id, input);
                    }}
                    id={element.id}
                    deleteContact={delFunc}/>);
                contactsMap.set(newCon.key, newCon);
            });
            this.setState({contacts: contactsMap, conRefs: conRefsLocal});
        } else {
            if (this.state.contacts == null || this.state.contacts.size === 0) this.setState({
                contacts: new Map(), conRefs: new Map()
            });
        }
    };

    addEmptyContact = () => {
        let contacts = new Map(this.state.contacts);
        let id = ContactContainer.getId();
        let newCon = (<ContactComponent
            key={id}
            data={{personId: this.props.personId}}
            ref={(input) => {
                this.state.conRefs.set(id, input);
            }}
            id={id}
            deleteContact={this.deleteContact}
        />);
        contacts.set(newCon.key, newCon);
        this.setState({contacts: contacts});
    };

    getJson = () => {
        let contacts = "[";
        if (this.state.conRefs.size !== 0) {
            this.state.conRefs.forEach(function (value) {
                if (value !== null) contacts += value.toJson() + ", ";
            });
            contacts = contacts.substring(0, contacts.length - 2);
        }
        contacts += "]";
        return contacts;
    };

    closeConfirmationDialog = () => {
        this.setState({show: false, idToDelete: null});
    }

    showConfirmationDialog = (params) => {
        this.setState({show: true, idToDelete: params.id});
    }

    deleteContact = () => {
        let contacts = new Map(this.state.contacts);
        let conRefs = new Map(this.state.conRefs);
        contacts.delete(this.state.idToDelete + "");
        conRefs.delete(this.state.idToDelete);
        this.setState({contacts: contacts, conRefs, show: false});
    };

    render() {
        return (<div>
            <Button
                style={{width: "100%", marginBottom: "5px", marginTop: "-10px"}}
                onClick={this.addEmptyContact}>
                Add contact
            </Button>
            {this.state.contacts}
            <Modal show={this.state.show} onHide={this.closeConfirmationDialog}>
                <Modal.Header closeButton>
                    <Modal.Title>Delete this contact?</Modal.Title>
                </Modal.Header>
                <Modal.Footer>
                    <Button onClick={this.closeConfirmationDialog}>Cancel</Button>
                    <Button bsStyle="danger" onClick={this.deleteContact}>Delete</Button>
                </Modal.Footer>
            </Modal>
        </div>);
    }
}
