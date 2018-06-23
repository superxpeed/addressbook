import {DetailedComponent} from "../Common/DetailedComponent";

require('../Common/style.css');

import React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {Table} from '../Table/Table';
import * as CommonActions from '../Common/CommonActions';
import * as Url from '../Common/Url';
import {Caches} from '../Table/Enums';
import {Tab, Tabs, Navbar, Nav, Button, Breadcrumb, Glyphicon} from 'react-bootstrap'
import * as TableActions from "../Table/TableActions";
@connect(
    state => ({
        tableDataOrganization: state.universalListReducer.tableDataOrganization,
        fieldDescriptionMapOrganization: state.universalListReducer.fieldDescriptionMapOrganization,
        totalDataSizeOrganization: state.universalListReducer.totalDataSizeOrganization,

        tableDataPerson: state.universalListReducer.tableDataPerson,
        fieldDescriptionMapPerson: state.universalListReducer.fieldDescriptionMapPerson,
        totalDataSizePerson: state.universalListReducer.totalDataSizePerson,

        selectedRowsPerson: state.universalListReducer.selectedRowsPerson,
        selectedRowsOrganization: state.universalListReducer.selectedRowsOrganization,
    }),
    dispatch => ({
        getList: bindActionCreators(CommonActions.getList, dispatch),
        onSelectRow: bindActionCreators(TableActions.onSelectRow, dispatch)
    })
)
class UniversalListForm extends React.Component {

    state = {
        lgShow: false
    };

    constructor(props, context) {
        super(props, context);

        this.handleSelect = this.handleSelect.bind(this);

        this.state = {
            activeTab: 1
        };
    }

    componentDidMount() {
        this.refreshTable(1,10,'id','desc',[], Caches.ORGANIZATION_CACHE);
    }

    componentWillReceiveProps(nextProps) {
        if(nextProps.selectedRowsOrganization.length === 1
            && (
                (nextProps.selectedRowsOrganization.length  !== this.props.selectedRowsOrganization.length)
                ||
                (nextProps.selectedRowsOrganization[0].id  !== this.props.selectedRowsOrganization[0].id)
            )
        ) {
            let converted = [];
            converted.push({name: 'orgId', value: nextProps.selectedRowsOrganization[0].id, comparator: '', type: 'TextFilter'});
            this.refreshTable(1,10,'id','desc',converted, Caches.PERSON_CACHE);
            this.setState({selectedRowsOrgId: nextProps.selectedRowsOrganization[0].id});
        }
    }

    componentDidUpdate() {}

    personTabClosed = (tab) => {
        if(tab === this.state.activeTab){
            this.setState({ activeTab: (tab - 1) });
        }
    };

    refreshTable = (start, pageSize, sortName,sortOrder, filterDto, cache) => {
        this.props.getList( Url.GET_LIST_4_UNIVERSAL_LIST_FORM + '?start=' + start + '&pageSize=' + pageSize + '&sortName=' + sortName + '&sortOrder=' + sortOrder + '&cache=' + cache, filterDto, cache);
    };

    handleSelect(key){
        console.log(key);
        this.setState({ activeTab: key });
    }

    render() {
        console.log('Organizations: ' + this.props.selectedRowsOrganization);
        console.log('Persons: ' + this.props.selectedRowsPerson);

        let lgClose = () => this.setState({ lgShow: false });

        let personTable;

        if(this.props.selectedRowsOrganization.length === 1){
            personTable = <Tab key={2} eventKey={2} title={this.props.selectedRowsOrganization[0].name}>
                <Table ref='table2'
                       selectMode='multi'
                       refreshTable={this.refreshTable}
                       cache={Caches.PERSON_CACHE}
                       selectedRows={this.props.selectedRowsPerson}
                       data={this.props.tableDataPerson.data}
                       totalDataSize={this.props.totalDataSizePerson}
                       fieldDescriptionMap={this.props.fieldDescriptionMapPerson}
                       parent={this}
                />
            </Tab>
        }else {
            personTable = <div/>
        }

        let persons = [];

        for(var i = 0; i < this.props.selectedRowsPerson.length; i++){
            let key = i + 3;
            let personLocal = this.props.selectedRowsPerson[i];
            persons.push(
                <Tab key={key + 'tab'} eventKey={key} title={
                        <span> {this.props.selectedRowsPerson[i].firstName}
                            <Button key={key + 'btn'} style={{height:'20px', width: '20px', padding: '0px', zIndex: 1000}} onClick={(e) => {
                                        e.stopPropagation();
                                        this.props.onSelectRow(personLocal, false, e, Caches.PERSON_CACHE);
                                        this.personTabClosed(key);
                                    }}>
                                X
                            </Button>
                        </span>
                }>
                    <DetailedComponent key={key + 'person'} person={this.props.selectedRowsPerson[i]}/>
                </Tab>
            );
        }

        return (
            <div>
                <Navbar>
                    <Navbar.Header>
                        <Navbar.Brand>
                            <a href='#home'>React-Bootstrap</a>
                        </Navbar.Brand>
                    </Navbar.Header>
                    <Navbar.Collapse>
                        <Nav>
                            <Breadcrumb>
                                <Breadcrumb.Item href="#">Home</Breadcrumb.Item>
                                <Breadcrumb.Item href="http://getbootstrap.com/components/#breadcrumbs">Library</Breadcrumb.Item>
                                <Breadcrumb.Item active>Data</Breadcrumb.Item>
                            </Breadcrumb>
                        </Nav>
                        <Nav pullRight>
                            <Button disabled={this.props.selectedRowsOrganization.length === 0}
                                onClick={() => this.setState({ lgShow: true })}>
                                Show details
                            </Button>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
                <Tabs activeKey={this.state.activeTab}
                      onSelect={this.handleSelect} id='tables'>
                    <Tab key={1} eventKey={1} title='Organizations'>
                        <Table ref='table1'
                               refreshTable={this.refreshTable}
                               cache={Caches.ORGANIZATION_CACHE}
                               selectMode='single'
                               selectedRows={this.props.selectedRowsOrganization}
                               data={this.props.tableDataOrganization.data}
                               totalDataSize={this.props.totalDataSizeOrganization}
                               fieldDescriptionMap={this.props.fieldDescriptionMapOrganization}
                               parent={this}
                        />
                    </Tab>
                    {personTable}
                    {persons}
                </Tabs>
            </div>)
    }
}

export default UniversalListForm;