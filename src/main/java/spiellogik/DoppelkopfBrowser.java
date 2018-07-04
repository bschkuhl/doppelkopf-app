package spiellogik;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;

import javax.swing.JTextPane;

import org.eclipse.paho.client.mqttv3.MqttException;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class DoppelkopfBrowser {

	private PWMediator pwm;
	private JFrame f;
	private String username;
	private HashMap componentMap;
	private JPanel panel;
	List<JLabel> labelArray;
	List<JLabel> ablageListe;
	private static JTextArea textArea;
	private static JScrollPane sp;

	public DoppelkopfBrowser(PWMediator pwm) {
		this.pwm = pwm;
		createAnmeldeFenster();

	}

	public void createAnmeldeFenster() {
		System.out.println("createAnmeldeFenster");
		f = new JFrame("Doppelkopf-App");
		f.setSize(800, 640);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		f.getContentPane().setLayout(null);

		JButton btnNewButton = new JButton("Anmelden");

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				username = JOptionPane.showInputDialog("Enter your name");

				pwm.createSpieler(username);

			}
		});

		btnNewButton.setBounds(333, 276, 127, 59);
		f.getContentPane().add(btnNewButton);
		f.getContentPane().setBackground(new Color(51, 102, 0));
		f.setVisible(true);

	}

	public void createStatsFenster(String a, String g, String p) {
		System.out.println("createStatsFenster");
		f.getContentPane().removeAll();

		Panel panel = new Panel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 37, 784, 64);
		f.getContentPane().add(panel);
		panel.setLayout(null);
		JLabel lblNewLabel = new JLabel("" + username);
		lblNewLabel.setFont(new Font("Lucida Handwriting", Font.BOLD, 30));
		lblNewLabel.setBounds(93, 0, 608, 64);
		panel.add(lblNewLabel);
		lblNewLabel.setBackground(Color.WHITE);
		double quote = 0;
		if (Integer.parseInt(g) > 0) {
			quote = Integer.parseInt(a) / Integer.parseInt(g);
			quote = round(quote, 2);
		}
		JTextArea textArea = new JTextArea("STATS\n" + "Gespielt: " + a + "\n" + "Gewonnen: " + g + "\n"
				+ "Gewinnquote: " + quote + "\n" + "Gesamtpunkte: " + p);
		textArea.setFont(new Font("Monospaced", Font.BOLD, 16));
		textArea.setBounds(108, 249, 289, 202);
		textArea.setEditable(false);
		f.getContentPane().add(textArea);

		JLabel lblHistory = new JLabel("HISTORY");
		lblHistory.setFont(new Font("Monospaced", Font.BOLD, 21));
		lblHistory.setBounds(108, 202, 174, 36);
		f.getContentPane().add(lblHistory);

		JButton btnNewButton = new JButton("SEARCH FOR GAME\r\n");

		btnNewButton.setBounds(541, 206, 201, 36);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// NEUER SPIELERCLIENT
				try {
					pwm.connectSpieler();
				} catch (MqttException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		f.getContentPane().add(btnNewButton);

		f.getContentPane().revalidate();
		f.getContentPane().repaint();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void createSpielFenster() {
		System.out.println("createSpielFenster");
		f.getContentPane().removeAll();
		textArea = new JTextArea();
		ablageListe = new LinkedList<JLabel>();
		textArea.setEditable(false);
		// textArea.setAutoscrolls(true);

		sp = new JScrollPane(textArea);
		sp.setBounds(563, 75, 221, 391);
		sp.setAutoscrolls(true);
		f.getContentPane().add(sp);

		JButton btnNewButton = new JButton("Spiel verlassen");
		btnNewButton.setBounds(563, 36, 139, 23);
		f.getContentPane().add(btnNewButton);

		JLabel spielertag = new JLabel(username);
		spielertag.setFont(new Font("Monospaced", Font.BOLD, 21));
		spielertag.setBounds(10, 36, 174, 23);
		f.getContentPane().add(spielertag);

//		JLabel lblNewLabel = new JLabel("" + username);
//		f.getContentPane().add(lblNewLabel);

		panel = new JPanel();
		panel.setBounds(10, 75, 543, 391);
		panel.setName("spielbrett");
		f.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel ablage1 = new JLabel();
		JLabel ablage2 = new JLabel();
		JLabel ablage3 = new JLabel();
		JLabel ablage4 = new JLabel();

		ablage1.setBounds(130, 90, 62, 96);
		ablage2.setBounds(207, 90, 62, 96);
		ablage3.setBounds(284, 90, 62, 96);
		ablage4.setBounds(361, 90, 62, 96);

		ablageListe.add(ablage1);
		ablageListe.add(ablage2);
		ablageListe.add(ablage3);
		ablageListe.add(ablage4);
		panel.add(ablage1);
		panel.add(ablage2);
		panel.add(ablage3);
		panel.add(ablage4);

		createHand();
		f.getContentPane().revalidate();
		f.getContentPane().repaint();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void createHand() {
		System.out.println("createHand");
		int j = 0;
		labelArray = new LinkedList<JLabel>();

		for (int i = 0; i < 12; i++) {
			labelArray.add(new JLabel());
			labelArray.get(i).setName("" + (i + 1));
			int x = 70 + (62 * i);
			if (i < 6) {
				labelArray.get(i).setBounds(x, 300, 52, 80);
			} else {
				int x2 = 45 + (62 * j);
				labelArray.get(i).setBounds(x2, 205, 52, 80);
				j++;
			}

			panel.add(labelArray.get(i));
		}
	}

	public void updateHand(String[] karten) {
System.out.println("updateHand");
		for (int j = 0; j < 12; j++) {
			labelArray.get(j).setIcon(null);
		}
		for (int i = 0; i < karten.length; i++) {
			String farbe = "";
			String wert = "";
			JLabel l;
			if (karten[i].split("_")[1].length() > 0) {

				switch (karten[i].split("_")[1]) {

				case "Herz":
					farbe = "H";
					switch (karten[i].split("_")[2]) {
					case "Ass":
						wert = "Ass";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "Koenig":
						wert = "Koenig";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "Dame":
						wert = "Dame";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "Bube":
						wert = "Bube";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "10":
						wert = "10";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "9":
						wert = "9";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					}
					break;
				case "Karo":
					farbe = "Ka";
					switch (karten[i].split("_")[2]) {
					case "Ass":
						wert = "Ass";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "Koenig":
						wert = "Koenig";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "Dame":
						wert = "Dame";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "Bube":
						wert = "Bube";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "10":
						wert = "10";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "9":
						wert = "9";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					}
					break;
				case "Kreuz":
					farbe = "K";
					switch (karten[i].split("_")[2]) {
					case "Ass":
						wert = "Ass";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "Koenig":
						wert = "Koenig";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "Dame":
						wert = "Dame";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "Bube":
						wert = "Bube";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "10":
						wert = "10";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "9":
						wert = "9";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					}
					break;
				case "Pik":
					farbe = "P";
					switch (karten[i].split("_")[2]) {
					case "Ass":
						wert = "Ass";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "Koenig":
						wert = "Koenig";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "Dame":
						wert = "Dame";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "Bube":
						wert = "Bube";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "10":
						wert = "10";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					case "9":
						wert = "9";
						l = labelArray.get(Integer.parseInt(karten[i].split("_")[0]));
						l.setIcon(scaleCard(wert + farbe, "hand"));
						break;
					}
					break;
				}
			}
		}
		for (int j = 0; j < 12; j++) {
			if (labelArray.get(j).getIcon() != null) {
				labelArray.get(j).addMouseListener(new MouseListener() {

					@Override
					public void mouseClicked(MouseEvent arg0) {
						// HIER KARTE SPIELEN

					}

					@Override
					public void mouseEntered(MouseEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseExited(MouseEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mousePressed(MouseEvent arg0) {
						// TODO Auto-generated method stub
//						JLabel icon = (JLabel) arg0.getComponent();
//						Icon i = icon.getIcon();
//						boolean hasIcon = !(icon.getIcon() == null);
//						if (hasIcon) {
							String a = arg0.getComponent().getName();
							pwm.karteSpielen(a);
//						}
					}

					@Override
					public void mouseReleased(MouseEvent arg0) {
						// TODO Auto-generated method stub

					}
				});
			}
			
		}
		// labelArray.get(i).setIcon(scaleCard("10H"));
	}

	public ImageIcon scaleCard(String karte, String a) {
		System.out.println("scaleCard");
		karte = karte + ".png";
		String projektpfad = "/Karten/";
		String kartenpfad = projektpfad + karte;
		ImageIcon imageIcon = new ImageIcon(windows.class.getResource(kartenpfad));
		Image image = imageIcon.getImage();
		Image newimg;
		if (a.equals("ablage")) {
			newimg = image.getScaledInstance(62, 96, java.awt.Image.SCALE_SMOOTH);
		} else {
			newimg = image.getScaledInstance(52, 80, java.awt.Image.SCALE_SMOOTH);
		}
	
		ImageIcon scaledCard = new ImageIcon(newimg);
		return scaledCard;
	}
	


	public static double round(double value, int places) {
		System.out.println("round");
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public static void infobox(String info) {
		System.out.println("infobox");
		info = info + "\n";
		if (textArea != null) {
			textArea.append(info);
			JScrollBar vertical = sp.getVerticalScrollBar();
			vertical.setValue( vertical.getMaximum() );
		}
	}

	public void updateStapel(String[] karten) {
		System.out.println("updateStapel");
		for (int i = 0; i < 4; i++) {
			ablageListe.get(i).setIcon(null);
		}

		for (int i = 0; i < karten.length; i++) {
			String farbe = "";
			String wert = "";
			JLabel l;
			switch (karten[i].split("_")[1]) {

			case "Herz":
				farbe = "H";
				switch (karten[i].split("_")[2]) {
				case "Ass":
					wert = "Ass";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "Koenig":
					wert = "Koenig";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "Dame":
					wert = "Dame";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "Bube":
					wert = "Bube";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "10":
					wert = "10";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "9":
					wert = "9";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				}
				break;
			case "Karo":
				farbe = "Ka";
				switch (karten[i].split("_")[2]) {
				case "Ass":
					wert = "Ass";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "Koenig":
					wert = "Koenig";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "Dame":
					wert = "Dame";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "Bube":
					wert = "Bube";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "10":
					wert = "10";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "9":
					wert = "9";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				}
				break;
			case "Kreuz":
				System.out.println((karten[i].split("_")[1]));
				farbe = "K";
				switch (karten[i].split("_")[2]) {
				case "Ass":
					wert = "Ass";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "Koenig":
					wert = "Koenig";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "Dame":
					wert = "Dame";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "Bube":
					wert = "Bube";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "10":
					wert = "10";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "9":
					wert = "9";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				}
				break;
			case "Pik":
				System.out.println((karten[i].split("_")[1]));
				farbe = "P";
				switch (karten[i].split("_")[2]) {
				case "Ass":
					wert = "Ass";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "Koenig":
					wert = "Koenig";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "Dame":
					wert = "Dame";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "Bube":
					wert = "Bube";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "10":
					wert = "10";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				case "9":
					wert = "9";
					l = ablageListe.get(Integer.parseInt(karten[i].split("_")[0]));
					l.setIcon(scaleCard(wert + farbe, "ablage"));
					break;
				}
				break;
			}

		}

	}
	// private void createComponentMap() {
	// componentMap = new HashMap<String, Component>();
	// Component[] components = f.getContentPane().getComponents();
	// for (int i = 0; i < components.length; i++) {
	// componentMap.put(components[i].getName(), components[i]);
	// }
	// }
	//
	// public Component getComponentByName(String name) {
	// System.out.println(componentMap);
	// if (componentMap.containsKey(name)) {
	// return (Component) componentMap.get(name);
	// } else
	// return null;
	// }
}
