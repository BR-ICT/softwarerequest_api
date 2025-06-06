import { httpClient } from "../utils/HttpClient";
import {
  HTTP_CREATEID_SUCCESS,
  HTTP_CREATEID_FETCHING,
  HTTP_CREATEID_FAILED,
  HTTP_CREATEID_CLEAR,
  server,
} from "../constants";

export const setStateCREATEIDToSuccess = (payload) => ({
  type: HTTP_CREATEID_SUCCESS,
  payload,
});

const setStateCREATEIDToFetching = () => ({
  type: HTTP_CREATEID_FETCHING,
});

const setStateCREATEIDToFailed = () => ({
  type: HTTP_CREATEID_FAILED,
});

const setStateCREATEIDToClear = () => ({
  type: HTTP_CREATEID_CLEAR,
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

export const RETURNNORMAL = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.RETURNNORMAL_URL, formData);
      //alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const RETURNPARENT = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.RETURNPARENT_URL, formData);
      //alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const updateandgetID = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.UPDATEANDGETID_URL, formData);
      //alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const updateonly = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.UPDATEONLY_URL, formData);
      //alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const SAVEID = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.SAVEID_URL, formData);
      //alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

////////////

export const fetchgetitem = (selectedDate, selectedValue) => {
  return async (dispatch) => {
    // console.log("whs: " + whs + " item: " + item);s
    dofetchgetitem(dispatch, selectedDate, selectedValue);
  };
};

const dofetchgetitem = async (dispatch, selectedDate, selectedValue) => {
  try {
    let result = await httpClient.get(
      `${server.GETITEM_URL}/${selectedDate}/${selectedValue}`
    );
    dispatch(setStateCREATEIDToSuccess(result.data));
  } catch (err) {
    dispatch(setStateCREATEIDToFailed());
  }
};

////////////

export const getitem = (selectedDate, selectedValue) => {
  return async (dispatch) => {
    try {
      //alert(selectedDate + "  " + selectedValue);
      let result = await httpClient.get(
        `${server.GETITEM_URL}/${selectedDate}/${selectedValue}`
      );
      setStateCREATEIDToFetching();
      dispatch(setStateCREATEIDToSuccess(result.data));

      //alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const DELETEID = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.put(server.DELETEID_URL, formData);
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
