package spiellogik;

import java.util.ArrayList;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author Tobias
 */
public class Spielbrett {

	public Karte[] stapel;
	public Karte[][] blatt;
	List<Karte> ablegestapel = new ArrayList<Karte>();
	List<Spieler> re = new ArrayList<Spieler>();
	List<Spieler> kontra = new ArrayList<Spieler>();
	private Spieler spieler1;
	private Spieler spieler2;
	private Spieler spieler3;
	private Spieler spieler4;
	public Spieler[] alleSpieler;
	private int currentPlayer = 0;
	private String bedienen;
	private Karte hoechsteGespielte;
	private int bekommtStich;
	private boolean weiter = false;
	private String mAnswer;
	String ausgabe = "";
	Gamedata gamedata;
	Save save;
	static MqttClient spielbrettclient;
	// Initialisiere Spielbrett, d.h. erstellen der Karten, mischen des Stapels,
	// verteilen und starten des Spiels
	public Spielbrett(Spieler[] spielerdaten) throws MqttException, InterruptedException {

		stapel = new Karte[48];
		blatt = new Karte[4][12];

		spieler1 = spielerdaten[0];
		spieler1.nummer = 1;
		spieler2 = spielerdaten[1];
		spieler2.nummer = 2;
		spieler3 = spielerdaten[2];
		spieler3.nummer = 3;
		spieler4 = spielerdaten[3];
		spieler4.nummer = 4;
		alleSpieler = spielerdaten;
		gamedata = new Gamedata(0, 0, 0, 0, "","","", "", "");
		save = new Save();
		
		spielbrettclient = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());

