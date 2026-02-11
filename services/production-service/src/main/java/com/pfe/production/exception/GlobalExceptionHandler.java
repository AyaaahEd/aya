package com.pfe.production.exception;

import com.pfe.production.domain.ErrorEntry;
import com.pfe.production.dto.ErrorResponse;
import com.pfe.production.service.ErrorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorService errorService;

    public GlobalExceptionHandler(ErrorService errorService) {
        this.errorService = errorService;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorEntry error = errorService.logError(
                ex.getEntityType(),
                ex.getEntityId(),
                ex.getCode(),
                ex.getMessage(),
                getStackTraceAsString(ex));

        return new ResponseEntity<>(ErrorResponse.fromEntity(error), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorEntry error = errorService.logError(
                "SYSTEM",
                "UNKNOWN",
                "INTERNAL_ERROR",
                ex.getMessage(),
                getStackTraceAsString(ex));

        return new ResponseEntity<>(ErrorResponse.fromEntity(error), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getStackTraceAsString(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}
