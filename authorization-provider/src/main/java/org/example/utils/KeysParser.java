package org.example.utils;

import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeysParser {
	@SneakyThrows
	public static RSAPrivateKey getPrivateKeyFromString(String key) {
		String privateKeyPEM = key;
		privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----\n", "");
		privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
		privateKeyPEM = privateKeyPEM.replaceAll("\\s+","");

		byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
		KeyFactory kf = KeyFactory.getInstance("RSA");

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
		return (RSAPrivateKey) kf.generatePrivate(keySpec);
	}
}
