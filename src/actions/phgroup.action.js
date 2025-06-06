import { httpClient } from "./../utils/HttpClient";
import {
  HTTP_PHGROUP_SUCCESS,
  HTTP_PHGROUP_FETCHING,
  HTTP_PHGROUP_FAILED,
  HTTP_PHGROUP_CLEAR,
  server,
} from "../constants";

export const setStatePHGroupToSuccess = (payload) => ({
  type: HTTP_PHGROUP_SUCCESS,
  payload,
});

const setStatePHGroupToFetching = () => ({
  type: HTTP_PHGROUP_FETCHING,
});

const setStatePHGroupToFailed = () => ({
  type: HTTP_PHGROUP_FAILED,
});

const setStatePHGroupToClear = () => ({
  type: HTTP_PHGROUP_CLEAR,
});

export const getPHGroups = (dept) => {
  return async (dispatch) => {
    // console.log("dept: " + dept);
    dispatch(setStatePHGroupToFetching());
    doGetPHGroup(dispatch, dept);
  };
};

const doGetPHGroup = async (dispatch, dept) => {
  try {
    let result = await httpClient.get(`${server.PHGROUP_URL}/${dept}`);
    dispatch(setStatePHGroupToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStatePHGroupToFailed());
  }
};
