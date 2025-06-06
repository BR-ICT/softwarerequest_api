import { httpClient } from "../utils/HttpClient";
import {
  HTTP_GETIMAGE_SUCCESS,
  HTTP_GETIMAGE_FETCHING,
  HTTP_GETIMAGE_FAILED,
  HTTP_GETIMAGE_CLEAR,
  server,
} from "../constants";
import jwt from "jsonwebtoken";

export const setStateGETIMAGEToSuccess = (payload) => ({
  type: HTTP_GETIMAGE_SUCCESS,
  payload,
});

const setStateGETIMAGEToFetching = () => ({
  type: HTTP_GETIMAGE_FETCHING,
});

const setStateGETIMAGEToFailed = () => ({
  type: HTTP_GETIMAGE_FAILED,
});

const setStateGETIMAGEToClear = () => ({
  type: HTTP_GETIMAGE_CLEAR,
});

export const getImage = (vID) => {
  return async (dispatch) => {
    dispatch(setStateGETIMAGEToFetching());
    dogetImage(dispatch, vID);
  };
};

export const dogetImage = async (dispatch, vID) => {
  try {
    let result = await httpClient.get(`${server.GETIMAGE_URL}/${vID}`);
    dispatch(setStateGETIMAGEToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateGETIMAGEToFailed());
  }
};
