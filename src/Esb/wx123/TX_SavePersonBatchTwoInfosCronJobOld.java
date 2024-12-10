package Esb.wx123;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ibm.icu.text.SimpleDateFormat;
import com.weaver.esb.client.EsbClient;
import com.weaver.esb.spi.EsbService;
import weaver.conn.RecordSet;
import weaver.general.Util;

import java.util.*;

/**
 * @author test
 * @title: DH_SavePersonInfos
 * @description: 企微_人员批量导入定时任务_基于单人员导入接口循环调用
 * @date 2023-03-29 14:12
 */
public class TX_SavePersonBatchTwoInfosCronJobOld {
    private static Log log = LogFactory.getLog(TX_SavePersonBatchTwoInfosCronJobOld.class);

    public Map execute(Map param) {

        log.error("企微_人员批量导入定时任务--->begin");
        RecordSet rsquery = new RecordSet();
        RecordSet rsquery1 = new RecordSet();
        String isall = Util.null2String(param.get("isall"));//逻辑标识 Y - 插入全部,默认当天和前一天
        log.error("isall:" + isall);
        //响应JSON对象
        JSONObject jsonObj_result = new JSONObject();
        //接口调用开始
        EsbService service = EsbClient.getService();
        String result = service.execute("TX_GETTokenInfo", "");
        JSONObject resultobj = JSONObject.parseObject(result);
        String code = Util.null2String(resultobj.get("code"));

        log.error("resultobj:" + resultobj);
        String access_token = "";
        if (!code.equals("100")) {
            jsonObj_result.put("flag", "E");
            jsonObj_result.put("msg", "获取access_token失败");
            return jsonObj_result;
        } else {
            JSONObject data = JSON.parseObject(Util.null2String(resultobj.get("data")));
            access_token = data.getString("access_token");
            log.error("access_token:" + access_token);
        }

        int addnum = 0;//新增成功人员总数
        StringBuilder sb_success = new StringBuilder();

        int errnum = 0;//新增失败人员总数
        JSONArray sb_errorArr = new JSONArray();

        int updatenum=0;//更新成功人数
        JSONArray UP_Arr=new JSONArray();

        int updateerrnum =0;//更新失败人员总数
        JSONArray up_errorArr= new JSONArray();

        int totalnum = 0;//新增人员总数
        int upnum=0;//更新人员总数
        int detnum = 0;//删除成功人员总数
        int errdetnum=0;//删除失败人员总数
        JSONArray del_err=new JSONArray();
        JSONArray del_success=new JSONArray();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //当前日期
        String datestr = sdf.format(new Date());

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        //昨天日期
        String datestrold = sdf.format(calendar.getTime());

        //查询所有人员信息
        String queryUserSql = "SELECT  a.* FROM hrmresource a\n" +
                "                WHERE  a.STATUS in (0,1,2,3,5) \n" +
                //"                AND a.workcode != '' \n" +
                //"                AND a.workcode = '012863' \n" +
                "                AND a.accounttype != 1 \n" +
                "                AND a.SUBCOMPANYID1=6 ";
        //根据万向空间部门ID查询企微ID



        //增加过滤 - 人员取当天和前一天新增的进行插入
        if (isall.equalsIgnoreCase("N")) {
            queryUserSql = queryUserSql + " AND (a.createdate = '" + datestr + "' OR a.createdate = '" + datestrold + "' " +
                    "OR a.lastmoddate='" + datestr + "'OR a.lastmoddate='" + datestrold + "')";//新增人员更新过滤
            //queryUserSql = queryUserSql + " AND a.id=5454";

        }
        log.error("queryUserSql:" + queryUserSql);

        //人员离职id数组
        JSONArray leavelist = new JSONArray();

        rsquery.execute(queryUserSql);
        while (rsquery.next()) {
            String status = Util.null2String(rsquery.getString("STATUS"));//人员状态
            String id = Util.null2String(rsquery.getString("id")).trim();//id
            if (status.equals("5")) {
                //人员离职
                leavelist.add(id);

            } else {
                String departmentid="";
                //String id = Util.null2String(rsquery.getString("id"));
                String queryUserSql1="SELECT * FROM uf_QW_DepartmentID a\n";
                queryUserSql1=queryUserSql1+"WHERE a.wxkjid="+Util.null2String(rsquery.getString("departmentid"));
                log.error("queryUserSql1:" + queryUserSql1);
                rsquery1.execute(queryUserSql1);
                while (rsquery1.next()){
                    departmentid = Util.null2String(rsquery1.getString("qywxid")).trim();//企微部门ID
                }
                log.error(rsquery1.toString());
                JSONObject addpersonobj = new JSONObject();
                String lastname = Util.null2String(rsquery.getString("lastname")).trim();//人员姓名
                String workcode = Util.null2String(rsquery.getString("workcode")).trim();//人员编码
                String mobile =Util.null2String(rsquery.getString("mobile"));
                log.error("departmentid:"+departmentid);
                String gender=Util.null2String(rsquery.getString("sex"));//性别
                String email = Util.null2String(rsquery.getString("email")).trim();//电子邮箱
                String birthday = Util.null2String(rsquery.getString("birthday"));//生日
                String direct_leader = Util.null2String(rsquery.getString("managerid"));//直接上级

                addpersonobj.put("userid", id);
                addpersonobj.put("name", lastname);
                addpersonobj.put("mobile", mobile);
                addpersonobj.put("department", departmentid);
                addpersonobj.put("gender", gender);
                //addpersonobj.put("email", email);
                addpersonobj.put("direct_leader", direct_leader);
                addpersonobj.put("ACCESS_TOKEN", access_token);

                log.error("addpersonobj:" + addpersonobj.toString());
                totalnum++;
                //自定义字段对象
//            fieldExtobj.put("mealallowance",mealallowance);
//            fieldExtobj.put("icnum",icnum);
                if (!access_token.equals("")) {
                    //调用人员批量新增接口
                    EsbService service2 = EsbClient.getService();
                    EsbService service3 = EsbClient.getService();

                    String result2 = service2.execute("QW_SavePersonSingleInfosCronJob", addpersonobj.toJSONString());
                    JSONObject resultobj2 = JSONObject.parseObject(result2);
                    log.error("resultobj2："+resultobj2.toString());
                    //log.error("resultobj2:"+resultobj2.toString());

                    String errcode2 = Util.null2String(resultobj2.get("code"));
                    String errmsg = Util.null2String(resultobj2.get("msg"));
                    String[] msg=errmsg.split(",");
                    log.error("errmsg:" + errmsg+ msg[0].toString());
/*                    if (msg[0].equals("access_token missing")){
                        JSONObject regettoken =Regettoken();
                        if (regettoken.get("flag").equals("S")){
                            access_token=regettoken.getString("access_token");
                        }else {
                            return regettoken;
                        }
                    }*/
                    if (!errcode2.equals("100")) {
                        if (msg[0].toString().equalsIgnoreCase("userid existed")||msg[0].toString().equalsIgnoreCase("mobile existed")) {
                            sb_success.append(lastname).append("_").append(workcode).append(";");
//                          //人员已存在,进入更新程序
                            //log.error("人员已存在,进入更新程序-->");
                            JSONObject updatepersonobj = new JSONObject();
                            updatepersonobj.put("userid", id);
                            updatepersonobj.put("name", lastname);
                            updatepersonobj.put("mobile", mobile);
                            updatepersonobj.put("department", departmentid);
                            updatepersonobj.put("gender", gender);
                            //updatepersonobj.put("email", email);
                            updatepersonobj.put("direct_leader", direct_leader);
                            updatepersonobj.put("ACCESS_TOKEN", access_token);
                            log.error("人员已存在,进入更新程序-->" + updatepersonobj.toJSONString());

                            String result3 = service3.execute("QW_UpdatePersonSingleInfosCronJob", updatepersonobj.toJSONString());
                            JSONObject resultobj3 = JSONObject.parseObject(result3);
                            log.info("resultobj3:"+resultobj3.toString());
                            String errcode3 = Util.null2String(resultobj3.get("code"));
                            String errmsg3 = Util.null2String(resultobj3.get("msg"));
                            if (!errcode3.equals("100")) {
                                log.error("人员" + lastname + "更新失败;" + errmsg3);
                                updateerrnum++;
                                JSONObject up_errorObject = new JSONObject();
                                up_errorObject.put("lastname", lastname);
                                //up_errorObject.put("reason", errmsg3);
                                up_errorArr.add(up_errorObject);
                            } else {
                                upnum++;
                                JSONObject up_Object = new JSONObject();
                                up_Object.put("lastname", lastname);
                                //up_Object.put("reason", errmsg3);
                                UP_Arr.add(up_Object);
                                log.error("人员" + lastname + "更新成功;");
                            }
                        } else {
                            JSONObject sb_errorObject = new JSONObject();
                            sb_errorObject.put("lastname", lastname);
                            //sb_errorObject.put("reason", errmsg);
                            sb_errorArr.add(sb_errorObject);
                            errnum++;
                        }
                        //log.error("error:" + resultobj2);
                    } else {
                        log.error("success:" + resultobj2);
                        addnum++;
                        sb_success.append(lastname).append("_").append(workcode).append(";");
                    }
                } else {
                    errnum++;
                    JSONObject sb_errorObject = new JSONObject();
                    sb_errorObject.put("lastname", lastname);
                    sb_errorObject.put("reason", "access_token获取失败");
                    sb_errorArr.add(sb_errorObject);
                }
            }
        }
        //人员离职接口调用
        EsbService service1 = EsbClient.getService();
        log.error("leaveliststr:"+leavelist);
        for (int i=0;i<leavelist.size();i++){
            JSONObject detpersonobj = new JSONObject();
            detpersonobj.put("USERID",leavelist.get(i));
            String result1 = service1.execute("QW_DeletePersonSingle", detpersonobj.toJSONString());
            JSONObject resultobj1 = JSONObject.parseObject(result1);
            String errcode1 = Util.null2String(resultobj1.get("code"));
            if (!errcode1.equals("0")) {
                String errmsg1 = Util.null2String(resultobj1.get("msg"));
                JSONObject del_Object = new JSONObject();
                del_Object.put("USERID", Util.null2String(detpersonobj.toJSONString()));
                //del_Object.put("reason", Util.null2String(resultobj1.get("msg")));
                del_err.add(del_Object);
                errdetnum++;
                log.error("人员" + leavelist.get(i) + "删除失败;" + del_err.toJSONString());
            } else {
                JSONObject del_Object = new JSONObject();
                del_Object.put("USERID", Util.null2String(detpersonobj.toJSONString()));
                //del_Object.put("reason", Util.null2String(resultobj1.get("msg")));
                del_success.add(del_Object);
                log.error("人员" + leavelist.get(i) + "删除成功;");
                detnum++;
            }
        }

        jsonObj_result.put("code","S");
        jsonObj_result.put("msg", "人员接口调用成功:" + totalnum);
        jsonObj_result.put("successinfos", "删除成功人员总数:"+detnum+"删除人员名单："+del_success.toJSONString()+",新增成功人员总数:" + addnum + ",新增人员信息如下:" + sb_success+"更新成功人员总数："+upnum+",更新成功人员："+UP_Arr.toJSONString());
        jsonObj_result.put("errorinfos", "删除失败人员总数："+errdetnum+"删除失败人员名单"+del_err.toJSONString()+"新增失败人员总数:" + errnum+"新增失败人员:"+sb_errorArr.toJSONString()+"更新失败人员总数:" + updateerrnum+",更新失败人员："+up_errorArr.toJSONString());

        log.error("企微_人员导入定时任务--->end");
        return jsonObj_result;
    }


    public static void main(String[] args) {
        JSONArray objects = new JSONArray();
        objects.add("1");
        objects.add("2");
        objects.add("3");
        objects.add("1");
        System.out.println(objects.toString());

    }

    public static JSONObject Regettoken(){
        JSONObject jsonObj_result = new JSONObject();

        EsbService service = EsbClient.getService();
        String result = service.execute("TX_GETToken", "");
        JSONObject resultobj = JSONObject.parseObject(result);
        String code = Util.null2String(resultobj.get("code"));
        log.error("Regettoken_result:" + resultobj);
        String access_token = "";
        if (!code.equals("100")) {
            jsonObj_result.put("flag", "E");
            jsonObj_result.put("msg", "获取access_token失败");
        } else {
            JSONObject data = JSON.parseObject(Util.null2String(resultobj.get("data")));
            access_token = data.getString("access_token");
            log.error("access_token:" + access_token);
            jsonObj_result.put("flag","S");
            jsonObj_result.put("access_token",access_token);
        }
        return jsonObj_result;
    }
}
