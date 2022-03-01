package com.example.tmdt_be.service.exception;

import me.coong.core.exception.ServiceBadException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OrganizationNotFoundException extends ServiceBadException {

    public OrganizationNotFoundException(Long id) {
        super(String.format("ID %s is not found", id));
    }

    @Override
    public String getErrorCode() {
        return "ORGANIZATION_NOT_FOUND";
    }
}
