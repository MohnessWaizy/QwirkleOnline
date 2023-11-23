package org.ServerGui.Controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.SlowMove;
import de.upb.swtpra1819interface.models.WrongMove;

import org.ConfigManager.ConfigManager;
import org.CostumMessages.NewTournament;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * <p>
 * Controller for scene CreateTournament. Used to provide possibility to create
 * custom tournaments.
 * </p>
 */

public class CreateTournamentSceneController extends AbstractSceneController {

	@FXML
	private Button button_decTimeToDrawTournament;
	@FXML
	private Button button_decPointLossWrongMoveTournament;
	@FXML
	private Text text_timeToDrawTournament;
	@FXML
	private Button button_createTournament;
	@FXML
	private Button button_incTimeToDrawTournament;
	@FXML
	private Button button_decRespiteTournament;
	@FXML
	private Text text_tileFrequencyTournament;
	@FXML
	private Button button_decPointLossSlowMoveTournament;
	@FXML
	private Text text_respiteTournament;
	@FXML
	private Button button_incPointLossSlowMoveTournament;
	@FXML
	private TextField textField_maxTilesTournament;
	@FXML
	private TextField textField_respiteTournament;
	@FXML
	private Button button_decMaxTilesTournament;
	@FXML
	private Text text_shapeAmountTournament;
	@FXML
	private RadioButton radioButton_nothingWrongMoveTournament;
	@FXML
	private Button button_incPointLossWrongMoveTournament;
	@FXML
	private TextField textField_pointLossWrongMoveTournament;
	@FXML
	private RadioButton radioButton_decPointsSlowMoveTournament;
	@FXML
	private Button button_incMaxTilesTournament;
	@FXML
	private Button button_incTileFrequencyTournament;
	@FXML
	private Text text_wrongMaxTilesTournament;
	@FXML
	private RadioButton radioButton_removeFromGameSlowMoveTournament;
	@FXML
	private Text text_wrongShapeAmountTournament;
	@FXML
	private Text text_wrongTileFrequencyTournament;
	@FXML
	private Button button_incMaxPlayersTournament;
	@FXML
	private RadioButton radioButton_removeFromGameWrongMoveTournament;
	@FXML
	private Text text_wrongMoveTournament;
	@FXML
	private TextField textField_timeToDrawTournament;
	@FXML
	private Text text_wrongRespiteTournament;
	@FXML
	private Button button_incShapesTournament;
	@FXML
	private TextField textField_pointLossSlowMoveTournament;
	@FXML
	private Button button_saveAsTournament;
	@FXML
	private Text text_maxTilesTournament;
	@FXML
	private RadioButton radioButton_decPointsWrongMoveTournament;
	@FXML
	private TextField textField_tileFrequencyTournament;
	@FXML
	private Button button_incRespiteTournament;
	@FXML
	private TextField textField_shapesTournament;
	@FXML
	private Button button_decTileFrequencyTournament;
	@FXML
	private Button button_decShapesTournament;
	@FXML
	private Text text_wrongTimeToDrawTournament;
	@FXML
	private TextField textField_nameTournament;
	@FXML
	private AnchorPane anchorPane_mainTournament;
	@FXML
	private Text text_nameTournament;
	@FXML
	private Text text_slowMoveTournament;
	@FXML
	private Text text_wrongPointLossWrongMoveTournament;
	@FXML
	private Text text_wrongPointLossSlowMoveTournament;
	@FXML
	private Text text_noNameTournament;

	private ToggleGroup wrongMoveConsequenceTournament = new ToggleGroup();
	private ToggleGroup slowMoveConsequenceTournament = new ToggleGroup();

	private String tournamentName;

	private final static String REGEXINTEGER = "\\d*";
	private final static String REGEXWHOLEINTEGER = "[+-]?\\d*";
	private final static String REGEXDECIMAL = "\\d{0,7}([\\.]\\d{0,4})?";
	private final static ExtensionFilter JSONEXTENSION = new ExtensionFilter("Konfigurationsdatei", "*.json");

