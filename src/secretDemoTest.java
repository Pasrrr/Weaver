import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 曹嘉伟
 * @Date: 2024/8/28
 * @Description:
 */
class secretDemoTest {

    @Test
    void getSecret() {
        String appCode="AP3GYSNO1TYXRV";
        String appSecret="wHVFVGslCvPQXPjAzanf92XpOUZfxCxA";
        secretDemo secretDemo=new secretDemo();
        String result=secretDemo.getSecret(appCode,appSecret);
        System.out.println(result);
    }
}