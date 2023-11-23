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
	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private int port;
	private NetClient client;
	private String ip;
	private boolean running = true;
	private Parser parser = null;
	private ConnectionState connectionState = ConnectionState.NOT_STARTED;

	/**
	 * @param port
	 * @param client reference to the Client that runs the thread
	 * @param ip
	 */
	public NetworkClient(int port, NetClient client, String iP) {
		super();
		this.port = port;
		this.client = client;
		this.ip = iP;
		this.parser = new Parser();
	}



	/**
	 * The acutal Thread.
	 */
	public void run() {
		connectionState = ConnectionState.CONNECTION_BUILDING;
		System.out.println("Starting run, building connection...");
		try {
			SocketAddress address = new InetSocketAddress(ip, port);
			socket = new Socket();
			socket.setSoTimeout(2000);
			socket.setKeepAlive(true);
			socket.connect(address, 2000);
			
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));			
			
		} catch(SocketTimeoutException e) {
			System.err.println("Could not connect to host!");
			connectionState = ConnectionState.CONNECTION_FAILED;
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host:" + ip);
			connectionState = ConnectionState.CONNECTION_FAILED;
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + ip);
			connectionState = ConnectionState.CONNECTION_FAILED;
		} 
		String fromServer;
		if (connectionState != ConnectionState.CONNECTION_FAILED) {
			System.out.println("Connection successful...");
			connectionState = ConnectionState.CONNECTION_SUCCESSFULL;
			while (running) {
				try {
					if(in.ready() && (fromServer = in.readLine()) != null){
                        if(!fromServer.equals("")){
                            client.addToMsgQueue(fromServer);
                        }
                    }
				} catch (ParsingException e) {
					out.println(
							parser.serialize(new ParsingError("Die letzte Nachricht konnte nicht gelesen werden.", 0)));
					System.out.println("Could not parsing message.");
				} catch(IOException e) {
					System.out.println("There is a problem with socket: " + e.getMessage());
				}			
				
				try {
					Thread.sleep(1);
				} catch(InterruptedException ex) {
					System.out.println("Could not sleep in NetworkClient...");
				}
				
			}
			
			try {
				if(out != null){
	                out.close();
	            }

	            if(in != null){
	                in.close();
	            }

	            if(socket != null){
	            	socket.close();
	            }
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("Closing connection...");
			connectionState = ConnectionState.CONNECTION_CLOSED;
		}
	}
	
	/**
	 * Returns the actual connection state.
	 * 
	 * @return Actual connection state.
	 */
	public synchronized ConnectionState getConnectionState() {
		return connectionState;
	}

	/**
	 * Disconnects socket from server.
	 */
	public synchronized void disconnect() {
		this.running = false;
	}

	/**
	 * Sends message to server. Message should be parsed by {@link Parser}.
	 * 
	 * @param msg Message as JSON.
	 */
	public synchronized void sendMsg(String msg) {
		out.println(msg);
		out.flush();

	}
}
