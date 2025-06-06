import {
  HTTP_COMPANY_SUCCESS,
  HTTP_COMPANY_FETCHING,
  HTTP_COMPANY_FAILED,
  HTTP_COMPANY_CLEAR,
} from "../constants";

const initialState = {
  result: null,
  isFetching: false,
  isError: false,
};

export default (state = initialState, { type, payload }) => {
  switch (type) {
    case HTTP_COMPANY_FETCHING:
      return { ...state, result: null, isFetching: true, isError: false };
    case HTTP_COMPANY_FAILED:
      return { ...state, result: null, isFetching: false, isError: true };
    case HTTP_COMPANY_SUCCESS:
      return { ...state, result: payload, isFetching: false, isError: false };

    default:
      return state;
  }
};
