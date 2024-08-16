package com.manohar;

import java.net.ConnectException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.manohar.api.Client;
import com.manohar.api.Server;
import com.manohar.singleprocess.InitiatorPlayer;
import com.manohar.singleprocess.PlayerRunnable;
import com.manohar.util.Constants;

/**
 * 
 * Start point of the application.
 * 
 * @author manohar
 *
 */
public class App {

	/**
	 * The application first tries to connect to a server, If there is not a
	 * published server, then Client() class throw a ConnectException which is
	 * cathed by try-catch block to initialize a server.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		App app = new App();
		try {

			synchronized (app){
				app.executeSolutionInSingleProcess();
				Thread.currentThread().sleep(20000);
				print("delimit --------------------------------------------------------------");
				app.executeSolutionInMultiProcess();
			}
		} catch (Exception e) {
			print(e.getMessage());
		}
	}

	private void executeSolutionInMultiProcess() {
		try {
			startClient();
		} catch (ConnectException e) {
			startServer();
		}
	}

	private void executeSolutionInSingleProcess() {
		print("have every player in a single JAVA process (same PID)");

		BlockingQueue<String> firstToSecond = new ArrayBlockingQueue<>(Constants.messageCount);
		BlockingQueue<String> secondToFirst = new ArrayBlockingQueue<String>(Constants.messageCount);

		// Both players use the same queues symmetrically.
		InitiatorPlayer firstPlayer = new InitiatorPlayer(firstToSecond, secondToFirst);
		print("main player");
		firstPlayer.printThreadIDAndProcessID();
		PlayerRunnable secondPlayer = new PlayerRunnable(secondToFirst, firstToSecond);
		print("initiator player");
		secondPlayer.printThreadIDAndProcessID();

		// Please note that we can start threads in reverse order. But thankfully to
		// blocking queues the second player will wait for initialization message from
		// the first player.
		new Thread(secondPlayer).start();
		new Thread(firstPlayer).start();
	}

	/**
	 * Method to create a Client() instance.
	 * 
	 * @throws ConnectException
	 */
	private static void startClient() throws ConnectException {

		print("Looking for a server");

		//player instances have different PID
		print("have every player in a separate JAVA process (different PID)");
		try {
			new Client();
		} catch (ConnectException e) {
			throw new ConnectException();
		}
	}

	/**
	 * Method to create a Server() instance.
	 * 
	 */

	private static void startServer() {
		print("Server could not found");
		print("Initializing a Server");
		new Server();
	}

	/**
	 * Instead of writing System.out.println, this is a simplified solution It gets
	 * the value for the given key from the bundle.
	 * 
	 * @param str
	 */
	private static void print(String str) {
		System.out.println(str);
	}
}
