package com.taotao.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 使用Freemarker网页静态化
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月30日 上午10:38:32
 *
 */

@Controller
public class FreeMarkerGenHtmlController {
	@Resource
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@RequestMapping("/genHtml")
	@ResponseBody
	public String genHtml() throws Exception{
		// 获取configuration对象
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		// configuration加载模板对象,需指定模板文件名
		Template template = configuration.getTemplate("hello.ftl");
		// 创建数据集
		Map<String, Object> dataModel = new HashMap<>();
		dataModel.put("hello", "spring整合Freemarker");
		// 创建文件输出流
		FileWriter fileWriter = new FileWriter(new File("F:/temp/freemarker_out/springFreeMarker.html"));
		// template调用process方法输出静态页面
		template.process(dataModel, fileWriter);
		// 关闭流
		fileWriter.close();
		return "OK";
	}
}
