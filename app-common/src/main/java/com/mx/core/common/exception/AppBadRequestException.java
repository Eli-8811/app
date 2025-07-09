package com.mx.core.common.exception;


public class AppBadRequestException extends AppException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AppBadRequestException(String code, String title, String message) {
        super(code, title, message);
    }

    public AppBadRequestException(Throwable cause, String code, String title, String message) {
        super(cause, code, title, message);
    }
    
}