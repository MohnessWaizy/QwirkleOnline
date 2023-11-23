package de.upb.cs.swtpra_04.qwirkle.controller;

/**
 * Main control interface for every activity controller.
 * It implements messages
 */
public interface Controller {

    /**
     * Called when a not allowed Message was sent from the Server
     * @param message
     */
    public void handleNotAllowed(String message);

    /**
     * Called when a Parsing Error Message was sent from the Server
     * @param message
     */
    public void handleParsingError(String message);

    /**
     * Called when a access denied Message was sent from the Server
     * @param message
     */
    public void handleAccessDenied(String message);
}
