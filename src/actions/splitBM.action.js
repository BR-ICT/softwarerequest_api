import { httpClient } from "../utils/HttpClient";
import {
  HTTP_SPLITBM_SUCCESS,
  HTTP_SPLITBM_FETCHING,
  HTTP_SPLITBM_FAILED,
  HTTP_SPLITBM_CLEAR,
  server,
} from "../constants";

export const setStateSPLITBMToSuccess = (payload) => ({
  type: HTTP_SPLITBM_SUCCESS,
  payload,
});

const setStateSPLITBMToFetching = () => ({
  type: HTTP_SPLITBM_FETCHING,
});

const setStateSPLITBMToFailed = () => ({
  type: HTTP_SPLITBM_FAILED,
});

const setStateSPLITBMToClear = () => ({
  type: HTTP_SPLITBM_CLEAR,
});

export const splitstatement = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.SPLITBM_URL, formData);
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

// export const uploadstatement = (formData) => {
//   return async (dispatch) => {
//     // console.log(" whs: " + whs);
//     dispatch(setStateUPLOADToFetching());
//     doUploadstatement(formData);
//   };
// };

// export const doUploadstatement = async (formData, dispatch) => {
//   try {
//     let result = await httpClient.post(server.UPLOADSTATEMENT_URL, {
//       formData,
//     });
//     dispatch(setStateUPLOADToSuccess(result.data));
//     alert(JSON.stringify(result.data));
//   } catch (err) {
//     dispatch(setStateUPLOADToFailed());
//   }
// };

// export const addSWRfile = (formData, history) => {
//   return async (dispatch) => {
//     try {
//       let result = await httpClient.post(server.SWRFILE_URL, formData);
//       alert(JSON.stringify(result.data));
//       return result.data;
//     } catch (err) {
//       alert(err.message);
//     }
//   };
// };
