package org.GameLogic.Exceptions;

/**
 * Corresponds to a parser error defined in the interface
 */
public class ParserException extends Exception {

	private ErrorType errorType;

	public ParserException(String message) {
		this(ErrorType.ValidationError, message);
	}

	public ParserException(ErrorType errorType, String message) {
		super(message);
		this.errorType = errorType;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

}
