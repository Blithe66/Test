package com.yixin.lucene;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class IndexSearchTest {

	@Test
	public void testIndexSearch() throws Exception{
		//1.创建分词器(要和创建索引时用的分词器一样)
		Analyzer analyzer = new IKAnalyzer();
		//2.创建索引目录对象(指定索引库的位置)
		Directory dir = FSDirectory.open(new File("E:\\dic"));
		//3.创建索引的读取流对象
		IndexReader indexReader = IndexReader.open(dir);
		//4.创建搜索对象
		IndexSearcher search = new IndexSearcher(indexReader);
		//5.创建查询条件对象,第一个参数为默认搜索域,如果没有指定搜索的域就去默认搜索域中取查找
		QueryParser queryParser = new QueryParser("name", analyzer);
		Query query = queryParser.parse("编程思想");
		//6.执行搜索,第一个参数,查询对象,  第二个参数:返回多少条记录
		TopDocs topDocs = search.search(query, 2);
		//7.打印一共搜索到了多少条记录
		System.out.println("=====count====" + topDocs.totalHits);
		System.out.println("======================================================");
		
		//8.获取搜索出来的结果集
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		//9.遍历搜索结果
		for(ScoreDoc scoreDoc : scoreDocs){
			//10.获取搜索结果中文档的ID
			int docID = scoreDoc.doc;
			//11.根据文档ID找到指定的文档
			Document document = indexReader.document(docID);
			//id和name字符串都是域名
			System.out.println("id====" + document.get("id"));
			System.out.println("name====" + document.get("name"));
			System.out.println("price====" + document.get("price"));
			System.out.println("======================================================");
		}
	}
	
	
	@Test
	public void testIndexTermQuery() throws Exception{
		//1.创建分词器(要和创建索引时用的分词器一样)
		Analyzer analyzer = new StandardAnalyzer();
		//2.创建索引目录对象(指定索引库的位置)
		Directory dir = FSDirectory.open(new File("E:\\dic"));
		//3.创建索引的读取流对象
		IndexReader indexReader = IndexReader.open(dir);
		//4.创建搜索对象
		IndexSearcher search = new IndexSearcher(indexReader);
		
		//
		Query query = new TermQuery(new Term("description", "java"));
		
		//6.执行搜索,第一个参数,查询对象,  第二个参数:返回多少条记录
		TopDocs topDocs = search.search(query, 2);
		//7.打印一共搜索到了多少条记录
		System.out.println("=====count====" + topDocs.totalHits);
		System.out.println("======================================================");
		
		//8.获取搜索出来的结果集
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		//9.遍历搜索结果
		for(ScoreDoc scoreDoc : scoreDocs){
			//10.获取搜索结果中文档的ID
			int docID = scoreDoc.doc;
			//11.根据文档ID找到指定的文档
			Document document = indexReader.document(docID);
			//id和name字符串都是域名
			System.out.println("id====" + document.get("id"));
			System.out.println("name====" + document.get("name"));
			System.out.println("price====" + document.get("price"));
			System.out.println("======================================================");
		}
	}
	
	@Test
	public void testIndexNumericRangeQuery() throws Exception{
		//1.创建分词器(要和创建索引时用的分词器一样)
		Analyzer analyzer = new StandardAnalyzer();
		//2.创建索引目录对象(指定索引库的位置)
		Directory dir = FSDirectory.open(new File("E:\\dic"));
		//3.创建索引的读取流对象
		IndexReader indexReader = IndexReader.open(dir);
		//4.创建搜索对象
		IndexSearcher search = new IndexSearcher(indexReader);
		
		//数字范围查询:
		//第一个参数:域名,第二个参数:最小值,第三个参数:最大值
		//,第四个参数:是否包含最小值,是为true否为false
		//第五个参数:是否包含最大值,是否true否为false
		NumericRangeQuery<Float> query = NumericRangeQuery.newFloatRange("price", 50f, 70f, true, true);
		
		//6.执行搜索,第一个参数,查询对象,  第二个参数:返回多少条记录
		TopDocs topDocs = search.search(query, 2);
		//7.打印一共搜索到了多少条记录
		System.out.println("=====count====" + topDocs.totalHits);
		System.out.println("======================================================");
		
		//8.获取搜索出来的结果集
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		//9.遍历搜索结果
		for(ScoreDoc scoreDoc : scoreDocs){
			//10.获取搜索结果中文档的ID
			int docID = scoreDoc.doc;
			//11.根据文档ID找到指定的文档
			Document document = indexReader.document(docID);
			//id和name字符串都是域名
			System.out.println("id====" + document.get("id"));
			System.out.println("name====" + document.get("name"));
			System.out.println("price====" + document.get("price"));
			System.out.println("======================================================");
		}
	}
	
	@Test
	public void testIndexBooleanQuery() throws Exception{
		//1.创建分词器(要和创建索引时用的分词器一样)
		Analyzer analyzer = new StandardAnalyzer();
		//2.创建索引目录对象(指定索引库的位置)
		Directory dir = FSDirectory.open(new File("E:\\dic"));
		//3.创建索引的读取流对象
		IndexReader indexReader = IndexReader.open(dir);
		//4.创建搜索对象
		IndexSearcher search = new IndexSearcher(indexReader);
		
		//数字范围查询:
		//第一个参数:域名,第二个参数:最小值,第三个参数:最大值
		//,第四个参数:是否包含最小值,是为true否为false
		//第五个参数:是否包含最大值,是否true否为false
		NumericRangeQuery<Float> query1 = NumericRangeQuery.newFloatRange("price", 50f, 70f, true, true);
		Query query2 = new TermQuery(new Term("description", "java"));
		//组合条件查询:查询价格大于等于50小于等于70并且描述中含有java的数据
		BooleanQuery query = new BooleanQuery();
		query.add(query1, Occur.MUST);
		query.add(query2, Occur.MUST);
		
		//6.执行搜索,第一个参数,查询对象,  第二个参数:返回多少条记录
		TopDocs topDocs = search.search(query, 2);
		//7.打印一共搜索到了多少条记录
		System.out.println("=====count====" + topDocs.totalHits);
		System.out.println("======================================================");
		
		//8.获取搜索出来的结果集
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		//9.遍历搜索结果
		for(ScoreDoc scoreDoc : scoreDocs){
			//10.获取搜索结果中文档的ID
			int docID = scoreDoc.doc;
			//11.根据文档ID找到指定的文档
			Document document = indexReader.document(docID);
			//id和name字符串都是域名
			System.out.println("id====" + document.get("id"));
			System.out.println("name====" + document.get("name"));
			System.out.println("price====" + document.get("price"));
			System.out.println("======================================================");
		}
	}
	
	@Test
	public void testIndexMultiFieldQuery() throws Exception{
		//1.创建分词器(要和创建索引时用的分词器一样)
		Analyzer analyzer = new StandardAnalyzer();
		//2.创建索引目录对象(指定索引库的位置)
		Directory dir = FSDirectory.open(new File("E:\\dic"));
		//3.创建索引的读取流对象
		IndexReader indexReader = IndexReader.open(dir);
		//4.创建搜索对象
		IndexSearcher search = new IndexSearcher(indexReader);
		
		//名称和描述中都含有lucene的数据
		String[] fields = {"name", "description"};
		QueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);
		Query query = queryParser.parse("lucene");
		
		
		//6.执行搜索,第一个参数,查询对象,  第二个参数:返回多少条记录
		TopDocs topDocs = search.search(query, 2);
		//7.打印一共搜索到了多少条记录
		System.out.println("=====count====" + topDocs.totalHits);
		System.out.println("======================================================");
		
		//8.获取搜索出来的结果集
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		//9.遍历搜索结果
		for(ScoreDoc scoreDoc : scoreDocs){
			//10.获取搜索结果中文档的ID
			int docID = scoreDoc.doc;
			//11.根据文档ID找到指定的文档
			Document document = indexReader.document(docID);
			//id和name字符串都是域名
			System.out.println("id====" + document.get("id"));
			System.out.println("name====" + document.get("name"));
			System.out.println("price====" + document.get("price"));
			System.out.println("======================================================");
		}
	}
}
