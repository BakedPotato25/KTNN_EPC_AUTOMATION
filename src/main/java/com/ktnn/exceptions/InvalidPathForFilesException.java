package com.ktnn.exceptions;

/**
 * Class InvalidPathForFilesException để bắt exception liên quan tới file
 */
public class InvalidPathForFilesException extends FrameworkException {

	/**
	 * Throw exception khi đường dẫn không hợp lệ lúc thao tác với file
	 * @param message : Nội dung lỗi chi tiết
	 */
	public InvalidPathForFilesException(String message) {
		super(message);
	}
}
