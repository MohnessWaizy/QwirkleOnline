package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Tile;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class PlayerHandsResponse extends Message {
	public static final int uniqueID = 426;
	private Map<Client, Collection<Tile>> hands;

	public PlayerHandsResponse(Map<Client, Collection<Tile>> hands) {
		super(426);
		this.hands = hands;
	}

	public Map<Client, Collection<Tile>> getHands() {
		return hands;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PlayerHandsResponse)) {
			return false;
		}
		PlayerHandsResponse that = (PlayerHandsResponse) o;
		return Objects.equals(getHands(), that.getHands());
	}
}
