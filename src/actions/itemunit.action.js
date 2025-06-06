import { httpClient } from "./../utils/HttpClient";
import {
  HTTP_ITEMUNIT_SUCCESS,
  HTTP_ITEMUNIT_FETCHING,
  HTTP_ITEMUNIT_FAILED,
  HTTP_ITEMUNIT_CLEAR,
  server,
} from "../constants";

export const setStateItemUnitToSuccess = (payload) => ({
  type: HTTP_ITEMUNIT_SUCCESS,
  payload,
});

const setStateItemUnitToFetching = () => ({
  type: HTTP_ITEMUNIT_FETCHING,
});

const setStateItemUnitToFailed = () => ({
  type: HTTP_ITEMUNIT_FAILED,
});

const setStateItemUnitToClear = () => ({
  type: HTTP_ITEMUNIT_CLEAR,
});

export const getItemUnits = (item) => {
  return async (dispatch) => {
    // console.log("item: " + item);
    dispatch(setStateItemUnitToFetching());
    doGetItemUnit(dispatch, item);
  };
};

const doGetItemUnit = async (dispatch, item) => {
  try {
    let result = await httpClient.get(`${server.ITEMUNIT_URL}/${item}`);
    dispatch(setStateItemUnitToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateItemUnitToFailed());
  }
};
