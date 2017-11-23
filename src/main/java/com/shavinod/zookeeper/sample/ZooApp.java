package com.shavinod.zookeeper.sample;

import java.util.ArrayList;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

public class ZooApp {

	private final static int maxRetries = 3;
	private final static int baseSleepTimeMs = 1000;
	private final static String connectString = "127.0.0.1:2181";
	private final static String configPath = "/tmp/zookeeper";

	public static void main(String[] args) throws Exception {
		final RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
		final CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);

		client.start();

		final Watcher watcher = new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				// TODO Auto-generated method stub
				try {
					GetDataBuilder getDataBuilder = client.getData();
					byte[] configBytes = getDataBuilder.forPath(configPath);
					String config = new String(configBytes);
					System.out.println(event.getType() + " , " + config);

					client.getData().usingWatcher(this).forPath(configPath);

				} catch (Exception ex) {
					// TODO: handle exception
					System.out.println(ex);
				}

			}
		};

		Stat stat = client.checkExists().forPath(configPath);

		if (stat != null) {
			System.out.println("Found node: " + stat);
			GetDataBuilder getDataBuilder = client.getData();
			byte[] configBytes = getDataBuilder.forPath(configPath);
			String config = new String(configBytes);
			System.out.println("Data: " + config);

			client.setData().forPath(configPath, "Hello from Java.".getBytes());

		} else {

			ArrayList<ACL> acls = new ArrayList<>();
			acls.add(new ACL(Perms.ALL, new Id("ip", "127.0.0.1")));
			client.create().creatingParentsIfNeeded().withACL(acls).forPath(configPath, "Hello from Java.".getBytes());

			System.out.println("Created node: " + configPath);
		}

		// create the first watcher
		client.getData().usingWatcher(watcher).forPath(configPath);

		// wait and listen too changes
		Thread.sleep(300 * 1000);
	}
}
