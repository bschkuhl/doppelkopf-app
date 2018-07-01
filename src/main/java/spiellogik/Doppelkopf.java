package spiellogik;

import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


/**
 *
 * @author Tobias & Basti
 */
public class Doppelkopf {
	public static int clientCount = 0;
	public static Spieler[] spielerDaten = new Spieler[4];

	/**
	 * @param args
	 *            the command line arguments
	 * @throws MqttException
	 */
	public static void main(String[] args) throws MqttException {

		MqttClient doppelkopfclient = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());

		doppelkopfclient.setCallback(new MqttCallback() {
			@Override
			public void connectionLost(Throwable cause) {
				System.out.println(cause);
			}

			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {

				System.out.println("new player joined");

				String m = new String(message.getPayload());
				String id = m.split("&")[0].split("=")[1];
				String name = m.split("&")[1].split("=")[1];
				Spieler neuerSpieler = new Spieler(id, name);
				spielerDaten[clientCount] = neuerSpieler;
				clientCount++;
				checkPlayerCount();

			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
			}
		});
		
		MqttConnectOptions opts = new MqttConnectOptions();
		opts.setKeepAliveInterval(300);
		doppelkopfclient.connect(opts);


		doppelkopfclient.subscribe("spielerData");

	}

	public static void checkPlayerCount() throws MqttException, InterruptedException {

		if (clientCount == 4) {
			System.out.println("4 Spieler vorhanden!");

			new Spielbrett(spielerDaten);
		}
		System.out.println("Es werden noch Spieler gesucht.");
	}
}
