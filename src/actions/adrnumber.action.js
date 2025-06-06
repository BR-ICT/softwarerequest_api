import { httpClient } from "../utils/HttpClient";
import {
  HTTP_ADRNUMBER_SUCCESS,
  HTTP_ADRNUMBER_FETCHING,
  HTTP_ADRNUMBER_FAILED,
  HTTP_ADRNUMBER_CLEAR,
  server,
} from "../constants";

export const setStateADRNumberToSuccess = (payload) => ({
  type: HTTP_ADRNUMBER_SUCCESS,
  payload,
});

const setStateADRNumberToFetching = () => ({
  type: HTTP_ADRNUMBER_FETCHING,
});

const setStateADRNumberToFailed = () => ({
  type: HTTP_ADRNUMBER_FAILED,
});

const setStateADRNumberToClear = () => ({
  type: HTTP_ADRNUMBER_CLEAR,
});

export const getADRNumber = (fromStatus, toStatus) => {
  return async (dispatch) => {
    dispatch(setStateADRNumberToFetching());
    doGetADRNumber(dispatch, fromStatus, toStatus);
  };
};

const doGetADRNumber = async (dispatch, fromStatus, toStatus) => {
  try {
    let result = await httpClient.get(
      `${server.ADRNUMBER_URL}/${fromStatus}/${toStatus}`
    );
    dispatch(setStateADRNumberToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateADRNumberToFailed());
  }
};

export const getADRNumberAccountant = (fromStatus, toStatus) => {
  return async (dispatch) => {
    dispatch(setStateADRNumberToFetching());
    doGetADRNumberAccountant(dispatch, fromStatus, toStatus);
  };
};

const doGetADRNumberAccountant = async (dispatch, fromStatus, toStatus) => {
  try {
    let result = await httpClient.get(
      `${server.ADRNUMBERACCOUNTANT_URL}/${fromStatus}/${toStatus}`
    );
    dispatch(setStateADRNumberToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateADRNumberToFailed());
  }
};

export const addADRNumber = (dispatch, orderno) => {
  dispatch(setStateADRNumberToFetching());
  return async (dispatch) => {
    try {
      await dispatch(setStateADRNumberToSuccess(orderno));
    } catch (err) {
      // alert(err.message);
      dispatch(setStateADRNumberToFailed());
    }
  };
};
