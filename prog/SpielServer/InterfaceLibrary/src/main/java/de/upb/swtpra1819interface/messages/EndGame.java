package de.upb.swtpra1819interface.messages;

import java.util.Objects;

public class EndGame extends Message {
	public static final int uniqueID = 401;

	public EndGame() {
		super(401);
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof EndGame)) {
			return false;
		}
		EndGame that = (EndGame) o;
		return Objects.equals(Integer.valueOf(getUniqueId()), Integer.valueOf(that.getUniqueId()));
	}
}
