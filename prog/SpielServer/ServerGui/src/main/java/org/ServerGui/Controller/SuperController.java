package org.ServerGui.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import de.upb.swtpra1819interface.messages.Message;

import org.CostumMessages.Shutdown;
import org.BasicCommunication.BasicCommunicationHandler;
import org.NetworkInterface.MessageWithClientId;

import org.ServerGui.Model.DataContainer;
import org.ServerGui.Model.GuiCommunication;
import org.ServerGui.Controller.SceneMapping;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * <p>
 * The <i>SuperController</i> is the general entry point of the client. It
 * delegates the control of every Scene. Moreover the <i>SuperController</i>
 * enables switching to other scenes.
 * </p>
 */
public class SuperController extends Application {

	public static final CountDownLatch latch = new CountDownLatch(1);
	public static SuperController controller = null;
	private BasicCommunicationHandler lobby;
	private NetworkController networkController = null;

	// Window
	public static Stage stage = null;

	// GuiData
	private GuiCommunication guiData = new GuiCommunication();
	// DataHandling
	private Thread networkControllerThread = null;
	private DataContainer data = new DataContainer();

	// Actual Scene
	private SceneMapping actualScene = SceneMapping.DEFAULT;

	/**
	 * 
	 * @return
	 */
	public static SuperController waitForSuperController() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return controller;
	}

	public SuperController() {
		controller = this;
		latch.countDown();
	}

	/**
	 * Fast mapping from Scenes of the enum to the loaded FXML Scenes
	 */
	private final Map<SceneMapping, DisplayedScene> guiScenes = new HashMap<SceneMapping, DisplayedScene>() {
		/**
		 * Serial Version UID for fixing warning(generated by eclipse)
		 */
		private static final long serialVersionUID = -1639062731972029042L;

		{
			put(SceneMapping.CREATE_GAME, new DisplayedScene("View/CreateGame.fxml", "Style/CreateGame.css", false,
					new CreateGameSceneController()));
			put(SceneMapping.ASSIGN_PLAYERS, new DisplayedScene("View/AssignPlayers.fxml", "Style/CreateGame.css",
					false, new AssignPlayersSceneController(data)));
			put(SceneMapping.SAVED_OR_NEW_GAME, new DisplayedScene("View/NewOrSavedGame.fxml", "Style/dialog.css",
					false, new NewOrSavedGameSceneController()));
			put(SceneMapping.LOBBY,
					new DisplayedScene("View/Lobby.fxml", "Style/ServerLobby.css", false, new LobbySceneController()));
			put(SceneMapping.CONFIG,
					new DisplayedScene("View/Config.fxml", "Style/config.css", false, new ConfigSceneController()));
			put(SceneMapping.SAVED_OR_NEW_TOURNAMENT, new DisplayedScene("View/NewOrSavedTournament.fxml",
					"Style/dialog.css", false, new NewOrSavedTournamentSceneController()));
			put(SceneMapping.CREATE_TOURNAMENT, new DisplayedScene("View/CreateTournament.fxml", "Style/CreateGame.css",
					false, new CreateTournamentSceneController()));
			put(SceneMapping.RANKING, new DisplayedScene("View/Ranking.fxml", "Style/CreateGame.css", false,
					new RankingSceneController()));
			put(SceneMapping.SELECT_WINNER, new DisplayedScene("View/SelectWinner.fxml", "Style/CreateGame.css", false,
					new SelectWinnerSceneController(data)));
			put(SceneMapping.ASSIGN_PLAYERS_TOURNAMENT, new DisplayedScene("View/AssignPlayersTournament.fxml",
					"Style/CreateGame.css", false, new AssignPlayersTournamentSceneController(data)));
		}
	};

	/**
	 * This method is called automatically by JavaFx.
	 * 
	 * @param primaryStage
	 */
	public void start(Stage primaryStage) {
		// Keep the reference
		stage = primaryStage;

		// Reference SuperController
		AbstractSceneController.setSuperController(this);
		// Reference GuiData
		// AbstractSceneController.setGuiData(guiData);
		// Load the first (default) Scene
		Scene scene = guiScenes.get(SceneMapping.LOBBY).getScene();
		stage.setScene(scene);
		stage.show();
		networkController = new NetworkController(this, guiData, data);
		networkControllerThread = new Thread(networkController);
		networkControllerThread.start();
		networkControllerThread.setName("Message_Controller_Gui");
	}

	/**
	 * Center the actual Scene relative to the Screen.
	 */
	public void centerScene() {
		Scene scene = stage.getScene();

		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

		stage.setX((screenBounds.getWidth() - scene.getWidth()) / 2);
		stage.setY((screenBounds.getHeight() - scene.getHeight()) / 2);
	}

	/**
	 * <p>
	 * Switch to a new Scene to be displayed.
	 * </p>
	 * 
	 * @param sm
	 *            Scene to be displayed
	 */
	public void displayScene(SceneMapping sm) {
		// Don't switch scenes if not needed
		if (actualScene != sm) {
			DisplayedScene nextScene = guiScenes.get(sm);
			Scene scene = nextScene.getScene();
			stage.setScene(scene);
			nextScene.getSceneController().onSceneSwitch(null);
			actualScene = sm;
		}
	}

	/**
	 *
	 * <p>
	 * Switch to a new Scene to be displayed.
	 * </p>
	 * 
	 * @param sm
	 *            Scene to be displayed
	 * @param stage
	 *            Associated Stage
	 */
	public void displayScene(SceneMapping sm, Stage stage) {
		// Don't switch scenes if not needed
		if (actualScene != sm) {
			Scene scene = guiScenes.get(sm).getScene();
			stage.setScene(scene);
			actualScene = sm;
		}
	}

	/**
	 * <p>
	 * Switch to a new Scene to be displayed.
	 * </p>
	 * 
	 * @param sm
	 *            Scene to be displayed
	 * @param message
	 *            Message that will be sent when the scene changes
	 */
	public <T> void displayScene(SceneMapping sm, T message) {
		// Don't switch scenes if not needed
		if (actualScene != sm) {
			// Get associated scene
			DisplayedScene nextScene = guiScenes.get(sm);
			Scene scene = nextScene.getScene();

			// Set up stage
			stage.setScene(scene);
			stage.setResizable(nextScene.isResizeable());

			// Notify next Controller
			nextScene.getSceneController().onSceneSwitch(message);
			actualScene = sm;
		}

	}

	/**
	 * <p>
	 * Switch to a new Scene to be displayed.
	 * </p>
	 * 
	 * @param sm
	 *            Scene to be displayed
	 * @param message
	 *            Message that will be sent when the scene changes
	 * @param stage
	 *            Associated Stage
	 */
	public <T> void displayScene(SceneMapping sm, T message, Stage stage) {
		// Don't switch scenes if not needed
		if (actualScene != sm) {
			// Get associated scene
			DisplayedScene nextScene = guiScenes.get(sm);
			Scene scene = nextScene.getScene();

			// Set up stage
			stage.setScene(scene);
			stage.setResizable(nextScene.isResizeable());

			// Notify next Controller
			nextScene.getSceneController().onSceneSwitch(message);
			actualScene = sm;

		}
	}

	/**
	 * Function resets actualScene to Lobby when CreateGame or CreateTournament are
	 * exited
	 */
	public void checkScene() {
		if (actualScene == SceneMapping.CREATE_GAME || actualScene == SceneMapping.CREATE_TOURNAMENT) {
			this.actualScene = SceneMapping.LOBBY;
		}

		if (actualScene == SceneMapping.RANKING) {
			RankingSceneController ctrl = (RankingSceneController) this.getGuiScenes().get(SceneMapping.RANKING)
					.getSceneController();
			ctrl.resetContent();
		}
	}

	/**
	 * Function resets actualScene to Lobby when CreateGame or
	 * AssignPlayersTournament are exited because games or tournaments were created
	 */
	public void resetScene() {
		this.actualScene = SceneMapping.LOBBY_DUMMY;
	}

	@Override
	public void stop() {
		sendMessage(new Shutdown());
		networkController.shutdown();
	}

	public GuiCommunication getGuiCommunication() {
		return guiData;
	}

	public DataContainer getData() {
		return data;
	}

	public void setLobby(BasicCommunicationHandler lobby) {
		this.lobby = lobby;
	}

	public static void setLobbySceneController(LobbySceneController lobbyCtrl) {
		AbstractSceneController.setLobbyCtrl(lobbyCtrl);
	}

	public void sendMessage(Message msg) {
		lobby.addToMessageQueue(new MessageWithClientId(msg, -1));
	}

	public Map<SceneMapping, DisplayedScene> getGuiScenes() {
		return guiScenes;
	}

	public static Stage getStage() {
		return stage;
	}

	public static void setStage(Stage stage) {
		SuperController.stage = stage;
	}

	public <T> void notifyScene(SceneMapping sm, T message) {
		if (actualScene != sm) {
			// Get associated scene
			DisplayedScene scene = guiScenes.get(sm);
			scene.getSceneController().reciveMessage(message);
		}
	}

}