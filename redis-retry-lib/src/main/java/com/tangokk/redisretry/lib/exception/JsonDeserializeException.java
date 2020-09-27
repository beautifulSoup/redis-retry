package com.tangokk.redisretry.lib.exception;

public class JsonDeserializeException extends RuntimeException {


    public JsonDeserializeException(String message, Throwable t) {
        super(message, t);
    }

}
