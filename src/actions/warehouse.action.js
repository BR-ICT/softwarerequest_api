import { httpClient } from "./../utils/HttpClient";
import {
  HTTP_WAREHOUSE_SUCCESS,
  HTTP_WAREHOUSE_FETCHING,
  HTTP_WAREHOUSE_FAILED,
  HTTP_WAREHOUSE_CLEAR,
  server,
} from "../constants";

export const setStateWarehouseToSuccess = (payload) => ({
  type: HTTP_WAREHOUSE_SUCCESS,
  payload,
});

const setStateWarehouseToFetching = () => ({
  type: HTTP_WAREHOUSE_FETCHING,
});

const setStateWarehouseToFailed = () => ({
  type: HTTP_WAREHOUSE_FAILED,
});

const setStateWarehouseToClear = () => ({
  type: HTTP_WAREHOUSE_CLEAR,
});

export const getWarehouse = () => {
  return async (dispatch) => {
    dispatch(setStateWarehouseToFetching());
    doGetWarehouse(dispatch);
  };
};

const doGetWarehouse = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.WAREHOUSE_URL}`);
    dispatch(setStateWarehouseToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(setStateWarehouseToFailed());
  }
};
