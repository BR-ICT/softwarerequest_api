import { httpClient } from "../utils/HttpClient";
import {
  HTTP_CHARGE_SUCCESS,
  HTTP_CHARGE_FETCHING,
  HTTP_CHARGE_FAILED,
  HTTP_CHARGE_CLEAR,
  server,
} from "../constants";

export const setStateChargeToSuccess = (payload) => ({
  type: HTTP_CHARGE_SUCCESS,
  payload,
});

const setStateChargeToFetching = () => ({
  type: HTTP_CHARGE_FETCHING,
});

const setStateChargeToFailed = () => ({
  type: HTTP_CHARGE_FAILED,
});

const setStateChargeToClear = () => ({
  type: HTTP_CHARGE_CLEAR,
});

export const getCharges = () => {
  return async (dispatch) => {
    dispatch(setStateChargeToFetching());
    doGetCharge(dispatch);
  };
};

const doGetCharge = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.CHARGE_URL}`);
    dispatch(setStateChargeToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateChargeToFailed());
  }
};
