package demo.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LinkSql {
	//systemName为开发人员中的系统表如：菜单表权限表
	public static String systemName = "bmodel";
	
	//adminName为管理员中的表如：栏目表
	public static String adminmName = "smodel";
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/layui?characterEncoding=UTF-8";
	private static String username = "y";
	private static String password = "xing";
	
	private static Connection conn;
	private static PreparedStatement ps;
	private static ResultSet rs;
	
	/**
	 * 连接数据库操作
	 * @return
	 */
	public static Connection getConn() {
	    try {
	        Class.forName(driver); //classLoader,加载对应驱动
	        conn = (Connection) DriverManager.getConnection(url, username, password);
	    } catch (ClassNotFoundException e) {
	    	System.out.println("数据库驱动加载失败");
	        e.printStackTrace();
	    } catch (SQLException e) {
	    	System.out.println("数据库连接失败");
	        e.printStackTrace();
	    }
	    return conn;
	}
	/**
	 * 关闭数据库连接
	 * 关闭PreparedStatement
	 * @param stmt
	 * @param conn
	 */
	public static void close(PreparedStatement ps, Connection conn) {
		if (ps != null) {
			try {
				ps.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("PreparedStatement 关闭失败");
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Connection 关闭失败");
			}
		}
	}
	
	/**
	 * 对数据库进行查询操作
	 * 返回值为ResultSet
	 * 将conn设置为手动提交出现异常直接回滚
	 * @param conn 
	 * @param sql
	 * @return
	 * @throws Exception 
	 */
	public static PreparedStatement Execute(Connection conn, String sql,String role,String tn) throws Exception{
		try {
			ps = conn.prepareStatement(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ps ;
		
	}
	

}
