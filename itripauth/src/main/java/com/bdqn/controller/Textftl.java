package com.bdqn.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Textftl {
    public static void main(String[] args) throws Exception {
        Configuration configuration=new Configuration();
        configuration.setDefaultEncoding("utf-8");
        configuration.setDirectoryForTemplateLoading(new File("F:\\学习资料\\334Itrip\\itripbackend\\itripauth\\src\\main\\resources"));
        Template template=configuration.getTemplate("Text.ftl");
        Map<String,Object>map=new HashMap<>();
        map.put("name","小狗");
        template.process(map,new FileWriter("D:\\a.html"));
    }
}
