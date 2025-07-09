package com.mx.core.common.exception;

public class AppBadCredentialsException extends RuntimeException {
	
    private static final long serialVersionUID = 1L;

    private final String code;
    private final String title;
    private final String message;

    public AppBadCredentialsException(Throwable cause, String code, String title, String message) {
        super(message, cause);
        this.code = code;
        this.title = title;
        this.message = message;
    }

    public AppBadCredentialsException(String code, String title, String message) {
        super(message);
        this.code = code;
        this.title = title;
        this.message = message;
    }

	public String getCode() {
		return code;
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}
    
}