/**
 * 
 */
package com.ydw.oa.wkflow.util.coder;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

/**
 * 代码生成 - mybatis-plus
 * 官方示例
 * @author 冯晓东
 *
 */
public class CodeGenerator {

	private static final String parent = "com.ydw.oa.wkflow.business_main";

	private static final String jdbcUrl = "jdbc:mysql://test.eqbidding.com:3306/oa-wkflow?nullCatalogMeansCurrent=true&serverTimezone=Asia/Shanghai&useSSL=false&characterEncoding=utf-8";
	private static final String driver = "com.mysql.cj.jdbc.Driver";
	private static final String uname = "fxd";
	private static final String password = "123456";
	
	/**
	 * <p>
	 * 读取控制台内容
	 * </p>
	 */
	public static String scanner(String tip) {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		StringBuilder help = new StringBuilder();
		help.append("请输入" + tip + "：");
		System.out.println(help.toString());
		if (scanner.hasNext()) {
			String ipt = scanner.next();
			if (StringUtils.isNotEmpty(ipt)) {
				return ipt;
			}
		}
		throw new MybatisPlusException("请输入正确的" + tip + "！");
	}

	public static void main(String[] args) {
     // 代码生成器
     AutoGenerator mpg = new AutoGenerator();

     // 全局配置
     GlobalConfig gc = new GlobalConfig();
     String projectPath = System.getProperty("user.dir");
     gc.setOutputDir(projectPath + "/src/main/java");
     gc.setAuthor("hxj");
     gc.setOpen(false);
     //实体属性 Swagger2 注解
     gc.setSwagger2(true); 
     mpg.setGlobalConfig(gc);

     // 数据源配置
     DataSourceConfig dsc = new DataSourceConfig();
     dsc.setUrl(jdbcUrl);
     // dsc.setSchemaName("public");
     dsc.setDriverName(driver);
     dsc.setUsername(uname);
     dsc.setPassword(password);
     mpg.setDataSource(dsc);

     // 包配置
     PackageConfig pc = new PackageConfig();
     pc.setModuleName(scanner("模块名"));
     pc.setParent(parent);
     mpg.setPackageInfo(pc);

     // 自定义配置
     InjectionConfig cfg = new InjectionConfig() {
         @Override
         public void initMap() {
             // to do nothing
         }
     };

     // 如果模板引擎是 freemarker
     //String templatePath = "/templates/mapper.xml.ftl";
     // 如果模板引擎是 velocity
     String templatePath = "/templates/mapper.xml.vm";

     // 自定义输出配置
     List<FileOutConfig> focList = new ArrayList<>();
     // 自定义配置会被优先输出
     focList.add(new FileOutConfig(templatePath) {
         @Override
         public String outputFile(TableInfo tableInfo) {
             // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
             return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                     + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
         }
     });
     /*
     cfg.setFileCreate(new IFileCreate() {
         @Override
         public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
             // 判断自定义文件夹是否需要创建
             checkDir("调用默认方法创建的目录");
             return false;
         }
     });
     */
     cfg.setFileOutConfigList(focList);
     mpg.setCfg(cfg);

     // 配置模板
     TemplateConfig templateConfig = new TemplateConfig();

     // 配置自定义输出模板
     //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
     // templateConfig.setEntity("templates/entity2.java");
     // templateConfig.setService();
     // templateConfig.setController();

     templateConfig.setXml(null);
     mpg.setTemplate(templateConfig);

     // 策略配置
     StrategyConfig strategy = new StrategyConfig();
     strategy.setNaming(NamingStrategy.underline_to_camel);
     strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//     strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
     strategy.setEntityLombokModel(true);
     strategy.setRestControllerStyle(true);
     // 公共父类
//     strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
     // 写于父类中的公共字段
     strategy.setSuperEntityColumns("id");
     strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
     strategy.setControllerMappingHyphenStyle(true);
     strategy.setTablePrefix("t_");
     mpg.setStrategy(strategy);
     mpg.setTemplateEngine(new VelocityTemplateEngine());
     mpg.execute();
 }

}
