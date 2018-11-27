package com.zenith.util;
import java.security.MessageDigest;

public class MD5
{
	public static String getMD5Str(String plainText)
	{
		String md5Str = null;
		try {
			StringBuffer buf = new StringBuffer();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte[] b = md.digest();
			for (int offset = 0; offset < b.length; offset++) {
				int i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			md5Str = buf.toString().substring(8, 24);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5Str;
	}
}