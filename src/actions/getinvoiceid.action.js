import { httpClient } from "../utils/HttpClient";
import {
  HTTP_GETINVOICEID_SUCCESS,
  HTTP_GETINVOICEID_FETCHING,
  HTTP_GETINVOICEID_FAILED,
  HTTP_GETINVOICEID_CLEAR,
  server,
} from "../constants";

export const setStateGETINVOICEIDToSuccess = (payload) => ({
  type: HTTP_GETINVOICEID_SUCCESS,
  payload,
});

const setStateGETINVOICEIDToFetching = () => ({
  type: HTTP_GETINVOICEID_FETCHING,
});

const setStateGETINVOICEIDToFailed = () => ({
  type: HTTP_GETINVOICEID_FAILED,
});

const setStateGETINVOICEIDToClear = () => ({
  type: HTTP_GETINVOICEID_CLEAR,
});

export const getinvoiceid = () => {
  return async (dispatch) => {
    try {
      let result = await httpClient.get(`${server.GETINVOICEID_URL}`);
      setStateGETINVOICEIDToFetching();
      dispatch(setStateGETINVOICEIDToSuccess(result.data));

      //alert(JSON.stringify(result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};
