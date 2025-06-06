import { httpClient } from "./../utils/HttpClient";
import {
  HTTP_DEPARTMENT_SUCCESS,
  HTTP_DEPARTMENT_FETCHING,
  HTTP_DEPARTMENT_FAILED,
  HTTP_DEPARTMENT_CLEAR,
  server,
} from "../constants";

export const setStateDepartmentToSuccess = (payload) => ({
  type: HTTP_DEPARTMENT_SUCCESS,
  payload,
});

const setStateDepartmentToFetching = () => ({
  type: HTTP_DEPARTMENT_FETCHING,
});

const setStateDepartmentToFailed = () => ({
  type: HTTP_DEPARTMENT_FAILED,
});

const setStateDepartmentToClear = () => ({
  type: HTTP_DEPARTMENT_CLEAR,
});

export const getDepartments = (bu) => {
  return async (dispatch) => {
    dispatch(setStateDepartmentToFetching());
    doGetDepartment(dispatch, bu);
  };
};

const doGetDepartment = async (dispatch, bu) => {
  try {
    let result = await httpClient.get(`${server.DEPARTMENT_URL}/${bu}`);
    dispatch(setStateDepartmentToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateDepartmentToFailed());
  }
};
