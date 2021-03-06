package spiellogik;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

/**
 *
 * @author Tobias & Basti
 */
public class Doppelkopf {
	public static int clientCount = 0;
	public static Spieler[] spielerDaten;
	private static EntityManagerFactory factory;
	private static EntityManager em;
	private static Save save;
	private static MqttClient doppelkopfclient;

	// public Doppelkopf() throws MqttException {
	//
	// }

	public static void main(String[] args) throws MqttException {
		spielerDaten = new Spieler[4];
		save = new Save();
		doppelkopfclient = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
		doppelkopfclient.setCallback(new MqttCallback() {
			@Override
			public void connectionLost(Throwable cause) {
				System.out.println(cause);
			}

			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				String m = new String(message.getPayload());
				String mtype = m.split("&")[0].split("=")[1];
				if (mtype.equals("connect")) {
					System.out.println("new player joined ");

					String id = m.split("&")[1].split("=")[1];
					String name = m.split("&")[2].split("=")[1];
					Spieler neuerSpieler = new Spieler(id, name);
					System.out.print("Name: " + name + " ID: " + id + "\n");
					spielerDaten[clientCount] = neuerSpieler;
					clientCount++;
					checkPlayerCount();
				} else if (mtype.equals("getstats")) {
					getStats(m);
				}

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
		System.out.println("checkPlayerCount");
		if (clientCount == 4) {
			new Spielbrett(spielerDaten);
			doppelkopfclient.disconnect();
			doppelkopfclient.close();
		} else {
			MqttMessage response = new MqttMessage();
			Integer fehlen = 4 - clientCount;
			String messageString = "Es fehlen noch " + fehlen + " Spieler." + "\n" + "Bitte keine Panik!";
			response.setPayload(messageString.getBytes());

			doppelkopfclient.publish("allData", response);
		}
	}

	public static void getStats(String m) throws MqttPersistenceException, MqttException {
		System.out.println("getStats");
		String id = m.split("&")[1].split("=")[1];
		String name = m.split("&")[2].split("=")[1];
		String stats = save.getGames(name);
		String anzahl = "";
		String gewonnen = "";
		String punkte = "";
		if (stats.split("&")[0].split("=")[1].length() > 0) {
			anzahl = stats.split("&")[0].split("=")[1];
			gewonnen = stats.split("&")[1].split("=")[1];
			punkte = stats.split("&")[2].split("=")[1];
		}

		String messageString = "mtype=stats&a=" + anzahl + "&g=" + gewonnen + "&p=" + punkte;
		MqttMessage response = new MqttMessage();
		response.setPayload(messageString.getBytes());
		doppelkopfclient.publish("an=" + id, response);
	}

}
