package esteem.jun.sign;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ibm.icu.text.SimpleDateFormat;
import esteem.jun.wanxiang.action.BaseActionInfo;
import weaver.conn.RecordSet;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.file.Prop;
import weaver.general.BaseBean;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.interfaces.workflow.action.Action;
import weaver.security.rsa.Base64;
import weaver.soa.workflow.request.RequestInfo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author:
 * @Date:
 * @Description:
 */
public class DocV2 extends BaseBean implements Action  {
    private static Log log = LogFactory.getLog(DocV2.class);
    @Override
    public String execute(RequestInfo requestInfo) {

        RecordSet recordSet = new RecordSet();
        RecordSet recordSet2 = new RecordSet();
        String requestid=requestInfo.getRequestid();
        String tableName = requestInfo.getRequestManager().getBillTableName();
        BaseActionInfo baseActionInfo = new BaseActionInfo(requestInfo);
        String flowids=baseActionInfo.getMainTableValue("flowid");
        List docIds=new ArrayList();
        Map jsonObj_result = new JSONObject();
        try {
            log.info("-------------------------查询电子签附件begin-------------------------------");
            String[] flowid=flowids.substring(1,flowids.length()-1).split(",");
            log.info(flowid[0].toString()+"+"+flowid[1].toString());
            for (int i = 0; i <flowid.length; i++){
                JSONObject Form=new JSONObject();
                Map map=new JSONObject();
                map.put("UserId", Prop.getPropValue("qian","UserID"));
                Form.put("Operator",map);
                JSONArray BusinessIds=new JSONArray();
                BusinessIds.add(flowid[i]);
                //BusinessIds.add("yDCN1UUckpvg0uvdUyB0IhJSbefV0BRe");
                Form.put("BusinessIds",BusinessIds);
                Form.put("BusinessType","FLOW");
                Form.put("UrlTtl","36000");

                String Result= QianAPIUtil.DownFlowFile(Form.toString(),"DescribeFileUrls");
                log.info("Form:"+Form.toString());
                JSONObject result = JSON.parseObject(Result);
                log.info("Result:"+Result);

                JSONObject Response=result.getJSONObject("Response");
                JSONArray FileUrls=Response.getJSONArray("FileUrls");
                String FileUrl=FileUrls.getString(0);
                JSONObject FileUrlsobj = JSON.parseObject(FileUrl);
                String Url=FileUrlsobj.getString("Url");
                log.info(Url);

                //new一个URL对象
                URL url = new URL(Url);
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

/*        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");

        DocInfo docInfo = new DocInfo();
        docInfo.setDocSubject("" + map.get("name"));//文档主题
        docInfo.setDoccontent("");//文档内容
        docInfo.setSeccategory(35);//存储的磁盘id
        docInfo.setDoccreaterid(1);//创建者id
        docInfo.setDoccreatedate(dateFormat.format(currentDate));//文档创建日期
        docInfo.setDoccreatetime(timeFormat.format(currentDate));//文档创建时间
        docInfo.setDoclastmoddate(dateFormat.format(currentDate));//文档修改日期
        docInfo.setDoclastmodtime(timeFormat.format(currentDate));//文档修改时间
        docInfo.setDoccreatertype(0);//文档创建类型
        docInfo.setDoclangurage(7);//盲猜文档语言，无关紧要
        docInfo.setOwnerid(1);//文档拥有者*/

                //读取存储磁盘id，默认81
                //getIntFromRequestHeader方法意为读取请求头“dirId”键的值，如不存在则默认为81
                int dirId = 35;
                //赋予时间参数
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
                //获取原文件信息
                String[] docIdlist =baseActionInfo.getMainTableValue("gzfj").split(",");
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
                int docId = new DocServiceImpl().createDocByUser(docInfo, new User(1));
                docIds.add(docId);
                //当id为0时说明创建失败
                log.info("docid:"+docId);
            }

            //当id为0时说明创建失败
            String gzfj=docIds.toString().replaceAll("\\[|]","");
            String sql="update "+tableName+" set gzfj='"+gzfj.replaceAll(" ","")+"' where requestid="+requestid;
            recordSet.execute(sql);
            log.info("-------------------------查询电子签附件end-------------------------------");
            if (docIds.size()<1){
                //自定义类，返回创建失败信息
                jsonObj_result.put("flag","附件创建失败");
                return Action.FAILURE_AND_CONTINUE;
            }else{
                //返回创建文档的id
                return Action.SUCCESS;
            }
        } catch (Exception e) {
            jsonObj_result.put("flag", "代码错误");
            //返回异常信息供前端调用
            return Action.FAILURE_AND_CONTINUE;
        }
    }


}
