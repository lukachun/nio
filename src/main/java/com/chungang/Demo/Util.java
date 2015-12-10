package com.chungang.Demo;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class Util {

	private static final String TEST_APP_SECRET = "Lk9e7ZY3G/FBFF7tt0VzTQ==";
	
	public static String encodeRequestBody(String requestBody) {
		try {
			SecretKeySpec key = new SecretKeySpec(TEST_APP_SECRET.getBytes(), "AES");
			Cipher cipher;
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] aesEncrypted = cipher.doFinal(requestBody.getBytes());
			return new String(Base64.encodeBase64(aesEncrypted));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(encodeRequestBody("{\"XiaomiId\":237223406}"));
	}
}
