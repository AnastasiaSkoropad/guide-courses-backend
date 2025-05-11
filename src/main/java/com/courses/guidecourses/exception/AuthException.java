package com.courses.guidecourses.exception;

public class AuthException extends RuntimeException {
    private final String code;

    /**
     * @param code    свій код помилки, наприклад "AUTH_FAILED" або "AUTH_SERVER_UNAVAILABLE"
     * @param message повідомлення, яке потім повернеться клієнту
     */
    public AuthException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * Для того, щоб передати також початкову причину (наприклад, ResourceAccessException)
     */
    public AuthException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
