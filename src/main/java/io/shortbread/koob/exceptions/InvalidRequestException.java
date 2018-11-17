package io.shortbread.koob.exceptions;

import lombok.Getter;

public class InvalidRequestException extends Exception {

    @Getter
    private String message;

    public InvalidRequestException(String message) {
        this.message = message;
    }
}
