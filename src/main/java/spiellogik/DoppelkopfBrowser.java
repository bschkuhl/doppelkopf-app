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
import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

public class DoppelkopfBrowser {

	private PWMediator pwm;
	public JFrame f;
	public String username;
	private HashMap componentMap;
	public JPanel panel;
	List<JLabel> labelArray;

	public DoppelkopfBrowser(PWMediator pwm) {
		this.pwm = pwm;
		createAnmeldeFenster();

	}

	public void createAnmeldeFenster() {
		f = new JFrame("Doppelkopf-App");
		f.setSize(800, 640);
		f.setResizable(false);

		f.setLocation(300, 200);
		f.getContentPane().setLayout(null);

		JButton btnNewButton = new JButton("Anmelden");

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				username = JOptionPane.showInputDialog("Enter your name");
				pwm.getStats(username);

			}
		});

		btnNewButton.setBounds(333, 276, 127, 59);
		f.getContentPane().add(btnNewButton);
		f.getContentPane().setBackground(new Color(51, 102, 0));
		f.setVisible(true);

	}

	public void createStatsFenster(String a, String g, String p) {

		f.getContentPane().removeAll();
		f.getContentPane().revalidate();
		f.getContentPane().repaint();

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
				pwm.createSpieler();

			}
		});
		f.getContentPane().add(btnNewButton);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void createSpielFenster() {
		f.getContentPane().revalidate();
		f.getContentPane().repaint();
		f.getContentPane().removeAll();
		JTextArea textArea = new JTextArea();
		textArea.setBounds(563, 75, 221, 391);
		f.getContentPane().add(textArea);
		textArea.setEditable(false);

		JButton btnNewButton = new JButton("Spiel verlassen");
		btnNewButton.setBounds(563, 36, 139, 23);
		f.getContentPane().add(btnNewButton);

		JLabel lblNewLabel = new JLabel("" + username);
		f.getContentPane().add(lblNewLabel);

		panel = new JPanel();
		panel.setBounds(10, 75, 543, 391);
		panel.setName("spielbrett");
		f.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel ablage1 = new JLabel();
		JLabel ablage2 = new JLabel();
		JLabel ablage3 = new JLabel();
		JLabel ablage4 = new JLabel();

		ablage1.setBounds(50, 150, 52, 80);
		ablage2.setBounds(112, 150, 52, 80);
		ablage3.setBounds(174, 150, 52, 80);
		ablage4.setBounds(236, 150, 52, 80);

		panel.add(ablage1);
		panel.add(ablage2);
		panel.add(ablage3);
		panel.add(ablage4);

		createHand();

		f.setVisible(true);
	}

	public void createHand() {
		int j = 0;
		labelArray = new LinkedList<JLabel>();

		for (int i = 0; i < 12; i++) {
			labelArray.add(new JLabel());
			labelArray.get(i).setName("" + i);
			int x = 10 + (62 * i);
			if (i < 6) {
				labelArray.get(i).setBounds(x, 300, 52, 80);
			} else {
				int x2 = 10 + (62 * j);
				labelArray.get(i).setBounds(x2, 205, 52, 80);
				j++;
			}
			// LISTE DURCHGEHEN
			labelArray.get(i).addMouseListener(new MouseListener() {

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
					Object a = arg0.getComponent().getName();
					System.out.println(a);

				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub

				}
			});

			panel.add(labelArray.get(i));
		}
	}

	public void updateHand(String[] karten) {
		for (int i = 0; i < 12; i++) {
			String farbe = "";
			String wert = "";
			JLabel l;

			if (karten[i].split("_")[1].length() > 0) {

				switch (karten[i].split("_")[1]) {

				case "Herz":
					farbe = "H";
					switch (karten[i].split("_")[1]) {
					case "Ass":
						wert = "Ass";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "Koenig":
						wert = "Koenig";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "Dame":
						wert = "Dame";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "Bube":
						wert = "Bube";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "10":
						wert = "10";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "9":
						wert = "9";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					}
					break;
				case "Karo":
					farbe = "Ka";
					switch (karten[i].split("_")[1]) {
					case "Ass":
						wert = "Ass";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "Koenig":
						wert = "Koenig";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "Dame":
						wert = "Dame";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "Bube":
						wert = "Bube";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "10":
						wert = "10";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "9":
						wert = "9";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					}
					break;
				case "Kreuz":
					farbe = "K";
					switch (karten[i].split("_")[1]) {
					case "Ass":
						wert = "Ass";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "Koenig":
						wert = "Koenig";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "Dame":
						wert = "Dame";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "Bube":
						wert = "Bube";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "10":
						wert = "10";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "9":
						wert = "9";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					}
					break;
				case "Pik":
					farbe = "P";
					switch (karten[i].split("_")[1]) {
					case "Ass":
						wert = "Ass";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "Koenig":
						wert = "Koenig";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "Dame":
						wert = "Dame";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "Bube":
						wert = "Bube";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "10":
						wert = "10";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					case "9":
						wert = "9";
						l = (JLabel) getComponentByName(karten[i].split("_")[0]);
						l.setIcon(scaleCard(wert + farbe));
						break;
					}
					break;
				}
			}
		}

		// labelArray.get(i).setIcon(scaleCard("10H"));
	}

	public ImageIcon scaleCard(String karte) {
		karte = karte + ".png";
		String projektpfad = "/Karten/";
		String kartenpfad = projektpfad + karte;
		ImageIcon imageIcon = new ImageIcon(windows.class.getResource(kartenpfad));
		Image image = imageIcon.getImage();
		Image newimg = image.getScaledInstance(52, 80, java.awt.Image.SCALE_SMOOTH);
		ImageIcon scaledCard = new ImageIcon(newimg);
		return scaledCard;
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	private void createComponentMap() {
		componentMap = new HashMap<String, Component>();
		Component[] components = f.getContentPane().getComponents();
		for (int i = 0; i < components.length; i++) {
			componentMap.put(components[i].getName(), components[i]);
		}
	}

	public Component getComponentByName(String name) {
		if (componentMap.containsKey(name)) {
			return (Component) componentMap.get(name);
		} else
			return null;
	}
}
