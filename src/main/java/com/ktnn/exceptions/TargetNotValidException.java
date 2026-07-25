package com.ktnn.exceptions;

/**
 * Define the exception when using the invalid execution target
 */
public class TargetNotValidException extends IllegalStateException {

    /**
     * Creates an exception when using the invalid execution target
     * @param target : The target which you want to execute
     */
    public TargetNotValidException(String target) {
        super(String.format("Target %s not supported. Use either local or gird", target));
    }
}
