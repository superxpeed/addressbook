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

    constructor(props) {
        super(props);
    }

    handleChange = (e) => {
        this.setState({[e.target.name]: e.target.value});
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

    render() {

        return (<Accordion>
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
                    id="data"
                    type="text"
                    name="data"
                    value={this.state.data}
                    label="Enter data"
                    variant="outlined"
                    autoComplete="off"
                    sx={{mt: 2, display: "flex"}}
                    onChange={this.handleChange}
                />
                <TextField
                    id="description"
                    type="text"
                    name="description"
                    value={this.state.description}
                    label="Enter description"
                    variant="outlined"
                    autoComplete="off"
                    sx={{mt: 2, display: "flex"}}
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
