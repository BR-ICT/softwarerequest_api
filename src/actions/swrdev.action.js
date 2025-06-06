import { httpClient } from "../utils/HttpClient";
import {
  HTTP_SWRDEV_SUCCESS,
  HTTP_SWRDEV_FETCHING,
  HTTP_SWRDEV_FAILED,
  HTTP_SWRDEV_CLEAR,
  server,
} from "../constants";
import jwt from "jsonwebtoken";

export const setStateSWRDEVToSuccess = (payload) => ({
  type: HTTP_SWRDEV_SUCCESS,
  payload,
});

const setStateSWRDEVToFetching = () => ({
  type: HTTP_SWRDEV_FETCHING,
});

const setStateSWRDEVToFailed = () => ({
  type: HTTP_SWRDEV_FAILED,
});

const setStateSWRDEVToClear = () => ({
  type: HTTP_SWRDEV_CLEAR,
});

export const getDev = () => {
  return async (dispatch) => {
    dispatch(setStateSWRDEVToSuccess());
    doGetDev(dispatch);
  };
};

const doGetDev = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.GETDEV}`);
    dispatch(setStateSWRDEVToSuccess(result.data));
  } catch (err) {
    dispatch(setStateSWRDEVToFailed());
  }
};
