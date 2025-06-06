import { httpClient } from "./../utils/HttpClient";
import {
  HTTP_SUPPLIER_SUCCESS,
  HTTP_SUPPLIER_FETCHING,
  HTTP_SUPPLIER_FAILED,
  HTTP_SUPPLIER_CLEAR,
  server,
} from "../constants";

export const setStateSupplierToSuccess = (payload) => ({
  type: HTTP_SUPPLIER_SUCCESS,
  payload,
});

const setStateSupplierToFetching = () => ({
  type: HTTP_SUPPLIER_FETCHING,
});

const setStateSupplierToFailed = () => ({
  type: HTTP_SUPPLIER_FAILED,
});

const setStateSupplierToClear = () => ({
  type: HTTP_SUPPLIER_CLEAR,
});

export const getSuppliers = (prno) => {
  return async (dispatch) => {
    // console.log("PR: " + prno);
    dispatch(setStateSupplierToFetching());
    doGetSupplier(dispatch, prno);
  };
};

const doGetSupplier = async (dispatch, prno) => {
  try {
    let result = await httpClient.get(`${server.SUPPLIER_URL}`);
    // alert(JSON.stringify(result.data));
    dispatch(setStateSupplierToSuccess(result.data));
  } catch (err) {
    dispatch(setStateSupplierToFailed());
  }
};
