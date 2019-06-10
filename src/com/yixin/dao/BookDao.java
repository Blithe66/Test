package com.yixin.dao;

import com.yixin.po.Book;

import java.util.List;


public interface BookDao {

	//图书查询
	public List<Book> findBookList()throws Exception;

}
