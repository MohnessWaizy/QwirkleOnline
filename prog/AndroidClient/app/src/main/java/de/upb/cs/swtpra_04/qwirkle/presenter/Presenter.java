package de.upb.cs.swtpra_04.qwirkle.presenter;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import de.upb.cs.swtpra_04.qwirkle.controller.Controller;
import de.upb.cs.swtpra_04.qwirkle.controller.GameController;
import de.upb.cs.swtpra_04.qwirkle.controller.GameControllerPlayer;
import de.upb.cs.swtpra_04.qwirkle.controller.GameControllerSpectator;
import de.upb.cs.swtpra_04.qwirkle.controller.GameFinishedController;
import de.upb.cs.swtpra_04.qwirkle.controller.LobbyController;
import de.upb.cs.swtpra_04.qwirkle.controller.LoginController;


import de.upb.cs.swtpra_04.qwirkle.model.network.NetClient;
import de.upb.cs.swtpra_04.qwirkle.view.LoginActivity;
import de.upb.swtpra1819interface.messages.AbortGame;
import de.upb.swtpra1819interface.messages.AccessDenied;
import de.upb.swtpra1819interface.messages.BagRequest;
import de.upb.swtpra1819interface.messages.BagResponse;
import de.upb.swtpra1819interface.messages.ConnectAccepted;
import de.upb.swtpra1819interface.messages.CurrentPlayer;
import de.upb.swtpra1819interface.messages.EndGame;
import de.upb.swtpra1819interface.messages.GameDataRequest;
import de.upb.swtpra1819interface.messages.GameDataResponse;
import de.upb.swtpra1819interface.messages.GameJoinAccepted;
import de.upb.swtpra1819interface.messages.GameJoinRequest;
import de.upb.swtpra1819interface.messages.GameListRequest;
import de.upb.swtpra1819interface.messages.GameListResponse;
import de.upb.swtpra1819interface.messages.LeavingPlayer;
import de.upb.swtpra1819interface.messages.LeavingRequest;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.messages.MessageSend;
import de.upb.swtpra1819interface.messages.MessageSignal;
import de.upb.swtpra1819interface.messages.MoveValid;
import de.upb.swtpra1819interface.messages.NotAllowed;
import de.upb.swtpra1819interface.messages.ParsingError;
import de.upb.swtpra1819interface.messages.PauseGame;
import de.upb.swtpra1819interface.messages.PlayTiles;
import de.upb.swtpra1819interface.messages.PlayerHandsRequest;
import de.upb.swtpra1819interface.messages.PlayerHandsResponse;
import de.upb.swtpra1819interface.messages.ResumeGame;
import de.upb.swtpra1819interface.messages.ScoreRequest;
import de.upb.swtpra1819interface.messages.ScoreResponse;
import de.upb.swtpra1819interface.messages.SendTiles;
import de.upb.swtpra1819interface.messages.SpectatorJoinAccepted;
import de.upb.swtpra1819interface.messages.SpectatorJoinRequest;
import de.upb.swtpra1819interface.messages.StartGame;
import de.upb.swtpra1819interface.messages.StartTiles;
import de.upb.swtpra1819interface.messages.TileSwapRequest;
import de.upb.swtpra1819interface.messages.TileSwapResponse;
import de.upb.swtpra1819interface.messages.TileSwapValid;
import de.upb.swtpra1819interface.messages.TotalTimeRequest;
import de.upb.swtpra1819interface.messages.TotalTimeResponse;
import de.upb.swtpra1819interface.messages.TurnTimeLeftRequest;
import de.upb.swtpra1819interface.messages.TurnTimeLeftResponse;
import de.upb.swtpra1819interface.messages.Update;
import de.upb.swtpra1819interface.messages.Winner;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.models.Game;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;

/**
 * Main class for providing messages to the controllers
 */
public class Presenter extends Application {

    /**
     * Tag for Log
     */
    private static final String TAG = "Presenter";

