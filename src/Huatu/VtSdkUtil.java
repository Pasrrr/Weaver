package Huatu;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.general.Util;
import weaver.hrm.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 曹嘉伟
 * @Date: 2024/9/24
 * @Description:
 */
public class VtSdkUtil {
    private static Log log = LogFactory.getLog(VtSdkUtil.class.getName());
    public interface libVtExtAPI extends Library {
        libVtExtAPI INSTANTCE = (libVtExtAPI) Native.loadLibrary("/home/weaver/ecology/classbean/Huatu/libVtExtAPI.so", libVtExtAPI.class);
        int VtExtInitWithServer(String ip, int port, String appid, String appkey, int logFlag);
        int VtExtIsCrypt(String path, IntByReference isEnc);
        int VtExtEncryptFile(String srcPath, String destPath, int cryptMode, int domain);
        int VtExtDecryptFile(String src, String dest);
        int VtExtIsCryptHeader(byte[] bytes, IntByReference nHeadLength, LongByReference ulHeader);
        int VtExtGetEncryptHeader(int crypt, int domain, byte[] headerData, int length, LongByReference header);
        int VtExtEncryptBuffer(long header, long offset, byte[] buffer, int length);
        int VtExtDecryptBuffer(long header, long offset, byte[] buffer, int length);
        int VtExtDeleteHeader(long header);
        int VtExtOutsidePack(byte[] config, int len);
        int VtExtGetCryptFileDomain(String path, IntByReference domain);

    }


    /*通过连接服务器校验注册*/
    public static int VtExtInitWithServer(String ip, int port, String appid, String appkey, int logFlag){
        log.info("开始激活");
        int sdkReslut = -1;
        try {
            sdkReslut = libVtExtAPI.INSTANTCE.VtExtInitWithServer(ip, port, appid, appkey, logFlag);
            if (sdkReslut == 0) {
                log.info("激活成功");
            } else {
                log.info("VtExtInitWithServer Error: "+ sdkReslut);
            }
        }catch (Exception e){
            log.info(sdkReslut);
        }
        return sdkReslut;
    }

    /**
     * 调用加密头判断接口,判断文件流是否是加密头
     *
     * @param bytes
     * @param ulHeader
     * @return
     */
    public static int VtExtIsCryptHeader(byte[] bytes, IntByReference nHeadLength, LongByReference ulHeader) {
        int result = -1;
        try {
            log.info("开始调用isCryptHeader判断文件加密头");
            result = libVtExtAPI.INSTANTCE.VtExtIsCryptHeader(bytes, nHeadLength, ulHeader);
            if (result == 0) {
                log.info("结束调用VtExtIsCryptHeader判断文件加密头");
                if (ulHeader.getValue() == 0)
                {
                    log.info("不是加密文件");
                }
                else
                {
                    log.info("是加密文件");
                }
            }
            else {
                log.error("VtExtIsCryptHeader Error: "+ result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 解密数据流接口
     *
     * @param ulHeader 加密头
     * @param offset   偏移量 从0开始
     * @param buffer   所有要加密的数据
     * @param length   每一次待加密的数据
     * @return
     */
    public static int VtExtDecryptBuffer(long ulHeader, long offset, byte[] buffer, int length) {
        int result = -1;
        try {
            //待解密数据距离起始内容(加密头长度后的位置)的偏移
            result = libVtExtAPI.INSTANTCE.VtExtDecryptBuffer(ulHeader, offset, buffer, length);
            if (result == 0) {
                log.info("解密流成功");
            }
            else {
                log.error("VtExtDecryptBuffer Error: "+ result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



    /**
     * 释放加密获取的header;
     *
     * @param ulHeader
     */
    public static int VtExtDeleteHeader(long ulHeader) {
        int reslut = -1;
        try {
            reslut = libVtExtAPI.INSTANTCE.VtExtDeleteHeader(ulHeader);
            if (reslut == 0) {
                log.info("释放加密获取的header成功");
            }
            else{
                log.error("VtExtDeleteHeader Error: "+ reslut);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return reslut;
    }
    public static Map<String,String> getFileInfoByDocAttachment(String docId, User user) throws Exception{
        DocServiceImpl docService= new DocServiceImpl();
        Map<String,String> map=new HashMap<>();
        DocInfo doc =docService.getDocByUser(Util.getIntValue(docId), user, "");
        DocAttachment[] docAttachments=doc.getAttachments();
        for(int i=0;i<docAttachments.length;i++ ){
            DocAttachment docAttachment=docAttachments[i];
            String content =docAttachment.getFilecontent();
            String fileName=docAttachment.getFilename();
            String filepath=docAttachment.getFilerealpath();
            map.put("content",content);
            map.put("fileName",fileName);
            map.put("filepath",filepath);
        }
        return map;
    }

    /**
     * 调用加密头判断接口,判断文件流是否是加密头
     *
     * @param bytes
     * @param ulHeader
     * @return
     */
    public static int VtExtDeleteHeader(String path, IntByReference domain) {
        int result = -1;
        try {
            log.info("开始调用判断文件安全域");
            result = libVtExtAPI.INSTANTCE.VtExtGetCryptFileDomain(path, domain);
            if (result == 0) {
            }
            else {
                log.error("VtExtIsCryptHeader Error: "+ result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
