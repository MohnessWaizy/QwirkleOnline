package org.ServerGui.Controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.SlowMove;
import de.upb.swtpra1819interface.models.WrongMove;

import org.ConfigManager.ConfigManager;
import org.CostumMessages.NewGame;

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
 * Controller for scene CreateGame. Used to provide possibility to create custom
 * games.
 * </p>
 */

public class CreateGameSceneController extends AbstractSceneController {

	@FXML
	private TextField textField_maxPlayers;
	@FXML
	private Button button_decTimeToDraw;
	@FXML
	private Button button_decPointLossWrongMove;
	@FXML
	private Text text_timeToDraw;
	@FXML
	private Button button_createGame;
	@FXML
	private Button button_incTimeToDraw;
	@FXML
	private Button button_decRespite;
	@FXML
	private Text text_tileFrequency;
	@FXML
	private Button button_decPointLossSlowMove;
	@FXML
	private Text text_respite;
	@FXML
	private Button button_incPointLossSlowMove;
	@FXML
	private TextField textField_maxTiles;
	@FXML
	private TextField textField_respite;
	@FXML
	private Button button_decMaxTiles;
	@FXML
	private Text text_shapeAmount;
	@FXML
	private RadioButton radioButton_nothingWrongMove;
	@FXML
	private Button button_incPointLossWrongMove;
	@FXML
	private TextField textField_pointLossWrongMove;
	@FXML
	private RadioButton radioButton_decPointsSlowMove;
	@FXML
	private Button button_incMaxTiles;
	@FXML
	private Button button_incTileFrequency;
	@FXML
	private Text text_wrongMaxTiles;
	@FXML
	private RadioButton radioButton_removeFromGameSlowMove;
	@FXML
	private Text text_wrongShapeAmount;
	@FXML
	private Text text_wrongTileFrequency;
	@FXML
	private Text text_maxPlayers;
	@FXML
	private Button button_incMaxPlayers;
	@FXML
	private RadioButton radioButton_removeFromGameWrongMove;
	@FXML
	private Text text_wrongMove;
	@FXML
	private TextField textField_timeToDraw;
	@FXML
	private Text text_wrongRespite;
	@FXML
	private Button button_decMaxPlayers;
	@FXML
	private Button button_incShapes;
	@FXML
	private TextField textField_pointLossSlowMove;
	@FXML
	private Button button_saveAs;
	@FXML
	private Text text_maxTiles;
	@FXML
	private RadioButton radioButton_decPointsWrongMove;
	@FXML
	private TextField textField_tileFrequency;
	@FXML
	private Button button_incRespite;
	@FXML
	private TextField textField_shapes;
	@FXML
	private Button button_decTileFrequency;
	@FXML
	private Button button_decShapes;
	@FXML
	private Text text_wrongTimeToDraw;
	@FXML
	private TextField textField_name;
	@FXML
	private AnchorPane anchorPane_main;
	@FXML
	private Text text_name;
	@FXML
	private Text text_wrongMaxPlayers;
	@FXML
	private Text text_slowMove;
	@FXML
	private Text text_wrongPointLossWrongMove;
	@FXML
	private Text text_wrongPointLossSlowMove;
	@FXML
	private Text text_noName;

	ToggleGroup wrongMoveConsequence = new ToggleGroup();
	ToggleGroup slowMoveConsequence = new ToggleGroup();

	private String gameName;

	private final static String REGEXINTEGER = "\\d*";
	private final static String REGEXWHOLEINTEGER = "[+-]?\\d*";
	private final static String REGEXDECIMAL = "\\d{0,7}([\\.]\\d{0,4})?";

