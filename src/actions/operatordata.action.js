import { httpClient } from "../utils/HttpClient";
import {
  HTTP_OPERATIONDATA_SUCCESS,
  HTTP_OPERATIONDATA_FETCHING,
  HTTP_OPERATIONDATA_FAILED,
  HTTP_OPERATIONDATA_CLEAR,
  server,
} from "../constants";
import jwt from "jsonwebtoken";

export const setStateOPERATIONDATAToSuccess = (payload) => ({
  type: HTTP_OPERATIONDATA_SUCCESS,
  payload,
});

const setStateOPERATIONDATAToFetching = () => ({
  type: HTTP_OPERATIONDATA_FETCHING,
});

const setStateOPERATIONDATAToFailed = () => ({
  type: HTTP_OPERATIONDATA_FAILED,
});

const setStateOPERATIONDATAToClear = () => ({
  type: HTTP_OPERATIONDATA_CLEAR,
});

// export const getOperationdata = (vID) => {
//   return async (dispatch) => {
//     try {
//       let result = await httpClient.get(`${server.GETOPERATIONDATA_URL}`);
//       dispatch(setStateOPERATIONDATAToFetching(result.data));
//       return result.data;
//     } catch (err) {
//       alert(err.message);
//       dispatch(setStateOPERATIONDATAToFailed());
//     }
//   };
// };

export const getOperationdata = () => {
  return async (dispatch) => {
    dispatch(setStateOPERATIONDATAToFetching());
    dogetOperationdata(dispatch);
  };
};

export const dogetOperationdata = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.GETOPERATIONDATA_URL}`);
    dispatch(setStateOPERATIONDATAToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateOPERATIONDATAToFailed());
  }
};

export const getOperationfilterdata = (fromdate, todate) => {
  return async (dispatch) => {
    dispatch(setStateOPERATIONDATAToFetching());
    dogetOperationfilterdata(fromdate, todate, dispatch);
  };
};

export const dogetOperationfilterdata = async (fromdate, todate, dispatch) => {
  try {
    let result = await httpClient.get(
      `${server.GETOPERATIONFILTERDATA_URL}/${fromdate}/${todate}`
    );
    dispatch(setStateOPERATIONDATAToSuccess(result.data));
  } catch (err) {
    dispatch(setStateOPERATIONDATAToFailed());
  }
};
