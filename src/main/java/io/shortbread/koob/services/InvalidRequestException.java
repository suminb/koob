package io.shortbread.koob.services;

import lombok.Getter;

public class InvalidRequestException extends Exception {

    @Getter
    private String message;

    public InvalidRequestException(String message) {
        this.message = message;
    }
}
