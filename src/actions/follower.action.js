import { httpClient } from "../utils/HttpClient";
import {
  HTTP_FOLLOWER_SUCCESS,
  HTTP_FOLLOWER_FETCHING,
  HTTP_FOLLOWER_FAILED,
  HTTP_FOLLOWER_CLEAR,
  server,
} from "../constants";

export const setStateFOLLOWERToSuccess = (payload) => ({
  type: HTTP_FOLLOWER_SUCCESS,
  payload,
});

const setStateFOLLOWERToFetching = () => ({
  type: HTTP_FOLLOWER_FETCHING,
});

const setStateFOLLOWERToFailed = () => ({
  type: HTTP_FOLLOWER_FAILED,
});

const setStateFOLLOWERToClear = () => ({
  type: HTTP_FOLLOWER_CLEAR,
});

// export const addVisitorHeader = (formData, history) => {
//   return async (dispatch) => {
//     try {
//       let result = await httpClient.put(server.ADDVISITORHEADER_URL, formData);
//       alert(JSON.stringify(result.data));
//       return result.data;
//     } catch (err) {
//       alert(err.message);
//     }
//   };
// };

// export const getEmployee = (history) => {
//   return async (dispatch) => {
//     try {
//       let result = await httpClient.get(server.GETEMPLOYEE_URL);
//       return result.data;
//     } catch (err) {
//       alert(err.message);
//     }
//   };
// };

// export const getEmployee = (history) => {
//   return async (dispatch) => {
//     try {
//       let result = await httpClient.get(`${server.GETEMPLOYEE_URL}`);
//       dispatch(setStateEMPLOYEEToSuccess(result.data));
//       return result.data;
//     } catch (err) {
//       alert(err.message);
//       dispatch(setStateEMPLOYEEToFailed());
//     }
//   };
// };

export const addFollower = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(server.ADDFOLLOWER_URL, formData);
      // alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const getFollower = (vID, CONO, DIVI, LOCATION) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.get(
        `${server.GETFOLLOWER_URL}/${vID}/${CONO}/${DIVI}/${LOCATION}`
      );
      dispatch(setStateFOLLOWERToSuccess(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
      dispatch(setStateFOLLOWERToFailed());
    }
  };
};

export const updateFollower = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.UPDATEFOLLOWER_URL, formData);
      alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const deletefollower = (ID, REMARK1, CONO, DIVI, LOCATION) => {
  return async (dispatch) => {
    try {
      await httpClient.delete(
        `${server.DELETEFOLLOWER_URL}/${ID}/${REMARK1}/${CONO}/${DIVI}/${LOCATION}`
      );
    } catch (err) {
      alert(err.message);
    }
  };
};
