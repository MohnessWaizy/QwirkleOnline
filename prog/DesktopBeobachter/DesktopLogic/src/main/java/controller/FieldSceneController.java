package controller;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.NetworkInterface.NetClient;

import de.upb.swtpra1819interface.messages.BagRequest;
import de.upb.swtpra1819interface.messages.GameDataRequest;
import de.upb.swtpra1819interface.messages.GameDataResponse;
import de.upb.swtpra1819interface.messages.LeavingRequest;
import de.upb.swtpra1819interface.messages.MessageSend;
import de.upb.swtpra1819interface.messages.PlayerHandsRequest;
import de.upb.swtpra1819interface.messages.ScoreRequest;
import de.upb.swtpra1819interface.messages.TotalTimeRequest;
import de.upb.swtpra1819interface.messages.TurnTimeLeftRequest;
import de.upb.swtpra1819interface.messages.Winner;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import main.App;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import model.GameData;
import model.GameWrapper;
import model.ImageLoader;
import parser.FieldParser;

/**
 * controller for the scene field.fxml
 *
 */
public class FieldSceneController extends AbstractSceneController implements FieldSceneControllerInterface {

	@FXML
	TextArea textArea_chat;
	@FXML
	TextArea textArea_chatText;
	@FXML
	Button button_sendChat;
	@FXML
	Button button_zoomIn;
	@FXML
	Button button_center;
	@FXML
	Button button_zoomOut;
	@FXML
	Button button_exitGame;
	@FXML
	Button button_bagHand;
	@FXML
	Button button_unPin;
	@FXML
	Label label_gameID;
	@FXML
	Label label_turnTime;
	@FXML
	Label label_playerOnTurn;
	@FXML
	Label label_numberTilesInBag;
	@FXML
	Canvas canvas_board;
	@FXML
	Canvas canvas_pause;
	@FXML
	Label label_totalTime;
	@FXML
	VBox vBox_playerPanels;
	@FXML
	HBox hBox_bag;
	@FXML
	ScrollPane scrollPane_playerPanels;
	@FXML
	ScrollPane scrollPane_bagHand;
	@FXML
	AnchorPane anchorPane_field;
	@FXML
	Label label_gameState;

	private GameData game;
	private Client client;
	private NetClient netClient;
	private ArrayList<PlayerPanel> playerPanels;
	private HashMap<Integer, HBox> hBoxHands;
	private boolean bagSelected;
	private boolean playerPinned;
	private int pinnedPlayer;

	private int turn = 0;

	private GraphicsContext gc;
	private GraphicsContext gp;

	private double lastx = 0;
	private double lasty = 0;

	private double tileOffsetX = 0;
	private double tileOffsetY = 0;
	private double tempTileOffsetX = 0;
	private double tempTileOffsetY = 0;

	private int gridSize = 80;
	private String gridColor = "0xd6d6d6";

	private ImageLoader imgLoader;
	private BufferedImage[][] tileImages;
	private HashMap<Integer, Integer> playerTileMapping;

	private Timer turnTimeTimer;
	private TimerTask turnTimeTimerTask;
	private Timer totalTimeTimer;
	private TimerTask totalTimeTimerTask;
	private FieldParser fieldParser;

	private long turnTime;
	private long totalTime;
	private boolean turnTimeTimerRunning;
	private boolean totalTimeTimerRunning;
	private boolean mappingEnabled;

	public FieldSceneController(GameData game) {
		super();
		imgLoader = new ImageLoader("tileimages/texture.zip");
		tileImages = imgLoader.getImages();
		this.game = game;
		bagSelected = true;
		playerPanels = new ArrayList<PlayerPanel>();
		playerTileMapping = new HashMap<Integer, Integer>();
		hBoxHands = new HashMap<Integer, HBox>();
		pinnedPlayer = -1;
	}

