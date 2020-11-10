package com.z.utility;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptBase64 {
	private static final char[] BASE64_DIGITS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
	private static final byte[] DECODER_RING = new byte[128];

	static {
		Arrays.fill(DECODER_RING, (byte) -1);
		int i = 0;
		for (final char c : BASE64_DIGITS) {
			DECODER_RING[c] = (byte) i++;
		}
		
		DECODER_RING['='] = 0;
	}
	
	public String Encrypt(String text, String key) throws Exception
    {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] keyBytes= new byte[16];
		byte[] b= key.getBytes("UTF-8");
		int len= b.length;
		
		if (len > keyBytes.length) len = keyBytes.length;
		
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
		cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);
	
		byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
		return encode(results);
    }

	public String Decrypt(String text, String key) throws Exception
    {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] keyBytes= new byte[16];
		byte[] b= key.getBytes("UTF-8");
		int len= b.length;
		
		if (len > keyBytes.length) len = keyBytes.length;
		
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
		cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);
		byte [] results = cipher.doFinal(decode(text));
		return new String(results,"UTF-8");
    }
	
	protected static byte[] decode(String encodedString) {
		final char[] chars = encodedString.toCharArray();

		int newlineCount = 0;
        for (final char c : chars) {
            switch (c) {
            case '\r':
            case '\n':
                newlineCount++;
                break;

            default:
            }
        }

        final int len = chars.length - newlineCount;
        int numBytes = ((len + 3) / 4) * 3;

        // fix up length relative to padding
        if (len > 1) {
            if (chars[chars.length - 2] == '=') {
                numBytes -= 2;
            } else if (chars[chars.length - 1] == '=') {
                numBytes--;
            }
        }

        final byte[] result = new byte[numBytes];
        int newlineOffset = 0;

        // decode each block of 4 chars into 3 bytes
        for (int i = 0; i < (len + 3) / 4; ++i) {
            int charOffset = newlineOffset + (i * 4);

            final char c1 = chars[charOffset++];
            final char c2 = chars[charOffset++];
            final char c3 = chars[charOffset++];
            final char c4 = chars[charOffset++];

            if (!(validChar(c1) && validChar(c2) && validChar(c3) && validChar(c4))) {
                throw new IllegalArgumentException(
                        "Invalid Base64 character in block: '" + c1 + c2 + c3
                                + c4 + "'");
            }

            // pack
            final int x = DECODER_RING[c1] << 18 | DECODER_RING[c2] << 12
                    | (c3 == '=' ? 0 : DECODER_RING[c3] << 6)
                    | (c4 == '=' ? 0 : DECODER_RING[c4]);

            // unpack
            int byteOffset = i * 3;
            result[byteOffset++] = (byte) (x >> 16);
            if (c3 != '=') {
                result[byteOffset++] = (byte) ((x >> 8) & 0xFF);
                if (c4 != '=') {
                    result[byteOffset++] = (byte) (x & 0xFF);
                }
            }

            // skip newlines after block
            outer: while (chars.length > charOffset) {
                switch (chars[charOffset++]) {
                case '\r':
                case '\n':
                    newlineOffset++;
                    break;

                default:
                    break outer;
                }
            }
        }

        return result;
	}
	
	protected String encode(byte[] bytes) {
        return encode(bytes, 0, bytes.length, false);
    }
	
	protected static String encode(byte[] bytes, boolean newlines) {
        return encode(bytes, 0, bytes.length, newlines);
    }

	protected static String encode(byte[] bytes, int off, int len, boolean newlines) {
        final char[] output = new char[(((len + 2) / 3) * 4) + (newlines ? len / 43 : 0)];
        int pos = 0;

        // encode each block of 3 bytes into 4 chars
        for (int i = 0; i < (len + 2) / 3; ++i) {

            int pad = 0;
            if (len + 1 < (i + 1) * 3) {
                // two trailing '='s
                pad = 2;
            } else if (len < (i + 1) * 3) {
                // one trailing '='
                pad = 1;
            }

            // pack
            final int x = (byteAt(bytes, i, off) << 16)
                    | (pad > 1 ? 0 : (byteAt(bytes, i, off + 1) << 8))
                    | (pad > 0 ? 0 : (byteAt(bytes, i, off + 2)));

            // unpack
            output[pos++] = BASE64_DIGITS[x >> 18];
            output[pos++] = BASE64_DIGITS[(x >> 12) & 0x3F];
            output[pos++] = pad > 1 ? '=' : BASE64_DIGITS[(x >> 6) & 0x3F];
            output[pos++] = pad > 0 ? '=' : BASE64_DIGITS[x & 0x3F];

            if (newlines && ((i + 1) % 19 == 0)) {
                output[pos++] = '\n';
            }
        }
        return new String(output, 0, pos);
    }

	protected final static int byteAt(byte[] data, int block, int off) {
        return unsign(data[(block * 3) + off]);
    }
	
	protected final static boolean validChar(char c) {
        return (c < 128) && (DECODER_RING[c] != -1);
    }
	
	protected final static int unsign(byte b) {
        return b < 0 ? b + 256 : b;
    }
}
