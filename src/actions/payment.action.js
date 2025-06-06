import { httpClient } from "../utils/HttpClient";
import {
  HTTP_PAYMENT_SUCCESS,
  HTTP_PAYMENT_FETCHING,
  HTTP_PAYMENT_FAILED,
  HTTP_PAYMENT_CLEAR,
  server,
} from "../constants";

export const setStatePaymentToSuccess = (payload) => ({
  type: HTTP_PAYMENT_SUCCESS,
  payload,
});

const setStatePaymentToFetching = () => ({
  type: HTTP_PAYMENT_FETCHING,
});

const setStatePaymentToFailed = () => ({
  type: HTTP_PAYMENT_FAILED,
});

const setStatePaymentToClear = () => ({
  type: HTTP_PAYMENT_CLEAR,
});

export const getPayments = () => {
  return async (dispatch) => {
    dispatch(setStatePaymentToFetching());
    doGetPayments(dispatch);
  };
};

const doGetPayments = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.PAYMENT_URL}`);
    dispatch(setStatePaymentToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStatePaymentToFailed());
  }
};

export const getPaymentsBySupplier = (supplier) => {
  console.log("supplier: " + supplier);
  return async (dispatch) => {
    dispatch(setStatePaymentToFetching());
    doGetPaymentsBySupplier(dispatch, supplier);
  };
};

const doGetPaymentsBySupplier = async (dispatch, supplier) => {
  try {
    let result = await httpClient.get(
      `${server.PAYMENTSUPPLIER_URL}/${supplier}`
    );
    dispatch(setStatePaymentToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStatePaymentToFailed());
  }
};
