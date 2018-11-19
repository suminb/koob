package io.shortbread.koob.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends Exception {

    @Getter
    private String message;

    public InvalidRequestException(String message) {
        this.message = message;
    }
}
