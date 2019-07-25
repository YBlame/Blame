package demo.tool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class GetSession {

	public static String getSession(){
		HttpServletRequest request = null;
		HttpSession session = request.getSession();
		String user = session.getAttribute("user").toString();
		return null;
		
		
	}
}
