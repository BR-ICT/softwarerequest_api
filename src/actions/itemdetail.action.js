import { httpClient } from "../utils/HttpClient";
import {
  HTTP_ITEMDETAIL_SUCCESS,
  HTTP_ITEMDETAIL_FETCHING,
  HTTP_ITEMDETAIL_FAILED,
  HTTP_ITEMDETAIL_CLEAR,
  server,
} from "../constants";

export const setStateItemDetailToSuccess = (payload) => ({
  type: HTTP_ITEMDETAIL_SUCCESS,
  payload,
});

const setStateItemDetailToFetching = () => ({
  type: HTTP_ITEMDETAIL_FETCHING,
});

const setStateItemDetailToFailed = () => ({
  type: HTTP_ITEMDETAIL_FAILED,
});

const setStateItemDetailToClear = () => ({
  type: HTTP_ITEMDETAIL_CLEAR,
});

export const getItemDetails = (whs, item) => {
  return async (dispatch) => {
    // console.log("whs: " + whs + " item: " + item);
    dispatch(setStateItemDetailToFetching());
    doGetItemDetail(dispatch, whs, item);
  };
};

const doGetItemDetail = async (dispatch, whs, item) => {
  try {
    let result = await httpClient.get(
      `${server.ITEMDETAIL_URL}/${whs}/${item}`
    );
    dispatch(setStateItemDetailToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateItemDetailToFailed());
  }
};

export const fetchItemDetails = (whs, item) => {
  return async (dispatch) => {
    // console.log("whs: " + whs + " item: " + item);s
    dofetchItemDetail(dispatch, whs, item);
  };
};

const dofetchItemDetail = async (dispatch, whs, item) => {
  try {
    let result = await httpClient.get(
      `${server.ITEMDETAIL_URL}/${whs}/${item}`
    );
    dispatch(setStateItemDetailToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateItemDetailToFailed());
  }
};

export const addItemDetail = (formData, history) => {
  return async (dispatch) => {
    try {
      let result = await httpClient.post(server.ITEM_URL, formData);
      // alert(JSON.stringify(result.data));
      // await dispatch(adrnumberActions.addADRNumber(dispatch, result.data));
      return result.data;
    } catch (err) {
      alert(err.message);
    }
  };
};
