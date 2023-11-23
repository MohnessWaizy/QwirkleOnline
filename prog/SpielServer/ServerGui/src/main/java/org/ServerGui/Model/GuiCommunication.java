package org.ServerGui.Model;

import java.util.concurrent.ConcurrentLinkedQueue;


import de.upb.swtpra1819interface.messages.Message;

public class GuiCommunication{
	protected ConcurrentLinkedQueue<Message> queue;

	public GuiCommunication() {
		this.queue = new ConcurrentLinkedQueue<Message>();
	}
	
	public Message pollNextMessage() {
		return queue.poll();
	}

	public synchronized void addToMessageQueue(Message msg) {
		if (msg != null) {
			queue.offer(msg);
		}
	}
}
