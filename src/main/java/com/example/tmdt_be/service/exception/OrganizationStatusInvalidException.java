package com.example.tmdt_be.service.exception;

import me.coong.core.exception.ServiceBadException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OrganizationStatusInvalidException extends ServiceBadException {

    public OrganizationStatusInvalidException(String status) {
        super(String.format("Status %s is invalid", status));
    }

    @Override
    public String getErrorCode() {
        return "ORGANIZATION_STATUS_INVALID";
    }
}
