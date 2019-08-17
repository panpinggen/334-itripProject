package com.bdqn.controller;


import cn.itrip.common.*;
import cn.itrip.dao.itripUser.ItripUserMapper;
import cn.itrip.pojo.ItripUser;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bdqn.biz.SentSSM;
import com.bdqn.biz.TokenBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Random;

@Controller
@RequestMapping("/api")
@Api(value = "api",description = "用户模块")
public class UserContrel {
    @Resource
    ItripUserMapper dao;
    @Resource
    TokenBiz biz;

    @Resource
    JredisApi api;

    @Resource
    SentSSM sentSSM;

    @Resource
    ItripUserMapper userdao;

    @RequestMapping(value="/validatephone")
    @ResponseBody
    public Dto validatephone(String user,String code) throws Exception {
        try {
            //去Redis中查找数据
            String result = api.getRedis("Code:"+user);
            if(result.equals(code)){
                //根据手机号查询实体，然后update
                ItripUser tt = dao.getUserE(user);
                dao.UpdateByid(tt.getUserCode());
                return DtoUtil.returnSuccess("激活成功！");
            }
        }
       catch (Exception ex){
            return DtoUtil.returnFail("激活失败！","10000");

       }
        return DtoUtil.returnFail("激活失败！","10000");
    }

    @RequestMapping(value="/registerbyphone",method = RequestMethod.POST)
    @ResponseBody
    public Dto registerbyphone(@RequestBody ItripUser itripUser)throws Exception{
        //为手机发送验证码
        try {
            Random random = new Random(7);
            int i =random.nextInt(9999);
            //把随机4位数发送给手机，并存入Redis
            sentSSM.setPhone(itripUser.getUserCode(),""+i);
            //存入到Redis
            api.setRedis("Code:"+itripUser.getUserCode(),60,""+i);
            //把实体存到数据库
            ItripUser user = new ItripUser();
            user.setUserCode(itripUser.getUserCode());
            user.setUserPassword(itripUser.getUserPassword());
            user.setUserName(itripUser.getUserName());
            user.setActivated(0);
            Integer result = userdao.insertItripUser(user);
            if(result>0){
               return DtoUtil.returnDataSuccess(user);
            }
        }
        catch (Exception ex){
            return DtoUtil.returnFail("注册失败","10000");
        }
        return DtoUtil.returnFail("注册失败","10000");
    }

    @RequestMapping(value="/dologin",method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="form",required=true,value="用户名",name="name",defaultValue="itrip@163.com"),
            @ApiImplicitParam(paramType="form",required=true,value="密码",name="password",defaultValue="111111"),
    })
    public Dto getList(HttpServletRequest request, String name, String password) throws Exception {
        try{
            ItripUser user = dao.iflogin(name,MD5.getMd5(password,32));
            if(user!=null){
                //生成token
                String token=biz.generateToken(request.getHeader("User-Agent"),user);
                api.setRedis(token,7200,JSONObject.toJSONString(user));
                //返回前台页面需要当前时间与过期时间
                ItripTokenVO tokenVO=new ItripTokenVO(token,Calendar.getInstance().getTimeInMillis()*7200,Calendar.getInstance().getTimeInMillis());
                return DtoUtil.returnDataSuccess(tokenVO);
            }


        }catch (Exception ex){

        }
            return DtoUtil.returnFail("登录失败","1000");
    }
}
