package esteem.jun.wanxiang.services.impl;

import esteem.jun.common.util.CommonUtil;
import esteem.jun.common.util.ModeDataUtil;
import esteem.jun.wanxiang.action.bean.ResultMesage;
import esteem.jun.wanxiang.req.*;
import esteem.jun.wanxiang.services.WanXiangXgxxService;
import org.apache.axis.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.file.Prop;
import weaver.general.Util;
import weaver.workflow.webservices.*;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

import static weaver.general.Util.null2String;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Code of Passion
 * @Date: 2022-11-22 10:48
 * @Description:
 */
public class WanXiangXgxxServiceImpl implements WanXiangXgxxService {
    public static final String CPUDT = "CPUDT";
    private Log log = LogFactory.getLog(WanXiangXgxxServiceImpl.class.getName());
    String sql = " select hrmresource.id,hrmresource.subcompanyid1 from hrmresource join cus_fielddata on hrmresource.id =cus_fielddata.id and cus_fielddata.SCOPEID='-1' and cus_fielddata.field13 = ?";

    @Override
    public ResultMesage creatPurchaseRequisitionWorkFlow(PurchaseRequisitionReq purchaseRequisitionReq) throws UnsupportedEncodingException {
        String workflowid = Prop.getPropValue("wanxiang", "cgsq.workflowid");
        log.info("------------------------采购申购流程(SAP)----------------------------begin");
        log.info("参数信息：" + purchaseRequisitionReq.toString());
        ResultMesage resultMesage = new ResultMesage();
        try {
            RecordSet recordSet = new RecordSet();
            RecordSet recordSet1 = new RecordSet();
            if (StringUtils.isEmpty(purchaseRequisitionReq.getBNAME())) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:申请人信息不能为空");
                return resultMesage;
            }
            String creater = "";
            String subcompanyid1="";
            //String sql=" select hrmresource.id from hrmresource join cus_fielddata on cus_fielddata.SCOPEID='-1' and cus_fielddata.field13 = '"+Util.null2String(purchaseRequisitionReq.getBNAME())+"'";
            recordSet.executeQuery(sql, null2String(purchaseRequisitionReq.getBNAME()));
            if (recordSet.next()) {
                creater = null2String(recordSet.getString("id"));
                subcompanyid1=null2String(recordSet.getString("subcompanyid1"));
            }
            if ("".equals(creater)) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:无法获取创建人ID");
                return resultMesage;
            }
            if ("99".equals(subcompanyid1)){
                workflowid=Prop.getPropValue("wanxiang", "cgsqout.workflowid");
            }
            Map<String, String> hrMap = CommonUtil.getHRMapById(creater);
            String createrId = null2String(hrMap.get("createid"));
            Date now = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            /*------------------------------------------主表字段处理--------------------------------------------------------------*/
            List<String> mainInfoList = new ArrayList<>();
            /**经办人*/
            mainInfoList.add("jbr");
            /**申请部门*/
            mainInfoList.add("sgbm");
            /**申请日期*/
            mainInfoList.add("sqrq");
            /**单号*/
            mainInfoList.add("dh");
            /**采购申请编号*/
            mainInfoList.add("BANFN");
            /**采购由请类型*/
            mainInfoList.add("BSART");
            /**采购申请类型描述 */
            mainInfoList.add("BATXT");

