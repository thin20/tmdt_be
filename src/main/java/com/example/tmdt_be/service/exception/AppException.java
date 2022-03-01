package com.example.tmdt_be.service.exception;

import lombok.Getter;
import lombok.Setter;
import me.coong.core.exception.ServiceBadException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
@Setter
public class AppException extends ServiceBadException {

    String errorCode;
    String message;
    List<String> errorField;

    public AppException(String error, String message, List<String> errorField) {
        super(message);
        this.message = message;
        this.errorCode = error;
        this.errorField = errorField;
    }

    public AppException(String error, String message) {
        super(message);
        this.message = message;
        this.errorCode = error;
    }

    public AppException(String error) {
        super("");
        this.errorCode = error;
    }
}