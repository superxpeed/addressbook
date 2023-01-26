import React from "react";
import {ContactComponent} from "./ContactComponent";
import Button from "@mui/material/Button";
import {Dialog, DialogActions, DialogContentText, DialogTitle} from "@mui/material";
import DialogContent from "@mui/material/DialogContent";

export class ContactContainer extends React.Component {
    state = {
        contacts: new Map(), conRefs: new Map(), show: false, expanded: ""
    };

    updateExpanded = (id) => {
        this.setState({expanded: id})
        this.state.conRefs.forEach(function (value) {
            if (value !== null) value.forceUpdate();
        });
    }

    getExpanded = () => {
        return this.state.expanded
    }

    addFullContact = (contacts) => {
        if (contacts != null && contacts.length !== 0) {
            let contactsMap = new Map(this.state.contacts);
            for (let i = 0; i < contacts.length; i++) {
                let newCon = (<ContactComponent
                    key={contacts[i].id}
                    data={contacts[i]}
                    expanded={this.getExpanded}
                    updateExpanded={this.updateExpanded}
                    updateContactsStatus={this.props.updateContactsStatus}
                    ref={(input) => {
                        this.state.conRefs.set(contacts[i].id, input);
                    }}
                    id={contacts[i].id}
                    deleteContact={this.showConfirmationDialog}/>);
                contactsMap.set(newCon.key, newCon);
            }
            this.setState({contacts: contactsMap, conRefs: this.state.conRefs});
        } else {
            if (this.state.contacts == null || this.state.contacts.size === 0) this.setState({
                contacts: new Map(), conRefs: new Map()
            });
        }
    };

    addEmptyContact = () => {
        let contacts = new Map(this.state.contacts);
        let id = Math.floor(Math.random() * 10000);
        let newCon = (<ContactComponent
            key={id}
            expanded={this.getExpanded}
            updateExpanded={this.updateExpanded}
            updateContactsStatus={this.props.updateContactsStatus}
            data={{personId: this.props.personId}}
            ref={(input) => {
                this.state.conRefs.set(id, input);
            }}
            id={id}
            deleteContact={this.showConfirmationDialog}
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
            <Button sx={{mt: 1, width: "100%", height: "56px", marginBottom: "30px"}} variant="outlined"
                    onClick={this.addEmptyContact}>
                Add contact
            </Button>
            {Array.from(this.state.contacts.values())}
            <Dialog
                open={this.state.show}
                onClose={() => this.closeConfirmationDialog()}
                aria-describedby="confirmation-modal-description"
            >
                <DialogTitle>Confirm contact deletion</DialogTitle>
                <DialogContent>
                    <DialogContentText id="confirmation-modal-description">
                        Delete this contact?
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={this.closeConfirmationDialog}>Cancel</Button>
                    <Button variant="outlined" color="error" onClick={this.deleteContact}>Delete</Button>
                </DialogActions>
            </Dialog>
        </div>);
    }
}