	public CreateTournamentSceneController() {
		super();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.text_wrongPointLossWrongMoveTournament.setVisible(false);
		this.text_wrongPointLossSlowMoveTournament.setVisible(false);
		this.text_wrongRespiteTournament.setVisible(false);
		this.text_wrongShapeAmountTournament.setVisible(false);
		this.text_wrongTimeToDrawTournament.setVisible(false);
		this.text_wrongMaxTilesTournament.setVisible(false);
		this.text_wrongTileFrequencyTournament.setVisible(false);

		this.textField_nameTournament.setStyle("-fx-border-color: red;");

		radioButton_nothingWrongMoveTournament.setToggleGroup(wrongMoveConsequenceTournament);
		radioButton_decPointsWrongMoveTournament.setToggleGroup(wrongMoveConsequenceTournament);
		radioButton_removeFromGameWrongMoveTournament.setToggleGroup(wrongMoveConsequenceTournament);
		radioButton_nothingWrongMoveTournament.setSelected(true);

		radioButton_decPointsSlowMoveTournament.setToggleGroup(slowMoveConsequenceTournament);
		radioButton_removeFromGameSlowMoveTournament.setToggleGroup(slowMoveConsequenceTournament);
		radioButton_decPointsSlowMoveTournament.setSelected(true);

		button_decPointLossWrongMoveTournament.setDisable(true);
		button_incPointLossWrongMoveTournament.setDisable(true);
		textField_pointLossWrongMoveTournament.setDisable(true);
		button_decTileFrequencyTournament.setDisable(true);
		button_createTournament.setDisable(true);
		button_saveAsTournament.setDisable(true);

		button_saveAsTournament.setOnMouseClicked(action -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Wähle einen Speicherort");
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Config files (*.json)", "*.json");
			fileChooser.getExtensionFilters().add(extFilter);

			File file = fileChooser.showSaveDialog(button_saveAsTournament.getScene().getWindow());
			if (file != null) {
				ConfigManager.saveConfig(file, readConfig());
			}
		});

		button_createTournament.setOnMouseClicked(action -> {
			if (!this.checkValues()) {
				return;
			}

			Configuration config = readConfig();
			NewTournament tournament = new NewTournament(config, tournamentName, new ArrayList<Client>());
			Stage stage = (Stage) this.button_createTournament.getScene().getWindow();
			this.switchScene(SceneMapping.ASSIGN_PLAYERS_TOURNAMENT, tournament, stage);

			resetValues();
			supCtrl.resetScene();
		});

		button_decShapesTournament.setOnMouseClicked(action -> {
			textField_shapesTournament
					.setText(Integer.toString(Integer.parseInt(textField_shapesTournament.getText()) - 1));
			button_incShapesTournament.setDisable(false);

			if (Integer.parseInt(textField_shapesTournament.getText()) == 2) {
				button_decShapesTournament.setDisable(true);
			}
		});

		button_incShapesTournament.setOnMouseClicked(action -> {
			textField_shapesTournament
					.setText(Integer.toString(Integer.parseInt(textField_shapesTournament.getText()) + 1));

			if (!(Integer.parseInt(textField_shapesTournament.getText()) == 2)) {
				button_decShapesTournament.setDisable(false);
			}

			if (Integer.parseInt(textField_shapesTournament.getText()) == 12) {
				button_incShapesTournament.setDisable(true);
			}
		});

		button_decTileFrequencyTournament.setOnMouseClicked(action -> {

			textField_tileFrequencyTournament
					.setText(Integer.toString(Integer.parseInt(textField_tileFrequencyTournament.getText()) - 1));
			button_incTileFrequencyTournament.setDisable(false);

			if (Integer.parseInt(textField_tileFrequencyTournament.getText()) == 1) {

				button_decTileFrequencyTournament.setDisable(true);

			}
		});

		button_incTileFrequencyTournament.setOnMouseClicked(action -> {
			textField_tileFrequencyTournament
					.setText(Integer.toString(Integer.parseInt(textField_tileFrequencyTournament.getText()) + 1));
			button_decTileFrequencyTournament.setDisable(false);
		});

		button_decRespiteTournament.setOnMouseClicked(action -> {
			textField_respiteTournament
					.setText(Integer.toString(Integer.parseInt(textField_respiteTournament.getText()) - 1));

			if (Integer.parseInt(textField_respiteTournament.getText()) == 0) {
				button_decRespiteTournament.setDisable(true);
				text_wrongRespiteTournament.setVisible(true);
				this.textField_respiteTournament.setStyle("-fx-border-color: red;");
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}
		});

