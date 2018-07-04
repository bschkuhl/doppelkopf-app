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
		System.out.println("createStats");
		String anzahl = stats.split("&")[1].split("=")[1];
		String gewonnen = stats.split("&")[2].split("=")[1];
		String punkte = stats.split("&")[3].split("=")[1];

		browser.createStatsFenster(anzahl, gewonnen, punkte);
	}

	public void createSpieler(String username) {
		System.out.println("createSpieler");
		this.name = username;
		try {
			spieler = new Spielerclient(name, this);

		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void showCards(String message) {
		System.out.println("showCards");
		//0_Pik_Dame&1_Kreuz_Koenig&2_Pik_Bube&3_Kreuz_Bube&4_Herz_Koenig&5_Herz_Ass&6_Herz_Dame&7_Pik_10&8_Karo_Koenig&9_Karo_Dame&11_Kreuz_Koenig&
		Integer klength = message.split("&").length;
		String[] karten = new String[klength];
		for (int i = 0; i < klength; i++) {
			karten[i] = message.split("&")[i];
		}

		browser.updateHand(karten);
	}

	public void showAblage(String message) {
		System.out.println("showAblage");
		Integer klength = message.split("&").length;

		String[] karten = new String[klength];
		for (int i = 0; i < klength; i++) {

			karten[i] = message.split("&")[i];
		}

		browser.updateStapel(karten);

	}

	public void connectSpieler() throws MqttException {
		System.out.println("connectSpieler");
		browser.createSpielFenster();
		browser.createHand();
		Spielerclient.searchGame();

	}

	public void yourTurn() {
		System.out.println("yourTurn");
		darfSpielen = true;
		// AUSGABE "DEIN ZUG"
		browser.infobox("Spielen Sie eine Karte.");
	}

	public void karteSpielen(String a) {
		System.out.println("karteSpielen");
		if (darfSpielen) {
			spieler.clientKarteSpielen(a);
			darfSpielen = false;
		}
	}

	public void setInfoText(String m) {
		System.out.println("setInfoText");
		browser.infobox(m);
	}
}
