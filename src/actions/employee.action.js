import { httpClient } from "../utils/HttpClient";
import {
  HTTP_EMPLOYEE_SUCCESS,
  HTTP_EMPLOYEE_FETCHING,
  HTTP_EMPLOYEE_FAILED,
  HTTP_EMPLOYEE_CLEAR,
  server,
} from "../constants";

export const setStateEMPLOYEEToSuccess = (payload) => ({
  type: HTTP_EMPLOYEE_SUCCESS,
  payload,
});

const setStateEMPLOYEEToFetching = () => ({
  type: HTTP_EMPLOYEE_FETCHING,
});

const setStateEMPLOYEEToFailed = () => ({
  type: HTTP_EMPLOYEE_FAILED,
});

const setStateEMPLOYEEToClear = () => ({
  type: HTTP_EMPLOYEE_CLEAR,
});

// export const addVisitorHeader = (formData, history) => {
//   return async (dispatch) => {
//     try {
//       let result = await httpClient.put(server.ADDVISITORHEADER_URL, formData);
//       alert(JSON.stringify(result.data));
//       return result.data;
//     } catch (err) {
//       alert(err.message);
//     }
//   };
// };

// export const getEmployee = (history) => {
//   return async (dispatch) => {
//     try {
//       let result = await httpClient.get(server.GETEMPLOYEE_URL);
//       return result.data;
//     } catch (err) {
//       alert(err.message);
//     }
//   };
// };

export const getEmployee = () => {
  return async (dispatch) => {
    try {
      let result = await httpClient.get(`${server.GETEMPLOYEE_URL}`);
      dispatch(setStateEMPLOYEEToSuccess(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
      dispatch(setStateEMPLOYEEToFailed());
    }
  };
};
