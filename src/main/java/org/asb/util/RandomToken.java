package org.asb.util;

import java.util.Random;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

public class RandomToken {
	
	private static final String charset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVMXYZ";
	
	public static String generate(int length) {
		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int pos = rand.nextInt(charset.length());
			sb.append(charset.charAt(pos));
		}
		return sb.toString();

	}

}
