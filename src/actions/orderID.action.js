import { httpClient } from "../utils/HttpClient";
import {
  HTTP_ORDERID_SUCCESS,
  HTTP_ORDERID_FETCHING,
  HTTP_ORDERID_FAILED,
  HTTP_ORDERID_CLEAR,
  server,
} from "../constants";
import jwt from "jsonwebtoken";

export const setStateORDERIDToSuccess = (payload) => ({
  type: HTTP_ORDERID_SUCCESS,
  payload,
});

const setStateORDERIDToFetching = () => ({
  type: HTTP_ORDERID_FETCHING,
});

const setStateORDERIDToFailed = () => ({
  type: HTTP_ORDERID_FAILED,
});

const setStateORDERIDToClear = () => ({
  type: HTTP_ORDERID_CLEAR,
});

export const addOrderID = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(
        server.INSERTVISITORHEADER_URL,
        formData
      );
      dispatch(setStateORDERIDToSuccess(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
      dispatch(setStateORDERIDToFailed());
    }
  };
};
