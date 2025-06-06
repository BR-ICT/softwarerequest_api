import { httpClient } from "../utils/HttpClient";
import {
  HTTP_MARHEAD_SUCCESS,
  HTTP_MARHEAD_FETCHING,
  HTTP_MARHEAD_FAILED,
  HTTP_MARHEAD_CLEAR,
  server,
} from "../constants";
import * as adrnumberActions from "./adrnumber.action";

export const setStateMARHeadToSuccess = (payload) => ({
  type: HTTP_MARHEAD_SUCCESS,
  payload,
});

const setStateMARHeadToFetching = () => ({
  type: HTTP_MARHEAD_FETCHING,
});

const setStateMARHeadToFailed = () => ({
  type: HTTP_MARHEAD_FAILED,
});

const setStateMARHeadToClear = () => ({
  type: HTTP_MARHEAD_CLEAR,
});

export const getMARHead = (marno, fromstatus, toStatus) => {
  return async (dispatch) => {
    dispatch(setStateMARHeadToFetching());
    doGetMARHead(dispatch, marno, fromstatus, toStatus);
  };
};

const doGetMARHead = async (dispatch, marno, fromstatus, toStatus) => {
  try {
    let result = await httpClient.get(
      `${server.MARHEAD_URL}/${marno}/${fromstatus}/${toStatus}`
    );
    // alert(JSON.stringify(result.data));
    dispatch(setStateMARHeadToSuccess(result.data));
  } catch (err) {
    dispatch(setStateMARHeadToFailed());
  }
};

export const getMARHeadMonitoring = (
  prefix,
  marno,
  fromdate,
  todate,
  status
) => {
  return async (dispatch) => {
    dispatch(setStateMARHeadToFetching());
    doGetMARHeadMonitoring(dispatch, prefix, marno, fromdate, todate, status);
  };
};

const doGetMARHeadMonitoring = async (
  dispatch,
  prefix,
  marno,
  fromdate,
  todate,
  status
) => {
  try {
    let result = await httpClient.get(
      `${server.MARHEADMONITORING_URL}/${prefix}/${marno}/${fromdate}/${todate}/${status}`
    );
    // alert(JSON.stringify(result.data));
    dispatch(setStateMARHeadToSuccess(result.data));
  } catch (err) {
    dispatch(setStateMARHeadToFailed());
  }
};

export const getMARHeadMonitoringV2 = (formData) => {
  return async (dispatch) => {
    dispatch(setStateMARHeadToFetching());
    doGetMARHeadMonitoringV2(dispatch, formData);
  };
};

const doGetMARHeadMonitoringV2 = async (dispatch, formData) => {
  try {
    let result = await httpClient.get(server.MARHEADMONITORING_URL, formData);
    // alert(JSON.stringify(result.data));
    dispatch(setStateMARHeadToSuccess(result.data));
  } catch (err) {
    dispatch(setStateMARHeadToFailed());
  }
};

export const addMARHead = (formData, history) => {
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

export const addMARHeadV2 = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(server.MARHEAD_URL, formData);
      // alert(JSON.stringify(result.data));
      // await dispatch(adrnumberActions.addADRNumber(dispatch, result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const updateMARHead = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.MARHEAD_URL, formData);
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const submitMAR = (prefix, marno, status, submit) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(
        `${server.SUBMITMAR_URL}/${prefix}/${marno}/${status}/${submit}`
      );
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

// export const submitMARA = (marno, status, submit) => {
//   return async (dispatch) => {
//     try {
//       let result = await httpClient.put(
//         `${server.SUBMITMARA_URL}/${marno}/${status}/${submit}`
//       );
//       return result.data;
//     } catch (err) {
//       alert(err.message);
//     }
//   };
// };

export const rejectMAR = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.REJECTMAR_URL, formData);
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const cancelMAR = (marno, status, submit) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(
        `${server.CANCELMAR_URL}/${marno}/${status}/${submit}`
      );
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const allocateMAR = (marno, submit) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(
        `${server.ALLOCATEMAR_URL}/${marno}/${submit}`
      );
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const confirmMAR = (marno, submit) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(
        `${server.CONFIRMMAR_URL}/${marno}/${submit}`
      );
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const approveMAR = (formData, history) => {
  return async (dispatch) => {
    try {
      // console.log(formData);
      let result = await httpClient.put(server.APPROVEMAR_URL, formData);
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const cancelOrder = (fac, marno, status, submit) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(
        `${server.CANCELORDER_URL}/${fac}/${marno}/${status}/${submit}`
      );
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};
