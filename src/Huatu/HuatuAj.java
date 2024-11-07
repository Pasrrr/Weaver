package Huatu;

import com.alibaba.fastjson.JSON;
import com.api.wx123.RequestBody;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 曹嘉伟
 * @Date: 2024/9/25
 * @Description:
 */
@Path("/Huatu")
public class HuatuAj {

    private static Log log = LogFactory.getLog(HuatuAj.class.getName());

    @POST
    @Path("/Domain")
    @Consumes({MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public String VtExtGetCryptFileDomain(@RequestBody String docid, HttpServletRequest httpServletRequest) {
        log.info("VtExtGetCryptFileDomainAction:接口成功进入");
        log.info("docid:"+docid);
        String result=null;

        try {
            //Map<String, String> fileInfo =VtSdkUtil.getFileInfoByDocAttachment(docid,user);
        }catch (Exception e){
            throw  new RuntimeException();
        }
        log.info("ZLowPRAction:接口结束");
        log.info("JSONresult:"+result);
        return JSON.toJSONString(result);
    }
}
