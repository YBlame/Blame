package demo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import demo.tool.LinkSql;
import demo.tool.PageUtils;

public class Bmodel {
	private static PreparedStatement ps;
	private static ResultSet rs;
	private static Connection conn;
	private List<Map<String, Object>> list;
	public static String findBmByGuId(String guid) throws Exception {
		// TODO Auto-generated method stub
		conn = LinkSql.getConn();
		String bm = LinkSql.systemName;
		String sqlCount = "select bm from "+bm+" where guid ='"+guid+"'";
		ps = conn.prepareStatement(sqlCount);
		rs = ps.executeQuery();
		while(rs.next()){
			bm = rs.getString("bm");
		}
		return bm;
	}
	public static String findBmcBybm(String dataName) throws Exception {
		// TODO Auto-generated method stub
		conn = LinkSql.getConn();
		String bm = LinkSql.systemName;
		String sqlCount = "select bmc from "+bm+" where bm ='"+dataName+"'";
		ps = conn.prepareStatement(sqlCount);
		rs = ps.executeQuery();
		String bmc= null;
		while(rs.next()){
			bmc = rs.getString("bmc");
		}
		return bmc;
	}
	
	public static String findSBmByGuId(String guid) throws Exception {
		// TODO Auto-generated method stub
		conn = LinkSql.getConn();
		String bm = LinkSql.adminmName;
		String sqlCount = "select bm from "+bm+" where guid ='"+guid+"'";
		ps = conn.prepareStatement(sqlCount);
		rs = ps.executeQuery();
		while(rs.next()){
			bm = rs.getString("bm");
		}
		return bm;
	}
	public static String findSbmcBybm(String dataName) throws Exception {
		// TODO Auto-generated method stub
		conn = LinkSql.getConn();
		String bm = LinkSql.adminmName;
		String sqlCount = "select bmc from "+bm+" where bm ='"+dataName+"'";
		ps = conn.prepareStatement(sqlCount);
		rs = ps.executeQuery();
		String bmc= null;
		while(rs.next()){
			bmc = rs.getString("bmc");
		}
		return bmc;
	}
	
}
