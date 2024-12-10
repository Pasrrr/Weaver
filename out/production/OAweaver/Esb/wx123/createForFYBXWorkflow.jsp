<%@ page language="java" contentType="application/json; charset=UTF-8"%>
<%@ page import="weaver.conn.RecordSet"%>
<%@ page import="weaver.general.BaseBean" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.IOException" %>

<%@ page import="com.alibaba.fastjson.JSONObject"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.alibaba.fastjson.JSONArray"%>
<%@ page import="weaver.workflow.webservices.*"%><%@ page import="java.nio.charset.StandardCharsets"%><%@ page import="java.io.InputStreamReader"%><%@ page import="java.util.*"%><%@ page import="org.apache.commons.lang.StringUtils"%>

<%!
	public static String fetchPostByTextPlain(BufferedReader reader) {
        try {
            char[] buf = new char[512];
            int len = 0;
            StringBuffer contentBuffer = new StringBuffer();
            while ((len = reader.read(buf)) != -1) {
                contentBuffer.append(buf, 0, len);
            }
            return contentBuffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
	
	public static Map getUserInfo(String uid) {
		Map map = new HashMap();
        String sql = "select * from hrmresource where workcode='"+uid+"'";
        RecordSet rs = new RecordSet();
        rs.execute(sql);
        while (rs.next()) {
			map.put("id",rs.getString("id"));
			map.put("subcompanyid1",rs.getString("subcompanyid1"));
			map.put("departmentid",rs.getString("departmentid"));
			map.put("lastname",rs.getString("lastname"));
         return map;
       }
       return null;
    }

%>
<%--  触发费用报销流程 --%>
<%


		//String jsonstr = Util.null2String(fetchPostByTextPlain(request.getReader()));
        String jsonstr = Util.null2String(fetchPostByTextPlain(new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))));

		JSONObject result = new JSONObject();
		new BaseBean().writeLog("参数:"+jsonstr);
