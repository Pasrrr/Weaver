package JHX;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.yzj.img.service.UploadImg;
import com.yzj.img.utils.ImageBean;
import esteem.jun.common.util.ArrayUtils;
import esteem.jun.common.util.Base64Utils;
import esteem.jun.sign.ESigTreasureUtil;
import esteem.jun.wanxiang.action.BaseActionInfo;
import sun.misc.BASE64Decoder;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.Base64.Decoder;


public class CWYKSH extends BaseBean implements Action {
    public static Base64.Encoder encoder;
    private static Log log = LogFactory.getLog(CWYKSH.class);
    private static final String KEY_ALGORITHM="RSA";
    private static final String SIGNATRUE_ALGORITHM = "MD5withRSA";
    private static final Decoder BASE64_DECODER = Base64.getDecoder();
    public static String appSecret = "YtkGr@Nm3a$2kyW$";
    /**测试环境秘钥*/
    //public static String privatekey = "MzA4MjAyNzcwMjAxMDAzMDBkMDYwOTJhODY0ODg2ZjcwZDAxMDEwMTA1MDAwNDgyMDI2MTMwODIwMjVkMDIwMTAwMDI4MTgxMDA5ZTQ5MGMxOGQ3NDM5NTU0Mzg2OGMwZDcxODNjMGU2ZDg4ZjlhODNiZGY4YTYxNDQwNWRiNmNmYzNjZGE1NDg0ZjVlMjA3YTA3NzIzOTA4NWE0MmZlY2U4MzdmMTc0ODk4YjgzMzNjMDgyYWUwMjM0NDQ5Mjk2OWQ0ZTQ0ZDQ0MjkxM2ZhNGE0NWFhMzUwMTI1N2ZiOWIxM2E2MGFjNjQzOWMyNDNjOWJiOWE5ZGZmMzI0YTc0YjA0MWFkMzQxNDg4MzMxMjMyMmIzNWUxMDc4ODJhMDc4ZTI3ODY1NjMzNjA5NDdmYTAwNDliNDI1MDFiNDA0ZDllODI5ZjEzZjdiMDIwMzAxMDAwMTAyODE4MDczMDgwYjJlOTI2MjlmNDc5MWE3MmY1ZTIyNTJhMmY5OWZhZTMwNDEzMmY3ZDIzMDI5NDE4YTM2ZDVkMmE1ZDFlYjBmZDUxMzFkMDI0ZmE5OGZlNGY0N2FkZjBkMmVmMDM2ZGZhNmUwYTUxN2U4YWEyYjliOGNiMWNkOGJkZmU1NGMwNTkzMDk5NWE2YmM2YmZjOTEzMmM0YzBkNjA0ZTNmYjVlNTRhMzFlM2Q1YTJlYjM4MGMyZDY3NzQ5MjFjNzEwYzM3ZmY1MzFmOTliNzc5MDk4MWMxZGRjMmFiNTg3ZjdhYWI2MDNiZmRiNjE2MDI1NGZlMDI4ODE3OWYwMDEwMjQxMDBjZmQ2ZWJhOTAwMTQ1ZjJkMDI1YmU4NDE0YzM0M2Y1MDI5ZmZmNjg0OGI0ZjRkYjRkYzc0MjEwMGI4MzIyM2ZiMGVmZDI5OGExYjhiZDFlNDIyOTk1YmM0N2ViNTU0ZTM0MmNjODhmMjk0MmMzOThkMmNjZmMwNTM5ODY1OTIwMTAyNDEwMGMyZjY5MDE3ZWM2OTkwMWMyMWVlN2E1YTMwOTYwZjlkOGQ1OTI0NjQ2ZmJiOGUyODczNTRlODBlMzViN2Q1OGU1ZWMwZjdjM2NjMmUxYjI0MzAyMGFjNjFkOTY0NDdlZGM4OThhNmE5NDVkMjYzOTNmN2Q4Nzg1ZjIxZTIxOTdiMDI0MTAwYTIzMGZmMTA5MDVlNTU3M2EzNWVhMzlkYTk2MTMwNzM1NzgzNDcxNjYyM2M4ZGQwNzEyNTMyYzgwM2Y2MjRmYmZiZDM3NjQyMmY4MmVhNTU5NGU2ODZhNThkOTdlNjU5YmYzYzJjMzZkOTg4YjU5NGM0ZmU4ZjAwZWQ3MTdlMDEwMjQxMDA5MWE0OWI5NDIzOTllMDdjMDNhNTIxNjVjYWIxZWY0YzY1NTE1ODZjNjc0Mzk3MzkwNDlmNDMxMzhhNzFhMDY5NTBjMzlhZDM3YmVlNWQ3YzM1MmZkMzU2ZWQ4ZDhhNjY0OWRhMmY3Njc4YTA2ODlmMWIyMTZkMWFiNDM3MzE4ZDAyNDA3ZTBiMzhkYjQwODZkNzNlN2Y1MTUyM2ViYWEwNWNiN2Q5YjUxZjU3MTE4NDQ4NjhkZDY1MzY4YzVmNjc3NGMyYTBmNzA4YTYyNjVlODFiNWU5ODFlODU1NTkwNmU4ZGMzZTdjNThlNzgxMTlmNjhjY2I2ZTM5NDAwYzEwOWY5Yw==";
    /**正式环境秘钥*/
    public static String privatekey ="MzA4MjAyNzcwMjAxMDAzMDBkMDYwOTJhODY0ODg2ZjcwZDAxMDEwMTA1MDAwNDgyMDI2MTMwODIwMjVkMDIwMTAwMDI4MTgxMDA5NDUxY2Q5MjNlZmViNzM4YzhiOGU1NDU1NzdlNWZlNjViYTBmNjkwMWJjYjNjNTIyZTIzODdmY2U1YWI4NDBjMjNhZmRkYThkZjljMTFjN2UxYjdiY2Q5NWJmZTJkZDNkMTA0NTRlMjExZjYzNTc3NGM5MmQ0MjZhY2JkNTY2YjRhMzIzNDljMDM0ODQ1NDE1ZDNmZDI1NzEyMzNkZDFmNGQzMGI3Y2JlODc2ZjI5M2JlM2ViZDliNTNiOGIyZjdhZjFlNDhiNDdjYjBlODQ0ZjBjNjgzZDdmZmU0MGRjYzc4ZTg0ZDI5Yzk1ZTEwZGFjNDcxNzQ4MjhmYzg1NThmMDIwMzAxMDAwMTAyODE4MDBkZTM4ZjI1ODdjMmEwNzNhZmQwMjhhMDEyYWU2MWRhOTc2ZmZlNDZjNjYzODRiNTg5NWU4ODY4NzUzNTU3N2IyZjg4NTM3MDcxZTgxMjdlMmUzM2RmN2E1NzMzZjkyNGExYTkwZjJkZDA1MmM2ZjRmMWY1YjllYTE5MzhlZGIzOWY0MDQ4YmFlZjJjNTQxZGI4ZWNkZmYzNzk5ZmRhMjZmZTZkYzRkM2RhZWY2OGU2M2QyY2ViMmZhMjM4MTliZWU4MGViYTQyZWM5NGNhODFmNTJjMzFlMmEyZWY1ZGY3MGY2ZmRmNTIyY2NhYWFiYmU1MzhlYjAyOTZiMjVkZTkwMjQxMDBlOGE4ODcyYzViMjg3ZDllZWI1YWVjOGIxZGVhZDAyNDNmNDk0NmFlYTE2OGJhNTk1ODVlNmYxNDg1YmE5ZTBmNTk1ZWI1MmYyZWZkN2QwN2RkMjRhNWQzYWQ5NTcyMmI0YzI1NGU5YWQ0ODNiZTRjMjFkYTEwNzRlNjQ2MDViNTAyNDEwMGEzMzMyOWJjM2Y3ZTc1N2Y0NzMyMTJmNTY0ZTY2N2VjOGUyNGI1OTU0MWU5ZTRiYjFmZjRlNjAyNmRiYTJjMjQ1ZWIwYzEyMGZjYzk4NjEyM2ZlMjdhZTkzNmZiZWYzZmFjMDhkNTgyN2FhZGI1NDU2ZGI0OWY2ZWU4ZmZmOGIzMDI0MTAwYjZlMzBiNjZmZGJkODU4YTMwMWU0ODQ1NGY1ZTJmMDFkZjUxYmRhMzM2YjQ3OTkwMTQxYzFmOWI3NTQ3ZWU0OWM4ODlkZjQ5NThkOWUwYjgxNmM5NjEyNTU1OTU1ZGFhZDU2ZjlhMmJiMTk0NjIzOWJjN2U4YjYzMjk0NGFmZTUwMjQwNDU1ZGU5Y2Y2OWVkODY2M2EzMGY1NTlmOTg1Mzk2YjhhNTcyNTRmYzJjMTgwZTcwYmIxNDhjZWY1YjU0NTY4ZWRlZjBhMjI2MzM0YzM1MDA2Y2M3MDcxNzM5OTdiYzA5ODQ0NWZjZmE4OGNhNmNkMjI1ZGYzMDI1MjQ0NDI4YTEwMjQxMDBiYzlmZmI1ODcxMTU3YWI1MWFiOWEwMTk4YzQ1NjYxM2EwOTUyZDQwYWNhMzA0NDM3NWE1OTA1ZTg1ZWU2OWIwNDM2ZWZhZTM1ZTFjMjg0OGMwOWFmZTc2MWY2MWRlNWE0ZDg0NzQwNjRmMzc5NDJjNzg5MDkxMmM4OWQyYThkYg==";
    public static String subappSecret = "YtkGr@Nm3a$2kyW$";
    public static String subchannelId = "SAP";
    public static String channelId = "SAP";
    public static String charset = "utf-8";

