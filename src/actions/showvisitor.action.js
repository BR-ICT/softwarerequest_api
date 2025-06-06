import { httpClient } from "../utils/HttpClient";
import {
  HTTP_SHOWVISITOR_SUCCESS,
  HTTP_SHOWVISITOR_FETCHING,
  HTTP_SHOWVISITOR_FAILED,
  HTTP_SHOWVISITOR_CLEAR,
  server,
} from "../constants";
import jwt from "jsonwebtoken";

export const setStateSHOWVISITORToSuccess = (payload) => ({
  type: HTTP_SHOWVISITOR_SUCCESS,
  payload,
});

const setStateSHOWVISITORToFetching = () => ({
  type: HTTP_SHOWVISITOR_FETCHING,
});

const setStateSHOWVISITORToFailed = () => ({
  type: HTTP_SHOWVISITOR_FAILED,
});

const setStateSHOWVISITORToClear = () => ({
  type: HTTP_SHOWVISITOR_CLEAR,
});

// export const getshowVisitor = (vID) => {
//   return async (dispatch) => {
//     dispatch(setStateSHOWVISITORToFetching());
//     doGetshowVisitor(dispatch, vID);
//   };
// };

// export const doGetshowVisitor = async (dispatch, vID) => {
//   try {
//     let result = await httpClient.get(`${server.SHOWVISITOR_URL}/${vID}`);
//     dispatch(setStateSHOWVISITORToSuccess(result.data));
//     // alert(JSON.stringify(result.data));
//   } catch (err) {
//     dispatch(setStateSHOWVISITORToFailed());
//   }
// };

export const getshowVisitor = (vID, location) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.get(
        `${server.SHOWVISITOR_URL}/${vID}/${location}`
      );

      dispatch(setStateSHOWVISITORToSuccess(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
      dispatch(setStateSHOWVISITORToFailed());
    }
  };
};
