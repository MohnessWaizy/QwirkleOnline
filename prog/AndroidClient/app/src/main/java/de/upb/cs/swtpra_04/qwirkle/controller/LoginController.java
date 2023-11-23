package de.upb.cs.swtpra_04.qwirkle.controller;


import de.upb.swtpra1819interface.messages.ConnectAccepted;

/**
 * Control interface for the login activity
 */
public interface LoginController extends Controller {

    /**
     * Called when the Server accepted the login
     * @param connectAccepted
     */
    public void onLoginAccepted(ConnectAccepted connectAccepted);
}