	@Override
	public <T> void onSceneSwitch(T message) {
		if (message instanceof GameWrapper) {
			GameWrapper gameWrapper = (GameWrapper) message;
			game.setGame(gameWrapper.getGame());
			game.setPreviousPlayer(new Client(-1, null, null));
			client = gameWrapper.getClient();
			netClient = gameWrapper.getNetClient();
			label_gameID.setText(Integer.toString(gameWrapper.getGame().getGameId()));
			fieldParser = new FieldParser(this, netClient, 1);
			if (gameWrapper.getGame().getGameState() == GameState.IN_PROGRESS) {
				ScoreRequest scoreRequest = new ScoreRequest();
				this.netClient.sendMsg(scoreRequest);
				mappingEnabled = false;
			} else if (gameWrapper.getGame().getGameState() == GameState.PAUSED) {
				drawPauseCanvas();
				ScoreRequest scoreRequest = new ScoreRequest();
				this.netClient.sendMsg(scoreRequest);
				mappingEnabled = false;
			} else {
				if (gameWrapper.getGame().getGameState() == GameState.ENDED) {
				}
				GameDataRequest gameDataRequest = new GameDataRequest();
				this.netClient.sendMsg(gameDataRequest);
			}
			redrawCanvas();
		} else {
			switchScene(SceneMapping.LOBBY);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		gc = canvas_board.getGraphicsContext2D();
		gp = canvas_pause.getGraphicsContext2D();
		gc.translate(canvas_board.getWidth() / 2, canvas_board.getHeight() / 2);

		anchorPane_field.setOnMousePressed(action -> {
			this.lastx = action.getSceneX();
			this.lasty = action.getSceneY();
		});

		anchorPane_field.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				canvas_pause.setHeight((double) newValue);

				gc.translate(0, -canvas_board.getHeight() / 2);
				canvas_board.setHeight((double) newValue);
				gc.translate(0, canvas_board.getHeight() / 2);

				centerField();
				if (game.getGameState() == GameState.PAUSED) {
					clearPauseCanvas();
					drawPauseCanvas();
				}
			}
		});
		anchorPane_field.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				gc.translate(-canvas_board.getWidth() / 2, 0);
				canvas_pause.setWidth((double) newValue);
				canvas_board.setWidth((double) newValue);
				gc.translate(canvas_board.getWidth() / 2, 0);
				centerField();
				if (game.getGameState() == GameState.PAUSED) {
					clearPauseCanvas();
					drawPauseCanvas();
				}
			}
		});

		anchorPane_field.setOnMouseReleased(action -> {
			tileOffsetX += tempTileOffsetX;
			tileOffsetY += tempTileOffsetY;
			tempTileOffsetX = 0;
			tempTileOffsetY = 0;
		});

		anchorPane_field.setOnMouseDragged(action -> {
			tempTileOffsetX = this.lastx - action.getSceneX();
			tempTileOffsetY = this.lasty - action.getSceneY();
			redrawCanvas();
		});

		button_sendChat.setOnAction(action -> {
			if (textArea_chatText.getText().length() != 0) {
				netClient.sendMsg(new MessageSend(textArea_chatText.getText()));
				textArea_chatText.setText("");
			}
		});

		button_center.setOnAction(action -> {
			centerField();
		});

		button_unPin.setOnAction(action -> {
			playerPinned = false;
			updateCurrentPlayer();
			button_unPin.setDisable(true);
			button_unPin.setVisible(false);
			scrollPane_bagHand.setContent(hBoxHands.get(game.getCurrentPlayer().getClientId()));
			for (PlayerPanel panel : playerPanels) {
				if (panel.getPlayerID() == pinnedPlayer) {
					if (panel.getPlayerID() == game.getCurrentPlayer().getClientId()) {
						panel.setStyle("-fx-background-color: #6290c3;");
					} else {
						panel.setStyle("-fx-background-color: grey;");
					}
				}
			}
			pinnedPlayer = -1;
		});

		button_zoomIn.setOnAction(action -> {
			if (gridSize < 200) {
				resize(10);
			}
		});

		button_zoomOut.setOnAction(action -> {
			if (gridSize > 10) {
				resize(-10);
			}
		});

		button_exitGame.setOnAction(action -> {
			showExitDialog();
		});

		button_bagHand.setOnMouseReleased(action -> {
			this.switchBagHand();
		});

	}

	/**
	 * Shows the Dialog Alert window to confirm the quit game action
	 */
	private void showExitDialog() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setGraphic(null);
		alert.setTitle("Spiel beenden");
		alert.setHeaderText("Spiel beenden");
		alert.setContentText("Sie sind dabei das Spiel zu verlassen.");

		ButtonType buttonTypeOne = new ButtonType("Spiel weiterlaufen lassen");
		ButtonType buttonTypeTwo = new ButtonType("Spiel beenden");

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(App.class.getClassLoader().getResource("icons/cubes.png").toExternalForm()));

		DialogPane pane = alert.getDialogPane();
		pane.getStylesheets().add(getClass().getClassLoader().getResource("style/alert.css").toExternalForm());
		pane.getStyleClass().add("dialog");

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Optional<ButtonType> option = alert.showAndWait();

				if (option.get() == buttonTypeTwo) {
					exit();
				} else if (option.get() == buttonTypeOne) {

				}

			}
		});
	}

	/**
	 * Updates the chat
	 * 
	 * @param client
	 * @param message
	 */

	public void updateChat(Client client, String message) {
		textArea_chat.setText(textArea_chat.getText() + "\n" + client.getClientName() + ": " + message);
		textArea_chat.setScrollTop(Double.MAX_VALUE);
	}

	/**
	 * Resets the scene for the next game
	 */
	public void resetScene() {
		fieldParser.stopParser();
		game = new GameData();
		client = new Client(-1, null, null);
		playerPanels = new ArrayList<PlayerPanel>();
		bagSelected = true;
		gridSize = 80;
		playerTileMapping = new HashMap<Integer, Integer>();
		if (totalTimeTimerRunning) {
			totalTimeTimer.cancel();
		}
		if (turnTimeTimerRunning) {
			turnTimeTimer.cancel();
		}
		totalTimeTimer = null;
		totalTimeTimerTask = null;
		totalTime = 0;
		netClient = null;
		hBoxHands = new HashMap<Integer, HBox>();
		playerPinned = false;
		pinnedPlayer = -1;
		playerTileMapping = new HashMap<Integer, Integer>();
		clearFieldCanvas();
		clearPauseCanvas();
		turnTimeTimerRunning = false;
		totalTimeTimerRunning = false;
		textArea_chat.setText("");
		textArea_chatText.setText("");
		if (!bagSelected) {
			switchBagHand();
		}
		button_unPin.setVisible(false);
		button_unPin.setDisable(true);
		label_gameID.setText("");
		label_turnTime.setText("Zeit für aktuellen Zug: ");
		label_playerOnTurn.setText("Spieler am Zug: ");
		label_totalTime.setText("Spieldauer: ");
		vBox_playerPanels.getChildren().clear();
		hBox_bag.getChildren().clear();
		scrollPane_bagHand.setContent(hBox_bag);
		label_gameState.setText("Spielstatus: ");
	}

	/**
	 * Updates the Mapping for the Player-Tile Mapping on Update Response
	 * 
	 * @param tiles Liste of last layed tiles
	 * @param id    Player Id of the current player
	 */
	private void updateMapping(List<TileOnPosition> tiles, int id) {
		if (mappingEnabled) {
			for (TileOnPosition tile : tiles) {
				this.playerTileMapping.put(tile.getTile().getUniqueId(), id);
			}
		}
	}

	/**
	 * Mapps the layed tiles to the given PlayerId and marks them on the field
	 * 
	 * @param id Id of the player which tiles should be marked
	 */
	private void mapPlayerTiles(int id) {
		if (mappingEnabled && !game.getBoard().isEmpty() && !playerTileMapping.isEmpty()) {
			for (TileOnPosition tile : game.getBoard()) {
				if (this.playerTileMapping.get(tile.getTile().getUniqueId()) == id) {
					markTile(tile.getCoordX(), tile.getCoordY());
				}
			}
		}
	}

	/**
	 * Redraws the Field
	 * 
	 */
	private void redrawCanvas() {
		resetCanvas();
		drawTiles();

		if (game != null && game.getCurrentPlayer() != null) {
			if (!playerPinned) {
				mapPlayerTiles(game.getCurrentPlayer().getClientId());
			} else {
				mapPlayerTiles(pinnedPlayer);
			}
		}

	}

	/**
	 * Draws the tiles on the Field
	 * 
	 */
	private void drawTiles() {
		if (game.getBoard() != null) {
			for (TileOnPosition tile : game.getBoard()) {
				int x = tile.getCoordX();
				int y = tile.getCoordY();
				int colorId = tile.getTile().getColor();
				int shapeId = tile.getTile().getShape();
				layoutTile(x, y, colorId, shapeId);
			}
		}

	}

	/**
	 * Zooms the board with given zoom
	 * 
	 * @param zoom The zoom
	 */
	private void resize(int zoom) {

		gridSize += zoom;

		redrawCanvas();
	}

	/**
	 * Center the position of the zoom
	 */
	private void centerField() {
		tileOffsetX = 0;
		tileOffsetY = 0;
		redrawCanvas();

	}

	/**
	 * Draws the grid on the Field
	 * 
	 * @param color The color in which the grid should be drawn
	 */
	private void drawGrid(String color) {

		gc.setStroke(Paint.valueOf(color));

		double offsetX = tileOffsetX + tempTileOffsetX;
		double offsetY = tileOffsetY + tempTileOffsetY;

		offsetX = offsetX % gridSize;
		offsetY = offsetY % gridSize;

		double anzahlLinesX = canvas_board.getWidth() / 2 - gridSize / 2;
		double anzahlLinesY = canvas_board.getHeight() / 2 - gridSize / 2;

		anzahlLinesX = anzahlLinesX / gridSize;
		anzahlLinesY = anzahlLinesY / gridSize;

		for (int i = 0; i < anzahlLinesX + 1; i++) {
			gc.strokeLine((i * gridSize) + gridSize / 2 - offsetX, -canvas_board.getHeight() / 2,
					(i * gridSize) + gridSize / 2 - offsetX, canvas_board.getHeight() / 2);
			gc.strokeLine(-(i * gridSize) - gridSize / 2 - offsetX, -canvas_board.getHeight() / 2,
					-(i * gridSize) - gridSize / 2 - offsetX, canvas_board.getHeight() / 2);
		}
		for (int i = 0; i < anzahlLinesY + 1; i++) {
			gc.strokeLine(-canvas_board.getWidth() / 2, (i * gridSize) + gridSize / 2 - offsetY,
					canvas_board.getWidth() / 2, (i * gridSize) + gridSize / 2 - offsetY);
			gc.strokeLine(-canvas_board.getWidth() / 2, -(i * gridSize) - gridSize / 2 - offsetY,
					canvas_board.getWidth() / 2, -(i * gridSize) - gridSize / 2 - offsetY);
		}

	}

	/**
	 * Draws pause Screen
	 * 
	 */
	private void drawPauseCanvas() {

		double canvasTeiler = canvas_pause.getWidth() / 8;

		gp.fillRect((3 * canvasTeiler) - canvasTeiler / 2, canvas_pause.getHeight() / 4, canvasTeiler,
				canvas_pause.getHeight() / 2);
		gp.fillRect((5 * canvasTeiler) - canvasTeiler / 2, canvas_pause.getHeight() / 4, canvasTeiler,
				canvas_pause.getHeight() / 2);
		gp.setFill(Color.rgb(0, 0, 0, 0.5));
		gp.fillRect(0, 0, canvas_pause.getWidth(), canvas_pause.getHeight());
		gp.setFill(Color.BLACK);
	}

	/**
	 * Clears Pause Screen
	 *
	 */
	private void clearPauseCanvas() {
		gp.clearRect(0, 0, canvas_pause.getWidth(), canvas_pause.getHeight());
	}

	/**
	 * Clears the field canvas
	 */
	private void clearFieldCanvas() {
		gc.translate(-canvas_board.getWidth() / 2, -canvas_board.getHeight() / 2);
		gc.clearRect(0, 0, canvas_board.getWidth(), canvas_board.getHeight());
		gc.translate(canvas_board.getWidth() / 2, canvas_board.getHeight() / 2);
	}

	/**
	 * Resets the board canvas
	 */
	private void resetCanvas() {
		gc.translate(-canvas_board.getWidth() / 2, -canvas_board.getHeight() / 2);
		gc.clearRect(0, 0, canvas_board.getWidth(), canvas_board.getHeight());
		gc.translate(canvas_board.getWidth() / 2, canvas_board.getHeight() / 2);
		drawGrid(gridColor);

	}

	/**
	 * Shows the current player and if no other player is pinned, also his hand
	 */

	public void updateCurrentPlayer(Client client) {
		if (game.getCurrentPlayer() != null) {
			game.setPreviousPlayer(game.getCurrentPlayer());
		}
		game.setCurrentPlayer(client);
		updateCurrentPlayer();
	}

	public void updateCurrentPlayer() {
		netClient.sendMsg(new PlayerHandsRequest());
		netClient.sendMsg(new BagRequest());
		netClient.sendMsg(new ScoreRequest());

		// If the previous player is not pinned, dye him grey again
		Client previousPlayer = game.getPreviousPlayer();
		if (previousPlayer.getClientId() >= 0) {
			for (PlayerPanel playerPanel : playerPanels) {
				if (playerPanel.getPlayerID() == previousPlayer.getClientId()) {
					if (pinnedPlayer != playerPanel.getPlayerID()) {
						playerPanel.setStyle("-fx-background-color: grey;");
					}
				}
			}
		}
		// If the new current player is not pinned, dye him blue
		Client currentPlayer = game.getCurrentPlayer();
		for (PlayerPanel panel : playerPanels) {
			if (panel.getPlayerID() == currentPlayer.getClientId()) {
				if (pinnedPlayer != panel.getPlayerID()) {
					panel.setStyle("-fx-background-color: #6290c3;");
				}
			}
		}
		label_playerOnTurn.setText("Spieler am Zug: " + currentPlayer.getClientName());
		updateTurnTime(game.getConfig().getTurnTime());
		updatePlayerHand(currentPlayer);
		if (!bagSelected && !playerPinned) {
			scrollPane_bagHand.setContent(hBoxHands.get(currentPlayer.getClientId()));
		}
	}

	/**
	 * Generates the PlayerPanels
	 */

	private void generatePlayerPanels() {
		for (Client clientInThisGame : game.getClients()) {
			final PlayerPanel playerPanel = new PlayerPanel(clientInThisGame.getClientId(),
					clientInThisGame.getClientName());
			HBox hBox = new HBox();
			hBoxHands.put(clientInThisGame.getClientId(), hBox);
			playerPanel.setOnMouseClicked(action -> {
				playerPinned = true;
				if (bagSelected) {
					switchBagHand();
				}
				playerPinned = true;
				if (pinnedPlayer >= 0) {
					for (PlayerPanel panel : playerPanels) {
						if (panel.getPlayerID() == pinnedPlayer) {
							if (panel.getPlayerID() == game.getCurrentPlayer().getClientId()) {
								panel.setStyle("-fx-background-color: #6290c3;");
							} else {
								panel.setStyle("-fx-background-color: grey;");
							}
						}
					}
				}
				pinnedPlayer = clientInThisGame.getClientId();
				button_unPin.setDisable(false);
				button_unPin.setVisible(true);
				updatePlayerHand(clientInThisGame);
				scrollPane_bagHand.setContent(hBox);
				playerPinned = true;
				redrawCanvas();
				playerPanel.setStyle("-fx-background-color: #2d1e2f;");
			});
			vBox_playerPanels.getChildren().add(playerPanel);
			playerPanels.add(playerPanel);
		}
	}

	/**
	 * Exits the game
	 */

	private void exit() {
		netClient.sendMsg(new LeavingRequest());
		resetScene();
		switchScene(SceneMapping.LOBBY, true);
	}

	private void layoutTile(int x, int y, int colorId, int shapeId) {

		Image img = SwingFXUtils.toFXImage(this.tileImages[colorId][shapeId], null);
		gc.drawImage(img, x * (gridSize) - gridSize / 2 + 1 - tileOffsetX - tempTileOffsetX,
				y * (gridSize) - gridSize / 2 + 1 - tileOffsetY - tempTileOffsetY, this.gridSize - 1,
				this.gridSize - 1);

	}

	/**
	 * Draws a border to a Tile, that lays on the position (x/y)
	 * 
	 * @param x X Position of the tile
	 * @param y Y Position of the tile
	 */
	private void markTile(int x, int y) {

		gc.setStroke(Paint.valueOf("0x000000"));
		gc.setLineWidth(3);
		gc.strokeRect(x * (gridSize) - gridSize / 2 + 1 - tileOffsetX - tempTileOffsetX,
				y * (gridSize) - gridSize / 2 + 1 - tileOffsetY - tempTileOffsetY, this.gridSize - 1,
				this.gridSize - 1);
		gc.setLineWidth(1);
	}

	/**
	 * Switches between hands of player and bag in scrollPane_bagHand
	 */

	private void switchBagHand() {
		if (this.bagSelected) {
			// Change the button's icon
			button_bagHand.getStyleClass().remove(1);
			button_bagHand.getStyleClass().add("button_bagIcon");
			label_numberTilesInBag.setVisible(false);

			bagSelected = false;
			scrollPane_bagHand.setContent(hBoxHands.get(game.getCurrentPlayer().getClientId()));
		} else {
			// Change the button's icon
			button_bagHand.getStyleClass().remove(1);
			button_bagHand.getStyleClass().add("button_handIcon");

			bagSelected = true;
			label_numberTilesInBag.setVisible(true);
			label_numberTilesInBag.setText("Steine: " + Integer.toString(game.getBag().getNumberOfTiles()));
			scrollPane_bagHand.setContent(hBox_bag);
			// Dye the pinned player...
			for (PlayerPanel panel : playerPanels) {
				if (panel.getPlayerID() == pinnedPlayer) {
					if (pinnedPlayer == game.getCurrentPlayer().getClientId()) {
						// ... blue, if he is the current player
						panel.setStyle("-fx-background-color: #6290c3;");
					} else {
						// ... grey, if he is not
						panel.setStyle("-fx-background-color: grey;");
					}
				}
			}
			playerPinned = false;
			pinnedPlayer = -1;
			button_unPin.setDisable(true);
			button_unPin.setVisible(false);
		}
	}

	/**
	 * Deletes a player from the panels and the clients list
	 * 
	 * @param client
	 */

	public void playerLeft(Client client) {
		ArrayList<Client> clients = game.getClients();
		// Check, if you got kicked
		if (client.getClientId() == this.client.getClientId()) {
			exit();
		} else {
			PlayerPanel panelForRemove = null;
			for (PlayerPanel panel : playerPanels) {
				if (panel.getPlayerID() == client.getClientId()) {

					panelForRemove = panel;
					Client clientForRemove = null;
					for (Client clientInClients : clients) {
						if (clientInClients.getClientId() == client.getClientId()) {
							clientForRemove = clientInClients;
						}
					}
					clients.remove(clientForRemove);
				}
			}
			vBox_playerPanels.getChildren().remove(panelForRemove);
			playerPanels.remove(panelForRemove);
		}
		game.setClients(clients);
	}

	/**
	 * Updates the turn time
	 * 
	 * @param time
	 */

	public void updateTurnTime(long time) {
		if (turnTimeTimerRunning) {
			turnTimeTimerTask.cancel();
		}
		turnTime = time / 1000;
		turnTimeTimer = new Timer();
		turnTimeTimerRunning = true;
		turnTimeTimerTask = new TimerTask() {
			public void run() {
				if (turnTime == 0) {
					turnTimeTimerTask.cancel();
					turnTimeTimerRunning = false;
				}
				final long hours = turnTime / 3600;
				long turnTimeSave = turnTime - hours * 3600;
				final long minutes = turnTimeSave / 60;
				turnTimeSave = turnTimeSave - minutes * 60;
				final long seconds = turnTimeSave;

				Platform.runLater(() -> {
					label_turnTime.setText(("Zeit für aktuellen Zug: " + Long.toString(hours) + ":"
							+ Long.toString(minutes) + ":" + Long.toString(seconds)));
				});
				if (game.getGameState() != GameState.IN_PROGRESS) {
					turnTimeTimer.cancel();
					turnTimeTimerRunning = false;
				} else {
					turnTime--;
				}
			}
		};
		turnTimeTimer.schedule(turnTimeTimerTask, 0, 1000);
	}

	/**
	 * Updates the total time
	 * 
	 * @param time
	 */

	public void updateTotalTime(long time) {
		game.getGameState();
		if (totalTimeTimerRunning) {
			totalTimeTimerTask.cancel();
		}
		totalTime = time / 1000;
		totalTimeTimer = new Timer();
		totalTimeTimerRunning = true;
		totalTimeTimerTask = new TimerTask() {
			public void run() {
				final long hours = totalTime / 3600;
				long totalTimeSave = totalTime - hours * 3600;
				final long minutes = totalTimeSave / 60;
				totalTimeSave = totalTimeSave - minutes * 60;
				final long seconds = totalTimeSave;

				Platform.runLater(() -> {
					label_totalTime.setText(("Spieldauer: " + Long.toString(hours) + ":" + Long.toString(minutes) + ":"
							+ Long.toString(seconds)));
				});
				if (game.getGameState() != GameState.IN_PROGRESS) {
					totalTimeTimer.cancel();
					totalTimeTimerRunning = false;
				} else {
					totalTime++;
				}
			}
		};
		totalTimeTimer.schedule(totalTimeTimerTask, 0, 1000);
	}

	/**
	 * Updates the tiles in the bag's HBox
	 * 
	 * @param collection
	 */

	public void updateBag(Collection<Tile> bag) {
		game.getBag().setTiles((List<Tile>) bag);
		this.updateNumberTilesInBag(bag.size());
		hBox_bag.getChildren().clear();
		ImageView imageViewTile;
		Image imageTile;
		for (Tile tile : bag) {
			imageViewTile = new ImageView();
			imageViewTile.setFitHeight(100);
			imageViewTile.setFitWidth(100);
			imageTile = SwingFXUtils.toFXImage(this.tileImages[tile.getColor()][tile.getShape()], null);
			imageViewTile.setImage(imageTile);
			hBox_bag.getChildren().add(imageViewTile);
			hBox_bag.setMargin(imageViewTile, new Insets(10, 10, 10, 10));
		}
	}

	/**
	 * Shows the winner
	 */

	public void showWinner(Winner winner) {
		game.setWinner(winner);
		if (this.game.getGameState() != GameState.ENDED) {
			this.updateScores(winner.getLeaderboard());
			netClient.sendMsg(new PlayerHandsRequest());
			netClient.sendMsg(new BagRequest());
		}
		label_gameState.setText(label_gameState.getText() + " (Sieger: " + winner.getClient().getClientName() + " mit "
				+ winner.getScore() + " Punkten)");
	}

	/**
	 * Update tiles on the field
	 * 
	 * @param tileOnPositionList
	 */

	public void updateTilesOnField(List<TileOnPosition> tileOnPositionList) {
		game.addBoardTiles(tileOnPositionList);
		this.updateMapping(tileOnPositionList, game.getCurrentPlayer().getClientId());
		redrawCanvas();
	}

	/**
	 * Update update number tiles in the bag
	 * 
	 * @param numberTilesInBag
	 */

	public void updateNumberTilesInBag(int numberTilesInBag) {
		game.getBag().setNumberOfTiles(numberTilesInBag);
		label_numberTilesInBag.setText("Steine: " + Integer.toString(numberTilesInBag));
		;
	}

	/**
	 * Update scores of all players
	 */

	public void updateScores(Map<Client, Integer> scores) {
		if (playerPanels == null || playerPanels.isEmpty()) {
			this.game.setClients(new ArrayList<>(scores.keySet()));
			GameDataRequest gameDataRequest = new GameDataRequest();
			this.netClient.sendMsg(gameDataRequest);
			this.generatePlayerPanels();
			if (this.game.getGameState() == GameState.ENDED) {
				this.updateCurrentPlayer();
			}
		} else {
			game.setScores((HashMap<Client, Integer>) scores);
			for (Entry<Client, Integer> entry : scores.entrySet()) {
				for (PlayerPanel panel : playerPanels) {
					if (panel.getPlayerID() == entry.getKey().getClientId()) {
						panel.updatePlayerScore(entry.getValue());
					}
				}
			}
		}
	}

	/**
	 * Updates the tiles in the player's HBox
	 * 
	 * @param client
	 */

	public void updatePlayerHand(Map<Client, ArrayList<Tile>> hands) {
		game.setHands((HashMap<Client, ArrayList<Tile>>) hands);
		updatePlayerHand(game.getCurrentPlayer());
	}

	public void updatePlayerHand(Client client) {
		ArrayList<Tile> tilesOnHand = game.getHands().get(client.getClientId());
		ImageView imageViewTile;
		Image imageTile;
		HBox hBox = hBoxHands.get(client.getClientId());
		if (hBox != null) {
			hBox.getChildren().clear();
			if (tilesOnHand != null) {
				for (Tile tile : tilesOnHand) {
					imageViewTile = new ImageView();
					imageViewTile.setFitHeight(100);
					imageViewTile.setFitWidth(100);
					imageTile = SwingFXUtils.toFXImage(this.tileImages[tile.getColor()][tile.getShape()], null);
					imageViewTile.setImage(imageTile);
					hBox.getChildren().add(imageViewTile);
					hBox.setMargin(imageViewTile, new Insets(10, 10, 10, 10));
				}
			}
		}

	}

	/* (non-Javadoc)
	 * @see controller.FieldSceneControllerInterface#updateGameData(de.upb.swtpra1819interface.messages.GameDataResponse)
	 */
	public void updateGameData(GameDataResponse gameData) {
		game.importGameData(gameData);
		switch (gameData.getGameState()) {
		case PAUSED:
			label_gameState.setText("Spielstatus: Das Spiel wurde pausiert");
			this.generatePlayerPanels();
			updateCurrentPlayer(gameData.getCurrentClient());
			netClient.sendMsg(new BagRequest());
			netClient.sendMsg(new ScoreRequest());
			netClient.sendMsg(new PlayerHandsRequest());
			netClient.sendMsg(new TotalTimeRequest());
			netClient.sendMsg(new TurnTimeLeftRequest());
			break;
		case IN_PROGRESS:
			label_gameState.setText("Spielstatus: Das Spiel läuft");
			updateCurrentPlayer(gameData.getCurrentClient());
			netClient.sendMsg(new BagRequest());
			netClient.sendMsg(new ScoreRequest());
			netClient.sendMsg(new PlayerHandsRequest());
			netClient.sendMsg(new TotalTimeRequest());
			netClient.sendMsg(new TurnTimeLeftRequest());
			break;
		case NOT_STARTED:
			label_gameState.setText("Spielstatus: Das Spiel wurde noch nicht gestartet");
			break;
		case ENDED:
			label_gameState.setText("Spielstatus: Das Spiel ist beendet");
			netClient.sendMsg(new BagRequest());
			netClient.sendMsg(new ScoreRequest());
			netClient.sendMsg(new PlayerHandsRequest());
			netClient.sendMsg(new TotalTimeRequest());
			netClient.sendMsg(new TurnTimeLeftRequest());
			clearPauseCanvas();
			break;
		}
		redrawCanvas();
	}

	/**
	 * Pauses the game
	 */

	public void pauseGame() {
		game.setGameState(GameState.PAUSED);
		drawPauseCanvas();
		turnTimeTimer.cancel();
		totalTimeTimer.cancel();
		turnTimeTimerRunning = false;
		totalTimeTimerRunning = false;
		label_gameState.setText("Spielstatus: Das Spiel wurde pausiert");
	}

	/**
	 * Resumes the game
	 */

	public void resumeGame() {
		game.setGameState(GameState.IN_PROGRESS);
		clearPauseCanvas();
		updateTotalTime(totalTime * 1000);
		updateTurnTime(turnTime * 1000);
		label_gameState.setText("Spielstatus: Das Spiel läuft");
	}

	/**
	 * Starts the game
	 * 
	 * @param configuration
	 * @param collection
	 */

	public void startGame(Collection<Client> collection, Configuration configuration) {
		game.setClients((ArrayList<Client>) collection);
		game.setConfig(configuration);
		game.setGameState(GameState.IN_PROGRESS);
		generatePlayerPanels();
		updateTotalTime(0);
		label_gameState.setText("Spielstatus: Das Spiel läuft");
	}

	/* (non-Javadoc)
	 * @see controller.FieldSceneControllerInterface#abortGame()
	 */
	public void abortGame() {
		game.setGameState(GameState.ENDED);
		clearPauseCanvas();
		turnTimeTimer.cancel();
		totalTimeTimer.cancel();
		label_gameState.setText("Spielstatus: Das Spiel wurde abgebrochen");
		fieldParser.stopParser();
	}

	/**
	 * Ends the game
	 */

	public void endGame() {
		game.setGameState(GameState.ENDED);
		clearPauseCanvas();
		turnTimeTimer.cancel();
		totalTimeTimer.cancel();
		label_gameState.setText("Spielstatus: Das Spiel ist beendet");
		fieldParser.stopParser();
	}
}
