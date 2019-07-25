package demo.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GoodsController {

	@ResponseBody
	@RequestMapping(value="saveGoods",method=RequestMethod.POST)
	private String saveImgInfo(String title,String smallTit,String imgUrls){
		System.out.println(title);
		System.out.println(smallTit);
		System.out.println(imgUrls);
		String[] urls = imgUrls.split(",");
		for (int i = 0; i < urls.length; i++) {
			System.out.println("图片名称："+urls[i]);
		}
		return "1";
	}
}
