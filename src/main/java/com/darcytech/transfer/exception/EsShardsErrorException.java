package com.darcytech.transfer.exception;

/**
 * User: dixi
 * Time: 2015/12/19 14:01
 */
public class EsShardsErrorException extends RuntimeException {

    public EsShardsErrorException() {
        super();
    }

    public EsShardsErrorException(String msg) {
        super(msg);
    }
}
