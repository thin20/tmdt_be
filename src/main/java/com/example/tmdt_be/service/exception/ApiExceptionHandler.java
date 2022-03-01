package com.example.tmdt_be.service.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.common.MessageResource;

@RestControllerAdvice
public class ApiExceptionHandler {

    @Autowired
    private MessageResource messageResource;

    /**
     * Tất cả các Exception không được khai báo sẽ được xử lý tại đây
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleAllException(Exception ex, WebRequest request) {
        // quá trình kiểm soat lỗi diễn ra ở đây
        return new ErrorMessage("Lỗi của Trang", ex.getLocalizedMessage(), null);
    }

    @ExceptionHandler(AppException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage TodoAppException(AppException ex, WebRequest request) {
        String errorCode = DataUtil.safeToString(ex.getErrorCode());
        int errorSize = 0;
        if (ex != null && ex.getErrorField() != null && ex.getErrorField().size() > 0) {
            errorSize = ex.getErrorField().size();
        }
        String[] arrayError = null;
        if (errorSize > 0) {
            arrayError = new String[errorSize];
            int i = 0;
            for (String field : ex.getErrorField()) {
                arrayError[i] = DataUtil.safeToString(field);
                i++;
            }
        }
        String message = "";
        if (errorSize > 0) {
            message = messageResource.getMessage(errorCode, arrayError);
        } else {
            message = messageResource.getMessage(errorCode);
        }

        if (DataUtil.isStringNullOrEmpty(message)) {
            message = DataUtil.safeToString(ex.getMessage());
        }

        return new ErrorMessage(errorCode, message, ex != null ? ex.getErrorField() : null);
    }
}
