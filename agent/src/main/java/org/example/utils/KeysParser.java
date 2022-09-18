package org.example.utils;

import lombok.SneakyThrows;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeysParser {
	@SneakyThrows
	public static RSAPublicKey getPublicKeyFromString(String key) {
		String publicKeyPEM = key;

		publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----\n", "");
		publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
		publicKeyPEM = publicKeyPEM.replaceAll("\\s+","");

		byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
		KeyFactory kf = KeyFactory.getInstance("RSA");

		return (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
	}
}
