package demo.tool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.annotation.Resource;


public class PublicMethod {
	private static PreparedStatement ps = null;
	private static ResultSet rs = null;
	private static Connection conn = null;
	
	/**
	 * 查询描述表字段
	 * @param tn 表名
	 * @param guid 
	 * @return rs
	 * @throws Exception
	 */
	public static ResultSet findBmodelField(String tn,String guid)throws Exception{
		conn = LinkSql.getConn();
		String sqlSelect = "SELECT zdm,zdmc,isform,isedit,width,tips,formtypes,initval,jsdm,api,guid FROM "+tn+"_des WHERE guid = \'" + guid
				+ "\' ORDER BY lisnum asc , id asc";
		ps = conn.prepareStatement(sqlSelect);
		rs = ps.executeQuery();
		return rs;
	}
	
	/**
	 * 获取描述表中字段名
	 * @param destn
	 * @return
	 * @throws Exception
	 */
	public static ResultSet findBmodelByZdm(String destn) throws Exception{
		conn = LinkSql.getConn();
		String sqlzdm = "select zdm from " + destn +" order by lisnum asc , id asc ";
		ps = conn.prepareStatement(sqlzdm);
		rs = ps.executeQuery();
		return rs;
		
		
	}

}
