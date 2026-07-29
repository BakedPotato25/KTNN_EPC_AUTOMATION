package com.ktnn.exceptions;

/**
 * Định nghĩa exception khi loại Headless không được hỗ trợ
 */
public class HeadlessNotSupportedException extends IllegalStateException {
    public HeadlessNotSupportedException(String browser) {
        super(String.format("The %s browser does not support headless mode", browser));
    }
}