		button_incRespiteTournament.setOnMouseClicked(action -> {
			this.textField_timeToDrawTournament.setStyle("-fx-border-color: none;");
			textField_respiteTournament
					.setText(Integer.toString(Integer.parseInt(textField_respiteTournament.getText()) + 1));
			button_decRespiteTournament.setDisable(false);
			this.button_createTournament.setDisable(false);
			this.button_saveAsTournament.setDisable(false);
		});

		button_decTimeToDrawTournament.setOnMouseClicked(action -> {
			textField_timeToDrawTournament
					.setText(Integer.toString(Integer.parseInt(textField_timeToDrawTournament.getText()) - 1));

			if (Integer.parseInt(textField_timeToDrawTournament.getText()) == 0) {
				button_decTimeToDrawTournament.setDisable(true);
				text_wrongTimeToDrawTournament.setVisible(true);
				this.textField_timeToDrawTournament.setStyle("-fx-border-color: red;");
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}
		});

		button_incTimeToDrawTournament.setOnMouseClicked(action -> {
			this.textField_timeToDrawTournament.setStyle("-fx-border-color: none;");
			button_decTimeToDrawTournament.setDisable(false);
			textField_timeToDrawTournament
					.setText(Integer.toString(Integer.parseInt(textField_timeToDrawTournament.getText()) + 1));
			this.button_createTournament.setDisable(false);
			this.button_saveAsTournament.setDisable(false);
		});

		button_decMaxTilesTournament.setOnMouseClicked(action -> {
			textField_maxTilesTournament
					.setText(Integer.toString(Integer.parseInt(textField_maxTilesTournament.getText()) - 1));

			if (Integer.parseInt(textField_maxTilesTournament.getText()) == 1) {
				button_decMaxTilesTournament.setDisable(true);
			}
		});

		button_incMaxTilesTournament.setOnMouseClicked(action -> {
			button_decMaxTilesTournament.setDisable(false);
			textField_maxTilesTournament
					.setText(Integer.toString(Integer.parseInt(textField_maxTilesTournament.getText()) + 1));
		});

		button_decPointLossWrongMoveTournament.setOnMouseClicked(action -> {
			textField_pointLossWrongMoveTournament
					.setText(Integer.toString(Integer.parseInt(textField_pointLossWrongMoveTournament.getText()) - 1));
		});

		button_incPointLossWrongMoveTournament.setOnMouseClicked(action -> {
			textField_pointLossWrongMoveTournament
					.setText(Integer.toString(Integer.parseInt(textField_pointLossWrongMoveTournament.getText()) + 1));
		});

		button_decPointLossSlowMoveTournament.setOnMouseClicked(action -> {
			textField_pointLossSlowMoveTournament
					.setText(Integer.toString(Integer.parseInt(textField_pointLossSlowMoveTournament.getText()) - 1));
		});

		button_incPointLossSlowMoveTournament.setOnMouseClicked(action -> {
			textField_pointLossSlowMoveTournament
					.setText(Integer.toString(Integer.parseInt(textField_pointLossSlowMoveTournament.getText()) + 1));
		});

		textField_nameTournament.textProperty().addListener((observable, oldValue, newValue) -> {
			this.button_createTournament.setDisable(false);
			this.button_saveAsTournament.setDisable(false);
			textField_nameTournament.setStyle("-fx-border-color: none;");
			text_noNameTournament.setVisible(false);

			if (textField_nameTournament.getText().isEmpty()) {
				text_noNameTournament.setVisible(true);
				this.textField_nameTournament.setStyle("-fx-border-color: red;");
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}

			this.tournamentName = newValue;
		});

