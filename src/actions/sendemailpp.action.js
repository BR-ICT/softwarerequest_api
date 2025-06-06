import { httpClient } from "../utils/HttpClient";
import {
  HTTP_SENDEMAILPP_SUCCESS,
  HTTP_SENDEMAILPP_FETCHING,
  HTTP_SENDEMAILPP_FAILED,
  HTTP_SENDEMAILPP_CLEAR,
  server,
} from "../constants";
// import * as prheadActions from "./prhead.action";

export const setStateSendEmailPPToSuccess = (payload) => ({
  type: HTTP_SENDEMAILPP_SUCCESS,
  payload,
});

const setStateSendEmailPPToFetching = () => ({
  type: HTTP_SENDEMAILPP_FETCHING,
});

const setStateSendEmailPPToFailed = () => ({
  type: HTTP_SENDEMAILPP_FAILED,
});

const setStateSendEmailPPToClear = () => ({
  type: HTTP_SENDEMAILPP_CLEAR,
});

export const SendEmail = (prefix, ordno, status, submit) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(
        `${server.SENDEMAILPP_URL}/${prefix}/${ordno}/${status}/${submit}`
      );

      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const SendEmailwithoutauthen = (
  prefix,
  ordno,
  status,
  submit,
  cono,
  divi,
  location,
  vMeetdate,
  vMeettime,
  vName,
  vSurname,
  vROOMNO,
  vRemark
) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(
        `${server.SENDEMAILWITHOUTAUTHEN_URL}/` +
          `${prefix}/` +
          `${ordno}/` +
          `${status}/` +
          `${submit}/` +
          `${cono}/` +
          `${divi}/` +
          `${location}/` +
          `${vMeetdate}/` +
          `${vMeettime}/` +
          `${vName}/` +
          `${vSurname}/` +
          `${vROOMNO}/` +
          `${vRemark}`
      );

      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const SendEmailFAP = (
  prefix,
  ordno,
  status,
  submit,
  foodcheck,
  foodqty,
  atkcheck,
  atkqty,
  parkcheck,
  parkqty,
  etc,
  beveragecheck,
  beveragenumber,
  snackcheck,
  snacksnumber,
  sandalcheck,
  sandalnumber,
  meetdate,
  meettime,
  meetdateout,
  meettimeout,
  room,
  company,
  name,
  surname
) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(
        `${server.SENDEMAILFAP_URL}/${prefix}/${ordno}/${status}/${submit}/${foodcheck}
        /${foodqty}/${atkcheck}/${atkqty}/${parkcheck}/${parkqty}/${etc}/${beveragecheck}
        /${beveragenumber}/${snackcheck}/${snacksnumber}/${sandalcheck}/${sandalnumber}
        /${meetdate}/${meettime}/${meetdateout}/${meettimeout}/${room}/${company}/${name}
        /${surname}`
      );

      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};

export const SendEmailxx = (prefix) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(`${server.SENDEMAILXX_URL}/${prefix}`);

      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};
