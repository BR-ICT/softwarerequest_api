import {
  HTTP_LOCATION_SUCCESS,
  HTTP_LOCATION_FETCHING,
  HTTP_LOCATION_FAILED,
  HTTP_LOCATION_CLEAR,
} from "../constants";

const initialState = {
  result: null,
  isFetching: false,
  isError: false,
};

export default (state = initialState, { type, payload }) => {
  switch (type) {
    case HTTP_LOCATION_FETCHING:
      return { ...state, result: null, isFetching: true, isError: false };
    case HTTP_LOCATION_FAILED:
      return { ...state, result: null, isFetching: false, isError: true };
    case HTTP_LOCATION_SUCCESS:
      return { ...state, result: payload, isFetching: false, isError: false };

    default:
      return state;
  }
};
