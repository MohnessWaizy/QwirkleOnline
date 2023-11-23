package org.NetworkInterface;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MultiServer implements Runnable {
	private HashMap<Long, MultiServerThread> idMap = null;
	private long idCount = 0;
	private NetworkServer server;
	private boolean listening = true;
	private ServerSocket serverSocket = null;
	short port;

	/**
	 * @param port   Port that should be opened.
	 * @param server The Server that runs this thread
	 */
	public MultiServer(short port, NetworkServer server) {
		idMap = new HashMap<Long, MultiServerThread>();

		this.server = server;
		this.port = port;
	}

	/**
	 * closes threads if needed
	 */
	private void update() {
		Iterator<Entry<Long, MultiServerThread>> it = idMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Long, MultiServerThread> pair = (Map.Entry<Long, MultiServerThread>) it.next();
			if (pair.getValue().getIsClosed() == Closed.closed.WAITFORJOIN) {
				pair.getValue().setIsClosed(Closed.closed.TRUE);
				try {
					pair.getValue().join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				idMap.remove(pair.getKey());
			}
		}
	}

	/**
	 * @return clientId of new client
	 */
	private long getNewId() {
		++idCount;
		return idCount;
	}

	/**
	 * @param msg Message that should be transmitted
	 * @param id  clientId of the receiver
	 */
	public synchronized void sendMsg(String msg, long id) {
		if (idMap.get(id) != null) {
			idMap.get(id).sendMessage(msg);
		}
	}

	/**
	 * @param msg Message that should be transmitted Send a the message to all
	 *            clients
	 */
	public synchronized void sendMsg(String msg) {
		Iterator<Entry<Long, MultiServerThread>> it = idMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Long, MultiServerThread> pair = (Map.Entry<Long, MultiServerThread>) it.next();
			pair.getValue().sendMessage(msg);
		}
	}

	/**
	 * @param clientId Client that should be kicked
	 * @return boolean value if kick was successful
	 */
	public synchronized boolean kick(long clientId) {
		try {
			idMap.get(clientId).setIsClosed(Closed.closed.TRUE);
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
			System.err.println("Could not listen on port: " + port + "...");
			System.exit(-1);
		}

		while (listening) {
			MultiServerThread thread;
			try {
				long id = getNewId();
				thread = new MultiServerThread(serverSocket.accept(), server, id);
				idMap.put(id, thread);
				thread.start();
			} catch (SocketTimeoutException e) {
				// do nothing this behavior is right. This exception is thrown if no new
				// connection is opened in 100ms. Only way to not get stuck until a new client
				// connects.
			} catch (IOException e) {
				e.printStackTrace();
			}
			update();

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
	}

}