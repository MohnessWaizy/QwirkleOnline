package controller;

import java.util.List;

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

	public DisplayedScene(String fxmlFile, List<String> cssFiles, boolean resizeable,
			AbstractSceneController sceneController) {
		try {
			// Load FXML
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlFile));
			// Connect to controller
			fxmlLoader.setController(sceneController);
			// Create new scene
			scene = new Scene(fxmlLoader.load());

			// Add stylesheet
			if (cssFiles != null) {
				for (String file : cssFiles) {
					try {
						scene.getStylesheets().add(getClass().getClassLoader().getResource(file).toExternalForm());
					} catch (IllegalStateException ils) {
						System.out.println("File: " + file + " not found");
					}
				}
			}

			this.resizeable = resizeable;
			this.sc = sceneController;

			System.out.println(scene + "Scene loaded");
		} catch (java.lang.IllegalStateException resourceError) {
			System.out.println("File not found: " + resourceError.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public DisplayedScene(String fxmlFile, AbstractSceneController sceneController) {
		this(fxmlFile, null, false, sceneController);
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