    @Override
    public String execute(RequestInfo requestInfo) {
        String requestName = requestInfo.getRequestManager().getRequestname();
        String requestId = requestInfo.getRequestid();
        String workflowId =requestInfo.getWorkflowid();
        log.info("触发流程请求:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:"+requestId+"执行开始!");
        String tableName = requestInfo.getRequestManager().getBillTableName();
        BaseActionInfo baseActionInfo = new BaseActionInfo(requestInfo);
        RecordSet recordSet = new RecordSet();
        log.error("=======================上传文件至影像系统=====================");
        try {
            List<ImageBean>  imageList=new  ArrayList<ImageBean>();
            User user = HrmUserVarify.getUser(requestInfo.getRequestManager().getRequest(), null);
            //发票附件
            String fpfj=baseActionInfo.getMainTableValue("fpfj");
            if (!fpfj.equals("")){
                String[] docIdlist =fpfj.split(",");
                for (String doc:docIdlist){
                    Map<String, String> fileInfo = ESigTreasureUtil.getFileInfoByDocAttachment(doc, user);
                    ImageBean  imageBean=new  ImageBean();
                    //设置影像文件流
                    imageBean.setImageFile(base2InputStream(fileInfo.get("content")));
                    //设置影像名称
                    imageBean.setSrcName(fileInfo.get("fileName"));
                    //设置影像分类
                    imageBean.setImageType("0");
                    //添加到影像参数列表中
                    imageList.add(imageBean);
                }
            }
            //合同附件
            String htfj=baseActionInfo.getMainTableValue("htfj");
            if (!htfj.equals("")){
                String[] docIdlist =htfj.split(",");
                for (String doc:docIdlist){
                    Map<String, String> fileInfo = ESigTreasureUtil.getFileInfoByDocAttachment(doc, user);
                    ImageBean  imageBean=new  ImageBean();
                    //设置影像文件流
                    imageBean.setImageFile(base2InputStream(fileInfo.get("content")));
                    //设置影像名称
                    imageBean.setSrcName(fileInfo.get("fileName"));
                    //设置影像分类
                    imageBean.setImageType("0");
                    //添加到影像参数列表中
                    imageList.add(imageBean);
                }
            }
            //立项报告
            String lxbg=baseActionInfo.getMainTableValue("lxbg");
            if (!lxbg.equals("")){
                String[] docIdlist =lxbg.split(",");
                for (String doc:docIdlist){
                    Map<String, String> fileInfo = ESigTreasureUtil.getFileInfoByDocAttachment(doc, user);
                    ImageBean  imageBean=new  ImageBean();
                    //设置影像文件流
                    imageBean.setImageFile(base2InputStream(fileInfo.get("content")));
                    //设置影像名称
                    imageBean.setSrcName(fileInfo.get("fileName"));
                    //设置影像分类
                    imageBean.setImageType("0");
                    //添加到影像参数列表中
                    imageList.add(imageBean);
                }
            }
            //预验收报告
            String yysbg=baseActionInfo.getMainTableValue("yysbg");
            if (!yysbg.equals("")){
                String[] docIdlist =yysbg.split(",");
                for (String doc:docIdlist){
                    Map<String, String> fileInfo = ESigTreasureUtil.getFileInfoByDocAttachment(doc, user);
                    ImageBean  imageBean=new  ImageBean();
                    //设置影像文件流
                    imageBean.setImageFile(base2InputStream(fileInfo.get("content")));
                    //设置影像名称
                    imageBean.setSrcName(fileInfo.get("fileName"));
                    //设置影像分类
                    imageBean.setImageType("0");
                    //添加到影像参数列表中
                    imageList.add(imageBean);
                }
            }
            //终验收报告
            String zysbg=baseActionInfo.getMainTableValue("zysbg");
            if (!zysbg.equals("")){
                String[] docIdlist =zysbg.split(",");
                for (String doc:docIdlist){
                    Map<String, String> fileInfo = ESigTreasureUtil.getFileInfoByDocAttachment(doc, user);
                    ImageBean  imageBean=new  ImageBean();
                    //设置影像文件流
                    imageBean.setImageFile(base2InputStream(fileInfo.get("content")));
                    //设置影像名称
                    imageBean.setSrcName(fileInfo.get("fileName"));
                    //设置影像分类
                    imageBean.setImageType("0");
                    //添加到影像参数列表中
                    imageList.add(imageBean);
                }
            }
            //维保反馈单
            String wbfkd=baseActionInfo.getMainTableValue("wbfkd");
            if (!wbfkd.equals("")){
                String[] docIdlist =wbfkd.split(",");
                for (String doc:docIdlist){
                    Map<String, String> fileInfo = ESigTreasureUtil.getFileInfoByDocAttachment(doc, user);
                    ImageBean  imageBean=new  ImageBean();
                    //设置影像文件流
                    imageBean.setImageFile(base2InputStream(fileInfo.get("content")));
                    //设置影像名称
                    imageBean.setSrcName(fileInfo.get("fileName"));
                    //设置影像分类
                    imageBean.setImageType("0");
                    //添加到影像参数列表中
                    imageList.add(imageBean);
                }
            }
            //报关单
            String bgd=baseActionInfo.getMainTableValue("bgd");
            if (!bgd.equals("")){
                String[] docIdlist =bgd.split(",");
                for (String doc:docIdlist){
                    Map<String, String> fileInfo = ESigTreasureUtil.getFileInfoByDocAttachment(doc, user);
                    ImageBean  imageBean=new  ImageBean();
                    //设置影像文件流
                    imageBean.setImageFile(base2InputStream(fileInfo.get("content")));
                    //设置影像名称
                    imageBean.setSrcName(fileInfo.get("fileName"));
                    //设置影像分类
                    imageBean.setImageType("0");
                    //添加到影像参数列表中
                    imageList.add(imageBean);
                }
            }
            //补充资料
            String bczl=baseActionInfo.getMainTableValue("bczl");
            if (!bczl.equals("")){
                String[] docIdlist =bczl.split(",");
                for (String doc:docIdlist){
                    Map<String, String> fileInfo = ESigTreasureUtil.getFileInfoByDocAttachment(doc, user);
                    ImageBean  imageBean=new  ImageBean();
                    //设置影像文件流
                    imageBean.setImageFile(base2InputStream(fileInfo.get("content")));
                    //设置影像名称
                    imageBean.setSrcName(fileInfo.get("fileName"));
                    //设置影像分类
                    imageBean.setImageType("0");
                    //添加到影像参数列表中
                    imageList.add(imageBean);
                }
            }
            Map<String,String>  paramMap=new HashMap<String, String>();
            //设置系统号
            paramMap.put("sysCode", "WXCW_YIERSANAPI");
            //设置流水号
            paramMap.put("flwCode", "sfzm1234567");
            //设置功能号
            paramMap.put("funCode", "WXCW_YIERSAN");
            //设置机构号
            paramMap.put("orgCode", "222");
            //设置操作员编号
            paramMap.put("operCode", "3333");
            //设置账号
            paramMap.put("user", "wxcw001");
            //设置密码
            paramMap.put("psw", "wxcw001");
            //调用方法,如不抛出异常则上传成功
            //影像上传下载请求地址
            String IMG_URL = "http://172.27.202.175:7001/YZJImage/IImgXmlUpLoadSyn";
            String U_IMG_URL = "http://172.27.202.175:7001/YZJImage/ISerFileUp";
            UploadImg uploadImg = new UploadImg(IMG_URL,U_IMG_URL);
            uploadImg.uploadImages(imageList,paramMap);
        } catch (TencentCloudSDKException e) {
            log.info("触发流程:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:" + requestId + ";程序异常!");
            requestInfo.getRequestManager().setMessage("111100");
            requestInfo.getRequestManager().setMessagecontent(e.toString());
            return Action.FAILURE_AND_CONTINUE;
        } catch (Exception e) {
            log.info("触发流程:" + requestName + ";执行接口类名:" + this.getClass().getName() + ";发起请求RequestId:" + requestId + ";程序异常!");
            requestInfo.getRequestManager().setMessage("111100");
            requestInfo.getRequestManager().setMessagecontent("系统异常,请联系系统管理员!");
            return Action.FAILURE_AND_CONTINUE;
        }
        log.error("=======================上传文件至影像系统end=====================");
        return  Action.SUCCESS;
    }

