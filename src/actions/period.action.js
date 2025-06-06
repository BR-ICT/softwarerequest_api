import {
  HTTP_PERIOD_FETCHING,
  HTTP_PERIOD_FAILED,
  HTTP_PERIOD_SUCCESS,
  HTTP_PERIOD_CLEAR,
} from "../constants";
import { server } from "../constants";
import { httpClient } from "../utils/HttpClient";

// Information being sent to Reducer
export const setStatePeriodToSuccess = (payload) => ({
  type: HTTP_PERIOD_SUCCESS,
  payload,
});

const setStatePeriodToFetching = () => ({
  type: HTTP_PERIOD_FETCHING,
});

const setStatePeriodToFailed = () => ({
  type: HTTP_PERIOD_FAILED,
});

const setStatePeriodToClear = () => ({
  type: HTTP_PERIOD_CLEAR,
});

export const getPeriods = () => {
  return async (dispatch) => {
    dispatch(setStatePeriodToFetching());
    doGetPeriods(dispatch);
  };
};

const doGetPeriods = async (dispatch) => {
  try {
    let result = await httpClient.get(server.PERIOD_URL);
    dispatch(setStatePeriodToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStatePeriodToFailed());
  }
};
