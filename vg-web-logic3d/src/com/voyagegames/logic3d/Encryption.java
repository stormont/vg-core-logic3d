package com.voyagegames.logic3d;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    private static final String HEX = "0123456789ABCDEF";
    
    public static String encrypt(final String seed, final String cleartext) throws Exception {
    	return toHex(encrypt(seed, cleartext.getBytes()));
    }
    
    public static String decrypt(final String seed, final String encrypted) throws Exception {
    	return new String(decrypt(seed, toByte(encrypted)));
    }
    
    public static byte[] encrypt(final String seed, final byte[] cleartext) throws Exception {
    	final byte[] rawKey = getRawKey(seed.getBytes());
    	final byte[] result = encrypt(rawKey, cleartext);
        
    	return result;
    }
    
    public static byte[] decrypt(final String seed, final byte[] encrypted) throws Exception {
    	final byte[] rawKey = getRawKey(seed.getBytes());
    	final byte[] result = decrypt(rawKey, encrypted);
        
    	return result;
    }

    // TODO Not sure we need these...
    /*
    public static String toHex(final String txt) {
    	return toHex(txt.getBytes());
    }
    
    public static String fromHex(final String hex) {
    	return new String(toByte(hex));
    }
    */

    private static byte[] getRawKey(final byte[] seed) throws Exception {
    	final KeyGenerator kgen = KeyGenerator.getInstance("AES");
    	final SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        
    	sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        
        final SecretKey skey = kgen.generateKey();
        
        return skey.getEncoded();
    }

    private static byte[] encrypt(final byte[] raw, final byte[] clear) throws Exception {
    	final SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        final Cipher cipher = Cipher.getInstance("AES");
        
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(clear);
    }

    private static byte[] decrypt(final byte[] raw, final byte[] encrypted) throws Exception {
        final SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        final Cipher cipher = Cipher.getInstance("AES");
        
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(encrypted);
    }
    
    private static byte[] toByte(final String hexString) {
		final int len = hexString.length() / 2;
		final byte[] result = new byte[len];
		
		for (int i = 0; i < len; ++i) {
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		}
		
		return result;
    }

    private static String toHex(final byte[] buf) {
		if (buf == null) {
			return null;
		}
		
		StringBuffer result = new StringBuffer(2 * buf.length);
		
		for (int i = 0; i < buf.length; ++i) {
			appendHex(result, buf[i]);
		}
		
		return result.toString();
    }
    
    private static void appendHex(final StringBuffer sb, final byte b) {
    	sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
    
}
