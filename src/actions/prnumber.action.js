import { httpClient } from "./../utils/HttpClient";
import {
  HTTP_PRNUMBER_SUCCESS,
  HTTP_PRNUMBER_FETCHING,
  HTTP_PRNUMBER_FAILED,
  HTTP_PRNUMBER_CLEAR,
  server,
} from "../constants";

export const setStatePRNumberToSuccess = (payload) => ({
  type: HTTP_PRNUMBER_SUCCESS,
  payload,
});

const setStatePRNumberToFetching = () => ({
  type: HTTP_PRNUMBER_FETCHING,
});

const setStatePRNumberToFailed = () => ({
  type: HTTP_PRNUMBER_FAILED,
});

const setStatePRNumberToClear = () => ({
  type: HTTP_PRNUMBER_CLEAR,
});

export const getEPRNumbers = (fromStatus, toStatus) => {
  return async (dispatch) => {
    dispatch(setStatePRNumberToFetching());
    doGetEPRNumbers(dispatch, fromStatus, toStatus);
  };
};

const doGetEPRNumbers = async (dispatch, fromStatus, toStatus) => {
  try {
    let result = await httpClient.get(
      `${server.EPRNUMBER_URL}/${fromStatus}/${toStatus}`
    );
    dispatch(setStatePRNumberToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    // localStorage.removeItem(server.TOKEN_KEY);
    dispatch(setStatePRNumberToFailed());
  }
};

export const getEPRNumbersWithOutUser = () => {
  return async (dispatch) => {
    dispatch(setStatePRNumberToFetching());
    doGetEPRNumbersWithOutUser(dispatch);
  };
};

const doGetEPRNumbersWithOutUser = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.EPRNUMBERWITHOUTUSER_URL}`);
    dispatch(setStatePRNumberToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    // localStorage.removeItem(server.TOKEN_KEY);
    dispatch(setStatePRNumberToFailed());
  }
};

export const getEPRNumbersUser = (status) => {
  return async (dispatch) => {
    dispatch(setStatePRNumberToFetching(status));
    doGetEPRNumbersUser(dispatch, status);
  };
};

const doGetEPRNumbersUser = async (dispatch, status) => {
  try {
    let result = await httpClient.get(`${server.EPRNUMBERUSER_URL}/${status}`);
    dispatch(setStatePRNumberToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    // localStorage.removeItem(server.TOKEN_KEY);
    dispatch(setStatePRNumberToFailed());
  }
};

export const getPONumbersCancel = (status) => {
  return async (dispatch) => {
    dispatch(setStatePRNumberToFetching(status));
    doGetPONumbersCancel(dispatch, status);
  };
};

const doGetPONumbersCancel = async (dispatch, status) => {
  try {
    let result = await httpClient.get(`${server.EPRNUMBERUSER_URL}/${status}`);
    dispatch(setStatePRNumberToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    // localStorage.removeItem(server.TOKEN_KEY);
    dispatch(setStatePRNumberToFailed());
  }
};
