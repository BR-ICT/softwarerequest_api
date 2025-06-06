import { httpClient } from "./../utils/HttpClient";
import {
  HTTP_APPROVE_SUCCESS,
  HTTP_APPROVE_FETCHING,
  HTTP_APPROVE_FAILED,
  HTTP_APPROVE_CLEAR,
  server,
} from "../constants";

export const setStateApproveToSuccess = (payload) => ({
  type: HTTP_APPROVE_SUCCESS,
  payload,
});

const setStateApproveToFetching = () => ({
  type: HTTP_APPROVE_FETCHING,
});

const setStateApproveToFailed = () => ({
  type: HTTP_APPROVE_FAILED,
});

const setStateApproveToClear = () => ({
  type: HTTP_APPROVE_CLEAR,
});

export const getApproves = () => {
  return async (dispatch) => {
    dispatch(setStateApproveToFetching());
    doGetApproves(dispatch);
  };
};

const doGetApproves = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.APPROVE_URL}`);
    dispatch(setStateApproveToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateApproveToFailed());
  }
};
