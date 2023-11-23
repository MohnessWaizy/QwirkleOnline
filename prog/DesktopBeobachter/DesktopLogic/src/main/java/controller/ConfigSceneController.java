package controller;

import java.net.URL;
import java.util.ResourceBundle;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.SlowMove;
import de.upb.swtpra1819interface.models.WrongMove;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Tuple;


/**
 * This class controls the conifg.fxml file. It put given game configuration
 * into in the matching labels
 *
 */
public class ConfigSceneController extends AbstractSceneController {

	/**
	 * FXML Label to present the game name
	 */
	@FXML
	Label label_gameName;

	/**
	 * FXML Label to present the number of different colored tiles
	 */
	@FXML
	Label label_countColor;

	/**
	 * FXML Label to present the number of tiles
	 */
	@FXML
	Label label_countTiles;

	/**
	 * FXML Label to present maximum number of tiles in the hand of a player
	 */
	@FXML
	Label label_handTiles;

	/**
	 * FXML Label to present maximum amount of time of a player to make a move
	 */
	@FXML
	Label label_turnTime;

	/**
	 * FXML Label to present the visualization time for the AIs
	 */
	@FXML
	Label label_visuTime;

	/**
	 * FXML Label to present the penalty for the case of a wrong move
	 */
	@FXML
	Label label_wrongMovePenalty;

	/**
	 * FXML Label to present the penalty for the case of a slow move
	 */
	@FXML
	Label label_slowMovePenalty;

	/**
	 * FXML Label to present the maximum amount of players in the game lobby
	 */
	@FXML
	Label label_maxPlayerAmount;
	
	@FXML
	Label label_wrongMove;
	
	@FXML
	Label label_slowMove;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	/**
	 * Set given game configuration into matching panels 
	 * 
	 * @param gameName name of the game instance
	 * @param config   configuration of the game instance
	 */
	public void setConfig(String gameName, Configuration config) {
		setGameName(gameName);
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

	/* 
	 * @see controller.AbstractSceneController#receiveMessage(java.lang.Object)
	 */
	@Override
	public <T> void receiveMessage(T message) {
		if (message instanceof Tuple) {
			Tuple<String, Configuration> info = (Tuple<String, Configuration>) message;
			setConfig(info.getFirst(), info.getSecond());
		}
	}

	/**
	 * Sets label_gameName to game name 
	 * 
	 * @param gameName
	 */
	public void setGameName(String gameName) {
		label_gameName.setText("Spielname: " + gameName);
	}

	/**
	 * Sets label_countColor to amount of different colors of tiles
	 * 
	 * @param countColor
	 */
	public void setCountColor(int countColor) {
		label_countColor.setText("Anzahl Farben: " + Integer.toString(countColor));
	}

	/**
	 * Sets label_countTiles to amount of tiles
	 * 
	 * @param countTiles
	 */
	public void setCountTiles(int countTiles) {
		label_countTiles.setText("Anzahl Steine: " + Integer.toString(countTiles));
	}

	/**
	 * Sets label_countTiles to tiles on hand of player
	 * 
	 * @param handTiles
	 */
	public void setHandTiles(int handTiles) {
		label_handTiles.setText("Steine auf der Hand: " + Integer.toString(handTiles));
	}

	/**
	 * Sets label_turnTime to maximum time for a players turn
	 * 
	 * @param turnTime
	 */
	public void setTurnTime(long turnTime) {
		label_turnTime.setText("Zeit für ein Zug: " + Long.toString(turnTime));
	}

	/**
	 * Sets label_visuTime to visualization time 
	 * 
	 * @param visuTime
	 */
	public void setVisuTime(long visuTime) {
		label_visuTime.setText("Visualisierungszeit: " + Long.toString(visuTime));
	}
	
	/**
	 * Checks the given wrongMove and sets the label_wronMove to the given penaltyType in the game coniguration
	 * @param wrongMove
	 */
	public void setWrongMove(WrongMove wrongMove) {
		label_wrongMove.setText("Bestrafung für falschen Spielzug: " + wrongMove.name());
	}

	/**
	 * Sets label_wrongMovePenalty to the given penalty
	 * 
	 * @param wrongMovePenalty
	 */
	public void setWrongMovePenalty(int wrongMovePenalty) {
		label_wrongMovePenalty.setText("Punktabzug für falschen Spielzug: " + wrongMovePenalty);
	}
	
	/**
	 * Checks the value of slow move penaltyType and 
	 * sets label_slowMovePenalty to the given penalty in the game configuration
	 * 
	 * @param slowMovePenalty
	 */
	public void setSlowMove(SlowMove slowMove) {
		label_slowMove.setText("Bestrafung für langsamen Spielzug: " +  slowMove.name());
	}

	/**
	 * Sets label_slowMovePenalty to the given penalty
	 * 
	 * @param slowMovePenalty
	 */
	public void setSlowMovePenalty(int slowMovePenalty) {
		label_slowMovePenalty.setText("Punktabzug für langsamen Spielzug: " + slowMovePenalty);
	}

	/**
	 * Sets label_maxPlayerAmount to maximum amount of players possible in a game
	 * 
	 * @param maxPlayerNumber
	 */
	public void setMaxPlayerAmount(int maxPlayerNumber) {
		label_maxPlayerAmount.setText("Maximale Spieleranzahl: " + Integer.toString(maxPlayerNumber));
	}

}
