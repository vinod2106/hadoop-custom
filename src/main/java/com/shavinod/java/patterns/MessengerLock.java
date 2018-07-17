package com.shavinod.java.patterns;

public class MessengerLock {

	private static final Object lock = new Object();
	private static volatile MessengerLock messenger;

	private MessengerLock() {
	}

	public static MessengerLock getInstance() {

		if (messenger == null) {
			synchronized (lock) {
				if (messenger == null) {
					messenger = new MessengerLock();
				}
			}
		}

		return messenger;
	}

	public void send(String message) {
		System.out.println(message);
	}

}