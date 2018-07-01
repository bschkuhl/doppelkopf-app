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

	public Spielerclient(String name, PWMediator pwm) throws MqttException {
		Spielerclient.pwm = pwm;
		// Scanner reader = new Scanner(System.in);
		// String name = "";
		// System.out.println("Bitte geben Sie ihren Nutzernamen ein:");
		// name = reader.nextLine();
		// System.out.println("Hallo " + name + " möchten Sie jetzt zum Doppelkopfserver
		// verbinden? (y/n)");

		// String connect = reader.nextLine();

		// switch (connect) {
		// case "y":

		MqttClient spielerclient = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());

		spielerclient.setCallback(new MqttCallback() {
			@Override
			public void connectionLost(Throwable cause) {
				System.out.println(cause);
			}

			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				if (topic.equals("allData")) {
					System.out.println(new String(message.getPayload()));
				} else {
					String m = new String(message.getPayload());
					String messageType = m.split("&")[0].split("=")[1];
					String messageContent = m.split("&")[1].split("=")[1];

					if (messageType.equals("karteSpielen")) {
						System.out.println(messageContent);
						clientKarteSpielen();
					} else if (messageType.equals("karte")) {

					} else if (messageType.equals("nachricht")) {
						System.out.println(messageContent);
					}  else if (messageType.equals("stats")){
						sendStats(m);
					}
					else {
						Spielerclient.pwm.showCards(messageContent);
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

		// break;
		// case "n":
		// System.out.println("Okay!");
		// break;
		//
		// default:
		// System.out.println("Hm?");
		// break;
		// }

	}

	public static void publishData(String channel, String data) throws MqttException {

		String messageString = data;
		MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
		client.connect();
		MqttMessage message = new MqttMessage();
		message.setPayload(messageString.getBytes());
		client.publish(channel, message);
		client.disconnect();
	}

	public static void clientKarteSpielen() throws MqttException {
		Scanner reader = new Scanner(System.in);
		int n = reader.nextInt();
		String m = "mtype=karte&mcontent=" + n;
		publishData("von=" + clientId, m);
	}
	
	public static void searchGame() throws MqttException {
		String message = "mtype=connect&ID=" + clientId + "&name=" + Spielerclient.name;
		Spielerclient.publishData("spielerData", message);
	}
	
	public static void sendStats(String m) {
		pwm.createStats(m);
	}
}
