import {
  HTTP_LOCATION_FETCHING,
  HTTP_LOCATION_FAILED,
  HTTP_LOCATION_SUCCESS,
  HTTP_LOCATION_CLEAR,
} from "../constants";
import { server } from "../constants";
import { httpClient } from "../utils/HttpClient";

// Information being sent to Reducer
export const setStateLOCATIONToSuccess = (payload) => ({
  type: HTTP_LOCATION_SUCCESS,
  payload,
});

const setStateLOCATIONToFetching = () => ({
  type: HTTP_LOCATION_FETCHING,
});

const setStateLOCATIONToFailed = () => ({
  type: HTTP_LOCATION_FAILED,
});

const setStateLOCATIONToClear = () => ({
  type: HTTP_LOCATION_CLEAR,
});

export const getLocation = (CONO, DIVI) => {
  return async (dispatch) => {
    dispatch(setStateLOCATIONToFailed());
    doGetLocations(CONO, DIVI, dispatch);
  };
};

const doGetLocations = async (CONO, DIVI, dispatch) => {
  try {
    let result = await httpClient.get(`${server.LOCATION_URL}/${CONO}/${DIVI}`);
    dispatch(setStateLOCATIONToSuccess(result.data));
  } catch (err) {
    dispatch(setStateLOCATIONToFailed());
  }
};

// export const getCompanysWithConoDivi = (cono, divi) => {
//   return async (dispatch) => {
//     dispatch(setStateCompanyToFetching());
//     doGetCompanysWithConoDivi(dispatch, cono, divi);
//   };
// };

// const doGetCompanysWithConoDivi = async (dispatch, cono, divi) => {
//   try {
//     let result = await httpClient.get(
//       `${server.COMPANYWITHCONODIVI_URL}/${cono}/${divi}`
//     );
//     dispatch(setStateCompanyToSuccess(result.data));
//     // alert(JSON.stringify(result.data));
//   } catch (err) {
//     dispatch(setStateCompanyToFailed());
//   }
// };
