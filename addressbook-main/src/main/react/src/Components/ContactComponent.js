import React from "react";
import {ContactTypes} from "../Common/Utils";
import {
    Accordion,
    AccordionDetails,
    AccordionSummary,
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    TextField,
    Typography
} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import Button from "@mui/material/Button";

export class ContactComponent extends React.Component {
    state = {
        data: this.props.data.data != null ? this.props.data.data : "",
        description: this.props.data.description != null ? this.props.data.description : "",
        type: this.props.data.type != null ? this.props.data.type : "0",
        personId: this.props.data.personId != null ? this.props.data.personId : "",
    };

    handleChange = (e) => {
        this.setState({[e.target.name]: e.target.value});
        if (e.target.name === "data" || e.target.name === "description") {
            this.props.updateContactsStatus(this.props.id + "&&" + e.target.name, e.target.value != null && e.target.value.trim().length !== 0);
        }
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

    componentDidMount() {
        this.props.updateContactsStatus(this.props.id + "&&data", this.state.data != null && this.state.data.trim().length !== 0)
        this.props.updateContactsStatus(this.props.id + "&&description", this.state.description != null && this.state.description.trim().length !== 0)
    }

    componentWillUnmount() {
        this.props.updateContactsStatus(this.props.id + "&&data", true)
        this.props.updateContactsStatus(this.props.id + "&&description", true)
    }

    render() {
        return (<Accordion expanded={this.props.expanded() === this.props.id}
                           onChange={() => this.props.updateExpanded(this.props.id)}>
            <AccordionSummary
                expandIcon={<ExpandMoreIcon/>}
                aria-controls="panel1a-content">
                <Typography>{this.props.data.type == null ? "New contact" : ContactTypes.getEngType(this.props.data.type)}</Typography>
            </AccordionSummary>
            <AccordionDetails>
                <FormControl fullWidth>
                    <InputLabel id="type-label">Type</InputLabel>
                    <Select
                        labelId="type-label"
                        id="type"
                        name="type"
                        value={this.state.type}
                        label="Type"
                        onChange={this.handleChange}
                    >
                        <MenuItem value={0}>Mobile phone</MenuItem>
                        <MenuItem value={1}>Home phone</MenuItem>
                        <MenuItem value={2}>Address</MenuItem>
                        <MenuItem value={3}>E-mail</MenuItem>
                    </Select>
                </FormControl>
                <TextField
                    error={this.state.data == null || this.state.data.trim().length === 0}
                    id="data"
                    type="text"
                    name="data"
                    value={this.state.data}
                    label="Enter data"
                    variant="outlined"
                    autoComplete="off"
                    helperText={this.state.data == null || this.state.data.trim().length === 0 ? "Required field!" : ""}
                    sx={{mt: 5, display: "flex", height: "80px"}}
                    onChange={this.handleChange}
                />
                <TextField
                    error={this.state.description == null || this.state.description.trim().length === 0}
                    id="description"
                    type="text"
                    name="description"
                    value={this.state.description}
                    label="Enter description"
                    variant="outlined"
                    autoComplete="off"
                    helperText={this.state.description == null || this.state.description.trim().length === 0 ? "Required field!" : ""}
                    sx={{mt: 2, display: "flex", height: "80px"}}
                    onChange={this.handleChange}
                />
                <Button sx={{mt: 2, width: "100%", height: "56px"}}
                        onClick={this.props.deleteContact.bind(null, {id: this.props.id})}
                        variant="outlined"
                        color="error">
                    Delete contact
                </Button>
            </AccordionDetails>
        </Accordion>)
    }
}
