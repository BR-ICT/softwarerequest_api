import { httpClient } from "./../utils/HttpClient";
import {
  HTTP_STATUS_SUCCESS,
  HTTP_STATUS_FETCHING,
  HTTP_STATUS_FAILED,
  HTTP_STATUS_CLEAR,
  server,
} from "../constants";

export const setStateStatusToSuccess = (payload) => ({
  type: HTTP_STATUS_SUCCESS,
  payload,
});

const setStateStatusToFetching = () => ({
  type: HTTP_STATUS_FETCHING,
});

const setStateStatusToFailed = () => ({
  type: HTTP_STATUS_FAILED,
});

const setStateStatusToClear = () => ({
  type: HTTP_STATUS_CLEAR,
});

export const getStatuses = () => {
  return async (dispatch) => {
    dispatch(setStateStatusToFetching());
    doGetStatus(dispatch);
  };
};

const doGetStatus = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.EPRSTATUS_URL}`);
    dispatch(setStateStatusToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateStatusToFailed());
  }
};
