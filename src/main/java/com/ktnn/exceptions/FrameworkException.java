package com.ktnn.exceptions;

/**
 * Định nghĩa exception dùng trong project này
 */
public class FrameworkException extends RuntimeException {
    public FrameworkException(String message) {
        super(message);
    }
    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
