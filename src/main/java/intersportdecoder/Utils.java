package main.java.intersportdecoder;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFrame;

import org.apache.commons.io.IOUtils;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

/** the class Utils */
public class Utils {

	/***
	 * reads the smb password from the local folder
	 * 
	 * @param directoryOfBikeContract
	 *            - the directory of the bike contract
	 * @return the smb password
	 */
	public static String getSmbPassword(String directoryOfBikeContract) {

		String response = null;
		String path = directoryOfBikeContract + File.separator + Constants.DECODER + File.separator
				+ Constants.SMB_ACCESS + File.separator + Constants.SMB_ACCESS_TXT;

		try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(path))))) {
			String line = null;

			while ((line = bufferedReader.readLine()) != null) {
				response = line.trim();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * yields the location
	 * 
	 * @return the location
	 */
	public static int[] getLocation(JFrame jFrame) {

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		double width = dimension.getWidth() / 2 - jFrame.getSize().width / 2 - 150;
		String widthAsString = String.valueOf(width);
		String widthRemainer = widthAsString.substring(0, widthAsString.indexOf("."));

		double height = dimension.getHeight() / 2 - jFrame.getSize().height / 2 - 150;
		String heightAsString = String.valueOf(height);
		String heightRemainer = heightAsString.substring(0, heightAsString.indexOf("."));

		return new int[] { Integer.valueOf(widthRemainer), Integer.valueOf(heightRemainer) };
	}

	/**
	 * reads the file that contains data for access ressources and the key for doing
	 * encryption
	 * 
	 * @param directoryOfContractDigitalizer
	 *            - the directoryOfContractDigitalizer * @return - the data for
	 *            access in a map
	 * @throws FileNotFoundException
	 *             - in case if technical error
	 * @throws IOException
	 *             - in case if technical error
	 */
	public static Map<String, String> readAccess(String directoryOfContractDigitalizer)
			throws FileNotFoundException, IOException {

		String path = directoryOfContractDigitalizer + File.separator + Constants.DECODER + File.separator
				+ Constants.ACCESS + File.separator + Constants.ACCESS_TXT;

		Map<String, String> access = null;

		File file = new File(path);

		if (file.exists()) {

			access = new HashMap<>();

			String line = null;
			try (BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(path))))) {

				while ((line = bufferedReader.readLine()) != null) {

					if (line.contains(Constants.ENCRYPTION)) {
						access.put(Constants.ENCRYPTION, handleLineOfAccess(line));
					}
				}
			}
		}

		return access;
	}

	private static String handleLineOfAccess(String line) {
		{
			return line.split("=")[1].trim();
		}
	}

	/**
	 * request access from Pi
	 * 
	 * @param path
	 *            - the path
	 * @param user
	 *            - the user
	 * @param password
	 *            - the password
	 * @param pathToAccess
	 *            - the path to the access folder
	 * @throws IOException
	 *             - in case of technical error
	 */
	public static void requestAccess(String path, String user, String password, String pathToAccess)
			throws IOException {

		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, user, password);

		SmbFile smbFile = new SmbFile(path, auth);

		if (smbFile.exists()) {

			SmbFile[] files = smbFile.listFiles();

			SmbFileInputStream in = null;
			FileOutputStream out = null;
			File file;
			for (SmbFile smbfile : files) {

				file = new File(pathToAccess + File.separator + smbfile.getName());

				in = new SmbFileInputStream(smbfile);
				out = new FileOutputStream(file);
				IOUtils.copy(in, out);
				break;
			}

			in.close();
			out.close();
		}

	}

	/**
	 * clears the folder access
	 * 
	 * @param directoryOfBikeContract
	 *            - the directory of the bike contract
	 */
	public static void clearAccess(String directory) {

		File directoryOfAcces = new File(
				directory + File.separator + Constants.DECODER + File.separator + Constants.ACCESS);

		for (File file : directoryOfAcces.listFiles()) {
			file.delete();
		}

	}

	/**
	 * AES
	 *
	 * @param message
	 * @return the decrypted
	 * @throws NoSuchPaddingException
	 *             - in case of technical error
	 * @throws NoSuchAlgorithmException
	 *             - in case of technical error
	 * @throws BadPaddingException
	 *             - in case of technical error
	 * @throws IllegalBlockSizeException
	 *             - in case of technical error
	 * @throws InvalidKeyException
	 *             - in case of technical error
	 */
	public static String decrypt(String key, String encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

		if (!encrypted.equals(null)) {

			byte[] z = key.getBytes(Constants.CHARSET);
			SecretKeySpec skeySpec = new SecretKeySpec(z, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] original = cipher.doFinal(hexToByteArray(encrypted));
			return new String(original);
		} else {
			return Constants.EMPTY_STRING;
		}
	}

	/**
	 * hex to byte[] : 16dd
	 * 
	 * @param hex
	 *            hex string
	 * @return
	 */
	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}

		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}

}
