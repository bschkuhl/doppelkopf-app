package spiellogik;

import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Spielerclient {

	private static String clientId = "";

	private static String name = "";
	private static PWMediator pwm;
    private static MqttClient client;
	public Spielerclient(String name, PWMediator pwm) throws MqttException {
		Spielerclient.pwm = pwm;
		MqttClient spielerclient = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());

		spielerclient.setCallback(new MqttCallback() {
			@Override
			public void connectionLost(Throwable cause) {
				System.out.println(cause);
			}

			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				String messageContent = "";
				if (topic.equals("allData")) {
					String m = new String(message.getPayload());
					setInfo(m);

				} else {
					String m = new String(message.getPayload());
					String messageType = m.split("&")[0].split("=")[1];
					if (m.split("&")[1].split("=").length > 1) {
						messageContent = m.split("&")[1].split("=")[1];
					} else {
						messageContent = "";
					}

					if (messageType.equals("karteSpielen")) {
						karteSpielen();

					} else if (messageType.equals("karte")) {

					} else if (messageType.equals("nachricht")) {
						setInfo(messageContent);
					} else if (messageType.equals("stats")) {
						sendStats(m);
					} else if (messageType.equals("blattAnzeigen")) {
						messageContent = m.split("=")[2];
						Spielerclient.pwm.showCards(messageContent);
					} else if (messageType.equals("stapel")) {
						if (m.split("=").length > 2) {
							messageContent = m.split("=")[2];
							Spielerclient.pwm.showAblage(messageContent);
						}
					}else if (messageType.equals("disconnet")) {
						disconnectClient();
					} 
				}

			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
			}
		});

		MqttConnectOptions opts = new MqttConnectOptions();
		opts.setKeepAliveInterval(300);
		spielerclient.connect(opts);

		spielerclient.subscribe("allData");

		clientId = spielerclient.getClientId();
		Spielerclient.name = name;
		spielerclient.subscribe("an=" + clientId);
		String messageString = "mtype=getstats&ID=" + clientId + "&name=" + name;
		MqttMessage message = new MqttMessage();
		message.setPayload(messageString.getBytes());
		spielerclient.publish("spielerData", message);

	}

	public static void publishData(String channel, String data) {
		System.out.println("client publishData");
		try {
			String messageString = data;
			client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
			client.connect();
			MqttMessage message = new MqttMessage();
			message.setPayload(messageString.getBytes());
			client.publish(channel, message);
			client.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

	public static void clientKarteSpielen(String a) {
		System.out.println("clientKarteSpielen");
		// pwm.karteSpielen();

		// int n = reader.nextInt();
		String m = "mtype=karte&mcontent=" + a;
		publishData("von=" + clientId, m);
	}

	public static void searchGame() throws MqttException {
		System.out.println("searchGame");
		String message = "mtype=connect&ID=" + clientId + "&name=" + Spielerclient.name;
		Spielerclient.publishData("spielerData", message);
	}

	public static void sendStats(String m) {
		System.out.println("sendStats");
		pwm.createStats(m);
	}

	public static String getClientId() {
		System.out.println("getClientId");
		return clientId;
	}

	public static void setClientId(String clientId) {
		System.out.println("setClientId");
		Spielerclient.clientId = clientId;
	}

	public static void karteSpielen() {
		System.out.println("karteSpielen");
		pwm.yourTurn();
	}

	private void setInfo(String m) {
		System.out.println("setInfo");
		pwm.setInfoText(m);
	}
	
	public static void disconnectClient() throws MqttException {
		client.close();
	}

}
