package main;

import client.GameClient;
import server.GameServer;
import util.IllegalPortNumberException;

// TODO: Auto-generated Javadoc
/**
 * The Class Hexagon.
 */
public class Hexagon {

	/**
	 * Starts either a GameClient or a GameServer with the given arguments.
	 * args[0] = {server, client}
	 * args[1], if args[0].equals(server) = PortNumber
	 * args[1], if args[0].equals(client) = IPAdress of the server
	 * args[2],  if args[0].equals(client) = Port
	 * 
	 * If the PortNumber or IPadress were not possible, the user is asked for it with a jPane.
	 * 
	 * @param args 	args[0] = {server, client}
	 * 				args[1], if args[0].equals(server) = PortNumber
	 * 				args[1], if args[0].equals(client) = IPAdress of the server
	 * 				args[2],  if args[0].equals(client) = Port
	 */
	public static void main(String[] args) {
		boolean GameClientCreated = false;
		boolean GameServerCreated = false;
		try {
			if (args[0].equals("server")) {
				
				
				/*
				 * Try to call Constructor with requested port number
				 */
				try {
					new GameServer(args[1]);
					GameServerCreated = true;
				}
				/*
				 * If no or too many arguments are used, program closes with
				 * instructions how to start the server
				 */
				catch (ArrayIndexOutOfBoundsException ex) {
					System.out.println("Usage:\n\t java GameServer [portnumber]");
					GameServerCreated = false;
				}
				/*
				 * If a non-integer is used as argument, program closes with error
				 */
				catch (NumberFormatException ex) {
					System.out.println("Error: Port number must be an Integer!");
					GameServerCreated = false;
				}
				/*
				 * If a negative integer is used, program closes with error
				 */
				catch (IllegalPortNumberException ex) {
					System.out.println(ex.getMessage());
					GameServerCreated = false;
				}
				if (GameServerCreated==false) {
					try {
						new GameServer();
						GameServerCreated=true;
					}
					/*
					 * If no or too many arguments are used, program closes with
					 * instructions how to start the server
					 */
					catch (ArrayIndexOutOfBoundsException ex) {
						System.out.println("Usage:\n\t java GameServer [portnumber]");
						GameServerCreated=true;
					}
					/*
					 * If a non-integer is used as argument, program closes with error
					 */
					catch (NumberFormatException ex) {
						System.out.println("Error: Port number must be an Integer!");
						GameServerCreated=true;
					}
					/*
					 * If a negative integer is used, program closes with error
					 */
					catch (IllegalPortNumberException ex) {
						System.out.println(ex.getMessage());
						GameServerCreated=true;
					}
				}
			}
			
			if (args[0].equals("client")) {
				
				try {
					String[] s = args[1].split(":");
					new GameClient(s[0], s[1]);
					GameClientCreated = true;
				}
				catch (IllegalPortNumberException ex) {
					GameClientCreated = false;
				}
				if (GameClientCreated==false) {
					try {
						new GameClient();
						GameClientCreated = true;
					}
					catch (IllegalPortNumberException ex) {
						GameClientCreated = false;
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("Add console Parameters: use GameServer to start the Server and GameClient to start a client");
			GameServerCreated = false;
		}
		if (GameClientCreated==false && GameServerCreated==false) {
			System.out.println("Console or window arguments were wrong, please restart the .jar");
		}
	}

}
