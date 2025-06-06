package com.br.utility;

public interface SecurityConstants {
    String HEADER_REQUEST_TOKEN = "x-access-token";
    String HEADER_REQUEST_UUID = "x-access-uuid";
    String HEADER_REQUEST_USER = "x-access-user";
    String MDC_UUID_KEY = "UUID";
    String TOKEN_PREFIX = "Bearer ";
    String CLAIMS_ROLE = "role";
    long EXPIRATION_TIME = 28800000; // 8 Hours
}