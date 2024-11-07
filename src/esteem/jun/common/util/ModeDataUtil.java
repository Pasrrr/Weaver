package esteem.jun.common.util;


import weaver.file.Prop;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.formmode.webservices.ModeDataServiceImpl;
import weaver.general.Util;

import java.util.Map;

/**
 * @className: ModeDataUtil
 * @author: jun
 * @date: 2021-12-31 10:41
 * @Depiction:
 **/
public class ModeDataUtil {
    private static Log log = LogFactory.getLog(ModeDataUtil.class.getName());

    /**param1 建模主表相关数据 param2 模块id param3 用户id*/
    public static String SaveModeDataInfo(Map<String,Object> modeDataMainMap, String modeId, String userId){
        ModeDataServiceImpl modeDateService = new ModeDataServiceImpl();
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        stringBuffer.append("<ROOT>");
        stringBuffer.append("<header>");
        stringBuffer.append("<userid>"+userId+"</userid>");
        stringBuffer.append("<modeid>"+modeId+"</modeid>");
        /**是否执行页面扩展接口动作 Y执行 N不执行*/
        stringBuffer.append("<doPageexpandInterface>N</doPageexpandInterface>");
        /**如果是新增则值为空 如果有数据则为修改*/
        stringBuffer.append("<id>"+Util.null2String(modeDataMainMap.get("id"))+"</id>");
        stringBuffer.append("</header>");
        stringBuffer.append("<search>");
        stringBuffer.append("<condition/>");
        /**是否验证权限*/
        stringBuffer.append("<right>N</right>");
        stringBuffer.append("</search>");
        stringBuffer.append("<data id=\"\">");
        stringBuffer.append("<maintable>");
        RecordSet recordSet1 = new RecordSet();
        String sql1="select t1.* from workflow_billfield t1 join workflow_bill t2  on t1.billid=t2.id and t1.viewtype ='0' join modeinfo t3 on t3.formid= t2.id where t3.id='"+modeId+"'";
        recordSet1.executeQuery(sql1);
        while(recordSet1.next()) {
            String fieldname = Util.null2String(recordSet1.getString("fieldname"));
            String fielddbtype = Util.null2String(recordSet1.getString("fielddbtype"));
            if (null != modeDataMainMap.get(fieldname)) {
                stringBuffer.append("<field><filedname>" + fieldname + "</filedname><fileddbtype>" + fielddbtype + "</fileddbtype><filedvalue><![CDATA[" + modeDataMainMap.get(fieldname) + "]]></filedvalue></field>");
            }
        }
        stringBuffer.append("</maintable>");
        stringBuffer.append("</data>");
        stringBuffer.append("</ROOT>");
        String result =modeDateService.saveModeData(stringBuffer.toString());
        return result;
    }

    public static String SaveSapProLogInfo(UF_SAP_PRO_LOG uf_sap_pro_log){
        ModeDataServiceImpl modeDateService = new ModeDataServiceImpl();
        String modeid= Prop.getPropValue("wanxiang", "sap.pro.log.modeid");
        log.info("modeid："+modeid);
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        stringBuffer.append("<ROOT>");
        stringBuffer.append("<header>");
        stringBuffer.append("<userid>1</userid>");
        stringBuffer.append("<modeid>"+modeid+"</modeid>");
        stringBuffer.append("<doPageexpandInterface>N</doPageexpandInterface>");
        stringBuffer.append("<id></id>");
        stringBuffer.append("</header>");
        stringBuffer.append("<search>");
        stringBuffer.append("<condition />");
        stringBuffer.append("<right>N</right>");
        stringBuffer.append("</search>");
        stringBuffer.append("<data id=\"\">");
        stringBuffer.append("<maintable>");
        stringBuffer.append("<field><filedname>rzbt</filedname><fileddbtype>varchar(999)</fileddbtype><filedvalue><![CDATA["+uf_sap_pro_log.getRzbt()+"]]></filedvalue><fieldshowname>日志标题</fieldshowname></field>");
        stringBuffer.append("<field><filedname>rzlx</filedname><fileddbtype>varchar(999)</fileddbtype><filedvalue><![CDATA["+uf_sap_pro_log.getRzlx()+"]]></filedvalue><fieldshowname>日志类型</fieldshowname></field>");
        stringBuffer.append("<field><filedname>fssj</filedname><fileddbtype>varchar(999)</fileddbtype><filedvalue><![CDATA["+uf_sap_pro_log.getFssj()+"]]></filedvalue><fieldshowname>发送数据</fieldshowname></field>");
        stringBuffer.append("<field><filedname>jssj</filedname><fileddbtype>varchar(999)</fileddbtype><filedvalue><![CDATA["+uf_sap_pro_log.getJssj()+"]]></filedvalue><fieldshowname>接收数据</fieldshowname></field>");
        stringBuffer.append("<field><filedname>jxhjg</filedname><fileddbtype>varchar(999)</fileddbtype><filedvalue><![CDATA["+uf_sap_pro_log.getJxhjg()+"]]></filedvalue><fieldshowname>解析后结果</fieldshowname></field>");
        stringBuffer.append("<field><filedname>qxx</filedname><fileddbtype>varchar(999)</fileddbtype><filedvalue><![CDATA["+uf_sap_pro_log.getQxx()+"]]></filedvalue><fieldshowname>其他信息</fieldshowname></field>");
        stringBuffer.append("<field><filedname>xgid</filedname><fileddbtype>varchar(999)</fileddbtype><filedvalue><![CDATA["+uf_sap_pro_log.getXgid()+"]]></filedvalue><fieldshowname>相关ID</fieldshowname></field>");
        stringBuffer.append("<field><filedname>fsf</filedname><fileddbtype>varchar(999)</fileddbtype><filedvalue><![CDATA["+uf_sap_pro_log.getFsf()+"]]></filedvalue><fieldshowname>发送方</fieldshowname></field>");
        stringBuffer.append("<field><filedname>xglc</filedname><fileddbtype>varchar(999)</fileddbtype><filedvalue><![CDATA["+uf_sap_pro_log.getXglc()+"]]></filedvalue><fieldshowname>相关流程</fieldshowname></field>");
        stringBuffer.append("<field><filedname>zhzt</filedname><fileddbtype>varchar(999)</fileddbtype><filedvalue><![CDATA["+uf_sap_pro_log.getZhzt()+"]]></filedvalue><fieldshowname>执行状态</fieldshowname></field>");
        stringBuffer.append("<field><filedname>hs</filedname><fileddbtype>varchar(999)</fileddbtype><filedvalue><![CDATA["+uf_sap_pro_log.getHs()+"]]></filedvalue><fieldshowname>耗时</fieldshowname></field>");
        stringBuffer.append("<field><filedname>qqdz</filedname><fileddbtype>varchar(999)</fileddbtype><filedvalue><![CDATA["+uf_sap_pro_log.getQqdz()+"]]></filedvalue><fieldshowname>请求地址</fieldshowname></field>");
        stringBuffer.append("<field><filedname>jkdz</filedname><fileddbtype>varchar(999)</fileddbtype><filedvalue><![CDATA["+uf_sap_pro_log.getJkdz()+"]]></filedvalue><fieldshowname>接口地址</fieldshowname></field>");
        stringBuffer.append("</maintable>");
        stringBuffer.append("</data>");
        stringBuffer.append("</ROOT>");
        String result =modeDateService.saveModeData(stringBuffer.toString());
        return result;
    }
}
