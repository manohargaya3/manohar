package com.manohar.multiprocess.player;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * Serializable Player class which has name, messageCount, message variables.
 * 
 * @author manohar
 *
 */
public class Player implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String name;
	private AtomicInteger messageCount;
	private String message;

	public Player(String name, AtomicInteger messageCount, String message) {
		this.name = name;
		this.messageCount = messageCount;
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public AtomicInteger getMessageCount() {
		return messageCount;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * This method increases messageCount by 1.
	 */
	public void increaseMessageCount() {
		this.messageCount.incrementAndGet();
	}

	/**
	 * method getResponseFor() prepares the reply of incoming message and increases the message count.
	 * @param sender
	 */
	public void getResponseFor(Player sender) {
		String m = sender.getMessage() + " " + this.getMessageCount();
		increaseMessageCount();
		this.setMessage(m);
	}
	public void printThreadIDAndProcessID() {
		System.out.println(
				"Current Thread ID: "
						+ Thread.currentThread().getId());

		String processName = ManagementFactory.getRuntimeMXBean().getName();
		long processId = Long.parseLong(processName.split("@")[0]);
		System.out.println("Process ID: " + processId);
	}
}
