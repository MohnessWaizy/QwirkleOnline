package org.ServerGui.Controller;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * <p>
 * Every Scene created needs its own Controller for its GUI. This class
 * implements <i>{@linkplain Initializable}</i> to initialize its Controller.
 * For a new SceneController, one simply needs to extend this class and
 * implement all needed method(given by the <i>{@linkplain Initializable}</i>
 * Interface).
 * </p>
 */
public abstract class AbstractSceneController implements Initializable {

	// Reference to SuperController to be able to switch Scenes
	protected static SuperController supCtrl;

	protected static LobbySceneController lobbyCtrl;

	private Stage stage = null;

	/**
	 * <p>
	 * {@linkplain application.SuperController#displayScene}
	 * </p>
	 * 
	 * @param sm
	 *            Scene to be displayed
	 */
	public final void switchScene(SceneMapping sm) {
		supCtrl.displayScene(sm);
	}

	/**
	 * Function switches scene via displayScene on chosen stage
	 * 
	 * <p>
	 * {@linkplain application.SuperController#displayScene}
	 * </p>
	 * 
	 * @param sm
	 *            scene to be displayed
	 * @param stage
	 *            stage to be switched on
	 */
	public final void switchScene(SceneMapping sm, Stage stage) {
		supCtrl.displayScene(sm, stage);
	}

	/**
	 * Function switches scene via displayScene and transmits message
	 * 
	 * <p>
	 * {@linkplain application.SuperController#displayScene}
	 * </p>
	 * 
	 * @param sm
	 *            scene to be displayed
	 * @param message
	 *            message to be transmitted to new scene
	 */
	public final <T> void switchScene(SceneMapping sm, T message) {
		supCtrl.displayScene(sm, message);
	}

	/**
	 * Function switches scene via displayScene on chosen stage and transmits
	 * message
	 * 
	 * <p>
	 * {@linkplain application.SuperController#displayScene}
	 * </p>
	 * 
	 * @param sm
	 *            scene to be displayed
	 * @param message
	 *            message to be transmitted to new scene
	 * @param stage
	 *            stage to be switched on
	 */
	public final <T> void switchScene(SceneMapping sm, T message, Stage stage) {
		supCtrl.displayScene(sm, message, stage);
	}

	/**
	 * Function processes message transmitte
	 * 
	 * <p>
	 * {@linkplain application.SuperController#displayScene}
	 * </p>
	 * 
	 * @param sm
	 *            scene to be displayed
	 * @param message
	 *            message to be transmitted to new scene
	 * @param stage
	 *            stage to be switched on
	 */
	public <T> void onSceneSwitch(T message) {

	}

	/**
	 * Function opens new Stage and sets given scene
	 * 
	 * @param scene
	 *            scene to be displayed
	 */
	protected void addSubStage(SceneMapping scene) {
		Stage subStage = new Stage();
		subStage.setScene(supCtrl.getGuiScenes().get(scene).getScene());
		subStage.initOwner(SuperController.getStage().getOwner());
		subStage.initModality(Modality.APPLICATION_MODAL);
		subStage.show();
		subStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				checkScene();
			}
		});
	}

	/**
	 * <p>
	 * {@linkplain application.SuperController#notifyScene}
	 * </p>
	 * 
	 * @param sm
	 *            scene to be notified
	 * @param message
	 *            message to be transmitted
	 */
	public final <T> void notifySceneMessage(SceneMapping sm, T message) {
		supCtrl.notifyScene(sm, message);
	}

	/**
	 * Function processes message transmitted
	 * 
	 * @param message
	 *            message to be processed
	 */
	public <T> void reciveMessage(T message) {

	}

	/**
	 * Function processes message transmitte
	 * 
	 * <p>
	 * {@linkplain application.SuperController#checkScene}
	 * </p>
	 * 
	 */
	public void checkScene() {
		this.supCtrl.checkScene();
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public static void setSuperController(SuperController superController) {
		supCtrl = superController;
	}

	public static LobbySceneController getLobbyCtrl() {
		return lobbyCtrl;
	}

	public static void setLobbyCtrl(LobbySceneController lobbyCtrl) {
		AbstractSceneController.lobbyCtrl = lobbyCtrl;
	}

	public static SuperController getSupCtrl() {
		return supCtrl;
	}
}
