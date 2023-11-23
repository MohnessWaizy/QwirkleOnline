package controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.App;
import model.GameData;

/**
 * <p>
 * The <i>SuperController</i> is the general entry point of the client. It
 * delegates the control of every Scene. Moreover the <i>SuperController</i>
 * enables switching to other scenes.
 * </p>
 */
public class SuperController extends Application {

	// Window
	private Stage stage = null;

	// Actual Scene
	private SceneMapping actualScene = SceneMapping.LOG_IN;
	// Mapping from Scenes of the enum to the loaded FXML Scenes

	private final Map<SceneMapping, DisplayedScene> guiScenes = new HashMap<SceneMapping, DisplayedScene>() {
		{
			put(SceneMapping.LOG_IN, new DisplayedScene("view/Login.fxml", Arrays.asList(
					new String[] { "style/application.css", "style/login.css", "style/buttonIcons.css" }),
					false, new LoginSceneController()));

			put(SceneMapping.LOBBY, new DisplayedScene("view/LobbyGui.fxml", Arrays.asList(
					new String[] { "style/application.css", "style/buttonIcons.css", "style/lobby.css" }),
					false, new LobbySceneController()));

			put(SceneMapping.CONFIG, new DisplayedScene("view/Config.fxml",
					Arrays.asList(new String[] { "style/config.css"}), false, new ConfigSceneController()));

			put(SceneMapping.FIELD, new DisplayedScene("view/Field.fxml", Arrays.asList(
					new String[] { "style/application.css", "style/buttonIcons.css", "style/field.css" }),
					true, new FieldSceneController(new GameData())));
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

		// Set Title and Application Icon
		stage.getIcons().add(new Image(App.class.getClassLoader().getResource("icons/cubes.png").toExternalForm()));
		stage.setTitle("Qwirkle");

		// Reference SuperController
		AbstractSceneController.setSuperController(this);

		// Load the first (default) Scene
		DisplayedScene nextScene = guiScenes.get(actualScene);
		Scene scene = nextScene.getScene();

		// Set up stage
		stage.setScene(scene);
		stage.setResizable(nextScene.isResizeable());
		stage.show();

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
				System.exit(0);
			}
		});
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
	 * @param sm Scene to be displayed
	 */
	public void displayScene(SceneMapping sm) {
		// Don't switch scenes if not needed
		if (actualScene != sm) {
			// Get associated scene
			DisplayedScene nextScene = guiScenes.get(sm);
			Scene scene = nextScene.getScene();

			// Set up stage
			stage.setScene(scene);
			stage.setResizable(nextScene.isResizeable());

			// Notify next Controller
			nextScene.getSceneController().onSceneSwitch();
			actualScene = sm;

		}
	}

	public <T> void notifyScene(SceneMapping sm, T message) {

		if (actualScene != sm) {
			// Get associated scene
			DisplayedScene scene = guiScenes.get(sm);
			scene.getSceneController().receiveMessage(message);
		}
	}

	/**
	 * Entry point of this Application
	 * 
	 * @param args
	 */
	public static void init(String[] args) {
		launch(args);
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Map<SceneMapping, DisplayedScene> getGuiScenes() {
		return guiScenes;
	}

}
