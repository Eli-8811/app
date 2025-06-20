package com.mx.core.common.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String code;
    private final String title;
    private final String message;

    public AppException(Throwable cause, String code, String title, String message) {
        super(message, cause);
        this.code = code;
        this.title = title;
        this.message = message;
    }

    public AppException(String code, String title, String message) {
        super(message);
        this.code = code;
        this.title = title;
        this.message = message;
    }
    
}