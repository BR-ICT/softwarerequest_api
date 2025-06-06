import { httpClient } from "../utils/HttpClient";
import {
  HTTP_MONITORINGRECEIPT_SUCCESS,
  HTTP_MONITORINGRECEIPT_FETCHING,
  HTTP_MONITORINGRECEIPT_FAILED,
  HTTP_MONITORINGRECEIPT_CLEAR,
  server,
} from "../constants";

export const setStateMonitoringReceiptToSuccess = (payload) => ({
  type: HTTP_MONITORINGRECEIPT_SUCCESS,
  payload,
});

export const setStateMonitoringReceiptToFetching = () => ({
  type: HTTP_MONITORINGRECEIPT_FETCHING,
});

const setStateMonitoringReceiptToFailed = () => ({
  type: HTTP_MONITORINGRECEIPT_FAILED,
});

const setStateMonitoringReceiptToClear = () => ({
  type: HTTP_MONITORINGRECEIPT_CLEAR,
});

export const getMonitoringReceipt = () => {
  return async (dispatch) => {
    dispatch(setStateMonitoringReceiptToFetching());
    doGetMonitoringReceipt(dispatch);
  };
};

export const doGetMonitoringReceipt = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.MONITORINGRECEIPT_URL}`);
    dispatch(setStateMonitoringReceiptToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateMonitoringReceiptToFailed());
  }
};

export const fetchMonitoringReceipt = () => {
  return async (dispatch) => {
    dofetchMonitoringReceipt(dispatch);
    // try {
    //   let result = await httpClient.get(`${server.MONITORINGRECEIPT_URL}`);
    //   dispatch(setStateMonitoringReceiptToSuccess(result.data));
    //   // alert(JSON.stringify(result.data));
    // } catch (err) {
    //   dispatch(setStateMonitoringReceiptToFailed());
    // }
  };
};

const dofetchMonitoringReceipt = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.MONITORINGRECEIPT_URL}`);
    dispatch(setStateMonitoringReceiptToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateMonitoringReceiptToFailed());
  }
};

export const addMoniringreceipt = (formData) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(server.ADDMONITORINGRECEIPT, formData);
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};
