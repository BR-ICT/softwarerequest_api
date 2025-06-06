import { httpClient } from "../utils/HttpClient";
import {
  HTTP_TEXTM3_SUCCESS,
  HTTP_TEXTM3_FETCHING,
  HTTP_TEXTM3_FAILED,
  HTTP_TEXTM3_CLEAR,
  server,
} from "../constants";

export const setStateTextM3ToSuccess = (payload) => ({
  type: HTTP_TEXTM3_SUCCESS,
  payload,
});

const setStateTextM3ToFetching = () => ({
  type: HTTP_TEXTM3_FETCHING,
});

const setStateTextM3ToFailed = () => ({
  type: HTTP_TEXTM3_FAILED,
});

const setStateTextM3ToClear = () => ({
  type: HTTP_TEXTM3_CLEAR,
});

export const getTextM3s = (textid) => {
  return async (dispatch) => {
    dispatch(setStateTextM3ToFetching());
    doGetTextM3(dispatch, textid);
  };
};

const doGetTextM3 = async (dispatch, textid) => {
  try {
    let result = await httpClient.get(`${server.TEXTM3_URL}/${textid}`);
    dispatch(setStateTextM3ToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateTextM3ToFailed());
  }
};

export const updateTextM3 = (prno, line, text) => {
  return async (dispatch) => {
    dispatch(setStateTextM3ToFetching());
    doUpdateTextM3(dispatch, prno, line, text);
  };
};

const doUpdateTextM3 = async (dispatch, prno, line, text) => {
  try {
    await httpClient.post(`${server.UPDATETEXTM3_URL}/${prno}/${line}/${text}`);

    // let result = await httpClient.get(`${server.PODETAIL_URL}/${prno}`);
    // dispatch(prdetailbuyerActions.setStatePRDetailBuyerToSuccess(result.data));

    // alert(JSON.stringify(result.data));
    // dispatch(setStateGenPOToSuccess(result.data));
  } catch (err) {
    dispatch(setStateTextM3ToFailed());
  }
};
