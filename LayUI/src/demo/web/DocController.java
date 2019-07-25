package demo.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletException;
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
import demo.tool.PublicMethod;
import demo.tool.UUIDUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 本类中凡是guid或者Modelguid为"73c2efa3c34f4904ae0eee4ab31dfa79"都属于菜单管理
 * @author BLAME
 *
 */
@Controller
public class DocController {

	private PreparedStatement ps;
	private ResultSet rs;
	private Connection conn;
	private List<Map<String, Object>> list;

	@RequestMapping(value = "findDoc")
	@ResponseBody
	/**
	 * 获取表头
	 * @param page
	 * @param request
	 * @param res
	 * @param guid
	 * @param num
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Object findDoc(PageUtils page, HttpServletRequest request, HttpServletResponse res, String guid, Integer num,Integer  limit)
			throws Exception {
		try {
			HttpSession session = request.getSession();
			final String user = (String) session.getAttribute("user");
			final String role = (String) session.getAttribute("role");
			list = new ArrayList<Map<String, Object>>();
			String tn = null;
			ResultSetMetaData md = null;
			int columnCount = 0;
			String destn = null;
			String sqlRale = null;
			tn= Bmodel.findBmByGuId(guid);//描述表
			destn = tn + "_des";
			String sqlDes = "select zdm,zdmc,width,formtypes from " + destn + " where 1=1 ";
			sqlDes = sqlDes + " and isshow = 1  ";
			if (user != null && role != null) {
				sqlRale = " and sqlrale = \'session(\\'user\\')=\\'" + user + "\\' and session(\\'role\\')=\\'" + role
						+ "\\'' ";
				sqlDes = sqlDes + sqlRale;
			}
			sqlDes = sqlDes + " order by lisnum asc,id asc ";
			conn = LinkSql.getConn();
			ps = LinkSql.Execute(conn,sqlDes,role,destn);
			rs = ps.executeQuery();
			md = rs.getMetaData(); // 获得结果集结构信息,元数据
			columnCount = md.getColumnCount(); // 获得列数
			while (rs.next()) {
				Map<String, Object> rowData = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(md.getColumnName(i), rs.getObject(i));
				}
				list.add(rowData);
			}
		} finally{
			ps.close();
			rs.close();
			conn.close();
		}
		return list;

	}
	/**
	 * 渲染数据表格
	 * @param page
	 * @param request
	 * @param res
	 * @param guid
	 * @param num
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "findDocTable", produces = "text/html;charset=utf-8")
	@ResponseBody
	// 获取表头、表数据
	public Object findDocTable(PageUtils page, HttpServletRequest request, HttpServletResponse res, String guid,String postData, Integer num,Integer  limit)
			throws Exception {
		HttpSession session = request.getSession();
		final String role = (String) session.getAttribute("role");
		list = new ArrayList<Map<String, Object>>();
		String tn = null;
		ResultSetMetaData md = null;
		int columnCount = 0;
		String destn = null;
		tn= Bmodel.findBmByGuId(guid);//描述表
		destn = tn + "_des";
		if (limit==null) {
			limit=10;
		}
		page.setRows(limit);
		list = new ArrayList<Map<String, Object>>();
		String sqlZdmc = " ";
		conn = LinkSql.getConn();
		String sqlzdm = "select zdm from " + destn;
		ps = LinkSql.Execute(conn,sqlzdm,role,destn);
		rs = ps.executeQuery();
		md = rs.getMetaData(); // 获得结果集结构信息,元数据
		columnCount = md.getColumnCount(); // 获得列数
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				sqlZdmc = sqlZdmc + rs.getObject(i) + ",";
			}
		}
		sqlZdmc = sqlZdmc.substring(0, sqlZdmc.length() - 1);
		if (sqlZdmc.length()!=0) {
			String sqlWhere = "";
			if (postData!=null) {
				String tmp[] =postData.split("&");
				for (int i = 0; i < tmp.length; i++) {
					String s = postData.split("&")[i];
					s = s+" ";
					String name = s.split("=")[0];
					String value =s.split("=")[1];
					value=value.trim();
					if (value.length()!=0) {
						if (!value.equals("请选择")) {
							sqlWhere += " and "+name+" like '%"+value+"%' ";
						}
					}
				}
			}
			String sqlData=null;
			sqlData = "select " + sqlZdmc + ",guid from " + tn + " where 1=1 "+sqlWhere;
			ps = LinkSql.Execute(conn,sqlData,role,tn);
			rs = ps.executeQuery();
			md = rs.getMetaData();
			columnCount = md.getColumnCount();
			while (rs.next()) {
				Map<String, Object> rowData = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(md.getColumnName(i), rs.getObject(i));
				}
				list.add(rowData);
			}
			
			//得到总数
			String sqlCount = "select count(*) from " + tn + " where 1=1 "+sqlWhere+" ";
				ps = conn.prepareStatement(sqlCount);
				rs = ps.executeQuery();
			md = rs.getMetaData();
			columnCount = md.getColumnCount();
			String count = null;
			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					count = rs.getObject(i).toString();
				}
			}
			JSONArray json = JSONArray.fromObject(list);
			String js = json.toString();
			String jso = "{\"code\":0,\"msg\":\"\",\"count\":" + count + ",\"data\":" + js + "}";
			return jso;
		}else{
			String jso = "{\"code\":0,\"msg\":\"\",\"count\":" + 0 + ",\"data\":" + null + "}";
			return jso;
		}
		
	}
	
	
	/**
	 * 查询条件
	 * @throws Exception 
	 */
	@RequestMapping(value = "queryCondition")
	@ResponseBody
	public Object queryCondition(HttpServletRequest request, HttpServletResponse res,String guid) throws Exception{
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		try {
			/*
			 * 定义
			 * */
			list = new ArrayList<Map<String, Object>>();//实例化
			String tn  = null;//数据表表名，根据guid获取
			String desTn = null;//描述表表名
			String sqlWhere=  null;//sql语句条件
			ResultSetMetaData md = null;
			int columnCount=0;
			//根据guid获取数据表表名，根据规则得到描述表表名
			tn= Bmodel.findBmByGuId(guid);//描述表
			desTn = tn + "_des";
			
			//连接数据库
			conn = LinkSql.getConn();//创建对象
			sqlWhere =" and  isselect >=1 ORDER BY isselect asc";
			String sqlFind=" SELECT id,zdm,zdmc,initval,types,jsdm,isedit,api,formtypes,guid,isselect,width FROM  "+desTn+" WHERE 1=1"+sqlWhere;
			ps =LinkSql.Execute(conn,sqlFind,role,desTn);
			rs = ps.executeQuery();
			md = rs.getMetaData(); // 获得结果集结构信息,元数据
			columnCount = md.getColumnCount(); // 获得列数
			while (rs.next()) {
				Map<String, Object> rowData = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(md.getColumnName(i), rs.getObject(i));
				}
				list.add(rowData);
			}
		} finally {
			rs.close();
			ps.close();
			conn.close();
		}
		return list;
		
	}
	/**
	 * 去添加数据页面
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toAddDataJsp")
	public void toAddDataJsp(Model model, HttpServletRequest request, HttpServletResponse res, String guid,Integer id,String thisName)
			throws Exception {
			if (id!=null||thisName!=null) {
		        request.setAttribute("id", id);
		        request.setAttribute("parentName", thisName);
			}
		request.getRequestDispatcher("/doc_add.jsp").forward(request, res);
	}

	/**
	 * 去添加数据
	 * 
	 * @param model
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toAddData")
	@ResponseBody
	public Object toAddData(Model model, HttpServletRequest request, HttpServletResponse res, String guid,String guidBmodel)
			throws Exception {
		// 拿到guid,查询模型表表名
		list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData md = null;
		String tn = null;
		int columnCount = 0;
		if (guid==null||guid.equals("")||guid.equals("null")) {
			guid="73c2efa3c34f4904ae0eee4ab31dfa79";
		}
		tn= Bmodel.findBmByGuId(guid);//描述表
		// 获取描述表字段
		rs = PublicMethod.findBmodelField(tn, guid);
		md = rs.getMetaData(); // 获得结果集结构信息,元数据
		columnCount = md.getColumnCount(); // 获得列数
		while (rs.next()) {
			Map<String, Object> rowData = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			if (rs.getObject("jsdm").equals(".")) {
				rowData.put("jsdmFlag", "0");
			}else{
				rowData.put("jsdmFlag", "1");
			}
			list.add(rowData);
		}
		rs.close();
		return list;
	}
	
	/**
	 * 去菜单管理列表页面
	 * @param request
	 * @param response
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toMenu")
	public String toMenu(HttpServletRequest request, HttpServletResponse response,String guid) throws Exception{
		request.setAttribute("guid", "73c2efa3c34f4904ae0eee4ab31dfa79");
		return "menu";
		
	}
	/**
	 * 加载菜单
	 * @param request
	 * @param res
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	static String html = "" ;
	@RequestMapping("toMenuIndex")
	@ResponseBody
	public String toMenuIndex(HttpServletRequest request, HttpServletResponse response,String guid,String flag) throws Exception{
		/*
		 * 定义
		 * */
		html="";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  
		String tn  = null;//数据表表名，根据guid获取
		//根据guid获取数据表表名，根据规则得到描述表表名
		tn= Bmodel.findBmByGuId(guid);//描述表
		try {
			dispalyListConn(tn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "";
		}
		return html;
	}
	
    public static void dispalyListConn(String tn) throws Exception {
        Connection conn = null;
        try {
            conn = LinkSql.getConn();
            displayList(conn,0,0,tn);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
			conn.close();
		}
    }
    /**
     * 递归显示父子级列表
     * @param conn
     * @param pid
     * @param level
     * @param tn
     * @throws Exception
     */
    public static void displayList(Connection conn,int pid,int level,String tn) throws Exception {
			PreparedStatement psta = null;
			ResultSet rs = null;
			if (pid==0) {
				
			}
			String sql = "SELECT id, guid, parentMenu, NAME, url, qx, sort, isshow FROM "+tn+" WHERE parentMenu = ? ORDER BY sort asc";
			psta = conn.prepareStatement(sql);
			psta.setInt(1,pid);
			rs = psta.executeQuery();
			while(rs.next()) {
				//打印输出
				String name=rs.getString("NAME");
				int parentMenu =rs.getInt("parentMenu");
				String guid = rs.getString("guid");
				String url =rs.getString("url");
				if (url==null) {
					url="";
				}
				String qx =rs.getString("qx");
				if (qx==null) {
					qx="";
				}
				int sort =rs.getInt("sort");
				if (sort==0) {
					sort=0;
				}
				String isshow= rs.getString("isshow");
				if (isshow==null) {
					isshow="";
				}
			    html+="<tr id='"+rs.getInt("id")+"' pId='"+parentMenu+"'>";
			    html+="	<td nowrap><i class='icon- hide'></i><a href='toUpdateDoc?guid="+guid+"&guidBmodel='>"+name+"</a></td>";
			    html+="	<td title=''>"+url+"</td>";
			    html+="	<td style='text-align:center;'>";
			    html+="		<input type='hidden' name='ids' value='"+guid+"'/>";	
			    html+="		<input name='sorts' type='text' value='"+sort+"' style='width:50px;margin:0;padding:0;text-align:center;'>";
			    html+="	</td>";
			    html+="	<td style='text-align:center;'>"+isshow+"</td>";
			    html+="	<td title=''>"+qx+"</td>";
			    html+="	<td nowrap>";
			    html+="		<a href='toUpdateDoc?guid="+guid+"&guidBmodel='>修改</a>";
			    html+="		<a href='javascript:deleteDoc(\""+rs.getString("id")+"\",\""+guid+"\",\""+name+"\");' >删除</a>";
			    html+="		<a href='toAddDataJsp?id="+rs.getString("id")+"&thisName="+name+"'>添加下级菜单</a>";
			    html+="	</td>";
			    html+="</tr>";
			    displayList(conn,rs.getInt("id"),level+1,tn);
			}
		
            
    }
	
	/**
	 * 查询菜单管理中上级菜单
	 * @param request
	 * @param res
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("findParentMenu")
	@ResponseBody
	public Object findParentMenu( HttpServletRequest request, HttpServletResponse res,String apiVal,String api,String zdmc,String guid)
			throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		String tn = null;
		int columnCount = 0;
		ResultSetMetaData md = null;
		list = new ArrayList<Map<String, Object>>();
		tn= Bmodel.findBmByGuId(guid);//描述表
		conn =LinkSql.getConn();
		String sqlFindDes ="SELECT id,guid,name FROM "+tn+" WHERE parentMenu=0";
		ps = LinkSql.Execute(conn,sqlFindDes,role,tn);
		rs = ps.executeQuery();
		try {
			md = rs.getMetaData(); // 获得结果集结构信息,元数据
			columnCount = md.getColumnCount(); // 获得列数
			rs.last();// 指针移到最后一条后面
			int rows = rs.getRow();
			if (rows==0) {
				String ex ="empty";
				return ex;
			}
			rs.beforeFirst();
			while (rs.next()) {
				Map<String, Object> rowData = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(md.getColumnName(i), rs.getObject(i));
				}
				list.add(rowData);
			}
		} catch (Exception e) {
			String ex ="error";
			return ex;
		}
		return list;
		
	}
	
	/**
	 * 修改和添加时查询对应字段信息
	 * @param request
	 * @param res
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("findParentMenuDes")
	@ResponseBody
	public Object findParentMenuDes( HttpServletRequest request, HttpServletResponse res,String guid)
			throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		String tn = null;
		int columnCount = 0;
		ResultSetMetaData md = null;
		list = new ArrayList<Map<String, Object>>();
		tn= Bmodel.findBmByGuId(guid);//描述表
		conn =LinkSql.getConn();
		String sqlFindDes ="select zdm,zdmc,width,guid from "+tn+"_des"+" where guid = \""+guid+"\" ";
		ps = LinkSql.Execute(conn,sqlFindDes,role,tn+"_des");
		rs = ps.executeQuery();
		try {
			md = rs.getMetaData(); // 获得结果集结构信息,元数据
			columnCount = md.getColumnCount(); // 获得列数
			while (rs.next()) {
				Map<String, Object> rowData = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(md.getColumnName(i), rs.getObject(i));
				}
				list.add(rowData);
			}
		} catch (Exception e) {
			String ex ="error";
			return ex;
		}
		return list;
	}
	static String flagStr =""; 
	/**
	 * 删除菜单及其子级菜单
	 * 删除父类之前先查询是否存在子级，若存在就先删除子级
	 * @param model
	 * @param guidBmodel
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("doDeleteMenu")
	@ResponseBody
	public Object doDeleteMenu(Integer id,String guid) throws Exception {
		String flag = null;
		try {
			forDeleteConn(id);
			flagStr = flagStr.substring(0,flagStr.length()-1);
			flag="delFinish";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			flag = "delError";
		}
		return flag;
	}
	/**
	 * 递归删除的数据库链接
	 * @throws Exception
	 */
	public void forDeleteConn(Integer id) throws Exception{
		Connection conn = null;
        try {
            conn = LinkSql.getConn();
            forDelete(conn,id);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
           conn.close();
        }
	}
	/**
	 * 递归删除过程
	 * @param conn
	 * @param thisId
	 * @throws SQLException
	 */
	public void forDelete(Connection conn, Integer thisId) throws SQLException {
		PreparedStatement psta = null;
        ResultSet rs = null;
        Integer id = null;
        String sqlDel = "delete from menu where id = ?";
    	psta = conn.prepareStatement(sqlDel);
    	psta.setInt(1, thisId);
    	int flag=0;
		flag = psta.executeUpdate();
		flagStr+=flag+",";
    	if (flag==1) {
    		String sql = "SELECT id, guid, NAME FROM layui.menu WHERE parentMenu = ? ";
            psta = conn.prepareStatement(sql);
            psta.setInt(1,thisId);
            rs = psta.executeQuery();
            while(rs.next()) {
            	id = rs.getInt("id");
            	forDelete(conn,id);
            }
		}
	}
	/**
	 * 查找JSDM包括级联查询操作
	 * 
	 * @param model
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("findJSDM")
	@ResponseBody
	public Object findJSDM( HttpServletRequest request, HttpServletResponse res,String apiVal,String api,String zdmc,String guid)
			throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
				String tn = null;
				String jsdm = null;
				String sqlWhere = null;
				int columnCount = 0;
				ResultSetMetaData md = null;
				list = new ArrayList<Map<String, Object>>();
				tn= Bmodel.findBmByGuId(guid);//描述表
				conn =LinkSql.getConn();
				String sqlFindDes ="select jsdm from "+tn+"_des"+" where zdm = \""+zdmc+"\" and guid = \""+guid+"\" ";
				ps = LinkSql.Execute(conn,sqlFindDes,role,tn+"_des");
				rs = ps.executeQuery();
				while(rs!=null && rs.next()){
					int i = rs.getRow();
					jsdm = rs.getString(i);//获取当前记录的第1列数据
				}
				try {
					String sql =null;
					String selectFrom = "";
					String[] parts = jsdm.split("[|]");
					
					int index = 3;//因为根据jsdm源字段在以竖线分割后的第三个下标下(下标从零开始)
					int jsdmFlag = Integer.parseInt(parts[0]);//得到第一位为jsdm级联数量
					for (int i = 0; i <jsdmFlag ; i++) {
						selectFrom += ""+parts[index]+",";
						index+=2;
					}
					selectFrom = selectFrom.substring(0, selectFrom.length() - 1);// 数据表字段
					int sub = jsdm.lastIndexOf("||");
					if (sub ==-1) {
						sqlWhere = " ";
					}else{
						api= jsdm.substring(sub+2);
						sqlWhere =" and "+api+"=\""+apiVal+"\"   ";
					}
					sql = "select distinct "+selectFrom+" from "+parts[1]+"."+parts[2]+" where  1=1 "+ sqlWhere +"";
					ps =  LinkSql.Execute(conn,sql,role,parts[2]);
				} catch (Exception e1) {
					String ex1 ="errorJSDM";
					return ex1;
				}
				try {
					rs = ps.executeQuery();
					md = rs.getMetaData(); // 获得结果集结构信息,元数据
					columnCount = md.getColumnCount(); // 获得列数
					while (rs.next()) {
						Map<String, Object> rowData = new HashMap<String, Object>();
						for (int i = 1; i <= columnCount; i++) {
							rowData.put(md.getColumnName(i), rs.getObject(i));
						}
						list.add(rowData);
					}
				} catch (Exception e) {
					String ex ="error";
					return ex;
				}finally {
					rs.close();
					ps.close();
					conn.close();
				}
			return list;
	}
	
	/**
	 * 返回JSDM对应字段
	 * @param request
	 * @param res
	 * @param zdmc
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("findJSDMZdm")
	@ResponseBody
	public Object findJSDMZdm( HttpServletRequest request, HttpServletResponse res,String zdmc,String guid)
			throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
				String tn = null;
				String jsdm = null;
				list = new ArrayList<Map<String, Object>>();
				tn= Bmodel.findBmByGuId(guid);//描述表
				conn =LinkSql.getConn();
				String sqlFindDes ="select jsdm from "+tn+"_des"+" where zdm = \""+zdmc+"\" and guid = \""+guid+"\"";
				ps =  LinkSql.Execute(conn,sqlFindDes,role,tn+"_des");
				rs = ps.executeQuery();
				while(rs!=null && rs.next()){
					int i = rs.getRow();
					jsdm = rs.getString(i);//获取当前记录的第1列数据
				}
				String jsdmVal = "";
				String fromVal = "";
				int jsdmFlag = 0;
				int index = 0;
				String[] parts =null;
				List val = new ArrayList<>();
				try {
					parts = jsdm.split("[|]");//分割字符串
					jsdmFlag = Integer.parseInt(parts[0]);//得到数量
					index = 3;//定位下标
					for (int i = 0; i <jsdmFlag ; i++) {
						jsdmVal += ""+parts[index]+",";//拼接源字段
						index+=2;
					}
					jsdmVal = jsdmVal.substring(0, jsdmVal.length() - 1);// 数据表字段
					parts = jsdm.split("[|]");
					jsdmFlag = Integer.parseInt(parts[0]);
					index = 4;
					for (int i = 0; i <jsdmFlag ; i++) {
						fromVal += ""+parts[index]+",";
						index+=2;
					}
					fromVal = fromVal.substring(0, fromVal.length() - 1);// 数据表字段
					val.add(0, jsdmVal);//JSDM中 源表的字段
					val.add(1, fromVal);//JSDM中 目标表字段
				} catch (Exception e1) {
					String ex1 ="errorJSDM";
					return ex1;
				}finally {
					rs.close();
					ps.close();
					conn.close();
				}
			return val;
	}
	/**
	 * 添加数据
	 * 
	 * @param model
	 * @param guid
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("doc_doAdd")
	public void boc_doAdd(Model model, String guid, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		guid = request.getParameter("guid");
		String dataName = "";
		String destn = "";
		String dataTname = null;
		ResultSetMetaData md = null;
		int columnCount = 0;
		String name = null;
		Enumeration pNames = request.getParameterNames();
		if (guid==null||guid.equals("")||guid.equals("null")) {
			guid="73c2efa3c34f4904ae0eee4ab31dfa79";
		}
			dataName =Bmodel.findBmByGuId(guid);//描述表
			dataTname =Bmodel.findBmcBybm(dataName);
		
		// 获取数据表中字段
		list = new ArrayList<Map<String, Object>>();
		String sqlZdmc = "";
		destn = dataName + "_des";
		rs = PublicMethod.findBmodelByZdm(destn);
		md = rs.getMetaData(); // 获得结果集结构信息,元数据
		columnCount = md.getColumnCount(); // 获得列数
		// 循环出字段名
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				sqlZdmc = sqlZdmc + rs.getObject(i) + ",";

			}
		}
		rs.last();// 指针移到最后一条后面
		int rows = rs.getRow();
		String value = "";
		// 循环出字段对应值
		for (int i = 0; i < rows; i++) {
			value += "? ,";
		}

		value = value.substring(0, value.length() - 1);// 数据表字段对应值
		sqlZdmc = sqlZdmc.substring(0, sqlZdmc.length() - 1);// 数据表字段
		guid = UUIDUtil.getUUID();
		String sql = "INSERT INTO " + dataName + "(" + sqlZdmc + ",guid)VALUES(" + value + ",\'" + guid + "\')";
		conn= LinkSql.getConn();
		conn.setAutoCommit(false);
		String flag = null;
		try {
			ps = LinkSql.Execute(conn,sql,role,dataName);
			for (int e = 1; e <= rows; e++) {
				while (pNames.hasMoreElements()) {
					name = (String) pNames.nextElement();
					if (name.equals("file")) {
						System.out.println(request.getParameter("imgUrls"));
						ps.setString(e, request.getParameter("imgUrls"));
					}else{
						if (request.getParameter(name) == "") {
							
							if (name.equals("parentMenu")) {
								ps.setInt(e, 0);
							}else{
								ps.setString(e, null);
							}
							
						} else {
							ps.setString(e, request.getParameter(name));
						}
					}
					
					break;
				}
			}
			ps.executeUpdate();
			conn.commit();
			if (dataName.equals("menu")) {
				flag = "addFinish";
				request.getRequestDispatcher("/toMenu").forward(request,
						response);
			}else{
				flag = "addFinish";
				request.getRequestDispatcher("/doc_Index.jsp?flag=" + flag + "&bmc=" + dataTname).forward(request,
						response);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
				conn.rollback();
				flag = "addError";
				request.getRequestDispatcher("/doc_add.jsp?flag=" + flag).forward(request, response);
		}finally {
			rs.close();
			ps.close();
			conn.close();
		}
	}

	/**
	 * 删除数据信息
	 * 
	 * @param model
	 * @param guidBmodel
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("deleteDoc")
	@ResponseBody
	public Object deleteDoc(HttpServletRequest request,Model model, String guidBmodel, String guid) throws Exception {
		String bmodelName = null;
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		String flag = null;
		bmodelName = Bmodel.findBmByGuId(guidBmodel);//描述表
		conn = LinkSql.getConn();
		conn.setAutoCommit(false);
		String sql = "DELETE FROM " + bmodelName + " WHERE guid= \'" + guid + "\'";
		try {
			ps = LinkSql.Execute(conn,sql,role,bmodelName);
			ps.executeUpdate();
			conn.commit();
			flag = "delFinish";
		} catch (Exception e) {
			conn.rollback();
			flag = "delError";
		}
		return flag;
	}

	/**
	 * 去修改页面
	 * 
	 * @param request
	 * @param res
	 * @param guid
	 * @param guidBmodel
	 * @throws Exception
	 */
	@RequestMapping("toUpdateDoc")
	public void toUpdate(HttpServletRequest request, HttpServletResponse res, String guid, String guidBmodel)
			throws Exception {
		request.getRequestDispatcher("/doc_edit.jsp").forward(request, res);
	}

	/**
	 * 修改页面初始化； 添加表单 回显数据
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toUpdateData")
	@ResponseBody
	public Object toUpdateData(Model model, HttpServletRequest request, HttpServletResponse res, String guid,
			String guidBmodel) throws Exception {
		// 拿到guid,查询模型表表名
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		conn = LinkSql.getConn();
		list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new TreeMap<String, Object>();
		ResultSetMetaData md = null;
		String tn = null;
		int columnCount = 0;
		if (guidBmodel==null||guidBmodel.equals("")) {
			guidBmodel="73c2efa3c34f4904ae0eee4ab31dfa79";
		}
		//获取表名
		
		tn = Bmodel.findBmByGuId(guidBmodel);
		// 获取数据表中字段
		String sqlZdmc = "";
		String destn = tn + "_des";
		rs = PublicMethod.findBmodelByZdm(destn);
		md = rs.getMetaData(); // 获得结果集结构信息,元数据
		columnCount = md.getColumnCount(); // 获得列数
		// 循环出字段名
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				sqlZdmc = sqlZdmc + rs.getObject(i) + ",";

			}
		}
		sqlZdmc = sqlZdmc.substring(0, sqlZdmc.length() - 1);
		String sqlData = "select " + sqlZdmc + ",guid from " + tn + " where guid = \'"+guid+"\' ";
		ps = LinkSql.Execute(conn,sqlData,role,tn);
		rs = ps.executeQuery();
		md = rs.getMetaData();
		columnCount = md.getColumnCount();
		int parentId = 0;
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				if (md.getColumnName(i).equals("parentMenu")) {
					if (rs.getObject(i)!=null) {
						parentId = Integer.parseInt(rs.getObject(i).toString());
					}
					
				}
				map.put(md.getColumnName(i), rs.getObject(i));
			}
		}
		if (parentId!=0) {
			String sqlFindDes ="SELECT name FROM "+tn+" WHERE id="+parentId+"";
			ps = LinkSql.Execute(conn,sqlFindDes,role,tn);
			rs = ps.executeQuery();
			String parentName = null;
			md = rs.getMetaData();
			Integer cc = md.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= cc; i++) {
					parentName = rs.getObject(i).toString();
				}
			}
			map.put("parentName",parentName);
		}
		rs.close();
		ps.close();
		conn.close();
		
		return map;
	}
	/**
	 * 返回表单字段
	 * @param model
	 * @param request
	 * @param res
	 * @param guid
	 * @param guidBmodel
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("returnZdmList")
	@ResponseBody
	public Object returnZdmList(Model model, HttpServletRequest request, HttpServletResponse res, String guid,
			String guidBmodel) throws Exception {
		// 获取描述表字段
		list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData md = null;
		String tn = null;
		int columnCount = 0;
		if (guidBmodel==null||guidBmodel.equals("")) {
			guidBmodel="73c2efa3c34f4904ae0eee4ab31dfa79";
		}
		tn = Bmodel.findBmByGuId(guidBmodel);
		// 获取数据表中字段
		rs = PublicMethod.findBmodelField(tn, guidBmodel);
		md = rs.getMetaData(); // 获得结果集结构信息,元数据
		columnCount = md.getColumnCount(); // 获得列数
		while (rs.next()) {
			Map<String, Object> rowData = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
				if (rs.getObject("jsdm").equals(".")) {
					rowData.put("jsdmFlag", "0");
				}else{
					rowData.put("jsdmFlag", "1");
				}
			}
			list.add(rowData);
		}
		rs.close();
		ps.close();
		conn.close();
		return list;
	}
	/**
	 * 从主页更改排序顺序
	 * @param ids
	 * @param sorts
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("updateSort")
	public void updateSort(String ids,String sorts, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		String tn = Bmodel.findBmByGuId("73c2efa3c34f4904ae0eee4ab31dfa79");
		conn =LinkSql.getConn();
		conn.setAutoCommit(false);
		if (ids!=null||sorts!=null) {
			String[] result = ids.split(",");
			String[] s = sorts.split(",");
			for (int i = 0; i < result.length; i++) {
				String sortStr=s[i];
				if (sortStr==null||sortStr.equals("")) {
					sortStr="null";
				}else{
					sortStr="'"+s[i]+"'";
				}
				String sql = "UPDATE "+tn+" SET sort="+sortStr+" WHERE guid='"+result[i]+"'";
				try {
					ps = LinkSql.Execute(conn,sql,role,tn);
					int flag = ps.executeUpdate();
					conn.commit();
					if (flag!=1) {
						conn.rollback();
						String flagStr = "editError";
						request.getRequestDispatcher("/toMenu").forward(request,
								response);
					}
				} catch (Exception e) {
					conn.rollback();
				}
			}
		}
		
		String flagStr = "editFinish";
		request.getRequestDispatcher("/toMenu").forward(request,
				response);
	}
	/**
	 * 做修改操作
	 * @param model
	 * @param guid
	 * @param guidBmodel
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("doc_doEdit")
	public void doc_doEdit(Model model, String guid,String guidBmodel, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		guidBmodel = request.getParameter("guidBmodel");//描述表GUID;做查询表名和字段名操作
		guid =request.getParameter("guid"); //数据表GUID;做修改数据操作
		String dataName = "";
		String destn = "";
		String dataTname = null;
		ResultSetMetaData md = null;
		int columnCount = 0;
		String name = null;
		Enumeration pNames = request.getParameterNames();
		if (guidBmodel==null||guidBmodel.equals("")) {
			guidBmodel="73c2efa3c34f4904ae0eee4ab31dfa79";
		}
			dataName = Bmodel.findBmByGuId(guidBmodel);
			dataTname = Bmodel.findBmcBybm(dataName);
		// 获取数据表中字段
		list = new ArrayList<Map<String, Object>>();
		String sqlZdmc = "";
		destn = dataName + "_des";
		rs = PublicMethod.findBmodelByZdm(destn);
		md = rs.getMetaData(); // 获得结果集结构信息,元数据
		columnCount = md.getColumnCount(); // 获得列数
		// 循环出字段名
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				sqlZdmc = sqlZdmc + rs.getObject(i) + "=?,";
			}
		}
		conn =LinkSql.getConn();
		conn.setAutoCommit(false);
		rs.last();// 指针移到最后一条后面
		int rows = rs.getRow();
		sqlZdmc = sqlZdmc.substring(0, sqlZdmc.length() - 1);// 数据表字段
		String sql = "UPDATE "+dataName+" SET "+sqlZdmc+" WHERE guid =\'"+guid+"\'";
		ps = LinkSql.Execute(conn,sql,role,dataName);
		for (int e = 1; e <= rows; e++) {
			while (pNames.hasMoreElements()) {
				name = (String) pNames.nextElement();
				if (request.getParameter(name) == "") {
					ps.setString(e, null);
				} else {
					ps.setString(e, request.getParameter(name));
				}
				break;
			}
		}
		String flag = null;
		try {
			ps.executeUpdate();
			conn.commit();
			if (dataName.equals("menu")) {
				flag = "editFinish";
				request.getRequestDispatcher("/toMenu").forward(request,
						response);
			}else{
				flag = "editFinish";
				request.getRequestDispatcher("/doc_Index.jsp?flag=" + flag + "&bmc=" + dataTname +"&guidBmodel="+ guid+" &guid="+guidBmodel).forward(request,
						response);
			}
		} catch (Exception e) {
			conn.rollback();
			flag = "editError";
			request.getRequestDispatcher("/doc_edit.jsp?flag=" + flag + "&bmc=" + dataTname +" &guidBmodel="+guid +" &guid="+guidBmodel).forward(request,
					response);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 角色页面
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping("toRuleIndex")
	public void toRoleIndex(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String guid="acc0357dd46e42129f534f4820781a5a";
		String bmc = "角色";
		request.setAttribute("guid", guid);
		request.setAttribute("bmc", bmc);
		request.getRequestDispatcher("/rule_Index.jsp?guid=" + guid + "&bmc=" + bmc ).forward(request,
				response);
	}
	/**
	 * 角色页面
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping("findRoleList")
	@ResponseBody
	public String findRoleList(HttpServletRequest request,HttpServletResponse response,String guid,Integer total) throws Exception{
		list = new ArrayList<Map<String, Object>>();
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		String tn = null;
		ResultSetMetaData md = null;
		int columnCount = 0;
		String destn = null;
		guid ="acc0357dd46e42129f534f4820781a5a";
		tn = Bmodel.findBmByGuId(guid);
		destn = tn + "_des";
		list = new ArrayList<Map<String, Object>>();
		String sqlZdmc = " ";
		conn = LinkSql.getConn();
		conn.setAutoCommit(false);
		String sqlzdm = "select zdm from " + destn;
		ps = LinkSql.Execute(conn,sqlzdm,role,destn);
		rs = ps.executeQuery();
		md = rs.getMetaData(); // 获得结果集结构信息,元数据
		columnCount = md.getColumnCount(); // 获得列数
		//得到分页数据
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				sqlZdmc = sqlZdmc + rs.getObject(i) + ",";
			}
		}
		sqlZdmc = sqlZdmc.substring(0, sqlZdmc.length() - 1);
		String sqlWhere = "";
		String sqlData=null;
		sqlData = "select guid,englishName,roleName,ssjg from " + tn + " where 1=1 "+sqlWhere;
		try {
			ps =  LinkSql.Execute(conn,sqlData,role,tn);
			rs = ps.executeQuery();
		} catch (Exception e) {
			conn.rollback();
		}
		md = rs.getMetaData();
		columnCount = md.getColumnCount();
		while (rs.next()) {
			Map<String, Object> rowData = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);
		}
		
		//得到总数
		String sqlCount = "select count(*) from " + tn + " where 1=1 "+sqlWhere+" ";
		try {
			ps =   LinkSql.Execute(conn,sqlCount,role,tn);
			rs = ps.executeQuery();
		} catch (Exception e) {
			conn.rollback();
		}
		md = rs.getMetaData();
		columnCount = md.getColumnCount();
		String count = null;
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				count = rs.getObject(i).toString();
			}
		}
		JSONArray json = JSONArray.fromObject(list);
		String js = json.toString();
		String jso = "{\"code\":0,\"msg\":\"\",\"count\":" + count + ",\"data\":" + js + "}";
		return jso;
	}
	
	/**
	 * 角色页面
	 * @throws IOException 
	 * @throws ServletException 
	 */
	static List<Object> listTree = new ArrayList<>();
	@RequestMapping("findTreeList")
	@ResponseBody
	public JSONArray findTreeList(HttpServletRequest request,HttpServletResponse response,String roleMenuGuid) throws Exception{
		listTree=new ArrayList<>();
		PreparedStatement psta = null;
        ResultSet rs = null;
        String tn = Bmodel.findBmByGuId("5daf30f7153b4638ab306cc5ba6903a0");
        String sql ="SELECT menuGuids FROM  "+tn+" WHERE roleGuid = \'"+roleMenuGuid+"\'";
        conn = LinkSql.getConn();
        conn.setAutoCommit(false);
        psta = conn.prepareStatement(sql);
        rs = psta.executeQuery();
        String menuGuids=  "";
        while(rs.next()) {
        	menuGuids=rs.getString("menuGuids");
        }

        List<Object> treeList= findTypeTree(menuGuids);
		JSONArray json = JSONArray.fromObject(treeList);
		return json;
	}
	
	public  List<Object> findTypeTree(String menuGuids) throws  Exception {
		PreparedStatement psta = null;
        ResultSet rs = null;
        String sql ="SELECT * FROM menu";
        conn = LinkSql.getConn();
        psta = conn.prepareStatement(sql);
        rs = psta.executeQuery();
        String [] result = null;
        int len = 0;
        if (menuGuids!=null) {
        	result = menuGuids.split(",");
        	len=result.length;
		}else{
			len=0;
		}
        while(rs.next()) {
        	String name=rs.getString("NAME");
        	int parentMenu =rs.getInt("parentMenu");
        	String guid = rs.getString("guid");
        	int id  = rs.getInt("id");
        	if(parentMenu==0){//判断是否是一级菜单
               JSONObject treeObject = new JSONObject();
               for(int a = 0;a<len;a++){
            	   if(guid.equals(result[a])){
            		   treeObject.put("checked", true);
            	   }
               }
                treeObject.put("spread", true);//是否直接展开
                treeObject.put("id", id);
                treeObject.put("guid", guid);
                treeObject.put("title", name);//tree的节点名称
                treeObject.put("children", getChildren(id,menuGuids));//孩子节点，递归遍历
                listTree.add(treeObject);
          }
        }
        conn.close();
        return listTree;
    }
	//获取树形图子类
    public static List<Object> getChildren(Integer parentId,String menuGuids) throws Exception{
    	Connection conn =LinkSql.getConn();
		PreparedStatement psta = null;
		List<Object> listChildren = new ArrayList<>();
        ResultSet rs = null;
        String sql ="SELECT * FROM menu where parentMenu=?";
        psta = conn.prepareStatement(sql);
        psta.setInt(1, parentId);
        rs = psta.executeQuery();
        String [] result = null;
        int len = 0;
        if (menuGuids!=null) {
        	result = menuGuids.split(",");
        	len=result.length;
		}else{
			len=0;
		}
        while(rs.next()) {
        	String name=rs.getString("NAME");
        	int parentMenu =rs.getInt("parentMenu");
        	String guid = rs.getString("guid");
        	int id  = rs.getInt("id");
            if(parentMenu==parentId){//判断是否是一级菜单
               JSONObject treeObject = new JSONObject();
               for(int a = 0;a<len;a++){
            	   if(guid.equals(result[a])){
            		   treeObject.put("checked", true);
            	   }
               }
                treeObject.put("id", id);
                treeObject.put("spread", true);//是否直接展开
                treeObject.put("guid", guid);
                treeObject.put("title", name);//tree的节点名称
                treeObject.put("children", getChildren(id,menuGuids));//孩子节点，递归遍历
                listChildren.add(treeObject);
            }
        }
        return listChildren;
    }
    
    /**
	 * 权限页面
	 * 页面返回值是	guidRole功能编号
	 *			guidMenu角色编号
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping("findRuleList")
	@ResponseBody
	public String findRuleList(HttpServletRequest request,HttpServletResponse response,String guidRole,String guidMenu) throws Exception{
		list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData md = null;
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		int columnCount = 0;
		String guid ="5855c929a66f4f39afafbe5623788837";//为描述表中编号
		String tn =Bmodel.findBmByGuId(guid);
		conn = LinkSql.getConn();
		conn.setAutoCommit(false);
		String sqlWhere = " and roleGuid = \'"+guidRole+"\' AND menuGuid=\'"+guidMenu+"\'";
		String sqlData=null;
		sqlData = "SELECT rules FROM "+tn+" WHERE 1=1 "+sqlWhere;
		try {
			ps = LinkSql.Execute(conn,sqlData,role,tn);
			rs = ps.executeQuery();
		} catch (Exception e) {
			conn.rollback();
		}
		md = rs.getMetaData();
		columnCount = md.getColumnCount();
		String rules = null;
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				rules = rs.getString("rules");
			}
		}
		return rules;
	}
	 /**
	 * 获取全部菜单编号
	 * 	用于全选时使用
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping("findAllMenu")
	@ResponseBody
	public String findAllMenu(HttpServletRequest request,HttpServletResponse response,String guidRole,String guidMenu) throws Exception{
		list = new ArrayList<Map<String, Object>>();
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		ResultSetMetaData md = null;
		int columnCount = 0;
		String guid ="73c2efa3c34f4904ae0eee4ab31dfa79";//为描述表中编号   菜单表
		String tn =Bmodel.findBmByGuId(guid);
		conn = LinkSql.getConn();
		String sqlData=null;
		sqlData = "SELECT id FROM "+tn+" WHERE 1=1 ";
		try {
			ps = LinkSql.Execute(conn,sqlData,role,tn);
			rs = ps.executeQuery();
		} catch (Exception e) {
			return "null";
		}
		md = rs.getMetaData();
		columnCount = md.getColumnCount();
		String ids = "";
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				ids += rs.getString("id")+",";
			}
		}
		ids = ids.substring(0,ids.length() - 1);
		return ids;
	}
	
	
	 /**
		 * 修改或者新增权限
		 * 页面返回值是	guidRole功能编号
		 *			guidMenu角色编号
		 * 如果存在当前功能和角色的编号就进行修改操作
		 * 不存在则相反
		 * @throws IOException 
		 * @throws ServletException 
		 */
		@RequestMapping("inOrUpRule")
		@ResponseBody
		public String inOrUpRule(HttpServletRequest request,HttpServletResponse response,String guidRole,String guidMenu,String chapterstr,String checkedData) throws Exception{
			list = new ArrayList<Map<String, Object>>();
			HttpSession session = request.getSession();
			String role = session.getAttribute("role").toString();
			String guid ="5855c929a66f4f39afafbe5623788837";//为描述表中编号
			String tn = Bmodel.findBmByGuId(guid);
			conn = LinkSql.getConn();
			conn.setAutoCommit(false);
			String sqlWhere = " and roleGuid = \'"+guidRole+"\' AND menuGuid=\'"+guidMenu+"\'";
			String sqlData=null;
			sqlData = "SELECT rules FROM "+tn+" WHERE 1=1 "+sqlWhere;
			String flagStr = null;
			try {
				ps =  LinkSql.Execute(conn,sqlData,role,tn);
				rs = ps.executeQuery();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "-404";
			}
			rs.last();
			int length = rs.getRow();
			if (length==1) {//存在，需要修改当前编号的数据
				int flag = 0;
				String sqlWhereUpdate = " and roleGuid = \'"+guidRole+"\' AND menuGuid=\'"+guidMenu+"\'";
				String sqlDataUpdate = "UPDATE "+tn+" SET rules='"+chapterstr+"' WHERE 1=1 "+sqlWhereUpdate;
				try {
					ps = LinkSql.Execute(conn,sqlDataUpdate,role,tn);
					flag = ps.executeUpdate();
					conn.commit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					conn.rollback();
					return "-404";
				}
				if (flag==1) {
					flagStr = "editAdd";
				}else{
					flagStr = "editError";
				}
			}else{
				int flag =0;
				String guidRoleRule = UUIDUtil.getUUID();
				String sqlDataUpdate = "INSERT INTO "+tn+" ( guid, roleGuid, menuGuid, rules) VALUES ('"+guidRoleRule+"', '"+guidRole+"', '"+guidMenu+"', '"+chapterstr+"')";
				try {
					ps = LinkSql.Execute(conn,sqlDataUpdate,role,tn);
					flag = ps.executeUpdate();
					conn.commit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					conn.rollback();
					return "-404";
				}
				if (flag==1) {
					flagStr = "editAdd";
				}else{
					flagStr = "editError";
				}
			}
			
			
			
			return flagStr;
		}
		
		
		
		/**
		 * 赋予全部权限
		 * @throws IOException 
		 * @throws ServletException 
		 */
		static String mguid="";
		@RequestMapping("addAllRule")
		@ResponseBody
		public String addAllRule(HttpServletRequest request,HttpServletResponse response,String roleGuid,String jsons) throws Exception{
			list = new ArrayList<Map<String, Object>>();
			HttpSession session = request.getSession();
			String role = session.getAttribute("role").toString();
			String guid ="5855c929a66f4f39afafbe5623788837";//为描述表中编号
			String rules = "INSERT,EDIT,ALLDELETE,SELECT,IMPORT,EXPORT,DELETE";
			String tn = Bmodel.findBmByGuId(guid);
			conn = LinkSql.getConn();
			conn.setAutoCommit(false);
			String sqlWhere = " and roleGuid = \'"+roleGuid+"\'";
			String sqlData=null;
			sqlData = "SELECT roleGuid FROM "+tn+" WHERE 1=1 "+sqlWhere;
			Boolean flagStr = true;
			try {
				ps = LinkSql.Execute(conn,sqlData,role,tn);
				rs = ps.executeQuery();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "-404";
			}
			rs.last();
			int length = rs.getRow();
			if (length!=0) {//存在，需要修改当前编号的数据
				rs.beforeFirst();
				int flag = 0;
				String rg = null;
				while (rs.next()) {
					rg = rs.getString("roleGuid");
				}
				String sqlWhereUpdate = " and roleGuid = \'"+rg+"\'";
				String sqlDataUpdate = "delete from "+tn+"  WHERE 1=1 "+sqlWhereUpdate;
				try {
					ps = LinkSql.Execute(conn,sqlDataUpdate,role,tn);
					flag = ps.executeUpdate();
					conn.commit();
				} catch (Exception e) {
					conn.rollback();
				}
				if (flag!=0) {
					flagStr = true;
				}else{
					flagStr = false;
				}
			}
			findAllSonMenu("0");
			mguid = mguid.substring(0,mguid.length());
			String[] guids = mguid.split(",");
			for (int i = 0; i < guids.length; i++) {
				int flag =0;
				String guidRoleRule = UUIDUtil.getUUID();
				String sqlDataUpdate = "INSERT INTO "+tn+" ( guid, roleGuid, menuGuid, rules ) VALUES ('"+guidRoleRule+"', '"+roleGuid+"', '"+guids[i]+"', '"+rules+"')";
				try {
					ps = LinkSql.Execute(conn,sqlDataUpdate,role,tn);
					flag = ps.executeUpdate();
					conn.commit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					conn.rollback();
					return "-404";
				}
				if (flag==1) {
					flagStr = true;
				}else{
					flagStr = false;
				}
			}
			if (flagStr) {
				return "finish";
			}else{
				return "error";
			}
			
		}
		
		public  void findAllSonMenu(String parent) throws Exception{
			conn = LinkSql.getConn();
			String sqlWhere = " and parentMenu!="+parent;
			String sqlData=null;
			sqlData = "SELECT id,guid FROM menu WHERE 1=1 "+sqlWhere;
			ps = conn.prepareStatement(sqlData);
			ResultSet rsSon = ps.executeQuery();
			int id =0;
			String guid =null;
			while(rsSon.next()){
				id = rsSon.getInt("id");
				guid = rsSon.getString("guid");
				String sqlWhereRs = " and parentMenu="+id+"";
				String sqlDataRs=null;
				sqlDataRs = "SELECT id,guid FROM menu WHERE 1=1 "+sqlWhereRs;
				ps = conn.prepareStatement(sqlDataRs);
				ResultSet resultSet = ps.executeQuery();
				if (resultSet.next()) {
					while(resultSet.next()){
						Integer idSon = resultSet.getInt("id") ;
						findMenuLast(idSon);
					}
				}else{
					mguid+=guid+",";
				}
			}
		}
		private  void findMenuLast(Integer parent) throws Exception {
			// TODO Auto-generated method stub
			conn = LinkSql.getConn();
			String sqlWhere = " and parentMenu="+parent+"";
			String sqlData=null;
			sqlData = "SELECT id,guid,parentMenu FROM menu WHERE 1=1 "+sqlWhere;
			ps = conn.prepareStatement(sqlData);
			ResultSet rsLast = ps.executeQuery();
			int id=0;
			String guid = null;
			while(rsLast.next()){
				guid = rsLast.getString("guid");
				id = rsLast.getInt("id");
				String sqlWhereRs = " and parentMenu="+id;
				String sqlDataRs=null;
				sqlDataRs = "SELECT id,guid FROM menu WHERE 1=1 "+sqlWhereRs;
				ps = conn.prepareStatement(sqlDataRs);
				ResultSet resultSet = ps.executeQuery();
				if (resultSet.next()) {
					while(resultSet.next()){
						findMenuLast(resultSet.getInt("id"));
					}
				}else{
					mguid+=guid+",";
				}
			}
		}
		
		
		/**
		 * 在给角色添加完权限，在从本表中添加功能
		 * 在添加过程中，判断rule表中是否存在有不属于当前角色的权限设置
		 * 如果有，进行删除方法
		 * @param roleMenuGuid
		 * @param menuGuid
		 * @return
		 * @throws Exception
		 */
		@RequestMapping("inOrUpMenu")
		@ResponseBody
		public String inOrUpMenu(HttpServletRequest request,String roleMenuGuid,String menuGuid,String jsons) throws Exception{
			list = new ArrayList<Map<String, Object>>();
			HttpSession session = request.getSession();
			String role = session.getAttribute("role").toString();
			String guid ="5daf30f7153b4638ab306cc5ba6903a0";//为描述表中编号
			String tn = Bmodel.findBmByGuId(guid);
			conn = LinkSql.getConn();
			conn.setAutoCommit(false);
			String sqlWhere = " and roleGuid = \'"+roleMenuGuid+"\' ";
			String sqlData=null;
			sqlData = "SELECT roleGuid FROM "+tn+" WHERE 1=1 "+sqlWhere;
			String flagStr = null;
			try {
				//查询当前角色对应哪些菜单
				ps = LinkSql.Execute(conn,sqlData,role,tn);
				rs = ps.executeQuery();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "-404";
			}
			rs.last();
			int length = rs.getRow();
			menuGuid = menuGuid.substring(0,menuGuid.length() - 1);
			if (length==1) {//存在，需要修改当前编号的数据
				int flag = 0;
				String sqlDataUpdate = "UPDATE "+tn+" SET menuGuids='"+menuGuid+"',jsons='"+jsons+"' WHERE 1=1 "+sqlWhere;
				try {
					//如果存在进行修改当前角色中的菜单
					ps = LinkSql.Execute(conn,sqlDataUpdate,role,tn);
					flag = ps.executeUpdate();
					conn.commit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					conn.rollback();
					flagStr = "editError";
				}
				if (flag==1) {
					flagStr = "editAdd";
				}else{
					flagStr = "editError";
				}
			}else{
				int flag =0;
				String guidRoleMenu = UUIDUtil.getUUID();
				String sqlDataUpdate = "INSERT INTO "+tn+" (guid, roleGuid, menuGuids,jsons ) VALUES ( '"+guidRoleMenu+"', '"+roleMenuGuid+"', '"+menuGuid+"','"+jsons+"' );";
				try {
					//未找到当前角色的对应菜单，进行添加操作
					ps =LinkSql.Execute(conn,sqlDataUpdate,role,tn);
					flag = ps.executeUpdate();
					conn.commit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					conn.rollback();
					return "-404";
				}
				if (flag==1) {
					flagStr = "editAdd";
				}else{
					flagStr = "editError";
				}
			}
			
			return flagStr;
		}
}