	public CreateGameSceneController() {
		super();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.text_wrongPointLossWrongMove.setVisible(false);
		this.text_wrongPointLossSlowMove.setVisible(false);
		this.text_wrongRespite.setVisible(false);
		this.text_wrongShapeAmount.setVisible(false);
		this.text_wrongTimeToDraw.setVisible(false);
		this.text_wrongMaxTiles.setVisible(false);
		this.text_wrongTileFrequency.setVisible(false);
		this.text_wrongMaxPlayers.setVisible(false);

		this.textField_name.setStyle("-fx-border-color: red;");

		radioButton_nothingWrongMove.setToggleGroup(wrongMoveConsequence);
		radioButton_decPointsWrongMove.setToggleGroup(wrongMoveConsequence);
		radioButton_removeFromGameWrongMove.setToggleGroup(wrongMoveConsequence);
		radioButton_nothingWrongMove.setSelected(true);

		radioButton_decPointsSlowMove.setToggleGroup(slowMoveConsequence);
		radioButton_removeFromGameSlowMove.setToggleGroup(slowMoveConsequence);
		radioButton_decPointsSlowMove.setSelected(true);

		button_decPointLossWrongMove.setDisable(true);
		button_incPointLossWrongMove.setDisable(true);
		textField_pointLossWrongMove.setDisable(true);
		button_decTileFrequency.setDisable(true);
		button_createGame.setDisable(true);
		button_saveAs.setDisable(true);

		button_saveAs.setOnMouseClicked(action -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Wähle einen Speicherort");
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Config files (*.json)", "*.json");
			fileChooser.getExtensionFilters().add(extFilter);

			File file = fileChooser.showSaveDialog(button_saveAs.getScene().getWindow());
			if (file != null) {
				ConfigManager.saveConfig(file, readConfig());
			}
		});

		button_createGame.setOnMouseClicked(action -> {
			if (!this.checkValues()) {
				return;
			}

			Configuration cf = readConfig();
			supCtrl.sendMessage(new NewGame(gameName, false, cf));
			resetValues();
			Stage stage = (Stage) this.button_createGame.getScene().getWindow();
			stage.close();
			supCtrl.resetScene();
		});

		button_decShapes.setOnMouseClicked(action -> {
			textField_shapes.setText(Integer.toString(Integer.parseInt(textField_shapes.getText()) - 1));
			button_incShapes.setDisable(false);

			if (Integer.parseInt(textField_shapes.getText()) == 2) {
				button_decShapes.setDisable(true);
			}
		});

		button_incShapes.setOnMouseClicked(action -> {
			textField_shapes.setText(Integer.toString(Integer.parseInt(textField_shapes.getText()) + 1));

			if (!(Integer.parseInt(textField_shapes.getText()) == 2)) {
				button_decShapes.setDisable(false);
			}

			if (Integer.parseInt(textField_shapes.getText()) == 12) {
				button_incShapes.setDisable(true);
			}
		});

		button_decTileFrequency.setOnMouseClicked(action -> {

			textField_tileFrequency.setText(Integer.toString(Integer.parseInt(textField_tileFrequency.getText()) - 1));
			button_incTileFrequency.setDisable(false);

			if (Integer.parseInt(textField_tileFrequency.getText()) == 1) {

				button_decTileFrequency.setDisable(true);

			}
		});

		button_incTileFrequency.setOnMouseClicked(action -> {
			textField_tileFrequency.setText(Integer.toString(Integer.parseInt(textField_tileFrequency.getText()) + 1));
			button_decTileFrequency.setDisable(false);
		});

		button_decRespite.setOnMouseClicked(action -> {
			textField_respite.setText(Integer.toString(Integer.parseInt(textField_respite.getText()) - 1000));

			if (Integer.parseInt(textField_respite.getText()) < 1000) {
				button_decRespite.setDisable(true);
				text_wrongRespite.setVisible(true);
				this.textField_respite.setStyle("-fx-border-color: red;");
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}
		});

		button_incRespite.setOnMouseClicked(action -> {
			this.textField_timeToDraw.setStyle("-fx-border-color: none;");
			textField_respite.setText(Integer.toString(Integer.parseInt(textField_respite.getText()) + 1000));
			button_decRespite.setDisable(false);
			if (checkNameField() == false) {
				this.button_createGame.setDisable(false);
				this.button_saveAs.setDisable(false);
			}
		});

		button_decTimeToDraw.setOnMouseClicked(action -> {
			textField_timeToDraw.setText(Integer.toString(Integer.parseInt(textField_timeToDraw.getText()) - 1000));

			if (Integer.parseInt(textField_timeToDraw.getText()) < 1000) {
				button_decTimeToDraw.setDisable(true);
				text_wrongTimeToDraw.setVisible(true);
				this.textField_timeToDraw.setStyle("-fx-border-color: red;");
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}
		});

