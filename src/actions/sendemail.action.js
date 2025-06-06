import { httpClient } from "./../utils/HttpClient";
import {
  HTTP_SENDEMAIL_SUCCESS,
  HTTP_SENDEMAIL_FETCHING,
  HTTP_SENDEMAIL_FAILED,
  HTTP_SENDEMAIL_CLEAR,
  server,
} from "../constants";
import * as prheadActions from "./prhead.action";

export const setStateSendEmailToSuccess = (payload) => ({
  type: HTTP_SENDEMAIL_SUCCESS,
  payload,
});

const setStateSendEmailToFetching = () => ({
  type: HTTP_SENDEMAIL_FETCHING,
});

const setStateSendEmailToFailed = () => ({
  type: HTTP_SENDEMAIL_FAILED,
});

const setStateSendEmailToClear = () => ({
  type: HTTP_SENDEMAIL_CLEAR,
});

export const reSendEmailMAR = (prefix, adrno, status, submit) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(
        `${server.RESENDEMAIL_URL}/${prefix}/${adrno}/${status}/${submit}`
      );
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};
