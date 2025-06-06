import { httpClient } from "../utils/HttpClient";
import {
  HTTP_SCHEDULE_SUCCESS,
  HTTP_SCHEDULE_FETCHING,
  HTTP_SCHEDULE_FAILED,
  HTTP_SCHEDULE_CLEAR,
  server,
} from "../constants";
import jwt from "jsonwebtoken";

export const setStateSCHEDULEToSuccess = (payload) => ({
  type: HTTP_SCHEDULE_SUCCESS,
  payload,
});

const setStateSCHEDULEToFetching = () => ({
  type: HTTP_SCHEDULE_FETCHING,
});

const setStateSCHEDULEToFailed = () => ({
  type: HTTP_SCHEDULE_FAILED,
});

const setStateSCHEDULEToClear = () => ({
  type: HTTP_SCHEDULE_CLEAR,
});

export const getjsonschedule = () => {
  return async (dispatch) => {
    dispatch(setStateSCHEDULEToFetching());
    dogetjsonschedule(dispatch);
  };
};

export const dogetjsonschedule = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.GETJSONSCHEDULE_URL}`);
    dispatch(setStateSCHEDULEToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateSCHEDULEToFailed());
  }
};

// export const getOperationfilterdata = (fromdate, todate) => {
//   return async (dispatch) => {
//     dispatch(setStateOPERATIONDATAToFetching());
//     dogetOperationfilterdata(fromdate, todate, dispatch);
//   };
// };

// export const dogetOperationfilterdata = async (fromdate, todate, dispatch) => {
//   try {
//     let result = await httpClient.get(
//       `${server.GETOPERATIONFILTERDATA_URL}/${fromdate}/${todate}`
//     );
//     dispatch(setStateOPERATIONDATAToSuccess(result.data));
//   } catch (err) {
//     dispatch(setStateOPERATIONDATAToFailed());
//   }
// };
