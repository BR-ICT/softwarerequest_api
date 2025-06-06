import { httpClient } from "./../utils/HttpClient";
import {
  HTTP_PRHEADAPPROVE_SUCCESS,
  HTTP_PRHEADAPPROVE_FETCHING,
  HTTP_PRHEADAPPROVE_FAILED,
  HTTP_PRHEADAPPROVE_CLEAR,
  server,
} from "../constants";

export const setStatePRHeadApproveToSuccess = (payload) => ({
  type: HTTP_PRHEADAPPROVE_SUCCESS,
  payload,
});

const setStatePRHeadApproveToFetching = () => ({
  type: HTTP_PRHEADAPPROVE_FETCHING,
});

const setStatePRHeadApproveToFailed = () => ({
  type: HTTP_PRHEADAPPROVE_FAILED,
});

const setStatePRHeadApproveToClear = () => ({
  type: HTTP_PRHEADAPPROVE_CLEAR,
});

export const getPRHeadApproves = (
  cono,
  divi,
  prno,
  fromstatus,
  tostatus,
  approve,
  page
) => {
  return async (dispatch) => {
    // console.log("PR: " + prno + " page: " + page);
    dispatch(setStatePRHeadApproveToFetching());

    if (page === "approvempr") {
      doGetMPRHeadApprove(
        dispatch,
        cono,
        divi,
        prno,
        fromstatus,
        tostatus,
        approve
      );
    } else {
      doGetEPRHeadApprove(
        dispatch,
        cono,
        divi,
        prno,
        fromstatus,
        tostatus,
        approve
      );
    }
  };
};

// export const getMPRHeadApproves = (
//   cono,
//   divi,
//   prno,
//   fromstatus,
//   tostatus,
//   approve
// ) => {
//   return async (dispatch) => {
//     // console.log("PR: " + prno + " STS: " + status);
//     dispatch(setStatePRHeadApproveToFetching());
//     doGetMPRHeadApprove(
//       dispatch,
//       cono,
//       divi,
//       prno,
//       fromstatus,
//       tostatus,
//       approve
//     );
//   };
// };

const doGetMPRHeadApprove = async (
  dispatch,
  cono,
  divi,
  prno,
  fromstatus,
  tostatus,
  approve
) => {
  try {
    let result = await httpClient.get(
      `${server.MPRHEADAPPROVE_URL}/${cono}/${divi}/${prno}/${fromstatus}/${tostatus}/${approve}`
    );
    // alert(JSON.stringify(result.data));
    dispatch(setStatePRHeadApproveToSuccess(result.data));
  } catch (err) {
    dispatch(setStatePRHeadApproveToFailed());
  }
};

// export const getEPRHeadApproves = (
//   cono,
//   divi,
//   prno,
//   fromstatus,
//   tostatus,
//   approve
// ) => {
//   return async (dispatch) => {
//     // console.log("PR: " + prno + " STS: " + status);
//     dispatch(setStatePRHeadApproveToFetching());
//     doGetEPRHeadApprove(
//       dispatch,
//       cono,
//       divi,
//       prno,
//       fromstatus,
//       tostatus,
//       approve
//     );
//   };
// };

const doGetEPRHeadApprove = async (
  dispatch,
  cono,
  divi,
  prno,
  fromstatus,
  tostatus,
  approve
) => {
  try {
    let result = await httpClient.get(
      `${server.EPRHEADAPPROVE_URL}/${cono}/${divi}/${prno}/${fromstatus}/${tostatus}/${approve}`
    );
    // alert(JSON.stringify(result.data));
    dispatch(setStatePRHeadApproveToSuccess(result.data));
  } catch (err) {
    dispatch(setStatePRHeadApproveToFailed());
  }
};

export const approvePRHead = (formData, history, page) => {
  // console.log("page: " + page + JSON.stringify(history));
  return async (dispatch) => {
    try {
      let result = null;
      if (page === "approvempr") {
        result = await httpClient.put(server.PRAPPROVE_URL, formData);
      } else {
        // result = await httpClient.put(server.PRAPPROVE_URL, formData);
      }

      alert(JSON.stringify(result.data));
      history.goBack();
    } catch (err) {
      alert(err.message);
    }
  };
};

export const approveFinalPRHead = (formData, history, page) => {
  // console.log("page: " + page + JSON.stringify(history));
  return async (dispatch) => {
    try {
      let result = null;
      if (page === "approvempr") {
        result = await httpClient.put(server.PRAPPROVE_URL, formData);
      } else {
        // result = await httpClient.put(server.PRAPPROVE_URL, formData);
      }

      // alert(JSON.stringify(result.data));
      // history.goBack();
    } catch (err) {
      alert(err.message);
    }
  };
};

export const checkApprovePRHead = (formData, history, page) => {
  return async (dispatch) => {
    try {
      // let result = await httpClient.put(server.CHECKPRAPPROVE_URL, formData);
      let result = null;
      if (page === "approvempr") {
        result = await httpClient.put(server.CHECKPRAPPROVE_URL, formData);
      } else {
        // result = await httpClient.put(server.CHECKPRAPPROVE_URL, formData);
      }

      // alert(JSON.stringify(result.data));
      // history.goBack();
    } catch (err) {
      alert(err.message);
    }
  };
};

export const rejectPRHead = (formData, history, page) => {
  // console.log("page: " + page);
  return async (dispatch) => {
    try {
      let result = null;
      if (page === "approvempr") {
        result = await httpClient.put(server.PRREJECT_URL, formData);
      } else {
        // result = await httpClient.put(server.PRREJECT_URL, formData);
      }

      alert(JSON.stringify(result.data));
      history.goBack();
    } catch (err) {
      alert(err.message);
    }
  };
};

export const rejectFinalPRHead = (formData, history, page) => {
  // console.log("page: " + page);
  return async (dispatch) => {
    try {
      let result = null;
      if (page === "approvempr") {
        result = await httpClient.put(server.PRREJECT_URL, formData);
      } else {
        // result = await httpClient.put(server.PRREJECT_URL, formData);
      }

      // alert(JSON.stringify(result.data));
      // history.goBack();
    } catch (err) {
      alert(err.message);
    }
  };
};