		textField_shapesTournament.textProperty().addListener((observable, oldValue, newValue) -> {
			this.button_createTournament.setDisable(false);
			this.button_saveAsTournament.setDisable(false);
			button_decShapesTournament.setDisable(false);
			button_incShapesTournament.setDisable(false);
			textField_shapesTournament.setStyle("-fx-border-color: none;");
			text_wrongShapeAmountTournament.setVisible(false);

			if (textField_shapesTournament.getText().isEmpty()) {
				text_wrongShapeAmountTournament.setVisible(true);
				this.textField_shapesTournament.setStyle("-fx-border-color: red;");
				button_decShapesTournament.setDisable(true);
				button_incShapesTournament.setDisable(true);
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}

			else if (!newValue.matches(REGEXINTEGER)) {
				text_wrongShapeAmountTournament.setVisible(true);
				this.textField_shapesTournament.setStyle("-fx-border-color: red;");
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}

			else if (Integer.parseInt(newValue) == 2) {
				button_decShapesTournament.setDisable(true);
			}

			else if (Integer.parseInt(newValue) < 2) {
				text_wrongShapeAmountTournament.setVisible(true);
				this.textField_shapesTournament.setStyle("-fx-border-color: red;");
				button_decShapesTournament.setDisable(true);
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}

			else if (Integer.parseInt(newValue) > 12) {
				text_wrongShapeAmountTournament.setVisible(true);
				this.textField_shapesTournament.setStyle("-fx-border-color: red;");
				button_incShapesTournament.setDisable(true);
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}
		});

		textField_tileFrequencyTournament.textProperty().addListener((observable, oldValue, newValue) -> {
			this.button_createTournament.setDisable(false);
			this.button_saveAsTournament.setDisable(false);
			button_decTileFrequencyTournament.setDisable(false);
			button_incTileFrequencyTournament.setDisable(false);
			textField_tileFrequencyTournament.setStyle("-fx-border-color: none;");
			text_wrongTileFrequencyTournament.setVisible(false);

			if (textField_tileFrequencyTournament.getText().isEmpty()) {
				text_wrongTileFrequencyTournament.setVisible(true);
				this.textField_tileFrequencyTournament.setStyle("-fx-border-color: red;");
				button_decTileFrequencyTournament.setDisable(true);
				button_incTileFrequencyTournament.setDisable(true);
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}

			else if (!newValue.matches(REGEXINTEGER)) {
				text_wrongTileFrequencyTournament.setVisible(true);
				this.textField_tileFrequencyTournament.setStyle("-fx-border-color: red;");
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}

			else if (Integer.parseInt(newValue) < 1) {
				text_wrongTileFrequencyTournament.setVisible(true);
				this.textField_tileFrequencyTournament.setStyle("-fx-border-color: red;");
				button_decTileFrequencyTournament.setDisable(true);
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}
		});

		textField_respiteTournament.textProperty().addListener((observable, oldValue, newValue) -> {
			this.button_createTournament.setDisable(false);
			this.button_saveAsTournament.setDisable(false);
			button_decRespiteTournament.setDisable(false);
			button_incRespiteTournament.setDisable(false);
			textField_respiteTournament.setStyle("-fx-border-color: none;");
			text_wrongRespiteTournament.setVisible(false);

			if (textField_respiteTournament.getText().isEmpty()) {
				text_wrongRespiteTournament.setVisible(true);
				this.textField_respiteTournament.setStyle("-fx-border-color: red;");
				button_decRespiteTournament.setDisable(true);
				button_incRespiteTournament.setDisable(true);
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}

			else if (!newValue.matches(REGEXDECIMAL)) {
				text_wrongRespiteTournament.setVisible(true);
				this.textField_respiteTournament.setStyle("-fx-border-color: red;");
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}

			else if (!(Integer.parseInt(newValue) > 0)) {
				text_wrongRespiteTournament.setVisible(true);
				this.textField_respiteTournament.setStyle("-fx-border-color: red;");
				button_decRespiteTournament.setDisable(true);
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}
		});

		textField_timeToDrawTournament.textProperty().addListener((observable, oldValue, newValue) -> {
			this.button_createTournament.setDisable(false);
			this.button_saveAsTournament.setDisable(false);
			button_decTimeToDrawTournament.setDisable(false);
			button_incTimeToDrawTournament.setDisable(false);
			textField_timeToDrawTournament.setStyle("-fx-border-color: none;");
			text_wrongTimeToDrawTournament.setVisible(false);

			if (textField_timeToDrawTournament.getText().isEmpty()) {
				text_wrongTimeToDrawTournament.setVisible(true);
				this.textField_timeToDrawTournament.setStyle("-fx-border-color: red;");
				button_decTimeToDrawTournament.setDisable(true);
				button_incTimeToDrawTournament.setDisable(true);
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}

			else if (!newValue.matches(REGEXDECIMAL)) {
				text_wrongTimeToDrawTournament.setVisible(true);
				this.textField_timeToDrawTournament.setStyle("-fx-border-color: red;");
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}

			else if (!(Integer.parseInt(newValue) > 0)) {
				text_wrongTimeToDrawTournament.setVisible(true);
				this.textField_timeToDrawTournament.setStyle("-fx-border-color: red;");
				button_decTimeToDrawTournament.setDisable(true);
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}
		});

