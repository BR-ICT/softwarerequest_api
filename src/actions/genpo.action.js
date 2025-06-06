import { httpClient } from "./../utils/HttpClient";
import {
  HTTP_GENPO_SUCCESS,
  HTTP_GENPO_FETCHING,
  HTTP_GENPO_FAILED,
  HTTP_GENPO_CLEAR,
  server,
} from "../constants";
import * as prdetailbuyerActions from "./prdetailbuyer.action";

export const setStateGenPOToSuccess = (payload) => ({
  type: HTTP_GENPO_SUCCESS,
  payload,
});

const setStateGenPOToFetching = () => ({
  type: HTTP_GENPO_FETCHING,
});

const setStateGenPOToFailed = () => ({
  type: HTTP_GENPO_FAILED,
});

const setStateGenPOToClear = () => ({
  type: HTTP_GENPO_CLEAR,
});

export const getPONumber = () => {
  let result = "0123456";
  return result;
};

export const genPONumber = (status, prnoline, delivery, payment) => {
  return async (dispatch) => {
    // console.log("PR: " + prno + " STS: " + status);
    dispatch(setStateGenPOToFetching());
    doGenPONumber(dispatch, status, prnoline, delivery, payment);
  };
};

const doGenPONumber = async (dispatch, status, prnoline, delivery, payment) => {
  try {
    let result = await httpClient.post(
      `${server.PRGENPO_URL}/${status}/${prnoline}/${delivery}/${payment}`
    );
    alert(JSON.stringify(result.data));
    dispatch(setStateGenPOToSuccess(result.data));
  } catch (err) {
    dispatch(setStateGenPOToFailed());
  }
};

export const cancelPONumber = (pono) => {
  return async (dispatch) => {
    // console.log("PR: " + prno + " STS: " + status);
    dispatch(setStateGenPOToFetching());
    doCancelPONumber(dispatch, pono);
  };
};

const doCancelPONumber = async (dispatch, pono) => {
  try {
    let result = await httpClient.post(`${server.CANCELPO_URL}/${pono}`);
    alert(JSON.stringify(result.data));
    dispatch(setStateGenPOToSuccess(result.data));
  } catch (err) {
    dispatch(setStateGenPOToFailed());
  }
};

export const changePO = (prno, orderdate, delidate, payment) => {
  return async (dispatch) => {
    dispatch(setStateGenPOToFetching());
    doChangePO(dispatch, prno, orderdate, delidate, payment);
  };
};

const doChangePO = async (dispatch, prno, orderdate, delidate, payment) => {
  try {
    let result = await httpClient.post(
      `${server.CHANGEPO_URL}/${prno}/${orderdate}/${delidate}/${payment}`
      // `${server.CHANGEPO_URL}/${prno}/${orderdate}/${delidate}`
    );
    alert(JSON.stringify(result.data));
    dispatch(setStateGenPOToSuccess(result.data));
  } catch (err) {
    dispatch(setStateGenPOToFailed());
  }
};

export const changeConfirmDatePO = (prno, line, date) => {
  return async (dispatch) => {
    dispatch(setStateGenPOToFetching());
    doConfirmDatePO(dispatch, prno, line, date);
  };
};

const doConfirmDatePO = async (dispatch, prno, line, date) => {
  try {
    await httpClient.post(
      `${server.CHANGECONFIRMDATEPO_URL}/${prno}/${line}/${date}`
    );

    let result = await httpClient.get(`${server.PODETAIL_URL}/${prno}`);
    dispatch(prdetailbuyerActions.setStatePRDetailBuyerToSuccess(result.data));

    // alert(JSON.stringify(result.data));
    // dispatch(setStateGenPOToSuccess(result.data));
  } catch (err) {
    dispatch(setStateGenPOToFailed());
  }
};
