Create a custom Hbase Serializer.

#Run zookeeper 
mvn compile exec:java -Dexec.mainClass="com.shavinod.zookeeper.sample.ZooApp"

#hard reset git
git reset --hard

#Run Kafka
mvn compile exec:java -Dexec.mainClass="com.shavinod.kafka.example.Run" -Dexec.args="producer"
mvn compile exec:java -Dexec.mainClass="com.shavinod.kafka.example.Run" -Dexec.args="consumer"