    /**
     * In whole game will only exists one presenterU
     */
    private static Presenter presenter;

    /**
     * NetClient for connection handling.
     */
    private NetClient client;
    private int clientID;

    private Controller activeController;
    private LobbyController lobbyController;
    private GameController gameController;
    private GameFinishedController gameFinishedController;
    private LoginController loginController;

    private Game joinedGame;

    /**
     * Timeout for connection establishing.
     * If connection did not set up properly within given timout,
     * connection establishing will be exited.
     */
    private short timeoutEstablishConnection = 2000;

    @Override
    public void onCreate() {
        super.onCreate();

        presenter = this;
        activeController = null;
        joinedGame = null;
    }

    public void registerLoginActivity(LoginController controller) {
        loginController = controller;
    }

    public void registerLobbyActivity(LobbyController controller) {
        lobbyController = controller;
    }

    public void registerGameActivity(GameController controller) {
        gameController = controller;
    }

    public void registerGameFinishedActivity(GameFinishedController controller) {
        gameFinishedController = controller;
    }

    /**
     * Will block thread for until maximum timeout reached or connection is established.
     *
     * @param username
     * @param ip
     * @param port
     * @return
     */
    public boolean connectClientToServer(String username, String ip, int port) {
        Log.d(TAG, "REQUEST: Connect to Server");
        if (client != null) {
            client.disconnect();
            client = null;
        }

        client = new NetClient(ip, port, this, timeoutEstablishConnection);
        if (client.checkConnection()) {
            client.connectToServer(username, ClientType.PLAYER);
            return true;
        } else {
            return false;
        }
    }

    /********* Gamejoin and Chat ****************/

    public void gameListRequest() {
        Log.d(TAG, "REQUEST: Game List");
        GameListRequest gameListRequest = new GameListRequest();
        client.sendMessage(gameListRequest);
    }

    public void spectatorJoinRequest(int selectedGame) {
        Log.d(TAG, "REQUEST: Spectator Join");
        SpectatorJoinRequest spectatorJoinRequest = new SpectatorJoinRequest(selectedGame);
        client.sendMessage(spectatorJoinRequest);
    }

    public void gameJoinRequest(int selectedGame) {
        Log.d(TAG, "REQUEST: Player Join");
        GameJoinRequest gameJoinRequest = new GameJoinRequest(selectedGame);
        client.sendMessage(gameJoinRequest);

    }

    public void messageSend(String message) {
        Log.d(TAG, "REQUEST: Send Message");
        MessageSend messageSend = new MessageSend(message);
        client.sendMessage(messageSend);
    }

    /************************* Gamelogic ****************************/

    public void leavingRequest() {
        Log.d(TAG, "REQUEST: Leaving");
        LeavingRequest leavingRequest = new LeavingRequest();
        client.sendMessage(leavingRequest);
    }

    public void scoreRequest() {
        Log.d(TAG, "REQUEST: Score Update");
        ScoreRequest scoreRequest = new ScoreRequest();
        client.sendMessage(scoreRequest);
    }

    public void turnTimeLeftRequest() {
        Log.d(TAG, "REQUEST: Turn time left");
        TurnTimeLeftRequest turnTimeLeftRequest = new TurnTimeLeftRequest();
        client.sendMessage(turnTimeLeftRequest);
    }

    public void totalTimeRequest() {
        Log.d(TAG, "REQUEST: Total time");
        TotalTimeRequest totalTimeRequest = new TotalTimeRequest();
        client.sendMessage(totalTimeRequest);
    }

    public void bagRequest() {
        Log.d(TAG, "REQUEST: Bag");
        BagRequest bagRequest = new BagRequest();
        client.sendMessage(bagRequest);
    }

    public void playerHandRequest() {
        Log.d(TAG, "REQUEST: Player hands");
        PlayerHandsRequest playerHandsRequest = new PlayerHandsRequest();
        client.sendMessage(playerHandsRequest);
    }

