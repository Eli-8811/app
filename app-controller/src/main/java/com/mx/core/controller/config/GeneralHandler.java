package com.mx.core.controller.config;

import org.springframework.web.bind.annotation.ControllerAdvice;

import com.mx.core.controller.AuthController;

@ControllerAdvice(basePackageClasses = {
		AuthController.class
})
public class GeneralHandler extends RestAdvice {
	
	public String getErrorCodeInitial() {
		return "app-api";
	}
	
}
