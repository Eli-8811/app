package com.mx.core.common.exception;

public class AppNotFoundException extends AppException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AppNotFoundException(String code, String title, String message) {
		super(code, title, message);
 	}
	
}