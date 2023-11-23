package org.ServerGui.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.SlowMove;
import de.upb.swtpra1819interface.models.WrongMove;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.ServerGui.Model.Tuple;

/**
 * <p>
 * Controller for scene Config. Used to display a specific game's configuration.
 * </p>
 */

public class ConfigSceneController extends AbstractSceneController {

	@FXML
	Label label_gameName;
	@FXML
	Label label_countColor;
	@FXML
	Label label_countTiles;
	@FXML
	Label label_handTiles;
	@FXML
	Label label_turnTime;
	@FXML
	Label label_visuTime;
	@FXML
	Label label_wrongMove;
	@FXML
	Label label_wrongMovePenalty;
	@FXML
	Label label_slowMove;
	@FXML
	Label label_slowMovePenalty;
	@FXML
	Label label_maxPlayerAmount;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	/**
	 * Function triggers setting of labels showing config data.
	 * 
	 * @param gameName name of game whose configuration is shown
	 * @param config   games config to be displayed
	 */
	public void setConfig(String gameName, Configuration config) {
		this.setGameName(gameName);
		setCountColor(config.getColorShapeCount());
		setCountTiles(config.getTileCount());
		setHandTiles(config.getMaxHandTiles());
		setTurnTime(config.getTurnTime());
		setVisuTime(config.getTimeVisualization());
		setWrongMove(config.getWrongMove());
		setWrongMovePenalty(config.getWrongMovePenalty());
		setSlowMove(config.getSlowMove());
		setSlowMovePenalty(config.getSlowMovePenalty());
		setMaxPlayerAmount(config.getMaxPlayerNumber());
	}

	/**
	 * Function processes message send by previous scene on scene switch
	 * 
	 * @param message message sent by previous scene
	 */
	@Override
	public <T> void reciveMessage(T message) {
		if (message instanceof Tuple) {
			Tuple<String, Configuration> info = (Tuple<String, Configuration>) message;
			setConfig(info.getFirst(), info.getSecond());
		}
	}

	public void setGameName(String gameName) {
		label_gameName.setText("Spielname: " + gameName);
	}

	public void setCountColor(int countColor) {
		label_countColor.setText("Anzahl Farben: " + Integer.toString(countColor));
	}

	public void setCountTiles(int countTiles) {
		label_countTiles.setText("Anzahl Steine: " + Integer.toString(countTiles));
	}

	public void setHandTiles(int handTiles) {
		label_handTiles.setText("Steine auf der Hand: " + Integer.toString(handTiles));
	}

	public void setTurnTime(long turnTime) {
		label_turnTime.setText("Zeit für ein Zug: " + Long.toString(turnTime));
	}

	public void setVisuTime(long visuTime) {
		label_visuTime.setText("Visualisierungszeit: " + Long.toString(visuTime));
	}
	
	public void setWrongMove(WrongMove wrongMove) {
		label_wrongMove.setText("Falscher Spielzug: " + wrongMove.name());
	}

	public void setWrongMovePenalty(int wrongMovePenalty) {
		label_wrongMovePenalty.setText("Bestrafung für falschen Spielzug: " + wrongMovePenalty);
	}
	public void setSlowMove(SlowMove slowMove) {
		label_slowMove.setText("Falscher Spielzug: " + slowMove.name());
	}
	public void setSlowMovePenalty(int slowMovePenalty) {
		label_slowMovePenalty.setText("Bestrafung für langsamen Spielzug: " + slowMovePenalty);
	}

	public void setMaxPlayerAmount(int maxPlayerNumber) {
		label_maxPlayerAmount.setText("Maximale Spieleranzahl: " + Integer.toString(maxPlayerNumber));
	}

}
