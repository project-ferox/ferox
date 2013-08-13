package com.tantaman.ferox.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

//http://www.rgagnon.com/javadetails/java-0416.html
public class MD5 {
	public static byte[] createChecksum(String filename) throws Exception {
		InputStream fis =  new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	public static String getMD5Checksum(String filename) throws Exception {
		return Hex.getHex(createChecksum(filename));
	}
}
