import { httpClient } from "../utils/HttpClient";
import {
  HTTP_CHECKOUT_SUCCESS,
  HTTP_CHECKOUT_FETCHING,
  HTTP_CHECKOUT_FAILED,
  HTTP_CHECKOUT_CLEAR,
  server,
} from "../constants";

export const setStateCHECKOUTToSuccess = (payload) => ({
  type: HTTP_CHECKOUT_SUCCESS,
  payload,
});

const setStateCHECKOUTToFetching = () => ({
  type: HTTP_CHECKOUT_FETCHING,
});

const setStateCHECKOUTToFailed = () => ({
  type: HTTP_CHECKOUT_FAILED,
});

const setStateCHECKOUTToClear = () => ({
  type: HTTP_CHECKOUT_CLEAR,
});

export const checkOut = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.CHECKOUT_URL, formData);
      //alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const checkOut1 = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.CHECKOUT1_URL, formData);
      //alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const checkOutwithdatetime = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(
        server.CHECKOUTWITHDATETIME_URL,
        formData
      );
      //alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};
