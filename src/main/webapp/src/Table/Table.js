import React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';
import * as TableActions from './TableActions';

export const FILTER_REF = 'filter_';

@connect(
    null,
    dispatch => ({
        onSelectRow: bindActionCreators(TableActions.onSelectRow, dispatch),
        onSelectAllRowsOnCurrentPage: bindActionCreators(TableActions.onSelectAllRowsOnCurrentPage, dispatch)
    })
    , null, {withRef: true}
)
export class Table extends React.Component {

    state = {
        prevFilterResultLength: 0,
        sortName: 'id',
        sortOrder: 'desc',
        sizePerPage: 10,
        page: 1,
        filterObj: {}
    };

    onSortChange = (sortName, sortOrder) => {
        if(this.state.sortName !== sortName || this.state.sortOrder !== sortOrder){
            this.setState({ sortName, sortOrder });
            this.props.refreshTable(this.state.page, this.state.sizePerPage, sortName, sortOrder, this.convertFilterObj(this.state.filterObj), this.props.cache);
        }
    };

    static html2text = (html) => {
        let tag = document.createElement('div');
        tag.innerHTML = html;
        return tag.innerText;
    };

    static sameCellFormatter = (cell) => {
        return <div title={cell}>{cell}</div>
    };

    static resumeCellFormatter = (cell, row) => {
        return <div title={Table.html2text(cell)}>{Table.html2text(cell)}</div>
    };

    convertFilterObj = (filterObj) => {
        let converted = [];
        for (const key of Object.keys(filterObj)) {
            if(filterObj.hasOwnProperty(key)){
                if(filterObj[key].type === 'DateFilter'){
                    converted.push({name: key, value: filterObj[key].value.date, comparator: filterObj[key].value.comparator, type: filterObj[key].type});
                }
                if(filterObj[key].type === 'NumberFilter'){
                    converted.push({name: key, value: filterObj[key].value.number, comparator: filterObj[key].value.comparator, type: filterObj[key].type});
                }
                if(filterObj[key].type === 'TextFilter'){
                    converted.push({name: key, value: filterObj[key].value, comparator: '', type: filterObj[key].type});
                }
            }
        }
        return converted;
    };

    onFilterChange = (filterObj) => {
        this.setState({ filterObj });
        this.props.refreshTable(this.state.page, this.state.sizePerPage, this.state.sortName, this.state.sortOrder, this.convertFilterObj(filterObj), this.props.cache);
    };

    onSizePerPageList = (sizePerPage) => {
        if(this.state.sizePerPage !== sizePerPage){
            this.setState({ sizePerPage });
            this.props.refreshTable(this.state.page, sizePerPage, this.state.sortName, this.state.sortOrder, this.convertFilterObj(this.state.filterObj), this.props.cache);
        }
    };

    onPageChange = (page, sizePerPage) => {
        if(this.state.page !== page){
            this.setState({ page });
            this.props.refreshTable(page, this.state.sizePerPage, this.state.sortName, this.state.sortOrder, this.convertFilterObj(this.state.filterObj), this.props.cache);
        }
    };

    onSelectTableRow = (row, isSelected, e = {}) => {
        this.props.onSelectRow(row, isSelected, e, this.props.cache);
    };

    onSelectTableAllRowsOnCurrentPage = (isSelected, rows) => {
        this.props.onSelectAllRowsOnCurrentPage(isSelected, rows, this.props.cache);
    };

    render() {
        const renderShowsTotal = (start, to, total) => {
            return (
                <p style={{position: 'relative',
                    left: '90%',
                    display: 'inline-block'}}> С {start} по {to} из {total}</p>
            );
        };

        let selectRowProp;
        if(this.props.selectMode === 'multi'){
            selectRowProp = {
                mode: 'checkbox',
                bgColor: '#98CAF1',
                onSelect: this.onSelectTableRow,
                clickToSelect: true,
                onSelectAll: this.onSelectTableAllRowsOnCurrentPage,
                selected: this.props.selectedRows.map(x => x.id),
                onlyUnselectVisible: true
            };
        }
        if(this.props.selectMode === 'single'){
            selectRowProp = {
                mode: 'radio',
                bgColor: '#98CAF1',
                onSelect: this.onSelectTableRow,
                clickToSelect: true,
                selected: this.props.selectedRows.map(x => x.id),
                onlyUnselectVisible: true
            };
        }

        const options = {
            sizePerPageList: [ 10, 25, 50 ],
            sizePerPage: this.state.sizePerPage,
            paginationShowsTotal: renderShowsTotal,
            noDataText: 'No data to display',
            sortName: this.state.sortName,
            sortOrder: this.state.sortOrder,
            onSortChange: this.onSortChange,
            onPageChange: this.onPageChange,
            onFilterChange: this.onFilterChange,
            onSizePerPageList: this.onSizePerPageList,
            page: this.state.page
        };

        const getFilterByType = (info) => {
            if(info.type === 'java.lang.String') return {
                type: 'TextFilter',
                delay: 1000
            };
            if(info.type === 'java.lang.Long') return  {
                type: 'NumberFilter',
                delay: 1000,
                numberComparators: [ '=', '>', '<=' ]
            };
            if(info.type === 'java.util.Date') return {
                type: 'DateFilter'
            };
        };

        let columns = null;

        if (this.props.fieldDescriptionMap !== null && this.props.fieldDescriptionMap !== undefined && Object.keys(this.props.fieldDescriptionMap).length !== 0 || this.props.fieldDescriptionMap.constructor !== Object) {
            columns = Object.keys(this.props.fieldDescriptionMap).map((key) =>
                <TableHeaderColumn
                           ref={FILTER_REF + key}
                           dataSort={ true }
                           filter={ getFilterByType(this.props.fieldDescriptionMap[key])}
                           width={this.props.fieldDescriptionMap[key].width}
                           dataField={this.props.fieldDescriptionMap[key].name}
                           key={this.props.fieldDescriptionMap[key].name}
                           isKey={this.props.fieldDescriptionMap[key].name === 'id'}
                           dataFormat={this.props.fieldDescriptionMap[key].name === 'resume' ? Table.resumeCellFormatter : Table.sameCellFormatter}
                           hidden={this.props.fieldDescriptionMap[key].hidden}>{this.props.fieldDescriptionMap[key].rusName}</TableHeaderColumn>
            )
        }
        let bootstrapTable;
        if (columns != null) {
            bootstrapTable = <BootstrapTable data={this.props.data} hover pagination condensed
                                             ref='bootstrap_table'
                                             fetchInfo={ { dataTotalSize: this.props.totalDataSize } }
                                             remote={ true }
                                             trClassName={this.trClassFormat}
                                             tableHeaderClass='list-form-table-header'
                                             selectRow={selectRowProp}
                                             options={options}>
                                {columns}
                            </BootstrapTable>
        } else {
            bootstrapTable = <div/>
        }
        return (
            <div>
                {bootstrapTable}
            </div>
        )
    }
}
