import { httpClient } from "./../utils/HttpClient";
import {
  HTTP_DEPTANDCOST_SUCCESS,
  HTTP_DEPTANDCOST_FETCHING,
  HTTP_DEPTANDCOST_FAILED,
  HTTP_DEPTANDCOST_CLEAR,
  server,
} from "../constants";

export const setStateDeptAndCostToSuccess = (payload) => ({
  type: HTTP_DEPTANDCOST_SUCCESS,
  payload,
});

const setStateDeptAndCostToFetching = () => ({
  type: HTTP_DEPTANDCOST_FETCHING,
});

const setStateDeptAndCostToFailed = () => ({
  type: HTTP_DEPTANDCOST_FAILED,
});

const setStateDeptAndCostToClear = () => ({
  type: HTTP_DEPTANDCOST_CLEAR,
});

export const getDeptAndCosts = () => {
  return async (dispatch) => {
    dispatch(setStateDeptAndCostToFetching());
    doGetDeptAndCost(dispatch);
  };
};

const doGetDeptAndCost = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.DEPTANDCOST_URL}`);
    dispatch(setStateDeptAndCostToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateDeptAndCostToFailed());
  }
};
