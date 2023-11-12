package com.bondarenko.algo.misc;

import java.math.BigInteger;
import java.security.MessageDigest;

public class HashingUtil {

	public static BigInteger hash(String key) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			return new BigInteger(messageDigest.digest(key.getBytes()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