//		if(!"".equals(jsonstr)){
			String errMessage = "200";
            try {
                JSONObject param = JSONObject.parseObject(jsonstr);

                //获取当天日期
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format(new Date());

                //表单数据
                String formData = Util.null2String(param.get("formData"));
                if (!formData.equals("")) {
                     //主表字段
                    WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[27]; //字段信息
                    int i = 0;
                    //流程示例id
                    String processInstanceId = Util.null2String(param.get("processInstanceId"));
                    wrti[i]  = new WorkflowRequestTableField();
                    wrti[i].setFieldName("processInstanceld");
                    wrti[i].setFieldValue(processInstanceId);
                    wrti[i].setView(true);
                    wrti[i].setEdit(true);
                    i++;

                    String taskId = Util.null2String(param.get("taskId"));
                    wrti[i]  = new WorkflowRequestTableField();
                    wrti[i].setFieldName("processTaskld");
                    wrti[i].setFieldValue(taskId);
                    wrti[i].setView(true);
                    wrti[i].setEdit(true);
                    i++;

                    JSONObject formDataobj = JSONObject.parseObject(formData);
                    //sqr
                    String fillUser = formDataobj.getJSONObject("fillUser").getJSONArray("value").getJSONObject(0).getString("employeeNo");
                    new BaseBean().writeLog("fillUser:"+fillUser);
                    if (!fillUser.equals("")){
                        Map user = getUserInfo(fillUser);
                        String userid = user.get("id").toString();
                        new BaseBean().writeLog("userid:"+userid);
                        wrti[i]  = new WorkflowRequestTableField();
                        wrti[i].setFieldName("sqr");
                        wrti[i].setFieldValue(userid);
                        wrti[i].setView(true);
                        wrti[i].setEdit(true);
                        i++;

                        String reimUser =  "";//报销人
                        String reimUsername =  "";
                        if (formDataobj.containsKey("reimUser")) {
                            reimUser = formDataobj.getJSONObject("reimUser").getJSONArray("value").getJSONObject(0).getString("employeeNo");
                            reimUsername = formDataobj.getJSONObject("reimUser").getJSONArray("value").getJSONObject(0).getString("text");

                             new BaseBean().writeLog("reimUser:"+reimUser);
                             Map reimUsermap = getUserInfo(reimUser);

                             String reimUserid = reimUsermap.get("id").toString();
                             new BaseBean().writeLog("reimUserid:"+reimUserid);
                             wrti[i]  = new WorkflowRequestTableField();
                             wrti[i].setFieldName("reimUser");
                             wrti[i].setFieldValue(reimUserid);
                             wrti[i].setView(true);
                             wrti[i].setEdit(true);
                            i++;
                        }

                        String formCode =  "";//单据号
                        if (formDataobj.containsKey("formDataCode")) {
                            formCode = formDataobj.getJSONObject("formDataCode").getJSONObject("value").getString("text");
                            new BaseBean().writeLog("formDataCode:"+formCode);
                            wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("formCode");
                            wrti[i].setFieldValue(formCode);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                             //new BaseBean().writeLog("i1:"+i);
                            i++;
                        }



                        //事由  reimburseName - >
                        String reimburseName =  "";
                        if (formDataobj.containsKey("reimburseName")) {
                            reimburseName = formDataobj.getJSONObject("reimburseName").getJSONObject("value").getString("text");
                            new BaseBean().writeLog("reimburseName:"+reimburseName);
                            wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("sy");
                            wrti[i].setFieldValue(reimburseName);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;
                        }

                        //承担部门  coverDepartment
                        String coverDepartment =  "";
                        if (formDataobj.containsKey("coverDepartment")) {
                            coverDepartment = formDataobj.getJSONObject("coverDepartment").getJSONObject("value").getString("businessCode");
                            new BaseBean().writeLog("coverDepartment:"+coverDepartment);
                            wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("szbm");
                            wrti[i].setFieldValue(coverDepartment);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;
                        }

                        //备注  comments
                        String comments =  "";
                        if (formDataobj.containsKey("comments")) {
                            comments = formDataobj.getJSONObject("comments").getJSONObject("value").getString("text");
                            new BaseBean().writeLog("comments:"+comments);
                            wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("bz");
                            wrti[i].setFieldValue(comments);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;
                        }

                        String bankName =  "";//银行中文名称
                        String bankBranchName =  "";//分支行名称
                        String bankAcctName =  "";//账户名
                        String bankAcctNumber =  "";//户号
                        String paymentType =  "";//账户类型
                        String bankBranchNo =  "";//分支行号联行号
                        String bankLocation =  "";//开户行地址
                        String bankCityName =  "";//银行城市
                        String bankProvinceName =  "";//银行省份
                        if (formDataobj.containsKey("payeeAccount")) {
                           JSONObject payeeAccount = formDataobj.getJSONObject("payeeAccount").getJSONObject("value");
                           bankName = payeeAccount.getString("bankName");
                           new BaseBean().writeLog("bankName:"+bankName);
                            wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("bankName");
                            wrti[i].setFieldValue(bankName);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;

                           bankBranchName = payeeAccount.getString("bankBranchName");
                           new BaseBean().writeLog("bankBranchName:"+bankBranchName);
                            wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("bankBranchName");
                            wrti[i].setFieldValue(bankBranchName);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;

                           bankAcctName = payeeAccount.getString("bankAcctName");
                           new BaseBean().writeLog("bankAcctName:"+bankAcctName);
                            wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("bankAcctName");
                            wrti[i].setFieldValue(bankAcctName);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;

                           bankAcctNumber = payeeAccount.getString("bankAcctNumber");
                           new BaseBean().writeLog("bankAcctNumber:"+bankAcctNumber);
                           wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("bankAcctNumber");
                            wrti[i].setFieldValue(bankAcctNumber);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;

                           paymentType = payeeAccount.getString("paymentType");
                           new BaseBean().writeLog("paymentType:"+paymentType);
                           wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("paymentType");
                            wrti[i].setFieldValue(paymentType);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;

                           bankBranchNo = payeeAccount.getString("bankBranchNo");
                           new BaseBean().writeLog("bankBranchNo:"+bankBranchNo);
                           wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("bankBranchNo");
                            wrti[i].setFieldValue(bankBranchNo);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;

                           bankLocation = payeeAccount.getString("bankLocation");
                           new BaseBean().writeLog("bankLocation:"+bankLocation);
                           wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("bankLocation");
                            wrti[i].setFieldValue(bankLocation);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;

                           bankCityName = payeeAccount.getString("bankCityName");
                           new BaseBean().writeLog("bankCityName:"+bankCityName);
                           wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("bankCityName");
                            wrti[i].setFieldValue(bankCityName);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;

                           bankProvinceName = payeeAccount.getString("bankProvinceName");
                           new BaseBean().writeLog("bankProvinceName:"+bankProvinceName);
                           wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("bankProvinceName");
                            wrti[i].setFieldValue(bankProvinceName);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;

                        }

                        String legalEntity =  "";//公司抬头
                        if (formDataobj.containsKey("legalEntity")) {
                            legalEntity = formDataobj.getJSONObject("legalEntity").getJSONObject("value").getString("text");
                            new BaseBean().writeLog("legalEntity:"+legalEntity);
                             wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("legalEntity");
                            wrti[i].setFieldValue(legalEntity);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;
                        }

                        String submittedAt =  "";//提单时间
                        if (formDataobj.containsKey("submittedAt")) {
                            submittedAt = formDataobj.getJSONObject("submittedAt").getJSONObject("value").getString("text");
                            new BaseBean().writeLog("submittedAt:"+submittedAt);
                            wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("submittedAt");
                            wrti[i].setFieldValue(submittedAt);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;
                            wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("sqrq");
                            wrti[i].setFieldValue(submittedAt);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;

                        }
                        String amount =  "";//报销金额
                        if (formDataobj.containsKey("amount")) {
                            amount = formDataobj.getJSONObject("amount").getJSONObject("value").getString("text");
                            new BaseBean().writeLog("amount:"+amount);
                            wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("amount");
                            wrti[i].setFieldValue(amount);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;
                        }
                        String baseAmount =  "";//报销本币金额
                        if (formDataobj.containsKey("baseAmount")) {
                            baseAmount = formDataobj.getJSONObject("baseAmount").getJSONObject("value").getString("text");
                            new BaseBean().writeLog("baseAmount:"+baseAmount);
                            wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("baseAmount");
                            wrti[i].setFieldValue(baseAmount);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;
                        }

                        String payableAmount =  "";//支付金额
                        if (formDataobj.containsKey("payableAmount")) {
                            payableAmount = formDataobj.getJSONObject("payableAmount").getJSONObject("value").getString("text");
                            new BaseBean().writeLog("payableAmount:"+payableAmount);
                            wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("payableAmount");
                            wrti[i].setFieldValue(payableAmount);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;
                        }

                        String payableBaseAmount =  "";	//支付本币金额
                        if (formDataobj.containsKey("payableBaseAmount")) {
                            payableBaseAmount = formDataobj.getJSONObject("payableBaseAmount").getJSONObject("value").getString("text");
                            new BaseBean().writeLog("payableBaseAmount:"+payableBaseAmount);
                            wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("payableBaseAmount");
                            wrti[i].setFieldValue(payableBaseAmount);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;
                        }
                        String deductionAmount =  "";	//核销金额
                        if (formDataobj.containsKey("deductionAmount")) {
                            deductionAmount = formDataobj.getJSONObject("deductionAmount").getJSONObject("value").getString("text");
                             new BaseBean().writeLog("deductionAmount:"+deductionAmount);
                              wrti[i]  = new WorkflowRequestTableField();
                              wrti[i].setFieldName("deductionAmount");
                              wrti[i].setFieldValue(deductionAmount);
                              wrti[i].setView(true);
                              wrti[i].setEdit(true);
                            i++;
                        }


                        String formSubTypeBizCode =  "";//单据类型业务编码
                        if (formDataobj.containsKey("formSubTypeBizCode")) {
                            formSubTypeBizCode = formDataobj.getJSONObject("formSubTypeBizCode").getJSONObject("value").getString("text");
                            new BaseBean().writeLog("formSubTypeBizCode:"+formSubTypeBizCode);
                            wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("djlxywbm");
                            wrti[i].setFieldValue(formSubTypeBizCode);
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);
                            i++;
                        }

                        //附件明细
                        if (formDataobj.containsKey("CF3")) {
                            List<String> urllist = new ArrayList<>();
                            List<String> namelist = new ArrayList<>();
                            JSONArray cf3arr = formDataobj.getJSONObject("CF3").getJSONArray("value");
                            new BaseBean().writeLog("cf3arr:"+cf3arr);
                            for(int m = 0; m < cf3arr.size(); m++) {
                              JSONObject jsonObject = cf3arr.getJSONObject(m);
                              urllist.add(jsonObject.getString("url"));
                              namelist.add("http:"+jsonObject.getString("name"));
                            }

                            String urlstr = StringUtils.join(urllist,"|");
                            String namestr = StringUtils.join(namelist,"|");
                            new BaseBean().writeLog("urlstr:"+urlstr);
                            new BaseBean().writeLog("namestr:"+namestr);

                            wrti[i]  = new WorkflowRequestTableField();
                            wrti[i].setFieldName("fj");
                            wrti[i].setFieldType(namestr);
                            wrti[i].setFieldValue(urlstr);//附件地址
                            wrti[i].setView(true);
                            wrti[i].setEdit(true);

                        }

                        WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];//主字段只有一行数据
                        wrtri[0] = new WorkflowRequestTableRecord();
                        wrtri[0].setWorkflowRequestTableFields(wrti);
                        WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();
                        wmi.setRequestRecords(wrtri);
                        new BaseBean().writeLog("明细字段:--->Begin");
                        //明细字段
                        WorkflowDetailTableInfo wdti[] = new WorkflowDetailTableInfo[1];
                        if (formDataobj.containsKey("expenseList")) {
                            JSONArray expenseListarr = formDataobj.getJSONObject("expenseList").getJSONArray("value");
                            new BaseBean().writeLog("明细数组:--->"+expenseListarr.size()+", "+expenseListarr);
                            wrtri = new WorkflowRequestTableRecord[expenseListarr.size()];
                            wdti[0] = new WorkflowDetailTableInfo();
                            wdti[0].setWorkflowRequestTableRecords(wrtri);//加入明细表7的数据
                            for (int j = 0; j <expenseListarr.size(); j++) {
                                new BaseBean().writeLog("明细序号--->"+j);

                                wrti = new WorkflowRequestTableField[11];
                                JSONObject expenseobj = expenseListarr.getJSONObject(j);
                                int k = 0;

                                //费用类型
                                String expenseTypeCode = "";
                                if (expenseobj.containsKey("expenseTypeCode")) {
                                    expenseTypeCode = expenseobj.getJSONObject("expenseTypeCode").getJSONObject("value").getString("text");
                                    new BaseBean().writeLog("expenseTypeCode:--->"+expenseTypeCode);
                                    wrti[k] = new WorkflowRequestTableField();
                                    wrti[k].setFieldName("expenseTypeCode");
                                    wrti[k].setFieldValue(expenseTypeCode);
                                    wrti[k].setView(true);
                                    wrti[k].setEdit(true);
                                    k++;
                                }
                                //消费金额
                                String consumeAmount = "";
                                if (expenseobj.containsKey("consumeAmount")) {
                                    consumeAmount = expenseobj.getJSONObject("consumeAmount").getJSONObject("value").getString("text");
                                    new BaseBean().writeLog("consumeAmount:--->"+consumeAmount);

                                    wrti[k] = new WorkflowRequestTableField();
                                    wrti[k].setFieldName("consumeAmount");
                                    wrti[k].setFieldValue(consumeAmount);
                                    wrti[k].setView(true);
                                    wrti[k].setEdit(true);
                                    k++;
                                }
                                //备注信息
                                String commentsdet = "";
                                if (expenseobj.containsKey("comments")) {
                                    commentsdet	 = expenseobj.getJSONObject("comments").getJSONObject("value").getString("text");
                                    new BaseBean().writeLog("明细备注信息commentsdet:--->"+commentsdet);

                                    wrti[k] = new WorkflowRequestTableField();
                                    wrti[k].setFieldName("comments");
                                    wrti[k].setFieldValue(commentsdet);
                                    wrti[k].setView(true);
                                    wrti[k].setEdit(true);
                                    k++;
                                 }

                                //发票信息--->
                                if (expenseobj.containsKey("invoiceList")) {
                                     new BaseBean().writeLog("进入发票信息:--->");
                                      JSONArray invoiceListArray = expenseobj.getJSONObject("invoiceList").getJSONArray("value");
                                          // for(int l = 0; l < invoiceListsize; l++) {
                                          JSONObject invoiceobj = invoiceListArray.getJSONObject(0);

                                          //发票验真状态
                                            String validateStatusDesc = invoiceobj.getString("validateStatusDesc");
                                            new BaseBean().writeLog("validateStatusDesc:--->"+validateStatusDesc);
                                            wrti[k] = new WorkflowRequestTableField();
                                            wrti[k].setFieldName("validateStatusDesc");
                                            wrti[k].setFieldValue(validateStatusDesc);
                                            wrti[k].setView(true);
                                            wrti[k].setEdit(true);
                                            k++;


                                            //是否代开发票
                                            String agentMark = invoiceobj.getString("agentMark");
                                            new BaseBean().writeLog("agentMark:--->"+agentMark);
                                            wrti[k] = new WorkflowRequestTableField();
                                            wrti[k].setFieldName("agentMark");
                                            wrti[k].setFieldValue(agentMark);
                                            wrti[k].setView(true);
                                            wrti[k].setEdit(true);
                                            k++;

                                            //发票代码
                                            String invoiceCode = invoiceobj.getString("invoiceCode");
                                            new BaseBean().writeLog("invoiceCode:--->"+invoiceCode);
                                            wrti[k] = new WorkflowRequestTableField();
                                            wrti[k].setFieldName("invoiceCode");
                                            wrti[k].setFieldValue(invoiceCode);
                                            wrti[k].setView(true);
                                            wrti[k].setEdit(true);
                                            k++;

                                            //发票号码
                                            String invoiceNumber = invoiceobj.getString("invoiceNumber");
                                            new BaseBean().writeLog("invoiceNumber:--->"+invoiceNumber);
                                            wrti[k] = new WorkflowRequestTableField();
                                            wrti[k].setFieldName("invoiceNumber");
                                            wrti[k].setFieldValue(invoiceNumber);
                                            wrti[k].setView(true);
                                            wrti[k].setEdit(true);
                                            k++;

                                            //是否加盖公司专用章
                                            String companySeal = invoiceobj.getString("companySeal");
                                            new BaseBean().writeLog("companySeal:--->"+companySeal);
                                            wrti[k] = new WorkflowRequestTableField();
                                            wrti[k].setFieldName("companySeal");
                                            wrti[k].setFieldValue(companySeal);
                                            wrti[k].setView(true);
                                            wrti[k].setEdit(true);
                                            k++;

                                            //发票备注
                                            String invoiceRemark = invoiceobj.getString("invoiceRemark");
                                            new BaseBean().writeLog("invoiceRemark:--->"+invoiceRemark);
                                            wrti[k] = new WorkflowRequestTableField();
                                            wrti[k].setFieldName("invoiceRemark");
                                            wrti[k].setFieldValue(invoiceRemark);
                                            wrti[k].setView(true);
                                            wrti[k].setEdit(true);
                                            k++;

                                            //结算方式 personal_advance（个人垫付）ent_paid_order（企业支付）pay_afterwards（事后统一支付）
                                            String settleType =  invoiceobj.getString("settleType");
                                            new BaseBean().writeLog("settleType:--->"+settleType);
                                            wrti[k] = new WorkflowRequestTableField();
                                            wrti[k].setFieldName("settleType");
                                            wrti[k].setFieldValue(settleType);
                                            wrti[k].setView(true);
                                            wrti[k].setEdit(true);
                                            k++;
                                            //舱位显示名称
                                            //座位显示名称
                                            //发票图片
                                            String invoiceUrl =  invoiceobj.getString("invoiceUrl");
                                            new BaseBean().writeLog("invoiceUrl:--->"+invoiceUrl);
                                            wrti[k] = new WorkflowRequestTableField();
                                            wrti[k].setFieldName("attachments");//附件
                                            wrti[k].setFieldType("http:"+invoiceobj.getString("invoiceImgName"));//http:开头代表该字段为附件字段
                                            wrti[k].setFieldValue(invoiceUrl);//附件地址
                                            wrti[k].setView(true);
                                            wrti[k].setEdit(true);
    //                                        wrti[k] = new WorkflowRequestTableField();
    //                                        wrti[k].setFieldName("fplj");
    //                                        wrti[k].setFieldValue(invoiceUrl);
    //                                        wrti[k].setView(true);
    //                                        wrti[k].setEdit(true);
    //                                }
                                 }
                                new BaseBean().writeLog("数据遍历完毕:--->");

                                wrtri[j] = new WorkflowRequestTableRecord();
                                //new BaseBean().writeLog("test1:--->");
                                wrtri[j].setWorkflowRequestTableFields(wrti);
                                //new BaseBean().writeLog("test2:--->");

                            }
                             wdti[0] = new WorkflowDetailTableInfo();
                             wdti[0].setWorkflowRequestTableRecords(wrtri);
                            // new BaseBean().writeLog("test3:--->");
                        }

                        //流程基本信息设置
                        WorkflowBaseInfo wbi = new WorkflowBaseInfo();
                        WorkflowRequestInfo wri = new WorkflowRequestInfo();//流程基本信息

                        wbi.setWorkflowId("811");
                        //wbi.setWorkflowId("598");
                        wri.setRequestName("费用报销流程-"+reimUsername+"-"+Util.null2String(date));//流程标题
                        wri.setCreatorId(user.get("id").toString());//创建人id
                        wri.setIsnextflow("1"); //停留在创建节点
                        wri.setRequestLevel("0");//0 正常，1重要，2紧急

                        //添加主字段数据
                        wri.setWorkflowMainTableInfo(wmi);//添加主字段数据
                        // new BaseBean().writeLog("test4:--->");
                        wri.setWorkflowDetailTableInfos(wdti);//添加明细字段数据
                        //new BaseBean().writeLog("test5:--->");
                        wri.setWorkflowBaseInfo(wbi);
                        //new BaseBean().writeLog("test6:--->");
                        WorkflowServiceImpl workflowService=new WorkflowServiceImpl();
                        int userid1 = Integer.parseInt(userid);
                        String requestid = workflowService.doCreateWorkflowRequest(wri,userid1);
                        new BaseBean().writeLog("requestid:--->"+requestid);
                        if(!"".equals(requestid)){
                            if(Integer.parseInt(requestid) >0){
                                result.put("success", true);
                                result.put("errorMsg", "流程触发成功:"+requestid);
                                new BaseBean().writeLog("流程触发成功:"+requestid);
                            }else{
                                result.put("success", false);
                                result.put("errorMsg", "费用流程触发失败,请联系管理员:"+requestid);
                                new BaseBean().writeLog("errorMsg", "费用流程触发失败,请联系管理员:"+requestid);
                            }
                        }
                    }
                }else{
                        result.put("success", false);
                        result.put("errorMsg", "流程触发失败,制单人为空");

                }
                out.println(result.toString());
                out.flush();

        } catch (Exception e) {

            result.put("success", false);
            result.put("errorMsg", "系统异常,流程触发失败,请联系管理员!");
            //e.printStackTrace();
            new BaseBean().writeLog("触发费用报销流程推送捕获异常：" + e.getMessage());
            out.println(result.toString());
            out.flush();
       }
//	}else{
//		result.put("success", false);
//        result.put("errorMsg", "流程触发失败,请联系管理员:-9");
//		out.println(result.toString());
//		out.flush();
//	}
		
%>