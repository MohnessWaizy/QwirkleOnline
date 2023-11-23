package org.NetworkInterface;

import java.net.*;
import java.nio.charset.StandardCharsets;

import de.upb.swtpra1819interface.messages.ParsingError;
import de.upb.swtpra1819interface.parser.Parser;
import de.upb.swtpra1819interface.parser.ParsingException;

import java.io.*;

/**
 * 
 * Thread that represents one connection with one client
 *
 */
public class MultiServerThread extends Thread {
	private Socket socket = null;
	private NetworkServer server = null;
	private int clientId = 0;
	private Parser parser;
	private PrintWriter out;

	private volatile boolean running = true;

	/**
	 * @param socket
	 *            The socket that should be managed
	 * @param server
	 *            The Server that runs the thread
	 * @param clientId
	 *            The id of the client that is connected
	 */
	public MultiServerThread(Socket socket, NetworkServer server, int clientId) {
		super("MultiServer Thread");
		parser = new Parser();
		this.socket = socket;
		this.server = server;
		this.clientId = clientId;
	}

	/**
	 * @return whether the thread is running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running
	 *            whether the thread is running
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * @param msg
	 *            Message that should be transmitted
	 */
	public synchronized void sendMessage(String msg) {
		out.println(msg);
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {

		try {
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null && running) {
				// do something with input and return something to client!
				if (!inputLine.equals("")) {
					try {
						server.addToMsgQueue(inputLine, this.clientId);
					} catch (ParsingException e) {
						out.println(parser
								.serialize(new ParsingError("Die letzte Nachricht konnte nicht gelesen werden.", 0)));
					}
				}

			}
			if (running) {

				server.clientDisconnected(clientId);
			}

			out.close();
			in.close();
			socket.close();
		} catch (SocketTimeoutException ex) {
			// handle Timeout;do the same as shutdown
			server.clientDisconnected(clientId);
		} catch (SocketException ex) {
			server.clientDisconnected(clientId);
		} catch (IOException e) {
		}
	}
}