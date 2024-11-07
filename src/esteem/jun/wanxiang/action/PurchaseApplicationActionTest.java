package esteem.jun.wanxiang.action;

import esteem.jun.common.util.WebServiceUtil;
import org.junit.jupiter.api.Test;
import weaver.conn.RecordSet;
import weaver.file.Prop;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author:
 * @Date:
 * @Description:
 */
class PurchaseApplicationActionTest {

    @Test
    void execute() {
        long startTime = System.currentTimeMillis();
        String sapurl = "/XISOAPAdapter/MessageServlet?senderParty=&senderService=BC_OA&receiverParty=&receiverService=&interface=SI_OA_Purchase_OUT&interfaceNamespace=urn:a123systems.com:Purchase";
        String urn = "ZMMFU016";
        try {
            StringBuffer contentXml = new StringBuffer();
            contentXml.append("<IS_HEAD>");
            /**采购申请类型*/
            contentXml.append("<EQ_BSART>" + "ZN1" + "</EQ_BSART>");
            /**历史单据号*/
            contentXml.append("<EQ_BANFN>" + "</EQ_BANFN>");
            contentXml.append("</IS_HEAD>");
            contentXml.append("<LT_ITEM>");
            contentXml.append("<item>");
            /**采购申请行项目号*/
            contentXml.append("<BNFPO>" + 1 + "</BNFPO>");
            /**科目分配类别*/
            contentXml.append("<KNTTP>" + "ZN1" + "</KNTTP>");
            /**项目类别*/
            contentXml.append("<PSTYP>"+"</PSTYP>");
            /**物料编码*/
            contentXml.append("<MATNR>" + "305006930" + "</MATNR>");
            /**物料描述*/
            contentXml.append("<TXZ01>" + "111" + "</TXZ01>");
            /**申请数量*/
            contentXml.append("<MENGE>" + "111" + "</MENGE>");
            /**单位*/
            contentXml.append("<MEINS>" + "KG" + "</MEINS>");
            /**交货日期*/
            contentXml.append("<LFDAT>" + "2024-03-26" + "</LFDAT>");
            /**物料组*/
            contentXml.append("<MATKL>" + "111" + "</MATKL>");
            /**工厂*/
            contentXml.append("<WERKS>" + "6000" + "</WERKS>");
            /**采购组*/
            contentXml.append("<EKGRP>" + "C01" + "</EKGRP>");
            /**申请人*/
            contentXml.append("<AFNAM>" + "LONGZD" + "</AFNAM>");
            /**采购申请中的价格*/
            contentXml.append("<PREIS>" + "111" + "</PREIS>");
            /**价格单位*/
            contentXml.append("<PEINH>" + "CNY" + "</PEINH>");
            /**删除标识*/
            contentXml.append("<LOEKZ>" + "111" + "</LOEKZ>");
            /**批准标识*/
            contentXml.append("<FRGKZ>" + "111" + "</FRGKZ>");
            /**最低订单单价*/
            contentXml.append("<ZLOWESTPR>" + "111" + "</ZLOWESTPR>");
            /**最低订单单价日期*/
            contentXml.append("<ZLOWESTDT>" + "2024-03-26" + "</ZLOWESTDT>");
            /**最低订单单价*/
            contentXml.append("<ZLASTPR>" + "111" + "</ZLASTPR>");
            /**最低订单单价日期*/
            contentXml.append("<ZLASTDT>" + "2024-03-26" + "</ZLASTDT>");
            /**总账科目*/
            contentXml.append("<SAKTO>"  + "</SAKTO>");
            /**成本中心*/
            contentXml.append("<KOSTL>"  + "</KOSTL>");
            /**成本控制范围*/
            contentXml.append("<KOKRS>" + "</KOKRS>");
            /**职能范围*/
            contentXml.append("<SKBER>"  + "</SKBER>");
            /**订单*/
            contentXml.append("<AUFNR>"  + "</AUFNR>");
            /**创建日期*/
            contentXml.append("<BADAT>"  + "</BADAT>");
            /**批准日期*/
            contentXml.append("<FRGDT>" + "</FRGDT>");
            /**品牌*/
            contentXml.append("<ZBRAND>"  + "</ZBRAND>");
            /**所属预算*/
            contentXml.append("<ZBUDGET>"  + "</ZBUDGET>");
            /**是否已签合同*/
            contentXml.append("<ZCONTRACT>"  + "</ZCONTRACT>");
            /**申请理由*/
            contentXml.append("<ZREASON>"  + "</ZREASON>");
            /**使用产线*/
            contentXml.append("<ZSHYCHX>"  + "</ZSHYCHX>");
            /**具体使用设备*/
            contentXml.append("<ZSHYSHB>"  + "</ZSHYSHB>");

            contentXml.append("</item>");
            contentXml.append("</LT_ITEM>");
            String soapXml = WebServiceUtil.getSoapXmlTitle(contentXml.toString(), urn);
            System.out.println("soapXml-->" + soapXml);
            String resultXml = WebServiceUtil.callSapWs(sapurl, soapXml);
            System.out.println("resultXml-->" + resultXml);
        } catch (Exception e) {

        }
    }
}