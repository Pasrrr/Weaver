package Qian.common.Util;


import esteem.jun.common.util.UF_SAP_PRO_LOG;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.file.Prop;
import weaver.formmode.webservices.ModeDataServiceImpl;
import weaver.general.Util;

import java.util.Map;

/**
 * @className: ModeDataUtil
 * @author: cao
 * @date: 2023-11-24 10:41
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
        stringBuffer.append("<id>"+Util.null2String(modeDataMainMap.get("Departmentid"))+"</id>");
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
    /**
     * @method: SaveQianDepartmentInfo
     * @author: cao
     * @date: 2023-11-24 10:41
     * @Depiction: 保存电子签ID至建模
     **/
    public static String SaveQianDepartmentInfo(Qian_Department_log qian_department_log){
        ModeDataServiceImpl modeDateService = new ModeDataServiceImpl();
        String modeid= Prop.getPropValue("qian", "qian.department.log.modeid");
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
        stringBuffer.append("<field><filedname>departmentid</filedname><fileddbtype>varchar(999)</fileddbtype><filedvalue><![CDATA["+qian_department_log.getDepartmentid()+"]]></filedvalue><fieldshowname>Departmentid</fieldshowname></field>");
        stringBuffer.append("<field><filedname>departmentname</filedname><fileddbtype>varchar(999)</fileddbtype><filedvalue><![CDATA["+qian_department_log.getDepartmentname()+"]]></filedvalue><fieldshowname>Departmentname</fieldshowname></field>");
        stringBuffer.append("<field><filedname>parentdeptid</filedname><fileddbtype>varchar(999)</fileddbtype><filedvalue><![CDATA["+qian_department_log.getParentDeptId()+"]]></filedvalue><fieldshowname>ParentDeptId</fieldshowname></field>");
        stringBuffer.append("<field><filedname>qiandeptid</filedname><fileddbtype>varchar(999)</fileddbtype><filedvalue><![CDATA["+qian_department_log.getQianDeptId()+"]]></filedvalue><fieldshowname>QianDeptId</fieldshowname></field>");
        stringBuffer.append("</maintable>");
        stringBuffer.append("</data>");
        stringBuffer.append("</ROOT>");
        String result =modeDateService.saveModeData(stringBuffer.toString());
        return result;
    }
}
