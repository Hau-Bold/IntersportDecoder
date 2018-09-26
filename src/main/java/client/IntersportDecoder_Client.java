package main.java.client;

import javax.swing.SwingUtilities;

import main.java.intersportdecoder.IntersportDecoder;

/** entry point for IntersportDecoder */
public class IntersportDecoder_Client {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new IntersportDecoder(args[0]).showFrame();

			}
		});

	}

}
