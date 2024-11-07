package JHX;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.weaver.esb.client.EsbClient;
import com.weaver.esb.spi.EsbService;
import esteem.jun.common.util.ArrayUtils;
import esteem.jun.common.util.Base64Utils;
import esteem.jun.wanxiang.action.BaseActionInfo;
import net.sf.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Base64.Decoder;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 曹嘉伟
 * @Date: 2024/8/1
 * @Description:
 */
public class MK extends BaseBean implements Action {
    public static Base64.Encoder encoder;
    private static Log log = LogFactory.getLog(MK.class);
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


        //响应对象
        JSONObject jsonObj_result = new JSONObject();

        log.error("每刻-九恒星----->begin");
        String datastr = "";
        Map UseList = new JSONObject();
        /**ERP端付款唯一标识*/
        String erpInsId = Util.null2String(baseActionInfo.getMainTableValue("dh"));
        /**付款单位*/
        String payCustNo = "0253";
        /**付款方账号*/
        String payAccountNo = "20701010253";
        /**付款方户名*/
        String payAccountName = "万向一二三股份公司";
        /**付款银行财务公司为：00*/
        String payBank = "00";
        /**期望付款日，格式：YYYY-MM-DD*/
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String payDate = Util.null2String(df.format(now));
        /**付款方开户行名称*/
        String payBankName = "财务公司";
        /**付款金额*/
        String amount = Util.null2String(baseActionInfo.getMainTableValue("amount"));
        /**是否自有资金付款*/
        String fundSource = "2";
        /**CNY-人民币 USD-美元 EUR-欧元 HKD-港元 GBP-英镑 参考资金系统主数据币种信息*/
        String currencyNo = "CNY";
/*        switch (currencyNo){
            case "1":
                currencyNo="CNY";
                break;
            case "2":
                currencyNo="USD";
                break;
            case "3":
                currencyNo="JPY";
                break;
            case "4":
                currencyNo="EUR";
                break;
        }*/
        /**款项类别默认为空*/
        String propNo = "";
        /**现金流编号默认为空*/
        String flowNo = "";
        /**预算编号,无为空*/
        String bdgNo = "";
        /**发票号*/
        //String billNo = Util.null2String(rsquery.getString("fph"));
        /**收款方账号*/
        String receAccountNo = Util.null2String(baseActionInfo.getMainTableValue("bankAcctNumber"));
        /**收款方户名*/
        String receAccountName = Util.null2String(baseActionInfo.getMainTableValue("bankAcctName"));
        /**收款方银行*/
        String receBankNo=Util.null2String(baseActionInfo.getMainTableValue("dhbh"));
        /**支行号*/
        String receCnaps=Util.null2String(baseActionInfo.getMainTableValue("bankBranchNo"));
        /**收款方开户行名称*/
        String receOpbankName = Util.null2String(baseActionInfo.getMainTableValue("bankName"));
        /**对公\对私*/
        String persionFlag = "0";
        /**加急标识加急-1 不加急-0*/
        String urgentFlag = "0";
        /**付款摘要信息*/
        String purpose = Util.null2String("报销");
        /**附言*/
        String remark = Util.null2String("报销");
        /**录入人*/
        String creator = Util.null2String("sqr");
        /**是否集团计划内 0：否 1：是*/
        String numberValue1 ="1";


        UseList.put("erpInsId", erpInsId);
        UseList.put("payCustNo", payCustNo);
        UseList.put("payAccountNo",payAccountNo);
        UseList.put("payAccountName", payAccountName);
        UseList.put("payBank", payBank);
        UseList.put("payDate", payDate);
        /*            UseList.put("payBankName", payBankName);*/
        UseList.put("amount", amount);
        UseList.put("fundSource", fundSource);
        UseList.put("currencyNo", currencyNo);
        UseList.put("propNo", propNo);
        UseList.put("flowNo", flowNo);
        UseList.put("bdgNo", bdgNo);
        //UseList.put("billNo", billNo);
        UseList.put("receAccountNo", receAccountNo);
        UseList.put("receAccountName", receAccountName);
        UseList.put("receOpbankName", receOpbankName);
        UseList.put("receBankNo",receBankNo);
        UseList.put("receCnaps",receCnaps);
        UseList.put("persionFlag", persionFlag);
        UseList.put("urgentFlag", urgentFlag);
        UseList.put("purpose", purpose);
        UseList.put("remark",remark);
        UseList.put("creator",creator);
        UseList.put("numberValue1", numberValue1);

        try {

            Date thisDate = new Date();
            SimpleDateFormat f= new SimpleDateFormat("yyyyMMddhhmmss");
            JSONObject list=new JSONObject();
            list.put("code","MPBS-T001");
            list.put("batchNo",UUID.randomUUID().toString().replace("-", ""));
            list.put("nodeId","client.001");
            list.put("channelId","ERP");
            list.put("clientId","0253");
            list.put("clientName","万向一二三股份公司");
            list.put("txDateTime",f.format(thisDate));
            list.put("data",UseList.toString());

            JSONObject jsonObject = JSONObject.fromObject(list);
            log.info("原始数据:"+jsonObject.toString());
            jsonObject.put("subchannelId", subchannelId);
            jsonObject.put("sign", getSign(jsonObject, subappSecret));
            log.info("jsonobject:" + jsonObject);
            datastr = encrypt(jsonObject.toString(), privatekey, "privateKey", charset);
        } catch (Exception var13) {
            log.error("加密报错：" + var13);
            var13.printStackTrace();
            return Action.FAILURE_AND_CONTINUE;
        }

        JSONObject data1 = new JSONObject();

        data1.put("channelId", channelId);
        data1.put("datastr", datastr);
        data1.put("timestamp", System.currentTimeMillis());
        data1.put("nonce", UUID.randomUUID().toString().replace("-", ""));
        log.info("data1:"+data1);
        JSONObject senddata = new JSONObject();
        senddata.put("data", data1);
        senddata.put("sign", getSign(data1, appSecret));
        String output = Base64Utils.encode(senddata.toString().getBytes());
        log.info("date:"+senddata.toString());
        log.info("output:"+output);

        com.alibaba.fastjson.JSONObject YKSP = new com.alibaba.fastjson.JSONObject();
        YKSP.put("message",output);
        EsbService service = EsbClient.getService();
        String result = service.execute("CW_YKSP",YKSP.toString());
        log.info("result:"+result);
        com.alibaba.fastjson.JSONObject resultobj = com.alibaba.fastjson.JSONObject.parseObject(result);
        jsonObj_result.put("msg",Util.null2String(resultobj.get("msg")));
        jsonObj_result.put("data",Util.null2String(resultobj.get("data")));
        jsonObj_result.put("code",Util.null2String(resultobj.get("code")));
        log.info(jsonObj_result.toString());
        return Action.SUCCESS;
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
