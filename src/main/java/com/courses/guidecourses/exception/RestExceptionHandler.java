package com.courses.guidecourses.exception;

import com.courses.guidecourses.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    // ... ваші існуючі обробники ...

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException ex) {
        ErrorResponse body = new ErrorResponse(ex.getCode(), ex.getMessage());
        HttpStatus status;

        switch (ex.getCode()) {
            case "AUTH_SERVER_UNAVAILABLE":
                status = HttpStatus.SERVICE_UNAVAILABLE;
                break;
            case "AUTH_FAILED":
                status = HttpStatus.UNAUTHORIZED;
                break;
            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        log.error("Auth error [{}]: {}", ex.getCode(), ex.getMessage(), ex);
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleDataAccess(DataAccessException ex) {
        log.error("Database connectivity issue", ex);
        return new ErrorResponse("DB_CONNECTION_ERROR",
                "Неможливо з’єднатися з базою даних. Спробуйте пізніше.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDbError(DataIntegrityViolationException ex) {
        String msg = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();
        return new ErrorResponse("DB_ERROR", msg);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOthers(Exception ex) {
        log.error("Unhandled exception", ex);
        return new ErrorResponse("INTERNAL_ERROR", "Сталася невідома помилка");
    }
}
