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
		String guid = "c3608a0db0af46b19536876089eec0dc";
		String tn = Bmodel.findBmByGuId(guid);
		String bmc = Bmodel.findBmcBybm(tn);
		request.getRequestDispatcher("/doc_Index.jsp?guid="+ guid+"&bmc="+bmc).forward(request, res);
	}
}
