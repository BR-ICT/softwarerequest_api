import { httpClient } from "../utils/HttpClient";
import {
  HTTP_SWRFILENUMBER_SUCCESS,
  HTTP_SWRFILENUMBER_FETCHING,
  HTTP_SWRFILENUMBER_FAILED,
  HTTP_SWRFILENUMBER_CLEAR,
  server,
} from "../constants";

export const setStateSWRFileNumberToSuccess = (payload) => ({
  type: HTTP_SWRFILENUMBER_SUCCESS,
  payload,
});

const setStateSWRFileNumberToFetching = () => ({
  type: HTTP_SWRFILENUMBER_FETCHING,
});

const setStateSWRFileNumberToFailed = () => ({
  type: HTTP_SWRFILENUMBER_FAILED,
});

const setStateSWRFileNumberToClear = () => ({
  type: HTTP_SWRFILENUMBER_CLEAR,
});

export const getSWRFileNumber = () => {
  return async (dispatch) => {
    // console.log("whs: " + whs + " item: " + item);
    dispatch(setStateSWRFileNumberToSuccess());
    doGetSWRFileNumber(dispatch);
  };
};

const doGetSWRFileNumber = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.GETSWRFILENUMBER_URL}`);
    dispatch(setStateSWRFileNumberToSuccess(result.data));
  } catch (err) {
    dispatch(setStateSWRFileNumberToFailed());
  }
};
