import { httpClient } from "../utils/HttpClient";
import {
  HTTP_EXPENITURE_SUCCESS,
  HTTP_EXPENITURE_FETCHING,
  HTTP_EXPENITURE_FAILED,
  HTTP_EXPENITURE_CLEAR,
  server,
} from "../constants";

export const setStateExpenitureToSuccess = (payload) => ({
  type: HTTP_EXPENITURE_SUCCESS,
  payload,
});

const setStateExpenitureToFetching = () => ({
  type: HTTP_EXPENITURE_FETCHING,
});

const setStateExpenitureToFailed = () => ({
  type: HTTP_EXPENITURE_FAILED,
});

const setStateExpenitureToClear = () => ({
  type: HTTP_EXPENITURE_CLEAR,
});

export const getExpenitures = (pono, line) => {
  return async (dispatch) => {
    dispatch(setStateExpenitureToFetching());
    doGetExpeniture(dispatch, pono, line);
  };
};

const doGetExpeniture = async (dispatch, pono, line) => {
  try {
    let result = await httpClient.get(
      `${server.EXPENITURE_URL}/${pono}/${line}`
    );
    dispatch(setStateExpenitureToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateExpenitureToFailed());
  }
};

export const updateCharge3 = (prno, prnoline, chargeline, code, amount) => {
  return async (dispatch) => {
    dispatch(setStateExpenitureToFetching());
    doUpdateCharge(dispatch, prno, prnoline, chargeline, code, amount);
  };
};

const doUpdateCharge = async (
  dispatch,
  pono,
  ponoline,
  chargeline,
  code,
  amount
) => {
  try {
    let resultCharge = await httpClient.post(
      `${server.UPDATECHARGE_URL}/${pono}/${ponoline}/${chargeline}/${code}/${amount}`
    );

    let result = await httpClient.get(
      `${server.EXPENITURE_URL}/${pono}/${ponoline}`
    );

    alert(JSON.stringify(resultCharge.data));
    dispatch(setStateExpenitureToSuccess(result.data));
  } catch (err) {
    alert(err.message);
    dispatch(setStateExpenitureToFailed());
  }
};
