package com.shavinod.kafka.example;

import java.io.IOException;

public class Run {

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			throw new IllegalArgumentException("Must have consumer or produser as argument");
		}

		switch (args[0]) {
		case "producer":
			Producer.main(args);
			break;

		case "consumer":
			Consumer.main(args);
			break;

		default:
			throw new IllegalArgumentException("Don't know how to do " + args[0]);

		}

	}
}
