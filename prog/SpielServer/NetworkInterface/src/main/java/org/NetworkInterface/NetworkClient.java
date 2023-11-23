package org.NetworkInterface;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

import de.upb.swtpra1819interface.parser.*;
import de.upb.swtpra1819interface.messages.ParsingError;

/**
 * 
 * Socket of the client side of the network communication
 *
 */
public class NetworkClient implements Runnable {
	private Socket kkSocket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private int port;
	private Client client;
	private String iP;
	private boolean running = true;
	private Parser parser = null;

	/**
	 * @param port
	 * @param client reference to the Client that runs the thread
	 * @param iP
	 */
	public NetworkClient(int port, Client client, String iP) {
		super();
		this.port = port;
		this.client = client;
		this.iP = iP;
		this.parser = new Parser();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			kkSocket = new Socket(iP, port);
			out = new PrintWriter(new OutputStreamWriter(kkSocket.getOutputStream(),StandardCharsets.UTF_8), true);
			in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host:" + iP);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + iP);
			System.exit(1);
		}
		String fromServer;
		try {
			while ((fromServer = in.readLine()) != null && running) {
				try {
					if (fromServer != null && !fromServer.equals("")) {
						client.addToMsgQueue(fromServer);
					}
				} catch (ParsingException e) {
					out.println(
							parser.serialize(new ParsingError("Die letzte Nachricht konnte nicht gelesen werden.", 0)));
				}
				
			}
			out.close();
			in.close();
			kkSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * is called to disconnect from the server
	 */
	public synchronized void disconnect() {
		this.running = false;
	}

	/**
	 * 
	 * Puts a message in the queue, so it is transmitted to the server with the next cycle
	 * 
	 * @param msg Message that should be transmitted
	 */
	public synchronized void sendMsg(String msg) {
		out.println(msg);
		out.flush();

	}
}