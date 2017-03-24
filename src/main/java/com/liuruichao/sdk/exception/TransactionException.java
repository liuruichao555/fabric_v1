package com.liuruichao.sdk.exception;

/**
 * TransactionException
 *
 * @author liuruichao
 * Created on 2017/3/24 17:05
 */
public class TransactionException extends Exception {
    private static final long serialVersionUID = 1L;

    public TransactionException(String message, Throwable parent) {
        super(message, parent);
    }

    public TransactionException(String message) {
        super(message);
    }
    public TransactionException(Throwable t) {
        super(t);
    }
}
