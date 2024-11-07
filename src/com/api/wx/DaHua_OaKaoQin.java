package com.api.wx;

import com.api.wx.services.DaHua_OaKaoQinService;
import com.api.wx.services.Impl.DaHua_OaKaoQinServiceImpl;
import com.api.wx.vo.ExtendVO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/wxoa")
public class DaHua_OaKaoQin {

   DaHua_OaKaoQinService daHuaOaKaoQinService= new DaHua_OaKaoQinServiceImpl();

    @Path("/mk")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String insertWorkAttendance(ExtendVO extendVO)
    {
      return daHuaOaKaoQinService.insertWorkAttendance(extendVO);
    }
}
