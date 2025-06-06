import {
  HTTP_COMPANY_FETCHING,
  HTTP_COMPANY_FAILED,
  HTTP_COMPANY_SUCCESS,
  HTTP_COMPANY_CLEAR,
} from "../constants";
import { server } from "../constants";
import { httpClient } from "./../utils/HttpClient";

// Information being sent to Reducer
export const setStateCompanyToSuccess = (payload) => ({
  type: HTTP_COMPANY_SUCCESS,
  payload,
});

const setStateCompanyToFetching = () => ({
  type: HTTP_COMPANY_FETCHING,
});

const setStateCompanyToFailed = () => ({
  type: HTTP_COMPANY_FAILED,
});

const setStateCompanyToClear = () => ({
  type: HTTP_COMPANY_CLEAR,
});

export const getCompanys = () => {
  return async (dispatch) => {
    dispatch(setStateCompanyToFetching());
    doGetCompanys(dispatch);
  };
};

const doGetCompanys = async (dispatch) => {
  try {
    let result = await httpClient.get(server.COMPANY_URL);
    dispatch(setStateCompanyToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateCompanyToFailed());
  }
};

export const getCompanysWithConoDivi = (cono, divi) => {
  return async (dispatch) => {
    dispatch(setStateCompanyToFetching());
    doGetCompanysWithConoDivi(dispatch, cono, divi);
  };
};

const doGetCompanysWithConoDivi = async (dispatch, cono, divi) => {
  try {
    let result = await httpClient.get(
      `${server.COMPANYWITHCONODIVI_URL}/${cono}/${divi}`
    );
    dispatch(setStateCompanyToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateCompanyToFailed());
  }
};
