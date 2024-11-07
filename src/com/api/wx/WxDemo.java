package com.api.wx;

import esteem.jun.wanxiang.services.impl.WxServiceImpl;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("/demo")
public class WxDemo {
    @Path("/test")
    @GET
    public String getAllUser(@QueryParam("page") Integer page,@QueryParam("size") Integer size)
    {
        WxServiceImpl wxService = new WxServiceImpl();
        return wxService.getAllUserInfo(page,size);
    }

    @Path("/test1")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getFiledById(@QueryParam("id") Integer id)
    {
        System.out.println("测试id:"+id);
        WxServiceImpl wxService = new WxServiceImpl();
        return wxService.getUserById(id);
    }


}
