package com.api.wx.services.Impl;

import com.alibaba.fastjson.JSON;
import com.api.wx.services.DaHua_OaKaoQinService;
import com.api.wx.vo.ExtendVO;
import com.api.wx.vo.ResponseVO;
import org.apache.axis.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSetTrans;

import java.util.regex.Pattern;

public class DaHua_OaKaoQinServiceImpl implements DaHua_OaKaoQinService {

    private Log log = LogFactory.getLog(DaHua_OaKaoQinServiceImpl.class.getName());


    String sql1 = "INSERT INTO hrmschedulesign (USERID, SIGNDATE,SIGNTIME) VALUES (?, ?,?)";
    String sql2 = "INSERT INTO kq_format_pool (resourceid, kqdate) VALUES (?, ?)";

    @Override
    public String insertWorkAttendance(ExtendVO extendVO) {

        log.info("personCode: "+extendVO.getPersonCode());
        log.info("date :"+extendVO.getSwingTime());
        //年-月-日
        String SIGNDATE;

        //时:分:秒
        String SIGNTIME;

        //人员id
        String personCode;

        /**
         * 参数为空返回异常
         */
        if (StringUtils.isEmpty(extendVO.getSwingTime()) &&
                StringUtils.isEmpty(extendVO.getPersonCode())) {
            log.info("/wxoa/mk 接口参数为空: ");
            return JSON.toJSONString(new ResponseVO(false, "10001", "入参不能为空", null));
        }

        personCode = extendVO.getPersonCode();
        log.info("personCode: "+personCode);
        log.info("date :"+extendVO.getSwingTime());
        //日期校验
        if (isValid(extendVO.getSwingTime())) {
            String[] date = DateProcessing(extendVO.getSwingTime());
            SIGNDATE = date[0];
            SIGNTIME = date[1];
            log.info("SIGNDATE: "+SIGNDATE+" "+"SIGNTIME: "+SIGNTIME);
        } else
            return JSON.toJSONString(new ResponseVO(false, "10002", "日期参数格式不对", null));

        //正式写入
        RecordSetTrans rst = new RecordSetTrans();
        //开启事务
        rst.setAutoCommit(false);
        try {
            rst.executeUpdate(sql1, personCode, SIGNDATE, SIGNTIME);
            rst.executeUpdate(sql2, personCode, SIGNDATE);
            // 提交事务
            rst.commit();
            log.info("提交事务成功");
        } catch (Exception e) {
            log.error("sql执行失败:"+e.getMessage());
            // 事务回滚
            rst.rollback();
            return JSON.toJSONString(new ResponseVO(false, "10002", e.getMessage(), null));

        }

        return JSON.toJSONString(new ResponseVO(true, "0", "", "写入数据成功"));
    }


    /**
     * 传入日期进行合规判断
     */
    public boolean isValid(String str) {
        // 校验规则："2022-10-12 07:56:14"
        String pattern = "^\\d{4}-\\d{2}-\\d{2} (\\d{2}):(\\d{2}):(\\d{2})$";
        return Pattern.matches(pattern, str);
    }

    public String[] DateProcessing(String date) {

        return date.split(" ");
    }

}
