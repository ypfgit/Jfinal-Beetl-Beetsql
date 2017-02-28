package com.demo.index;

import org.beetl.sql.ext.jfinal.Trans;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * IndexController
 */
public class IndexController extends Controller {
	@Before(Trans.class)
	public void index() {
		render("index.html");
	}
}





