package org.ServerGui.Controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import org.ConfigManager.*;

import de.upb.swtpra1819interface.models.Configuration;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * <p>
 * Controller for scene NewOrSavedGame. Used to offer possibility to load
 * configurations.
 * </p>
 */

public class NewOrSavedGameSceneController extends AbstractSceneController {

	@FXML
	Button button_loadConfig;

	@FXML
	Button button_createNewGame;

	public static final ExtensionFilter JSONEXTENSION = new ExtensionFilter("Konfigurationsdatei", "*.json");

	public NewOrSavedGameSceneController() {
		super();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.button_loadConfig.setOnMouseClicked(action -> {

			final FileChooser fc = new FileChooser();
			fc.setTitle("Wähle eine Konfigurationsdatei");
			fc.getExtensionFilters().add(JSONEXTENSION);
			fc.setInitialFileName("config.json");

			File file = fc.showOpenDialog(button_loadConfig.getScene().getWindow());
			if (file != null) {
				Configuration config = null;
				config = ConfigManager.loadConfig(file);
				switchScene(SceneMapping.CREATE_GAME, config, (Stage) this.button_loadConfig.getScene().getWindow());
			}

		});

		this.button_createNewGame.setOnMouseClicked(action -> {
			switchScene(SceneMapping.CREATE_GAME, (Stage) this.button_createNewGame.getScene().getWindow());
		});

	}

}
