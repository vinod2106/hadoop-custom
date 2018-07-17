package com.shavinod.java.patterns;

public class Messenger {

	private static Messenger messenger;

	private Messenger() {
		// TODO Auto-generated constructor stub
	}

	public synchronized static Messenger getInstance() {

		if (messenger == null) {
			messenger = new Messenger();
		}

		return messenger;
	}

	public void send(String message) {
		// TODO Auto-generated method stub

		System.out.println("Message sent");

	}
}
