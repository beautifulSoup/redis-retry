package com.tangokk.redisretry.lib.exception;

public class JsonSerializeException extends RuntimeException {

    public JsonSerializeException(String message, Throwable t) {
        super(message, t);
    }


}
