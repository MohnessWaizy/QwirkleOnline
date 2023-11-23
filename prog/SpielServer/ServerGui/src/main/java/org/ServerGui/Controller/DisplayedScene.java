package org.ServerGui.Controller;

import org.ServerGui.App;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * Container class to reference a <i>{@linkplain Scene}</i> to its
 * <i>{@linkplain AbstractSceneController}</i>. The Scene is generated from a
 * FXML file.
 */

public class DisplayedScene {

	private Scene scene;
	private AbstractSceneController sc;
	private boolean resizeable;

	/**
	 * Constructs a DisplayedScene container that will load your FXML file and sets
	 * the Gui Controller.
	 * 
	 * @param file            FXML file
	 * @param sceneController Custom Gui Controller for the FXML file
	 */

	public DisplayedScene(String fxmlFile, String cssFile, boolean resizeable,
			AbstractSceneController sceneController) {
		try {
			// Load FXML
			FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxmlFile));

			// Connect to controller
			fxmlLoader.setController(sceneController);
			this.setSceneController(sceneController);
			
			// Create new scene
			scene = new Scene(fxmlLoader.load());

			// Add stylesheet
			if (!cssFile.equals("")) {
				scene.getStylesheets().add(App.class.getResource(cssFile).toExternalForm());
			}
			scene.getStylesheets().add(App.class.getResource("Style/dialog.css").toExternalForm());
			scene.getStylesheets().add(App.class.getResource("Style/dialog.css").toExternalForm());

			this.resizeable = resizeable;
		} catch (java.lang.IllegalStateException resourceError) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public DisplayedScene(String fxmlFile, AbstractSceneController sceneController) {
		this(fxmlFile, "", false, sceneController);
	}

	public AbstractSceneController getSceneController() {
		return sc;
	}

	public void setSceneController(AbstractSceneController sc) {
		this.sc = sc;
	}

	public Scene getScene() {
		return scene;
	}

	public boolean isResizeable() {
		return resizeable;
	}

}
