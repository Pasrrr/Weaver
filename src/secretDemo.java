
import org.apache.commons.codec.digest.DigestUtils;
public class secretDemo {
    String appCode="AP3GYSNO1TYXRV";
    String appSecret="wHVFVGslCvPQXPjAzanf92XpOUZfxCxA";

    public String getSecret(String appCode,String appSecret){
        System.out.println(appSecret + ":" + appCode + ":" + System.currentTimeMillis());
        return DigestUtils.sha256Hex(appSecret + ":" + appCode + ":" + "1724826846000");
    }
}