package demo.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import demo.dao.Bmodel;

@Controller
public class ZhxxController {
	@RequestMapping(value = "findZhxx")
	public void findZhxx(HttpServletRequest request, HttpServletResponse res) throws Exception{
		String guid = "a65611e7bc194941a7050bb14000967d";
		String tn = Bmodel.findBmByGuId(guid);
		String bmc = Bmodel.findBmcBybm(tn);
		request.getRequestDispatcher("zhxx/zhxx_Index.jsp?guid="+ guid+"&bmc="+bmc).forward(request, res);
	}
	@RequestMapping(value = "findMenu")
	public void findMenu(HttpServletRequest request, HttpServletResponse res,String guid) throws Exception{
		request.getRequestDispatcher("zhxx/menu_Index.jsp?zhxxGuid="+ guid).forward(request, res);
	}
	
}
