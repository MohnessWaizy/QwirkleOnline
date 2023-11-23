package org.CostumMessages;

import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.models.Client;

/**
 * This Message symbolizes the GUI that a tournament was finished
 *
 */
public class FinishTournament extends Message {
	
	private int tournamentId;
	private Client winner;
	
	public FinishTournament(int tournamentId, Client winner) {
		super(3140);
		this.tournamentId = tournamentId;
		this.winner = winner;
	}

	public int getTournamentId() {
		return tournamentId;
	}

	public Client getWinner() {
		return winner;
	}
	
	

}
