import { httpClient } from "./../utils/HttpClient";
import {
  HTTP_COSTCENTER_SUCCESS,
  HTTP_COSTCENTER_FETCHING,
  HTTP_COSTCENTER_FAILED,
  HTTP_COSTCENTER_CLEAR,
  server,
} from "../constants";

export const setStateCostCenterToSuccess = (payload) => ({
  type: HTTP_COSTCENTER_SUCCESS,
  payload,
});

const setStateCostCenterToFetching = () => ({
  type: HTTP_COSTCENTER_FETCHING,
});

const setStateCostCenterToFailed = () => ({
  type: HTTP_COSTCENTER_FAILED,
});

const setStateCostCenterToClear = () => ({
  type: HTTP_COSTCENTER_CLEAR,
});

export const getCostCenters = (department) => {
  return async (dispatch) => {
    dispatch(setStateCostCenterToFetching());
    dogetCostCenter(dispatch, department);
  };
};

const dogetCostCenter = async (dispatch, department) => {
  try {
    let result = await httpClient.get(`${server.COSTCENTER_URL}/${department}`);
    dispatch(setStateCostCenterToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateCostCenterToFailed());
  }
};

export const getCostCentersWithOutDepartment = () => {
  return async (dispatch) => {
    dispatch(setStateCostCenterToFetching());
    dogetCostCenterWithOutDepartment(dispatch);
  };
};

const dogetCostCenterWithOutDepartment = async (dispatch) => {
  try {
    let result = await httpClient.get(
      `${server.COSTCENTERWITHOUTDEPARTMENT_URL}`
    );
    dispatch(setStateCostCenterToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateCostCenterToFailed());
  }
};

export const getCostCentersBU = (bu) => {
  return async (dispatch) => {
    dispatch(setStateCostCenterToFetching());
    dogetCostCenterBU(dispatch, bu);
  };
};

const dogetCostCenterBU = async (dispatch, bu) => {
  try {
    let result = await httpClient.get(`${server.COSTCENTERBU_URL}/${bu}`);
    dispatch(setStateCostCenterToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateCostCenterToFailed());
  }
};
