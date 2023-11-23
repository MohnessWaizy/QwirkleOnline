package org.GameLogic.Exceptions;

/**
 * Corresponds to a NotAllowed error defined in the interface
 */
public class NotAllowedException extends Exception {
	private ErrorType errorType;

	public NotAllowedException(String message) {
		this(ErrorType.ValidationError, message);
	}

	public NotAllowedException(ErrorType errorType, String message) {
		super(message);
		this.errorType = errorType;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

}
