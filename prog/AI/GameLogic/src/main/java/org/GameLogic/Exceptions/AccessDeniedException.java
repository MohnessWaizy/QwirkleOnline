package org.GameLogic.Exceptions;

/**
 * Corresponds to a AccessDenied error defined in the interface
 */
public class AccessDeniedException extends Exception {

	private ErrorType errorType;

	public AccessDeniedException(String message) {
		this(ErrorType.ValidationError, message);
	}

	public AccessDeniedException(ErrorType errorType, String message) {
		super(message);
		this.errorType = errorType;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

}
