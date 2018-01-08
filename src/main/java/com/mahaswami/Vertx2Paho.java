

package com.mahaswami;

import java.io.IOException;
import java.sql.Timestamp;

import java.awt.image.*;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.ByteArrayInputStream;
import sun.misc.BASE64Decoder;
import javax.imageio.*;

public class Vertx2Paho implements MqttCallback {

	public static void main(String[] args) {

		System.out.println("started 1");

		int qos 			= 2;
		String broker 		= "192.168.0.177";
		int port 			= 1883;
		String clientId 	= null;
		boolean cleanSession = false;			// Non durable subscriptions
		boolean ssl = false;
		String password = "f104fc22ae19d7fcaa8b0e66c9721d54fe108a771e0e06f075faa31ff4807032ed726127f214dfff12c48e4465e4bc8d966b2924c8e58d9fd205c09cf81dcd46";
		String userName = "seeitsendit+sasi@gmail.com";
		
		String protocol = "tcp://";

		if (ssl) {
		  protocol = "ssl://";
		}

		String url = protocol + broker + ":" + port;

		if (clientId == null || clientId.equals("")) {
			clientId = "SampleJavaV3_"  + System.currentTimeMillis();
		}

		System.out.println("url = " + url);

		try {
			Vertx2Paho sampleClient = new Vertx2Paho(url, clientId, cleanSession, userName, password);
			sampleClient.subscribe(qos);
					
		} catch(MqttException me) {
			// Display full details of any exception that occurs
			System.out.println("reason "+me.getReasonCode());
			System.out.println("msg "+me.getMessage());
			System.out.println("loc "+me.getLocalizedMessage());
			System.out.println("cause "+me.getCause());
			System.out.println("excep "+me);
			me.printStackTrace();
		}
	}


	// Private instance variables
	private MqttClient 			client;
	private String 				brokerUrl;
	private MqttConnectOptions 	conOpt;
	private boolean 			clean;
	private String password;
	private String userName;


    public Vertx2Paho(String brokerUrl, String clientId, boolean cleanSession, String userName, String password) throws MqttException {
    	this.brokerUrl = brokerUrl;
    	this.clean 	   = cleanSession;
    	this.password = password;
    	this.userName = userName;
    	MemoryPersistence dataStore = new MemoryPersistence();

    	try {
	    	conOpt = new MqttConnectOptions();
	    	conOpt.setCleanSession(clean);
	    	if(password != null ) {
	    	  conOpt.setPassword(this.password.toCharArray());
	    	}
	    	if(userName != null) {
	    	  conOpt.setUserName(this.userName);
	    	}

    		// Construct an MQTT blocking mode client
			client = new MqttClient(this.brokerUrl,clientId, dataStore);

			// Set this wrapper as the callback handler
	    	client.setCallback(this);

		} catch (MqttException e) {
			e.printStackTrace();
			log("Unable to set up client: "+e.toString());
			System.exit(1);
		}
    }

    public void subscribe(int qos) throws MqttException {
		log("in subscribe");

		// Connect to the MQTT server
		client.connect(conOpt);
		log("Connected to "+brokerUrl+" with client ID "+client.getClientId());
		client.subscribe("my_app/report/#", qos);

		MqttMessage message1 = new MqttMessage("Hello From Paho(**): multi level my_app message".getBytes());
		message1.setQos(qos);
		client.publish("my_app/report/new", message1);

		// Continue waiting for messages until the Enter is pressed
		log("Press <Enter> to exit");
		try {
		System.in.read();
		} catch (IOException e) {
		//If we can't read we'll just exit
		}

		// Disconnect the client from the server
		client.disconnect();
		log("Disconnected");
    }

    private void log(String message) {
		System.out.println(message);
    }

	/****************************************************************/
	/* Methods to implement the MqttCallback interface              */
	/****************************************************************/

    /**
     * @see MqttCallback#connectionLost(Throwable)
     */
	public void connectionLost(Throwable cause) {
		log("Connection to " + brokerUrl + " lost!" + cause);
		System.exit(1);
	}

    /**
     * @see MqttCallback#deliveryComplete(IMqttDeliveryToken)
     */
	public void deliveryComplete(IMqttDeliveryToken token) {

	}


    /**
     * @see MqttCallback#messageArrived(String, MqttMessage)
     */


	public void messageArrived(String topic, MqttMessage message) throws MqttException {

		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		String mess = new String(message.getPayload());
		System.out.println( mess);

	}
 

}
