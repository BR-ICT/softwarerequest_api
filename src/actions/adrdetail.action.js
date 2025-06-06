import { httpClient } from "../utils/HttpClient";
import {
  HTTP_ADRDETAIL_SUCCESS,
  HTTP_ADRDETAIL_FETCHING,
  HTTP_ADRDETAIL_FAILED,
  HTTP_ADRDETAIL_CLEAR,
  server,
} from "../constants";

export const setStateADRDetailToSuccess = (payload) => ({
  type: HTTP_ADRDETAIL_SUCCESS,
  payload,
});

const setStateADRDetailToFetching = () => ({
  type: HTTP_ADRDETAIL_FETCHING,
});

const setStateADRDetailToFailed = () => ({
  type: HTTP_ADRDETAIL_FAILED,
});

const setStateADRDetailToClear = () => ({
  type: HTTP_ADRDETAIL_CLEAR,
});

export const getADRDetail = (adrno, fromstatus, toStatus) => {
  return async (dispatch) => {
    dispatch(setStateADRDetailToFetching());
    doGetADRDetail(dispatch, adrno, fromstatus, toStatus);
  };
};

export const doGetADRDetail = async (dispatch, adrno, fromstatus, toStatus) => {
  try {
    let result = await httpClient.get(
      `${server.ADRDETAIL_URL}/${adrno}/${fromstatus}/${toStatus}`
    );
    // alert(JSON.stringify(result.data));
    dispatch(setStateADRDetailToSuccess(result.data));
  } catch (err) {
    dispatch(setStateADRDetailToFailed());
  }
};

export const fetchADRDetail = (adrno, fromstatus, toStatus) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.get(
        `${server.ADRDETAIL_URL}/${adrno}/${fromstatus}/${toStatus}`
      );
      // alert(JSON.stringify(result.data));
      dispatch(setStateADRDetailToSuccess(result.data));
    } catch (err) {
      dispatch(setStateADRDetailToFailed());
    }
  };
};

export const addADRDetail = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(server.ADRDETAIL_URL, formData);
      // alert(JSON.stringify(result.data));

      // fetchADRDetail(
      //   dispatch,
      //   formData.getAll("vADRNumber"),
      //   fromStatus,
      //   toStatus
      // );

      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const updateADRDetail = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.ADRDETAIL_URL, formData);
      // alert(JSON.stringify(result.data));

      // fetchADRDetail(
      //   dispatch,
      //   formData.getAll("vADRNumber"),
      //   fromStatus,
      //   toStatus
      // );

      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const deleteADRDetail = (adrno, itemline) => {
  return async (dispatch) => {
    try {
      await httpClient.delete(`${server.ADRDETAIL_URL}/${adrno}/${itemline}`);
    } catch (err) {
      alert(err.message);
    }
  };
};
