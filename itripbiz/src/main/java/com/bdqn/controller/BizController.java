package com.bdqn.controller;

import cn.itrip.common.Dto;
import cn.itrip.common.DtoUtil;
import cn.itrip.dao.itripAreaDic.ItripAreaDicMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/api")
public class BizController {
    @Resource
    ItripAreaDicMapper dao;

    @RequestMapping("/hotel/queryhotcity/{type}")
    @ResponseBody
    public Dto GetList(@PathVariable("type")String type) throws Exception {
        return DtoUtil.returnDataSuccess(dao.isHot(type));
    }
}
