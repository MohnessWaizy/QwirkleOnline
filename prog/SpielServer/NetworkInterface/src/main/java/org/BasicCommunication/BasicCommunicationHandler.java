package org.BasicCommunication;

import java.util.concurrent.ConcurrentLinkedQueue;
import org.NetworkInterface.MessageWithClientId;

/**
 * 
 * Class that is used as a base class for the Lobby and the CommunicationHandler
 *
 */
public abstract class BasicCommunicationHandler implements Runnable {

	protected ConcurrentLinkedQueue<MessageWithClientId> queue;
	private volatile boolean canReceive = true;

	public BasicCommunicationHandler() {
		this.queue = new ConcurrentLinkedQueue<MessageWithClientId>();
	}

	public MessageWithClientId pollNextMessage() {
		return queue.poll();
	}

	public synchronized void addToMessageQueue(MessageWithClientId msg) {
		if (msg != null) {
			queue.offer(msg);
		}
	}

	public boolean canReceiveMessage() {
		return canReceive;
	}

	public void shutdown() {
		this.canReceive = false;
	}
}
