package com.demo.blog;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.SQLResult;
import org.beetl.sql.ext.jfinal.JFinalBeetlSql;
import org.beetl.sql.ext.jfinal.Trans;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
/**
 * 架构师可以封装一个BaseModel，采用sqlManager来完成类似原来final的
 * ActivityModel，甚至比他的功能更强大！
 * @author xiandafu
 *
 */
@Before(Trans.class)
public class BigBlogController extends Controller {
	
	public void index() {
		SQLManager dao = JFinalBeetlSql.dao();
		
		/*List<BigBlog> blogs = dao.select("demo.queryBlog", BigBlog.class, null);
		System.out.println(blogs);*/
		Map paras = new HashMap<>();
		paras.put("id", 1);
		SQLResult sqlResult = dao.getSQLResult("demo.queryBlog",paras);
		List<Record> records = Db.query(sqlResult.jdbcSql,sqlResult.jdbcPara.toArray());
		System.out.println(JsonKit.toJson(records));
		
		List<BigBlog> list = dao.all(BigBlog.class, getParaToInt(0, 1), 10);
		int count = dao.allCount(BigBlog.class);
		Page page =getPage(list,count);		
		setAttr("blogPage",page);
		render("blog.html");
	}
	
	public void add() {
	}
	
	
	@Before({BlogValidator.class})
	public void save() {
		BigBlog blog = mapping(BigBlog.class);
		SQLManager dao = JFinalBeetlSql.dao();
		dao.insert(BigBlog.class, blog);
		
		redirect("/blog");
	}
	
	public void edit() {
		SQLManager dao = JFinalBeetlSql.dao();
		int id = getParaToInt();
		BigBlog blog = dao.unique(BigBlog.class, id);
		setAttr("bigBlog", blog);
	}
	
	@Before(BlogValidator.class)
	public void update() {
		BigBlog blog = mapping(BigBlog.class);
		SQLManager dao = JFinalBeetlSql.dao();
		dao.updateById(blog);
		redirect("/blog");
	}
	
	public void delete() {
		int id = getParaToInt();
		SQLManager dao = JFinalBeetlSql.dao();
		dao.deleteById(BigBlog.class,id);
		redirect("/blog");
	}
	
	private Page getPage(List list,int count){
		int pageNumber = getParaToInt(0, 1);
		int totalPage = count/10+count%10!=0?1:0;
		Page  page = new Page(list,pageNumber,10,totalPage,count);
		return page;
	}
	
	
	private <T> T mapping(Class<T> z){
		T o = null;
		try {
			 o = z.newInstance();
			BeanUtils.populate(o, this.getParaMap());
			return o;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	
	
}


