import { httpClient } from "../utils/HttpClient";
import {
  HTTP_ADRHEAD_SUCCESS,
  HTTP_ADRHEAD_FETCHING,
  HTTP_ADRHEAD_FAILED,
  HTTP_ADRHEAD_CLEAR,
  server,
} from "../constants";
import * as adrnumberActions from "./adrnumber.action";

export const setStateADRHeadToSuccess = (payload) => ({
  type: HTTP_ADRHEAD_SUCCESS,
  payload,
});

const setStateADRHeadToFetching = () => ({
  type: HTTP_ADRHEAD_FETCHING,
});

const setStateADRHeadToFailed = () => ({
  type: HTTP_ADRHEAD_FAILED,
});

const setStateADRHeadToClear = () => ({
  type: HTTP_ADRHEAD_CLEAR,
});

export const getADRHead = (adrno, fromstatus, toStatus) => {
  return async (dispatch) => {
    // console.log("PR: " + prno + " STS: " + status);
    dispatch(setStateADRHeadToFetching());
    doGetADRHead(dispatch, adrno, fromstatus, toStatus);
  };
};

const doGetADRHead = async (dispatch, adrno, fromstatus, toStatus) => {
  try {
    let result = await httpClient.get(
      `${server.ADRHEAD_URL}/${adrno}/${fromstatus}/${toStatus}`
    );
    // alert(JSON.stringify(result.data));
    dispatch(setStateADRHeadToSuccess(result.data));
  } catch (err) {
    dispatch(setStateADRHeadToFailed());
  }
};

export const addADRHead = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(server.ADRHEAD_URL, formData);
      alert(JSON.stringify(result.data));
      await dispatch(adrnumberActions.addADRNumber(dispatch, result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const addADRHeadV2 = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(server.ADRHEAD_URL, formData);
      // alert(JSON.stringify(result.data));
      // await dispatch(adrnumberActions.addADRNumber(dispatch, result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const updateADRHead = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.ADRHEAD_URL, formData);
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const updateStsADRHead = (adrno, status, submit) => {
  return async (dispatch) => {
    try {
      // console.log(formData);
      let result = await httpClient.put(
        `${server.STATUSADRHEAD_URL}/${adrno}/${status}/${submit}`
      );
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const rejectADRHead = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.REJECTADRHEAD_URL, formData);
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

// export const getFile = () => {
//   return async (dispatch) => {
//     try {
//       // let result = await httpClient.get("/data/file");
//       return await httpClient.get("/data/file");
//     } catch (err) {
//       alert(err.message);
//     }
//   };
// };

export const getFile = () => {
  return async (dispatch) => {
    // console.log("PR: " + prno + " STS: " + status);
    dispatch(setStateADRHeadToFetching());
    doGetFile(dispatch);
  };
};

const doGetFile = async (dispatch) => {
  try {
    let result = await httpClient.get(`/data/file`);
    // alert(JSON.stringify(result.data));
    dispatch(setStateADRHeadToSuccess(result));
  } catch (err) {
    dispatch(setStateADRHeadToFailed());
  }
};

export const updateFile = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(server.FILE_URL, formData);
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const updateADRImage = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.ADRIMAGE_URL, formData);
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const getADRHeadMonitoring = (fromstatus, toStatus) => {
  return async (dispatch) => {
    // console.log("PR: " + prno + " STS: " + status);
    dispatch(setStateADRHeadToFetching());
    doGetADRHeadMonitoring(dispatch, fromstatus, toStatus);
  };
};

const doGetADRHeadMonitoring = async (dispatch, fromstatus, toStatus) => {
  try {
    let result = await httpClient.get(
      `${server.ADRHEADMONITORING_URL}/${fromstatus}/${toStatus}`
    );
    // alert(JSON.stringify(result.data));
    dispatch(setStateADRHeadToSuccess(result.data));
  } catch (err) {
    dispatch(setStateADRHeadToFailed());
  }
};
