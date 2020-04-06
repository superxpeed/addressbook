import * as types from "../Common/Utils";
import { Caches, OrgTypes } from "../Common/Utils";
import * as tableActions from "../Table/TableActions";

const initialState = {
  tableDataOrganization: {
    data: [],
  },
  fieldDescriptionMapOrganization: {},
  totalDataSizeOrganization: 0,
  tableDataPerson: {
    data: [],
  },
  fieldDescriptionMapPerson: {},
  totalDataSizePerson: 0,
  selectedRowsPerson: [],
  selectedRowsOrganization: [],
};

export default function universalListReducer(
  state = initialState,
  action = {}
) {
  switch (action.type) {
    case types.GET_LIST + Caches.ORGANIZATION_CACHE + types.SUCCESS:
      return Object.assign({}, state, {
        tableDataOrganization: action.data,
        fieldDescriptionMapOrganization: action.fieldDescriptionMap,
        totalDataSizeOrganization: action.data.totalDataSize,
      });
    case types.GET_LIST + Caches.PERSON_CACHE + types.SUCCESS:
      return Object.assign({}, state, {
        tableDataPerson: action.data,
        fieldDescriptionMapPerson: action.fieldDescriptionMap,
        totalDataSizePerson: action.data.totalDataSize,
      });
    case tableActions.ON_SELECT_ROW + Caches.ORGANIZATION_CACHE:
      if (action.isSelected) {
        if (undefined === action.ctrlKey) {
          return Object.assign({}, state, {
            selectedRowsOrganization: [
              ...state.selectedRowsOrganization,
              action.row,
            ],
          });
        } else if (action.ctrlKey) {
          return Object.assign({}, state, {
            selectedRowsOrganization: [
              ...state.selectedRowsOrganization,
              action.row,
            ],
          });
        } else {
          return Object.assign({}, state, {
            selectedRowsOrganization: [action.row],
          });
        }
      } else {
        return Object.assign({}, state, {
          selectedRowsOrganization: state.selectedRowsOrganization.filter(
            (it) => it.id !== action.row.id
          ),
        });
      }
    case tableActions.ON_SELECT_ROW + Caches.PERSON_CACHE:
      if (action.isSelected) {
        if (undefined === action.ctrlKey) {
          return Object.assign({}, state, {
            selectedRowsPerson: [...state.selectedRowsPerson, action.row],
          });
        } else if (action.ctrlKey) {
          return Object.assign({}, state, {
            selectedRowsPerson: [...state.selectedRowsPerson, action.row],
          });
        } else {
          return Object.assign({}, state, {
            selectedRowsPerson: [action.row],
          });
        }
      } else {
        return Object.assign({}, state, {
          selectedRowsPerson: state.selectedRowsPerson.filter(
            (it) => it.id !== action.row.id
          ),
        });
      }

    case tableActions.UPDATE_ROW_IN_TABLE + Caches.PERSON_CACHE: {
      let newSelected = state.selectedRowsPerson.filter(
        (it) => it.id !== action.row.id
      );
      let newTableData = state.tableDataPerson.data.filter(
        (it) => it.id !== action.row.id
      );
      newSelected.push(action.row);
      newTableData.push(action.row);
      return Object.assign({}, state, {
        tableDataPerson: {
          data: newTableData,
          totalDataSize: state.tableDataPerson.totalDataSize,
        },
        selectedRowsPerson: newSelected,
      });
    }

    case tableActions.ADD_ROW_TO_TABLE + Caches.PERSON_CACHE: {
      let newSelected = state.selectedRowsPerson;
      let prevSize = state.tableDataPerson.totalDataSize;
      let newTableData = state.tableDataPerson.data;
      newSelected.push(action.row);
      newTableData.push(action.row);
      return Object.assign({}, state, {
        tableDataPerson: { data: newTableData, totalDataSize: prevSize + 1 },
        selectedRowsPerson: newSelected,
      });
    }

    case tableActions.UPDATE_ROW_IN_TABLE + Caches.ORGANIZATION_CACHE: {
      let newTableData = state.tableDataOrganization.data.filter(
        (it) => it.id !== action.row.id
      );
      let org = Object.assign({}, action.row);
      org["type"] = OrgTypes.getNumType(action.row["type"]);
      newTableData.push(org);
      return Object.assign({}, state, {
        tableDataOrganization: {
          data: newTableData,
          totalDataSize: state.tableDataOrganization.totalDataSize,
        },
        selectedRowsOrganization: [org],
      });
    }

    case tableActions.ADD_ROW_TO_TABLE + Caches.ORGANIZATION_CACHE: {
      let prevSize = state.tableDataOrganization.totalDataSize;
      let newTableData = state.tableDataOrganization.data;
      let org = Object.assign({}, action.row);
      org["type"] = OrgTypes.getNumType(action.row["type"]);
      newTableData.push(org);
      return Object.assign({}, state, {
        tableDataOrganization: {
          data: newTableData,
          totalDataSize: prevSize + 1,
        },
        selectedRowsOrganization: [org],
      });
    }

    case tableActions.ON_SELECT_ALL_ROWS_ON_CURRENT_PAGE + Caches.PERSON_CACHE:
      if (action.isSelected) {
        return Object.assign({}, state, {
          selectedRowsPerson: [...state.selectedRowsPerson, ...action.rows],
        });
      } else {
        return Object.assign({}, state, {
          selectedRowsPerson: state.selectedRowsPerson.filter(
            (x) => action.rows.indexOf(x) < 0
          ),
        });
      }
    case tableActions.ON_SELECT_ALL_ROWS_ON_CURRENT_PAGE +
      Caches.ORGANIZATION_CACHE:
      if (action.isSelected) {
        return Object.assign({}, state, {
          selectedRowsOrganization: [
            ...state.selectedRowsOrganization,
            ...action.rows,
          ],
        });
      } else {
        return Object.assign({}, state, {
          selectedRowsOrganization: state.selectedRowsOrganization.filter(
            (x) => action.rows.indexOf(x) < 0
          ),
        });
      }
    default:
      return state;
  }
}
