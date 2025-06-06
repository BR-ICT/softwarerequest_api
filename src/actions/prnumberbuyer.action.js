import { httpClient } from "./../utils/HttpClient";
import {
  HTTP_PRNUMBERBUYER_SUCCESS,
  HTTP_PRNUMBERBUYER_FETCHING,
  HTTP_PRNUMBERBUYER_FAILED,
  HTTP_PRNUMBERBUYER_CLEAR,
  server,
} from "../constants";
import {
  BrowserRouter as Router,
  Route,
  Redirect,
  Switch,
} from "react-router-dom";

export const setStatePRNumberBuyerToSuccess = (payload) => ({
  type: HTTP_PRNUMBERBUYER_SUCCESS,
  payload,
});

const setStatePRNumberBuyerToFetching = () => ({
  type: HTTP_PRNUMBERBUYER_FETCHING,
});

const setStatePRNumberBuyerToFailed = () => ({
  type: HTTP_PRNUMBERBUYER_FAILED,
});

const setStatePRNumberBuyerToClear = () => ({
  type: HTTP_PRNUMBERBUYER_CLEAR,
});

export const getEPRNumbers = (fromStatus, toStatus) => {
  return async (dispatch) => {
    dispatch(setStatePRNumberBuyerToFetching());
    doGetEPRNumbers(dispatch, fromStatus, toStatus);
  };
};

const doGetEPRNumbers = async (dispatch, fromStatus, toStatus) => {
  try {
    let result = await httpClient.get(
      `${server.EPRNUMBERBUYER_URL}/${fromStatus}/${toStatus}`
    );
    dispatch(setStatePRNumberBuyerToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    // localStorage.removeItem(server.TOKEN_KEY);
    dispatch(setStatePRNumberBuyerToFailed());
  }
};

export const getEPRNumbersGrouping = (statusHead, statusLine) => {
  return async (dispatch) => {
    dispatch(setStatePRNumberBuyerToFetching());
    doGetEPRNumbersGrouping(dispatch, statusHead, statusLine);
  };
};

const doGetEPRNumbersGrouping = async (dispatch, statusHead, statusLine) => {
  try {
    let result = await httpClient.get(
      `${server.EPRNUMBERGROUPING_URL}/${statusHead}/${statusLine}`
    );
    dispatch(setStatePRNumberBuyerToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    // localStorage.removeItem(server.TOKEN_KEY);
    dispatch(setStatePRNumberBuyerToFailed());
  }
};

export const getEPRNumbersGenPO = (statusHead, statusLine) => {
  return async (dispatch) => {
    dispatch(setStatePRNumberBuyerToFetching());
    doGetEPRNumbersGenPO(dispatch, statusHead, statusLine);
  };
};

const doGetEPRNumbersGenPO = async (dispatch, status) => {
  try {
    let result = await httpClient.get(`${server.EPRNUMBERGENPO_URL}/${status}`);
    dispatch(setStatePRNumberBuyerToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    // localStorage.removeItem(server.TOKEN_KEY);
    dispatch(setStatePRNumberBuyerToFailed());
  }
};

export const getPONumbers = (fromstatus, tostatus) => {
  return async (dispatch) => {
    dispatch(setStatePRNumberBuyerToFetching());
    doGetPONumber(dispatch, fromstatus, tostatus);
  };
};

const doGetPONumber = async (dispatch, fromstatus, tostatus) => {
  try {
    let result = await httpClient.get(
      `${server.PONUMBER_URL}/${fromstatus}/${tostatus}`
    );
    dispatch(setStatePRNumberBuyerToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    // localStorage.removeItem(server.TOKEN_KEY);
    dispatch(setStatePRNumberBuyerToFailed());
  }
};

export const getPRNumberReserv = (status) => {
  return async (dispatch) => {
    dispatch(setStatePRNumberBuyerToFetching());
    doGetPRNumberReserv(dispatch, status);
  };
};

const doGetPRNumberReserv = async (dispatch, status) => {
  try {
    let result = await httpClient.get(
      `${server.EPRNUMBERRESERV_URL}/${status}`
    );
    dispatch(setStatePRNumberBuyerToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    // localStorage.removeItem(server.TOKEN_KEY);
    dispatch(setStatePRNumberBuyerToFailed());
  }
};
