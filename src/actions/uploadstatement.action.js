import { httpClient } from "../utils/HttpClient";
import {
  HTTP_UPLOAD_SUCCESS,
  HTTP_UPLOAD_FETCHING,
  HTTP_UPLOAD_FAILED,
  HTTP_UPLOAD_CLEAR,
  server,
} from "../constants";

export const setStateUPLOADToSuccess = (payload) => ({
  type: HTTP_UPLOAD_SUCCESS,
  payload,
});

const setStateUPLOADToFetching = () => ({
  type: HTTP_UPLOAD_FETCHING,
});

const setStateUPLOADToFailed = () => ({
  type: HTTP_UPLOAD_FAILED,
});

const setStateUPLOADToClear = () => ({
  type: HTTP_UPLOAD_CLEAR,
});

export const uploadstatement = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.UPLOADSTATEMENT_URL, formData);
      //alert(JSON.stringify(result.data));
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