		textField_maxTilesTournament.textProperty().addListener((observable, oldValue, newValue) -> {
			this.button_createTournament.setDisable(false);
			this.button_saveAsTournament.setDisable(false);
			button_decMaxTilesTournament.setDisable(false);
			button_incMaxTilesTournament.setDisable(false);
			textField_maxTilesTournament.setStyle("-fx-border-color: none;");
			text_wrongMaxTilesTournament.setVisible(false);

			if (textField_maxTilesTournament.getText().isEmpty()) {
				text_wrongMaxTilesTournament.setVisible(true);
				this.textField_maxTilesTournament.setStyle("-fx-border-color: red;");
				button_decMaxTilesTournament.setDisable(true);
				button_incMaxTilesTournament.setDisable(true);
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}

			else if (!newValue.matches(REGEXINTEGER)) {
				text_wrongMaxTilesTournament.setVisible(true);
				this.textField_maxTilesTournament.setStyle("-fx-border-color: red;");
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}

			else if (Integer.parseInt(newValue) < 1) {
				text_wrongMaxTilesTournament.setVisible(true);
				this.textField_maxTilesTournament.setStyle("-fx-border-color: red;");
				button_decMaxTilesTournament.setDisable(true);
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}
		});

		textField_pointLossWrongMoveTournament.textProperty().addListener((observable, oldValue, newValue) -> {
			this.button_createTournament.setDisable(false);
			this.button_saveAsTournament.setDisable(false);
			textField_pointLossWrongMoveTournament.setStyle("-fx-border-color: none;");
			text_wrongPointLossWrongMoveTournament.setVisible(false);

			if (textField_pointLossWrongMoveTournament.getText().isEmpty()) {
				text_wrongPointLossWrongMoveTournament.setVisible(true);
				this.textField_pointLossWrongMoveTournament.setStyle("-fx-border-color: red;");
				button_decPointLossWrongMoveTournament.setDisable(true);
				button_incPointLossWrongMoveTournament.setDisable(true);
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}

			else if (!newValue.matches(REGEXWHOLEINTEGER)) {
				text_wrongPointLossWrongMoveTournament.setVisible(true);
				this.textField_pointLossWrongMoveTournament.setStyle("-fx-border-color: red;");
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}
		});

		textField_pointLossSlowMoveTournament.textProperty().addListener((observable, oldValue, newValue) -> {
			this.button_createTournament.setDisable(false);
			this.button_saveAsTournament.setDisable(false);
			textField_pointLossSlowMoveTournament.setStyle("-fx-border-color: none;");
			text_wrongPointLossSlowMoveTournament.setVisible(false);

			if (textField_pointLossSlowMoveTournament.getText().isEmpty()) {
				text_wrongPointLossSlowMoveTournament.setVisible(true);
				this.textField_pointLossSlowMoveTournament.setStyle("-fx-border-color: red;");
				button_decPointLossSlowMoveTournament.setDisable(true);
				button_incPointLossSlowMoveTournament.setDisable(true);
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}

			else if (!newValue.matches(REGEXWHOLEINTEGER)) {
				text_wrongPointLossSlowMoveTournament.setVisible(true);
				this.textField_pointLossSlowMoveTournament.setStyle("-fx-border-color: red;");
				this.button_createTournament.setDisable(true);
				this.button_saveAsTournament.setDisable(true);
			}
		});

		this.wrongMoveConsequenceTournament.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (!(radioButton_decPointsWrongMoveTournament.isSelected())) {
					button_decPointLossWrongMoveTournament.setDisable(true);
					button_incPointLossWrongMoveTournament.setDisable(true);
					textField_pointLossWrongMoveTournament.setDisable(true);
					boolean enableCreateGame = checkValues();
					if (enableCreateGame != true) {
						button_createTournament.setDisable(true);
						button_saveAsTournament.setDisable(true);
					}

					else {
						button_createTournament.setDisable(false);
						button_saveAsTournament.setDisable(false);
					}
				}