    private static InputStream base2InputStream(String base64string) {
        ByteArrayInputStream stream = null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bt = decoder.decodeBuffer(base64string);
            stream = new ByteArrayInputStream(bt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream;
    }


    public static String getSign(Map map, String appSecret) {
        if (appSecret == null) {
            log.error("签名秘钥为空，请检查是否配置！");
            return null;
        } else {
            StringBuilder valueSb = new StringBuilder();
            map.put("appSecret", appSecret);
            Map<Object, Object> sortParams = new TreeMap(map);
            Iterator var5 = sortParams.entrySet().iterator();

            while(var5.hasNext()) {
                Map.Entry<Object, Object> entry = (Map.Entry)var5.next();
                valueSb.append(entry.getValue());
            }

            map.remove("appSecret");
            return md5(valueSb.toString());
        }
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

    public static String encrypt(String data, String key, String keyType, String charset) throws Exception {
        String keyStr = new String(esteem.jun.common.util.Base64.decode(key.getBytes(charset)));
        byte[] keyByte = hexStrToBytes(keyStr);
        byte[] encrypt = null;
        if ("publicKey".equals(keyType)) {
            encrypt = encryptByPublicKey(data.getBytes(charset), keyByte);
        } else {
            if (!"privateKey".equals(keyType)) {
                throw new RuntimeException("当前系统没有维护rsa非对称加密本服务是公钥还是私钥。 当前类型是：" + keyType);
            }

            encrypt = encryptByPrivateKey(data.getBytes(charset), keyByte);
        }

        return new String(esteem.jun.common.util.Base64.encode(encrypt));
    }

    public static final byte[] hexStrToBytes(String s) {
        byte[] bytes = new byte[s.length() / 2];

        for(int i = 0; i < bytes.length; ++i) {
            bytes[i] = (byte)Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }

        return bytes;
    }

    public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, privateKey);
        byte[] enBytes = null;

        for(int i = 0; i < data.length; i += 64) {
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + 64));
            enBytes = ArrayUtils.addAll(enBytes, doFinal);
        }

        return enBytes;
    }

    public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {
        X509EncodedKeySpec X509KeySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(X509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, publicKey);
        byte[] enBytes = null;

        for(int i = 0; i < data.length; i += 64) {
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + 64));
            enBytes = ArrayUtils.addAll(enBytes, doFinal);
        }

        return enBytes;
    }
}


