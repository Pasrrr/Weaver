package esteem.jun.wanxiang.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import esteem.jun.wanxiang.pojo.FiledData;
import esteem.jun.wanxiang.pojo.RespBeanPage;
import esteem.jun.wanxiang.pojo.WxSimpleUser;
import esteem.jun.wanxiang.services.WxService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;

import java.util.*;

import static weaver.general.Util.null2String;

public class WxServiceImpl implements WxService {

    //日志
    private Log log = LogFactory.getLog(WxServiceImpl.class.getName());

    @Override
    public String getAllUserInfo(Integer page,Integer size) {

        if (page!=null&&size!=null){
            page=(page-1) *size;
        }
        //sql执行条件
//        String sql = "select a.*,b.field4 from hrmresource  a left join cus_fielddata b on a.id=b.id and b.scope='HrmCustomFieldByInfoType' and  b.scopeid=-1";
        String sql = "select a.*,b.field1,b.field2 from hrmresource  a left join cus_fielddata b on a.id=b.id and b.scope='HrmCustomFieldByInfoType' and  b.scopeid=-1 limit "+page+","+size;

        //分页使用
        String sql1 ="select COUNT(*) as total from hrmresource  a left join cus_fielddata b on a.id=b.id and b.scope='HrmCustomFieldByInfoType' and  b.scopeid=-1";

        RecordSet recordSet = new RecordSet();
        //分页使用
        RecordSet rsfy = new RecordSet();


        //存储所有的人员信息
        List<WxSimpleUser> list=new ArrayList<>();

        //执行sql语句
        recordSet.executeQuery(sql);
        rsfy.executeQuery(sql1);
        RespBeanPage respBeanPage = new RespBeanPage();
        while (rsfy.next()){
            respBeanPage.setSize(rsfy.getInt(1));
        }
        while (recordSet.next()){

            WxSimpleUser wxSimpleUser = new WxSimpleUser();
            wxSimpleUser.setId(recordSet.getInt("id"));
            wxSimpleUser.setLoginid(recordSet.getString("loginid"));
            wxSimpleUser.setPassword(recordSet.getString("password"));
            wxSimpleUser.setLastname(recordSet.getString("lastname"));
            wxSimpleUser.setSex(recordSet.getString("sex"));
            wxSimpleUser.setBirthday(recordSet.getString("birthday"));
            wxSimpleUser.setNationality(recordSet.getInt("nationality"));
            wxSimpleUser.setSystemlanguage(recordSet.getInt("systemlanguage"));
            wxSimpleUser.setMaritalstatus(recordSet.getString("maritalstatus"));
            wxSimpleUser.setTelephone(recordSet.getString("telephone"));
            wxSimpleUser.setMobile(recordSet.getString("mobile"));
            wxSimpleUser.setMobilecall(recordSet.getString("mobilecall"));
            wxSimpleUser.setEmail(recordSet.getString("email"));
            wxSimpleUser.setLocationid(recordSet.getInt("locationid"));
            wxSimpleUser.setWorkroom(recordSet.getString("workroom"));
            wxSimpleUser.setHomeaddress(recordSet.getString("homeaddress"));
            wxSimpleUser.setResourcetype(recordSet.getString("resourcetype"));
            wxSimpleUser.setStartdate(recordSet.getString("startdate"));
            wxSimpleUser.setField1(recordSet.getString("field1"));
            wxSimpleUser.setField2(recordSet.getString("field2"));

            list.add(wxSimpleUser);
        }
        //返回消息实体类
        respBeanPage.setData(list);

        return JSON.toJSONString(respBeanPage);

    }

    @Override
    public String getUserById(Integer userId) {

        String sql = "SELECT id,field1,field2 from cus_fielddata where scope='HrmCustomFieldByInfoType' and scopeid=-1 and id ="+userId;
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql);
        FiledData filedData = new FiledData();
        while (rs.next()){
            filedData.setId(rs.getInt("id"));
            if (!("".equals(rs.getInt("field1")))){
                filedData.setField1(rs.getInt("field1"));
            }
            if (!("".equals(rs.getInt("field2")))){
                filedData.setField2(rs.getInt("field2"));
            }

        }
        return JSON.toJSONString(filedData);
    }

    @Override
    public String getMeetingroom() {
        String sql = "SELECT * FROM meetingroom";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql);

        List<Map<String, String>> list = new ArrayList<>();
        while (rs.next()) {
            Map<String,String> map = new HashMap<>();
            String status=null2String(rs.getString("STATUS"));
            try {
                if ("2".equals(status)){
                    continue;
                }else {
                    map.put("STATUS",null2String(rs.getString("STATUS")));
                }
            }catch (Exception e){
                log.error("筛选异常");
            }
            map.put("id",null2String(rs.getInt("id")));
            map.put("NAME",null2String(rs.getString("NAME")));
            map.put("ROOMDESC",null2String(rs.getString("ROOMDESC")));
            list.add(map);
        }

/*        return JSONArray.toJSON(list);*/
        return JSON.toJSONString(list,true);
    }
}
