import { httpClient } from "../utils/HttpClient";
import {
  HTTP_SWRHEADER_SUCCESS,
  HTTP_SWRHEADER_FETCHING,
  HTTP_SWRHEADER_FAILED,
  HTTP_SWRHEADER_CLEAR,
  server,
} from "../constants";

export const setStateSWRHEADERToSuccess = (payload) => ({
  type: HTTP_SWRHEADER_SUCCESS,
  payload,
});

const setStateSWRHEADERToFetching = () => ({
  type: HTTP_SWRHEADER_FETCHING,
});

const setStateSWRHEADERToFailed = () => ({
  type: HTTP_SWRHEADER_FAILED,
});

const setStateSWRHEADERToClear = () => ({
  type: HTTP_SWRHEADER_CLEAR,
});

const doGetSWRHEADER = async (dispatch, marno) => {
  try {
    let result = await httpClient.get(`${server.MARFILE_URL}/${marno}`);
    dispatch(setStateSWRHEADERToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateSWRHEADERToFailed());
  }
};

const dofetchMARFile = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.GETSWRFILE_URL}`);
    dispatch(setStateSWRHEADERToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateSWRHEADERToFailed());
  }
};

export const GetID = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(server.GetID_URL, formData);
      alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

// export const doGetDeptHead = () => {
//   return async (dispatch) => {
//     try {
//       let result = await httpClient.get(`${server.GETID_URL}`);
//       dispatch(setStateSWRHEADERToSuccess(result.data));
//       return result.data;
//     } catch (err) {
//       alert(err.message);
//       dispatch(setStateSWRHEADERToFailed());
//     }
//   };
// };

export const getDeptHead = () => {
  return async (dispatch) => {
    dispatch(setStateSWRHEADERToSuccess());
    doGetDeptHead(dispatch);
  };
};

const doGetDeptHead = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.GETDEPTHEAD}`);
    dispatch(setStateSWRHEADERToSuccess(result.data));
  } catch (err) {
    dispatch(setStateSWRHEADERToFailed());
  }
};

/// add header

export const addSWRheader = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(server.ADDSWRHEADER_URL, formData);
      alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

//////////////

// load header

// export const getLoadData = (formData, history) => {
//   return async (dispatch) => {
//     try {
//       let result = await httpClient.post(server.ADDSWRHEADER_URL, formData);
//       alert(JSON.stringify(result.data));
//       return result.data;
//     } catch (err) {
//       alert(err.message);
//     }
//   };
// };

/////////////
