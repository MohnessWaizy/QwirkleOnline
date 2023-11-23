package parser;
import clock.Clock;

public class ParserClock extends Clock{

	InterfaceParser parser;
	public ParserClock(long cycle, InterfaceParser parser) {
		super(cycle);
		this.parser = parser;
	}

	@Override
	public void timeElapsed() {
		parser.getAllQueueMessages();
	}

}
