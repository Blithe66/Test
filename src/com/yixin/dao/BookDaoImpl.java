package com.yixin.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yixin.po.Book;

public class BookDaoImpl implements BookDao {

	// 查询sql
	private static String sql = "SELECT * FROM book";

	
	@Override
	public List<Book> findBookList() throws Exception {

		// 数据库链接
		Connection connection = null;

		// 预编译statement
		PreparedStatement preparedStatement = null;

		// 结果集
		ResultSet resultSet = null;

		// 区域列表
		List<Book> list = new ArrayList<Book>();
		try {
			// 加载数据库驱动
			Class.forName("com.mysql.jdbc.Driver");
			// 连接数据库
			connection = DriverManager.getConnection(
					"jdbc:mysql://192.168.145.160:3306/rentcar_zh", "root", "123456");
			// 创建preparedStatement
			preparedStatement = connection.prepareStatement(sql);

			// 获取结果集
			resultSet = preparedStatement.executeQuery();

			// 结果集解析
			while (resultSet.next()) {
				Book book = new Book();
				book.setId(resultSet.getInt("id"));
				book.setName(resultSet.getString("name"));
				book.setPrice(resultSet.getFloat("price"));
				book.setPic(resultSet.getString("pic"));
				book.setDescription(resultSet.getString("description"));
				list.add(book);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(resultSet!=null){
				resultSet.close();
			}
			if(preparedStatement!=null){
				preparedStatement.close();
			}
			if(connection!=null){
				connection.close();
			}
		}

		return list;

	}

}
