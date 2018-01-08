package com.huai.gamesdk.tool;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES加密工具，密钥长度必需为8的倍数
 */
public final class GameDesTool {
	private static String Algorithm = "DES";
	private static String CHARSET = "UTF-8";

	/**
	 * 加密，密钥长度必需为8的倍数
	 * @param key
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static String encode( String key,String text ) throws Exception {
		return GameBase64.encode( encode( key.getBytes( CHARSET ),text.getBytes( CHARSET ) ) );
	}

	/**
	 * 加密，密钥长度必需为8的倍数
	 * @param key
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static byte[] encode( byte[] key,byte[] src ) throws Exception {
		SecretKey secretKey = new SecretKeySpec( key,"DES" );
		Cipher cipher = Cipher.getInstance( Algorithm );
		cipher.init( Cipher.ENCRYPT_MODE,secretKey );
		return cipher.doFinal( src );
	}

	/**
	 * 解密，密钥长度必需为8的倍数
	 * @param key
	 * @param base64Text
	 * @return
	 * @throws Exception
	 */
	public static String decode( String key,String base64Text ) throws Exception {
		return new String( decode( key.getBytes( CHARSET ), GameBase64.decode( base64Text ) ) );
	}

	/**
	 * 解密，密钥长度必需为8的倍数
	 * @param key
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static byte[] decode( byte[] key,byte[] src ) throws Exception {
		SecretKey deskey = new SecretKeySpec( key,"DES" );
		Cipher cipher = Cipher.getInstance( Algorithm );
		cipher.init( Cipher.DECRYPT_MODE,deskey );
		return cipher.doFinal( src );
	}
}
