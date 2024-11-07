package esteem.jun.sign;

import com.tencentcloudapi.common.CommonClient;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.ess.v20201111.models.*;
import org.apache.axis.encoding.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.general.Util;
import weaver.hrm.User;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ess.v20201111.EssClient;
import com.tencentcloudapi.ess.v20201111.models.UploadFilesRequest;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2023-12-08 20:24
 * @Description:
 */
public class ESigTreasureUtil {
    private static  Log log = LogFactory.getLog(ESigTreasureUtil.class.getName());

    public static Map<String,String> getFileInfoByDocAttachment(String docId, User user) throws Exception{
        DocServiceImpl docService= new DocServiceImpl();
        Map<String,String> map=new HashMap<>();
        DocInfo doc =docService.getDocByUser(Util.getIntValue(docId), user, "");
        DocAttachment[] docAttachments=doc.getAttachments();
        for(int i=0;i<docAttachments.length;i++ ){
            DocAttachment docAttachment=docAttachments[i];
            String content =docAttachment.getFilecontent();
            String fileName=docAttachment.getFilename();
            map.put("content",content);
            map.put("fileName",fileName);
            map.put("filetype","pdf");
        }
        return map;
    }


    public static UploadFile getUploadFileByDocAttachment(String docId, User user) throws Exception{
        DocServiceImpl docService= new DocServiceImpl();
        UploadFile uploadFile=new UploadFile();
        DocInfo doc =docService.getDocByUser(Util.getIntValue(docId), user, "");
        DocAttachment[] docAttachments=doc.getAttachments();
        for(int i=0;i<docAttachments.length;i++ ){
            DocAttachment docAttachment=docAttachments[i];
            String content =docAttachment.getFilecontent();
            String fileName=docAttachment.getFilename();
            uploadFile.setFileBody(content);
            uploadFile.setFileName(fileName);
        }
        return uploadFile;
    }

    public static String uploadFiles(UploadFilesRequest req,String SecretId,String SecretKey,String Endpoint) throws TencentCloudSDKException {
        Credential cred = new Credential(SecretId, SecretKey);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(Endpoint);
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        CommonClient client = new CommonClient("ess", "2020-11-11", cred, "", clientProfile);
        String result = client.call("UploadFiles", UploadFilesResponse.toJsonString(req));
        return result;
    }

    public static String CreateFlowByFiles(CreateFlowByFilesRequest req,String SecretId,String SecretKey,String Endpoint) throws TencentCloudSDKException {
        Credential cred = new Credential(SecretId, SecretKey);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(Endpoint);
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        CommonClient client = new CommonClient("ess", "2020-11-11", cred, "", clientProfile);
        String result = client.call("CreateFlowByFiles", CreateFlowByFilesResponse.toJsonString(req));
        return result;
    }
}