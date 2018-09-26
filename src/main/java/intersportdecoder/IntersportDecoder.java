package main.java.intersportdecoder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/** the class IntersportDecoder */
public class IntersportDecoder extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static String directoryOfDecoder;

	private JPanel panelMain;
	private JLabel labelEncrypted, labelDecrypted;
	public static JTextField txtEncrypted, txtDecrypted;

	public static JButton buttonSubmit;
	protected static String key = null;

	/**
	 * Constructor
	 * 
	 * @param directoryOfDecoder
	 */
	public IntersportDecoder(String directoryOfDecoder) {
		IntersportDecoder.directoryOfDecoder = directoryOfDecoder;
		initComponent();
	}

	private void initComponent() {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				Map<String, String> access = null;

				try {
					access = Utils.readAccess(directoryOfDecoder);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				if (access == null) {

					/** if file not in folder: read it from pi */

					String smbpasswd = Utils.getSmbPassword(directoryOfDecoder);

					try {
						Utils.requestAccess(Constants.PATH_TO_ACCESS_BIKECONTRACT_RELEASE, Constants.USER, smbpasswd,
								directoryOfDecoder + File.separator + Constants.DECODER + File.separator
										+ Constants.ACCESS);
						access = Utils.readAccess(directoryOfDecoder);
						Utils.clearAccess(directoryOfDecoder);
					} catch (IOException e) {

						JOptionPane.showMessageDialog(null, Constants.NOT_ABLE_TO_ACCESS, Constants.WARNING,
								JOptionPane.WARNING_MESSAGE);

						e.printStackTrace();
					}

				}

				/** access is readed */
				key = access.get(Constants.ENCRYPTION);

			}
		});

		this.setTitle(Constants.INTERSPORT_DECODER);

		this.setSize(Constants.WIDTH_OF_DECODER_FRAME, (Constants.HEIGHT_OF_DECODER_FRAME));
		this.setResizable(Boolean.TRUE);
		this.getContentPane().setBackground(Color.WHITE);
		this.setMinimumSize(
				new Dimension(Constants.MINIMAL_WIDTH_OF_DECODER_FRAME, Constants.MINIMAL_HEIGHT_OF_DECODER_FRAME));

		String path = directoryOfDecoder + File.separator + Constants.DECODER + File.separator + Constants.IMAGE
				+ File.separator + Constants.LOGO;
		ImageIcon imageIcon = new ImageIcon(path);

		Image image = imageIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
		this.setIconImage(image);

		int[] location = Utils.getLocation(this);
		this.setLocation(location[0], location[1]);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);

				handleClosingOfInstance();
			}

			private void handleClosingOfInstance() {
				Runtime.getRuntime().exit(0);

			}
		});

		panelMain = new JPanel(null);
		panelMain.setBackground(Color.WHITE);
		this.getContentPane().add(panelMain);

		labelEncrypted = new JLabel(Constants.ENCRYPTED_PASSWORD);
		labelEncrypted.setBounds(20, 10, 50, 25);
		panelMain.add(labelEncrypted);

		txtEncrypted = new JTextField();
		txtEncrypted.setBounds(80, 10, 300, 25);
		txtEncrypted.setBackground(Constants.INTERSPORT_CORAL);
		txtEncrypted.getDocument().addDocumentListener(new TextFieldListener(txtEncrypted));
		panelMain.add(txtEncrypted);

		buttonSubmit = new IconButton("confirm.png", 390, 10);
		buttonSubmit.setVisible(Boolean.FALSE);
		buttonSubmit.addActionListener(this);
		panelMain.add(buttonSubmit);

		labelDecrypted = new JLabel(Constants.DECRYPTED_PASSWORD);
		labelDecrypted.setBounds(20, 50, 50, 25);
		panelMain.add(labelDecrypted);

		txtDecrypted = new JTextField();
		txtDecrypted.setBounds(80, 50, 300, 25);
		txtDecrypted.setBackground(Constants.INTERSPORT_CORAL);
		txtDecrypted.getDocument().addDocumentListener(new TextFieldListener(txtDecrypted));
		panelMain.add(txtDecrypted);

	}

	public void showFrame() {
		this.setVisible(Boolean.TRUE);

	}

	public static String getDirectory() {
		return directoryOfDecoder;
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		Object o = event.getSource();

		if (o.equals(buttonSubmit)) {

			String encrypted = txtEncrypted.getText().trim();

			String decrypted = null;
			try {
				decrypted = Utils.decrypt(key, encrypted);
			} catch (Exception e) {

				e.printStackTrace();
				JOptionPane.showMessageDialog(null, Constants.NOT_ABLE_TO_DECRYPT, Constants.WARNING,
						JOptionPane.WARNING_MESSAGE);

			}

			txtDecrypted.setText(decrypted);

		}

	}

}
