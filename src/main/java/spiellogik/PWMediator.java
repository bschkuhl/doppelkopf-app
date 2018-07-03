package spiellogik;

import javax.swing.JFrame;

import org.eclipse.paho.client.mqttv3.MqttException;

public class PWMediator {

	private DoppelkopfBrowser browser;
	private String name;
	private Spielerclient spieler;
	private boolean darfSpielen = false;

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
		for (int i = 0; i < 12; i++) {
			karten[i] = message.split("&")[i];
		}

		browser.updateHand(karten);
	}
	
	public void showAblage(String message) {


			Integer klength = message.split("=")[2].split("&").length;

			String[] karten = new String[klength];
			for (int i = 0; i < klength; i++) {
				
				karten[i] = message.split("&")[i];
			}

			browser.updateStapel(karten);


		
	}

	public void connectSpieler() throws MqttException {
		browser.createSpielFenster();
		browser.createHand();
		Spielerclient.searchGame();

	}
	
	public void yourTurn() {
		darfSpielen = true;
		//AUSGABE "DEIN ZUG"
		browser.infobox("Spielen Sie eine Karte.");
	}

	public void karteSpielen(String a) {
		if (darfSpielen) {
			spieler.clientKarteSpielen(a);
			darfSpielen = false;
		}
	}

	public void setInfoText(String m) {
		browser.infobox(m);
	}
}
