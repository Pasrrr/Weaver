package Huatu;

import com.alibaba.fastjson.JSONObject;
import com.ibm.icu.text.SimpleDateFormat;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import esteem.jun.sign.ESigTreasureUtil;
import esteem.jun.wanxiang.action.BaseActionInfo;
import esteem.jun.wanxiang.action.PurchaseApplicationActionV3;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.misc.BASE64Decoder;
import weaver.conn.RecordSet;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.general.BaseBean;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.integration.framework.data.RecordData;
import weaver.interfaces.workflow.action.Action;
import weaver.security.rsa.Base64;
import weaver.soa.workflow.request.RequestInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static weaver.general.xcommon.IOUtils.toByteArray;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 曹嘉伟
 * @Date: 2024/9/24
 * @Description:
 */
public class VtSdk extends BaseBean implements Action {
    private Log log = LogFactory.getLog(VtSdk.class.getName());
    //解密服务器ip
    private  String ip;
    //端口
    private int port;
    //appid
    private String appid;
    //key
    private String Stringappkey;
    //附件字段



    @Override
    public String execute(RequestInfo requestInfo) {
        String requestName = requestInfo.getRequestManager().getRequestname();
        String requestId = requestInfo.getRequestid();
        log.info("触发流程请求:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId  执行开始!");
        BaseActionInfo baseActionInfo = new BaseActionInfo(requestInfo);
        String formId = baseActionInfo.getFormId();
        String tableName = requestInfo.getRequestManager().getBillTableName();
        List docIds=new ArrayList();
        RecordSet recordSet = new RecordSet();
        RecordSet recordSet2 = new RecordSet();
        Map jsonObj_result = new JSONObject();
        try {
            //源文件信息
            String strFile = baseActionInfo.getMainTableValue("djmwj");
            String[] docIdlist = strFile.split(",");
            for (int i = 0; i < docIdlist.length; i++) {
                User user = HrmUserVarify.getUser(requestInfo.getRequestManager().getRequest(), null);
                Map<String, String> fileInfo = VtSdkUtil.getFileInfoByDocAttachment(docIdlist[i], user);
                int sdkReslut = VtSdkUtil.VtExtInitWithServer(ip, port, appid, Stringappkey, 1);
                byte[] headBuff = new byte[1024];
                byte[] buffRes;
                byte[] data = new byte[20971520];
                BASE64Decoder base64Decoder = new BASE64Decoder();
                try {
                    buffRes = base64Decoder.decodeBuffer(fileInfo.get("content"));
                    log.info(buffRes);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                IntByReference nHeadLength = new IntByReference();
                nHeadLength.setValue(buffRes.length);
                log.info(buffRes.length);
                LongByReference ulHeader = new LongByReference();
                //读取文件前面1024字节判断, 默认读取文件1024字节进行判断即可
                int result = VtSdkUtil.VtExtIsCryptHeader(buffRes, nHeadLength, ulHeader);
                //log.info("nHeadLength:" + nHeadLength + ";ulHeader:" + ulHeader);
                if (result!=0){
                    continue;
                }
                log.info("VtExtIsCryptHeader:" + result);
                InputStream fs = new ByteArrayInputStream(buffRes);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] datecontent;
                //使得fs偏移1024，过滤加密头
                fs.read(headBuff, 0, 1024);
                int offset = 0;
                int length = 0;
                while ((length = fs.read(data, 0, 20971520)) != -1) {
                    result = VtSdkUtil.VtExtDecryptBuffer(ulHeader.getValue(), offset, data, length);
                    out.write(data, 0, length);
                    out.flush();
                    offset += length;
                }
                VtSdkUtil.VtExtDeleteHeader(ulHeader.getValue());
                datecontent = out.toByteArray();
                fs.close();
                log.info("VtExtDecryptBuffer:" + result);

                //读取存储磁盘id，默认81
                //getIntFromRequestHeader方法意为读取请求头“dirId”键的值，如不存在则默认为81
                int dirId = 35;
                //赋予时间参数
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
                //获取原文件信息

                String fjinfo = "SELECT IMAGEFILENAME FROM `docimagefile` WHERE DOCID=" + docIdlist[i];
                recordSet.execute(fjinfo);
                String docsub = "";
                if (recordSet.next()) {
                    docsub = recordSet.getString("IMAGEFILENAME");
                    log.info("docsub:" + docsub);
                }
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
                docAttachment.setFilecontent(Base64.encodeBase64String(datecontent));//附件内容
                docAttachment.setDocid(0);//文档id
                docAttachment.setImagefileid(0);//保存文件id
                //docAttachment.setIsextfile("false");//是否为ext文件
                docAttachment.setIszip(0);//是否是压缩文件
                // docAttachment.setDocfiletype("13");//文档文件类型
                docAttachment.setAesCode("");
                //将文档附件放置在文档对象中
                docInfo.setAttachments(new DocAttachment[]{docAttachment});
                //核心代码，ecology内置插入，会插入文档附件和文档对象，插入成功时会修改文档的id
                int docId = new DocServiceImpl().createDocByUser(docInfo, new User(1));
                //当id为0时说明创建失败
                log.info("docid:" + docId);
                docIds.add(docId);
            }
            //当id为0时说明创建失败
            log.info("docIds:"+docIds.toString());
            if (docIds.isEmpty()){
                jsonObj_result.put("flag","上传文件为解密文件，无需解密");
                requestInfo.getRequestManager().setMessage("111100");
                requestInfo.getRequestManager().setMessagecontent("上传文件为解密文件，无需解密");
                return Action.FAILURE_AND_CONTINUE;
            }else {
            String jmwj=docIds.toString().replaceAll("\\[|]","");
            String sql = "update " + tableName + " set jmwj='" + jmwj.replaceAll(" ","") + "' where requestid=" + requestId;
            recordSet2.execute(sql);
            //返回创建文档的id
            return Action.SUCCESS;
            }
        } catch (Exception e) {
            //返回异常信息供前端调用
            jsonObj_result.put("flag", "代码错误");
            return Action.FAILURE_AND_CONTINUE;
        }
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getStringappkey() {
        return Stringappkey;
    }

    public void setStringappkey(String stringappkey) {
        Stringappkey = stringappkey;
    }
}
