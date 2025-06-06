import {
  HTTP_PERIOD_SUCCESS,
  HTTP_PERIOD_FETCHING,
  HTTP_PERIOD_FAILED,
  HTTP_PERIOD_CLEAR,
} from "../constants";

const initialState = {
  result: null,
  isFetching: false,
  isError: false,
};

export default (state = initialState, { type, payload }) => {
  switch (type) {
    case HTTP_PERIOD_FETCHING:
      return { ...state, result: null, isFetching: true, isError: false };
    case HTTP_PERIOD_FAILED:
      return { ...state, result: null, isFetching: false, isError: true };
    case HTTP_PERIOD_SUCCESS:
      return { ...state, result: payload, isFetching: false, isError: false };

    default:
      return state;
  }
};
