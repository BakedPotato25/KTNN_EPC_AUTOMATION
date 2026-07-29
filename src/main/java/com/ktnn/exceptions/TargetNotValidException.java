package com.ktnn.exceptions;

/**
 * Định nghĩa exception khi dùng execution target không hợp lệ
 */
public class TargetNotValidException extends IllegalStateException {

    /**
     * Tạo exception khi dùng execution target không hợp lệ
     * @param target : Target muốn thực thi
     */
    public TargetNotValidException(String target) {
        super(String.format("Target %s not supported. Use either local or gird", target));
    }
}
