package spiellogik;

import javax.swing.JFrame;

import org.eclipse.paho.client.mqttv3.MqttException;

public class PWMediator {

	private DoppelkopfBrowser browser;
	private String name;
	private Spielerclient spieler;

	public PWMediator() {
		this.browser = new DoppelkopfBrowser(this);

	}

	public void getStats(String name) {
		this.name = name;
		Save save = new Save();
		String stats = save.getGames(name);
		String anzahl = "";
		String gewonnen = "";
		String punkte = "";
		if (stats.split("&")[0].split("=")[1].length() > 0) {
			anzahl = stats.split("&")[0].split("=")[1];
			gewonnen = stats.split("&")[1].split("=")[1];
			punkte = stats.split("&")[2].split("=")[1];
		}

		browser.createStatsFenster(anzahl, gewonnen, punkte);
	}

	public void createSpieler() {
		try {
			browser.createSpielFenster();
			spieler = new Spielerclient(name, this);
			browser.createHand();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void showCards(String message) {
		String[] karten = new String[12];
		for (int i = 0; i<12; i++) {
			karten[i] = message.split("&")[i];
		}

		browser.updateHand(karten);
	}
}
