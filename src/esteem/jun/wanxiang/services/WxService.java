package esteem.jun.wanxiang.services;

import esteem.jun.wanxiang.pojo.WxSimpleUser;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


@WebService
public interface WxService {

    /**
     * 获取所有人员信息
     * @return
     */
    @WebMethod(operationName = "getAllUserInfo")
    public String getAllUserInfo(@WebParam(name="Integer") Integer page,@WebParam(name="Integer") Integer size);

    /**
     * 根据id获取Filed1(职级),Filed2字段
     * @param userId
     * @return
     */
    @WebMethod(operationName = "getUserById")
    public String getUserById(@WebParam(name="Integer") Integer userId);
    /**
     * 获取Meetingroom信息
     * @param
     * @return
     */
    @WebMethod(operationName = "getMeetingroom")
    public String getMeetingroom();
}
