import {
  HTTP_ACCOUNTANT_FETCHING,
  HTTP_ACCOUNTANT_FAILED,
  HTTP_ACCOUNTANT_SUCCESS,
  HTTP_ACCOUNTANT_CLEAR,
} from "../constants";
import { server } from "../constants";
import { httpClient } from "../utils/HttpClient";
import jwt from "jsonwebtoken";

// Information being sent to Reducer
export const setStateAccountantToSuccess = (payload) => ({
  type: HTTP_ACCOUNTANT_SUCCESS,
  payload,
});

const setStateAccountantToFetching = () => ({
  type: HTTP_ACCOUNTANT_FETCHING,
});

const setStateAccountantToFailed = () => ({
  type: HTTP_ACCOUNTANT_FAILED,
});

const setStateAccountantToClear = () => ({
  type: HTTP_ACCOUNTANT_CLEAR,
});

export const getAccountant = () => {
  return async (dispatch) => {
    dispatch(setStateAccountantToFetching());
    doGetAccountant(dispatch);
  };
};

const doGetAccountant = async (dispatch) => {
  try {
    let result = await httpClient.get(server.ACCOUNTANT_URL);
    dispatch(setStateAccountantToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateAccountantToFailed());
  }
};
