package spiellogik;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JTextPane;

import org.eclipse.paho.client.mqttv3.MqttException;

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.Panel;
import java.awt.Font;

public class windows {

	public windows() throws IOException {

		createFenster();

	}

	public static void main(String[] args) throws MqttException, IOException {
		windows test = new windows();
	}

	public void createFenster() throws IOException {
		JFrame f = new JFrame("Doppelkopf-App");
		f.setSize(800, 640);
		f.setResizable(false);

		JTextArea textArea = new JTextArea();
		textArea.setBounds(563, 36, 221, 391);
		f.getContentPane().add(textArea);

		JButton btnNewButton = new JButton("Spiel verlassen");
		btnNewButton.setBounds(563, 36, 89, 23);
		f.getContentPane().add(btnNewButton);

		JLabel lblNewLabel = new JLabel("" + "username");
		f.getContentPane().add(lblNewLabel);

		JPanel panel = new JPanel();
		panel.setBounds(10, 75, 543, 391);
		panel.setName("spielbrett");
		f.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel ablage1 = new JLabel();
		JLabel ablage2 = new JLabel();
		JLabel ablage3 = new JLabel();
		JLabel ablage4 = new JLabel();

		panel.add(ablage1);
		panel.add(ablage2);
		panel.add(ablage3);
		panel.add(ablage4);

		//createHand();

		f.setVisible(true);
		

	}

	
}
