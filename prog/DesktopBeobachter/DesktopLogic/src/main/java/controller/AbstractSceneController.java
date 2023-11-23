package controller;

import javafx.fxml.Initializable;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
	private static SuperController supCtrl;

	protected void addSubStage(SceneMapping scene) {
		Stage subStage = new Stage();
		subStage.setScene(supCtrl.getGuiScenes().get(scene).getScene());
		subStage.initOwner(supCtrl.getStage().getOwner());
		subStage.initModality(Modality.APPLICATION_MODAL);
		subStage.show();
	}

	public AbstractSceneController() {

	}

	/**
	 * <p>
	 * {@linkplain application.SuperController#displayScene}
	 * </p>
	 * 
	 * @param    <T>
	 * 
	 * @param sm Scene to be displayed
	 */

	/**
	 * After a scene is successfully switched, the new scene will be notified.
	 */
	public void onSceneSwitch() {

	}

	/**
	 * After a scene is successfully switched, the new scene will be notified and
	 * will receive a message from the caller of switchScene.
	 * 
	 * @param message Message the next scene will receive
	 */

	public <T> void onSceneSwitch(T message) {

	}

	public final <T> void notifySceneMessage(SceneMapping sm, T message) {
		supCtrl.notifyScene(sm, message);
	}

	public <T> void receiveMessage(T message) {

	}

	public final void switchScene(SceneMapping sm) {
		supCtrl.displayScene(sm);
	}

	public final <T> void switchScene(SceneMapping sm, T message) {
		supCtrl.displayScene(sm, message);
	}

	public static void setSuperController(SuperController superController) {
		supCtrl = superController;
	}

}
