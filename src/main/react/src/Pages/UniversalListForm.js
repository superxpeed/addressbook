require('../Common/style.css');

import React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {Table} from '../Table/Table';
import * as CommonActions from './UniversalListActions';
import * as Url from '../Common/Url';
import {Caches, Generator, HashUtils} from '../Common/Utils';
import {Tab, Tabs, Navbar, Nav, Button, Breadcrumb} from 'react-bootstrap'
import * as TableActions from '../Table/TableActions';
import * as MenuActions from './MenuFormActions';
import {OrganizationComponent} from '../Components/OrganizationComponent';
import {PersonComponent} from '../Components/PersonComponent';

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
        breadcrumbs: state.menuReducer.breadcrumbs,
    }),
    dispatch => ({
        getList: bindActionCreators(CommonActions.getList, dispatch),
        onSelectRow: bindActionCreators(TableActions.onSelectRow, dispatch),
        updateRow: bindActionCreators(TableActions.updateRow, dispatch),
        addRow: bindActionCreators(TableActions.addRow, dispatch),
        getBreadcrumbs: bindActionCreators(MenuActions.getBreadcrumbs, dispatch),
        clearPersonSelection: bindActionCreators(CommonActions.clearPersonSelection, dispatch),
        logout: bindActionCreators(MenuActions.logout, dispatch)
    })
)

export default class UniversalListForm extends React.Component {

    constructor(props) {
        super(props);
        this.handleSelect = this.handleSelect.bind(this);
        this.state = {
            activeTab: 1
        };
    }

    componentDidMount() {
        let currentUrl = window.location.hash;
        this.refreshTable(1,10,'id','desc',[], Caches.ORGANIZATION_CACHE);
        this.props.getBreadcrumbs(HashUtils.cleanHash(currentUrl));
    }

    componentWillReceiveProps(nextProps) {
        if(nextProps.selectedRowsOrganization.length === 1
            && (
                (nextProps.selectedRowsOrganization.length  !== this.props.selectedRowsOrganization.length) ||
                (nextProps.selectedRowsOrganization[0].id  !== this.props.selectedRowsOrganization[0].id)
            )
        ) {
            let converted = [];
            converted.push({name: 'orgId', value: nextProps.selectedRowsOrganization[0].id, comparator: '', type: 'TextFilter'});
            this.refreshTable(1,10,'id','desc',converted, Caches.PERSON_CACHE);
            this.setState({selectedRowsOrgId: nextProps.selectedRowsOrganization[0].id});
            for(let i = 0; i < this.props.selectedRowsPerson.length; i++) {
                this.props.clearPersonSelection(this.props.selectedRowsPerson[i]);
            }
            this.setState({ createNewPerson: false, newPerson: undefined});
        }

        if(nextProps.selectedRowsOrganization.length === 0 && this.props.selectedRowsOrganization.length === 1){
            for(let i = 0; i < this.props.selectedRowsPerson.length; i++) {
                this.props.clearPersonSelection(this.props.selectedRowsPerson[i]);
            }
            this.setState({ createNewPerson: false, newPerson: undefined});
        }
    }

    personTabClosed = (tab) => {
        if(tab === this.state.activeTab){
            this.setState({ activeTab: (tab - 1) });
        }
    };

    refreshTable = (start, pageSize, sortName,sortOrder, filterDto, cache) => {
        if(cache === Caches.PERSON_CACHE && this.state.selectedRowsOrgId !== undefined && this.state.selectedRowsOrgId !== null && (filterDto === undefined || filterDto === null)) {
            filterDto = filterDto.filter(x => x.name === 'orgId' && x.value === this.state.selectedRowsOrgId );
            filterDto.push({name: 'orgId', value: this.state.selectedRowsOrgId, comparator: '', type: 'TextFilter'});
        }
        this.props.getList( Url.GET_LIST_4_UNIVERSAL_LIST_FORM + '?start=' + start + '&pageSize=' + pageSize + '&sortName=' + sortName + '&sortOrder=' + sortOrder + '&cache=' + cache, filterDto, cache);
    };

