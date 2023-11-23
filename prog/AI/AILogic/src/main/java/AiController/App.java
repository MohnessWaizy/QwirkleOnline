package AiController;

/**
 * The application is started here. If available, the arguments are read and
 * passed. If there are no arguments, a predefined IP, port and name is passed.
 */
public class App {

	/**
	 * Create an AI object and pass the arguments, if available.
	 * 
	 * @param args IP, port and name are the optional arguments.
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			/*
			 * If there are no arguments, a predefined IP, port and name is passed.
			 */
			AI ai = new AI("localhost", 33100, "swtpra04");
		} else {
			try {
				/*
				 * Check, if there is a '-' in front of all arguments
				 */
				if (!args[0].substring(0, 4).equals("-ip=") || !args[1].substring(0, 6).equals("-port=") || !args[2].substring(0, 6).equals("-name=")) {
					System.out.println("Please enter agruments with: -ip=<address> -port=<port> -name=<name>");
					System.exit(0);
				}
				/*
				 * The arguments are read and passed
				 */
				String ip = args[0].substring(4);
				int port = Integer.parseInt(args[1].substring(6));
				String name = args[2].substring(6);

				/*
				 * Check, if port is negative
				 */
				if (port < 0) {
					System.err.println("Don't enter negative port!");
					System.exit(0);
				}

				AI ai = new AI(ip, port, name);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println("Wrong number of arguments! Please enter: -IP, -Port and -Client name");
			} catch (NumberFormatException nfe) {
				System.err.println("Second argument has to be a port!");
			}
		}
	}
}
