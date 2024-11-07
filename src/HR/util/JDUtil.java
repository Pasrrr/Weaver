package HR.util;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.namespace.QName;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPHeaderElement;
import java.util.LinkedHashMap;
import com.alibaba.fastjson.JSONObject;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author:
 * @Date:
 * @Description:
 */
public class JDUtil {
    private static Log log = LogFactory.getLog(JDUtil.class.getName());

    private String username;

    private String password;

    public static String employeeFileQuery(JSONArray data) {
        Integer TIMEOUT_SECOND = 600000;
        try {

            String easAddrWs = "http://61.175.194.198:7888";

            Service service = new Service();
            Call call = (Call) service.createCall();

            call.setOperationName("login");
            call.setTargetEndpointAddress(easAddrWs + "/ormrpc/services/EASLogin?wsdl");
            call.setReturnType(new QName("urn:client", "WSContext"));
            call.setReturnClass(WSContext.class);
            call.setReturnQName(new QName("", "loginReturn"));
            call.setTimeout(TIMEOUT_SECOND);
            call.setMaintainSession(true);
            StringBuilder sbuf = new StringBuilder();

            WSContext context = (WSContext) call.invoke(new Object[]{"xuxiongwei", "A123Systems", "eas", "shry", "L2", 2});
            System.out.println("login webservice, sessionId: " + context.getSessionId() + ", dcName: " + context.getDcName() + ",thread = " + Thread.currentThread().getName());
            String sessionId = context.getSessionId();

            call.clearOperation();
            call.setOperationName("employeeFileQuery");
            call.setTargetEndpointAddress(easAddrWs + "/ormrpc/services/WSWxPersonQueryFacade?wsdl");
            call.setReturnQName(new QName("", "employeeListQueryReturn"));
            call.setTimeout(TIMEOUT_SECOND);
            call.setMaintainSession(true);
            SOAPHeaderElement header = new SOAPHeaderElement("http://login.webservice.bos.kingdee.com", "SessionId", sessionId);
            call.addHeader(header);
            String result = (String) call.invoke(new Object[]{data.toString()});
            //String result = (String) call.invoke(new Object[]{""});
            System.out.println("result:" + result);
            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String onboardingFormQuery(JSONObject data) {
        Integer TIMEOUT_SECOND = 600000;
        try {

            String easAddrWs = "http://61.175.194.198:7888";

            Service service = new Service();
            Call call = (Call) service.createCall();

            call.setOperationName("login");
            call.setTargetEndpointAddress(easAddrWs + "/ormrpc/services/EASLogin?wsdl");
            call.setReturnType(new QName("urn:client", "WSContext"));
            call.setReturnClass(WSContext.class);
            call.setReturnQName(new QName("", "loginReturn"));
            call.setTimeout(TIMEOUT_SECOND);
            call.setMaintainSession(true);
            StringBuilder sbuf = new StringBuilder();

            WSContext context = (WSContext) call.invoke(new Object[]{"xuxiongwei", "A123Systems", "eas", "shry", "L2", 2});
            System.out.println("login webservice, sessionId: " + context.getSessionId() + ", dcName: " + context.getDcName() + ",thread = " + Thread.currentThread().getName());
            String sessionId = context.getSessionId();

            call.clearOperation();
            call.setOperationName("onboardingFormQuery");
            call.setTargetEndpointAddress(easAddrWs + "/ormrpc/services/WSWxPersonQueryFacade?wsdl");
            call.setReturnQName(new QName("", "onboardingFormQueryReturn"));
            call.setTimeout(TIMEOUT_SECOND);
            call.setMaintainSession(true);
            SOAPHeaderElement header = new SOAPHeaderElement("http://login.webservice.bos.kingdee.com", "SessionId", sessionId);
            call.addHeader(header);
            String result = (String) call.invoke(new Object[]{data.toString()});
            //String result = (String) call.invoke(new Object[]{""});
            //System.out.println("result:" + result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