		button_incTimeToDraw.setOnMouseClicked(action -> {
			this.textField_timeToDraw.setStyle("-fx-border-color: none;");
			button_decTimeToDraw.setDisable(false);
			textField_timeToDraw.setText(Integer.toString(Integer.parseInt(textField_timeToDraw.getText()) + 1000));
			if (checkNameField() == false) {
				this.button_createGame.setDisable(false);
				this.button_saveAs.setDisable(false);
			}
		});

		button_decMaxTiles.setOnMouseClicked(action -> {
			textField_maxTiles.setText(Integer.toString(Integer.parseInt(textField_maxTiles.getText()) - 1));

			if (Integer.parseInt(textField_maxTiles.getText()) == 1) {
				button_decMaxTiles.setDisable(true);
			}
		});

		button_incMaxTiles.setOnMouseClicked(action -> {
			button_decMaxTiles.setDisable(false);
			textField_maxTiles.setText(Integer.toString(Integer.parseInt(textField_maxTiles.getText()) + 1));
		});

		button_decMaxPlayers.setOnMouseClicked(action -> {
			if (Integer.parseInt(textField_maxPlayers.getText()) == 2) {
				textField_maxPlayers.setText("0");
				button_decMaxPlayers.setDisable(true);
			} else {
				textField_maxPlayers.setText(Integer.toString(Integer.parseInt(textField_maxPlayers.getText()) - 1));
			}
		});

		button_incMaxPlayers.setOnMouseClicked(action -> {
			button_decMaxPlayers.setDisable(false);
			if (Integer.parseInt(textField_maxPlayers.getText()) != 0) {
				textField_maxPlayers.setText(Integer.toString(Integer.parseInt(textField_maxPlayers.getText()) + 1));
			} else {
				textField_maxPlayers.setText("2");
			}
		});

		button_decPointLossWrongMove.setOnMouseClicked(action -> {
			textField_pointLossWrongMove
					.setText(Integer.toString(Integer.parseInt(textField_pointLossWrongMove.getText()) - 1));
		});

		button_incPointLossWrongMove.setOnMouseClicked(action -> {
			textField_pointLossWrongMove
					.setText(Integer.toString(Integer.parseInt(textField_pointLossWrongMove.getText()) + 1));
		});

		button_decPointLossSlowMove.setOnMouseClicked(action -> {
			textField_pointLossSlowMove
					.setText(Integer.toString(Integer.parseInt(textField_pointLossSlowMove.getText()) - 1));
		});

		button_incPointLossSlowMove.setOnMouseClicked(action -> {
			textField_pointLossSlowMove
					.setText(Integer.toString(Integer.parseInt(textField_pointLossSlowMove.getText()) + 1));
		});

		textField_name.textProperty().addListener((observable, oldValue, newValue) -> {
			this.button_createGame.setDisable(false);
			this.button_saveAs.setDisable(false);
			textField_name.setStyle("-fx-border-color: none;");
			text_noName.setVisible(false);

			if (textField_name.getText().isEmpty()) {
				text_noName.setVisible(true);
				this.textField_name.setStyle("-fx-border-color: red;");
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}

			this.gameName = newValue;
		});

		textField_shapes.textProperty().addListener((observable, oldValue, newValue) -> {
			if (checkNameField() == false) {
				this.button_createGame.setDisable(false);
				this.button_saveAs.setDisable(false);
			}
			button_decShapes.setDisable(false);
			button_incShapes.setDisable(false);
			textField_shapes.setStyle("-fx-border-color: none;");
			text_wrongShapeAmount.setVisible(false);

			if (textField_shapes.getText().isEmpty()) {
				text_wrongShapeAmount.setVisible(true);
				this.textField_shapes.setStyle("-fx-border-color: red;");
				button_decShapes.setDisable(true);
				button_incShapes.setDisable(true);
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}

			else if (!newValue.matches(REGEXINTEGER)) {
				text_wrongShapeAmount.setVisible(true);
				this.textField_shapes.setStyle("-fx-border-color: red;");
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}

			else if (Integer.parseInt(newValue) == 2) {
				button_decShapes.setDisable(true);
			}

			else if (Integer.parseInt(newValue) < 2) {
				text_wrongShapeAmount.setVisible(true);
				this.textField_shapes.setStyle("-fx-border-color: red;");
				button_decShapes.setDisable(true);
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}

			else if (Integer.parseInt(newValue) > 12) {
				text_wrongShapeAmount.setVisible(true);
				this.textField_shapes.setStyle("-fx-border-color: red;");
				button_incShapes.setDisable(true);
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}
		});

