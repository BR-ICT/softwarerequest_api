import { httpClient } from "./../utils/HttpClient";
import {
  HTTP_MONTH_SUCCESS,
  HTTP_MONTH_FETCHING,
  HTTP_MONTH_FAILED,
  HTTP_MONTH_CLEAR,
  server,
} from "../constants";

export const setStateMonthToSuccess = (payload) => ({
  type: HTTP_MONTH_SUCCESS,
  payload,
});

const setStateMonthToFetching = () => ({
  type: HTTP_MONTH_FETCHING,
});

const setStateMonthToFailed = () => ({
  type: HTTP_MONTH_FAILED,
});

const setStateMonthToClear = () => ({
  type: HTTP_MONTH_CLEAR,
});

export const getMonths = () => {
  return async (dispatch) => {
    dispatch(setStateMonthToFetching());
    doGetMonth(dispatch);
  };
};

const doGetMonth = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.EPRMONTH_URL}`);
    dispatch(setStateMonthToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateMonthToFailed());
  }
};
