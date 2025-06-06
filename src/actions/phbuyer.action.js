import { httpClient } from "./../utils/HttpClient";
import {
  HTTP_PHBUYER_SUCCESS,
  HTTP_PHBUYER_FETCHING,
  HTTP_PHBUYER_FAILED,
  HTTP_PHBUYER_CLEAR,
  server,
} from "../constants";

export const HTTP_PHBUYERToSuccess = (payload) => ({
  type: HTTP_PHBUYER_SUCCESS,
  payload,
});

const HTTP_PHBUYERToFetching = () => ({
  type: HTTP_PHBUYER_FETCHING,
});

const HTTP_PHBUYERToFailed = () => ({
  type: HTTP_PHBUYER_FAILED,
});

const HTTP_PHBUYERToClear = () => ({
  type: HTTP_PHBUYER_CLEAR,
});

export const getPHBuyers = (dept, group) => {
  return async (dispatch) => {
    // console.log("dept: " + dept + " group: " + group);
    dispatch(HTTP_PHBUYERToFetching());
    doGetPHBuyer(dispatch, dept, group);
  };
};

const doGetPHBuyer = async (dispatch, dept, group) => {
  try {
    let result = await httpClient.get(`${server.PHBUYER_URL}/${dept}/${group}`);
    dispatch(HTTP_PHBUYERToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(HTTP_PHBUYERToFailed());
  }
};
