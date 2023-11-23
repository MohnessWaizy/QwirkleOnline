package org.NetworkInterface;
import java.net.*;

import de.upb.swtpra1819interface.messages.ParsingError;
import de.upb.swtpra1819interface.parser.Parser;
import de.upb.swtpra1819interface.parser.ParsingException;

import java.io.*;

public class MultiServerThread extends Thread {
	private Socket socket = null;
	private NetworkServer server = null;
	private long clientId = 0;
	private volatile String msg = "";
	private Parser parser;

	private volatile Closed.closed isClosed = Closed.closed.FALSE;

	/**
	 * @param socket   The socket that should be managed
	 * @param server   The Server that runs the thread
	 * @param clientId The id of the client that is connected
	 */
	public MultiServerThread(Socket socket, NetworkServer server, long clientId) {
		super("MultiServer Thread");
		parser = new Parser();
		this.socket = socket;
		this.server = server;
		this.clientId = clientId;
	}

	/**
	 * @return closed status of the thread
	 */
	public synchronized Closed.closed getIsClosed() {
		return isClosed;
	}

	/**
	 * @param isClosed new close status
	 */
	public synchronized void setIsClosed(Closed.closed isClosed) {
		this.isClosed = isClosed;
	}

	/**
	 * @param msg Message that should be transmitted
	 */
	public synchronized void sendMessage(String msg) {
		this.msg = msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {

		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String inputLine;
			out.println("Connected...");
			while ((inputLine = in.readLine()) != null && isClosed == Closed.closed.FALSE) {
				// do something with input and return something to client!
				if (!inputLine.equals("")) {
					try {
						server.addToMsgQueue(inputLine, clientId);
					} catch (ParsingException e) {
						out.println(parser
								.serialize(new ParsingError("Die letzte Nachricht konnte nicht gelesen werden.", 0)));
					}
				}

				out.println(msg);
				out.flush();
				if (!this.msg.equals("")) {
					this.msg = "";
				}
			}
			isClosed = Closed.closed.WAITFORJOIN;
			while (isClosed != Closed.closed.TRUE) {
			}
			out.close();
			in.close();
			socket.close();

		} catch (SocketTimeoutException e) {
			// handle Timeout
			// do the same as shutdown
			isClosed = Closed.closed.WAITFORJOIN;
			while (isClosed != Closed.closed.TRUE) {
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}