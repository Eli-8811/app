package com.mx.core.controller.config;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.dao.DataIntegrityViolationException;

import com.mx.core.common.exception.AppBadRequestException;
import com.mx.core.common.exception.AppException;
import com.mx.core.common.exception.AppNotFoundException;
import com.mx.core.common.exception.AppBadCredentialsException;
import com.mx.core.model.ResponseGeneric;

import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class RestAdvice extends ResponseEntityExceptionHandler {

	public abstract String getErrorCodeInitial();

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ResponseGeneric<Void>> handleGeneralException(Exception ex, WebRequest request) {
		log.error("Unexpected error: {}", request.getDescription(false), ex);
		return buildErrorResponse("Ocurrió un error interno en el servidor", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AppBadCredentialsException.class)
	public final ResponseEntity<ResponseGeneric<Void>> handleBadCredentialsException(AppBadCredentialsException ex,
			WebRequest request) {
		log.error("Authentication failed: {}", request.getDescription(false), ex);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseGeneric.buildError(ex.getMessage()));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<ResponseGeneric<Void>> handleConstraintViolationException(
			ConstraintViolationException ex, WebRequest request) {
		String message = ex.getConstraintViolations().stream().findFirst().map(this::formatViolation)
				.orElse("Entrada no válida");
		return buildErrorResponse(message, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ResponseGeneric<?>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
		String message = "Error de integridad de datos";
		Throwable cause = ex.getRootCause();
		if (cause != null && cause.getMessage() != null) {
			String causeMessage = cause.getMessage();
			message = causeMessage;
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseGeneric.buildError(message));
	}

	@ExceptionHandler(AppException.class)
	public final ResponseEntity<ResponseGeneric<Void>> handleAppException(AppException ex, WebRequest request) {
		log.error("Custom error: {}", request.getDescription(false), ex);
		return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AppNotFoundException.class)
	public final ResponseEntity<ResponseGeneric<Void>> handleNotFound(AppNotFoundException ex, WebRequest request) {
		log.error("Not found: {}", request.getDescription(false), ex);
		return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AppBadRequestException.class)
	public final ResponseEntity<ResponseGeneric<Void>> handleBadRequest(AppBadRequestException ex, WebRequest request) {
		log.error("Bad request: {}", request.getDescription(false), ex);
		return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<ResponseGeneric<Void>> buildErrorResponse(String message, HttpStatus status) {
		return ResponseEntity.status(status).body(ResponseGeneric.buildError(message));
	}

	private String formatViolation(ConstraintViolation<?> violation) {
		return violation.getMessage();
	}

}