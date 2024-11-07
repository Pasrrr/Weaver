package Dahuaesb;

import weaver.general.Util;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

/**
 * @author rkun
 * @title: DHncrypt
 * @description: TODO
 * @date 2023-04-03 17:05
 */
public class DH_GetNcrypt {
    public String execute(Map param) {
        String publicKey = Util.null2String(param.get("publicKey"));
        String password = Util.null2String(param.get("password"));
        String encrypt = encrypt(publicKey, password);
        return encrypt;
        }
    

//    public static void main(String[] args) {
//        String encrypt = encrypt("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBa7JTVf1eWKU5yPw8Wxf9JFRx4tKm1neBJcAu47YntcZTmZGtjgcF5QPlz+uWL+U0H2QO0wPk0X1nTy+pRqCUoFj/4xPP9POHpdtViPCwS4TJM725iQduXQ6Sxktt+KPLNoPzzXaGvGFObCXmJ/dWyg+cBvjr12AqRz/39BJDJwIDAQAB",
//                "Admin123");
//        System.out.println(encrypt);
//    }
    /**
     * RSA公钥加密
     *
     * @param publicKey 公钥
     * @param password 待加密的密码
     * @return 密文
     */
    public static String encrypt(String publicKey,String password){
        try {
            byte[] decoded = java.util.Base64.getDecoder().decode(publicKey.getBytes("UTF-8"));
            RSAPublicKey pubKey =
                    (RSAPublicKey)
                            KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            // RSA加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            //**此处Base64编码，开发者可以使用自己的库**
            String outStr = java.util.Base64.getEncoder().encodeToString(cipher.doFinal(password.getBytes("UTF-8")));
            //outStr 是加密密文
            System.out.println(outStr);
            return outStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
