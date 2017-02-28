package com.demo.common;

import org.beetl.core.GroupTemplate;
import org.beetl.ext.jfinal.BeetlRenderFactory;
import org.beetl.sql.ext.jfinal.JFinalBeetlSql;
import org.beetl.sql.ext.jfinal.Trans;
import com.demo.blog.BigBlogController;
import com.demo.blog.blog;
import com.demo.index.IndexController;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

/**
 * API引导式配置
 */
public class DemoConfig extends JFinalConfig {
	
	private  C3p0Plugin c3p0Plugin = null;

	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		PropKit.use("a_little_config.txt");				// 加载少量必要配置，随后可用PropKit.get(...)获取值
		me.setDevMode(PropKit.getBoolean("devMode", false));
		
		me.setMainRenderFactory(new BeetlRenderFactory());
		
		GroupTemplate gt = BeetlRenderFactory.groupTemplate;
		//根据gt可以添加扩展函数，格式化函数，共享变量等，
	
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		me.add("/", IndexController.class, "/index");	// 第三个参数为该Controller的视图存放路径
		me.add("/blog", BigBlogController.class);			// 第三个参数省略时默认与第一个参数值相同，在此即为 "/blog"
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
//		// 配置C3p0数据库连接池插件
		c3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
		me.add(c3p0Plugin);
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
		arp.addMapping("blog", blog.class);	// 映射blog 表到 Blog模型
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		me.addGlobalActionInterceptor(new Trans());
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		
	}
	
	
	
	@Override
	public void afterJFinalStart() {
		super.afterJFinalStart();
		
		JFinalBeetlSql.init(c3p0Plugin.getDataSource(),null);
	}

	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("WebRoot", 8080, "/", 5);
	}
}