				else {
					button_decPointLossWrongMoveTournament.setDisable(false);
					button_incPointLossWrongMoveTournament.setDisable(false);
					textField_pointLossWrongMoveTournament.setDisable(false);
				}
			}
		});

		this.slowMoveConsequenceTournament.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (!(radioButton_decPointsSlowMoveTournament.isSelected())) {
					button_decPointLossSlowMoveTournament.setDisable(true);
					button_incPointLossSlowMoveTournament.setDisable(true);
					textField_pointLossSlowMoveTournament.setDisable(true);
					boolean enableCreateGame = checkValues();
					if (enableCreateGame != true) {
						button_createTournament.setDisable(true);
						button_saveAsTournament.setDisable(true);
					}

					else {
						button_createTournament.setDisable(false);
						button_saveAsTournament.setDisable(false);
					}
				}

				else {
					button_decPointLossSlowMoveTournament.setDisable(false);
					button_incPointLossSlowMoveTournament.setDisable(false);
					textField_pointLossSlowMoveTournament.setDisable(false);
				}
			}
		});

	}

	/**
	 * Function checks if all input field texts are correct.
	 * 
	 * @return valueIncorrect returns whether all input fields are correct. Returns
	 *         true if not false if input fields are correct.
	 */
	private boolean checkValues() {
		boolean valueIncorrect = false;

		if (this.textField_nameTournament.getText().isEmpty()) {
			this.text_noNameTournament.setDisable(false);
			this.button_createTournament.setDisable(true);
			this.button_saveAsTournament.setDisable(true);
			valueIncorrect = true;
		}

		if (textField_shapesTournament.getText().isEmpty()) {
			text_wrongShapeAmountTournament.setVisible(true);
			this.textField_shapesTournament.setStyle("-fx-border-color: red;");
			button_decShapesTournament.setDisable(true);
			button_incShapesTournament.setDisable(true);
			this.button_createTournament.setDisable(true);
			this.button_saveAsTournament.setDisable(true);
			valueIncorrect = true;
		}
		if (!textField_shapesTournament.getText().matches(REGEXINTEGER)) {
			text_wrongShapeAmountTournament.setVisible(true);
			this.textField_shapesTournament.setStyle("-fx-border-color: red;");
			this.button_createTournament.setDisable(true);
			this.button_saveAsTournament.setDisable(true);
			valueIncorrect = true;
		} else if (Integer.parseInt(textField_shapesTournament.getText()) == 2) {
			button_decShapesTournament.setDisable(true);
		} else if (Integer.parseInt(textField_shapesTournament.getText()) < 2) {
			text_wrongShapeAmountTournament.setVisible(true);
			this.textField_shapesTournament.setStyle("-fx-border-color: red;");
			button_decShapesTournament.setDisable(true);
			this.button_createTournament.setDisable(true);
			this.button_saveAsTournament.setDisable(true);
			valueIncorrect = true;
		} else if (Integer.parseInt(textField_shapesTournament.getText()) > 12) {
			text_wrongShapeAmountTournament.setVisible(true);
			this.textField_shapesTournament.setStyle("-fx-border-color: red;");
			button_incShapesTournament.setDisable(true);
			this.button_createTournament.setDisable(true);
			this.button_saveAsTournament.setDisable(true);
			valueIncorrect = true;
		}

		if (textField_tileFrequencyTournament.getText().isEmpty()) {
			text_wrongTileFrequencyTournament.setVisible(true);
			this.textField_tileFrequencyTournament.setStyle("-fx-border-color: red;");
			button_decTileFrequencyTournament.setDisable(true);
			button_incTileFrequencyTournament.setDisable(true);
			this.button_createTournament.setDisable(true);
			this.button_saveAsTournament.setDisable(true);
			valueIncorrect = true;
		}
		if (!textField_tileFrequencyTournament.getText().matches(REGEXINTEGER)) {
			text_wrongTileFrequencyTournament.setVisible(true);
			this.textField_tileFrequencyTournament.setStyle("-fx-border-color: red;");
			this.button_createTournament.setDisable(true);
			this.button_saveAsTournament.setDisable(true);
			valueIncorrect = true;
		} else if (Integer.parseInt(textField_tileFrequencyTournament.getText()) < 1) {
			text_wrongTileFrequencyTournament.setVisible(true);
			this.textField_tileFrequencyTournament.setStyle("-fx-border-color: red;");
			button_decTileFrequencyTournament.setDisable(true);
			this.button_createTournament.setDisable(true);
			this.button_saveAsTournament.setDisable(true);
			valueIncorrect = true;
		}

		if (textField_respiteTournament.getText().isEmpty()) {
			text_wrongRespiteTournament.setVisible(true);
			this.textField_respiteTournament.setStyle("-fx-border-color: red;");
			button_decRespiteTournament.setDisable(true);
			button_incRespiteTournament.setDisable(true);
			this.button_createTournament.setDisable(true);
			this.button_saveAsTournament.setDisable(true);
			valueIncorrect = true;
		} else if (!textField_respiteTournament.getText().matches(REGEXDECIMAL)) {
			text_wrongRespiteTournament.setVisible(true);
			this.textField_respiteTournament.setStyle("-fx-border-color: red;");
			this.button_createTournament.setDisable(true);
			this.button_saveAsTournament.setDisable(true);
			valueIncorrect = true;
		} else if (!(Integer.parseInt(textField_respiteTournament.getText()) > 0)) {
			text_wrongRespiteTournament.setVisible(true);
			this.textField_respiteTournament.setStyle("-fx-border-color: red;");
			button_decRespiteTournament.setDisable(true);
			this.button_createTournament.setDisable(true);
			this.button_saveAsTournament.setDisable(true);
			valueIncorrect = true;
		}

		if (textField_timeToDrawTournament.getText().isEmpty()) {
			text_wrongTimeToDrawTournament.setVisible(true);
			this.textField_timeToDrawTournament.setStyle("-fx-border-color: red;");
			button_decTimeToDrawTournament.setDisable(true);
			button_incTimeToDrawTournament.setDisable(true);
			this.button_createTournament.setDisable(true);
			this.button_saveAsTournament.setDisable(true);
			valueIncorrect = true;
		}
		if (!textField_timeToDrawTournament.getText().matches(REGEXDECIMAL)) {
			text_wrongTimeToDrawTournament.setVisible(true);
			this.textField_timeToDrawTournament.setStyle("-fx-border-color: red;");
			this.button_createTournament.setDisable(true);
			this.button_saveAsTournament.setDisable(true);
			valueIncorrect = true;
		} else if (!(Integer.parseInt(textField_timeToDrawTournament.getText()) > 0)) {
			text_wrongTimeToDrawTournament.setVisible(true);
			this.textField_timeToDrawTournament.setStyle("-fx-border-color: red;");
			button_decTimeToDrawTournament.setDisable(true);
			this.button_createTournament.setDisable(true);
			this.button_saveAsTournament.setDisable(true);
			valueIncorrect = true;
		}

		if (textField_maxTilesTournament.getText().isEmpty()) {
			text_wrongMaxTilesTournament.setVisible(true);
			this.textField_maxTilesTournament.setStyle("-fx-border-color: red;");
			button_decMaxTilesTournament.setDisable(true);
			button_incMaxTilesTournament.setDisable(true);
			this.button_createTournament.setDisable(true);
			this.button_saveAsTournament.setDisable(true);
			valueIncorrect = true;
		}
		if (!textField_maxTilesTournament.getText().matches(REGEXINTEGER)) {
			text_wrongMaxTilesTournament.setVisible(true);
			this.textField_maxTilesTournament.setStyle("-fx-border-color: red;");
			this.button_createTournament.setDisable(true);
			this.button_saveAsTournament.setDisable(true);
			valueIncorrect = true;
		} else if (Integer.parseInt(textField_maxTilesTournament.getText()) < 1) {
			text_wrongMaxTilesTournament.setVisible(true);
			this.textField_maxTilesTournament.setStyle("-fx-border-color: red;");
			button_decMaxTilesTournament.setDisable(true);
			this.button_createTournament.setDisable(true);
			this.button_saveAsTournament.setDisable(true);
			valueIncorrect = true;
		}
		return !valueIncorrect;
	}

	/**
	 * Function resets gui elements to default
	 */
	private void resetValues() {
		this.textField_nameTournament.setText("");
		this.textField_shapesTournament.setText(Integer.toString(6));
		this.textField_tileFrequencyTournament.setText(Integer.toString(1));
		this.textField_respiteTournament.setText(Integer.toString(30000));
		this.textField_timeToDrawTournament.setText(Integer.toString(10000));
		this.textField_maxTilesTournament.setText(Integer.toString(6));
		this.wrongMoveConsequenceTournament.selectToggle(radioButton_nothingWrongMoveTournament);
		this.slowMoveConsequenceTournament.selectToggle(radioButton_decPointsSlowMoveTournament);
		this.textField_pointLossWrongMoveTournament.setText(Integer.toString(1));
		this.textField_pointLossSlowMoveTournament.setText(Integer.toString(1));
		button_decPointLossWrongMoveTournament.setDisable(true);
		button_incPointLossWrongMoveTournament.setDisable(true);
		textField_pointLossWrongMoveTournament.setDisable(true);
		button_decTileFrequencyTournament.setDisable(true);
		button_createTournament.setDisable(true);
		button_saveAsTournament.setDisable(true);
	}

	/**
	 * Function culls out user input for configuration and creates configuration
	 */
	private Configuration readConfig() {
		int colorShapeCount = Integer.parseInt(this.textField_shapesTournament.getText());
		int tileCount = Integer.parseInt(this.textField_tileFrequencyTournament.getText());
		long turnTime = Long.parseLong(this.textField_respiteTournament.getText());
		long timeVisualization = Long.parseLong(this.textField_timeToDrawTournament.getText());
		int maxHandTiles = Integer.parseInt(this.textField_maxTilesTournament.getText());

		int slowMovePenalty = Integer.parseInt(this.textField_pointLossSlowMoveTournament.getText());
		int wrongMovePenalty = Integer.parseInt(this.textField_pointLossWrongMoveTournament.getText());

		SlowMove slowMove = this.radioButton_decPointsSlowMoveTournament.isSelected() ? SlowMove.POINT_LOSS
				: SlowMove.KICK;
		WrongMove wrongMove;
		if (this.radioButton_nothingWrongMoveTournament.isSelected()) {
			wrongMove = WrongMove.NOTHING;
		} else if (this.radioButton_decPointsWrongMoveTournament.isSelected()) {
			wrongMove = WrongMove.POINT_LOSS;
		} else {
			wrongMove = WrongMove.KICK;
		}

		return new Configuration(colorShapeCount, tileCount, maxHandTiles, turnTime, timeVisualization, wrongMove,
				wrongMovePenalty, slowMove, slowMovePenalty, 3);
	}

	public <T> void onSceneSwitch(T message) {
		if (message instanceof Configuration) {
			this.textField_shapesTournament.setText(Integer.toString(((Configuration) message).getColorShapeCount()));
			this.textField_tileFrequencyTournament.setText(Integer.toString(((Configuration) message).getTileCount()));
			this.textField_respiteTournament.setText(Long.toString(((Configuration) message).getTurnTime()));
			this.textField_timeToDrawTournament
					.setText(Long.toString(((Configuration) message).getTimeVisualization()));
			this.textField_pointLossSlowMoveTournament
					.setText(Integer.toString(((Configuration) message).getSlowMovePenalty()));
			this.textField_pointLossWrongMoveTournament
					.setText(Integer.toString(((Configuration) message).getWrongMovePenalty()));

			if (((Configuration) message).getSlowMove() == SlowMove.POINT_LOSS) {
				this.radioButton_decPointsSlowMoveTournament.setSelected(true);
				this.textField_pointLossSlowMoveTournament.setDisable(false);
			}

			else if (((Configuration) message).getSlowMove() == SlowMove.KICK) {
				this.radioButton_removeFromGameSlowMoveTournament.setSelected(true);
			}

			if (((Configuration) message).getWrongMove() == WrongMove.POINT_LOSS) {
				this.radioButton_decPointsWrongMoveTournament.setSelected(true);
			}

			else if (((Configuration) message).getWrongMove() == WrongMove.KICK) {
				this.radioButton_removeFromGameWrongMoveTournament.setSelected(true);
			}

			else if (((Configuration) message).getWrongMove() == WrongMove.NOTHING) {
				this.radioButton_nothingWrongMoveTournament.setSelected(true);
			}
		}
	}
}
