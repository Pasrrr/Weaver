package esteem.jun.common.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    private static final char[] DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final ThreadLocal<MessageDigest> MESSAGE_DIGEST_LOCAL = new ThreadLocal<MessageDigest>() {
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException var2) {
                return null;
            }
        }
    };

    public MD5Utils() {
    }

    public static String md5Hex(byte[] bytes) throws NoSuchAlgorithmException {
        String var2;
        try {
            MessageDigest messageDigest = (MessageDigest)MESSAGE_DIGEST_LOCAL.get();
            if (messageDigest == null) {
                throw new NoSuchAlgorithmException("MessageDigest get MD5 instance error");
            }

            var2 = encodeHexString(messageDigest.digest(bytes));
        } finally {
            MESSAGE_DIGEST_LOCAL.remove();
        }

        return var2;
    }

    public static String md5Hex(String value, String encode) {
        try {
            return md5Hex(value.getBytes(encode));
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public static String encodeHexString(byte[] bytes) {
        int l = bytes.length;
        char[] out = new char[l << 1];
        int i = 0;

        for(int j = 0; i < l; ++i) {
            out[j++] = DIGITS_LOWER[(240 & bytes[i]) >>> 4];
            out[j++] = DIGITS_LOWER[15 & bytes[i]];
        }

        return new String(out);
    }
}
