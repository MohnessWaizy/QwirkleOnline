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
 * Controller for scene NewOrSavedTournament. Used to offer possibility to load
 * configurations.
 * </p>
 */

public class NewOrSavedTournamentSceneController extends AbstractSceneController {

	@FXML
	Button button_loadConfigTournament;

	@FXML
	Button button_createNewTournament;

	public final static ExtensionFilter JSONEXTENSION = new ExtensionFilter("Konfigurationsdatei", "*.json");

	public NewOrSavedTournamentSceneController() {
		super();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.button_loadConfigTournament.setOnMouseClicked(action -> {

			final FileChooser fc = new FileChooser();
			fc.setTitle("Wähle eine Konfigurationsdatei");
			fc.getExtensionFilters().add(JSONEXTENSION);
			fc.setInitialFileName("config.json");

			File file = fc.showOpenDialog(button_loadConfigTournament.getScene().getWindow());
			if (file != null) {
				Configuration config = null;
				config = ConfigManager.loadConfig(file);
				switchScene(SceneMapping.CREATE_TOURNAMENT, config,
						(Stage) this.button_loadConfigTournament.getScene().getWindow());
			}

		});

		this.button_createNewTournament.setOnMouseClicked(action -> {
			switchScene(SceneMapping.CREATE_TOURNAMENT, (Stage) this.button_createNewTournament.getScene().getWindow());
		});

	}

}
