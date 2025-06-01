package com.cts.blms.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

@ControllerAdvice
public class ExceptionHandlerController {

	@ExceptionHandler(InternalServerError.class)
	public String handleInternalServerError(Exception ex, Model model) {
		model.addAttribute("message", ex.getMessage());
		return "error";

	}

}
