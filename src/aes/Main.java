package aes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		AES as = new AES();
		String[] key = { "0f", "15", "71", "c9", "47", "d9", "e8", "59", "0c", "b7", "ad", "d6", "af", "7f", "67",
				"98" };
		Label displayLabel=new Label("0",Label.RIGHT);  
		Label memLabel=new Label(" ",Label.RIGHT); 
		JFrame frame = new JFrame("AES Implementation");
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.NORTH;
		panel.add(new JLabel("<html><h1><strong><i>Choose a file</i></strong></h1><hr></html>"), gbc);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		JPanel buttons = new JPanel(new GridBagLayout());
		JButton enc_button = new JButton();
		JButton dec_button = new JButton();
		enc_button.setText("Encryption");
		enc_button.setSize(300, 100);
		enc_button.setFont(enc_button.getFont().deriveFont(Font.BOLD));
		enc_button.setFocusPainted(false);
		//enc_button.setPreferredSize(new Dimension(400,100));
		enc_button.setBackground(Color.decode("#4CAF50"));
		dec_button.setBackground(Color.decode("#4CAF50"));
		dec_button.setText("Decryption");
		dec_button.setFont(dec_button.getFont().deriveFont(Font.BOLD));
		buttons.add(enc_button, gbc);
		buttons.add(dec_button, gbc);
		gbc.weighty = 1;
		panel.add(buttons, gbc);
		frame.add(panel);
		frame.pack();
		frame.setSize(400, 600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		enc_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String encryptFile = selectFile();
				try {
					
					if (!encryptFile.equals(null)) {
						try {
							as.encryption(encryptFile, key);
							JOptionPane.showMessageDialog(frame, "Encryption Completed!!!");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {

					}
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			}
		});
		dec_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String decryptFile = selectFile();
				try {
					if (!decryptFile.equals(null)) {

						try {
							as.decryption(decryptFile, key);
							JOptionPane.showMessageDialog(frame, "Decryption Completed!!!");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {

					}
				} catch (NullPointerException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
			}
		});

	}

	private static String selectFile() {
		// TODO Auto-generated method stub
		JFileChooser textFile = new JFileChooser();
		String s = null;
		JFrame text = new JFrame();
		int result = textFile.showOpenDialog(new JFrame());

		if (result == textFile.APPROVE_OPTION) {
			File selectedFile = textFile.getSelectedFile();
			s = selectedFile.getAbsolutePath();
		}
		return s;
	}

}
