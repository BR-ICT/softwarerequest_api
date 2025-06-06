import { httpClient } from "../utils/HttpClient";
import {
  HTTP_SWRID_SUCCESS,
  HTTP_SWRID_FETCHING,
  HTTP_SWRID_FAILED,
  HTTP_SWRID_CLEAR,
  server,
} from "../constants";
import jwt from "jsonwebtoken";

export const setStateSWRIDToSuccess = (payload) => ({
  type: HTTP_SWRID_SUCCESS,
  payload,
});

const setStateSWRIDToFetching = () => ({
  type: HTTP_SWRID_FETCHING,
});

const setStateSWRIDToFailed = () => ({
  type: HTTP_SWRID_FAILED,
});

const setStateSWRIDToClear = () => ({
  type: HTTP_SWRID_CLEAR,
});

export const doGetID1 = () => {
  return async (dispatch) => {
    try {
      let result = await httpClient.get(`${server.GETNEWID_URL}`);
      dispatch(setStateSWRIDToSuccess(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
      dispatch(setStateSWRIDToFailed());
    }
  };
};

export const doGetID = () => {
  return async (dispatch) => {
    try {
      let result = await httpClient.get(`${server.GETID_URL}`);
      dispatch(setStateSWRIDToSuccess(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
      dispatch(setStateSWRIDToFailed());
    }
  };
};
