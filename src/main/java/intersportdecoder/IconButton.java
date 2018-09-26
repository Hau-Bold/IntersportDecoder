package main.java.intersportdecoder;

import java.awt.Image;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class IconButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImageIcon icon;

	@Override
	public ImageIcon getIcon() {
		return icon;
	}

	public void setIcon(String pathOfIcon) {

		String pathToImage = IntersportDecoder.getDirectory() + File.separator + Constants.DECODER + File.separator
				+ Constants.IMAGE + File.separator + pathOfIcon;

		this.icon = new ImageIcon(pathToImage);
		this.icon.setImage(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
	}

	// Constructor
	public IconButton(String pathOfIcon, int xPos, int yPos) {

		this.setIcon(pathOfIcon);
		this.setBounds(xPos, yPos, 20, 20);
		setBorder(BorderFactory.createRaisedBevelBorder());
	}

}
