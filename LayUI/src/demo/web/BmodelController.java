package demo.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import demo.dao.Bmodel;
import demo.tool.LinkSql;
import demo.tool.PageUtils;
import demo.tool.UUIDUtil;
import net.sf.json.JSONArray;
/**
 * 开发系统中的模型表的Conntroller层
 * @author BLAME
 *
 */
@Controller
public class BmodelController {
	private PreparedStatement ps =null;
	private ResultSet rs =null;
	private Connection conn =null;
	/**
	 * 查询出模型表中数据，以json格式传入前台
	 * @param page
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "bmodel_findAll", produces = "text/html;charset=utf-8")
	@ResponseBody
	public Object findAll(PageUtils page,Integer limit,HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		try {
			if (limit==null) {
				limit=10;
			}
			page.setRows(limit);
			String tn =  LinkSql.systemName;
			List desList = new ArrayList();
			String sqlJoint = " ORDER BY orders asc    limit ?,? ";
			String sql = "SELECT guid,bmc,bm,orders from "+tn+" ";
			sql =sql+sqlJoint;
			//_______________________________
			conn =LinkSql.getConn();
			ps =LinkSql.Execute(conn,sql,role,tn);
			ps.setInt(1, page.getStart());
			ps.setInt(2, page.getRows());
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
			String sqlCount = "SELECT count(guid) FROM "+LinkSql.systemName+" WHERE 1=1 ";
			conn = LinkSql.getConn();
			ps =LinkSql.Execute(conn,sqlCount,role,LinkSql.systemName);
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
		}finally {
			LinkSql.close(ps, conn);
		}
	}
	/**
	 * 进入首页功能
	 * @param bzdk
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("bmodel_toIndex")
	public String toIndex() throws Exception {
		return "bmodel_Index";
	}
	/**
	 * 模型表中的新增功能
	 * @param bmc
	 * @param bm
	 * @param orders
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/bmodel_doAdd")
	public String doAdd(String bmc,String bm,String orders,HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		try {
			String uuid = UUIDUtil.getUUID();
			//连接数据库对模型表进行添加
			//完成后新增对应描述表
			String sqlAdd="INSERT INTO "+LinkSql.systemName+" (guid, bmc, bm, orders ) VALUES (?, ?, ?, ? );";
			conn = LinkSql.getConn();
			conn.setAutoCommit(false);
			ps =LinkSql.Execute(conn,sqlAdd,role,LinkSql.systemName);
			ps.setString(1, uuid);
			ps.setString(2, bmc);
			ps.setString(3, bm);
			ps.setString(4, orders);
			int flag =0;
			try {
				flag = ps.executeUpdate();
			} catch (Exception e) {
				conn.rollback();
			}
			if (flag==1) {
				//创建表
				conn.commit();
				String desName =bm+"_des";
				String sql = "CREATE TABLE "+desName+"(id INT PRIMARY KEY AUTO_INCREMENT COMMENT '自动增量，主键',zdm VARCHAR(20) COMMENT '字段名',zdmc VARCHAR(20) COMMENT '字段名称',lisnum INT COMMENT '浏览序号'DEFAULT 0,isedit INT COMMENT '可编辑性',width INT COMMENT '浏览宽度',initVal VARCHAR(100) COMMENT '初始值',`types` VARCHAR(10) COMMENT '字段类型',jsdm VARCHAR(100) COMMENT '关系式(字段关联)',isselect INT COMMENT '查询记录选项'DEFAULT 0,sqlrale varchar(100) COMMENT 'SQL条件',iskeep INT COMMENT '添加时保留原数',isshow INT COMMENT '显示符',fontfamilly VARCHAR(30) COMMENT '字体',fontsize INT COMMENT '字体大小',marleft INT COMMENT '左边距',martop INT COMMENT '上边距',beizhu VARCHAR(100) COMMENT '备注',height INT COMMENT '高度',api VARCHAR(30) COMMENT '接口4',zlong INT COMMENT '字段宽度',omit INT COMMENT '小数位',tips VARCHAR(20) COMMENT '提示信息',isform INT COMMENT '必填项',formtypes VARCHAR(50) COMMENT '表单类型',guid VARCHAR(128) NOT NULL ,FOREIGN KEY(guid) REFERENCES bmodel(guid))ENGINE=INNODB DEFAULT CHARSET=utf8";
				try {
					ps = LinkSql.Execute(conn,sql,role,desName);
					ps.executeUpdate(sql);
					conn.commit();
				} catch (Exception e) {
					conn.rollback();
				}
			}
		}finally{
			ps.close();
			conn.close();
		}
		return "redirect:bmodel_toIndex";
	}

	/**
	 * 模型表中的删除功能
	 * @param bmc
	 * @param bm
	 * @param orders
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/bmodel_del")
	@ResponseBody
	public String bmodel_del(String guid,HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		String flagStr = null;
		try {
			//对模型表中数据进行删除，
			//联动删除自动生成的描述表
			String tn = Bmodel.findBmByGuId(guid);
			conn = LinkSql.getConn();
			conn.setAutoCommit(false);
			String tnDes = tn+"_des";
			//删除描述表
			String sqlDelDes="DROP TABLE "+tnDes+"";
			int flag = 0;
			try {
				ps = LinkSql.Execute(conn,sqlDelDes,role,tnDes);
				flag = ps.executeUpdate();
				conn.commit();
				if (flag==0) {
					rs = conn.getMetaData().getTables(null, null, tn, null);
					if (rs.next()) {
						try {
							String sqlDel="DROP TABLE "+tn+"";
							ps = LinkSql.Execute(conn,sqlDel,role,tn);
							flag = ps.executeUpdate();
							conn.commit();
						} catch (Exception e) {
							conn.rollback();
						}
					}
				}
			} catch (Exception e) {
				conn.rollback();
				flagStr= "error";
				return flagStr;
			}
			if (flag==0) {
				//删除模型表中数据
				String sqlDel="DELETE FROM "+LinkSql.systemName+" WHERE guid = ?";
				ps = LinkSql.Execute(conn,sqlDel,role,LinkSql.systemName);
				ps.setString(1, guid);
				flag = ps.executeUpdate();
				conn.commit();
			}
			if (flag==1) {
				flagStr = "finish";
			}else{
				flagStr= "error";
			}
		} finally{
			ps.close();
			conn.close();
		}
		return flagStr;
	}
}
