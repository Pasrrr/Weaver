package esteem.jun.sign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ibm.icu.text.SimpleDateFormat;
import com.tencentcloudapi.ess.v20201111.models.*;
import esteem.jun.wanxiang.action.BaseActionInfo;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.interfaces.workflow.action.Action;
import weaver.security.rsa.Base64;
import weaver.soa.workflow.request.RequestInfo;
/**
 * Created with IntelliJ IDEA.
 *
 * @Author:
 * @Date:
 * @Description:
 */
public class DownloadFileV2 extends BaseBean implements Action{

    private Log log = LogFactory.getLog(DownloadFileV2.class.getName());
    private String OperatorId;
    private String SecretId;
    private String SecretKey;
    public static final Map<String,String> status_flag= new HashMap<String,String>();

    static{
        //未执行
        status_flag.put("bs0","0");
        //文件已上传
        status_flag.put("bs1","1");
        //文件准备完成
        status_flag.put("bs2","2");
        //创建签署文件流程完成
        status_flag.put("bs3","3");
        //获取签署链接完成
        status_flag.put("bs4","4");
        //签署完成
        status_flag.put("bs5","5");
        //签署后文件下载完成
        status_flag.put("bs6","6");
    }

    @Override
    public String execute(RequestInfo requestInfo) {
        String requestName = requestInfo.getRequestManager().getRequestname();
        String requestId = requestInfo.getRequestid();
        String workflowId = requestInfo.getWorkflowid();
        Map jsonObj_result = new JSONObject();
        RecordSet recordSet = new RecordSet();
        RecordSet recordSet2 = new RecordSet();
        List docIds=new ArrayList();
        int docId = 0;
        BaseActionInfo baseActionInfo = new BaseActionInfo(requestInfo);
        //用印类型 0-电子章 1-实体章
        String gzlx=baseActionInfo.getMainTableValue("gzlx");
        if(gzlx.equals(status_flag.get("bs0"))){
            try {
                String flowids=baseActionInfo.getMainTableValue("flowid");
                log.info(flowids);
                String[] flowid=flowids.substring(1,flowids.length()-1).split(",");
                log.info("触发流程请求:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:" + requestId + "执行开始!");
                String tableName = requestInfo.getRequestManager().getBillTableName();
                for (int i = 0; i <flowid.length; i++) {
                    String[] BusinessIds = {flowid[i]};
                    //String[] BusinessIds={"yDCWaUUckpv96y4zUEHAGI0yo2qidHsn"};
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserId(OperatorId);
                    log.info("---------------------查询签署文件URL---------------------------begin");
                    DescribeFileUrlsRequest req = new DescribeFileUrlsRequest();
                    req.setOperator(userInfo);
                    req.setBusinessIds(BusinessIds);
                    req.setBusinessType("FLOW");
                    // 返回的resp是一个DescribeFileUrlsResponse的实例，与请求对象对应
                    String resp = QianAPIUtil.DescribeFileUrls(req, SecretId, SecretKey, "ess.tencentcloudapi.com");
                    // 输出json格式的字符串回包
                    JSONObject DescribeFileUrlsRequest = JSON.parseObject(resp);
                    String RequestUrl = DescribeFileUrlsRequest.getJSONObject("Response").getJSONArray("FileUrls").getJSONObject(0).getString("Url");
                    log.info("---------------------查询签署文件URL---------------------------end");
                    log.info("---------------------写入OA---------------------------begin");
                    //new一个URL对象
                    URL url = new URL(RequestUrl);
                    //打开链接
                    URLConnection conn = url.openConnection();
                    //超时响应时间为5秒
                    conn.setConnectTimeout(5 * 1000);
                    //通过输入流获取图片数据
                    InputStream inStream = conn.getInputStream();
                    int byteread;
                    byte data[] = new byte[1024];

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    while ((byteread = inStream.read(data)) != -1) {
                        out.write(data, 0, byteread);
                        out.flush();
                    }
                    data = out.toByteArray();
                    inStream.close();
                    out.close();
                    int dirId = 35;
                    //赋予时间参数
                    Date currentDate = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
                    //获取原文件信息
                    //取文件
                    String qswjfj  = Util.null2String(baseActionInfo.getMainTableValue("xgzfjwds"));
                    if("".equals(qswjfj)){
                        qswjfj = baseActionInfo.getMainTableValue("fj");
                    }
                    String[] docIdlist =qswjfj.split(",");
                    User user = HrmUserVarify.getUser(requestInfo.getRequestManager().getRequest(), null);
                    Map<String, String> fileInfo = ESigTreasureUtil.getFileInfoByDocAttachment(docIdlist[i], user);
                    log.info(fileInfo.toString());
                    String docsub=fileInfo.get("fileName");
                    //创建文档对象，在ecology中附件都是以文档的形式（内含文档附件）存在
                    DocInfo docInfo = new DocInfo();
                    docInfo.setDocSubject(docsub);//文档主题
                    docInfo.setDoccontent("");//文档内容
                    docInfo.setSeccategory(dirId);//存储的磁盘id
                    docInfo.setDoccreaterid(1);//创建者id
                    docInfo.setDoccreatedate(dateFormat.format(currentDate));//文档创建日期
                    docInfo.setDoccreatetime(timeFormat.format(currentDate));//文档创建时间
                    docInfo.setDoclastmoddate(dateFormat.format(currentDate));//文档修改日期
                    docInfo.setDoclastmodtime(timeFormat.format(currentDate));//文档修改时间
                    docInfo.setDoccreatertype(2);//文档创建类型
                    docInfo.setDoclangurage(7);//盲猜文档语言，无关紧要
                    docInfo.setOwnerid(1);//文档拥有者
                    //创建文档附件对象
                    DocAttachment docAttachment = new DocAttachment();
                    docAttachment.setFilename(docsub);//附件名称
                    docAttachment.setFilecontent(Base64.encodeBase64String(data));//附件内容
                    docAttachment.setDocid(0);//文档id
                    docAttachment.setImagefileid(0);//保存文件id
                    //docAttachment.setIsextfile("false");//是否为ext文件
                    docAttachment.setIszip(0);//是否是压缩文件
                    // docAttachment.setDocfiletype("13");//文档文件类型
                    docAttachment.setAesCode("");
                    //将文档附件放置在文档对象中
                    docInfo.setAttachments(new DocAttachment[]{docAttachment});
                    //核心代码，ecology内置插入，会插入文档附件和文档对象，插入成功时会修改文档的id
                    docId = new DocServiceImpl().createDocByUser(docInfo, new User(1));
                    docIds.add(docId);
                    //当id为0时说明创建失败
                    log.info("docid:" + docId);
                }

                //当id为0时说明创建失败
                String gzfj=docIds.toString().replaceAll("\\[|]","");
                String sql="update "+tableName+" set gzfj='"+gzfj.replaceAll(" ","")+"' where requestid="+requestId;
                recordSet2.execute(sql);
                log.info("-------------------------写入OAend-------------------------------");

                if (docId == 0) {
                    //自定义类，返回创建失败信息
                    jsonObj_result.put("flag", "附件创建失败");
                    return Action.FAILURE_AND_CONTINUE;
                } else {
                    //返回创建文档的id
                    return Action.SUCCESS;
                }
            } catch (Exception e) {
                jsonObj_result.put("flag", "代码错误");
                //返回异常信息供前端调用
                return Action.FAILURE_AND_CONTINUE;
            }
        }else{
            log.info("实体章，跳过获取流程");
            return Action.SUCCESS;
        }

    }



    public String getOperatorId() {
        return OperatorId;
    }

    public void setOperatorId(String operatorId) {
        OperatorId = operatorId;
    }

    public String getSecretId() {
        return SecretId;
    }

    public void setSecretId(String secretId) {
        SecretId = secretId;
    }

    public String getSecretKey() {
        return SecretKey;
    }

    public void setSecretKey(String secretKey) {
        SecretKey = secretKey;
    }
}
