package esteem.jun.QKL.action;

import com.alibaba.fastjson.JSON;
import com.api.doc.search.util.DocLinkUtil;

import esteem.jun.common.util.Base64Utils;
import esteem.jun.common.util.MD5Utils;
import esteem.jun.common.util.ModeDataUtil;
import esteem.jun.sign.ESigTreasureUtil;
import esteem.jun.wanxiang.action.BaseActionInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.MD5;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import weaver.workflow.request.RequestCheckAddinRules;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 曹嘉伟
 * @Date: 2024/10/29
 * @Description:
 */
public class PayapplicationQKL extends BaseBean implements Action {

    private Log log = LogFactory.getLog(PayapplicationQKL.class.getName());

    private String accessKey;

    private String modeid;

    private String HOST;

    @Override
    public String execute(RequestInfo requestInfo) {
        String requestName = requestInfo.getRequestManager().getRequestname();
        User user = HrmUserVarify.getUser(requestInfo.getRequestManager().getRequest(), null);
        String requestId = requestInfo.getRequestid();
        BaseActionInfo baseActionInfo = new BaseActionInfo(requestInfo);
        String formId = baseActionInfo.getFormId();
        String tableName = requestInfo.getRequestManager().getBillTableName();
        log.info("触发流程请求:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId  执行开始!");
        JSONObject sendobj=new JSONObject();
        //判断是否触发
          String ZTERM=baseActionInfo.getMainTableValue("ZTERM");
          String ZTERM2=ZTERM.substring(0,2);
          log.info("ZTERM2:"+ZTERM2);
        try {
          if (ZTERM2.equals("ZQ")){
              //开立信息
                  sendobj.put("accessKey",accessKey);
                  /**买方统一社会信用代码-固定值*/
                  String buyerSocialCode="91330000577307779U";
                  sendobj.put("buyerSocialCode",buyerSocialCode);
                  /**合同卖方社会信用代码-固定值*/
                  String sellerSocialCode=Util.null2String(baseActionInfo.getMainTableValue("mftyshxydm"));
                  sendobj.put("sellerSocialCode",sellerSocialCode);
                  /**付款金额*/
                  Double payAmount=Double.parseDouble(baseActionInfo.getMainTableValue("jehj"))+Double.parseDouble(baseActionInfo.getMainTableValue("WMWST"));
                  payAmount= Double.parseDouble(new DecimalFormat("#.00").format(payAmount));
                  sendobj.put("payAmount",payAmount);
                  /**期望付款日期*/
                  String paymentDate= baseActionInfo.getMainTableValue("CPUDT");
                  sendobj.put("paymentDate",paymentDate);
                  /**预制发票号*/
                  String preAcctNo= baseActionInfo.getMainTableValue("BELNR");
                  sendobj.put("preAcctNo",preAcctNo);
                  /**审批通过日期*/
                  Date date=new Date();
                  SimpleDateFormat dateFormat=new SimpleDateFormat("YYYY-MM-dd");
                  String approveDate=dateFormat.format(date);
                  sendobj.put("approveDate",approveDate);
                  /**流程单号*/
                  String orderNumber=baseActionInfo.getMainTableValue("dh");
                  sendobj.put("orderNumber",orderNumber);
                  /**账期-*/
                  String dueDate=baseActionInfo.getMainTableValue("zq");
                  sendobj.put("dueDate",dueDate);

                  /**补充附件*/
                  String bcfj=baseActionInfo.getMainTableValue("bczl");
                  JSONArray mainfileList=new JSONArray();
                  String[] maindocIdlist = bcfj.split(",");
                  DocLinkUtil docLinkUtil=new DocLinkUtil();
                  for (int j = 0; j < maindocIdlist.length; j++) {
                      JSONObject file=new JSONObject();
                      RecordSet recordSet=new RecordSet();
                      String sqlbc="select * from docimagefile where docid="+maindocIdlist[j];
                      recordSet.execute(sqlbc);
                      String imagefileid="";
                      while (recordSet.next()){
                          imagefileid=recordSet.getString("imagefileid");
                      }
                      log.info("imagefileid:"+imagefileid);
                      String docurl =docLinkUtil.getThirdpartyDownloadLinkByFileid(imagefileid,user);
                      Map<String, String> fileInfo = ESigTreasureUtil.getFileInfoByDocAttachment(maindocIdlist[j], user);
                      String docsub=fileInfo.get("fileName");
                      file.put("fileName",docsub);
                      //file.put("fileUrl","https://img1.baidu.com/it/u=617614446,2150677945&fm=253&fmt=auto&app=120&f=JPEG?w=1237&h=500");
                      file.put("fileUrl","http://220.189.215.149:12380"+docurl);
                      log.info("filename+fileUrl:"+docsub+"/"+docurl);
                      mainfileList.put(file);
                      sendobj.put("fileList",mainfileList);
                  }


                  //合同信息,框架合同时关联订单信息
                  //判断是否框架合同
                  JSONArray contractInfo=new JSONArray();
                  String slsfyg=baseActionInfo.getMainTableValue("slsfyg");
                  String contract=baseActionInfo.getMainTableValue("glhtlc");
                  String Order =baseActionInfo.getMainTableValue("glddlc");
                  if (slsfyg.equals("01")){
                      String[] contractList = Order.split(",");
                      for (int i = 0; i < contractList.length; i++) {
                          RecordSet recordSet=new RecordSet();
                          String sql="select * from formtable_main_398 where requestid="+contractList[i];
                          recordSet.executeQuery(sql);
                          JSONObject contractobj=new JSONObject();
                          while (recordSet.next()) {
                              contractobj.put("contractCode",Util.null2String(recordSet.getString("EBELN")));//合同编号
                              contractobj.put("contractName","订单"+i);//合同名称
                              RecordSet recordSetdt =new RecordSet();
                              String mainid=recordSet.getString("id");
                              String sqldt="select * from formtable_main_398_dt1 where EBELP='00010' and mainid="+mainid;
                              log.info("sqldt:"+sqldt);
                              recordSetdt.executeQuery(sqldt);
                              while (recordSetdt.next()){
                                  contractobj.put("productName",Util.null2String(recordSetdt.getString("wlmc")));//产品名称
                              }
                              contractobj.put("beginDate",Util.null2String(recordSet.getString("BEDAT")));//合同起始日期
                              contractobj.put("endDate",Util.null2String(recordSet.getString("BEDAT")));//合同终止日期
                              contractobj.put("signDate",Util.null2String(recordSet.getString("BEDAT")));//合同签署日期
                              contractobj.put("sellerName",Util.null2String(recordSet.getString("gysmc")));//卖方名称
                              contractobj.put("sellerCreditCode",Util.null2String(recordSet.getString("mftyshbm")));//卖方统一社会信用代码
                              contractobj.put("buyerName","万向一二三股份公司");//买方名称
                              contractobj.put("buyerCreditCode","91330000577307779U");//买方统一社会信用代码
                              contractobj.put("amount",Util.null2String(recordSet.getString("ddzje")));//合同金额
                              contractobj.put("countFlag","01");//数量是否预估
                              String htfj=baseActionInfo.getMainTableValue("ddfj");
                              log.info("htfj:"+htfj);
                              JSONArray fileList=new JSONArray();
                              String[] dddocIdlist = htfj.split(",");
                              for (int j = 0; j < dddocIdlist.length; j++) {
                                  JSONObject file=new JSONObject();
                                  String sqldd="select * from docimagefile where docid="+dddocIdlist[j];
                                  recordSet.execute(sqldd);
                                  String imagefileid="";
                                  while (recordSet.next()){
                                      imagefileid=recordSet.getString("imagefileid");
                                  }
                                  log.info("imagefileid:"+imagefileid);
                                  String docurl =docLinkUtil.getThirdpartyDownloadLinkByFileid(imagefileid,user);
                                  Map<String, String> fileInfo = ESigTreasureUtil.getFileInfoByDocAttachment(dddocIdlist[j], user);
                                  String docsub=fileInfo.get("fileName");
                                  file.put("fileName",docsub);
                                  //file.put("fileUrl","https://img1.baidu.com/it/u=617614446,2150677945&fm=253&fmt=auto&app=120&f=JPEG?w=1237&h=500");
                                  file.put("fileUrl","http://220.189.215.149:12380"+docurl);
                                  log.info("filename+fileUrl:"+docsub+"/"+docurl);
                                  fileList.put(file);
                              }
                              contractobj.put("fileList",fileList);
                              contractInfo.put(contractobj);
                          }

                      }
                  }else {
                      String[] contractList = contract.split(",");
                      for (int i = 0; i < contractList.length; i++) {
                          RecordSet recordSet=new RecordSet();
                          String sql="select * from formtable_main_396 where requestid="+contractList[i];
                          recordSet.executeQuery(sql);
                          JSONObject contractobj=new JSONObject();
                          while (recordSet.next()) {
                              contractobj.put("contractCode",Util.null2String(recordSet.getString("htbh")));//合同编号
                              contractobj.put("contractName",Util.null2String(recordSet.getString("bz")));//合同名称
                              contractobj.put("productName",Util.null2String(recordSet.getString("bdwmc")));//产品名称
                              contractobj.put("beginDate",Util.null2String(recordSet.getString("KDATB")));//合同起始日期
                              contractobj.put("endDate",Util.null2String(recordSet.getString("KDATE")));//合同终止日期
                              contractobj.put("signDate",Util.null2String(recordSet.getString("KDATE")));//合同签署日期
                              contractobj.put("sellerName",Util.null2String(recordSet.getString("htdfdw")));//卖方名称
                              contractobj.put("sellerCreditCode",Util.null2String(recordSet.getString("mftyshxydm")));//卖方统一社会信用代码
                              contractobj.put("buyerName","万向一二三股份公司");//买方名称
                              contractobj.put("buyerCreditCode","91330000577307779U");//买方统一社会信用代码
                              contractobj.put("amount",Util.null2String(recordSet.getString("ybjehsdx")));//合同金额
                              contractobj.put("countFlag",Util.null2String(recordSet.getString("slsfyg")));//数量是否预估
                              String htfj=recordSet.getString("xgzhtfj");
                              log.info("htfj:"+htfj);
                              JSONArray fileList=new JSONArray();
                              String[] htdocIdlist = htfj.split(",");
                              for (int j = 0; j < htdocIdlist.length; j++) {
                                  JSONObject file=new JSONObject();
                                  String sqlht="select * from docimagefile where docid="+htdocIdlist[j];
                                  recordSet.execute(sqlht);
                                  String imagefileid="";
                                  while (recordSet.next()){
                                      imagefileid=recordSet.getString("imagefileid");
                                  }
                                  log.info("imagefileid:"+imagefileid);
                                  String docurl =docLinkUtil.getThirdpartyDownloadLinkByFileid(imagefileid,user);
                                  Map<String, String> fileInfo = ESigTreasureUtil.getFileInfoByDocAttachment(htdocIdlist[j], user);
                                  String docsub=fileInfo.get("fileName");
                                  file.put("fileName",docsub);
                                  //file.put("fileUrl","https://img1.baidu.com/it/u=617614446,2150677945&fm=253&fmt=auto&app=120&f=JPEG?w=1237&h=500");
                                  file.put("fileUrl","http://220.189.215.149:12380"+docurl);
                                  log.info("filename+fileUrl:"+docsub+"/"+docurl);
                                  fileList.put(file);

                              }
                              contractobj.put("fileList",fileList);
                              contractInfo.put(contractobj);
                          }

                      }
                  }
                  sendobj.put("contractList",contractInfo);

                  //发票信息
                  JSONArray invoiceInfo=new JSONArray();
                  JSONObject invoiceobj=new JSONObject();
                  RecordSet recordSetmode=new RecordSet();
                  Map<String,Object> map=new HashMap<>();
                  String invoice=baseActionInfo.getMainTableValue("XBLNR");
                  String[] invoiceList = invoice.split("-");
                  for (int i = 0; i < invoiceList.length; i++) {
                      map.put("BELNR",preAcctNo);
                      //发票号码
                      String invoiceNumber=Util.null2String(invoiceList[i]);
                      log.info(invoiceNumber);
                      map.put("XBLNR",invoiceNumber);

                      //发票类型
                      String invoiceType=Util.null2String(baseActionInfo.getMainTableValue("fplx"));
                      map.put("BLART",invoiceType);
                      //开票日期
                      String openDate=Util.null2String(baseActionInfo.getMainTableValue("BLDAT"));
                      map.put("BLDAT",openDate);
                      //卖方名称
                      String sellerName=Util.null2String(baseActionInfo.getMainTableValue("NAME_ORG1"));
                      map.put("sellerName",sellerName);
                      //卖方统一社会信用代码
                      String sellerCreditCode=Util.null2String(baseActionInfo.getMainTableValue("mftyshxydm"));
                      map.put("sellerCreditCode",sellerCreditCode);
                      //含税金额
                      String beforeTaxAmount="";
                      //未税金额
                      String afterTaxAmount="";
                      //校验码
                      String checkCode="";
                      RecordSet recordSetfp=new RecordSet();
                      String sql="select * from uf_fpxx where ZFPBM="+invoiceList[i];
                      recordSetfp.executeQuery(sql);
                      while (recordSetfp.next()) {
                          beforeTaxAmount=Util.null2String(recordSetfp.getString("ZFPJE"));
                          afterTaxAmount=Util.null2String(recordSetfp.getString("ZBHSJE"));
                          invoiceobj.put("beforeTaxAmount",beforeTaxAmount);//含税金额
                          map.put("beforeTaxAmount",beforeTaxAmount);
                          invoiceobj.put("afterTaxAmount",afterTaxAmount);//不含税金额
                          map.put("afterTaxAmount",afterTaxAmount);
                          //验证码
                          checkCode=Util.null2String(Util.null2String(recordSetfp.getString("ZBHSJE")));
                          //log.info("checkCode:"+checkCode);
                          map.put("checkCode",Util.null2String(getLastSixChars(checkCode)));
                      }
                      invoiceobj.put("invoiceNumber",invoiceNumber);
                      invoiceobj.put("invoiceCode","");//发票代码
                      invoiceobj.put("invoiceType",invoiceType);
                      invoiceobj.put("checkCode",checkCode);
                      invoiceobj.put("openDate",openDate);
                      invoiceobj.put("sellerName",sellerName);
                      invoiceobj.put("sellerCreditCode",sellerCreditCode);
                      invoiceobj.put("buyerName","万向一二三股份公司");//买方名称
                      map.put("buyerName","万向一二三股份公司");
                      invoiceobj.put("buyerCreditCode","91330000577307779U");//买方统一社会信用代码
                      map.put("buyerCreditCode","91330000577307779U");
                      JSONArray fileList=new JSONArray();
                      String fpfj=baseActionInfo.getMainTableValue("fpfj");
                      String[] fpdocIdlist = fpfj.split(",");
                      //for (int j = 0; j < fpdocIdlist.length; j++) {
                      JSONObject file=new JSONObject();
                      RecordSet recordSetdt=new RecordSet();
                      String sqlbc="select * from docimagefile where docid="+fpdocIdlist[i];
                      recordSetdt.execute(sqlbc);
                      String imagefileid="";
                      while (recordSetdt.next()){
                          imagefileid=recordSetdt.getString("imagefileid");
                      }
                      log.info("imagefileid:"+imagefileid);
                      String docurl =docLinkUtil.getThirdpartyDownloadLinkByFileid(imagefileid,user);
                      Map<String, String> fileInfo = ESigTreasureUtil.getFileInfoByDocAttachment(fpdocIdlist[i], user);
                      String docsub=fileInfo.get("fileName");
                      file.put("fileName",docsub);
                      //file.put("fileUrl","https://img1.baidu.com/it/u=617614446,2150677945&fm=253&fmt=auto&app=120&f=JPEG?w=1237&h=500");
                      file.put("fileUrl","http://220.189.215.149:12380"+docurl);
                      fileList.put(file);
                      //}
                      invoiceobj.put("fileList",fileList);
                      invoiceInfo.put(invoiceobj);
                      log.info(invoiceInfo.toString());
                      String sqlivmode="select * from uf_Ivmode where XBLNR ='"+invoiceNumber+"'";
                      recordSetmode.execute(sqlivmode);
                      if(recordSetmode.next()){
                          if(!invoiceType.equals(Util.null2String(recordSetmode.getString("BLART")))){
                              map.put("id",Util.null2String(recordSetmode.getString("id")));
                          }if(!openDate.equals(Util.null2String(recordSetmode.getString("BLDAT")))){
                              map.put("id",Util.null2String(recordSetmode.getString("id")));
                          }if(!sellerName.equals(Util.null2String(recordSetmode.getString("sellerName")))){
                              map.put("id",Util.null2String(recordSetmode.getString("id")));
                          }if(!sellerCreditCode.equals(Util.null2String(recordSetmode.getString("sellerCreditCode")))){
                              map.put("id",Util.null2String(recordSetmode.getString("id")));
                          }if(!beforeTaxAmount.equals(Util.null2String(recordSetmode.getString("beforeTaxAmount")))){
                              map.put("id",Util.null2String(recordSetmode.getString("id")));
                          }if(!afterTaxAmount.equals(Util.null2String(recordSetmode.getString("afterTaxAmount")))) {
                              map.put("id", Util.null2String(recordSetmode.getString("id")));
                          }if(!checkCode.equals(Util.null2String(recordSetmode.getString("checkCode")))){
                              map.put("id",Util.null2String(recordSetmode.getString("id")));
                          }
                      }
                      //log.info("map:"+map.toString());
                  }
                  ModeDataUtil.SaveModeDataInfo(map,modeid,"1");
                  log.info(invoiceInfo);
                  sendobj.put("invoiceList",invoiceInfo);
                  //log.info("accessKey+preAcctNo:"+accessKey+preAcctNo);
                  String parm=accessKey+preAcctNo;
                  String signStr= MD5Utils.md5Hex(parm,"utf-8");
                  sendobj.put("signStr",signStr);
                  log.info("请求参数："+sendobj.toString());
                  //发送至区块链
                  String Result=Send2QKL(sendobj.toString(),HOST);
                  //String Result="";
                  log.info("Result:"+Result);
                  com.alibaba.fastjson.JSONObject Resultobj=JSON.parseObject(Result);
                  log.info("返回参数："+Resultobj.toString());
                  String returnCode=Resultobj.getString("returnCode");
                  String returnDesc=Resultobj.getString("returnDesc");
                  if (returnCode.equals("17000")){
                      return Action.SUCCESS;
                  }else {
                      log.info("触发流程:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:" + requestId + ";程序异常!");
                      requestInfo.getRequestManager().setMessage(returnCode);
                      requestInfo.getRequestManager().setMessagecontent(returnDesc);
                      return Action.FAILURE_AND_CONTINUE;
                  }

              }else {
              log.info("非债权凭证，跳过接口");
              return Action.SUCCESS;
          }
        }catch (Exception e){
            //返回异常信息供前端调用
            log.info("触发流程:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:" + requestId + ";程序异常!");
            requestInfo.getRequestManager().setMessage("111100");
            requestInfo.getRequestManager().setMessagecontent(e.toString());
            return Action.FAILURE_AND_CONTINUE;
        }
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getModeid() {
        return modeid;
    }

    public void setModeid(String modeid) {
        this.modeid = modeid;
    }

    public String getHOST() {
        return HOST;
    }

    public void setHOST(String HOST) {
        this.HOST = HOST;
    }

    public static String md5(String param) {
        if (param != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(param.getBytes("utf-8"));
                return Base64Utils.encode(md.digest());
            } catch (NoSuchAlgorithmException var2) {
                var2.printStackTrace();
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
            }
        }

        return null;
    }

