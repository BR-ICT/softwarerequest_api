import { httpClient } from "../utils/HttpClient";
import {
  HTTP_MARNUMBER_SUCCESS,
  HTTP_MARNUMBER_FETCHING,
  HTTP_MARNUMBER_FAILED,
  HTTP_MARNUMBER_CLEAR,
  server,
} from "../constants";

export const setStateMARNumberToSuccess = (payload) => ({
  type: HTTP_MARNUMBER_SUCCESS,
  payload,
});

const setStateMARNumberToFetching = () => ({
  type: HTTP_MARNUMBER_FETCHING,
});

const setStateMARNumberToFailed = () => ({
  type: HTTP_MARNUMBER_FAILED,
});

const setStateMARNumberToClear = () => ({
  type: HTTP_MARNUMBER_CLEAR,
});

export const getMARNumber = (prefix, fromStatus, toStatus) => {
  return async (dispatch) => {
    dispatch(setStateMARNumberToFetching());
    doGetMARNumber(dispatch, prefix, fromStatus, toStatus);
  };
};

const doGetMARNumber = async (dispatch, prefix, fromStatus, toStatus) => {
  try {
    let result = await httpClient.get(
      `${server.MARNUMBER_URL}/${prefix}/${fromStatus}/${toStatus}`
    );
    dispatch(setStateMARNumberToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateMARNumberToFailed());
  }
};

export const getMARNumberMonitoring = (prefix, fromStatus, toStatus) => {
  return async (dispatch) => {
    dispatch(setStateMARNumberToFetching());
    doGetMARNumberMonitoring(dispatch, prefix, fromStatus, toStatus);
  };
};

const doGetMARNumberMonitoring = async (
  dispatch,
  prefix,
  fromStatus,
  toStatus
) => {
  try {
    let result = await httpClient.get(
      `${server.MARNUMBERMONITORING_URL}/${prefix}/${fromStatus}/${toStatus}`
    );
    dispatch(setStateMARNumberToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateMARNumberToFailed());
  }
};

export const getOrderNumber = (prefix) => {
  return async (dispatch) => {
    dispatch(setStateMARNumberToFetching());
    doGetOrderNumber(dispatch, prefix);
  };
};

const doGetOrderNumber = async (dispatch, prefix) => {
  try {
    let result = await httpClient.get(`${server.ORDERNUMBER_URL}/${prefix}`);
    dispatch(setStateMARNumberToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateMARNumberToFailed());
  }
};

export const getADRNumberAccountant = (fromStatus, toStatus) => {
  return async (dispatch) => {
    dispatch(setStateMARNumberToFetching());
    doGetADRNumberAccountant(dispatch, fromStatus, toStatus);
  };
};

const doGetADRNumberAccountant = async (dispatch, fromStatus, toStatus) => {
  try {
    let result = await httpClient.get(
      `${server.ADRNUMBERACCOUNTANT_URL}/${fromStatus}/${toStatus}`
    );
    dispatch(setStateMARNumberToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateMARNumberToFailed());
  }
};

export const addADRNumber = (dispatch, orderno) => {
  dispatch(setStateMARNumberToFetching());
  return async (dispatch) => {
    try {
      await dispatch(setStateMARNumberToSuccess(orderno));
    } catch (err) {
      // alert(err.message);
      dispatch(setStateMARNumberToFailed());
    }
  };
};
