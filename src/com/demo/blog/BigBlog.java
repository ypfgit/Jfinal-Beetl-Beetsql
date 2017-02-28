package com.demo.blog;

import org.beetl.sql.core.annotatoin.Table;

@Table(name="Blog")
public class BigBlog {
	int id;
	String title;
	String content ;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
