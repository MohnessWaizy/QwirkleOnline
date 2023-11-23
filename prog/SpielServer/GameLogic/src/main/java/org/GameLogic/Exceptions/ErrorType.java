package org.GameLogic.Exceptions;

/**
 * ValidationError are normal errors, where not Client is not punished for
 * creating the request. While BadRequestError, the amount of faulty requests is
 * increased, so that the client sending those can be kicked later.
 */
public enum ErrorType {
	ValidationError, BadRequestError
}
