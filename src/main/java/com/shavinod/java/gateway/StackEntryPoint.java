package com.shavinod.java.gateway;

import java.util.logging.Level;
import java.util.logging.Logger;

import py4j.GatewayServer;

public class StackEntryPoint {

	private Stack stack;

	public StackEntryPoint() {
		stack = new Stack();
		stack.push("Initial Item");
	}

	public Stack getStack() {
		return stack;
	}

	public static void main(String[] args) {
		int port = 25333;
		GatewayServer gatewayServer = new GatewayServer(new StackEntryPoint(), port);
		GatewayServer.turnLoggingOn();
		Logger logger = Logger.getLogger("py4j");
		logger.setLevel(Level.ALL);

		gatewayServer.start();
		logger.info(" ************* Started Logging *********************");
		System.out.println("Gateway Server Started on port " + port);
	}

}