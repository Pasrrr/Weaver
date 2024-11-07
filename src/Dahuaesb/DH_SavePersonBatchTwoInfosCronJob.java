package Dahuaesb;

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
 * @description: 大华_人员批量导入定时任务_基于单人员导入接口循环调用
 * @date 2023-03-29 14:12
 */
public class DH_SavePersonBatchTwoInfosCronJob {
    private static Log log = LogFactory.getLog(DH_SavePersonBatchTwoInfosCronJob.class);

    public Map execute(Map param) {
        log.error("大华_人员批量导入定时任务--->begin");
        RecordSet rsquery = new RecordSet();
        RecordSet rsquery1 = new RecordSet();
        String isall = Util.null2String(param.get("isall"));//逻辑标识 Y - 插入全部,默认当天和前一天
        log.error("isall:" + isall);
        //响应JSON对象
        JSONObject jsonObj_result = new JSONObject();
        //接口调用开始
        EsbService service = EsbClient.getService();
        String result = service.execute("DH_GetAuthorization", "");
        JSONObject resultobj = JSONObject.parseObject(result);
        String code = Util.null2String(resultobj.get("code"));

        log.error("resultobj:" + resultobj);
        String authorization = "";
        if (!code.equals("100")) {
            jsonObj_result.put("flag", "E");
            jsonObj_result.put("msg", "鉴权接口调用失败");
            return jsonObj_result;
        } else {
            JSONObject data = JSON.parseObject(Util.null2String(resultobj.get("data")));
            authorization = data.getString("Authorization");
            log.error("authorization:" + authorization);
        }

        int addnum = 0;//插入成功人员总数
        StringBuilder sb_success = new StringBuilder();

        int errnum = 0;//插入失败人员总数
        JSONArray sb_errorArr = new JSONArray();

        int totalnum = 0;//插入人员总数
        int detnum = 0;//删除人员总数

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


        //增加过滤 - 人员取当天和前一天新增的进行插入
        if (isall.equalsIgnoreCase("N")) {
            queryUserSql = queryUserSql + " AND (a.createdate = '" + datestr + "' OR a.createdate = '" + datestrold + "' " +
                    "OR a.lastmoddate='" + datestr + "'OR a.lastmoddate='" + datestrold + "')";//新增人员更新过滤
            //queryUserSql = queryUserSql + " AND (a.createdate = '2020-01-13' OR a.createdate = '2020-01-12')";

        }
        log.error("queryUserSql:" + queryUserSql);

        //人员离职身份证数组
        JSONArray leavelist = new JSONArray();

        rsquery.execute(queryUserSql);
        while (rsquery.next()) {
            String status = Util.null2String(rsquery.getString("STATUS"));//id
            String certificatenum = Util.null2String(rsquery.getString("certificatenum")).trim();//身份证号
            if (status.equals("5")) {
                //人员离职
                leavelist.add(certificatenum);
                detnum++;
            } else {

                String id = Util.null2String(rsquery.getString("id"));//id
                String querydatesql = "SELECT b.field14 AS carnum, b.field15 AS mealallowance FROM cus_fielddata b  WHERE b.scope = 'HrmCustomFieldByInfoType' AND b.scopeid = - 1 " +
                        "AND b.id = " + id;
                rsquery1.execute(querydatesql);
                String carnum = "";
                if (rsquery1.next()) {
                    carnum = Util.null2String(rsquery1.getString("carnum"));//车牌号码
                }
                JSONObject addpersonobj = new JSONObject();
                //String id = Util.null2String(rsquery.getInt("id"));
                String lastname = Util.null2String(rsquery.getString("lastname")).trim();//人员姓名
                String workcode = Util.null2String(rsquery.getString("workcode")).trim();//人员编码


                String departmentid = Util.null2String(rsquery.getString("departmentid"));//部门ID
                // String phone = Util.null2String(rsquery.getString("mobile"));//电话
                String email = Util.null2String(rsquery.getString("email")).trim();//电子邮箱
                String birthday = Util.null2String(rsquery.getString("birthday"));//生日
                String sex = Util.null2String(rsquery.getString("sex"));//性别
                //log.error("carnum:"+carnum);
                // String mealallowance = Util.null2String(rsquery.getString("mealallowance"));//餐补金额
//            String icnum = Util.null2String(rsquery.getString("icnum"));//IC卡号
                //
                //addpersonobj.put("id",id);
                addpersonobj.put("name", lastname);
                addpersonobj.put("code", certificatenum);
                addpersonobj.put("paperType", "-1");
                addpersonobj.put("paperNumber", certificatenum);
                addpersonobj.put("paperAddress", "杭州");
                //addpersonobj.put("departmentId",departmentid);
                addpersonobj.put("departmentCode", departmentid);
                addpersonobj.put("sex", sex);
                // addpersonobj.put("phone", phone);
                addpersonobj.put("email", email);
                addpersonobj.put("birthday", birthday);
                addpersonobj.put("service", "evo-thirdParty");
                addpersonobj.put("Authorization", authorization);
                addpersonobj.put("User-Id", "1");
                //车辆数组
                JSONArray carsarr = new JSONArray();
                if (!carnum.equals("")) {
                    JSONObject carNumobj = new JSONObject();
                    carNumobj.put("carNum", carnum);
                    //log.error("carNumobj:"+carNumobj.toString());
                    carsarr.add(carNumobj);
                }
                addpersonobj.put("cars", carsarr);

                log.error("addpersonobj:" + addpersonobj.toString());
                totalnum++;
                //自定义字段对象
//            fieldExtobj.put("mealallowance",mealallowance);
//            fieldExtobj.put("icnum",icnum);
                if (!authorization.equals("")) {
                    //调用人员批量新增接口
                    EsbService service2 = EsbClient.getService();
                    EsbService service3 = EsbClient.getService();

                    String result2 = service2.execute("DH_SavePersonSingleInfosCronJob", addpersonobj.toJSONString());
                    JSONObject resultobj2 = JSONObject.parseObject(result2);
                    //log.error("resultobj2:"+resultobj2.toString());

                    String code2 = Util.null2String(resultobj2.get("code"));
                    if (!code2.equals("100")) {

                        String errmsg = Util.null2String(resultobj2.get("msg"));
                        if (errmsg.equalsIgnoreCase("person code already exist")) {
                            addnum++;
                            sb_success.append(lastname).append("_").append(workcode).append(";");
//                      //人员已存在,进入更新程序
                            //log.error("人员已存在,进入更新程序-->");
                            JSONObject updatepersonobj = new JSONObject();
                            updatepersonobj.put("birthday", birthday);
                            updatepersonobj.put("paperType", "-1");
                            updatepersonobj.put("code", certificatenum);
                            updatepersonobj.put("departmentCode", departmentid);
                            updatepersonobj.put("sex", sex);
                            updatepersonobj.put("name", lastname);
                            updatepersonobj.put("paperNumber", certificatenum);
                            updatepersonobj.put("carNum", carnum);
                            updatepersonobj.put("email", email);
                            updatepersonobj.put("paperAddress", "杭州");
                            updatepersonobj.put("Authorization", authorization);
                            updatepersonobj.put("User-Id", "1");
                            log.error("人员已存在,进入更新程序-->" + updatepersonobj.toString());

                            String result3 = service3.execute("DH_UpdatePersonSingleInfosCronJob", updatepersonobj.toJSONString());
                            JSONObject resultobj3 = JSONObject.parseObject(result3);
                            String code3 = Util.null2String(resultobj3.get("code"));
                            if (!code3.equals("100")) {

                                String errmsg3 = Util.null2String(resultobj3.get("msg"));
                                log.error("人员" + lastname + "更新失败;" + errmsg3);

                            } else {
                                log.error("人员" + lastname + "更新成功;");
                            }
                        } else {
                            JSONObject sb_errorObject = new JSONObject();
                            sb_errorObject.put("lastname", lastname);
                            sb_errorObject.put("workcode", certificatenum);
                            sb_errorObject.put("reason", errmsg);
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
                    sb_errorObject.put("workcode", certificatenum);
                    sb_errorObject.put("reason", "authorization为空");
                    sb_errorArr.add(sb_errorObject);
                }
            }
        }
        //人员离职接口调用
        EsbService service1 = EsbClient.getService();
        JSONObject detpersonobj = new JSONObject();
        log.error("leaveliststr:"+leavelist);
        detpersonobj.put("codes",leavelist);
        detpersonobj.put("Authorization", authorization);
        String result1 = service1.execute("DH_DeletePersonToDHAPI", detpersonobj.toJSONString());
        JSONObject resultobj1 = JSONObject.parseObject(result1);
        String code1 = Util.null2String(resultobj1.get("code"));
        if (!code1.equals("100")) {

            String errmsg1 = Util.null2String(resultobj1.get("msg"));
            log.error("人员" + leavelist + "删除失败;" + errmsg1);

        } else {
            log.error("人员" + leavelist + "删除成功;");
        }
        jsonObj_result.put("flag", "S");
        jsonObj_result.put("msg", "人员接口调用成功:" + totalnum);
        jsonObj_result.put("successinfos", "删除成功人员总数:"+detnum+",插入成功人员总数:" + addnum + ",插入人员信息如下:" + sb_success);
        jsonObj_result.put("errornums", "插入失败人员总数:" + errnum);
        jsonObj_result.put("errorarr", sb_errorArr);

        log.error("大华_人员导入定时任务--->end");
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
}
