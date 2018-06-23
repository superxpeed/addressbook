import React from 'react';
import {Accordion, Panel, FormControl, Button} from 'react-bootstrap';

export class Contact extends React.Component {

    state = {
        data: this.props.data.data !== undefined ? this.props.data.data : '' ,
        description:this.props.data.description !== undefined ? this.props.data.description : '',
        type: this.props.data.type !== undefined ? this.props.data.type : '1'
    };

    constructor(props) {
        super(props);
    }

    handleChange = (e) =>{
        this.setState({ [e.target.id] : e.target.value });
        console.log(e.target.id + ' ' + e.target.value);
    };

    toJson(){
        return JSON.stringify({ 'data' : this.state.data, 'description' : this.state.description, 'type' : this.state.type, 'id' : this.props.id});
    }

    static getEngType = (type) => {
        if(type === '1') return 'Mobile phone';
        if(type === '2') return 'Home phone';
        if(type === '3') return 'Address';
        if(type === '4') return 'E-mail';
    };

    render() {

        return (

            <Accordion style={{margin: '0px', marginTop: '-40px'}}>
                <Button style={{width: '40px', marginTop: '10px' , marginBottom: '-5px', position:'relative', right: '-528px', bottom: '-34px', height: '38px', zIndex: 9999}} onClick={this.props.deleteContact.bind(null, { id: this.props.id })}>X</Button>
                <Panel  header={this.props.data.type === undefined ? 'New contact' : Contact.getEngType(this.props.data.type)} eventKey='1'>
                    <FormControl style={{marginTop: '5px'}} componentClass='select' placeholder='1' onChange={this.handleChange}>
                        <option value='1'>Mobile phone</option>
                        <option value='2'>Home phone</option>
                        <option value='3'>Address</option>
                        <option value='4'>E-mail</option>
                    </FormControl>
                    <FormControl style={{marginTop: '5px'}}
                                 type='text'
                                 value={this.state.data}
                                 placeholder='Enter data'
                                 id='data'
                                 onChange={this.handleChange}
                    />
                    <FormControl style={{marginTop: '5px'}}
                                 type='text'
                                 value={this.state.description}
                                 placeholder='Enter description'
                                 id='description'
                                 onChange={this.handleChange}
                    />
                </Panel>
            </Accordion>

        );
    }

}