		textField_tileFrequency.textProperty().addListener((observable, oldValue, newValue) -> {
			if (checkNameField() == false) {
				this.button_createGame.setDisable(false);
				this.button_saveAs.setDisable(false);
			}
			button_decTileFrequency.setDisable(false);
			button_incTileFrequency.setDisable(false);
			textField_tileFrequency.setStyle("-fx-border-color: none;");
			text_wrongTileFrequency.setVisible(false);

			if (textField_tileFrequency.getText().isEmpty()) {
				text_wrongTileFrequency.setVisible(true);
				this.textField_tileFrequency.setStyle("-fx-border-color: red;");
				button_decTileFrequency.setDisable(true);
				button_incTileFrequency.setDisable(true);
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}

			else if (!newValue.matches(REGEXINTEGER)) {
				text_wrongTileFrequency.setVisible(true);
				this.textField_tileFrequency.setStyle("-fx-border-color: red;");
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}

			else if (Integer.parseInt(newValue) < 1) {
				text_wrongTileFrequency.setVisible(true);
				this.textField_tileFrequency.setStyle("-fx-border-color: red;");
				button_decTileFrequency.setDisable(true);
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}
		});

		textField_respite.textProperty().addListener((observable, oldValue, newValue) -> {
			if (checkNameField() == false) {
				this.button_createGame.setDisable(false);
				this.button_saveAs.setDisable(false);
			}
			button_decRespite.setDisable(false);
			button_incRespite.setDisable(false);
			textField_respite.setStyle("-fx-border-color: none;");
			text_wrongRespite.setVisible(false);

			if (textField_respite.getText().isEmpty()) {
				text_wrongRespite.setVisible(true);
				this.textField_respite.setStyle("-fx-border-color: red;");
				button_decRespite.setDisable(true);
				button_incRespite.setDisable(true);
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}

			else if (!newValue.matches(REGEXDECIMAL)) {
				text_wrongRespite.setVisible(true);
				this.textField_respite.setStyle("-fx-border-color: red;");
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}

			else if (!(Integer.parseInt(newValue) > 999)) {
				text_wrongRespite.setVisible(true);
				this.textField_respite.setStyle("-fx-border-color: red;");
				button_decRespite.setDisable(true);
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}
		});

		textField_timeToDraw.textProperty().addListener((observable, oldValue, newValue) -> {
			if (checkNameField() == false) {
				this.button_createGame.setDisable(false);
				this.button_saveAs.setDisable(false);
			}
			button_decTimeToDraw.setDisable(false);
			button_incTimeToDraw.setDisable(false);
			textField_timeToDraw.setStyle("-fx-border-color: none;");
			text_wrongTimeToDraw.setVisible(false);

			if (textField_timeToDraw.getText().isEmpty()) {
				text_wrongTimeToDraw.setVisible(true);
				this.textField_timeToDraw.setStyle("-fx-border-color: red;");
				button_decTimeToDraw.setDisable(true);
				button_incTimeToDraw.setDisable(true);
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}

			else if (!newValue.matches(REGEXDECIMAL)) {
				text_wrongTimeToDraw.setVisible(true);
				this.textField_timeToDraw.setStyle("-fx-border-color: red;");
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}

			else if (!(Integer.parseInt(newValue) > 999)) {
				text_wrongTimeToDraw.setVisible(true);
				this.textField_timeToDraw.setStyle("-fx-border-color: red;");
				button_decTimeToDraw.setDisable(true);
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}
		});

