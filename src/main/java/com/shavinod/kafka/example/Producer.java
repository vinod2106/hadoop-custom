package com.shavinod.kafka.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.google.common.io.Resources;

public class Producer {
	public static void main(String[] args) throws IOException {

		// Read properties file and set producer
		KafkaProducer<String, String> producer;

		try (InputStream props = Resources.getResource("producer.props").openStream()) {
			Properties properties = new Properties();
			properties.load(props);
			producer = new KafkaProducer<>(properties);

		}

		try {

			for (int i = 0; i < 10000; i++) {

				// send messages to the producer
				producer.send(new ProducerRecord<String, String>("fast-messages",
						String.format("{\"type\":\"marker\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));

				producer.send(new ProducerRecord<String, String>("summary-markers",
						String.format("{\"type\":\"other\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));
				producer.flush();
				System.out.println("Sent msg number : " + i + "  | Producer :" + producer.toString());

			}

		}

		catch (Throwable throwable) {
			// TODO: handle exception
			System.out.println(throwable.getStackTrace());
		} finally {
			// TODO: handle finally clause
			producer.close();
		}

	}
}
