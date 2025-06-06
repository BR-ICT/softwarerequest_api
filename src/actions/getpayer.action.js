import { httpClient } from "../utils/HttpClient";
import {
  HTTP_GETPAYER_SUCCESS,
  HTTP_GETPAYER_FETCHING,
  HTTP_GETPAYER_FAILED,
  HTTP_GETPAYER_CLEAR,
  server,
} from "../constants";

export const setStateGETPAYERToSuccess = (payload) => ({
  type: HTTP_GETPAYER_SUCCESS,
  payload,
});

const setStateGETPAYERToFetching = () => ({
  type: HTTP_GETPAYER_FETCHING,
});

const setStateGETPAYERToFailed = () => ({
  type: HTTP_GETPAYER_FAILED,
});

const setStateGETPAYERToClear = () => ({
  type: HTTP_GETPAYER_CLEAR,
});

export const getpayer = () => {
  return async (dispatch) => {
    try {
      let result = await httpClient.get(`${server.GETPAYER_URL}`);
      setStateGETPAYERToFetching();
      dispatch(setStateGETPAYERToSuccess(result.data));

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