		textField_maxTiles.textProperty().addListener((observable, oldValue, newValue) -> {
			if (checkNameField() == false) {
				this.button_createGame.setDisable(false);
				this.button_saveAs.setDisable(false);
			}
			button_decMaxTiles.setDisable(false);
			button_incMaxTiles.setDisable(false);
			textField_maxTiles.setStyle("-fx-border-color: none;");
			text_wrongMaxTiles.setVisible(false);

			if (textField_maxTiles.getText().isEmpty()) {
				text_wrongMaxTiles.setVisible(true);
				this.textField_maxTiles.setStyle("-fx-border-color: red;");
				button_decMaxTiles.setDisable(true);
				button_incMaxTiles.setDisable(true);
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}

			else if (!newValue.matches(REGEXINTEGER)) {
				text_wrongMaxTiles.setVisible(true);
				this.textField_maxTiles.setStyle("-fx-border-color: red;");
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}

			else if (Integer.parseInt(newValue) < 1) {
				text_wrongMaxTiles.setVisible(true);
				this.textField_maxTiles.setStyle("-fx-border-color: red;");
				button_decMaxTiles.setDisable(true);
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}
		});

		textField_maxPlayers.textProperty().addListener((observable, oldValue, newValue) -> {
			if (checkNameField() == false) {
				this.button_createGame.setDisable(false);
				this.button_saveAs.setDisable(false);
			}
			button_decMaxPlayers.setDisable(false);
			button_incMaxPlayers.setDisable(false);
			textField_maxPlayers.setStyle("-fx-border-color: none;");
			text_wrongMaxPlayers.setVisible(false);

			if (textField_maxPlayers.getText().isEmpty()) {
				text_wrongMaxPlayers.setVisible(true);
				this.textField_maxPlayers.setStyle("-fx-border-color: red;");
				button_decMaxPlayers.setDisable(true);
				button_incMaxPlayers.setDisable(true);
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}

			else if (!newValue.matches(REGEXINTEGER)) {
				text_wrongMaxPlayers.setVisible(true);
				this.textField_maxPlayers.setStyle("-fx-border-color: red;");
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}

			else if (Integer.parseInt(newValue) == 0) {
				button_decMaxPlayers.setDisable(true);
			}

			else if (Integer.parseInt(newValue) < 2 && Integer.parseInt(newValue) != 0) {
				text_wrongMaxPlayers.setVisible(true);
				this.textField_maxPlayers.setStyle("-fx-border-color: red;");
				button_decMaxPlayers.setDisable(true);
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}
		});

		textField_pointLossWrongMove.textProperty().addListener((observable, oldValue, newValue) -> {
			if (checkNameField() == false) {
				this.button_createGame.setDisable(false);
				this.button_saveAs.setDisable(false);
			}
			textField_pointLossWrongMove.setStyle("-fx-border-color: none;");
			text_wrongPointLossWrongMove.setVisible(false);

			if (textField_pointLossWrongMove.getText().isEmpty()) {
				text_wrongPointLossWrongMove.setVisible(true);
				this.textField_pointLossWrongMove.setStyle("-fx-border-color: red;");
				button_decPointLossWrongMove.setDisable(true);
				button_incPointLossWrongMove.setDisable(true);
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}

			else if (!newValue.matches(REGEXWHOLEINTEGER)) {
				text_wrongPointLossWrongMove.setVisible(true);
				this.textField_pointLossWrongMove.setStyle("-fx-border-color: red;");
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}
		});

		textField_pointLossSlowMove.textProperty().addListener((observable, oldValue, newValue) -> {
			if (checkNameField() == false) {
				this.button_createGame.setDisable(false);
				this.button_saveAs.setDisable(false);
			}
			textField_pointLossSlowMove.setStyle("-fx-border-color: none;");
			text_wrongPointLossSlowMove.setVisible(false);

			if (textField_pointLossSlowMove.getText().isEmpty()) {
				text_wrongPointLossSlowMove.setVisible(true);
				this.textField_pointLossSlowMove.setStyle("-fx-border-color: red;");
				button_decPointLossSlowMove.setDisable(true);
				button_incPointLossSlowMove.setDisable(true);
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}

			else if (!newValue.matches(REGEXWHOLEINTEGER)) {
				text_wrongPointLossSlowMove.setVisible(true);
				this.textField_pointLossSlowMove.setStyle("-fx-border-color: red;");
				this.button_createGame.setDisable(true);
				this.button_saveAs.setDisable(true);
			}
		});

