package com.superquizzettone.web.api.exception;

import com.superquizzettone.dto.ResponseJSON;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler  {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseJSON<List<String>>> handleValidationExceptions(
			MethodArgumentNotValidException ex,
			WebRequest request) {

		List<String> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ResponseJSON.error(HttpStatus.BAD_REQUEST.value(), "Errore di validazione", errors));
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ResponseJSON<Void>> handleAgendaNotFoundException(NotFoundException ex, WebRequest request) {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(ResponseJSON.error(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
	}

	@ExceptionHandler(NotAllowedException.class)
	public ResponseEntity<ResponseJSON<Void>> handleNotAllowedException(NotAllowedException ex, WebRequest request) {
		return ResponseEntity
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseJSON.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage()));
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ResponseJSON<Void>> handleBadRequestException(BadRequestException ex, WebRequest request) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ResponseJSON.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
	}


	@ExceptionHandler(IdNotNullForInsertException.class)
	public ResponseEntity<ResponseJSON<Void>> handleIdNotNullForInsertException(IdNotNullForInsertException ex,
																				WebRequest request) {
		return ResponseEntity
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseJSON.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ResponseJSON<Void>> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ResponseJSON.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ResponseJSON<Void>> handleInsufficientAuthoritiesException(ForbiddenException ex, WebRequest request) {
		return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.body(ResponseJSON.error(HttpStatus.FORBIDDEN.value(), ex.getMessage()));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ResponseJSON<Void>> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
		return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.body(ResponseJSON.error(HttpStatus.FORBIDDEN.value(), ex.getMessage()));
	}

	@ExceptionHandler(UsernameNotValidException.class)
	public ResponseEntity<ResponseJSON<Void>> handleUsernameNotValidException(UsernameNotValidException ex, WebRequest request) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ResponseJSON.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
	}
	@ExceptionHandler(EmptyRoleException.class)
	public ResponseEntity<ResponseJSON<Void>> handleEmptyRoleException(EmptyRoleException ex, WebRequest request) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ResponseJSON.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
	}

}