    public static String Send2QKL(String jsobj,String HOST){
        String Result="";
        try {
            System.setProperty("https.protocols","TLSv1,TLSv1.1,TLSv1.2");
            HttpClient httpClient = createSSLInsecureClient();
            HttpPost httpPost = new HttpPost(HOST);
            httpPost.addHeader("Content-Type", "application/json");
            HttpEntity httpEntity = new StringEntity(jsobj, "UTF-8");
            httpPost.setEntity(httpEntity);
            HttpResponse response = httpClient.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            Result = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (ClientProtocolException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result;
    }

    public static CloseableHttpClient createSSLInsecureClient() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

/*    public static String HYHTSQL(String invoice) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url="jdbc:mysql://10.80.120.11:3306/rds-cpx?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";
        String username="wx123";
        String sql="select identifyingCode FROM t_fp_zzszyfp WHERE number_of_invoice="+invoice;
        String password="wx123@Admin";
        String checkCode="";
        Connection  conn= DriverManager.getConnection(url,username,password);
        Statement stmt=conn.createStatement();
        ResultSet rs=stmt.executeQuery(sql);
        while (rs.next()){
            checkCode=rs.getString("identifyingCode");
        }
        return checkCode;

    }*/
    public static String getLastSixChars(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        return str.length() > 6 ? str.substring(str.length() - 6) : str;
    }
}

