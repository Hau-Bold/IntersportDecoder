package main.java.intersportdecoder;

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** the class TextFieldListener */
public class TextFieldListener implements DocumentListener {

	private JTextField textField;

	public TextFieldListener(JTextField textField) {
		this.textField = textField;
	}

	@Override
	public void insertUpdate(DocumentEvent event) {

		textField.setBackground(Color.WHITE);

		if (textField == IntersportDecoder.txtEncrypted) {
			IntersportDecoder.buttonSubmit.setVisible(Boolean.TRUE);
		}

	}

	@Override
	public void removeUpdate(DocumentEvent event) {

		if (textField == IntersportDecoder.txtEncrypted) {
			IntersportDecoder.buttonSubmit.setVisible(Boolean.FALSE);
		}

		textField.setBackground(Constants.INTERSPORT_CORAL);

	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

}