    public void gameDataRequest() {
        Log.d(TAG, "REQUEST: Game Data");
        GameDataRequest gameDataRequest = new GameDataRequest();
        client.sendMessage(gameDataRequest);
    }

    public void tileSwapRequest(Collection<Tile> tiles) {
        Log.d(TAG, "REQUEST: Tile swap");
        TileSwapRequest tileSwapRequest = new TileSwapRequest(tiles);
        client.sendMessage(tileSwapRequest);
    }

    public void playTileRequest(Collection<TileOnPosition> tiles) {
        Log.d(TAG, "REQUEST: Play tiles");
        PlayTiles playTileRequest = new PlayTiles(tiles);
        client.sendMessage(playTileRequest);
    }

    /**
     * Method will be called by NetClient.
     * Every Message reading by NetClient is passed through this method.
     *
     * @param message
     */
    public void processMessage(Message message) {
        int id = message.getUniqueId();
        Log.d(TAG, "Received Message ID: " + id);

        switch (id) {
            case 101:
                ConnectAccepted connectAccepted = (ConnectAccepted) message;
                loginController.onLoginAccepted(connectAccepted);
                setClientID(connectAccepted.getClientId());
                break;
            case 301:
                GameListResponse gameListResponse = (GameListResponse) message;
                lobbyController.updateGameList((ArrayList<Game>) gameListResponse.getGames());
                break;
            case 303:
                GameJoinAccepted gameJoinAccepted = (GameJoinAccepted) message;
                lobbyController.acceptPlayerJoinRequest(gameJoinAccepted.getGame());
                break;
            case 305:
                SpectatorJoinAccepted spectatorJoinAccepted = (SpectatorJoinAccepted) message;
                lobbyController.acceptSpectatorJoinRequest(spectatorJoinAccepted.getGameId());
                break;
            case 307:
                MessageSignal messageSignal = (MessageSignal) message;
                gameController.addChatMessage(messageSignal.getClient(), messageSignal.getMessage());
                break;
            case 400:
                StartGame startGame = (StartGame) message;
                gameController.startGame(startGame.getConfig(), (ArrayList<de.upb.swtpra1819interface.models.Client>) startGame.getClients());
                break;
            case 401:
                EndGame endGame = (EndGame) message;
                gameController.endGame();
                break;
            case 402:
                AbortGame abortGame = (AbortGame) message;
                gameController.abortGame();
                break;
            case 403:
                PauseGame pauseGame = (PauseGame) message;
                gameController.pauseGame();
                break;
            case 404:
                ResumeGame resumeGame = (ResumeGame) message;
                gameController.resumeGame();
                break;
            case 406:
                LeavingPlayer leavingPlayer = (LeavingPlayer) message;
                gameController.notifyPlayerLeft(leavingPlayer.getClient().getClientId(), leavingPlayer.getClient().getClientName(), leavingPlayer.getClient().getClientType());
                break;
            case 407:
                Winner winner = (Winner) message;
                while (gameFinishedController == null) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
                gameFinishedController.setWinner(winner.getClient().getClientId(), winner.getClient().getClientName(), winner.getScore());
                gameFinishedController.setLeaderboard((HashMap<de.upb.swtpra1819interface.models.Client, Integer>) winner.getLeaderboard());
                gameFinishedController.showScene();

                break;
            case 408:
                StartTiles startTiles = (StartTiles) message;
                if (gameController instanceof GameControllerPlayer) {
                    GameControllerPlayer controller = (GameControllerPlayer) gameController;
                    controller.startTiles(startTiles.getTiles());
                }
                break;
            case 409:
                CurrentPlayer currentPlayer = (CurrentPlayer) message;
                gameController.setCurrentPlayer(currentPlayer.getClient());
                break;
            case 410:
                SendTiles sendTiles = (SendTiles) message;
                if (gameController instanceof GameControllerPlayer) {
                    GameControllerPlayer controller = (GameControllerPlayer) gameController;
                    controller.addTilesToHand(sendTiles.getTiles());
                }
                break;
            case 412:
                TileSwapValid tileSwapValid = (TileSwapValid) message;
                if (gameController instanceof GameControllerPlayer) {
                    GameControllerPlayer controller = (GameControllerPlayer) gameController;
                    controller.tileSwapValid(tileSwapValid.isValidation());
                }
                break;
            case 413:
                TileSwapResponse tileSwapResponse = (TileSwapResponse) message;
                if (gameController instanceof GameControllerPlayer) {
                    GameControllerPlayer controller = (GameControllerPlayer) gameController;
                    controller.addTilesToHand(tileSwapResponse.getTiles());
                }
                break;
            case 415:
                MoveValid moveValid = (MoveValid) message;
                if (gameController instanceof GameControllerPlayer) {
                    GameControllerPlayer controller = (GameControllerPlayer) gameController;
                    controller.moveValid(moveValid.isValidation());
                }
                break;
            case 416:
                Update update = (Update) message;
                gameController.updateBoard((List<TileOnPosition>) update.getUpdates());
                gameController.updateBagCount(update.getNumberTilesInBag());
                break;
            case 418:
                ScoreResponse scoreResponse = (ScoreResponse) message;
                gameController.updateScore((HashMap<de.upb.swtpra1819interface.models.Client, Integer>) scoreResponse.getScores());
                break;
            case 420:
                TurnTimeLeftResponse turnTimeLeftResponse = (TurnTimeLeftResponse) message;
                gameController.turnTimeLeftResponse(turnTimeLeftResponse.getTime());
                break;
            case 422:
                TotalTimeResponse totalTimeResponse = (TotalTimeResponse) message;
                gameController.totalTimeResponse(totalTimeResponse.getTime());
                break;
            case 424:
                BagResponse bagResponse = (BagResponse) message;
                if (gameController instanceof GameControllerSpectator) {
                    GameControllerSpectator controller = (GameControllerSpectator) gameController;
                    controller.updateBag((ArrayList<Tile>) bagResponse.getBag());
                }

                break;
            case 426:
                PlayerHandsResponse playerHandsResponse = (PlayerHandsResponse) message;
                if (gameController instanceof GameControllerSpectator) {
                    GameControllerSpectator controller = (GameControllerSpectator) gameController;
                    controller.updatePlayerHands((HashMap<de.upb.swtpra1819interface.models.Client, ArrayList<Tile>>) playerHandsResponse.getHands());
                }
                break;
            case 499:
                GameDataResponse gameDataResponse = (GameDataResponse) message;
                gameController.updateBoard((List<TileOnPosition>) gameDataResponse.getBoard());
                gameController.setCurrentPlayer(gameDataResponse.getCurrentClient());
                gameController.updateStatusView();
                break;
            case 900:
                AccessDenied accessDenied = (AccessDenied) message;
                activeController.handleAccessDenied(accessDenied.getMessage());
                break;
            case 910:
                ParsingError parsingError = (ParsingError) message;
                activeController.handleParsingError(parsingError.getMessage());
                break;
            case 920:
                NotAllowed notAllowed = (NotAllowed) message;
                activeController.handleNotAllowed(notAllowed.getMessage());
                break;
        }
    }

    public void setActiveController(Controller controller) {
        activeController = controller;
    }

    public void setJoinedGame(Game joinedGame) {
        this.joinedGame = joinedGame;
    }

    public Game getJoinedGame() {
        return joinedGame;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public int getClientID() {
        return this.clientID;
    }

    /**
     * Disconnect from server.
     */
    public void disconnect() {
        client.disconnect();
        client = null;
    }

    public static Presenter getInstance() {
        return presenter;
    }

    public GameController getGameController() {
        return gameController;
    }
}

