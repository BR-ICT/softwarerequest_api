import { httpClient } from "../utils/HttpClient";
import {
  HTTP_MARDETAIL_SUCCESS,
  HTTP_MARDETAIL_FETCHING,
  HTTP_MARDETAIL_FAILED,
  HTTP_MARDETAIL_CLEAR,
  server,
} from "../constants";

export const setStateMARDetailToSuccess = (payload) => ({
  type: HTTP_MARDETAIL_SUCCESS,
  payload,
});

const setStateMARDetailToFetching = () => ({
  type: HTTP_MARDETAIL_FETCHING,
});

const setStateMARDetailToFailed = () => ({
  type: HTTP_MARDETAIL_FAILED,
});

const setStateMARDetailToClear = () => ({
  type: HTTP_MARDETAIL_CLEAR,
});

export const getMARDetail = (marno, fromstatus, toStatus) => {
  return async (dispatch) => {
    dispatch(setStateMARDetailToFetching());
    doGetMARDetail(dispatch, marno, fromstatus, toStatus);
  };
};

export const doGetMARDetail = async (dispatch, marno, fromstatus, toStatus) => {
  try {
    let result = await httpClient.get(
      `${server.MARDETAIL_URL}/${marno}/${fromstatus}/${toStatus}`
    );
    // alert(JSON.stringify(result.data));
    dispatch(setStateMARDetailToSuccess(result.data));
  } catch (err) {
    dispatch(setStateMARDetailToFailed());
  }
};

export const fetchMARDetail = (marno, fromstatus, toStatus) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.get(
        `${server.MARDETAIL_URL}/${marno}/${fromstatus}/${toStatus}`
      );
      // alert(JSON.stringify(result.data));
      dispatch(setStateMARDetailToSuccess(result.data));
    } catch (err) {
      dispatch(setStateMARDetailToFailed());
    }
  };
};

export const addMARDetail = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(server.MARDETAIL_URL, formData);
      // alert(JSON.stringify(result));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const updateMARDetail = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.MARDETAIL_URL, formData);
      // alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const deleteMARDetail = (marno, itemline) => {
  return async (dispatch) => {
    try {
      await httpClient.delete(`${server.MARDETAIL_URL}/${marno}/${itemline}`);
    } catch (err) {
      alert(err.message);
    }
  };
};
