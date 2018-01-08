A simple MQTT Paho Client Demo

Instructions:

1. Point to your MQTT Broker

Edit Line #36 on file ./src/main/java/com/mahaswami/Vertx2Paho.java

2. Adjust MQTT Broker Port number in the above file in case it is not using the standard 1883.

3. Compile and run the package as below

mvn package exec:java

The above should run the Demo client and send on Sample MQTT Message to the Broker. Because of the subscription, it should receive the same message back to the Demo client and displayed as well.