            Map<String, String> mainFieldsMap = new HashMap<>();
            /**经办人*/
            mainFieldsMap.put("jbr", null2String(createrId));
            /**所属部门*/
            mainFieldsMap.put("sgbm", null2String(hrMap.get("departmentid")));
            /**申请日期*/
            mainFieldsMap.put("sqrq", null2String(df.format(now)));
            /**采购申请编号*/
            mainFieldsMap.put("BANFN", null2String(purchaseRequisitionReq.getBANFN()));
            /**采购由请类型*/
            mainFieldsMap.put("BSART", null2String(purchaseRequisitionReq.getBSART()));
            /**采购申请类型描述*/
            mainFieldsMap.put("BATXT", null2String(purchaseRequisitionReq.getBATXT()));
            /*------------------------------------------明细字段处理--------------------------------------------------------------*/
            List<List<Map<String, String>>> detailLists = new ArrayList<>();
            List<Map<String, String>> detail1List = new ArrayList<>();
            for (int i = 0; i < purchaseRequisitionReq.getItem().size(); i++) {
                PurchaseRequisitionDetailReq purchaseRequisitionDetailReq = purchaseRequisitionReq.getItem().get(i);
                log.info("参数信息：" + purchaseRequisitionDetailReq.toString());
                Map<String, String> dt1Map = new HashMap<>();
                /**判断是否删除*/
                String loekz =null2String(purchaseRequisitionDetailReq.getLOEKZ());
                try{
                    if (loekz.equals("X")){
                        continue;
                    }
                }catch (Exception e){
                    log.info("筛选运行异常");
                }
                /**物料编码*/
                dt1Map.put("MATNR", null2String(purchaseRequisitionDetailReq.getMATNR()));
                /**物料描述*/
                dt1Map.put("chmc", null2String(purchaseRequisitionDetailReq.getTXZ01()));
                /**申请数量*/
                dt1Map.put("sl", null2String(purchaseRequisitionDetailReq.getMENGE()));
                /**单位*/
                dt1Map.put("dw", null2String(purchaseRequisitionDetailReq.getMEINS()));
                /**交货日期*/
                dt1Map.put("xqrq", null2String(purchaseRequisitionDetailReq.getLFDAT()));

                /**需求人*/
                dt1Map.put("xqr1", null2String(purchaseRequisitionDetailReq.getAFNAM()));

/*                String xqr="";
                if(!"".equals(Util.null2String(purchaseRequisitionDetailReq.getAFNAM()))){
                    recordSet1.executeQuery(sql,Util.null2String(purchaseRequisitionDetailReq.getAFNAM()));
                    if(recordSet1.next()){
                        xqr=Util.null2String(recordSet1.getString("id"));
                    }
                    *//**需求人*//*
                    dt1Map.put("xqr", Util.null2String(xqr));
                }*/
                /**采购申请中的价格*/
                dt1Map.put("yjdj", null2String(purchaseRequisitionDetailReq.getPREIS()));

                /**价格单位*/
                dt1Map.put("jgdw", null2String(purchaseRequisitionDetailReq.getPEINH()));

                /**品牌*/
                dt1Map.put("pp", null2String(purchaseRequisitionDetailReq.getZBRAND()));
                /**是否已签合同*/
                dt1Map.put("sfydjht", null2String(purchaseRequisitionDetailReq.getZCONTRACT()));
                /**所属预算*/
                dt1Map.put("szys1", null2String(purchaseRequisitionDetailReq.getZBUDGET()));
                /**申请理由*/
                dt1Map.put("sqly", null2String(purchaseRequisitionDetailReq.getZREASON()));
                /**使用产线*/
                dt1Map.put("sycx", null2String(purchaseRequisitionDetailReq.getZSHYCHX()));
                /**具体使用设备*/
                dt1Map.put("jtsysb", null2String(purchaseRequisitionDetailReq.getZSHYSHB()));
                /**最低订单单价*/
                dt1Map.put("ZLOWESTPR", null2String(purchaseRequisitionDetailReq.getZLOWESTPR()));
                /**最低订单单价日期*/
                dt1Map.put("ZLOWESTDT", null2String(purchaseRequisitionDetailReq.getZLOWESTDT()));
                /**最近订单单价*/
                dt1Map.put("ZLASTPR", null2String(purchaseRequisitionDetailReq.getZLASTPR()));
                /**最近订单单价日期*/
                dt1Map.put("ZLASTDT", null2String(purchaseRequisitionDetailReq.getZLASTDT()));
                /**币种*/
                dt1Map.put("WAERS", null2String(purchaseRequisitionDetailReq.getWAERS()));
                /**成本中心*/
                dt1Map.put("KOSTL", null2String(purchaseRequisitionDetailReq.getKOSTL()));
                /**总账科目*/
                dt1Map.put("SAKTO", null2String(purchaseRequisitionDetailReq.getSAKTO()));

                detail1List.add(dt1Map);
            }
            detailLists.add(detail1List);
            String _ret = creatWorkFlow(workflowid, mainInfoList, mainFieldsMap, detailLists, createrId, hrMap.get("lastname"));
            //单据编号
            resultMesage.setDjbh(_ret);
            log.info("结果：" + _ret);
            if (!StringUtils.isEmpty(_ret) && Integer.parseInt(_ret) > 0) {
                resultMesage.setFlag("0");
                resultMesage.setMessage("流程创建成功!");
            } else {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:创建流程失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMesage.setFlag("1");
            resultMesage.setMessage("Interface exception");
            return resultMesage;
        }
        log.info("------------------------采购申购流程(SAP)流程----------------------------end");
        return resultMesage;
    }

    @Override
    public ResultMesage creatRawMaterialUnqualifiedWorkFlow(RawMaterialUnqualifiedReq rawMaterialUnqualifiedReq) throws UnsupportedEncodingException {
        String workflowid = Prop.getPropValue("wanxiang", "yclbhgp.workflowid");
        log.info("------------------------原材料不合格处理流程(SAP)----------------------------begin");
        log.info("参数信息：" + rawMaterialUnqualifiedReq.toString());
        ResultMesage resultMesage = new ResultMesage();
        try {
            RecordSet recordSet = new RecordSet();
            if (StringUtils.isEmpty(rawMaterialUnqualifiedReq.getERNAM())) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:申请人信息不能为空");
                return resultMesage;
            }
            String creater = "";

            //String sql=" select hrmresource.id from hrmresource join cus_fielddata on cus_fielddata.SCOPEID='-1' and cus_fielddata.field13 = '"+Util.null2String(rawMaterialUnqualifiedReq.getERNAM())+"'";
            recordSet.executeQuery(sql, null2String(rawMaterialUnqualifiedReq.getERNAM()));
            if (recordSet.next()) {
                creater = null2String(recordSet.getString("id"));
            }
            if ("".equals(creater)) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:无法获取创建人ID");
                return resultMesage;
            }
            Map<String, String> hrMap = CommonUtil.getHRMapById(creater);
            String createrId = null2String(hrMap.get("createid"));
            Date now = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            /*------------------------------------------主表字段处理--------------------------------------------------------------*/
            List<String> mainInfoList = new ArrayList<>();
            /**创建人*/
            mainInfoList.add("ERNAM");
            /**部门*/
            mainInfoList.add("bm");
            /**SAP质量通知单编码*/
            mainInfoList.add("QMNUM");
            /**创建日期*/
            mainInfoList.add("ERDAT");
            /**物料编号*/
            mainInfoList.add("MATNR");
            /**物料描述*/
            mainInfoList.add("maktx");
            /**供应商描述*/
            mainInfoList.add("NAME1");
            /**检验数量*/
            mainInfoList.add("BZMNG");
            /**参考编号*/
            mainInfoList.add("REFNUM");
            /**不良情况描述*/
            mainInfoList.add("blqkms");
            /**缺陷类型代码组描述*/
            mainInfoList.add("KURZTEXT");
            /**万向批次号*/
            mainInfoList.add("ZZBATCH");

            Map<String, String> mainFieldsMap = new HashMap<>();
            /**SAP质量通知单编码*/
            mainFieldsMap.put("QMNUM", null2String(rawMaterialUnqualifiedReq.getQMNUM()));
            /**创建人*/
            mainFieldsMap.put("ERNAM", null2String(createrId));

            mainFieldsMap.put("bm", null2String(hrMap.get("departmentid")));
            /**创建日期*/
            mainFieldsMap.put("ERDAT", null2String(df.format(now)));
            /**物料编号*/
            mainFieldsMap.put("MATNR", null2String(rawMaterialUnqualifiedReq.getMATNR()));
            /**物料描述*/
            mainFieldsMap.put("maktx", null2String(rawMaterialUnqualifiedReq.getMaktx()));
            /**供应商描述*/
            mainFieldsMap.put("NAME1", null2String(rawMaterialUnqualifiedReq.getNAME1()));
            /**检验数量*/
            mainFieldsMap.put("BZMNG", null2String(rawMaterialUnqualifiedReq.getBZMNG()));
            /**参考编号*/
            mainFieldsMap.put("REFNUM", null2String(""));
            /**不良情况描述*/
            mainFieldsMap.put("blqkms", null2String(""));
            /**缺陷类型代码组描述*/
            mainFieldsMap.put("KURZTEXT", null2String(rawMaterialUnqualifiedReq.getKURZTEXT()));
            /**万向批次号*/
            mainFieldsMap.put("ZZBATCH", null2String(rawMaterialUnqualifiedReq.getZZBATCH()));
            /*------------------------------------------明细字段处理--------------------------------------------------------------*/
            List<List<Map<String, String>>> detailLists = new ArrayList<>();
            String _ret = creatWorkFlow(workflowid, mainInfoList, mainFieldsMap, detailLists, createrId, hrMap.get("lastname"));
            //单据编号
            resultMesage.setDjbh(_ret);
            log.info("结果：" + _ret);
            if (!StringUtils.isEmpty(_ret) && Integer.parseInt(_ret) > 0) {
                resultMesage.setFlag("0");
                resultMesage.setMessage("流程创建成功!");
            } else {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:创建流程失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMesage.setFlag("1");
            resultMesage.setMessage("Interface exception");
            return resultMesage;
        }
        log.info("------------------------原材料不合格处理流程(SAP)流程----------------------------end");
        return resultMesage;
    }

    @Override
    public ResultMesage creatRawMaterialCheckWorkFlow(RawMaterialCheckReq rawMaterialCheckReq) throws UnsupportedEncodingException {
        String workflowid = Prop.getPropValue("wanxiang", "ycljy.workflowid");
        log.info("------------------------原材料检验处理流程(SAP)----------------------------begin");
        log.info("参数信息：" + rawMaterialCheckReq.toString());
        ResultMesage resultMesage = new ResultMesage();
        try {
            RecordSet recordSet = new RecordSet();
            if (StringUtils.isEmpty(rawMaterialCheckReq.getERNAM())) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:申请人信息不能为空");
                return resultMesage;
            }
            String creater = "";

            //String sql=" select hrmresource.id from hrmresource join cus_fielddata on cus_fielddata.SCOPEID='-1' and cus_fielddata.field13 = '"+Util.null2String(rawMaterialCheckReq.getERNAM())+"'";
            recordSet.executeQuery(sql, null2String(rawMaterialCheckReq.getERNAM()));
            if (recordSet.next()) {
                creater = null2String(recordSet.getString("id"));
            }
            if ("".equals(creater)) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:无法获取创建人ID");
                return resultMesage;
            }
            Map<String, String> hrMap = CommonUtil.getHRMapById(creater);
            String createrId = null2String(hrMap.get("createid"));
            Date now = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            /*------------------------------------------主表字段处理--------------------------------------------------------------*/
            List<String> mainInfoList = new ArrayList<>();
            /**申请人*/
            mainInfoList.add("sqr");
            /**申请部门*/
            mainInfoList.add("sqbm");
            /**申请日期*/
            mainInfoList.add("sqrq");

            /**检验批编号*/
            mainInfoList.add("PRUEFLOS");
            /**物料号*/
            mainInfoList.add("MATNR");
            /**物料名称*/
            mainInfoList.add("maktx");
            /**SAP批次号*/
            mainInfoList.add("CHARG");
            /**万向批次号*/
            mainInfoList.add("ZZBATCH");
            /**生产日期*/
            mainInfoList.add("HSDAT");
            /**供应商编码*/
            mainInfoList.add("LIFNR");
            /**供应商名称*/
            mainInfoList.add("NAME1");
            /**工厂*/
            mainInfoList.add("WERKS");
            /**库存地点*/
            mainInfoList.add("LAGORTVORG");
            /**检验数量*/
            mainInfoList.add("LOSMENGE");
            /**单位*/
            mainInfoList.add("MENGENEINH");
            /**所需开始日期*/
            mainInfoList.add("PASTRTERM");
            /**要求结束日期*/
            mainInfoList.add("PAENDTERM");

            Map<String, String> mainFieldsMap = new HashMap<>();

            /**申请人*/
            mainFieldsMap.put("sqr", null2String(creater));
            /**申请部门*/
            mainFieldsMap.put("sqbm", null2String(hrMap.get("departmentid")));
            /**申请日期*/
            mainFieldsMap.put("sqrq", null2String(df.format(now)));
            /**检验批编号*/
            mainFieldsMap.put("PRUEFLOS", null2String(rawMaterialCheckReq.getPRUEFLOS()));
            /**物料号*/
            mainFieldsMap.put("MATNR", null2String(rawMaterialCheckReq.getMATNR()));
            /**物料名称*/
            mainFieldsMap.put("maktx", null2String(rawMaterialCheckReq.getMaktx()));
            /**SAP批次号*/
            mainFieldsMap.put("CHARG", null2String(rawMaterialCheckReq.getCHARG()));
            /**万向批次号*/
            mainFieldsMap.put("ZZBATCH", null2String(rawMaterialCheckReq.getZZBATCH()));
            /**生产日期*/
            mainFieldsMap.put("HSDAT", null2String(rawMaterialCheckReq.getHSDAT()));
            /**供应商编码*/
            mainFieldsMap.put("LIFNR", null2String(rawMaterialCheckReq.getLIFNR()));
            /**供应商名称*/
            mainFieldsMap.put("NAME1", null2String(rawMaterialCheckReq.getNAME1()));
            /**工厂*/
            mainFieldsMap.put("WERKS", null2String(rawMaterialCheckReq.getWERKS()));
            /**库存地点*/
            mainFieldsMap.put("LAGORTVORG", null2String(rawMaterialCheckReq.getLAGORTVORG()));
            /**检验数量*/
            mainFieldsMap.put("LOSMENGE", null2String(rawMaterialCheckReq.getLOSMENGE()));
            /**单位*/
            mainFieldsMap.put("MENGENEINH", null2String(rawMaterialCheckReq.getMENGENEINH()));
            /**所需开始日期*/
            mainFieldsMap.put("PASTRTERM", null2String(rawMaterialCheckReq.getPASTRTERM()));
            /**要求结束日期*/
            mainFieldsMap.put("PAENDTERM", null2String(rawMaterialCheckReq.getPAENDTERM()));
            /*------------------------------------------明细字段处理--------------------------------------------------------------*/
            List<List<Map<String, String>>> detailLists = new ArrayList<>();
            String _ret = creatWorkFlow(workflowid, mainInfoList, mainFieldsMap, detailLists, createrId, hrMap.get("lastname"));
            //单据编号
            resultMesage.setDjbh(_ret);
            log.info("结果：" + _ret);
            if (!StringUtils.isEmpty(_ret) && Integer.parseInt(_ret) > 0) {
                resultMesage.setFlag("0");
                resultMesage.setMessage("流程创建成功!");
            } else {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:创建流程失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMesage.setFlag("1");
            resultMesage.setMessage("Interface exception");
            return resultMesage;
        }
        log.info("------------------------原材料检验流程(SAP)流程----------------------------end");
        return resultMesage;
    }

    @Override
    /**采购订单*/
    public ResultMesage creatPurchaseOrderWorkFlow(PurchaseOrderReq purchaseOrderReq) throws UnsupportedEncodingException {
        String ANLKL=null2String(purchaseOrderReq.getANLKL());
        String BSART=null2String(purchaseOrderReq.getBSART());
        String workflowid="";
        if (BSART.equals("Z060")&&ANLKL.equals("3")){
             workflowid = Prop.getPropValue("wanxiang", "fyhcgys.workflowid");
        }else if (BSART.equals("Z030")&&ANLKL.equals("2")){
             workflowid = Prop.getPropValue("wanxiang", "zjgcys.workflowid");
        }else if (BSART.equals("Z030")&&ANLKL.equals("1")){
             workflowid = Prop.getPropValue("wanxiang", "gdzcys.workflowid");
        }else {
             workflowid = Prop.getPropValue("wanxiang", "cgdd.workflowid");
        }
        log.info("------------------------采购订单or验收单处理流程(SAP)----------------------------begin");
        log.info("参数信息：" + purchaseOrderReq.toString());
        ResultMesage resultMesage = new ResultMesage();
        try {
            RecordSet recordSet = new RecordSet();
            if (StringUtils.isEmpty(purchaseOrderReq.getERNAM())) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:申请人信息不能为空");
                return resultMesage;
            }
            String creater = "";
            String subcompanyid1="";
            //String sql=" select hrmresource.id from hrmresource join cus_fielddata on cus_fielddata.SCOPEID='-1' and cus_fielddata.field13 = '"+Util.null2String(rawMaterialCheckReq.getERNAM())+"'";
            recordSet.executeQuery(sql, null2String(purchaseOrderReq.getERNAM()));
            if (recordSet.next()) {
                creater = null2String(recordSet.getString("id"));
                subcompanyid1=null2String(recordSet.getString("subcompanyid1"));
            }
            if ("".equals(creater)) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:无法获取创建人ID");
                return resultMesage;
            }
            if ("99".equals(subcompanyid1)){
                workflowid=Prop.getPropValue("wanxiang", "cgddout.workflowid");
            }
            Map<String, String> hrMap = CommonUtil.getHRMapById(creater);
            String createrId = null2String(hrMap.get("createid"));
            Date now = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            /*------------------------------------------主表字段处理--------------------------------------------------------------*/
            List<String> mainInfoList = new ArrayList<>();
            /**部门*/
            mainInfoList.add("bm");
            /**申请日期*/
            mainInfoList.add("sqrq");
            /**公司代码*/
            mainInfoList.add("BUKRS");
            /**创建对象的人员名称*/
            mainInfoList.add("ERNAM");
            /**采购凭证类别*/
            mainInfoList.add("BSTYP");
            /**采购凭证类型*/
            mainInfoList.add("BSART");
            /**供应商帐户号*/
            mainInfoList.add("LIFNR");
            /**采购组织*/
            mainInfoList.add("EKORG");
            /**采购凭证日期*/
            mainInfoList.add("BEDAT");
            /**审批策略*/
            mainInfoList.add("FRGSX");
            /**批准标识：采购凭证*/
            mainInfoList.add("FRGKE");
            /**发布状态*/
            mainInfoList.add("FRGZU");
            /**采购凭证编号*/
            mainInfoList.add("EBELN");
            /**固定资产类别*/
            mainInfoList.add("ANLKL");
            /**付款类型编码*/
            mainInfoList.add("ZTERM");
            /**付款类型描述*/
            mainInfoList.add("TEXT1");
            /**货币类型*/
            mainInfoList.add("WAERS");
            /**汇率*/
            mainInfoList.add("WKURS");
            /**不含税价*/
            mainInfoList.add("ddzje");
            /**含税价*/
            mainInfoList.add("ddzjews");
            /**万向采购进项税*/
            mainInfoList.add("se");


            Map<String, String> mainFieldsMap = new HashMap<>();
            /**部门*/
            mainFieldsMap.put("bm", null2String(hrMap.get("departmentid")));
            /**申请日期*/
            mainFieldsMap.put("sqrq", null2String(df.format(now)));
            /**公司代码*/
            mainFieldsMap.put("BUKRS", null2String(purchaseOrderReq.getBUKRS()));
            /**创建对象的人员名称*/
            mainFieldsMap.put("ERNAM", null2String(creater));
            /**采购凭证类别*/
            mainFieldsMap.put("BSTYP", null2String(purchaseOrderReq.getBSTYP()));
            /**采购凭证类型*/
            mainFieldsMap.put("BSART", null2String(purchaseOrderReq.getBSART()));
            /**供应商帐户号*/
            mainFieldsMap.put("LIFNR", null2String(purchaseOrderReq.getLIFNR()));
            /**采购组织*/
            mainFieldsMap.put("EKORG", null2String(purchaseOrderReq.getEKORG()));
            /**采购凭证日期*/
            mainFieldsMap.put("BEDAT", null2String(purchaseOrderReq.getBEDAT()));
            /**审批策略*/
            mainFieldsMap.put("FRGSX", null2String(purchaseOrderReq.getFRGSX()));
            /**批准标识：采购凭证 */
            mainFieldsMap.put("FRGKE", null2String(purchaseOrderReq.getFRGKE()));
            /**发布状态 */
            mainFieldsMap.put("FRGZU", null2String(purchaseOrderReq.getFRGZU()));
            /**采购凭证编号*/
            mainFieldsMap.put("EBELN", null2String(purchaseOrderReq.getEBELN()));
            /**固定资产类别*/
            mainFieldsMap.put("ANLKL", null2String(purchaseOrderReq.getANLKL()));
            /**付款类型编码*/
            mainFieldsMap.put("ZTERM", null2String(purchaseOrderReq.getZTERM()));
            /**付款类型描述*/
            mainFieldsMap.put("TEXT1", null2String(purchaseOrderReq.getTEXT1()));
            /**货币类型*/
            mainFieldsMap.put("WAERS", null2String(purchaseOrderReq.getWAERS()));
            /**汇率*/
            mainFieldsMap.put("WKURS", null2String(purchaseOrderReq.getWKURS()));
            /**不含税价*/
            mainFieldsMap.put("ddzje",null2String(purchaseOrderReq.getNETWR()));
            /**含税价*/
            mainFieldsMap.put("ddzjews",null2String(purchaseOrderReq.getBRTWR()));
            /**万向采购进项税*/
            mainFieldsMap.put("se",null2String(purchaseOrderReq.getZSE()));

            /*------------------------------------------明细字段处理--------------------------------------------------------------*/
            List<List<Map<String, String>>> detailLists = new ArrayList<>();
            List<Map<String, String>> detail1List = new ArrayList<>();
            for (int i = 0; i < purchaseOrderReq.getItem().size(); i++) {
                PurchaseOrderDetailReq purchaseOrderDetailReq = purchaseOrderReq.getItem().get(i);
                log.info("参数信息：" + purchaseOrderDetailReq.toString());
                Map<String, String> dt1Map = new HashMap<>();
                /**采购凭证的项目编号 */
                dt1Map.put("EBELP", null2String(purchaseOrderDetailReq.getEBELP()));
                /**科目分配类别*/
                dt1Map.put("KNTTP", null2String(purchaseOrderDetailReq.getKNTTP()));
                /**项目类别*/
                dt1Map.put("PSTYP", null2String(purchaseOrderDetailReq.getPSTYP()));
                /**采购凭证删除标*/
                dt1Map.put("LOEKZ", null2String(purchaseOrderDetailReq.getLOEKZ()));
                /**物料编号*/
                dt1Map.put("MATNR", null2String(purchaseOrderDetailReq.getMATNR()));
                /**工厂*/
                dt1Map.put("WERKS", null2String(purchaseOrderDetailReq.getWERKS()));
                /**采购信息记录号*/
                dt1Map.put("INFNR", null2String(purchaseOrderDetailReq.getINFNR()));
                /**采购订单数量*/
                dt1Map.put("MENGE", null2String(purchaseOrderDetailReq.getMENGE()));
                /**采购订单计量单位*/
                dt1Map.put("MEINS", null2String(purchaseOrderDetailReq.getMEINS()));
                /**订单价格单位(采购)*/
                dt1Map.put("BPRME", null2String(purchaseOrderDetailReq.getBPRME()));
                /**单价*/
                dt1Map.put("NETPR", null2String(purchaseOrderDetailReq.getNETPR()));
                /**总价*/
                dt1Map.put("NETWR", null2String(purchaseOrderDetailReq.getNETWR()));
                /**采购申请编号*/
                dt1Map.put("BANFN", null2String(purchaseOrderDetailReq.getBANFN()));
                /**采购申请的项目编号 */
                dt1Map.put("BNFPO", null2String(purchaseOrderDetailReq.getBNFPO()));
                /**成本中心*/
                dt1Map.put("KOSTL", null2String(purchaseOrderDetailReq.getKOSTL()));
                /**重要的采购协议号*/
                dt1Map.put("KONNR", null2String(purchaseOrderDetailReq.getKONNR()));
                /**基本采购协议的项目编号 */
                dt1Map.put("KTPNR", null2String(purchaseOrderDetailReq.getKTPNR()));
                /**物料描述 */
                dt1Map.put("wlmc", null2String(purchaseOrderDetailReq.getTXZ01()));
                /**资产编号 */
                dt1Map.put("ANLN1", null2String(purchaseOrderDetailReq.getANLN1()));
                /**采购申请单价 */
                dt1Map.put("PREIS", null2String(purchaseOrderDetailReq.getPREIS()));

                detail1List.add(dt1Map);
            }
            detailLists.add(detail1List);


            String _ret = creatWorkFlow(workflowid, mainInfoList, mainFieldsMap, detailLists, createrId, hrMap.get("lastname"));
            //单据编号
            resultMesage.setDjbh(_ret);
            log.info("结果：" + _ret);
            if (!StringUtils.isEmpty(_ret) && Integer.parseInt(_ret) > 0) {
                resultMesage.setFlag("0");
                resultMesage.setMessage("流程创建成功!");
            } else {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:创建流程失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMesage.setFlag("1");
            resultMesage.setMessage("Interface exception");
            return resultMesage;
        }
        log.info("------------------------采购订单or验收单流程(SAP)流程----------------------------end");
        return resultMesage;
    }

    // 合同评审
    public ResultMesage creatContractReviewWorkFlow(ContractReviewReq contractReviewReq) throws UnsupportedEncodingException {
        /**主表*/
        List<String> mainInfoList = new ArrayList<>();
        Map<String, String> mainFieldsMap = new HashMap<>();
        /**明细表*/
        List<List<Map<String, String>>> detailLists = new ArrayList<>();
        List<Map<String, String>> detail1List = new ArrayList<>();
        /**返回值*/
        ResultMesage resultMesage = new ResultMesage();
        /**申请人*/
        String createrId = "";
        /**判断是否为测试合同*/
        if (!null2String(contractReviewReq.getItem().get(0).getKNTTP()).equals("W")) {
            String workflowid = Prop.getPropValue("wanxiang", "htps.workflowid");
            log.info("------------------------合同评审流程(SAP)----------------------------begin");
            log.info("参数信息：" + contractReviewReq.toString());
            try {
                RecordSet recordSet = new RecordSet();
                if (StringUtils.isEmpty(contractReviewReq.getERNAM())) {
                    resultMesage.setFlag("1");
                    resultMesage.setMessage("错误信息:申请人信息不能为空");
                    return resultMesage;
                }
                String creater = "";
                //String sql=" select hrmresource.id from hrmresource join cus_fielddata on cus_fielddata.SCOPEID='-1' and cus_fielddata.field13 = '"+Util.null2String(rawMaterialCheckReq.getERNAM())+"'";
                recordSet.executeQuery(sql, null2String(contractReviewReq.getERNAM()));
                if (recordSet.next()) {
                    creater = null2String(recordSet.getString("id"));
                }
                if ("".equals(creater)) {
                    resultMesage.setFlag("1");
                    resultMesage.setMessage("错误信息:无法获取创建人ID");
                    return resultMesage;
                }
                Map<String, String> hrMap = CommonUtil.getHRMapById(creater);
                createrId = null2String(hrMap.get("createid"));
                Date now = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                /*------------------------------------------主表字段处理--------------------------------------------------------------*/


                /**部门*/
                mainInfoList.add("bm");
                /**采购凭证类型*/
                mainInfoList.add("cgpzlx");
                /**合同对方单位*/
                mainInfoList.add("htdfdw");
                /**申请人*/
                mainInfoList.add("sqr");
                /**申请日期*/
                mainInfoList.add("sqrq");
                /**备注*/
                mainInfoList.add("bz");
                /**币种*/
                mainInfoList.add("bz1");
                /**合同编号*/
                mainInfoList.add("htbh");
                /**有效起始日期*/
                mainInfoList.add("KDATB");
                /**有效截止日期*/
                mainInfoList.add("KDATE");


                /**采购凭证类型*/
                mainFieldsMap.put("cgpzlx", null2String(contractReviewReq.getBSART()));
                /**合同对方单位*/
                mainFieldsMap.put("htdfdw", null2String(contractReviewReq.getNAME_ORG1()));
                /**申请人*/
                mainFieldsMap.put("sqr", null2String(createrId));
                /**申请日期*/
                mainFieldsMap.put("sqrq", null2String(df.format(now)));
                /**部门*/
                mainFieldsMap.put("bm", null2String(hrMap.get("departmentid")));
                /**备注*/
                mainFieldsMap.put("bz", null2String(contractReviewReq.getVTEXT()));
                /**币种*/
                mainFieldsMap.put("bz1", null2String(contractReviewReq.getWAERS()));
                /**合同编号*/
                mainFieldsMap.put("htbh", null2String(contractReviewReq.getEBELN()));
                /**有效起始日期*/
                mainFieldsMap.put("KDATB", null2String(contractReviewReq.getKDATB()));
                /**有效截止日期*/
                mainFieldsMap.put("KDATE", null2String(contractReviewReq.getKDATE()));

                /*------------------------------------------明细字段处理--------------------------------------------------------------*/

                for (int i = 0; i < contractReviewReq.getItem().size(); i++) {
                    ContractReviewDetailReq contracctReviewDetailReq = contractReviewReq.getItem().get(i);
                    log.info("参数信息：" + contracctReviewDetailReq.toString());
                    Map<String, String> dt1Map = new HashMap<>();
                    String LOEKZ = contracctReviewDetailReq.getLOEKZ();
                    log.info("LOEKZ:" + LOEKZ);
                    try {
                        if (LOEKZ.equals("L")) {
                            continue;
                        }
                    } catch (Exception e) {
                        log.info("筛选运行异常");
                    }
                    /**有效开始日期*/
                    dt1Map.put("KDATB", null2String(contracctReviewDetailReq.getKDATB()));
                    /**有效结束日期*/
                    dt1Map.put("KDATE", null2String(contracctReviewDetailReq.getKDATE()));
                    /**合同编号*/
                    //dt1Map.put("htbh",Util.null2String(contracctReviewDetailReq.getEBELN()));
                    /**物料编号*/
                    dt1Map.put("MATNR", null2String(contracctReviewDetailReq.getMATNR()));
                    /**采购凭证删除标*/
                    dt1Map.put("LOEKZ", null2String(contracctReviewDetailReq.getLOEKZ()));
                    /**物料描述*/
                    dt1Map.put("TXZ01", null2String(contracctReviewDetailReq.getTXZ01()));
                    /**目标数量*/
                    dt1Map.put("KTMNG", null2String(contracctReviewDetailReq.getKTMNG()));
                    /**单价*/
                    dt1Map.put("NETPR", null2String(contracctReviewDetailReq.getNETPR()));
                    /**总价*/
                    dt1Map.put("BRTWR", null2String(contracctReviewDetailReq.getBRTWR()));
                    /**需求人*/
                    dt1Map.put("AFNAM", null2String(contracctReviewDetailReq.getAFNAM()));
                    log.info("参数信息：" + dt1Map.toString());
                    detail1List.add(dt1Map);
                }
                detailLists.add(detail1List);
                String _ret = creatWorkFlow(workflowid, mainInfoList, mainFieldsMap, detailLists, createrId, hrMap.get("lastname"));
                //单据编号
                resultMesage.setDjbh(_ret);
                log.info("结果：" + _ret);
                if (!StringUtils.isEmpty(_ret) && Integer.parseInt(_ret) > 0) {
                    resultMesage.setFlag("0");
                    resultMesage.setMessage("流程创建成功!");
                } else {
                    resultMesage.setFlag("1");
                    resultMesage.setMessage("错误信息:创建流程失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultMesage.setFlag("1");
                resultMesage.setMessage("Interface exception");
                return resultMesage;
            }
            log.info("------------------------合同评审流程(SAP)流程----------------------------end");
        } else {
            String workflowid = Prop.getPropValue("wanxiang", "wwcs.workflowid");
            log.info("------------------------委外测试合同流程----------------------------begin");
            log.info("参数信息：" + contractReviewReq.toString());
            try {
                RecordSet recordSet = new RecordSet();
                if (StringUtils.isEmpty(contractReviewReq.getERNAM())) {
                    resultMesage.setFlag("1");
                    resultMesage.setMessage("错误信息:申请人信息不能为空");
                    return resultMesage;
                }
                String creater = "";
                //String sql=" select hrmresource.id from hrmresource join cus_fielddata on cus_fielddata.SCOPEID='-1' and cus_fielddata.field13 = '"+Util.null2String(rawMaterialCheckReq.getERNAM())+"'";
                recordSet.executeQuery(sql, null2String(contractReviewReq.getERNAM()));
                if (recordSet.next()) {
                    creater = null2String(recordSet.getString("id"));
                }
                if ("".equals(creater)) {
                    resultMesage.setFlag("1");
                    resultMesage.setMessage("错误信息:无法获取创建人ID");
                    return resultMesage;
                }
                Map<String, String> hrMap = CommonUtil.getHRMapById(creater);
                createrId = null2String(hrMap.get("createid"));
                Date now = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                /*------------------------------------------主表字段处理--------------------------------------------------------------*/
                /**申请人*/
                mainInfoList.add("sqr");
                /**申请日期*/
                mainInfoList.add("sqrq");
                /**部门*/
                mainInfoList.add("bm");
                /**合同类型*/
                mainInfoList.add("BSART");
                /**预选测试机构*/
                mainInfoList.add("NAME_ORG1");
                /**合同编码*/
                mainInfoList.add("cgddherp");
                /**币种*/
                mainInfoList.add("bz");

                /**申请人*/
                mainFieldsMap.put("sqr", null2String(createrId));
                /**申请日期*/
                mainFieldsMap.put("sqrq", null2String(df.format(now)));
                /**部门*/
                mainFieldsMap.put("bm", null2String(hrMap.get("departmentid")));
                /**合同类型*/
                mainFieldsMap.put("BSART", null2String(contractReviewReq.getBSART()));
                /**预选测试机构*/
                mainFieldsMap.put("NAME_ORG1", null2String(contractReviewReq.getNAME_ORG1()));
                /**合同编号*/
                mainFieldsMap.put("cgddherp", null2String(contractReviewReq.getEBELN()));
                /**币种*/
                mainFieldsMap.put("bz", null2String(contractReviewReq.getWAERS()));
                /*------------------------------------------明细字段处理--------------------------------------------------------------*/
                for (int i = 0; i < contractReviewReq.getItem().size(); i++) {
                    ContractReviewDetailReq contracctReviewDetailReq = contractReviewReq.getItem().get(i);
                    log.info("参数信息：" + contracctReviewDetailReq.toString());
                    Map<String, String> dt1Map = new HashMap<>();

                    /**有效开始日期*/
                    dt1Map.put("KDATB", null2String(contracctReviewDetailReq.getKDATB()));
                    /**有效结束日期*/
                    dt1Map.put("KDATE", null2String(contracctReviewDetailReq.getKDATE()));
                    /**目标数量*/
                    dt1Map.put("ypsl", null2String(contracctReviewDetailReq.getKTMNG()));
                    /**测试内容*/
                    dt1Map.put("csnr", null2String(contracctReviewDetailReq.getTXZ01()));
                    /**单价(2位)*/
                    dt1Map.put("NETPR", null2String(contracctReviewDetailReq.getNETPR()));
                    /**总价(2位)*/
                    dt1Map.put("BRTWR", null2String(contracctReviewDetailReq.getBRTWR()));
                    log.info("参数信息：" + dt1Map.toString());
                    detail1List.add(dt1Map);
                }
                detailLists.add(detail1List);
                String _ret = creatWorkFlow(workflowid, mainInfoList, mainFieldsMap, detailLists, createrId, hrMap.get("lastname"));
                //单据编号
                resultMesage.setDjbh(_ret);
                log.info("结果：" + _ret);
                if (!StringUtils.isEmpty(_ret) && Integer.parseInt(_ret) > 0) {
                    resultMesage.setFlag("0");
                    resultMesage.setMessage("流程创建成功!");
                } else {
                    resultMesage.setFlag("1");
                    resultMesage.setMessage("错误信息:创建流程失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultMesage.setFlag("1");
                resultMesage.setMessage("Interface exception");
                return resultMesage;
            }
            log.info("------------------------委外测试合同流程----------------------------end");
            }

            return resultMesage;
    }

    //发票入账流程
    public ResultMesage creatInvoiceEntryWorkFlow(InvoiceEntryReq invoiceEntryReq) throws UnsupportedEncodingException {
        String workflowid = Prop.getPropValue("wanxiang", "fprz.workflowid");
        log.info("------------------------发票入账流程(SAP)----------------------------begin");
        log.info("参数信息：" + invoiceEntryReq.toString());
        ResultMesage resultMesage = new ResultMesage();
        try {
            RecordSet recordSet = new RecordSet();
            if (StringUtils.isEmpty(invoiceEntryReq.getUSNAM())) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:申请人信息不能为空");
                return resultMesage;
            }
            String creater = "";
            recordSet.executeQuery(sql, null2String(invoiceEntryReq.getUSNAM()));
            if (recordSet.next()) {
                creater = null2String(recordSet.getString("id"));
            }
            if ("".equals(creater)) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:无法获取创建人ID");
                return resultMesage;
            }
            Map<String, String> hrMap = CommonUtil.getHRMapById(creater);
            String createrId = null2String(hrMap.get("createid"));
            Date now = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            /*------------------------------------------主表字段处理--------------------------------------------------------------*/
            List<String> mainInfoList = new ArrayList<>();

            /**公司代码*/
            mainInfoList.add("BUKRS");
            /**付款代码*/
            mainInfoList.add("ZTERM");
            /**会计凭证的凭证编号*/
            mainInfoList.add("BELNR");
            /**会计年度*/
            mainInfoList.add("GJAHR");
            /**凭证类型*/
            mainInfoList.add("BLART");
            /**凭证中的凭证日期*/
            mainInfoList.add("BLDAT");
            /**凭证中的过账日期*/
            mainInfoList.add("BUDAT");
            /**会计期间*/
            mainInfoList.add("MONAT");
            /**会计凭证输入日期*/
            mainInfoList.add("CPUDT");
            /** 申请人*/
            mainInfoList.add("sqr");
            /** 申请日期*/
            mainInfoList.add("sqrq");
            /** bm*/
            mainInfoList.add("bm");
            /**采购经办员*/
            mainInfoList.add("cgjby");
            /**参考凭证编号*/
            mainInfoList.add("XBLNR");
            /**冲销凭证号*/
            mainInfoList.add("STBLG");
            /**冲销凭证的会计年度*/
            mainInfoList.add("STJAH");
            /**凭证抬头文本*/
            mainInfoList.add("BKTXT");
            /**币种*/
            mainInfoList.add("bzz");
            /**未计划的运费*/
            mainInfoList.add("FRATH");
            /**供应商名称*/
            mainInfoList.add("NAME_ORG1");
            /**供应商编码*/
            mainInfoList.add("LIFNR");
            /**以凭证货币计的税额*/
            mainInfoList.add("WMWST");

            String WMWST = null2String(invoiceEntryReq.getWmwst1());
            int WMWSTLength = WMWST.length() - 1;
            String WMWSTLast = WMWST.substring(WMWSTLength);

            Map<String, String> mainFieldsMap = new HashMap<>();

            /**公司代码*/
            mainFieldsMap.put("BUKRS", null2String(invoiceEntryReq.getBUKRS()));
            /**付款代码*/
            mainFieldsMap.put("ZTERM", null2String(invoiceEntryReq.getZTERM()));
            /**会计凭证的凭证编号*/
            mainFieldsMap.put("BELNR", null2String(invoiceEntryReq.getBELNR()));
            /**会计年度*/
            mainFieldsMap.put("GJAHR", null2String(invoiceEntryReq.getGJAHR()));
            /**凭证类型*/
            mainFieldsMap.put("BLART", null2String(invoiceEntryReq.getBLART()));
            /**凭证中的凭证日期*/
            mainFieldsMap.put("BLDAT", null2String(invoiceEntryReq.getBLDAT()));
            /**凭证中的过账日期*/
            mainFieldsMap.put("BUDAT", null2String(invoiceEntryReq.getBUDAT()));
            /**会计期间*/
            mainFieldsMap.put("MONAT", null2String(invoiceEntryReq.getMONAT()));
            /**会计凭证输入日期*/
            mainFieldsMap.put("CPUDT", null2String(invoiceEntryReq.getCPUDT()));
            /** 申请人*/
            mainFieldsMap.put("sqr", null2String(creater));
            /** 申请部门*/
            mainFieldsMap.put("bm", null2String(hrMap.get("departmentid")));
            /**申请日期*/
            mainFieldsMap.put("sqrq", null2String(invoiceEntryReq.getCPUDT()));
            /**采购经办员*/
            mainFieldsMap.put("cgjby", null2String(creater));
            /**参考凭证编号*/
            mainFieldsMap.put("XBLNR", null2String(invoiceEntryReq.getZBZZD()));
            /**冲销凭证号*/
            mainFieldsMap.put("STBLG", null2String(invoiceEntryReq.getSTBLG()));
            /**冲销凭证的会计年度*/
            mainFieldsMap.put("STJAH", null2String(invoiceEntryReq.getSTJAH()));
            /**凭证抬头文本*/
            mainFieldsMap.put("BKTXT", null2String(invoiceEntryReq.getBKTXT()));
            /**币种*/
            mainFieldsMap.put("bzz", null2String(invoiceEntryReq.getWAERS()));
            /**未计划的运费*/
            mainFieldsMap.put("FRATH", null2String(invoiceEntryReq.getFRATH()));
            /**供应商名称*/
            mainFieldsMap.put("NAME_ORG1", null2String(invoiceEntryReq.getNAME_ORG1()));
            /**供应商编码*/
            mainFieldsMap.put("LIFNR", null2String(invoiceEntryReq.getLIFNR()));
            /**以凭证货币计的税额*/
            if (WMWSTLast.equals("-")) {
                WMWST = WMWSTLast + invoiceEntryReq.getWmwst1().substring(0, WMWSTLength);
                mainFieldsMap.put("WMWST", WMWST);
            }else{
                mainFieldsMap.put("WMWST", WMWST);
            }

            /*------------------------------------------明细字段处理--------------------------------------------------------------*/
            List<List<Map<String, String>>> detailLists = new ArrayList<>();
            List<Map<String, String>> detail1List = new ArrayList<>();
            detailLists.add(detail1List);
            List<Map<String, String>> detail2List = new ArrayList<>();
            for (int i = 0; i < invoiceEntryReq.getItem().size(); i++) {
                InvoiceEntryDetailReq invoiceEntryDetailReq = invoiceEntryReq.getItem().get(i);
                log.info("参数信息：" + invoiceEntryDetailReq.toString());
                Map<String, String> dt1Map = new HashMap<>();
                /**判断凭证货币金额与数量是否为负数*/
                String WRBTR = null2String(invoiceEntryDetailReq.getWRBTR());
                String MENGE = null2String(invoiceEntryDetailReq.getMENGE());
                int WLength = WRBTR.length() - 1;
                int MENGELength = MENGE.length() - 1;
                String WRBTRLast = WRBTR.substring(WLength);
                String MENGELast = MENGE.substring(MENGELength);
                /**凭证货币金额*/
                if (WRBTRLast.equals("-")) {
                    WRBTR = WRBTRLast + invoiceEntryDetailReq.getWRBTR().substring(0, WLength);
                    dt1Map.put("je", WRBTR);
                } else {
                    dt1Map.put("je", WRBTR);
                }
                /**数量*/
                if (MENGELast.equals("-")) {
                    MENGE = MENGELast + invoiceEntryDetailReq.getMENGE().substring(0, MENGELength);
                    dt1Map.put("MENGE", MENGE);
                }else{
                    dt1Map.put("MENGE", MENGE);
                }

                //dt1Map.put("je", null2String(invoiceEntryDetailReq.getWRBTR()));
                /**以凭证货币计的税额*/
                //dt1Map.put("WMWST",Util.null2String(invoiceEntryDetailReq.getWMWST()));
                /**基准日期*/
                dt1Map.put("ZFBDT", null2String(invoiceEntryDetailReq.getZFBDT()));
                /**付款条件代码*/
                dt1Map.put("ZTERM", null2String(invoiceEntryDetailReq.getZTERM()));
                /**采购订单号*/
                dt1Map.put("EBELN", null2String(invoiceEntryDetailReq.getEBELN()));
                /**采购订单行项目号*/
                dt1Map.put("EBELP", null2String(invoiceEntryDetailReq.getEBELP()));
                /**供应商编号*/
                /**物料编码*/
                dt1Map.put("MATNR", null2String(invoiceEntryDetailReq.getMATNR()));
                /**物料描述*/
                dt1Map.put("MAKTX", null2String(invoiceEntryDetailReq.getMAKTX()));
                /**收货数量*/
                dt1Map.put("MENGE1", null2String(invoiceEntryDetailReq.getMENGE1()));
                /**参考*/
                dt1Map.put("XBLNR_MKPF", null2String(invoiceEntryDetailReq.getXBLNR_MKPF()));
                /**订单价格*/
                dt1Map.put("BRTWR", null2String(invoiceEntryDetailReq.getBRTWR()));
                /**采购申请价格*/
                dt1Map.put("GSWRT", null2String(invoiceEntryDetailReq.getGSWRT()));


                detail2List.add(dt1Map);
            }
            detailLists.add(detail2List);
            String _ret = creatWorkFlow(workflowid, mainInfoList, mainFieldsMap, detailLists, createrId, hrMap.get("lastname"));
            log.info("creatWorkFlow"+workflowid+";"+mainInfoList+";"+mainFieldsMap+";"+detailLists+";"+createrId+";"+hrMap.get("lastname"));
            //单据编号
            resultMesage.setDjbh(_ret);
            log.info("结果：" + _ret);
            if (!StringUtils.isEmpty(_ret) && Integer.parseInt(_ret) > 0) {
                resultMesage.setFlag("0");
                resultMesage.setMessage("流程创建成功!");
            } else {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:创建流程失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMesage.setFlag("1");
            resultMesage.setMessage("Interface exception");
            return resultMesage;
        }
        log.info("------------------------发票入账流程(SAP)流程----------------------------end");
        return resultMesage;
    }
    /**Agile主数据*/
    @Override
    public ResultMesage creatAgileWorkFlow(MasterDateReq masterDateReq) throws UnsupportedEncodingException {
        String workflowid = Prop.getPropValue("wanxiang", "wlzsj.agileflowid");
        log.info("------------主数据创建流程(SAP)-----------begin");
        log.info("参数信息：" + masterDateReq.toString());
        ResultMesage resultMesage = new ResultMesage();
        try {
            RecordSet recordSet = new RecordSet();
            if (StringUtils.isEmpty(masterDateReq.getUSNAM())) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息：申请人信息不能为空");
                return resultMesage;
            }
            String creater = "";
            recordSet.executeQuery(sql, null2String(masterDateReq.getUSNAM()));
            if (recordSet.next()) {
                creater = null2String(recordSet.getString("id"));
            }
            if ("".equals(creater)) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息：无法获取创建人ID");
                return resultMesage;
            }
            Map<String, String> hrMap = CommonUtil.getHRMapById(creater);
            String createrId = null2String(hrMap.get("createid"));
            Date now = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            List<String> mainInfoList = new ArrayList<>();
            mainInfoList.add("sqr");
            mainInfoList.add("sqsj");
            mainInfoList.add("dh");
            mainInfoList.add("agileech");
            mainInfoList.add("ReasonForChange");

            Map<String, String> mainFieldsMap = new HashMap<>();
            mainFieldsMap.put("sqr", null2String(creater));
            mainFieldsMap.put("sqsj", null2String(df.format(now)));
            mainFieldsMap.put("agileech", null2String(masterDateReq.getAgileEc()));
            mainFieldsMap.put("ReasonForChange", null2String(masterDateReq.getReasonForChange()));
            List<List<Map<String, String>>> detailLists = new ArrayList<>();
            List<Map<String, String>> detail1List = new ArrayList<>();
            for (int i = 0; i < masterDateReq.getItem().size(); i++) {
                MasterDateDetailReq masterDateReq1 = masterDateReq.getItem().get(i);
                log.info("参数信息：" + masterDateReq1.toString());
                Map<String, String> dt1Map = new HashMap<>();
                String PRCTR=masterDateReq1.getPRCTR();
                log.info("PRCTR:"+PRCTR);
                if (PRCTR.equals("L600100000")){
                    continue;
                }
                String MATNR=null2String(masterDateReq1.getMATNR()).substring(9);
                dt1Map.put("XCHPF", null2String(masterDateReq1.getXCHPF()));
                dt1Map.put("MATKL", null2String(masterDateReq1.getMATKL()));
                dt1Map.put("MATNR", MATNR);
                dt1Map.put("MBRSH", null2String(masterDateReq1.getMBRSH()));
                dt1Map.put("MEINS", null2String(masterDateReq1.getMEINS()));
                dt1Map.put("MHDHB", null2String(masterDateReq1.getMHDHB()));
                dt1Map.put("MHDRZ", null2String(masterDateReq1.getMHDRZ()));
                dt1Map.put("MTART", null2String(masterDateReq1.getMTART()));
                dt1Map.put("PRCTR", null2String(masterDateReq1.getPRCTR()));
                dt1Map.put("WERKS", null2String(masterDateReq1.getWERKS()));
                dt1Map.put("ZCPDJ", null2String(masterDateReq1.getZMLEVEL()));
                dt1Map.put("ZWLPL", null2String(masterDateReq1.getZSORT()));
                dt1Map.put("wlzt", null2String(masterDateReq1.getZSTATUS()));
                dt1Map.put("BISMT", null2String(masterDateReq1.getBISMT()));
                dt1Map.put("MAKTX", null2String(masterDateReq1.getMAKTX()));
                dt1Map.put("IPRKZ", null2String(masterDateReq1.getIPRKZ()));
                dt1Map.put("List21", null2String(masterDateReq1.getList21()));
                detail1List.add(dt1Map);
                log.info(dt1Map.toString());
            }
            detailLists.add(detail1List);
            log.info("creatWorkFlow"+workflowid+";"+mainInfoList+";"+mainFieldsMap+";"+detailLists+";"+createrId+";"+hrMap.get("createid")+";"+hrMap.get("lastname"));

            String _ret = creatWorkFlowToNext(workflowid, mainInfoList, mainFieldsMap, detailLists, createrId, hrMap.get("lastname"));
            //单据编号
            resultMesage.setDjbh(_ret);
            log.info("结果：" + _ret);
            if (!StringUtils.isEmpty(_ret) && Integer.parseInt(_ret) > 0) {
                resultMesage.setFlag("0");
                resultMesage.setMessage("流程创建成功!"+masterDateReq.toString());
            } else {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:创建流程失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMesage.setFlag("1");
            resultMesage.setMessage("Interface exception");
            return resultMesage;
        }
        log.info("------------------------主数据创建(SAP)流程----------------------------end");
        return resultMesage;
    }
    /**项目类领料预留单*/
    @Override
    public ResultMesage creatProjectMaterialReserveWorkFlow(ProjectMaterialproofReq projectMaterialproofReq) throws UnsupportedEncodingException {
        /**主表*/
        List<String> mainInfoList = new ArrayList<>();
        Map<String, String> mainFieldsMap = new HashMap<>();
        /**明细表*/
        List<List<Map<String, String>>> detailLists = new ArrayList<>();
        List<Map<String, String>> detail1List = new ArrayList<>();
        /**返回值*/
        ResultMesage resultMesage = new ResultMesage();
        /**申请人*/
        String createrId = "";
        /**流程ID*/
        String workflowid = Prop.getPropValue("wanxiang", "xmyld.workflowid");
        log.info("------------------------项目类领料预留单----------------------------begin");
        log.info("参数信息：" + projectMaterialproofReq.toString());
        log.info("流程ID："+workflowid);
        try {
            RecordSet recordSet = new RecordSet();
            if (StringUtils.isEmpty(projectMaterialproofReq.getUSNAM())) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:申请人信息不能为空");
                return resultMesage;
            }
            String creater = "";
            recordSet.executeQuery(sql, null2String(projectMaterialproofReq.getUSNAM()));
            if (recordSet.next()) {
                creater = null2String(recordSet.getString("id"));
            }
            if ("".equals(creater)) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:无法获取创建人ID");
                return resultMesage;
            }
            Map<String, String> hrMap = CommonUtil.getHRMapById(creater);
            createrId = null2String(hrMap.get("createid"));
            Date now = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            /*------------------------------------------主表字段处理--------------------------------------------------------------*/
            /**创建人*/
            mainInfoList.add("USNAM");
            /**创建人部门*/
            mainInfoList.add("cjrbm");
            /**创建时间*/
            mainInfoList.add("cjsj");
            /**预留单号*/
            mainInfoList.add("RSNUM");
            /**预留单创建日期*/
            mainInfoList.add("RSDAT");
            /**移动类型*/
            mainInfoList.add("BWART");
            /**成本中心*/
            mainInfoList.add("KOSTL");
            /**成本中心名称*/
            mainInfoList.add("KTEXT");
            /** 项目号*/
            mainInfoList.add("AUFNR");
            /** 项目名称*/
            mainInfoList.add("KTEXT2");
            /** 专项项目*/
            mainInfoList.add("ZSPECPRO");


            /**创建人*/
            mainFieldsMap.put("USNAM", null2String(createrId));
            /**创建时间*/
            mainFieldsMap.put("cjsj", null2String(df.format(now)));
            /**创建人部门*/
            mainFieldsMap.put("cjrbm", null2String(hrMap.get("departmentid")));
            /**预留单号*/
            mainFieldsMap.put("RSNUM", null2String(projectMaterialproofReq.getRSNUM()));
            /**预留单创建日期*/
            mainFieldsMap.put("RSDAT", null2String(projectMaterialproofReq.getRSDAT()));
            /**移动类型*/
            mainFieldsMap.put("BWART", null2String(projectMaterialproofReq.getBWART()));
            /**成本中心*/
            mainFieldsMap.put("KOSTL", null2String(projectMaterialproofReq.getKOSTL()));
            /**成本中心名称*/
            mainFieldsMap.put("KTEXT", null2String(projectMaterialproofReq.getKTEXT()));
            /**项目号*/
            mainFieldsMap.put("AUFNR", null2String(projectMaterialproofReq.getAUFNR()));
            /**项目名称*/
            mainFieldsMap.put("KTEXT2", null2String(projectMaterialproofReq.getKTEXT2()));
            /**专项项目*/
            mainFieldsMap.put("ZSPECPRO", null2String(projectMaterialproofReq.getZSPECPRO()));

            /*------------------------------------------明细字段处理--------------------------------------------------------------*/
            for (int i = 0; i < projectMaterialproofReq.getItems().size(); i++) {
                ProjectMaterialproofDetailReq projectMaterialproofDetailReq = projectMaterialproofReq.getItems().get(i);
                log.info("参数信息：" + projectMaterialproofDetailReq.toString());
                Map<String, String> dt1Map = new HashMap<>();
                /**行*/
                dt1Map.put("RSPOS", null2String(projectMaterialproofDetailReq.getRSPOS()));
                /**物料*/
                dt1Map.put("MATNR", null2String(projectMaterialproofDetailReq.getMATNR()));
                /**物料描述*/
                dt1Map.put("MAKTX", null2String(projectMaterialproofDetailReq.getMAKTX()));
                /**领用数量*/
                dt1Map.put("BDMNG", null2String(projectMaterialproofDetailReq.getBDMNG()));
                /**计量单位*/
                dt1Map.put("MEINS", null2String(projectMaterialproofDetailReq.getMEINS()));
                /**标准成本单价*/
                dt1Map.put("STDPRS", null2String(projectMaterialproofDetailReq.getSTDPRS()));
                /**标准成本金额*/
                dt1Map.put("STDAMT", null2String(projectMaterialproofDetailReq.getSTDAMT()));
                /**工厂*/
                dt1Map.put("WERKS", null2String(projectMaterialproofDetailReq.getWERKS()));
                /**存储地点*/
                dt1Map.put("LGORT", null2String(projectMaterialproofDetailReq.getLGORT()));
                /**删除标识*/
                dt1Map.put("XLOEK", null2String(projectMaterialproofDetailReq.getXLOEK()));
                /**总账科目*/
                dt1Map.put("SAKNR", null2String(projectMaterialproofDetailReq.getSAKNR()));
                /**总账科目名称*/
                dt1Map.put("TXT50", null2String(projectMaterialproofDetailReq.getTXT50()));
                log.info("参数信息：" + dt1Map.toString());
                detail1List.add(dt1Map);
            }
            detailLists.add(detail1List);
            String _ret = creatWorkFlow(workflowid, mainInfoList, mainFieldsMap, detailLists, createrId, hrMap.get("lastname"));
            //单据编号
            resultMesage.setDjbh(_ret);
            log.info("结果：" + _ret);
            if (!StringUtils.isEmpty(_ret) && Integer.parseInt(_ret) > 0) {
                resultMesage.setFlag("0");
                resultMesage.setMessage("流程创建成功!");
            } else {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:创建流程失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultMesage.setFlag("1");
            resultMesage.setMessage("Interface exception");
            return resultMesage;
        }
        log.info("------------------------项目类领料预留单----------------------------end");
        return resultMesage;
    }

    @Override
    public ResultMesage creatSalesInvoiceMode(SalesInvoiceReq salesInvoicereq) throws UnsupportedEncodingException {

        /**返回值*/
        ResultMesage resultMesage = new ResultMesage();
        log.info("------------------销售发货单信息接收开始--------------------");
        for (int i = 0; i < salesInvoicereq.getItems().size(); i++) {
            SalesInvoiceDetailReq salesInvoiceDetailReq = salesInvoicereq.getItems().get(i);
            Map<String, Object> Mapdetail = new HashMap<>();
            log.info("参数信息：" + salesInvoiceDetailReq.toString());
            /**交货单*/
            Mapdetail.put("VBELN", null2String(salesInvoiceDetailReq.getVBELN()));
            /**交货行*/
            Mapdetail.put("POSNR", null2String(salesInvoiceDetailReq.getPOSNR()));
            /**交货单——行号*/
            Mapdetail.put("jhdhang",null2String(salesInvoiceDetailReq.getVBELN()) + "_" + null2String(salesInvoiceDetailReq.getPOSNR()));
            /**物料号*/
            Mapdetail.put("MATNR", null2String(salesInvoiceDetailReq.getMATNR()));
            /**物料名称*/
            Mapdetail.put("ARKTX", null2String(salesInvoiceDetailReq.getARKTX()));
            /**交货数量*/
            Mapdetail.put("LFIMG", null2String(salesInvoiceDetailReq.getLFIMG()));
            /**实际交货数量*/
            Mapdetail.put("LGMNG", null2String(salesInvoiceDetailReq.getLGMNG()));
            /**计量单位*/
            Mapdetail.put("MEINS", null2String(salesInvoiceDetailReq.getMEINS()));
            /**创建人*/
            Mapdetail.put("ERNAM", null2String(salesInvoiceDetailReq.getERNAM()));
            /**创建日期*/
            Mapdetail.put("ERDAT", null2String(salesInvoiceDetailReq.getERDAT()));
            /**工厂*/
            Mapdetail.put("WERKS", null2String(salesInvoiceDetailReq.getWERKS()));
            /**存储地点*/
            Mapdetail.put("LGORT", null2String(salesInvoiceDetailReq.getLGORT()));
            /**参考凭证*/
            Mapdetail.put("VGBEL", null2String(salesInvoiceDetailReq.getVGBEL()));
            /**参考行*/
            Mapdetail.put("VGPOS", null2String(salesInvoiceDetailReq.getVGPOS()));
            /**收货方*/
            Mapdetail.put("KUNNR", null2String(salesInvoiceDetailReq.getKUNNR()));
            /**送达方*/
            Mapdetail.put("KUNAG", null2String(salesInvoiceDetailReq.getKUNAG()));
            /**物料凭证*/
            Mapdetail.put("MBLNR", null2String(salesInvoiceDetailReq.getMBLNR()));
            /**客户物料*/
            Mapdetail.put("KDMAT", null2String(salesInvoiceDetailReq.getKDMAT()));
            /**过账状态*/
            Mapdetail.put("WBSTA", null2String(salesInvoiceDetailReq.getWBSTA()));
            /**输入日期*/
            Mapdetail.put("CPUDT", null2String(salesInvoiceDetailReq.getCPUDT()));
            /**输入时间*/
            Mapdetail.put("CPUTM", null2String(salesInvoiceDetailReq.getCPUTM()));
            /**过账日期*/
            Mapdetail.put("BUDAT", null2String(salesInvoiceDetailReq.getBUDAT()));
            /**用户名*/
            Mapdetail.put("USNAM", null2String(salesInvoiceDetailReq.getUSNAM()));
            /**第一次剩余未出门*/
            Mapdetail.put("sywcm",null2String(salesInvoiceDetailReq.getLFIMG()));
            Mapdetail.put("ycmsl",0);

            RecordSet recordSet = new RecordSet();
            String sql="select * from uf_xsfhd where VBELN ='" + null2String(salesInvoiceDetailReq.getVBELN()) + "' and POSNR = '" + null2String(salesInvoiceDetailReq.getPOSNR()) + "' ";
            recordSet.execute(sql);
            if(recordSet.next()){
                Mapdetail.put("sywcm",Util.null2String(recordSet.getString("sywcm")));
                Mapdetail.put("ycmsl",Util.null2String(recordSet.getString("ycmsl")));
                Mapdetail.put("id",Util.null2String(recordSet.getString("id")));
                log.info(Util.null2String(recordSet.getString("id")));
            }
            log.info(Mapdetail.toString());
            ModeDataUtil.SaveModeDataInfo(Mapdetail, "248", "1");
        }

        resultMesage.setFlag("0");
        resultMesage.setMessage("信息接收成功");
        resultMesage.setDjbh("");
        log.info("------------------销售发货单信息接收成功--------------------");
        return resultMesage;


    }

    @Override
    public ResultMesage creatProjectMaterialWorkFlow(ProjectMaterialReq projectMaterialReq) throws UnsupportedEncodingException {
        /**主表*/
        List<String> mainInfoList = new ArrayList<>();
        Map<String, String> mainFieldsMap = new HashMap<>();
        /**明细表*/
        List<List<Map<String, String>>> detailLists = new ArrayList<>();
        List<Map<String, String>> detail1List = new ArrayList<>();
        /**返回值*/
        ResultMesage resultMesage = new ResultMesage();
        /**申请人*/
        String createrId = "";
        /**流程ID*/
        String workflowid = Prop.getPropValue("wanxiang", "xmpz.workflowid");
        log.info("------------------------项目领料凭证----------------------------begin");
        log.info("参数信息：" + projectMaterialReq.toString());
        try {
            RecordSet recordSet = new RecordSet();
            if (StringUtils.isEmpty(projectMaterialReq.getUSNAM())) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:申请人信息不能为空");
                return resultMesage;
            }
            String creater = "";
            recordSet.executeQuery(sql, null2String(projectMaterialReq.getUSNAM()));
            if (recordSet.next()) {
                creater = null2String(recordSet.getString("id"));
            }
            if ("".equals(creater)) {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:无法获取创建人ID");
                return resultMesage;
            }
            Map<String, String> hrMap = CommonUtil.getHRMapById(creater);
            createrId = null2String(hrMap.get("createid"));
            Date now = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            /*------------------------------------------主表字段处理--------------------------------------------------------------*/
            /**SAP物料凭证号*/
            mainInfoList.add("MBLNR");
            /**创建人*/
            mainInfoList.add("USNAM");
            /**创建人部门*/
            mainInfoList.add("cjrbm");
            /**过账日期*/
            mainInfoList.add("BUDAT");
            /**创建日期*/
            mainInfoList.add("CPUDT");
            /**移动类型*/
            mainInfoList.add("BWART");




            /**SAP物料凭证号*/
            mainFieldsMap.put("MBLNR", null2String(projectMaterialReq.getMBLNR()));
            /**创建人*/
            mainFieldsMap.put("USNAM", null2String(createrId));
            /**部门*/
            mainFieldsMap.put("cjrbm", null2String(hrMap.get("departmentid")));
            /**过账日期*/
            mainFieldsMap.put("BUDAT", null2String(projectMaterialReq.getBUDAT()));
            /**创建日期*/
            mainFieldsMap.put("CPUDT", null2String(projectMaterialReq.getCPUDT()));
            /**移动类型*/
            mainFieldsMap.put("BWART", null2String(projectMaterialReq.getBWART()));


            /*------------------------------------------明细字段处理--------------------------------------------------------------*/
            for (int i = 0; i < projectMaterialReq.getItems().size(); i++) {
                ProjectMaterialDetailReq projectMaterialDetailReq = projectMaterialReq.getItems().get(i);
                log.info("参数信息：" + projectMaterialDetailReq.toString());
                RecordSet recordSetdetail = new RecordSet();
                recordSetdetail.executeQuery(sql, null2String(projectMaterialDetailReq.getUSNAM()));
                String user="";
                if (recordSetdetail.next()) {
                    user = null2String(recordSetdetail.getString("id"));
                    log.info("userid:"+user);
                }
                Map<String, String> hrMapdetail = CommonUtil.getHRMapById(user);
                /**预留人*/
                String userid= null2String(hrMapdetail.get("createid"));
                Map<String, String> dt1Map = new HashMap<>();
                /**行*/
                dt1Map.put("ZEILE", null2String(projectMaterialDetailReq.getZEILE()));
                /**项目号*/
                dt1Map.put("AUFNR", null2String(projectMaterialDetailReq.getAUFNR()));
                /**项目名称*/
                dt1Map.put("KTEXT", null2String(projectMaterialDetailReq.getKTEXT()));
                /**物料*/
                dt1Map.put("MATNR", null2String(projectMaterialDetailReq.getMATNR()));
                /**物料描述*/
                dt1Map.put("MAKTX", null2String(projectMaterialDetailReq.getMAKTX()));
                /**领用数量*/
                dt1Map.put("MENGE", null2String(projectMaterialDetailReq.getMENGE()));
                /**计量单位*/
                dt1Map.put("MEINS", null2String(projectMaterialDetailReq.getMEINS()));
                /**金额*/
                dt1Map.put("DMBTR", null2String(projectMaterialDetailReq.getDMBTR()));
                /**预留单号*/
                dt1Map.put("RSNUM", null2String(projectMaterialDetailReq.getRSNUM()));
                /**预留行*/
                dt1Map.put("RSPOS", null2String(projectMaterialDetailReq.getRSPOS()));
                /**预留人*/
                dt1Map.put("USNAM", null2String(userid));
                /**预留人名称*/
                dt1Map.put("NAME", null2String(projectMaterialDetailReq.getNAME()));
                log.info("参数信息：" + dt1Map.toString());
                detail1List.add(dt1Map);
            }
            detailLists.add(detail1List);
            String _ret = creatWorkFlowToNext(workflowid, mainInfoList, mainFieldsMap, detailLists, createrId, hrMap.get("lastname"));
            //单据编号
            resultMesage.setDjbh(_ret);
            log.info("结果：" + _ret);
            if (!StringUtils.isEmpty(_ret) && Integer.parseInt(_ret) > 0) {
                resultMesage.setFlag("0");
                resultMesage.setMessage("流程创建成功!");
            } else {
                resultMesage.setFlag("1");
                resultMesage.setMessage("错误信息:创建流程失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultMesage.setFlag("1");
            resultMesage.setMessage("Interface exception");
            return resultMesage;
        }
        log.info("------------------------项目领料凭证----------------------------end");
        return resultMesage;
    }
    public String creatWorkFlow(String workflowid, List<String> mainInfoList, Map<String, String> mainFieldsMap, List<List<Map<String, String>>> detailLists, String createrId, String createrName) throws UnsupportedEncodingException {

        RecordSet recordSet = new RecordSet();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String sql1 = "select * from workflow_base where  id='" + workflowid + "'";
        log.info("执行sql1-->" + sql1);
        recordSet.execute(sql1);
        recordSet.next();
        String createWorkFlowName = null2String(recordSet.getString("workflowname"));
        log.info("workflowname"+createWorkFlowName+"createrName"+createrName);
        //String createRequestName = createWorkFlowName + "-" + hrMap.get("lastname") + "-" + df.format(now);
        String createRequestName = createWorkFlowName + "-" + createrName + "-" + df.format(now);
        /*------------------------------------------创建流程--------------------------------------------------------------*/
        log.info("------------------------------------------创建流程--------------------------------------------------------------");
        WorkflowRequestInfo workflowRequestInfo = new WorkflowRequestInfo();
        workflowRequestInfo.setRequestName(createRequestName);//请求标题
        workflowRequestInfo.setRequestLevel("0");//请求重要级别
        workflowRequestInfo.setCreatorId(createrId);
        workflowRequestInfo.setIsnextflow("0");
        WorkflowBaseInfo workflowBaseInfo = new WorkflowBaseInfo();
        /**流程ID*/
        workflowBaseInfo.setWorkflowId(workflowid);
        /**流程名称*/
        workflowBaseInfo.setWorkflowName(createWorkFlowName);
        /**工作流信息*/
        workflowRequestInfo.setWorkflowBaseInfo(workflowBaseInfo);
        WorkflowMainTableInfo workflowMainTableInfo = new WorkflowMainTableInfo();
        WorkflowRequestTableRecord[] workflowRequestTableRecord = new WorkflowRequestTableRecord[1];
        WorkflowRequestTableField[] WorkflowRequestTableField = new WorkflowRequestTableField[mainInfoList.size()];
        for (int i = 0; i < mainInfoList.size(); i++) {
            WorkflowRequestTableField[i] = new WorkflowRequestTableField();
            String fieldName = mainInfoList.get(i);
            WorkflowRequestTableField[i].setFieldName(fieldName);//字段名
            WorkflowRequestTableField[i].setFieldValue(mainFieldsMap.get(fieldName));//字段值
            WorkflowRequestTableField[i].setView(true);
            WorkflowRequestTableField[i].setEdit(true);
        }
        workflowRequestTableRecord[0] = new WorkflowRequestTableRecord();
        workflowRequestTableRecord[0].setWorkflowRequestTableFields(WorkflowRequestTableField);
        workflowMainTableInfo.setRequestRecords(workflowRequestTableRecord);
        workflowRequestInfo.setWorkflowMainTableInfo(workflowMainTableInfo);
        if (detailLists != null) {
            WorkflowDetailTableInfo[] workflowDetailTableInfo = new WorkflowDetailTableInfo[detailLists.size()];//根据传入数据生成N张明细表
            for (int i = 0; i < detailLists.size(); i++) {
                List<Map<String, String>> tempList = detailLists.get(i);//每个明细表中的记录
                WorkflowRequestTableRecord[] workflowDetailTableRecord = new WorkflowRequestTableRecord[tempList.size()];//生成N行数据（N条记录）
                for (int j = 0; j < tempList.size(); j++) {
                    Map<String, String> temp = tempList.get(j);
                    WorkflowRequestTableField[] WorkflowDetailTableField = new WorkflowRequestTableField[temp.size()];//生成N个字段
                    int n = 0;
                    for (Map.Entry<String, String> entrySet : temp.entrySet()) {
                        //遍历map，给每个字段赋予字段名及值，设置显示属性
                        WorkflowDetailTableField[n] = new WorkflowRequestTableField();
                        WorkflowDetailTableField[n].setFieldName(entrySet.getKey());
                        WorkflowDetailTableField[n].setFieldValue(entrySet.getValue());
                        WorkflowDetailTableField[n].setView(true);
                        WorkflowDetailTableField[n].setEdit(true);
                        ++n;
                    }
                    workflowDetailTableRecord[j] = new WorkflowRequestTableRecord();//每行明细数据
                    workflowDetailTableRecord[j].setWorkflowRequestTableFields(WorkflowDetailTableField);//设置每行记录的字段
                }
                workflowDetailTableInfo[i] = new WorkflowDetailTableInfo();//每张明细表
                workflowDetailTableInfo[i].setWorkflowRequestTableRecords(workflowDetailTableRecord);//设置每张明细表的记录
            }
            workflowRequestInfo.setWorkflowDetailTableInfos(workflowDetailTableInfo);//设置所有明细表
        }

        WorkflowService workflowService = new WorkflowServiceImpl();
        String _ret = workflowService.doCreateWorkflowRequest(workflowRequestInfo, Integer.parseInt(createrId));
        return _ret;
    }

    public String creatWorkFlowToNext(String workflowid, List<String> mainInfoList, Map<String, String> mainFieldsMap, List<List<Map<String, String>>> detailLists, String createrId, String createrName) throws UnsupportedEncodingException {

        RecordSet recordSet = new RecordSet();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String sql1 = "select * from workflow_base where  id='" + workflowid + "'";
        log.info("执行sql1-->" + sql1);
        recordSet.execute(sql1);
        recordSet.next();
        String createWorkFlowName = null2String(recordSet.getString("workflowname"));
        log.info("workflowname"+createWorkFlowName+"createrName"+createrName);
        //String createRequestName = createWorkFlowName + "-" + hrMap.get("lastname") + "-" + df.format(now);
        String createRequestName = createWorkFlowName + "-" + createrName + "-" + df.format(now);
        /*------------------------------------------创建流程--------------------------------------------------------------*/
        log.info("------------------------------------------创建流程--------------------------------------------------------------");
        WorkflowRequestInfo workflowRequestInfo = new WorkflowRequestInfo();
        workflowRequestInfo.setRequestName(createRequestName);//请求标题
        workflowRequestInfo.setRequestLevel("0");//请求重要级别
        workflowRequestInfo.setCreatorId(createrId);
        workflowRequestInfo.setIsnextflow("1");
        WorkflowBaseInfo workflowBaseInfo = new WorkflowBaseInfo();
        /**流程ID*/
        workflowBaseInfo.setWorkflowId(workflowid);
        /**流程名称*/
        workflowBaseInfo.setWorkflowName(createWorkFlowName);
        /**工作流信息*/
        workflowRequestInfo.setWorkflowBaseInfo(workflowBaseInfo);
        WorkflowMainTableInfo workflowMainTableInfo = new WorkflowMainTableInfo();
        WorkflowRequestTableRecord[] workflowRequestTableRecord = new WorkflowRequestTableRecord[1];
        WorkflowRequestTableField[] WorkflowRequestTableField = new WorkflowRequestTableField[mainInfoList.size()];
        for (int i = 0; i < mainInfoList.size(); i++) {
            WorkflowRequestTableField[i] = new WorkflowRequestTableField();
            String fieldName = mainInfoList.get(i);
            WorkflowRequestTableField[i].setFieldName(fieldName);//字段名
            WorkflowRequestTableField[i].setFieldValue(mainFieldsMap.get(fieldName));//字段值
            WorkflowRequestTableField[i].setView(true);
            WorkflowRequestTableField[i].setEdit(true);
        }
        workflowRequestTableRecord[0] = new WorkflowRequestTableRecord();
        workflowRequestTableRecord[0].setWorkflowRequestTableFields(WorkflowRequestTableField);
        workflowMainTableInfo.setRequestRecords(workflowRequestTableRecord);
        workflowRequestInfo.setWorkflowMainTableInfo(workflowMainTableInfo);
        if (detailLists != null) {
            WorkflowDetailTableInfo[] workflowDetailTableInfo = new WorkflowDetailTableInfo[detailLists.size()];//根据传入数据生成N张明细表
            for (int i = 0; i < detailLists.size(); i++) {
                List<Map<String, String>> tempList = detailLists.get(i);//每个明细表中的记录
                WorkflowRequestTableRecord[] workflowDetailTableRecord = new WorkflowRequestTableRecord[tempList.size()];//生成N行数据（N条记录）
                for (int j = 0; j < tempList.size(); j++) {
                    Map<String, String> temp = tempList.get(j);
                    WorkflowRequestTableField[] WorkflowDetailTableField = new WorkflowRequestTableField[temp.size()];//生成N个字段
                    int n = 0;
                    for (Map.Entry<String, String> entrySet : temp.entrySet()) {
                        //遍历map，给每个字段赋予字段名及值，设置显示属性
                        WorkflowDetailTableField[n] = new WorkflowRequestTableField();
                        WorkflowDetailTableField[n].setFieldName(entrySet.getKey());
                        WorkflowDetailTableField[n].setFieldValue(entrySet.getValue());
                        WorkflowDetailTableField[n].setView(true);
                        WorkflowDetailTableField[n].setEdit(true);
                        ++n;
                    }
                    workflowDetailTableRecord[j] = new WorkflowRequestTableRecord();//每行明细数据
                    workflowDetailTableRecord[j].setWorkflowRequestTableFields(WorkflowDetailTableField);//设置每行记录的字段
                }
                workflowDetailTableInfo[i] = new WorkflowDetailTableInfo();//每张明细表
                workflowDetailTableInfo[i].setWorkflowRequestTableRecords(workflowDetailTableRecord);//设置每张明细表的记录
            }
            workflowRequestInfo.setWorkflowDetailTableInfos(workflowDetailTableInfo);//设置所有明细表
        }

        WorkflowService workflowService = new WorkflowServiceImpl();
        String _ret = workflowService.doCreateWorkflowRequest(workflowRequestInfo, Integer.parseInt(createrId));
        return _ret;
    }
}

