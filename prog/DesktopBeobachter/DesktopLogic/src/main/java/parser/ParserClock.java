package parser;
import model.Clock;

public class ParserClock extends Clock{

	/**
	 * The parser where clock will call {@link getAllQueueMessages()}.
	 */
	InterfaceParser parser;
	
	/**
	 * Instantiate the {@link ParserClock}.
	 * 
	 * @param cycle milliseconds
	 * @param parser The parser where clock will call {@link getAllQueueMessages()}
	 */
	public ParserClock(long cycle, InterfaceParser parser) {
		// clock is used for javafx events!
		super(cycle, true);
		this.parser = parser;
	}

	
	@Override
	public void timeElapsed() {
		parser.getAllQueueMessages();
	}

}
