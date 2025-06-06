import { httpClient } from "../utils/HttpClient";
import {
  HTTP_MAIN_REQUEST_SUCCESS,
  HTTP_MAIN_REQUEST_FETCHING,
  HTTP_MAIN_REQUEST_FAILED,
  HTTP_MAIN_REQUEST_CLEAR,
  server,
} from "../constants";

export const setStateMAIN_REQUESTToSuccess = (payload) => ({
  type: HTTP_MAIN_REQUEST_SUCCESS,
  payload,
});

const setStateMAIN_REQUESTToFetching = () => ({
  type: HTTP_MAIN_REQUEST_FETCHING,
});

const setStateMAIN_REQUESTToFailed = () => ({
  type: HTTP_MAIN_REQUEST_FAILED,
});

const setStateMAIN_REQUESTToClear = () => ({
  type: HTTP_MAIN_REQUEST_CLEAR,
});

export const getMARFile = (marno) => {
  return async (dispatch) => {
    // console.log("whs: " + whs + " item: " + item);
    dispatch(setStateMAIN_REQUESTToFetching());
    doGetMARFile(dispatch, marno);
  };
};

const doGetMARFile = async (dispatch, marno) => {
  try {
    let result = await httpClient.get(`${server.MARFILE_URL}/${marno}`);
    dispatch(setStateMAIN_REQUESTToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateMAIN_REQUESTToFailed());
  }
};

export const fetchMAIN_REQUEST = (marno) => {
  return async (dispatch) => {
    // console.log("whs: " + whs + " item: " + item);s
    dofetchMARFile(dispatch, marno);
  };
};

const dofetchMARFile = async (dispatch, marno) => {
  try {
    let result = await httpClient.get(`${server.MARFILE_URL}/${marno}`);
    dispatch(setStateMAIN_REQUESTToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateMAIN_REQUESTToFailed());
  }
};

// export const deleteMARfile = (formData, history) => {
//   return async (dispatch) => {
//     try {
//       let result = await httpClient.delete(server.MARFILE_URL, formData);
//       // alert(JSON.stringify(result.data));
//       return result.data;
//     } catch (err) {
//       alert(err.message);
//     }
//   };
// };

export const deleteMAIN_REQUEST = (formData, history) => {
  return async (dispatch) => {
    try {
      await httpClient.put(server.MARFILE_URL, formData);
    } catch (err) {
      alert(err.message);
    }
  };
};

export const addMAIN_REQUEST = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(server.SWRFILE_URL, formData);
      // alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};