		this.wrongMoveConsequence.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (!(radioButton_decPointsWrongMove.isSelected())) {
					button_decPointLossWrongMove.setDisable(true);
					button_incPointLossWrongMove.setDisable(true);
					textField_pointLossWrongMove.setDisable(true);
					boolean enableCreateGame = checkValues();
					if (enableCreateGame != true) {
						button_createGame.setDisable(true);
						button_saveAs.setDisable(true);
					}

					else {
						button_createGame.setDisable(false);
						button_saveAs.setDisable(false);
					}
				}

				else {
					button_decPointLossWrongMove.setDisable(false);
					button_incPointLossWrongMove.setDisable(false);
					textField_pointLossWrongMove.setDisable(false);
				}
			}
		});

		this.slowMoveConsequence.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (!(radioButton_decPointsSlowMove.isSelected())) {
					button_decPointLossSlowMove.setDisable(true);
					button_incPointLossSlowMove.setDisable(true);
					textField_pointLossSlowMove.setDisable(true);
					boolean enableCreateGame = checkValues();
					if (enableCreateGame != true) {
						button_createGame.setDisable(true);
						button_saveAs.setDisable(true);
					}

					else {
						button_createGame.setDisable(false);
						button_saveAs.setDisable(false);
					}
				}

				else {
					button_decPointLossSlowMove.setDisable(false);
					button_incPointLossSlowMove.setDisable(false);
					textField_pointLossSlowMove.setDisable(false);
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
		if (this.textField_name.getText().isEmpty()) {
			this.text_noName.setDisable(false);
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		}

		if (textField_shapes.getText().isEmpty()) {
			text_wrongShapeAmount.setVisible(true);
			this.textField_shapes.setStyle("-fx-border-color: red;");
			button_decShapes.setDisable(true);
			button_incShapes.setDisable(true);
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		} else if (!textField_shapes.getText().matches(REGEXINTEGER)) {
			text_wrongShapeAmount.setVisible(true);
			this.textField_shapes.setStyle("-fx-border-color: red;");
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		} else if (Integer.parseInt(textField_shapes.getText()) == 2) {
			button_decShapes.setDisable(true);
		} else if (Integer.parseInt(textField_shapes.getText()) < 2) {
			text_wrongShapeAmount.setVisible(true);
			this.textField_shapes.setStyle("-fx-border-color: red;");
			button_decShapes.setDisable(true);
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		} else if (Integer.parseInt(textField_shapes.getText()) > 12) {
			text_wrongShapeAmount.setVisible(true);
			this.textField_shapes.setStyle("-fx-border-color: red;");
			button_incShapes.setDisable(true);
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		}

		if (textField_tileFrequency.getText().isEmpty()) {
			text_wrongTileFrequency.setVisible(true);
			this.textField_tileFrequency.setStyle("-fx-border-color: red;");
			button_decTileFrequency.setDisable(true);
			button_incTileFrequency.setDisable(true);
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		}
		if (!textField_tileFrequency.getText().matches(REGEXINTEGER)) {
			text_wrongTileFrequency.setVisible(true);
			this.textField_tileFrequency.setStyle("-fx-border-color: red;");
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		} else if (Integer.parseInt(textField_tileFrequency.getText()) < 1) {
			text_wrongTileFrequency.setVisible(true);
			this.textField_tileFrequency.setStyle("-fx-border-color: red;");
			button_decTileFrequency.setDisable(true);
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		}

		if (textField_respite.getText().isEmpty()) {
			text_wrongRespite.setVisible(true);
			this.textField_respite.setStyle("-fx-border-color: red;");
			button_decRespite.setDisable(true);
			button_incRespite.setDisable(true);
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		} else if (!textField_respite.getText().matches(REGEXDECIMAL)) {
			text_wrongRespite.setVisible(true);
			this.textField_respite.setStyle("-fx-border-color: red;");
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		} else if (!(Integer.parseInt(textField_respite.getText()) > 0)) {
			text_wrongRespite.setVisible(true);
			this.textField_respite.setStyle("-fx-border-color: red;");
			button_decRespite.setDisable(true);
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		}

		if (textField_timeToDraw.getText().isEmpty()) {
			text_wrongTimeToDraw.setVisible(true);
			this.textField_timeToDraw.setStyle("-fx-border-color: red;");
			button_decTimeToDraw.setDisable(true);
			button_incTimeToDraw.setDisable(true);
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		}
		if (!textField_timeToDraw.getText().matches(REGEXDECIMAL)) {
			text_wrongTimeToDraw.setVisible(true);
			this.textField_timeToDraw.setStyle("-fx-border-color: red;");
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		} else if (!(Integer.parseInt(textField_timeToDraw.getText()) > 0)) {
			text_wrongTimeToDraw.setVisible(true);
			this.textField_timeToDraw.setStyle("-fx-border-color: red;");
			button_decTimeToDraw.setDisable(true);
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		}

		if (textField_maxTiles.getText().isEmpty()) {
			text_wrongMaxTiles.setVisible(true);
			this.textField_maxTiles.setStyle("-fx-border-color: red;");
			button_decMaxTiles.setDisable(true);
			button_incMaxTiles.setDisable(true);
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		}
		if (!textField_maxTiles.getText().matches(REGEXINTEGER)) {
			text_wrongMaxTiles.setVisible(true);
			this.textField_maxTiles.setStyle("-fx-border-color: red;");
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		} else if (Integer.parseInt(textField_maxTiles.getText()) < 1) {
			text_wrongMaxTiles.setVisible(true);
			this.textField_maxTiles.setStyle("-fx-border-color: red;");
			button_decMaxTiles.setDisable(true);
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		}

		if (textField_maxPlayers.getText().isEmpty()) {
			text_wrongMaxPlayers.setVisible(true);
			this.textField_maxPlayers.setStyle("-fx-border-color: red;");
			button_decMaxPlayers.setDisable(true);
			button_incMaxPlayers.setDisable(true);
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		}
		if (!textField_maxPlayers.getText().matches(REGEXINTEGER)) {
			text_wrongMaxPlayers.setVisible(true);
			this.textField_maxPlayers.setStyle("-fx-border-color: red;");
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		} else if (Integer.parseInt(textField_maxPlayers.getText()) == 2) {
			button_decMaxPlayers.setDisable(true);
		} else if (Integer.parseInt(textField_maxPlayers.getText()) < 2) {
			text_wrongMaxPlayers.setVisible(true);
			this.textField_maxPlayers.setStyle("-fx-border-color: red;");
			button_decMaxPlayers.setDisable(true);
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		}

		if (textField_pointLossWrongMove.getText().isEmpty()) {
			text_wrongPointLossWrongMove.setVisible(true);
			this.textField_pointLossWrongMove.setStyle("-fx-border-color: red;");
			button_decPointLossWrongMove.setDisable(true);
			button_incPointLossWrongMove.setDisable(true);
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		} else if (!textField_pointLossWrongMove.getText().matches(REGEXWHOLEINTEGER)) {
			text_wrongPointLossWrongMove.setVisible(true);
			this.textField_pointLossWrongMove.setStyle("-fx-border-color: red;");
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		}

		if (textField_pointLossSlowMove.getText().isEmpty()) {
			text_wrongPointLossSlowMove.setVisible(true);
			this.textField_pointLossSlowMove.setStyle("-fx-border-color: red;");
			button_decPointLossSlowMove.setDisable(true);
			button_incPointLossSlowMove.setDisable(true);
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		}
		if (!textField_pointLossSlowMove.getText().matches(REGEXWHOLEINTEGER)) {
			text_wrongPointLossSlowMove.setVisible(true);
			this.textField_pointLossSlowMove.setStyle("-fx-border-color: red;");
			this.button_createGame.setDisable(true);
			this.button_saveAs.setDisable(true);
			valueIncorrect = true;
		}

		return !valueIncorrect;
	}

	/**
	 * Function resets gui elements to default
	 */
	private void resetValues() {
		this.textField_name.setText("");
		this.textField_shapes.setText(Integer.toString(6));
		this.textField_tileFrequency.setText(Integer.toString(1));
		this.textField_respite.setText(Integer.toString(30000));
		this.textField_timeToDraw.setText(Integer.toString(10000));
		this.textField_maxTiles.setText(Integer.toString(6));
		this.textField_maxPlayers.setText(Integer.toString(4));
		this.wrongMoveConsequence.selectToggle(radioButton_nothingWrongMove);
		this.slowMoveConsequence.selectToggle(radioButton_decPointsSlowMove);
		this.textField_pointLossWrongMove.setText(Integer.toString(1));
		this.textField_pointLossSlowMove.setText(Integer.toString(1));
		button_decPointLossWrongMove.setDisable(true);
		button_incPointLossWrongMove.setDisable(true);
		textField_pointLossWrongMove.setDisable(true);
		button_decTileFrequency.setDisable(true);
		button_createGame.setDisable(true);
		button_saveAs.setDisable(true);
	}

	/**
	 *
	 * Function checks if name textfield is empty
	 *
	 * @return true if empty
	 */
	private boolean checkNameField() {
		return this.textField_name.getText().isEmpty();
	}

	/**
	 * Function culls out user input for configuration and creates configuration
	 */

	private Configuration readConfig() {
		int colorShapeCount = Integer.parseInt(this.textField_shapes.getText());
		int tileCount = Integer.parseInt(this.textField_tileFrequency.getText());
		int maxPlayerNumber = Integer.parseInt(this.textField_maxPlayers.getText());
		long turnTime = Long.parseLong(this.textField_respite.getText());
		long timeVisualization = Long.parseLong(this.textField_timeToDraw.getText());
		int maxHandTiles = Integer.parseInt(this.textField_maxTiles.getText());

		int slowMovePenalty = Integer.parseInt(this.textField_pointLossSlowMove.getText());
		int wrongMovePenalty = Integer.parseInt(this.textField_pointLossWrongMove.getText());

		SlowMove slowMove = this.radioButton_decPointsSlowMove.isSelected() ? SlowMove.POINT_LOSS : SlowMove.KICK;
		WrongMove wrongMove;
		if (this.radioButton_nothingWrongMove.isSelected()) {
			wrongMove = WrongMove.NOTHING;
		} else if (this.radioButton_decPointsWrongMove.isSelected()) {
			wrongMove = WrongMove.POINT_LOSS;
		} else {
			wrongMove = WrongMove.KICK;
		}

		return new Configuration(colorShapeCount, tileCount, maxHandTiles, turnTime, timeVisualization, wrongMove,
				wrongMovePenalty, slowMove, slowMovePenalty, maxPlayerNumber);
	}

	/**
	 * Function processes message sent by previous scene on scene switch
	 * 
	 * @param message
	 *            message sent by previous scene
	 */
	public <T> void onSceneSwitch(T message) {
		if (message instanceof Configuration) {
			this.textField_shapes.setText(Integer.toString(((Configuration) message).getColorShapeCount()));
			this.textField_tileFrequency.setText(Integer.toString(((Configuration) message).getTileCount()));
			this.textField_maxPlayers.setText(Integer.toString(((Configuration) message).getMaxPlayerNumber()));
			this.textField_respite.setText(Long.toString(((Configuration) message).getTurnTime()));
			this.textField_timeToDraw.setText(Long.toString(((Configuration) message).getTimeVisualization()));
			this.textField_pointLossSlowMove.setText(Integer.toString(((Configuration) message).getSlowMovePenalty()));
			this.textField_pointLossWrongMove
					.setText(Integer.toString(((Configuration) message).getWrongMovePenalty()));

			if (((Configuration) message).getSlowMove() == SlowMove.POINT_LOSS) {
				this.radioButton_decPointsSlowMove.setSelected(true);
				this.textField_pointLossSlowMove.setDisable(false);
			}

			else if (((Configuration) message).getSlowMove() == SlowMove.KICK) {
				this.radioButton_removeFromGameSlowMove.setSelected(true);
			}

			if (((Configuration) message).getWrongMove() == WrongMove.POINT_LOSS) {
				this.radioButton_decPointsWrongMove.setSelected(true);
			}

			else if (((Configuration) message).getWrongMove() == WrongMove.KICK) {
				this.radioButton_removeFromGameWrongMove.setSelected(true);
			}

			else if (((Configuration) message).getWrongMove() == WrongMove.NOTHING) {
				this.radioButton_nothingWrongMove.setSelected(true);
			}
		}
	}
}
