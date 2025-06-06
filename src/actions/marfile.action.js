import { httpClient } from "../utils/HttpClient";
import {
  HTTP_MARFILE_SUCCESS,
  HTTP_MARFILE_FETCHING,
  HTTP_MARFILE_FAILED,
  HTTP_MARFILE_CLEAR,
  server,
} from "../constants";

export const setStateMARFileToSuccess = (payload) => ({
  type: HTTP_MARFILE_SUCCESS,
  payload,
});

const setStateMARFileToFetching = () => ({
  type: HTTP_MARFILE_FETCHING,
});

const setStateMARFileToFailed = () => ({
  type: HTTP_MARFILE_FAILED,
});

const setStateMARFileToClear = () => ({
  type: HTTP_MARFILE_CLEAR,
});

export const getMARFile = (marno) => {
  return async (dispatch) => {
    // console.log("whs: " + whs + " item: " + item);
    dispatch(setStateMARFileToFetching());
    doGetMARFile(dispatch, marno);
  };
};

const doGetMARFile = async (dispatch, marno) => {
  try {
    let result = await httpClient.get(`${server.MARFILE_URL}/${marno}`);
    dispatch(setStateMARFileToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateMARFileToFailed());
  }
};

export const fetchMARFile = (marno) => {
  return async (dispatch) => {
    // console.log("whs: " + whs + " item: " + item);s
    dofetchMARFile(dispatch, marno);
  };
};

const dofetchMARFile = async (dispatch, marno) => {
  try {
    let result = await httpClient.get(`${server.MARFILE_URL}/${marno}`);
    dispatch(setStateMARFileToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateMARFileToFailed());
  }
};

export const addMARfile = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(server.MARFILE_URL, formData);
      // alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
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

export const deleteMARfile = (formData, history) => {
  return async (dispatch) => {
    try {
      await httpClient.put(server.MARFILE_URL, formData);
    } catch (err) {
      alert(err.message);
    }
  };
};
