package com.marParaiso.domain.exception;

public class InsufficientStockException extends  RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }
}
