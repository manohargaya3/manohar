package com.manohar.api;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.manohar.multiprocess.player.Player;

/**
 * 
 * @author manohar
 *
 * BaseApi is the Abstract Class for Client and Server classes. 
 * Common objects and methods are placed in this class.
 * 
 */
public abstract class BaseApi {

	Socket socket = null;
	Player player = null;
	Player sender = null;

	/**
	 * Each side (server, client) has it's own implementation on chat. 
	 * The main difference will be the count check.
	 * @param player : This class gets a player object to get the message, messageCount etc..
	 * @throws IOException 
	 * @throws ClassNotFoundException
	 */
	abstract void chat(Player player, ObjectInputStream is, ObjectOutputStream os) throws IOException, ClassNotFoundException;

	/**
	 * This method is common for both client and server side. 
	 * Closes the streams and finalizes the application.
	 * @throws IOException
	 */
	protected void exitApplication() throws IOException {
		System.out.println("reached to the limit and program successfully executed...");
		System.exit(0);
	}

	/**
	 * Common print method.
	 * Instead of writing System.out.println everywhere, this is a simplified solution
	 * 
	 * @param s
	 */
	protected void print(String s) {
		System.out.println(s);
	}
}
