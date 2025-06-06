import { httpClient } from "../utils/HttpClient";
import {
  HTTP_DELIVERY_SUCCESS,
  HTTP_DELIVERY_FETCHING,
  HTTP_DELIVERY_FAILED,
  HTTP_DELIVERY_CLEAR,
  server,
} from "../constants";

export const HsetStateDeliveryToSuccess = (payload) => ({
  type: HTTP_DELIVERY_SUCCESS,
  payload,
});

const HsetStateDeliveryToFetching = () => ({
  type: HTTP_DELIVERY_FETCHING,
});

const HsetStateDeliveryToFailed = () => ({
  type: HTTP_DELIVERY_FAILED,
});

const HsetStateDeliveryToClear = () => ({
  type: HTTP_DELIVERY_CLEAR,
});

export const getDeliverys = () => {
  return async (dispatch) => {
    dispatch(HsetStateDeliveryToFetching());
    doGetDelivery(dispatch);
  };
};

const doGetDelivery = async (dispatch) => {
  try {
    let result = await httpClient.get(`${server.DELIVERY_URL}`);
    dispatch(HsetStateDeliveryToSuccess(result.data));
    // alert(JSON.stringify(result.data));
  } catch (err) {
    dispatch(HsetStateDeliveryToFailed());
  }
};
