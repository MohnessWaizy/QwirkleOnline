package org.GameLogic.Communication;

import java.util.Map;

import org.GameLogic.Handler.GameHandler;
import org.GameLogic.Handler.GameManagement;
import org.NetworkInterface.MessageWithClientId;

import de.upb.swtpra1819interface.models.Client;

/**
 * Interface for a routing function for messages, to enable more specific
 * behavior in certain situations.
 */
public interface GameRunner {

	/**
	 * 
	 * @param messageWithClientId Message
	 * @param gameHandler         Message will be applied to the GameHandler
	 * @param gameManagement      Oversees games and clients
	 * @param idMapping           Mapping from ClientId to Client object
	 * @param gameId              Id of the game
	 */
	public void routeMessage(MessageWithClientId messageWithClientId, GameHandler gameHandler,
			GameManagement gameManagement, Map<Integer, Client> idMapping, int gameId);

}
