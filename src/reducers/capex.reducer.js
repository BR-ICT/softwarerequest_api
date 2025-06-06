import {
  HTTP_CAPEX_SUCCESS,
  HTTP_CAPEX_FETCHING,
  HTTP_CAPEX_FAILED,
  HTTP_CAPEX_CLEAR,
} from "../constants";

const initialState = {
  result: null,
  isFetching: false,
  isError: false,
};

export default (state = initialState, { type, payload }) => {
  switch (type) {
    case HTTP_CAPEX_FETCHING:
      // return { ...state, result: null, isFetching: true, isError: false };
      return Object.assign({}, state, {
        ...state,
        result: null,
        isFetching: true,
        isError: false,
      });
    case HTTP_CAPEX_FAILED:
      // return { ...state, result: null, isFetching: false, isError: true };
      return Object.assign({}, state, {
        ...state,
        result: null,
        isFetching: false,
        isError: true,
      });
    case HTTP_CAPEX_SUCCESS:
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
