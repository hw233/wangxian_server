package com.fy.gamegateway.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.geronimo.mail.util.Base64;


public class RSAHelper {

	private static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws NoSuchAlgorithmException {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

			StringWriter writer = new StringWriter();
			io(new InputStreamReader(ins), writer, -1);

			byte[] encodedKey = writer.toString().getBytes();

			// 先base64解码
			encodedKey = Base64.decode(encodedKey);

			return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
		} catch (IOException ex) {

		} catch (InvalidKeySpecException ex) {

		}

		return null;
	}

	private static void io(Reader in, Writer out, int bufferSize) throws IOException {
		if (bufferSize == -1) {
			bufferSize = 8192 >> 1;
		}
		char[] buffer = new char[bufferSize];
		int amount;

		while ((amount = in.read(buffer)) >= 0) {
			out.write(buffer, 0, amount);
		}
	}

	public static boolean doCheck(String content, String sign, String publicKey) {
		try {
			PublicKey pubKey = getPublicKeyFromX509("RSA", new ByteArrayInputStream(publicKey.getBytes()));
			Signature signature = Signature.getInstance("SHA1WithRSA");
			signature.initVerify(pubKey);
			signature.update(content.getBytes("UTF-8"));
			return signature.verify(Base64.decode(sign.getBytes()));
		} catch (Exception e) {
			return false;
		}
	}

	private static String readText(InputStream in, String encoding, int bufferSize) throws IOException {
		Reader reader = (encoding == null) ? new InputStreamReader(in) : new InputStreamReader(in, encoding);
		StringWriter writer = new StringWriter();
		io(reader, writer, bufferSize);
		return writer.toString();
	}

	public static String doSign(String content, String privateKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = readText(new ByteArrayInputStream(privateKey.getBytes()), "UTF-8", -1).getBytes();
			// 先base64解码
			encodedKey = Base64.decode(encodedKey);
			PrivateKey priKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));

			Signature signature = Signature.getInstance("SHA1WithRSA");

			signature.initSign(priKey);
			signature.update(content.getBytes("UTF-8"));

			byte[] signed = signature.sign();

			return new String(Base64.encode(signed));
		} catch (Exception e) {
			return "";
		}
	}
}