		spielbrettclient.setCallback(new MqttCallback() {
			@Override
			public void connectionLost(Throwable cause) {
				System.out.println(cause);
			}

			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				String m = new String(message.getPayload());
				mAnswer = m.split("&")[1].split("=")[1];
				weiter = true;
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
			}
		});
		spielbrettclient.connect();

		spielbrettclient.subscribe("von="+alleSpieler[0].getcId());
		spielbrettclient.subscribe("von="+alleSpieler[1].getcId());
		spielbrettclient.subscribe("von="+alleSpieler[2].getcId());
		spielbrettclient.subscribe("von="+alleSpieler[3].getcId());

		stapelErstellen();
		shuffle();
		verteileKarten();
		startGame();
	}

	// Erstellen des Stapels, Zuweisung von Werten (Farbe, Trumpf etc) zu den Karten
	public void stapelErstellen() {
		String color = null;
		String number = null;
		int value = 0;

		for (int i = 0; i < 48; i++) {
			boolean trumpf = true;
			int order = 14;
			if (i < 12) {
				color = "Karo";
			} else if (12 <= i && i < 24) {
				color = "Herz";
			} else if (24 <= i && i < 36) {
				color = "Pik";
			} else {
				color = "Kreuz";
			}

			int j = i % 12;
			switch (j) {
			case 0:
			case 1:
				number = "Ass";
				value = 11;
				if (!color.equals("Karo")) {
					trumpf = false;
				} else {
					order = 10;
				}
				break;
			case 2:
			case 3:
				number = "Koenig";
				value = 4;
				if (!color.equals("Karo")) {
					trumpf = false;
				} else {
					order = 12;
				}
				break;
			case 4:
			case 5:
				number = "Dame";
				value = 3;
				if (color.equals("Kreuz")) {
					order = 2;
				} else if (color.equals("Pik")) {
					order = 3;
				} else if (color.equals("Herz")) {
					order = 4;
				} else if (color.equals("Karo")) {
					order = 5;
				}
				break;
			case 6:
			case 7:
				number = "Bube";
				value = 2;
				if (color.equals("Kreuz")) {
					order = 6;
				} else if (color.equals("Pik")) {
					order = 7;
				} else if (color.equals("Herz")) {
					order = 8;
				} else if (color.equals("Karo")) {
					order = 9;
				}
				break;
			case 8:
			case 9:
				number = "10";
				value = 10;
				if (!color.equals("Karo") && !color.equals("Herz")) {
					trumpf = false;
				} else if (color.equals("Karo")) {
					order = 11;
				} else if (color.equals("Herz")) {
					order = 1;
				}
				break;
			case 10:
			case 11:
				number = "9";
				value = 0;
				if (color.equals("Karo")) {
					order = 13;
				} else {
					trumpf = false;
				}
				break;
			default:
				break;
			}

			stapel[i] = new Karte(color, number, value, trumpf, order);
		}
	}

	// Mischen des Stapels
	public void shuffle() {
		Karte tmp;
		int rand;
		Random r = new Random();
		for (int i = 0; i < stapel.length; i++) {
			rand = r.nextInt(stapel.length);
			tmp = stapel[i];
			stapel[i] = stapel[rand];
			stapel[rand] = tmp;
		}
	}

	// Zuweisung der Karten zu Spielern
	public void verteileKarten() {
		for (int i = 0; i < 48; i++) {
			if (i < 12) {
				blatt[0][i] = stapel[i];
			} else if (12 <= i && i < 24) {
				blatt[1][i % 12] = stapel[i];
			} else if (24 <= i && i < 36) {
				blatt[2][i % 12] = stapel[i];
			} else {
				blatt[3][i % 12] = stapel[i];
			}
		}
	}

	// Starten des Spiels, steuert Gewinnbedingungen, gibt Tabellen aus
	public void startGame() throws MqttException, InterruptedException {
		ausgabe="";
		ausgabe += "Spiel gestartet" + "\n";
		ausgabe += "################ NEUES SPIEL GESTARTET ################" + "\n";
		ausgabe += "************* ANSAGEN *************" + "\n";
		Spielbrett.publishData("allData", ausgabe);

		ansagen();
		teamsBilden();
		blattAnzeigen();
		for (int i = 0; i < 12; i++) {
			ausgabe="";
			ausgabe += "\n";
			ausgabe += "____________ AKTUELLER PUNKTESTAND ____________" + "\n";
			ausgabe += "Spieler 1: " + spieler1.getpunktestand() + "\n";
			ausgabe += "Spieler 2: " + spieler2.getpunktestand() + "\n";
			ausgabe += "Spieler 3: " + spieler3.getpunktestand() + "\n";
			ausgabe += "Spieler 4: " + spieler4.getpunktestand() + "\n";
			ausgabe += "_______________________________________________" + "\n";
			ausgabe += "################ RUNDE " + (i + 1) + " GESTARTET ################" + "\n";

			Spielbrett.publishData("allData", ausgabe);
			ausgabe = "";
			
			startRound();
			ausgabe = "";
			ausgabe +="\n";
			ausgabe +="################ RUNDE " + (i + 1) + " BEENDET ################";
			ausgabe +="\n";
			ausgabe +="################ SPIELER " + (bekommtStich + 1) + " GEWINNT DIE RUNDE ################"+"\n";

			if (berechneRe() >= 121) {
				ausgabe +="TEAM RE GEWINNT DAS SPIEL"+"\n";
				ausgabe +="TEAM RE (";
				for (Spieler s : re) {
					ausgabe +="Spieler " + s.nummer + " ";
				}
				ausgabe +=") GEWINNEN MIT EINER PUNKTZAHL VON: " + berechneRe() + " PUNKTEN!";
				ausgabe +="\n";
				break;
			} else if (berechneKontra() >= 120) {
				ausgabe +="TEAM KONTRA GEWINNT DAS SPIEL";
				ausgabe +="TEAM KONTRA (";
				for (Spieler s : kontra) {
					ausgabe +="Spieler " + s.nummer + " ";
				}
				ausgabe +=") GEWINNEN MIT EINER PUNKTZAHL VON: " + berechneKontra() + " PUNKTEN!";
				ausgabe +="\n";
				break;
			}
			Spielbrett.publishData("allData", ausgabe);
		}
		ausgabe = "";
		ausgabe +="____________ ENDPUNKTESTAND ____________";
		ausgabe +="\n";
		ausgabe +="Spieler 1: " + spieler1.getpunktestand() + "\n";
		ausgabe +="Spieler 2: " + spieler2.getpunktestand() + "\n";
		ausgabe +="Spieler 3: " + spieler3.getpunktestand() + "\n";
		ausgabe +="Spieler 4: " + spieler4.getpunktestand() + "\n";
		ausgabe +="_______________________________________________" + "\n";

		ausgabe +="################## SPIEL BEENDET ##################" + "\n";
		ausgabe +="###################################################" + "\n";
		Spielbrett.publishData("allData", ausgabe);
	}

	// Starten der einzelnen Runde (jeder spielt eine Karte)
	public void startRound() throws MqttException, InterruptedException {
		bedienen = null;
		for (int i = 0; i < 4; i++) {
			ausgabe="";
			ausgabe += "\n" + "################ SPIELER " + (currentPlayer + 1) + " ################";
			Spielbrett.publishData("allData", ausgabe);
		

			ablegestapelAnzeigen();
			karteSpielen();
			blattAnzeigen();

			if (currentPlayer == 3) {
				currentPlayer = currentPlayer - 3;
			} else {
				currentPlayer++;
			}
		}

		int neuePunkte = alleSpieler[bekommtStich].getpunktestand();
		neuePunkte += punkteBerechnen();
		alleSpieler[bekommtStich].setpunktestand(neuePunkte);
		currentPlayer = bekommtStich;
		hoechsteGespielte = null;

		gamedata.setSpielstand1(spieler1.getpunktestand());
		gamedata.setSpielstand2(spieler2.getpunktestand());
		gamedata.setSpielstand3(spieler3.getpunktestand());
		gamedata.setSpielstand4(spieler4.getpunktestand());
		gamedata.setSpieler1(alleSpieler[0].getUsername());
		gamedata.setSpieler2(alleSpieler[1].getUsername());
		gamedata.setSpieler3(alleSpieler[2].getUsername());
		gamedata.setSpieler4(alleSpieler[3].getUsername());
		gamedata.setDate("" + System.currentTimeMillis() + "");
		
		save.saveGame(gamedata);
//		save.getGames();
		ablegestapel.clear();
	}

	// Anzeigen des Ablegestapels vor dem Spielen einer Karte
	public void ablegestapelAnzeigen() throws MqttException {
		ausgabe="";
		ausgabe += "////// STAPEL ///////" + "\n";

		for (int k = 0; k < ablegestapel.size(); k++) {
			ausgabe += ablegestapel.get(k).getColor() + "\t";
		}
		ausgabe += "\n";
		for (int k = 0; k < ablegestapel.size(); k++) {
			ausgabe += ablegestapel.get(k).getNumber() + "\t";
		}

		if (ablegestapel.size() >= 1) {
			if (ablegestapel.get(0).getTrumpf()) {
				ausgabe += "\n" + "Trumpf muss bedient werden.";
				bedienen = "Trumpf";
			} else {
				ausgabe += "\n" + ablegestapel.get(0).getColor() + " muss in Fehl bedient werden.";
				bedienen = ablegestapel.get(0).getColor();
			}

		}
		ausgabe += "\n" + "Aktuelle Punkte auf Ablegestapel: " + punkteBerechnen() + "\n" + "//// STAPELENDE ////";
		Spielbrett.publishData("allData", ausgabe);

	}

	// Spielen einer Karte, steuert Bedingungen ob und wann welche Karten gespielt
	// werden dürfen
	public int karteSpielen() throws MqttException, InterruptedException {
		String mtype = "mtype=karteSpielen";
		String mcontent = "&mcontent=Spielen Sie eine Karte (1 - 12): ";
		publishData("an="+alleSpieler[currentPlayer].getcId(), mtype + mcontent);

		while (!weiter) {
			TimeUnit.SECONDS.sleep(1);
		}
		weiter = false;

		boolean kannBedienen = false;
		mtype="mtype=nachricht";
		mcontent="&mcontent=";
		int n = Integer.parseInt(mAnswer) - 1;
		if (0 <= n && n < 12) {
			
			if (bedienen == null) {
				ablegestapel.add(blatt[currentPlayer][n]);
				blatt[currentPlayer][n] = null;
			} else {
				if (bedienen.equals("Trumpf")) {
					
					for (int j = 0; j < blatt[currentPlayer].length; j++) {
						if (!(blatt[currentPlayer][j] == null)) {
							if (blatt[currentPlayer][j].getTrumpf()) {
								kannBedienen = true;
							}
						}
					}
					
					if (!(blatt[currentPlayer][n] == null)) {
						if (kannBedienen && !blatt[currentPlayer][n].getTrumpf()) {
							publishData("an="+alleSpieler[currentPlayer].getcId(), mtype + mcontent + "Trumpf muss bedient werden!!\n");
							karteSpielen();
						} else {
							ablegestapel.add(blatt[currentPlayer][n]);
							blatt[currentPlayer][n] = null;
						}
					} else {
						publishData("an="+alleSpieler[currentPlayer].getcId(), mtype + mcontent + "Die Karte existiert nicht.\n");
						karteSpielen();
					}
				} 

				else if (bedienen.equals("Kreuz")) {
					for (int j = 0; j < blatt[currentPlayer].length; j++) {
						if (!(blatt[currentPlayer][j] == null)) {
							if (blatt[currentPlayer][j].getColor().equals("Kreuz")
									&& !blatt[currentPlayer][j].getTrumpf()) {
								kannBedienen = true;
							}
						}
					}
					if (!(blatt[currentPlayer][n] == null)) {
						if (kannBedienen && blatt[currentPlayer][n].getColor().equals("Kreuz")
								&& blatt[currentPlayer][n].getTrumpf()) {
							publishData("an="+alleSpieler[currentPlayer].getcId(), mtype + mcontent + "Du musst mit Fehl bedienen.\n");
							karteSpielen();
						} else if (kannBedienen && !blatt[currentPlayer][n].getColor().equals("Kreuz")) {
							publishData("an="+alleSpieler[currentPlayer].getcId(), mtype + mcontent + "Kreuz muss als Fehl bedient weren!!\n");
							karteSpielen();
						} else {
							ablegestapel.add(blatt[currentPlayer][n]);
							blatt[currentPlayer][n] = null;
						}
					} else {
						publishData("an="+alleSpieler[currentPlayer].getcId(), mtype + mcontent + "Die Karte existiert nicht.\n");
						karteSpielen();
					}
				} else if (bedienen.equals("Pik")) {
					for (int j = 0; j < blatt[currentPlayer].length; j++) {
						if (!(blatt[currentPlayer][j] == null)) {
							if (blatt[currentPlayer][j].getColor().equals("Pik")
									&& !blatt[currentPlayer][j].getTrumpf()) {
								kannBedienen = true;
							}
						}
					}
					if (!(blatt[currentPlayer][n] == null)) {
						if (kannBedienen && blatt[currentPlayer][n].getColor().equals("Pik")
								&& blatt[currentPlayer][n].getTrumpf()) {
							publishData("an="+alleSpieler[currentPlayer].getcId(), mtype + mcontent + "Du musst mit Fehl bedienen.\n");
							karteSpielen();
						} else if (kannBedienen && !blatt[currentPlayer][n].getColor().equals("Pik")) {
							publishData("an="+alleSpieler[currentPlayer].getcId(),mtype + mcontent +  "Pik muss als Fehl bedient weren!!\n");
							karteSpielen();
						} else {
							ablegestapel.add(blatt[currentPlayer][n]);
							blatt[currentPlayer][n] = null;
						}
					} else {
						publishData("an="+alleSpieler[currentPlayer].getcId(), mtype + mcontent + "Die Karte existiert nicht.\n");
						karteSpielen();
					}
				} else if (bedienen.equals("Herz")) {
					for (int j = 0; j < blatt[currentPlayer].length; j++) {
						if (!(blatt[currentPlayer][j] == null)) {
							if (blatt[currentPlayer][j].getColor().equals("Herz")
									&& !blatt[currentPlayer][j].getTrumpf()) {
								kannBedienen = true;
							}
						}
					}
					if (!(blatt[currentPlayer][n] == null)) {
						if (kannBedienen && blatt[currentPlayer][n].getColor().equals("Herz")
								&& blatt[currentPlayer][n].getTrumpf()) {
							publishData("an="+alleSpieler[currentPlayer].getcId(), mtype + mcontent + "Du musst mit Fehl bedienen\n");
							karteSpielen();
						} else if (kannBedienen && !blatt[currentPlayer][n].getColor().equals("Herz")) {
							publishData("an="+alleSpieler[currentPlayer].getcId(),mtype + mcontent + "Herz muss als Fehl bedient weren!!\n");
							karteSpielen();
						} else {
							ablegestapel.add(blatt[currentPlayer][n]);
							blatt[currentPlayer][n] = null;
						}
					} else {
						publishData("an="+alleSpieler[currentPlayer].getcId(), mtype + mcontent + "Die Karte existiert nicht.\n");
						karteSpielen();
					}
				}
			}
			checkHoechste();
		} else {
			publishData("an="+alleSpieler[currentPlayer].getcId(), mtype + mcontent + "Geben Sie eine vorhandene Karte an!\n");
			karteSpielen();
		}
		return n;
	}

	// Darsstellen der Karten eines Spielers vor seinem Zug
	public void blattAnzeigen() throws MqttException {
		ausgabe="mtype=blattAnzeigen&mcontent=";
		//ausgabe += "----- BLATT -----" + "\n";
//		for (int k = 0; k < 12; k++) {
//			if (blatt[currentPlayer][k] != null) {
//				ausgabe += (k + 1) + ".\t";
//			}
//		}
//		ausgabe += "\n";
		for (int k = 0; k < 12; k++) {
			if (blatt[currentPlayer][k] != null) {
				ausgabe += k + "_" + blatt[currentPlayer][k].getColor() +"_" +  blatt[currentPlayer][k].getNumber() + "&" ;
			} 
		}
//		ausgabe += "\n";
//		for (int k = 0; k < 12; k++) {
//			if (blatt[currentPlayer][k] != null) {
//				
//			}
//		}
//		ausgabe += "\n";
//		ausgabe += "-- BLATT ENDE --";
		publishData("an="+alleSpieler[currentPlayer].getcId(), ausgabe);
	}

	// Berechnen der Punkte
	public int punkteBerechnen() {
		int punkte = 0;
		for (int k = 0; k < ablegestapel.size(); k++) {
			punkte += ablegestapel.get(k).getValue();
		}
		return punkte;
	}

	// Berechnen der aktuell hoechsten Karte auf dem Ablagestapel
	public void checkHoechste() {
		if (hoechsteGespielte == null) {
			hoechsteGespielte = ablegestapel.get(0);
			bekommtStich = currentPlayer;
		} else {
			if (hoechsteGespielte.getOrder() > ablegestapel.get(ablegestapel.size() - 1).getOrder()) {
				hoechsteGespielte = ablegestapel.get(ablegestapel.size() - 1);
				bekommtStich = currentPlayer;
			} else if (hoechsteGespielte.getOrder() == ablegestapel.get(ablegestapel.size() - 1).getOrder()) {
				if (hoechsteGespielte.getColor().equals(ablegestapel.get(ablegestapel.size() - 1).getColor())) {
					if (hoechsteGespielte.getValue() < ablegestapel.get(ablegestapel.size() - 1).getValue()) {
						hoechsteGespielte = ablegestapel.get(ablegestapel.size() - 1);
						bekommtStich = currentPlayer;
					}
				}
			}
		}
		System.out.println("Hoechste gespielte Karte hat Wert: " + hoechsteGespielte.getValue());

	}

	// Methode bestimmt später Spielvariationen, derzeit leer
	public void ansagen() throws MqttException {

		Spielbrett.publishData("allData", "GESPIELT WIRD OHNE ANSAGEN");
	}

	// Bestimmt werden die Teams nach Kreuz Dame
	public void teamsBilden() {
		for (int i = 0; i < 4; i++) {
			boolean hatDame = false;
			boolean solo = false;
			for (int k = 0; k < 12; k++) {
				if (blatt[i][k].getColor().equals("Kreuz") && blatt[i][k].getNumber().equals("Dame")) {
					if (hatDame) {
						solo = true;
					} else {
						hatDame = true;
					}
				}
			}
			if (hatDame) {
				if (!solo) {
					re.add(alleSpieler[i]);
				}
			} else {
				kontra.add(alleSpieler[i]);
			}

		}
	}

	// Berechnung der Punkte des Teams mit Kreuz Damen
	public int berechneRe() {
		int punkte = 0;
		for (Spieler s : re) {
			punkte += s.getpunktestand();
		}
		return punkte;
	}

	// Berechnung der Punkte des Teams ohne Kreuz Damen
	public int berechneKontra() {
		int punkte = 0;
		for (Spieler s : kontra) {
			punkte += s.getpunktestand();
		}
		return punkte;
	}

	public static void publishData(String channel, String data) throws MqttException {

		String messageString = data;
		MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());

		MqttMessage message = new MqttMessage();
		message.setPayload(messageString.getBytes());

		client.connect();
		client.publish(channel, message);

		client.disconnect();

	}

}
