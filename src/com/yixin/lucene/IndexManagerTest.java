package com.yixin.lucene;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.yixin.dao.BookDao;
import com.yixin.dao.BookDaoImpl;
import com.yixin.po.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;


/**
 * 由于使用like关键字查询的时候当数据量到500万以上的时候就有些慢了,随着数据量的增加,
 * 使用sql语句中的like会越来越慢.因为like使用的是顺序扫描的算法.
 * 解决方案:使用luene做完全文检索,这样可以大大增加系统的方法速度,因为lucene用的是倒排索引表的算法,
 * 		   对数据先建立索引,然后通过查询索引再去找到数据,所以非常快.
 * @author zj
 *
 */
public class IndexManagerTest {

	@Test
	public void testIndexCreate() throws Exception{
		//1. 从数据库中采集数据
		BookDao bookDao = new BookDaoImpl();
		List<Book> bookList = bookDao.findBookList();
		
		//2.创建文档(Lucen的存储结构):一个文档Document对象就是数据库中的一条数据
		List<Document> docList = new ArrayList<Document>();
		for(Book book : bookList){
			Document doc = new Document();
			
			Integer id = book.getId();
			String name = book.getName();
			Float price = book.getPrice();
			String pic = book.getPic();
			String description = book.getDescription();
			
			//3. 一个Field对象就相当于数据库表中的一行一列的数据,key就是列名,value就是这条数据列的值
			//第一个参数,就是存入的key,第二个参数就是存入的value,第三个参数就是是否存储
//			TextField idField = new TextField("id", id.toString(), Store.YES);
//			TextField nameField = new TextField("name", name, Store.YES);
//			TextField priceField = new TextField("price", price.toString(), Store.YES);
//			TextField picField = new TextField("pic", pic, Store.YES);
//			TextField descriptionField = new TextField("description", description, Store.YES);
			
			//是否分词:no
			//是否索引:yes
			//是否存储:yes
			StringField idField = new StringField("id", id.toString(), Field.Store.YES);
			
			//是否分词:yes
			//是否索引:yes
			//是否存储:yes
			TextField nameField = new TextField("name", name, Field.Store.YES);
			
			//是否分词:yes
			//是否索引:yes
			//是否存储:yes
			FloatField priceField = new FloatField("price", price, Field.Store.YES);
			
			//是否分词:no
			//是否索引:no
			//是否存储:yes
			StoredField picField = new StoredField("pic", pic);
			
			//是否分词:yes
			//是否索引:yes
			//是否存储:no
			TextField descriptionField = new TextField("description", description, Field.Store.NO);
			
			//4. 将所有File都放入Document对象中
			doc.add(idField);
			doc.add(nameField);
			doc.add(priceField);
			doc.add(picField);
			doc.add(descriptionField);
			
			docList.add(doc);
		}
		
		//5.创建分词器:StandardAnalyzer是标准分词器,对英文支持良好,对中文是单字分词
		Analyzer analyzer = new IKAnalyzer();
		//6.创建目录对象:用来指定保存到硬盘或者内存中的位置 file system
		Directory dir = FSDirectory.open(new File("E:\\dic"));
		//7.创建索引初始化对象:指定Lucen jar包的版本和使用的分词器的类型
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
		//8.创建索引的写对象
		IndexWriter indexWriter = new IndexWriter(dir, config);
		
		//9.遍历文档对象集合
		for(Document doc : docList){
			//10.将文档对象加入到索引写对象的流中
			indexWriter.addDocument(doc);
		}
		//11.提交
		indexWriter.commit();
		//12.关闭索引写对象的流
		indexWriter.close();
	}
	
	
	@Test
	public void testIndexDel() throws Exception{
		Analyzer analyzer = new StandardAnalyzer();
		Directory  dir = FSDirectory.open(new File("E:\\dic"));
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
		IndexWriter indexWriter = new IndexWriter(dir, config);
		
		//删除所有
		//indexWriter.deleteAll();
		
		//根据查询条件进行删除(根据主键进行删除)
		QueryParser parser = new QueryParser("id", analyzer);
		Query query = parser.parse("1");
		indexWriter.deleteDocuments(query);
		
		//提交
		indexWriter.commit();
		indexWriter.close();
	}
	
	@Test
	public void testIndexUpdate() throws Exception{
		//1.创建分词器:StandardAnalyzer是标准分词器,对英文支持良好,对中文是单字分词
		Analyzer analyzer = new StandardAnalyzer();
		//2.创建目录对象:用来指定保存到硬盘或者内存中的位置 file system
		Directory dir = FSDirectory.open(new File("E:\\dic"));
		//3.创建索引初始化对象:指定Lucen jar包的版本和使用的分词器的类型
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
		//4.创建索引的写对象
		IndexWriter indexWriter = new IndexWriter(dir, config);
		
		//5.term词元,也就是一个词;第一个参数:域名,第二个参数:域值
		Term term = new Term("id", "2");
		
		//创建更新的文档
		Document doc = new Document();
		doc.add(new StringField("id", "a101", Field.Store.YES));
		doc.add(new TextField("name", "xxxxxx", Field.Store.YES));
		doc.add(new FloatField("price", 12.2222f, Field.Store.YES));
		doc.add(new StoredField("pic", "xxxx.jpg"));
		doc.add(new TextField("description", "xxxxxxiiiiioooooppppppp", Field.Store.NO));
		
		//第一个参数:根据Term进行更新,第二个参数:就是需要更新到lucene中的文档
		//根据Term进行跟新,更新成指定的Document
		indexWriter.updateDocument(term, doc);
		
		indexWriter.commit();
		indexWriter.close();
	}
}
