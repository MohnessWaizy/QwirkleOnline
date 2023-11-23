package org.NetworkInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * Server backend that manages the sockets
 *
 */
public class MultiServer implements Runnable {
	private HashMap<Integer, MultiServerThread> idMap = null;
	private static int idCount = 0;
	private NetworkServer server;
	private boolean listening = true;
	private ServerSocket serverSocket = null;
	int port;

	/**
	 * @param port   Port that should be opened.
	 * @param server The Server that runs this thread
	 */
	public MultiServer(int port, NetworkServer server) {
		idMap = new HashMap<Integer, MultiServerThread>();

		this.server = server;
		this.port = port;
	}

	/**
	 * 
	 * Sends a message to a specified client
	 * 
	 * @param msg Message that should be transmitted
	 * @param id  clientId of the receiver
	 */
	public synchronized void sendMsg(String msg, int id) {
		if (idMap.get(id) != null) {
			idMap.get(id).sendMessage(msg);
		}
	}

	/**
	 * 
	 * Sends a message to all clients
	 * 
	 * @param msg Message that should be transmitted Send a the message to all
	 *            clients
	 */
	public synchronized void sendMsg(String msg) {
		Iterator<Entry<Integer, MultiServerThread>> it = idMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, MultiServerThread> pair = (Map.Entry<Integer, MultiServerThread>) it.next();
			pair.getValue().sendMessage(msg);
		}
	}

	/**
	 * 
	 * Kicks a client from the server
	 * 
	 * @param clientId Client that should be kicked
	 * @return boolean value if kick was successful
	 */
	public synchronized boolean kick(int clientId) {
		try {
			idMap.get(clientId).setRunning(false);
			idMap.get(clientId).join();
			idMap.remove(clientId);
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(100);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		while (listening) {
			MultiServerThread thread;
			try {
				if (idCount == -1) {
					throw new RuntimeException("Client overflow");
				}
				server.getKickMap().put(idCount, 0);
				thread = new MultiServerThread(serverSocket.accept(), server, idCount);
				idMap.put(idCount, thread);
				idCount++;
				thread.start();
				thread.setName("SocketThread_client" + idCount);
			} catch (SocketTimeoutException e) {
				// do nothing this behavior is right. This exception is thrown if no new
				// connection is opened in 10ms. Only way to not get stuck until a new client
				// connects.
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * shuts the server down
	 */
	public synchronized void shutdown() {
		this.listening = false;
		Iterator<Entry<Integer, MultiServerThread>> it = idMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, MultiServerThread> pair = (Map.Entry<Integer, MultiServerThread>) it.next();
			pair.getValue().setRunning(false);
			try {
				pair.getValue().join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}