    updateSelectedPerson = (person) => {
        this.props.updateRow(person, Caches.PERSON_CACHE);
        this.setState({ createNewPerson: false, newPerson: undefined});
    };

    createdNewPerson = (person) => {
        this.props.addRow(person, Caches.PERSON_CACHE);
        this.setState({ createNewPerson: false, newPerson: undefined});
    };

    handleSelect(key){
        this.setState({ activeTab: key });
    }

    render() {
        let personTable;
        let newPersonTab;
        let persons = [];
        let breads = [];
        let breadcrumbsCount = this.props.breadcrumbs.length;
        this.props.breadcrumbs.forEach(function(element, index){
            breads.push(<Breadcrumb.Item style={{fontWeight: index === breadcrumbsCount - 1 ? 'bold' : 'normal'}} key={element.url} href={'#' + element.url}> {element.name} </Breadcrumb.Item>)
        });

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
            </Tab>;
            let key = 0;
            for(let i = 0; i < this.props.selectedRowsPerson.length; i++){
                key = i + 3;
                let personLocal = this.props.selectedRowsPerson[i];
                persons.push(
                    <Tab key={key + 'tab'} eventKey={key} title={
                        <span> {this.props.selectedRowsPerson[i].firstName}
                            <Button key={key + 'btn'} style={{height:'20px', width: '20px', padding: '0px', marginLeft: '5px', zIndex: 1000}} onClick={(e) => {
                                e.stopPropagation();
                                this.props.onSelectRow(personLocal, false, e, Caches.PERSON_CACHE);
                                this.personTabClosed(key);
                            }}>
                                X
                            </Button>
                        </span>
                    }>
                        <PersonComponent key={key + 'person'} forUpdate={true} person={this.props.selectedRowsPerson[i]} onUpdate={this.updateSelectedPerson}/>
                    </Tab>
                );
            }
            if(this.state.createNewPerson === true){
                if(key === 0)
                    key = 3;
                else
                    key ++;
                newPersonTab = <Tab key={key} eventKey={key} title={
                                    <span> {'New person'}
                                        <Button key={'newPersonTabBtn'} style={{height:'20px', width: '20px', padding: '0px', marginLeft: '5px', zIndex: 1000}} onClick={(e) => {
                                            e.stopPropagation();
                                            this.setState({ createNewPerson: false, newPerson: undefined});
                                            this.personTabClosed(key);
                                        }}>X
                                        </Button>
                                    </span>
                                    }>
                                    <PersonComponent key={'newPersonKey'} forUpdate={false} person={this.state.newPerson} onUpdate={this.updateSelectedPerson}/>
                                </Tab>
            }else{
                newPersonTab = <div/>
            }
        }else {
            personTable = <div/>
        }

        return (
            <div>
                <Navbar>
                    <Navbar.Collapse>
                        <Nav>
                            <Breadcrumb>
                                {breads}
                            </Breadcrumb>
                        </Nav>
                        <Nav pullRight>
                            <Button disabled={this.props.selectedRowsOrganization.length !== 1} onClick={() => this.setState({ createNewPerson: true, newPerson: {id: Generator.uuidv4(), orgId: this.props.selectedRowsOrganization[0].id}})}>
                                Create person
                            </Button>
                            <Button onClick={() => this.props.logout()}>
                                Logout
                            </Button>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
                <Tabs activeKey={this.state.activeTab}
                      onSelect={this.handleSelect} id='tables'>
                    <Tab key={1} eventKey={1} title='Organizations'>
                        <div style={{marginBottom: '15px'}}>
                            <OrganizationComponent organization={this.props.selectedRowsOrganization.length === 1 ? this.props.selectedRowsOrganization[0] : {}}/>
                        </div>
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
                    {newPersonTab}
                </Tabs>
            </div>)
    }
}