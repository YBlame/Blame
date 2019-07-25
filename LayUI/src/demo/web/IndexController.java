package demo.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import demo.dao.Bmodel;
import demo.tool.LinkSql;
import demo.tool.PageUtils;
import net.sf.json.JSONArray;

@Controller
public class IndexController {
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private Connection conn = null;
	private Connection connCreate = null;
	private String usersName = null;
	/**
	 * 去index页面
	 * @param request
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toIndex")
	public String toIndex(HttpServletRequest request, HttpServletResponse res) throws Exception {
		HttpSession session = request.getSession();
		final String user ="zb123456";
		final String role = "ZB";
		session.setAttribute("user", user);
		session.setAttribute("role", role);
		usersName = user;
		return "index";
	}
	
	/**
	 * 查询全部描述字段
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "findAll", produces = "text/html;charset=utf-8")
	@ResponseBody
	public Object findAll(HttpServletRequest request, HttpServletResponse res, Integer limit ,PageUtils page, String guid)
			throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		List desList = new ArrayList();
		String bm = null;
		String bmDes=null;
		try {
			bm = Bmodel.findBmByGuId(guid);
			if (bm.equals("bmodel")) {
				bm = Bmodel.findSBmByGuId(guid);
			}
			bmDes= bm+"_des";
			if (limit==null) {
				limit=10;
			}
			page.setRows(limit);
			//查询分页信息
			
			String sqlJoint = " ORDER BY lisnum asc,id asc    limit ?,? ";
			String sqlWhere = " and guid= ?";
			String sql = "SELECT id,zdm,types,zlong,isform,guid,zdmc,lisnum,tips,formtypes,initVal,isedit,isshow,width,jsdm,isselect,sqlrale,iskeep,fontfamilly,fontsize,marleft,martop, beizhu,height, api,omit FROM "+bmDes+" WHERE 1=1 ";
			sql =sql+sqlWhere;
			sql =sql+sqlJoint;
			conn  =LinkSql.getConn();
			ps = LinkSql.Execute(conn,sql,role,bmDes);
			ps.setString(1, guid);
			ps.setInt(2, page.getStart());
			ps.setInt(3, page.getRows());
			rs = ps.executeQuery();
	        ResultSetMetaData md = rs.getMetaData();
	        int columnCount = md.getColumnCount();
	        while (rs.next()) {
	            Map rowData = new HashMap();
	            for (int i = 1; i <= columnCount; i++) {
	                rowData.put(md.getColumnName(i), rs.getObject(i));
	            }
	            desList.add(rowData);
	        }
	        //计数
	        conn  =LinkSql.getConn();
			String sqlCountWhere = " and guid=?";
			String sqlCount = "SELECT count(id) FROM "+bmDes+" WHERE 1=1 "+sqlCountWhere;
			ps = LinkSql.Execute(conn,sqlCount,role,bmDes);
			ps.setString(1, guid);
			rs = ps.executeQuery();
			md=null;
			columnCount=0;
			md = rs.getMetaData();
			String count =null;
	        columnCount = md.getColumnCount();
	        while (rs.next()) {
	            for (int i = 1; i <= columnCount; i++) {
	            	count = rs.getObject(i).toString();
	            }
	        }
			JSONArray json = JSONArray.fromObject(desList);
			String js = json.toString();
			String jso = "{\"code\":0,\"msg\":\"\",\"count\":" + count + ",\"data\":" + js + "}";
			return jso;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 添加描述字段
	 * @param request
	 * @param res
	 * @param bzdk
	 * @param model
	 * @param guid
	 * @throws Exception
	 */
	@RequestMapping("/doAdd")
	public void doAdd(HttpServletRequest request, HttpServletResponse res, Model model, String guid)
			throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		Enumeration pNames = request.getParameterNames();
		String name = null;
		String bmodelName = null;
		String isformStr = null;
		int flag = 0;
		String[] v = new String[26];
		conn = LinkSql.getConn();
		connCreate = LinkSql.getConn();
		conn.setAutoCommit(false);
		connCreate.setAutoCommit(false);
		guid = request.getParameter("guid");
			bmodelName = Bmodel.findBmByGuId(guid);
			if (bmodelName.equals("bmodel")) {
				bmodelName = Bmodel.findSBmByGuId(guid);
			}
		try{
			// 插入描述表
			String sql = "INSERT INTO " + bmodelName + "_des"
					+ "(guid,zdm,zdmc,types,zlong,lisnum,isshow,tips,formtypes,initVal,isform,isedit,width,jsdm,isselect,sqlrale,iskeep,fontfamilly,fontsize,marleft,martop, beizhu,height, api,omit )VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
			ps = LinkSql.Execute(conn,sql,role,bmodelName);
			for (int e = 1; e < v.length; e++) {
				while (pNames.hasMoreElements()) {
					name = (String) pNames.nextElement();
					if (!name.equals("optionValue")) {
						ps.setString(e, request.getParameter(name));
					}
					break;
				}
			}
			flag = ps.executeUpdate();
			rs = conn.getMetaData().getTables(null, null, bmodelName, null);
			if (rs.next()) {
				String zdm = request.getParameter("zdm");
				String types = request.getParameter("types");
				int isform = Integer.parseInt(request.getParameter("isform"));
				int zlong = Integer.parseInt(request.getParameter("zlong"));
				switch (types) {
				case "varchar":
					types = types + "(" + zlong + ")";
					break;
				case "int":
					zlong = 0;
					break;
				case "datetime":
					zlong = 0;
					break;
				case "text":
					zlong = 0;
					break;
				}
				if (isform == 1) {
					isformStr = " NOT NULL ";
				} else if (isform == 0) {
					isformStr = " ";
				}
				String sqlAdd = "ALTER TABLE " + bmodelName + " ADD " + zdm + " " + types + isformStr + "";
				ps = LinkSql.Execute(connCreate,sqlAdd,role,bmodelName);
				flag = ps.executeUpdate();
				flag = 2;
				connCreate.commit();
				conn.commit();
				request.getRequestDispatcher("/bzdk_Index.jsp?guid" + guid + "&flag=" + flag).forward(request, res);
			} else {
				conn.commit();
				request.getRequestDispatcher("toCreateTable").forward(request, res);
			}
		} catch (Exception e) {
			flag = -404;// 列名已经存在
			request.getRequestDispatcher("/bzdk_add.jsp?guid" + guid + "&flag=" + flag).forward(request, res);
			conn.rollback();
			connCreate.rollback();
		}finally {
			ps.close();
			conn.close();
		}
		
	}
	/**
	 * 创建描述表
	 * @param request
	 * @param res
	 * @param guid
	 * @throws Exception
	 */
	@RequestMapping("/toCreateTable")
	public void toCreateTable(HttpServletRequest request, HttpServletResponse res, String guid) throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		int flag = 0;
		String dataName = null;
		String zdm = null;
		int zlong = 0;
		int isform = 0;
		String isformStr = null;
		String types = null;
		String sqlJoint = "id INT AUTO_INCREMENT NOT NULL  PRIMARY KEY,guid varchar(128) NOT NULL,";
		String sqljoin = null;
		conn = LinkSql.getConn();
		dataName = Bmodel.findBmByGuId(guid);
		if (dataName.equals("bmodel")) {
			dataName = Bmodel.findSBmByGuId(guid);
		}
		String sql = "select zdm,types,zlong,isform"
				+ " from " + dataName + "_des" + " where guid= ?";
		ps =LinkSql.Execute(conn,sql,role,dataName + "_des");
		ps.setString(1, guid);
		rs = ps.executeQuery();
		while (rs.next()) {
			zdm = rs.getString(1);
			types = rs.getString(2);
			zlong = rs.getInt(3);
			isform = rs.getInt(4);
			switch (types) {
			case "varchar":
				types = types + "(" + zlong + ")";
				break;
			case "int":
				zlong = 0;
				break;
			case "datetime":
				zlong = 0;
				break;
			case "text":
				zlong = 0;
				break;
			}
			if (isform == 1) {
				isformStr = " NOT NULL";
			} else {
				isformStr = " NULL ";
			}

			sqlJoint += "" + zdm + " " + types + isformStr + ",";
		}
		try {
			sqljoin = sqlJoint.substring(0, sqlJoint.length() - 1);
			StringBuilder insertSql = new StringBuilder("create table " + dataName + " (" + "" + sqljoin + "" + ")");
			conn = LinkSql.getConn();
			conn.setAutoCommit(false);
			ps = LinkSql.Execute(conn,insertSql.toString(),role,dataName);
			flag = ps.executeUpdate();
			conn.commit();
			request.getRequestDispatcher("/bzdk_Index.jsp?guid" + guid + "&flag=" + flag).forward(request, res);
		} catch (Exception e) {
			conn.rollback();
			request.getRequestDispatcher("/bzdk_Index.jsp?guid" + guid + "&flag=" + flag).forward(request, res);
		}
	}
	/**
	 * 删除描述字段
	 * @param model
	 * @param zdm
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toDelete")
	@ResponseBody
	public Object toDelete(Model model, String zdm, String guid,HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		String bmodelName = null;
		int flag = 0;
			bmodelName =  Bmodel.findBmByGuId(guid);
			if (bmodelName.equals("bmodel")) {
				bmodelName = Bmodel.findSBmByGuId(guid);
			}
		conn = LinkSql.getConn();
		conn.setAutoCommit(false);
		String desName = bmodelName + "_des";
		String sql = "DELETE FROM " + desName + " WHERE zdm= ?";
		ps = LinkSql.Execute(conn,sql,role,desName);
		ps.setString(1, zdm);
		try {
			flag = ps.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			flag = -404;
		}
		try {
			if (flag == 1) {
				String sqlUpdate = "ALTER TABLE " + bmodelName + " DROP COLUMN " + zdm + "";
				conn = LinkSql.getConn();
				conn.setAutoCommit(false);
				ps = LinkSql.Execute(conn,sqlUpdate,role,bmodelName);
				flag = ps.executeUpdate();
				conn.commit();
			}
		} catch (Exception e) {
			conn.rollback();
		}
		flag = 5;
		return flag;
	}
	/**
	 * 去修改页面，数据回显
	 * @param request
	 * @param res
	 * @param model
	 * @param zdm
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toUpdate")
	public Object toUpdate(HttpServletRequest request, HttpServletResponse res, Model model, String zdm, String guid)
			throws Exception {
		try {
			HttpSession session = request.getSession();
			String role = session.getAttribute("role").toString();
			String bmodelName = null;
			Map<String, Object> map = new TreeMap<String, Object>();
			bmodelName = Bmodel.findBmByGuId(guid);
			if (bmodelName.equals("bmodel")) {
				bmodelName = Bmodel.findSBmByGuId(guid);
			}
			conn = LinkSql.getConn();
			conn.setAutoCommit(false);
			String desName = bmodelName + "_des";
			String sqlUpdate = "SELECT * FROM " + desName + " WHERE zdm =?";
			ps =   LinkSql.Execute(conn,sqlUpdate,role,bmodelName);
			ps.setString(1, zdm);
			rs = ps.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			while (rs.next()) {
			    for (int i = 1; i <= columnCount; i++) {
			        map.put(md.getColumnName(i), rs.getObject(i));
			    }
			}
			request.setAttribute("map", map);
		} catch (Exception e) {
			ps.close();
			conn.close();
		}
		return "bzdk_edit";
	}
	/**
	 * 做修改方法
	 * @param request
	 * @param res
	 * @param model
	 * @param zdm
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("doUpdate")
	@ResponseBody
	public Object doUpdate(HttpServletRequest request, HttpServletResponse res, Model model, String zdm, String guid)
			throws Exception {
		try {
			HttpSession session = request.getSession();
			String role = session.getAttribute("role").toString();
			Enumeration pNames = request.getParameterNames();
			String name = null;
			String bmodelName = null;
			String isformStr = null;
			int flag = 0;
			int zlong = 0;
			int isform = 0;
			String oldName = null;
			String types = null;
			String[] v = new String[25];
			conn = LinkSql.getConn();
			conn.setAutoCommit(false);
			int id = Integer.parseInt(request.getParameter("id"));
			bmodelName = Bmodel.findBmByGuId(guid);
			if (bmodelName.equals("bmodel")) {
				bmodelName = Bmodel.findSBmByGuId(guid);
			}
			String sqlSelect = "select zdm,types,zlong,isform,id,guid"
					+ " from " + bmodelName + "_des" + " where id= ?";
			ps =  LinkSql.Execute(conn,sqlSelect,role,bmodelName + "_des");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				zdm = rs.getString(1);
				oldName = zdm;
				types = rs.getString(2);
				zlong = rs.getInt(3);
				isform = rs.getInt(4);
			}
			// 修改描述表
			String sql = "UPDATE  " + bmodelName + "_des SET" 
					+ " zdm=?,zdmc=?,types=?,zlong=?,lisnum=?,isshow=?,tips=?,formtypes=?,initVal=?,isform=?,isedit=?,width=?,jsdm=?,isselect=?,sqlrale=?,iskeep=?,fontfamilly=?,fontsize=?,marleft=?,martop=?,beizhu=?,height=?,api=?,omit=?,guid=? WHERE id = "+id;
			try {
				ps = LinkSql.Execute(conn,sql,role,bmodelName + "_des");
				for (int e = 1; e <= v.length; e++) {
					while (pNames.hasMoreElements()) {
						name = (String) pNames.nextElement();
						ps.setString(e, request.getParameter(name));
						break;
					}
				}
				flag = ps.executeUpdate();
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
			}
			rs = conn.getMetaData().getTables(null, null, bmodelName, null);
			if (rs.next()) {
				zdm = request.getParameter("zdm");
				types = request.getParameter("types");
				isform = Integer.parseInt(request.getParameter("isform"));
				zlong = Integer.parseInt(request.getParameter("zlong"));
				switch (types) {
				case "varchar":
					types = types + "(" + zlong + ")";
					break;
				case "int":
					zlong = 0;
					break;
				case "datetime":
					zlong = 0;
					break;
				case "text":
					zlong = 0;
					break;
				}
				if (isform == 1) {
					isformStr = " NOT NULL ";
				} else if (isform == 0) {
					isformStr = " NULL ";
				}
				String sqlAdd = "ALTER TABLE " + bmodelName + " CHANGE  " +oldName +" "+ zdm + " " + types + isformStr + "";
				try {
					ps =  LinkSql.Execute(conn,sqlAdd,role,bmodelName);
					flag = ps.executeUpdate();
					conn.commit();
				} catch (Exception e) {
					conn.rollback();
				}
				flag = 2;
				request.getRequestDispatcher("/bzdk_Index.jsp?guid" + guid + "&flag=" + flag).forward(request, res);
			} else {
				request.getRequestDispatcher("toCreateTable").forward(request, res);
				return null;
			}
		}finally{
			rs.close();
			ps.close();
			conn.close();
		}
		return null;
	}

}
