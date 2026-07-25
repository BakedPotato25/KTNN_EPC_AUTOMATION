package com.ktnn.exceptions;

/**
 * Define the not supported exception for the Headless type
 */
public class HeadlessNotSupportedException extends IllegalStateException {
    public HeadlessNotSupportedException(String browser) {
        super(String.format("The %s browser does not support headless mode", browser));
    }
}
