package org.ServerGui.Controller;

import java.net.URL;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.ServerGui.Controller.AbstractSceneController;

import de.upb.swtpra1819interface.models.Client;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import org.ServerGui.Model.DataContainer;
import org.ServerGui.Model.Tuple;
import org.ServerGui.View.RankingHeadingPanel;
import org.ServerGui.View.RankingPanel;

/**
 * <p>
 * Controller for scene Ranking. Used to display a player's amount of won games
 * and total score.
 * </p>
 */

public class RankingSceneController extends AbstractSceneController {

	@FXML
	VBox VBox_scoreList;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	/**
	 * Function loads players, their amount of won games and their score into window
	 * 
	 * @param scoreList
	 *            list of players and their scores
	 */
	public void loadPlayers(Hashtable<Integer, Hashtable<Client, Tuple<Integer, Integer>>> scoreList) {
		//createHeading();
		for (Entry e : scoreList.entrySet()) {
			Client player = (Client) e.getKey();
			int gamesWon = (int) ((Tuple) e.getValue()).getFirst();
			int score = (int) ((Tuple) e.getValue()).getSecond();
			DataContainer dataContainer = this.lobbyCtrl.getSupCtrl().getData();
			VBox_scoreList.getChildren().add(new RankingPanel(player, dataContainer, gamesWon, score));
		}
	}

	@Override
	/**
	 * Function processes message transmitted by scene switch
	 * 
	 * @param scoreList
	 *            list of players and their scores
	 */
	public <T> void reciveMessage(T message) {
		resetContent();
		if (message instanceof Hashtable<?, ?>) {
			Hashtable<Integer, Hashtable<Client, Tuple<Integer, Integer>>> scoreList = (Hashtable<Integer, Hashtable<Client, Tuple<Integer, Integer>>>) message;
			loadPlayers(scoreList);
		}
	}

	/**
	 * Function resets window content
	 */
	public void resetContent() {
		this.VBox_scoreList.getChildren().clear();
	}

	/**
	 * Adds an information row for description
	 */
	public void createHeading() {
		RankingHeadingPanel panel = new RankingHeadingPanel();
		this.VBox_scoreList.getChildren().add(panel);
	}

}
