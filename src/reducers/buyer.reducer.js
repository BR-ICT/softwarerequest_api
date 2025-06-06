import {
    HTTP_BUYER_SUCCESS,
    HTTP_BUYER_FETCHING,
    HTTP_BUYER_FAILED,
    HTTP_BUYER_CLEAR
  } from "../constants";
  
  const initialState = {
    result: null,
    isFetching: false,
    isError: false
  };
  
  export default (state = initialState, { type, payload }) => {
    switch (type) {
      case HTTP_BUYER_FETCHING:
        // return { ...state, result: null, isFetching: true, isError: false };
        return Object.assign({}, state, {
          ...state,
          result: null,
          isFetching: true,
          isError: false
        });
      case HTTP_BUYER_FAILED:
        // return { ...state, result: null, isFetching: false, isError: true };
        return Object.assign({}, state, {
          ...state,
          result: null,
          isFetching: false,
          isError: true
        });
      case HTTP_BUYER_SUCCESS:
        // return { ...state, result: payload, isFetching: false, isError: false };
        return Object.assign({}, state, {
          ...state,
          result: payload,
          isFetching: false,
          isError: false
        });
  
      default:
        return state;
    }
  };
  