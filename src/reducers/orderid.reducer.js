import {
  HTTP_ORDERID_SUCCESS,
  HTTP_ORDERID_FETCHING,
  HTTP_ORDERID_FAILED,
  HTTP_ORDERID_CLEAR,
} from "../constants";

const initialState = {
  result: null,
  isFetching: false,
  isError: false,
};

export default (state = initialState, { type, payload }) => {
  switch (type) {
    case HTTP_ORDERID_FETCHING:
      // return { ...state, result: null, isFetching: true, isError: false };
      return Object.assign({}, state, {
        ...state,
        result: null,
        isFetching: true,
        isError: false,
      });
    case HTTP_ORDERID_FAILED:
      // return { ...state, result: null, isFetching: false, isError: true };
      return Object.assign({}, state, {
        ...state,
        result: null,
        isFetching: false,
        isError: true,
      });
    case HTTP_ORDERID_SUCCESS:
      // return { ...state, result: payload, isFetching: false, isError: false };
      return Object.assign({}, state, {
        ...state,
        result: payload,
        isFetching: false,
        isError: false,
      });

    default:
      return state;
  }
};
