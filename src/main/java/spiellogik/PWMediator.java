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

	public void createStats(String stats) {
		String anzahl = stats.split("&")[1].split("=")[1];
		String gewonnen = stats.split("&")[2].split("=")[1];
		String punkte = stats.split("&")[3].split("=")[1];

		browser.createStatsFenster(anzahl, gewonnen, punkte);
	}
	
	public void createSpieler(String username) {
		this.name = username;
		try {
			spieler = new Spielerclient(name, this);
			
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
	
	public void connectSpieler() throws MqttException {
		browser.createSpielFenster();
		browser.createHand();
		Spielerclient.searchGame();
		
	}
}
