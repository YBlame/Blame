package demo.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import demo.tool.MD5;
import net.sf.json.JSONArray;
/*
@Controller
public class LoginController {
	
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private Connection conn = null;
	private List<Map<String, Object>> list;
	*//**
	 * 登录方法
	 * @param model
	 * @param request
	 * @param res
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/toLogin")
	@ResponseBody
	public Object toLogin(Model model,HttpServletRequest request,HttpServletResponse res,String sj,String mm)
			throws Exception {
		 // 获取Statement 
        Statement stmt=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		String returnVal = null;
		list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData md = null;
		int columnCount = 0;
		String pwd = MD5.GetMd5(mm);
		if (sj.trim().equals("17600236100")) {
			String sqlWhere=" and sj=\'"+sj+"\' and mm=\'"+pwd+"\' ";
			String sql ="select DWBH,LXR,SJ,JS from syrzc where 1=1  "+sqlWhere;
			conn.prepareStatement(sql);
			rs = stmt.executeQuery(sql);
			rs.last();
			int row  = rs.getRow();
			if (row==0) {
				returnVal = "loginLose";//当登录失败
				return returnVal;
			}else{
				rs.previous();
				md = rs.getMetaData(); // 获得结果集结构信息,元数据
				columnCount = md.getColumnCount(); // 获得列数
				HttpSession session = request.getSession();
				while (rs.next()) {
					Map<String, Object> rowData = new HashMap<String, Object>();
					for (int i = 1; i <= columnCount; i++) {
						rowData.put(md.getColumnName(i), rs.getObject(i));
						session.setAttribute(md.getColumnName(i), rs.getObject(i));
					}
					list.add(rowData);
				}
				session.setAttribute("adminSJ",sj);
				return "loginAdmin";
				
			}
		}else{
			String sqlWhere=" and sj=\'"+sj+"\' and mm=\'"+pwd+"\' ";
			String sql ="select DWBH,LXR,SJ,JS,PARENT_DWBH from gsxx where 1=1 "+sqlWhere;
			conn.prepareStatement(sql);
			rs = stmt.executeQuery(sql);
			rs.last();
			int row  = rs.getRow();
			if (row==0) {
				returnVal = "loginLose";//当登录失败
				return returnVal;
			}else{
				rs.previous();
				md = rs.getMetaData(); // 获得结果集结构信息,元数据
				columnCount = md.getColumnCount(); // 获得列数
				HttpSession session = request.getSession();
				while (rs.next()) {
					Map<String, Object> rowData = new HashMap<String, Object>();
					for (int i = 1; i <= columnCount; i++) {
						rowData.put(md.getColumnName(i), rs.getObject(i));
						session.setAttribute(md.getColumnName(i), rs.getObject(i));
					}
					list.add(rowData);
				}
				String role = (String) session.getAttribute("PARENT_DWBH");
				if (role==null||role.length()==0) {
					
				}else{
					return "loginStop";
				}
				session.setAttribute("userSJ",sj);
				return "loginUser";
			}
		}
		
	}
	*//**
	 * 用户登录
	 * @param request
	 * @param restoIndex
	 * @return
	 *//*
	@RequestMapping("/toIndex")
	public String toIndex(HttpServletRequest request,HttpServletResponse res) {
		HttpSession session = request.getSession();
		if (session.getAttribute("userSJ")==null) {
			return "users/login";
		}
		return "users/index";
	}
	
	*//**
	 * 管理员登陆
	 * @param request
	 * @param res
	 * @return
	 *//*
	@RequestMapping("/toAdminIndex")
	public String toAdminIndex(HttpServletRequest request,HttpServletResponse res) {
		HttpSession session = request.getSession();
		if (session.getAttribute("adminSJ")==null) {
			return "users/login";
		}
		if (!session.getAttribute("adminSJ").equals("17600236100")) {
			return "redirect:toIndex";
		}
		return "users/adminIndex";
	}
	
}
*/