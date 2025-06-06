import { httpClient } from "../utils/HttpClient";
import {
  HTTP_ROOMCARD_SUCCESS,
  HTTP_ROOMCARD_FETCHING,
  HTTP_ROOMCARD_FAILED,
  HTTP_ROOMCARD_CLEAR,
  server,
} from "../constants";

export const setStateROOMCARDToSuccess = (payload) => ({
  type: HTTP_ROOMCARD_SUCCESS,
  payload,
});

const setStateROOMCARDToFetching = () => ({
  type: HTTP_ROOMCARD_FETCHING,
});

const setStateROOMCARDToFailed = () => ({
  type: HTTP_ROOMCARD_FAILED,
});

const setStateROOMCARDToClear = () => ({
  type: HTTP_ROOMCARD_CLEAR,
});

export const updateROOMCARD = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.updateROOMCARD_URL, formData);
      // alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const updateEMP = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.updateEMP_URL, formData);
      // alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};
