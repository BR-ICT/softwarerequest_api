import { httpClient } from "../utils/HttpClient";
import {
  HTTP_SWRFILE_SUCCESS,
  HTTP_SWRFILE_FETCHING,
  HTTP_SWRFILE_FAILED,
  HTTP_SWRFILE_CLEAR,
  server,
} from "../constants";

export const setStateSWRFileToSuccess = (payload) => ({
  type: HTTP_SWRFILE_SUCCESS,
  payload,
});

// export const setStateSWRFileNumberToSuccess = (payload) => ({
//   type: HTTP_SWRFILENUMBER_SUCCESS,
//   payload,
// });

const setStateSWRFileToFetching = () => ({
  type: HTTP_SWRFILE_FETCHING,
});

const setStateSWRFileToFailed = () => ({
  type: HTTP_SWRFILE_FAILED,
});

// const setStateSWRFileNumberToFailed = () => ({
//   type: HTTP_SWRFILENUMBER_FAILED_FAILED,
// });

const setStateSWRFileToClear = () => ({
  type: HTTP_SWRFILE_CLEAR,
});

export const getMARFile = (marno) => {
  return async (dispatch) => {
    // console.log("whs: " + whs + " item: " + item);
    dispatch(setStateSWRFileToFetching());
    doGetMARFile(dispatch, marno);
  };
};

const doGetMARFile = async (dispatch, marno) => {
  try {
    let result = await httpClient.get(`${server.MARFILE_URL}/${marno}`);
    dispatch(setStateSWRFileToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateSWRFileToFailed());
  }
};

// export const fetchMARFile = (marno) => {
//   return async (dispatch) => {
//     // console.log("whs: " + whs + " item: " + item);s
//     dofetchMARFile(dispatch, marno);
//   };
// };

// const dofetchMARFile = async (dispatch, marno) => {
//   try {
//     let result = await httpClient.get(`${server.MARFILE_URL}/${marno}`);
//     dispatch(setStateSWRFileToSuccess(result.data));
//     // alert(JSON.stringify(result.data));
//   } catch (err) {
//     dispatch(setStateSWRFileToFailed());
//   }
// };

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

export const addSWRfile = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(server.SWRFILE_URL, formData);
      alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const getSWRFile = () => {
  return async (dispatch) => {
    // console.log("whs: " + whs + " item: " + item);
    dispatch(setStateSWRFileToFetching());
    doGetSWRFile(dispatch);
  };
};

export const getSWRFileNumber = () => {
  return async (dispatch) => {
    // console.log("whs: " + whs + " item: " + item);
    dispatch(setStateSWRFileToSuccess());
    doGetSWRFileNumber(dispatch);
  };
};

const doGetSWRFile = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.GETSWRFILE_URL}`);
    dispatch(setStateSWRFileToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateSWRFileToFailed());
  }
};

const doGetSWRFileNumber = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.GETSWRFILENUMBER_URL}`);
    dispatch(setStateSWRFileToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateSWRFileToFailed());
  }
};

export const fetchSWRFile = () => {
  return async (dispatch) => {
    // console.log("whs: " + whs + " item: " + item);s
    dofetchMARFile(dispatch);
  };
};

const dofetchMARFile = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.GETSWRFILE_URL}`);
    dispatch(setStateSWRFileToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateSWRFileToFailed());
  }
};

export const updateSWRfile = (formData) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.UPDATESWRFILE_URL, formData);
      // alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const deleteSWRfile = (SFORNO) => {
  return async (dispatch) => {
    try {
      await httpClient.delete(`${server.DELETESWRFILE_URL}/${SFORNO}`);
    } catch (err) {
      alert(err.message);
    }
  };
};

export const GetID = () => {
  return async (dispatch) => {
    doGetID(dispatch);
  };
};

const doGetID = async (dispatch) => {
  try {
    const result = await httpClient.get(`${server.GETID_URL}`);
    dispatch(setStateSWRFileToSuccess(result.data));
    return result.data;
  } catch (err) {
    alert(err.message);
    dispatch(setStateSWRFileToFailed());
  }
};

// export const doGetID1 = () => {
//   return async (dispatch) => {
//     try {
//       let result = await httpClient.get(`${server.GETID_URL}`);
//       return result.data;
//     } catch (err) {
//       alert(err.message);
//     }
//   };
// };
