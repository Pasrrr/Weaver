package esteem.jun.sign;

import com.yzj.img.service.UploadImg;
import com.yzj.img.utils.ImageBean;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 曹嘉伟
 * @Date: 2024/7/25
 * @Description:
 */
class DownloadFileV2Test {

    @Test
    void execute() {
        try{
        File file =new  File("D:\\a");
        File[]  imageFiles=file.listFiles();
        List<ImageBean> imageList=new ArrayList<ImageBean>();
        for(File  imageFile:imageFiles){
            ImageBean  imageBean=new  ImageBean();
            //设置影像文件流
            imageBean.setImageFile(new FileInputStream(imageFile));
            //设置影像名称
            imageBean.setSrcName(imageFile.getName());
            //设置影像分类
            imageBean.setImageType("0");
            //添加到影像参数列表中
            imageList.add(imageBean);
        }

        Map<String,String> paramMap=new HashMap<String, String>();
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
            UploadImg uploadImg = new  UploadImg(IMG_URL,U_IMG_URL);
            uploadImg.uploadImages(imageList,paramMap);
        }catch(Exception e){
            System.out.println(e);
        }

    }
}