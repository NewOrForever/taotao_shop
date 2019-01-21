package com.taotao.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 网页静态化freemarker测试
 * <p>Description：</p>
 * @author： sq
 * @date： 2018年9月29日 下午12:42:25
 *
 */

public class TestFreeMarker {
	
	@Test
	public void testFreeMarker() throws Exception{
		// 1.创建模板文件(建真实文件，不是写代码)
		// 2.创建configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		// 3.设置模板所在路径
		configuration.setDirectoryForTemplateLoading(
				new File("F:/myeclipse2017_ci_10/workspace/taotao-item-web/src/main/webapp/WEB-INF/ftl"));
		// 4.设置默认字符集，一般为utf-8
		configuration.setDefaultEncoding("utf-8");
		// 5.configuration对象加载模板,需指定模板文件名
//		Template template = configuration.getTemplate("hello.ftl");
		Template template = configuration.getTemplate("student.ftl");
		// 6.创建数据集，可以是pojo也可以是map（推荐）
		Map<String, Object> dataModel = new HashMap<>();
		dataModel.put("hello", "hello freemarker");
		Student student = new Student(1, "小米", 11, "北京昌平回龙观");
		dataModel.put("student", student);
		List<Student> stuList = new ArrayList<>();
		stuList.add(new Student(1, "小米", 11, "北京昌平回龙观"));
		stuList.add(new Student(2, "小米2", 12, "北京昌平回龙观"));
		stuList.add(new Student(3, "小米3", 13, "北京昌平回龙观"));
		stuList.add(new Student(4, "小米4", 14, "北京昌平回龙观"));
		stuList.add(new Student(5, "小米5", 15, "北京昌平回龙观"));
		stuList.add(new Student(6, "小米6", 16, "北京昌平回龙观"));
		stuList.add(new Student(7, "小米7", 17, "北京昌平回龙观"));
		dataModel.put("stuList", stuList);
		//日期类型的处理
		dataModel.put("date", new Date());
		dataModel.put("val","123456");
		// 7.创建writer，指定输出路径
		FileWriter fileWriter = new FileWriter(new File("F:/temp/freemarker_out/student.html"));
		// 8.使用模板对象的process方法输出文件
		template.process(dataModel, fileWriter);
		// 9.关闭流
		fileWriter.close();
	}


	
	
}
