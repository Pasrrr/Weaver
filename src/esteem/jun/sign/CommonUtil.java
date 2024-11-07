package esteem.jun.sign;

import weaver.conn.RecordSet;
import weaver.general.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2022-11-30 11:25
 * @Description:
 */
public class CommonUtil {

    public static Map<String,String> getHRMapById(String id){
        Map<String,String> map =new HashMap<String,String>();
        RecordSet rs = new RecordSet();
        rs.execute("select * from hrmresource  where id='" + id + "'");
        if (rs.next()) {
            map.put("createid", Util.null2String(rs.getString("id")));
            map.put("departmentid", Util.null2String(rs.getString("departmentid")));
            map.put("subcompanyid", Util.null2String(rs.getString("subcompanyid1")));
            map.put("jobtitle", Util.null2String(rs.getString("jobtitle")));
            map.put("lastname", Util.null2String(rs.getString("lastname")));
            map.put("mobile", Util.null2String(rs.getString("mobile")));
        }
        return map;
    }

    public static Map<String, String> getMainDataByRequestId(String requestId, String mainTable) {
        return getMainDataByRequestId(requestId, mainTable, "*");
    }

    public static Map<String, String> getMainDataByRequestId(String requestId, String mainTable, String sqlfield) {
        RecordSet rs = new RecordSet();
        String sql = "select " + sqlfield + " from " + mainTable + " where requestid=?";
        rs.executeQuery(sql, new Object[]{requestId});
        return rs.next() ? recordSet2Map(rs) : null;
    }

    public static String getNameBygsid(String qyf1) {
        String dwmc="";
        RecordSet rs = new RecordSet();
        String sql="select * from  uf_gsqdlb where id='"+qyf1+"'";
        rs.execute(sql);
        if (rs.next()) {
            dwmc=Util.null2String(rs.getString("dwmc"));
        }
        return dwmc;
    }

    public static Map<String, String> recordSet2Map(RecordSet rs) {
        String[] colunms = rs.getColumnName();
        Map<String, String> result = new HashMap(colunms.length);
        int i = 0;

        for(int maxLen = colunms.length; i < maxLen; ++i) {
            result.put(colunms[i].toLowerCase(), rs.getString(colunms[i]));
        }

        return result;
    }

    public static List<Map<String, String>> getDetailDataByMainId(int mainId, String detailTable) {
        List<Map<String, String>> result = new ArrayList();
        RecordSet rs = new RecordSet();
        String sql = "select * from " + detailTable + " where mainid=? order by id asc";
        rs.executeQuery(sql, new Object[]{mainId});

        while(rs.next()) {
            result.add(recordSet2Map(rs));
        }
        return result;
    }




}