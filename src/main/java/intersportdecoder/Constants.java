package main.java.intersportdecoder;

import java.awt.Color;
import java.nio.charset.Charset;

/** the class Constants */
public class Constants {

	public static final String SMB_ACCESS = "SmbAccess";
	public static final String USER = "pi";
	public static final String SMB_ACCESS_TXT = "smbaccess.txt";
	public static final String INTERSPORT_DECODER = "Intersport AES Decoder";
	public static final String DECODER = "IntersportDecoder";
	public static final int WIDTH_OF_DECODER_FRAME = 450;
	public static final int HEIGHT_OF_DECODER_FRAME = 130;
	public static final int MINIMAL_WIDTH_OF_DECODER_FRAME = 100;
	public static final int MINIMAL_HEIGHT_OF_DECODER_FRAME = 100;
	public static final String IMAGE = "Image";
	public static final String LOGO = "logo.png";
	public static final String ENCRYPTED_PASSWORD = "Chiffre:";
	public static final String DECRYPTED_PASSWORD = "Klartext:";
	public static final String ACCESS = "Access";
	public static final String ACCESS_TXT = "Access.txt";

	public static final String PATH_TO_ACCESS_BIKECONTRACT_RELEASE = "smb://raspberrypi/access/";
	public static final String ENCRYPTION = "ENCRYPTION";
	public static final Object NOT_ABLE_TO_ACCESS = "Zugang zu Schlüssel nicht möglich!";
	public static final String WARNING = "Warnung";
	public static final Charset CHARSET = Charset.forName("ISO-8859-1");
	public static final String EMPTY_STRING = "";
	public static final Object NOT_ABLE_TO_DECRYPT = "Dechiffrierung nicht möglich";

	public static final Color INTERSPORT_CORAL = Color.decode("#F86A6A");

}
